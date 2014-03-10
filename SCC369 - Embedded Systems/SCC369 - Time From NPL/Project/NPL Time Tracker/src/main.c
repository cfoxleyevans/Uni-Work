#include <16f727.h>

//Tell the compiler to use no optimisation
#opt 0

//Set the config bits that will be used by the processor
#FUSES NOWDT, INTRC         

//Tell the compiler what clock speed we want the processor to use this makes
//sure that delays will be correct
#use delay(clock=16000000)
#include "flex_lcd.c"

/******************************************************************************
* Block: constant definitions											      *
* Purpose: This block defines human readable names to the pins on the board   *
******************************************************************************/
#define ORANGE_LED 			PIN_E0
#define RED_LED 			PIN_E1
#define LED_ON				0
#define LED_OFF				1

#define CARRIER_SENSE 		PIN_B4
#define CARRIER_SENSE_ON	1
#define CARRIER_SENSE_OFF	0

#define PUSH_BUTTON			PIN_B5
#define PUSH_BUTTON_ON  	0
#define PUSH_BUTTON_OFF		1

/******************************************************************************
* Block: global variable definitions									      *
* Purpose: This block defines global vars that will be used in ISR's          *
******************************************************************************/
long	g_carrier_off_ticks 	= 0;
long	g_isr_ticks_this_sec	= 0;
int 	g_seconds_passed		= 0;
int 	g_mins_passed			= 0;
int 	g_carrier_bit_a			= 0;
int 	g_carrier_bit_b			= 0;

int 	g_year_bits 			= 0;
int 	g_hour_bits 			= 0;

int 	g_year 					= 0;
int     g_hour 					= 0;

/******************************************************************************
* Function: setup_hardware													  *
* Purpose: This is used to configure all of the hardware on the board         *
* Accepts: nothing  														  *
* Reuturns: nothing 														  *
******************************************************************************/
void setup_hardware(void) {
    //make sure all ports are using digital I/O
    setup_adc_ports(NO_ANALOGS);

    //16000000/(4*16*250*10) = 100 so timer fires every 10ms
    setup_timer_2(T2_DIV_BY_16, 250, 10); 
    set_timer2(0);
    enable_interrupts(global);

  	lcd_init();  // Always call this first. 
}


/******************************************************************************
* Function: store_bits														  *
* Purpose: This is used store the bits that have been collected		          *
* Accepts: nothing  														  *
* Reuturns: nothing 														  *
******************************************************************************/
void store_bits(void) {
	switch(g_seconds_passed) {
		case 17:
			if(g_carrier_bit_a == 0)
				g_year += 80;
			break;
		
		case 18:
			if(g_carrier_bit_a == 0)
				 g_year += 40;
			break;
		
		case 19:
			if(g_carrier_bit_a == 0)
				 g_year += 20;
			break;
		
		case 20:
			if(g_carrier_bit_a == 0)
				 g_year += 10;
			break;
		
		case 21:
			if(g_carrier_bit_a == 0)
				 g_year += 8;
			break;
		
		case 22:
			if(g_carrier_bit_a == 0)
				 g_year += 4;
			break;
		
		case 23:
			if(g_carrier_bit_a == 0)
				 g_year += 2;
			break;
		
		case 24:
			if(g_carrier_bit_a == 0)
				 g_year += 1;
			break;

		
		case 39:
			if(g_carrier_bit_a == 0)
				 g_hour += 20;
		break;
		
		case 40:
			if(g_carrier_bit_a == 0)
				 g_hour += 10;
			break;

		case 41:
			if(g_carrier_bit_a == 0)
				g_hour += 8;
			break;

		case 42:
			if(g_carrier_bit_a == 0)
				g_hour += 4;
			break;

		case 43:
			if(g_carrier_bit_a == 0)
				g_hour += 2;
			break;

		case 44:
			if(g_carrier_bit_a == 0)
				g_hour += 1;
			break;

		default:
			break;
	}
}

/******************************************************************************
* Function: timer_2_isr													 	  *
* Purpose: This is used to check the carrier sense at regular intervals       *
* Accepts: nothing  														  *
* Reuturns: nothing 														  *
******************************************************************************/
#int_timer2
void timer_2_isr(void) {
	if(input(CARRIER_SENSE) == CARRIER_SENSE_OFF) {
		//inc the time off tick count
		g_carrier_off_ticks++;
		
		//if we have the min marker reset the seconds and inc the mins
		if(g_carrier_off_ticks == 50) {
			g_mins_passed++;
			g_seconds_passed = 0;

			g_year = 0;
			g_hour = 0;
		}
		
		//if we have the second marker inc the number of seconds 
		if(g_carrier_off_ticks == 10) {			
			g_seconds_passed++;
			g_isr_ticks_this_sec = 10;
		}

		if(g_isr_ticks_this_sec == 15) {
			g_carrier_bit_a = input(CARRIER_SENSE);
			store_bits();
		}
		if(g_isr_ticks_this_sec == 25) {
			g_carrier_bit_b = input(CARRIER_SENSE);
		}
	}
	else {
		//found the carrier so no need to count how long its off
		g_carrier_off_ticks = 0;
	}
	g_isr_ticks_this_sec++;	

}

/******************************************************************************
* Function: main           													  *
* Purpose: This is the entry point for program execution                      *
* Accepts: nothing  														  *
* Reuturns: nothing 														  *
******************************************************************************/
void main(void) {
	//Call routine to be aure that any final config has been done before 
	//we start the main program code
    setup_hardware();

	delay_ms(5000);

	enable_interrupts(int_timer2);

	while(1) {
  		if(input(CARRIER_SENSE) == CARRIER_SENSE_ON) {
    		output_bit(RED_LED, LED_ON);
    		output_bit(ORANGE_LED, LED_OFF);
    	}
    	else {
    		output_bit(ORANGE_LED, LED_ON);
    		output_bit(RED_LED, LED_OFF);
    	}
    	lcd_update(g_year, g_hour, g_mins_passed, g_seconds_passed);
    	delay_ms(100);
    }	
}
