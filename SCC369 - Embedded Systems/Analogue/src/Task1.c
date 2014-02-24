#include <18f2420.h>
#include "18f2420_registers.h"

#device ADC=10 //causes read_adc() to produce 10 bit results
#fuses HS,NOWDT,NOPROTECT,NOLVP,NOPUT
#fuses NOBROWNOUT //no brown-out detection
#fuses NOFCMEN //no fail-safe clock monitor
#fuses NOXINST //disable extended instruction set
#fuses NOIESO //disable internal external switchover mode
#fuses NOPBADEN //turn off 0-4 a/d on port b (port B is all digital)

//turn off all optimisation
#opt 0

//set the clock speed
#use delay(clock=20000000)

//set the spi ports
#use rs232(baud=115200, xmit=PIN_C6, rcv=PIN_C7)

//defines for the pins
#define LOW_ADDRESS_LATCH	PIN_A2
#define	HIGH_ADDRESS_LATCH	PIN_A3
#define MSB_ADDRESS			PIN_C0
#define	SRAM_OUTPUT_ENABLE	PIN_A4
#define	SRAM_WRITE_ENABLE	PIN_A5
#define	DAC_FRAME_SYNC		PIN_C2
#define	RED_LED				PIN_C4
#define MICROPHONE			PIN_A0
#define	LINE_IN				PIN_A1
#define	BUTTON				PIN_C1	

#define BUTTON_PRESSED      0
#define BUTTON_RELEASED 	1

//global vars
int dac_value = 0;
int dac_state = 0;


//this function sets up the hardware on the board
void set_up_hardware() {
	
	setup_adc(ADC_CLOCK_INTERNAL);
	setup_adc_ports(AN0_TO_AN1);

	setup_spi(SPI_MASTER | SPI_L_TO_H | SPI_CLK_DIV_4 | SPI_SS_DISABLED);

	#asm 
	//configure SPI clock rate to div4 (not div16) [CCSC=EVIL!]
	MOVLW 0x30	//clock rate div4, idle state for clock is high level
	MOVWF 0xFC6 //see section 17.3 of 18F2423 manual, and DAC datasheet
	MOVLW 0x40	//transmit occurs on transition from active to idle
	MOVWF 0xFC7
	#endasm
}

//this function is used the set the momeory to a known state
void init_memory() {
	//declare the value to write into the latches
	int value = 0;
	
	//SRAM output enable and write enable
	output_bit(SRAM_OUTPUT_ENABLE, 1);
	output_bit(SRAM_WRITE_ENABLE, 1);
	
	//latch lines
	output_bit(LOW_ADDRESS_LATCH, 0);
	output_bit(HIGH_ADDRESS_LATCH, 0);
	
	//latch 0x00 into the address latches
	output_b(value);

	//write the value into the lower latch
	output_bit(LOW_ADDRESS_LATCH, 1);
	output_bit(LOW_ADDRESS_LATCH, 0);

	//write the bit into the higher latch
	output_bit(HIGH_ADDRESS_LATCH, 1);
	output_bit(HIGH_ADDRESS_LATCH, 0);	
}

//this function is used to read a value from a given memory location
int read_from_memory(int msb, int higher_address, int lower_address) {
	//declare var to store the read value
	int value = 0;
	
	//latch the lower address
	output_b(lower_address);
	output_bit(LOW_ADDRESS_LATCH, 1);
	output_bit(LOW_ADDRESS_LATCH, 0);

	//latch the high address
	output_b(higher_address);
	output_bit(HIGH_ADDRESS_LATCH, 1);
	output_bit(HIGH_ADDRESS_LATCH, 0);

	//set the msb
	output_bit(MSB_ADDRESS, msb);

	//read the value 
	output_bit(SRAM_OUTPUT_ENABLE, 0);
	value = input_b();
	output_bit(SRAM_OUTPUT_ENABLE, 1);

	return value;
}

//this function is used to write a value to a given memory location
void write_to_memory(int msb, int higher_address, int lower_address, int value) {
	//latch the lower address
	output_b(lower_address);
	output_bit(LOW_ADDRESS_LATCH, 1);
	output_bit(LOW_ADDRESS_LATCH, 0);

	//latch the high address
	output_b(higher_address);
	output_bit(HIGH_ADDRESS_LATCH, 1);
	output_bit(HIGH_ADDRESS_LATCH, 0);

	//set the msb
	output_bit(MSB_ADDRESS, msb);

	output_b(value);
	output_bit(SRAM_WRITE_ENABLE, 0);
	output_bit(SRAM_WRITE_ENABLE, 1);
}

