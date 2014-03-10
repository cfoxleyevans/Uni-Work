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
* Block: constant definitions                                                 *
* Purpose: This block defines human readable names to the pins on the board   *
******************************************************************************/
#define ORANGE_LED          PIN_E0
#define RED_LED             PIN_E1
#define LED_ON              0
#define LED_OFF             1

#define CARRIER_SENSE       PIN_B4
#define CARRIER_SENSE_ON    1
#define CARRIER_SENSE_OFF   0


#define PUSH_BUTTON         PIN_B5
#define PUSH_BUTTON_ON      0
#define PUSH_BUTTON_OFF     1

/******************************************************************************
* Block: global variable definitions                                          *
* Purpose: This block defines global vars that will be used in ISR's          *
******************************************************************************/
//this is the number of ticks that timer2 has recorded since board start up
unsigned long     ticks                       = 0;

//this is the tick value that the signal went off at
unsigned long     signal_off_start            = 0;

//this is the tick value that the signal came on at
unsigned long     signal_on_start             = 0;

//this is the tick value that the previous signal came on at
unsigned long     prev_signal_on_start        = 0;

//this is the number of seconds that have passed this min
int               seconds_passed              = 0;

int               year                        = 0;
int               month                       = 0;
int               day                         = 0;
int               week_day                    = 0;
int               hour                        = 0;
int               mins                        = 0;

int     a_bits[59];

/******************************************************************************
* Function: setup_hardware                                                    *
* Purpose: This is used to configure all of the hardware on the board         *
* Accepts: nothing                                                            *
* Reuturns: nothing                                                           *
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
* Function: store_bits                                                        *
* Purpose: This is used store the bits that have been collected               *
* Accepts: nothing                                                            *
* Reuturns: nothing                                                           *
******************************************************************************/
void process_bits(void) {
    int i;

    for(i = 1; i < 59; i++) {
        switch(i) {
            //cases for the year
            case 17:
                if(a_bits[i])
                    year += 80;
            break;
        
            case 18:
                if(a_bits[i])
                    year += 40;
                break;
            
            case 19:
                if(a_bits[i])
                    year += 20;
                break;
            
            case 20:
                if(a_bits[i])
                    year += 10;
                break;
            
            case 21:
                if(a_bits[i])
                    year += 8;
                break;
            
            case 22:
                if(a_bits[i])
                    year += 4;
                break;
            
            case 23:
                if(a_bits[i])
                    year += 2;
                break;
            
            case 24:
                if(a_bits[i])
                    year += 1;
                break;

            //cases for the month
            case 25:
                if(a_bits[i])
                    month += 10;
                break;

            case 26:
                if(a_bits[i])
                    month += 8;
                break;

            case 27:
                if(a_bits[i])
                    month += 4;
                break;

            case 28:
                if(a_bits[i])
                    month += 2;
                break;

            case 29:
                if(a_bits[i])
                    month += 1;
                break;

            //cases for the day
            case 30:
                if(a_bits[i])
                    day += 20;
                break;

            case 31:
                if(a_bits[i])
                    day += 10;
                break;

            case 32:
                if(a_bits[i])
                    day += 8;
                break;

            case 33:
                if(a_bits[i])
                    day += 4;
                break;

            case 34:
                if(a_bits[i])
                    day += 2;
                break;

            case 35:
                if(a_bits[i])
                    day += 1;
                break;

            //cases for the day name
            case 36:
                if(a_bits[i])
                    week_day += 4;
                break;

            case 37:
                if(a_bits[i])
                    week_day += 2;
                break;

            case 38:
                if(a_bits[i])
                    week_day += 1;
                break;

            //cases for the hour
            case 39:
                if(a_bits[i])
                    hour += 20;
                 break;

            case 40:
                if(a_bits[i])
                    hour += 10;
                break;

            case 41:
                if(a_bits[i])
                    hour += 8;
                break;

            case 42:
                if(a_bits[i])
                    hour += 4;
                break;

            case 43:
                if(a_bits[i])
                    hour += 2;
                break;

            case 44:
                if(a_bits[i])
                    hour += 1;
                break;

            //cases for the min
            case 45:
                if(a_bits[i])
                    mins += 40;
                break;

            case 46:
                if(a_bits[i])
                    mins += 20;
                break;

            case 47:
                if(a_bits[i])
                    mins += 10;
                break;

            case 48:
                if(a_bits[i])
                    mins += 8;
                break;

            case 49:
                if(a_bits[i])
                    mins += 4;
                break;


            case 50:
                if(a_bits[i])
                    mins += 2;
                break;

            case 51:
                if(a_bits[i])
                    mins += 1;
                break;
        }    
    }
}

