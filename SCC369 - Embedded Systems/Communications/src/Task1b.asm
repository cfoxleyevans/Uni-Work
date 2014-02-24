#include "p16F737.inc"

; CONFIG1
 __CONFIG _CONFIG1, _FOSC_INTOSCIO & _WDTE_OFF & _PWRTE_OFF & _MCLRE_ON & _BOREN_ON & _BORV_20 & _CCP2MX_RC1 & _CP_OFF

; CONFIG2
 __CONFIG _CONFIG2, _FCMEN_ON & _IESO_ON & _BORSEN_ON
 
;registers to store bit patterns for numbers
NUMBER0	EQU 0x20
NUMBER1	EQU 0x21
NUMBER2	EQU 0x22
NUMBER3	EQU 0x23
NUMBER4	EQU 0x24
NUMBER5	EQU 0x25
NUMBER6	EQU 0x26
NUMBER7	EQU 0x27
NUMBER8	EQU 0x28
NUMBER9	EQU 0x29
 
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
		
	CALL BUILD_NUMBER_MAP
	
    GOTO    MAIN            ;continue to main software

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;BUILD_NUMBER_MAP: Builds the map of the bit patterns	  ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
BUILD_NUMBER_MAP:
	MOVLW   b'10001000'   	;move the bit pattern for 0 into W
    MOVWF   NUMBER0			;move this into the base address for the nubmers

	MOVLW   b'10111110'
    MOVWF   NUMBER1

	MOVLW    b'11000100'
    MOVWF   NUMBER2

	MOVLW    b'10010100'
    MOVWF   NUMBER3

	MOVLW    b'10110010'
    MOVWF   NUMBER4

	MOVLW   b'10010001'
    MOVWF   NUMBER5

	MOVLW   b'10000011'
    MOVWF   NUMBER6

	MOVLW    b'10110100'
    MOVWF   NUMBER7

	MOVLW   b'10000000'
    MOVWF   NUMBER8

	MOVLW   b'10110000'   	;move the bit parrern for 9 into w
    MOVWF   NUMBER9			;move this into the top address for the numbers

	RETURN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;MAIN: Loops checking for bytes on the wire				  ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
MAIN:
	BTFSC 	PIR1, RCIF		;check for data 
	CALL 	READ			;read the data and display the byte
	
	GOTO	MAIN			;do over

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;READ: Reads a byte off the wire, checks to see if it	  ;
;	   is a digit if it is echo back to the monitor		  ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
READ:
	MOVF 	RCREG, W		;move the byte to the working register
	MOVWF	ARG1			;store the byte for later

	CALL	WRITE			;echo back the data to the monitor
	
	CALL	IS_DIGIT		;check to see if the byte is a digit
	
	BTFSC	RET1, 0x00		;if the byte is a digit this will be 1
	CALL	DISPLAY			;found a 1 so display the digit
	
	RETURN   

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;WRITE: Writes the byte in VAR1 onto the wire  			  ;
;Accepts: The byte to write in the ARG1 register          ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
WRITE:			
	MOVF	ARG1, W			;move the byte into the working register
	
	BTFSS 	TXSTA, TRMT		;make sure the wire is clear
	MOVWF	TXREG			;write the byte onto the wire
	
	RETURN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;DISPLAY: Displays a digit on the display	              ;
;Accepts: The byte to display in ARG1                     ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
DISPLAY:
	MOVLW	NUMBER0			;set the fsr to point to 0
	MOVWF	FSR
	
	MOVLW	d'48'			;move 48 into the working register
	SUBWF	ARG1, W			;subtract this from the byte to see what number it is		
	
	ADDWF	FSR, F			;add to value to fsr to index the correct digit
	
	MOVF	INDF, W			;move the value into porta
	MOVWF	PORTA 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;IS_DIGIT: Checks to see if the value in ARG1 is an ASCII digit  ;
;Accepts: A byte in the register ARG1                            ;
;Returns: The value 1 in RET1 if the input is a digit, 0 if not  ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
IS_DIGIT:
	MOVFW	ARG1			;move the argument to the working register
	SUBLW	d'57'			;subtract 57 from it
	BTFSS	STATUS, C		;is there anything left
	GOTO	NOT_DIGIT		;yes so its greater than 57 and not a digit
	GOTO	LESS_THAN_57	;no so less than 57 check if > 48

LESS_THAN_57:
	MOVFW	ARG1			;move the argument to the working register
	SUBLW	d'47'			;subtract 48 from it
	BTFSS	STATUS, C		;is there anything left
	GOTO	DIGIT			;yes so its a digit
	GOTO	NOT_DIGIT 		;no so not a digit
	
DIGIT:
	BSF		RET1, 0x00		;set the return value to 1
	RETURN
	
NOT_DIGIT:
	BCF		RET1, 0x00		;set the return value to 0
	RETURN
	
	END