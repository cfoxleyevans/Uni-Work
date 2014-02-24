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
 
;flags for various states
SLAVE_FLAG	EQU	0x50

MASTER_GREEN_STATUS	EQU	0x51
MASTER_RED_STATUS	EQU	0x52

SLAVE_GREEN_STATUS	EQU	0x54
SLAVE_RED_STATUS	EQU	0x54



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
	MOVWF	MASTER_GREEN_STATUS
	MOVWF	MASTER_RED_STATUS
	MOVWF	SLAVE_GREEN_STATUS
	MOVWF	SLAVE_RED_STATUS
	
	
	BTFSC	PORTB, 0x02		;check if this is the slave or master borad
	GOTO	SPI_SLAVE
	GOTO	SPI_MASTER
	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;SERIAL_SERIAL_TRANSMIT: Writes the byte in W to the serial      ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;	
SERIAL_TRANSMIT:
	BANKSEL	TXSTA
	
	BTFSS 	TXSTA, TRMT		;make sure the wire is clear
	GOTO	SERIAL_TRANSMIT
	
	
	BANKSEL	TXREG
	MOVWF	TXREG			;write the byte onto the wire
	
	RETURN
	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;SPI_MASTER: Configures SPI for master					  ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
SPI_MASTER:
	BANKSEL TRISA               
    BSF     SSPSTAT, SMP 		;input sampled at end of data timing       
    BSF     SSPSTAT, CKE        ;transmission on active-idle

    BCF     TRISC, 0x03   		;set the clock for output     
    BCF     TRISC, 0x05         ;set the data out for output
	BSF		TRISC, 0x04			;set the data in for input

    BANKSEL PORTA               
    CLRF    SSPCON     			;enable the serial port     	    
    BSF     SSPCON, SSPEN

	GOTO MASTER_MAIN			;goto the main function

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;SPI_SLAVE: Configures SPI for slave				      ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
SPI_SLAVE:
	BANKSEL TRISA
	BCF     SSPSTAT, SMP		;must be clear in slave mode	    
    BSF     SSPSTAT, CKE 		;transmission on active-idle   

    BSF     TRISC, 0x03  		;set the clock for input  		 
    BCF     TRISC, 0x05     	;set the data out for output
	BSF		TRISC, 0x04			;set the data in for input

    BANKSEL PORTA           	;enable the serial port	
    MOVLW   b'01100101'     
    MOVWF   SSPCON
	
	GOTO SLAVE_MAIN				;goto the main function
	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;MASTER_WRITE_BYTE: Write a byte to tx                    ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
MASTER_WRITE_BYTE:
	MOVWF	SSPBUF 					;move the W into the TX reg
	BANKSEL SSPSTAT 	
TRANSFER_DONE:
 	BTFSS	SSPSTAT,BF 				;check if the buffer is full
	GOTO TRANSFER_DONE				;if not then check again
	BANKSEL SSPBUF 	
	RETURN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;MASTER_MAIN: The main code loop for the master			  ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
MASTER_MAIN:
	MOVLW	0x00					;write a byte to the slave
	CALL	MASTER_WRITE_BYTE		;return when the transfer is complete
	
	MOVFW	SSPBUF
	MOVWF	VAR1
	
	BTFSS 	PORTB, 0x03				;test switches 
	CALL	DISPLAY_GREEN_ON
	
	BTFSC	PORTB, 0x03
	CALL	DISPLAY_GREEN_OFF

	BTFSS	PORTB, 0x04
	CALL	DISPLAY_RED_ON
	
	BTFSC	PORTB, 0x04
	CALL	DISPLAY_RED_OFF
	
	BTFSS 	VAR1, 0x03				;test the values from the slave 
	CALL	DISPLAY_SLAVE_GREEN_ON
	
	BTFSC	VAR1, 0x03
	CALL	DISPLAY_SLAVE_GREEN_OFF

	BTFSS	VAR1, 0x04
	CALL	DISPLAY_SLAVE_RED_ON
	
	BTFSC	VAR1, 0x04
	CALL	DISPLAY_SLAVE_RED_OFF
	
	GOTO MASTER_MAIN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;SLAVE_MAIN: The main code loop for the slave			  ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
SLAVE_MAIN:	
	MOVFW	PORTB					;move portb into the buffer
	MOVWF	SSPBUF					;move the byte to tx ready for transfer
	
	GOTO SLAVE_MAIN					;do over	
		
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;DISPLAY_GREEN_ON: Called when the green butten is pressed;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;	
DISPLAY_GREEN_ON:
	BTFSC	MASTER_GREEN_STATUS, 0x00	;test the status
	RETURN

	MOVLW	0x01			;set the status to on
	MOVWF	MASTER_GREEN_STATUS
	
	MOVLW	d'77'			;write a char into M
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'97'			;write a char into a
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'115'			;write a char into s
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'116'			;write a char into t
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'114'			;write a char into r
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'32'			;write a char into ' '
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'71'			;write a char into G
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'114'			;write a char into r
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	SERIAL_TRANSMIT
	
	RETURN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;DISPLAY_GREEN_OFF: Called when the green butten is released;
