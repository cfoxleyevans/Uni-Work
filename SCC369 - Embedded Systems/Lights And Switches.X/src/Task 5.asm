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
    CLRF    VALA        ;move 0 into vala

    CALL    DISPLAY     ;call the display method

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; DISPLAY                                                             ;
; This block increments a counter displays the value on the lights    ;
; it then stalls for a short period before continuing                 ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
DISPLAY:
    MOVFW   VALA        ;move the current value of vala into portb
    MOVWF   PORTB

    CALL    WAIT        ;call the wait function
  
    INCF    VALA, F     ;increment the count

    GOTO    DISPLAY     ;do over

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; WAIT                                                                ;
; This subroutine causes the processor to stall for a short period    ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
WAIT:
    MOVLW   0x60        ;store 255 in both of the counters
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