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
long	g_carrier_off_ticks = 0;
int 	g_mins_passed		= 0;



/******************************************************************************
* Function: setup_hardware													  *
* Purpose: This is used to configure all of the hardware on the board         *
* Accepts: nothing  														  *
* Reuturns: nothing 														  *
******************************************************************************/
void setup_hardware(void) {
    //make sure all ports are using digital I/O
    setup_adc_ports(NO_ANALOGS);

    //16000000/(4*16*250*10) = 1000 so timer fires every 1ms
    setup_timer_2(T2_DIV_BY_16, 250, 1); 
    set_timer2(0);
    enable_interrupts(global);

  	lcd_init();  // Always call this first. 
}

#int_timer2
void count_ticks(void) {
	if(input(CARRIER_SENSE) == CARRIER_SENSE_OFF) {
		g_carrier_off_ticks++;
	}
	else {
		g_carrier_off_ticks = 0;
	}
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

    	if(g_carrier_off_ticks == 500) {
    		g_mins_passed += 1;
    		lcd_clear();
    		lcd_put_int(g_mins_passed);
    	}
    }	
}