;Accepts: Nothing                                           ;
;Returns: Nothing                                           ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
DISPLAY_GREEN_OFF:
	BTFSS	MASTER_GREEN_STATUS, 0x00
	RETURN
	
	MOVLW	0x00			;set the status to off
	MOVWF	MASTER_GREEN_STATUS
	
	MOVLW	d'77'			;write a char into M
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'97'			;write a char into a
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'115'			;write a char into s
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'116'			;write a char into t
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'114'			;write a char into r
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'32'			;write a char into ' '
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'71'			;write a char into G
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'114'			;write a char into r
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'102'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'102'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	SERIAL_TRANSMIT
	
	
	RETURN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;DISPLAY_RED_ON: Called when the red butten is pressed    ;
;Accepts: Nothing                                         ;
;Returns: Nothing                                         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;	
DISPLAY_RED_ON:
	BTFSC	MASTER_RED_STATUS, 0x00
	RETURN

	MOVLW	0x01			;set the status to on
	MOVWF	MASTER_RED_STATUS
		
	MOVLW	d'77'			;write a char into M
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'97'			;write a char into a
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'115'			;write a char into s
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'116'			;write a char into t
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'114'			;write a char into r
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'32'			;write a char into ' '
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'82'			;write a char into R
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'100'			;write a char into d
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	SERIAL_TRANSMIT
	RETURN
	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;DISPLAY_RED_OFF: Called when the red butten is released    ;
;Accepts: Nothing                                           ;
;Returns: Nothing                                           ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;	
DISPLAY_RED_OFF:
	BTFSS	MASTER_RED_STATUS, 0x00
	RETURN
	
	MOVLW	0x00			;set the status to off
	MOVWF	MASTER_RED_STATUS
	
	MOVLW	d'77'			;write a char into M
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'97'			;write a char into a
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'115'			;write a char into s
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'116'			;write a char into t
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'114'			;write a char into r
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'32'			;write a char into ' '
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'82'			;write a char into R
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'100'			;write a char into d
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'102'			;write a char into f
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'102'			;write a char into f
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	SERIAL_TRANSMIT
	RETURN	
	
	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;DISPLAY_SLAVE_GREEN_ON: Called when the green butten is pressed;
;Accepts: Nothing                                               ;
;Returns: Nothing                                               ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;	
DISPLAY_SLAVE_GREEN_ON:
	BTFSC	SLAVE_GREEN_STATUS, 0x00	;test the status
	RETURN

	MOVLW	0x01			;set the status to on
	MOVWF	SLAVE_GREEN_STATUS
	
	MOVLW	d'83'			;write a char into S
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'108'			;write a char into l
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'97'			;write a char into a
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'118'			;write a char into v
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'32'			;write a char into ' '
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'71'			;write a char into G
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'114'			;write a char into r
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	SERIAL_TRANSMIT
	
	
	RETURN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;DISPLAY_SLAVE_GREEN_OFF: Called when the green butten is released;
;Accepts: Nothing                                                 ;
;Returns: Nothing                                                 ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
DISPLAY_SLAVE_GREEN_OFF:
	BTFSS	SLAVE_GREEN_STATUS, 0x00
	RETURN
	
	MOVLW	0x00			;set the status to off
	MOVWF	SLAVE_GREEN_STATUS
	
	MOVLW	d'83'			;write a char into S
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'108'			;write a char into l
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'97'			;write a char into a
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'118'			;write a char into v
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'32'			;write a char into ' '
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'71'			;write a char into G
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'114'			;write a char into r
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'102'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'102'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	SERIAL_TRANSMIT
		
	RETURN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;DISPLAY_SLAVE_RED_ON: Called when the red butten is pressed ;
;Accepts: Nothing                                            ;
;Returns: Nothing                                            ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;	
DISPLAY_SLAVE_RED_ON:
	BTFSC	SLAVE_RED_STATUS, 0x00
	RETURN

	MOVLW	0x01			;set the status to on
	MOVWF	SLAVE_RED_STATUS
		
	MOVLW	d'83'			;write a char into S
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'108'			;write a char into l
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'97'			;write a char into a
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'118'			;write a char into v
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'32'			;write a char into ' '
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'82'			;write a char into R
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'100'			;write a char into d
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'110'			;write a char into n
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	SERIAL_TRANSMIT
	
	RETURN
	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;DISPLAY_SLAVE_RED_OFF: Called when the red butten is released ;
;Accepts: Nothing                                              ;
;Returns: Nothing                                              ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;	
DISPLAY_SLAVE_RED_OFF:
	BTFSS	SLAVE_RED_STATUS, 0x00
	RETURN
	
	MOVLW	0x00			;set the status to off
	MOVWF	SLAVE_RED_STATUS
	
	MOVLW	d'83'			;write a char into S
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'108'			;write a char into l
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'97'			;write a char into a
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'118'			;write a char into v
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'32'			;write a char into ' '
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'82'			;write a char into R
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'101'			;write a char into e
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'100'			;write a char into d
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'58'			;write a char into :
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'79'			;write a char into O
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'102'			;write a char into f
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'102'			;write a char into f
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'10'			;write a char into LF
	CALL 	SERIAL_TRANSMIT
	
	MOVLW	d'13'			;write a char into CR
	CALL 	SERIAL_TRANSMIT
	
	RETURN
	
	END


