#include "p16f84a.inc"

; CONFIG
; __config 0xFFF3
 __CONFIG _FOSC_EXTRC & _WDTE_OFF & _PWRTE_ON & _CP_OFF

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; DECLARATIONS                                                        ;
; This section of code is where constant definitions are made         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
NUMBER0     	EQU 0x15    ;memory locations for the number patterns
NUMBER1     	EQU 0x16
NUMBER2     	EQU 0x17
NUMBER3     	EQU 0x18
NUMBER4     	EQU 0x19
NUMBER5     	EQU 0x1A
NUMBER6     	EQU 0x1B
NUMBER7     	EQU 0x1C
NUMBER8     	EQU 0x1D
NUMBER9     	EQU 0x1E

NUMBERS_BASE    EQU 0X15    ;the base index of the patterns

BLOCK1VALUE     EQU 0x20    ;the value currently stored in the block
BLOCK2VALUE     EQU 0x21
BLOCK3VALUE     EQU 0x22
BLOCK4VALUE     EQU 0x23

COUNTER1    	EQU 0X24    ;a general purpose counter
COUNTER2    	EQU 0X25

LOOPC1       	EQU 0x26    ;loops vars for the wait routine
LOOPC2       	EQU 0x27

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; INIT                                                                ;
; This section of code is where any pre program setup is done         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
INIT:
    BSF     STATUS, RP0         ;move to bank 0

	CLRF	TRISA               ;set the digits for output

	BSF		TRISA, 0x04         ;set the switch for input

    CLRF    TRISB               ;set the lights for output

    BCF     STATUS, RP0         ;move back to bank 0

	CLRF	PORTA               ;turn on just the first block
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

	MOVLW   b'00001011'   	;move the bit parrern for 9 into w
    MOVWF   NUMBER9			;move this into the top address for the numbers

    MOVLW   b'10001000'            ;move 0 into the value of each display
    MOVWF   BLOCK1VALUE
    MOVWF   BLOCK2VALUE
    MOVWF   BLOCK3VALUE
    MOVWF   BLOCK4VALUE

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; INIT                                                                ;
; This section of code is where any pre program setup is done         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
MAIN:

CLOCK:
    MOVLW   NUMBERS_BASE    ;reset the pointer
    MOVWF   FSR

    MOVWF   BLOCK1VALUE     ;move to the correct index
    ADDWF   FSR, F

    MOVFW   INDF            ;move the bit pattern into portb
    MOVWF   PORTB
    
    CALL    WAIT

    CALL    INCBLOCK1

    GOTO    CLOCK           ;repaeat

INCBLOCK1:
    MOVFW   BLOCK1VALUE     ;check to make sure we are less than 9
    SUBLW   d'10'
    BTFSC   STATUS, Z
    GOTO    BLOCK1RESET     ;more than 9 so reset this block

    INCF    BLOCK1VALUE, F  ;increment the block value
    RETURN

BLOCK1RESET:
    MOVLW   0x00            ;reset the block value
    MOVWF   BLOCK1VALUE

    RETURN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; WAIT                                                                ;
; This subroutine causes the processor to stall for a short period    ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
WAIT:
    MOVLW   0xFF        ;store 255 in both of the counters 0x05
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
