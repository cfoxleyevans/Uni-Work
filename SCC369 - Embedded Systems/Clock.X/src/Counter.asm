#include "p16f84a.inc"

; CONFIG
; __config 0xFFF3
 __CONFIG _FOSC_EXTRC & _WDTE_OFF & _PWRTE_ON & _CP_OFF

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; DECLARATIONS                                                        ;
; This section of code is where constant definitions are made         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
NUMBER0     	EQU  0x15   ;memory locations for the number patterns
NUMBER1     	EQU  0x16 
NUMBER2     	EQU  0x17  
NUMBER3     	EQU  0x18 
NUMBER4     	EQU  0x19 
NUMBER5     	EQU  0x1A
NUMBER6     	EQU  0x1B
NUMBER7     	EQU  0x1C
NUMBER8     	EQU  0x1D
NUMBER9     	EQU  0x1E

NUMBERS_BASE    EQU 0X15    ;the base index of the patterns

COUNTER1    	EQU 0X10    ;a general purpose counter
COUNTER2    	EQU 0X11

LOOPC1       	EQU 0x12    ;loops vars for the wait routine
LOOPC2       	EQU 0x13

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; INIT                                                                ;
; This section of code is where any pre program setup is done         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
INIT:
    BSF     STATUS, RP0          ;move to bank 0

	CLRF	TRISA			;set the digits for output	
		
	BSF		TRISA, 0x04		;set the switch for input

    CLRF    TRISB           ;set the lights for output
	
    BCF     STATUS, RP0           ;move bank to bank 0
	
	CLRF	PORTA			;turn on just the first block
	BSF     PORTA, 0x00

    MOVLW   b'10001000'   	;move the bit pattern for 0 into W
    MOVWF   NUMBER0			;move this into the base address for the nubmers
    
	MOVLW   b'11101011'
    MOVWF   NUMBER1			
    
	MOVLW    b'01001100'
    MOVWF   NUMBER2		
    
	MOVLW    b'01001001'   
    MOVWF   NUMBER3			
    
	MOVLW    b'00101011'   
    MOVWF   NUMBER4	
	    
	MOVLW   b'00011001'
    MOVWF   NUMBER5		
    
	MOVLW   b'00111000'   
    MOVWF   NUMBER6		
    
	MOVLW    b'11001011'   
    MOVWF   NUMBER7			
    
	MOVLW   b'00001000'  
    MOVWF   NUMBER8		
    
	MOVLW   b'00001011'   	;load the last bit parrten into w
    MOVWF   NUMBER9			;move this into the top address for the numbers

MAIN:
    MOVLW   NUMBERS_BASE    ;move the base address into the pointer register
    MOVWF   FSR

    MOVFW   INDF
    MOVWF   PORTB
    CLRF    PORTA
    BSF     PORTA, 0x00
    CALL    WAIT

    
    INCF    FSR, F
    INCF    FSR, F
    INCF    FSR, F
    MOVFW   INDF
    MOVWF   PORTB
    CLRF    PORTA
    BSF     PORTA, 0x01
    CALL    WAIT

    
    INCF    FSR, F
    MOVFW   INDF
    MOVWF   PORTB
    CLRF    PORTA
    BSF     PORTA, 0x02
    CALL    WAIT


    GOTO MAIN





;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; WAIT                                                                ;
; This subroutine causes the processor to stall for a short period    ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
WAIT:
    MOVLW   0x05        ;store 255 in both of the counters
    MOVWF   COUNTER1
    MOVWF   COUNTER1

    GOTO    LOOP1       ;go into the first loop
LOOP1:
    DECFSZ  COUNTER1, F ;decrement the loop counter untill it gets to 0
    GOTO    LOOP2       ;move into the second loop

    RETURN              ;return the the point where the wait sub was called
LOOP2:
    DECFSZ  COUNTER2, F ;decrement the loop counter untill it gets to 0
    GOTO    LOOP2       ;loop around
    GOTO    LOOP1       ;go back to the first loop

    END
