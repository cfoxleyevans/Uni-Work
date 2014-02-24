#include "p16f84a.inc"

; CONFIG
; __config 0xFFF3
 __CONFIG _FOSC_EXTRC & _WDTE_OFF & _PWRTE_ON & _CP_OFF

;variable definitions
VALA    EQU 0x10
VALB    EQU 0x11
VALC    EQU 0x12

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
    MOVLW   0xAA        ;set the value of vala
    MOVWF   VALA

    MOVLW   0xCC        ;set the value of valb
    MOVWF   VALB

    MOVLW   0xEE        ;set the value of valc
    MOVWF   VALC

    MOVFW   VALA        ;write the value in the register into portb
    MOVWF   PORTB

    END