/******************************************************************************
* Function: timer_2_isr                                                       *
* Purpose: This is used to increment the tick count at regular intervals      *
* Accepts: nothing                                                            *
* Reuturns: nothing                                                           *
******************************************************************************/
#int_timer2
void timer_2_isr(void) {
    //increase the ticks by one 
    ticks++;
}

/******************************************************************************
* Function: port_b_isr                                                        *
* Purpose: This is used process the change in the carrier signal              *
* Accepts: nothing                                                            *
* Reuturns: nothing                                                           *
******************************************************************************/
#int_rb
void port_b_isr(void) {
    //local vars to hold the length of time the signal has been off
    //and the length of time the previous signal was on
    unsigned long signal_off_length = 0;
    unsigned long prev_signal_on_length = 0;

    //set the led to reflect the new state of the carrier
    output_bit(ORANGE_LED, input(CARRIER_SENSE));

    //the carrier has gone off - this means that we need to start timing
    //so that when it comes back on we can calculate the amount of time that it was off
    //and thus the bit pattern it represents
    if(input(CARRIER_SENSE) == CARRIER_SENSE_OFF) {
        //set the new current off start
        signal_off_start = ticks;
        //return from isr no more work to do
        return;
    }
    
    /*reaching here means that the carrier has come back on so we need to process this*/

    //set the previous on start and the new current on start
    prev_signal_on_start = signal_on_start;
    signal_on_start = ticks;

    //determine the amount off time that the signal was off by taking the on time away
    //from the off time 
    signal_off_length = signal_on_start - signal_off_start;

    //determine how long the previous signal was on for by taking the previous on time away
    //from the current start time
    prev_signal_on_length =  signal_off_start - prev_signal_on_start;

    //if the signal was off for between 70ms and 130ms and was previously on for at least
    //150ms then we have found 00
    if(signal_off_length >= 7 && signal_off_length <= 13 &&prev_signal_on_length > 15) {
        a_bits[seconds_passed] = 0;
        seconds_passed++;
    }

    //if the signal was off for between 170ms and 230ms and was previously on for at least
    //150ms then we have found 10
    if(signal_off_length >= 17 && signal_off_length <= 23 &&prev_signal_on_length > 15) {
        a_bits[seconds_passed] = 1;
        seconds_passed++;
    }

    //more code needs to be here to detect the b bit

    //if the signal was off more than 470ms then we have found the min marker
    //reset all of the data and process what we have stored
    if(signal_off_length >= 47) {
        seconds_passed = 1;
        year = 0;
        month = 0;
        day = 0;
        week_day = 0;
        hour = 0;
        mins = 0;
        process_bits();     
    }
}

/******************************************************************************
* Function: main                                                              *
* Purpose: This is the entry point for program execution                      *
* Accepts: nothing                                                            *
* Reuturns: nothing                                                           *
******************************************************************************/
void main(void) {
    //Call routine to be sure that any final config has been done before 
    //we start the main program code
    setup_hardware();

    //not needed in production - ICD interfers with the signal, this 
    //gives time to unplug
    delay_ms(5000);

    //enable the interupts on timer 2 and on pin4 of port b
    enable_interrupts(int_timer2);
    enable_interrupts(int_rb4);

    //update the display every 750ms
    while(1) {
        lcd_display_time(day, month, year, week_day, hour, mins, seconds_passed);
        delay_ms(750);
    }   
}