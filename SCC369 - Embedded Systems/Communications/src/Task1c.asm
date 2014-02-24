#include "p16F737.inc"

; CONFIG1
 __CONFIG _CONFIG1, _FOSC_INTOSCIO & _WDTE_OFF & _PWRTE_OFF & _MCLRE_ON & _BOREN_ON & _BORV_20 & _CCP2MX_RC1 & _CP_OFF

; CONFIG2
 __CONFIG _CONFIG2, _FCMEN_ON & _IESO_ON & _BORSEN_ON
 
;registers to store bit patterns for numbers
GREEN_STATUS	EQU	0x20
RED_STATUS		EQU	0x21
 
;general purpose registers
VAR1	EQU 0x30
VAR2	EQU	0x31
 
;function argument and return registers
ARG1	EQU	0x40
RET1	EQU	0x41
 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;INIT: Reclock the pic to a more suitable state	          ;
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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;WAIT_FOR_STABLE_CLOCK: Make sure that the clock is stable ;
;Accepts: Nothing                                          ;
;Returns: Nothing                                          ;
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
;SET_UP_COMS: Set up the comms hardware for USART         ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
SET_UP_COMS:
	BANKSEL	TRISA			;select bank1
		
	BSF 	TRISC, 0x06		;set serial pins to input
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
;SET_UP_HARDWARE: Sets up the lights and switches         ;
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
	
	MOVLW	0x00			;clear the status of both buttons
	MOVWF	GREEN_STATUS
	MOVWF	RED_STATUS
		
    GOTO    MAIN            ;continue to main software


TRANSMIT:
	BANKSEL	TXSTA
	
	BTFSS 	TXSTA, TRMT		;make sure the wire is clear
	GOTO	TRANSMIT
	
	
	BANKSEL	TXREG
	MOVWF	TXREG			;write the byte onto the wire
	
	RETURN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;MAIN: Loops checking for bytes on the wire				  ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
MAIN:
	BTFSS 	PORTB, 0x03
	CALL	DISPLAY_GREEN_ON
	
	BTFSC	PORTB, 0x03
	CALL	DISPLAY_GREEN_OFF

	BTFSS	PORTB, 0x04
	CALL	DISPLAY_RED_ON
	
	BTFSC	PORTB, 0x04
	CALL	DISPLAY_RED_OFF
	
	GOTO	MAIN			;do over


DISPLAY_GREEN_ON:
	BTFSC	GREEN_STATUS, 0x00	;test the status
	RETURN

	MOVLW	0x01			;set the status to on
	MOVWF	GREEN_STATUS
	
	MOVLW	d'71'			;write a char into G
	CALL 	TRANSMIT
	
	MOVLW	d'114'			;write a char into r
	CALL 	TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	TRANSMIT
	
	
	RETURN


DISPLAY_GREEN_OFF:
	BTFSS	GREEN_STATUS, 0x00
	RETURN
	
	MOVLW	0x00			;set the status to off
	MOVWF	GREEN_STATUS
	
	MOVLW	d'71'			;write a char into G
	CALL 	TRANSMIT
	
	MOVLW	d'114'			;write a char into r
	CALL 	TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	TRANSMIT
	
	MOVLW	d'102'			;write a char into n
	CALL 	TRANSMIT
	
	MOVLW	d'102'			;write a char into n
	CALL 	TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	TRANSMIT
	
	
	RETURN

DISPLAY_RED_ON:
	BTFSC	RED_STATUS, 0x00
	RETURN

	MOVLW	0x01			;set the status to on
	MOVWF	RED_STATUS
	
	MOVLW	0x00			;set the status to off
	MOVWF	GREEN_STATUS
	
	MOVLW	d'82'			;write a char into R
	CALL 	TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	TRANSMIT
	
	MOVLW	d'100'			;write a char into d
	CALL 	TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	TRANSMIT
	RETURN
	
	
DISPLAY_RED_OFF:
	BTFSS	RED_STATUS, 0x00
	RETURN
	
	MOVLW	0x00			;set the status to off
	MOVWF	RED_STATUS
	
	MOVLW	0x00			;set the status to off
	MOVWF	GREEN_STATUS
	
	MOVLW	d'82'			;write a char into R
	CALL 	TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	TRANSMIT
	
	MOVLW	d'100'			;write a char into d
	CALL 	TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	TRANSMIT
	
	MOVLW	d'102'			;write a char into f
	CALL 	TRANSMIT
	
	MOVLW	d'102'			;write a char into f
	CALL 	TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	TRANSMIT
	RETURN	
	
	END


	