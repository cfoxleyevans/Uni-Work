#include "p16F737.inc"

; CONFIG1
 __CONFIG _CONFIG1, _FOSC_INTOSCIO & _WDTE_OFF & _PWRTE_OFF & _MCLRE_ON & _BOREN_ON & _BORV_20 & _CCP2MX_RC1 & _CP_OFF

; CONFIG2
 __CONFIG _CONFIG2, _FCMEN_ON & _IESO_ON & _BORSEN_ON
 
 VAR1	EQU 0x20

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;INIT:										              ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
INIT:
    CLRF    ADCON0          ;Disable the A/D module

   	BANKSEL	TRISA			;Select bank1
   
    BCF     OSCCON, IRCF0   ;Select 4MHz oscillator 
    BSF     OSCCON, IRCF1	;The 16F737 powers on in low
    BSF     OSCCON, IRCF2	;power mode at 125kHz.
    						;4MHz is more suited to useful
    						;communications speeds.
    	
	GOTO 	WAIT_FOR_STABLE_CLOCK

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;WAIT_FOR_STABLE_CLOCK:                                   ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
WAIT_FOR_STABLE_CLOCK:
    BTFSS   OSCCON, IOFS    ;It takes a while for the chip
    						;to settle to the new clock speed
    GOTO    WAIT_FOR_STABLE_CLOCK

    MOVLW   0x0F            ;Turn of analogue inputs.
    MOVWF   ADCON1

    MOVLW   0xFF            ;Turn of the comparators.
    MOVWF   CMCON
    
	GOTO SET_UP_COMS

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;SET_UP_COMS:                  			                  ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
SET_UP_COMS:
	BANKSEL	TRISA			;select bank1
		
	BSF 	TRISC, 0x06		;set serial to input
	BSF		TRISC, 0x07
	
	BSF		TXSTA, TXEN		;turn on the transmission enable bit 
	
	BSF		TXSTA, BRGH		;set the baud rate to high
	MOVLW	d'25'			;set the baud rate
	MOVWF	SPBRG

	BANKSEL	PORTA			;select to bank0
    
	BSF		RCSTA, SPEN		;configure pins for USART
	BSF		RCSTA, CREN
	
	GOTO SET_UP_HARDWARE

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;SET_UP_HARDWARE:	                                      ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
SET_UP_HARDWARE:
	BANKSEL	TRISA			;move to bank1
    
	CLRF	TRISA           ;set the digits for output
	BSF		TRISB, 0x02     ;set the switch and jumper for input
	BSF		TRISB, 0x03         
	BSF		TRISB, 0x04
    
	BANKSEL PORTA			;move to bank0

	MOVLW	b'10001000'		;write default value into the display
	MOVWF	PORTA

    GOTO    MAIN            ;continue to main software

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;MAIN:	                                      			  ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
MAIN:
	BTFSS 	PORTB, 0x03
	CALL	DISPLAY_ONE
	
	BTFSS	PORTB, 0x04
	CALL	DISPLAY_TWO
	
	GOTO	MAIN

DISPLAY_ONE:
	MOVLW	b'10111110'
	MOVWF	PORTA
	RETURN


DISPLAY_TWO:
	MOVLW	b'11000100'
	MOVWF	PORTA
	RETURN
	
	END