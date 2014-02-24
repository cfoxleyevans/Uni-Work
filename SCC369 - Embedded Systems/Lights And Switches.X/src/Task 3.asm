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
   MOVFW    PORTA       ;move the switch state to the working register
   
   XORLW    0x0F        ;xor the value with 00001111 to flip the bits
   
   MOVWF    PORTB       ;move the pattern into the lights

   GOTO MAIN            ;keep checking the switches

   END