//this function is used to make sure that the memory is working as expected
void mem_check() {
	int i,j,k,value;
	//write 255 into even slots and 255 into odd slots
	printf("INFO: WRITING TO MEMORY\n\r");
	for(i = 0; i < 2; i++) {
		for(j = 0; j < 255; j++) {
			for(k = 0; k < 255; k++) {
				if(k % 2 == 0) {
					write_to_memory(i,j,k,k);
					//printf("INFO: WRITTEN 255 TO (%u,%u,%u)\n\r", i, j, k);
				}	
				else {
					write_to_memory(i,j,k,255 - k);
					//printf("INFO: WRITTEN 254 TO (%u,%u,%u)\n\r", i, j, k);
				}	
			}
		}
	}
	
	//read back the memory chek to make sure ok
	printf("INFO: READING FROM MEMORY\n\r");
	for(i = 0; i < 2; i++) {
		for(j = 0; j < 255; j++) {
			for(k = 0; k < 255; k++) {
				value = read_from_memory(i, j, k);
				if(k % 2 == 0 && value != k) {
					printf("ERROR: BAD READ FROM ((%u,%u,%u)\n\r", i, j, k);
				}
				else if (k % 2 == 1 && value != 255 - k) {
					printf("ERROR: BAD READ FROM ((%u,%u,%u)\n\r", i, j, k);
				}
			}
		}
	}
	printf("INFO: MEMCHECK FINISHED\n\r");
}

//this function is used to write a value to the dac
void convert_d_to_a(int data) {
	int high_byte, low_byte = 0;

	high_byte = (data >> 4) | 0x40;
	low_byte =  (data << 4) | 0x00;
	
	output_bit(DAC_FRAME_SYNC, 0);
	spi_write(high_byte);
	spi_write(low_byte);
	output_bit(DAC_FRAME_SYNC, 1);
}


unsigned long write_triangle_wave() {
	//decalre the loop counter
	int i = 0;

	//loop to write the up part of the wave
	for(i= 0; i < 255; i++) {
		convert_d_to_a(i);
		printf("%lu\n\r", read_adc());
	}
		
	//loop to write the down part of the wave		
	for(i = 255; i > 0; i--) {
		convert_d_to_a(i);
		printf("%lu\n\r", read_adc());
	}
}

#INT_TIMER2
void write_to_dac() {
	//if going up
	if (!dac_state) {
		//write the value and test if at the upper limit
		convert_d_to_a(dac_value);
		if (++dac_value == 255) {
			//if at the upper limit then flip the state
			dac_state = dac_state == 0 ? 1 : 0;
		}
		printf("%u\n\r", (int)(read_adc() >> 2));
	} 
	else if (dac_state) {
		convert_d_to_a(dac_value);
		if (--dac_value == 0) {
			dac_state = dac_state == 0 ? 1 : 0;
	    }
	    printf("%u\n\r", (int)(read_adc() >> 2));
	}
}

void main(void) {
	//var definitions
	int button_state = 0;
	int play_wave = 0;

	//set up the hardware
	set_up_hardware();
	init_memory();
	set_adc_channel(LINE_IN);

	//set up the interupts
	setup_timer_2(T2_DIV_BY_4, 250, 2); //2.5k interupts per second
   	set_timer2(0);
   	enable_interrupts(GLOBAL);

  	//delay to unplug the programmer
	delay_ms(5000);

	//check the moemory
	mem_check();

	//main loop
	while(1) {
		if(input(BUTTON) == BUTTON_PRESSED && button_state == 0) {
			button_state = 1;
		}
		if(input(BUTTON) == BUTTON_RELEASED && button_state == 1) {
			//reset the button state and flip the play wave flag 
			button_state = 0;
			play_wave = play_wave == 0 ? 1 : 0;
			if(play_wave) {
				enable_interrupts(INT_TIMER2);
   				
			}
			else {
				disable_interrupts(INT_TIMER2);
			}
		}
	}
}