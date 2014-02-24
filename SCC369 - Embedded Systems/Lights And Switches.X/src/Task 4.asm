#include "p16f84a.inc"

; CONFIG
; __config 0xFFF3
 __CONFIG _FOSC_EXTRC & _WDTE_OFF & _PWRTE_ON & _CP_OFF

;constant definitions
SWITCH1 EQU 0x00
SWITCH2 EQU 0x01
SWITCH3 EQU 0x02
SWITCH4 EQU 0x03

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
    CLRF    VALA           ;write 0 into the counter
   
    BTFSS   PORTA, SWITCH1 ;read the first switch and inc vala if pressed
    INCF    VALA,  F

    BTFSS   PORTA, SWITCH2 ;read the second switch and inc vala if pressed
    INCF    VALA,  F

    BTFSS   PORTA, SWITCH3 ;read the third switch and inc vala if pressed
    INCF    VALA,  F

    BTFSS   PORTA, SWITCH4 ;read the fourth switch and inc vala if pressed
    INCF    VALA,  F

    MOVFW   VALA           ;write the count into portb
    MOVWF   PORTB       

    GOTO    MAIN           ;do over
    
    END
