#include "p16f84a.inc"

; CONFIG
; __config 0xFFF3
 __CONFIG _FOSC_EXTRC & _WDTE_OFF & _PWRTE_ON & _CP_OFF


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; INIT                                                                ;
; This section of code is where any pre program setup is done         ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
INIT:
    BSF     STATUS, RP0     ;move to bank 1

    MOVLW   0XFF            ;set the switches for input
    MOVWF   TRISA

    CLRF    TRISB           ;set the lights for output

    BCF     STATUS, RP0     ;move bank to bank 0

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; MAIN                                                                ;
; This is the main entry point of the application                     ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
MAIN:
    MOVLW   0xFF            ;set the bit pattern 11111111 in portb
    MOVWF   PORTB

    END
