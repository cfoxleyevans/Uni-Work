#include "p16f84a.inc"

; CONFIG
; __config 0xFFF3
 __CONFIG _FOSC_EXTRC & _WDTE_OFF & _PWRTE_ON & _CP_OFF

;variable definitions
VALA       EQU 0x10
COUNTER1   EQU 0x11
COUNTER2   EQU 0x12

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; INIT                                                                ;
; This section of code is where any pre program setup is done         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
INIT:
    BSF     STATUS, RP0 ;move to bank 1

    MOVLW   0XFF        ;set the switches for input
    MOVWF   TRISA

    CLRF    TRISB       ;set the lights for output

    BCF     STATUS, RP0 ;move to bank 0

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; MAIN                                                                ;
; This is the main entry point of the application                     ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
MAIN:
    BCF     STATUS, C
    MOVLW   b'00000001'
    MOVWF   PORTB

    CALL    KNIGHT

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; KNIGHT                                                                ;
; This is the block of code that controls                     ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
KNIGHT:
    MOVLW   d'07'       ;set how many stop to move
    MOVWF   VALA
    CALL    UP          ;move up the lights

    MOVLW   d'07'       ;set how many stops to move
    MOVWF   VALA
    CALL    DOWN        ;move down the lights

    GOTO KNIGHT

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; UP                                                                  ;
; This subroutine is used to wrap moving the light up the board       ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
UP:
    CALL    WAIT        ;have the processor stall
    RLF     PORTB, F    ;move the bit
    DECFSZ  VALA, F     ;see if we have finished the cycle
    GOTO    UP          ;loop
    RETURN


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; DOWN                                                                ;
; This subroutine is used to wrap moving the light down the board     ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
DOWN:
    CALL    WAIT        ;have the processor stall
    RRF     PORTB, F  ;move the bit
    DECFSZ  VALA, F     ;see if we have finished the cycle
    GOTO    DOWN        ;loop
    RETURN


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; SHIFT_DOWN                                                          ;
; This subroutine is used to create a SRL style function              ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
SHIFT_DOWN:
    RRF     PORTB, F    ;shift the 1 down
    RETURN

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; WAIT                                                                ;
; This subroutine causes the processor to stall for a short period    ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
WAIT:
    MOVLW   0x7A        ;store 255 in both of the counters
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