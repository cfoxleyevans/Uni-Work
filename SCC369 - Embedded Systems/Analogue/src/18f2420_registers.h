/**
 * For the pic18f2420 to access SFR's directly
 * 
 * Draft 0 - might not work, patches welcome!
 * Send patches to j.vidler@lancaster.ac.uk
 */

#ifndef PIC18F2420_SFRS_H
#define PIC18F2420_SFRS_H

	#BYTE TOSU = 0xFFF
	#BYTE INDF2 = 0xFDF
	#BYTE CCPR1H = 0xFBF
	#BYTE IPR1 = 0xF9F
	#BYTE TOS = 0xFFE
	#BYTE POSTINC2 = 0xFDE
	#BYTE CCPR1L = 0xFBE
	#BYTE PIR1 = 0xF9E
	#BYTE TOSL = 0xFFD
	#BYTE POSTDEC2 = 0xFDD
	#BYTE CCP1CON = 0xFBD
	#BYTE PIE1 = 0xF9D
	#BYTE STKPTR = 0xFFC
	#BYTE PREINC2 = 0xFDC
	#BYTE CCPR2 = 0xFBC
	#BYTE PCLATU = 0xFFB
	#BYTE PLUSW2 = 0xFDB
	#BYTE CCPR2L = 0xFBB
	#BYTE OSCTUNE = 0xF9B
	#BYTE PCLAT = 0xFFA
	#BYTE FSR2 = 0xFDA
	#BYTE CCP2CON = 0xFBA
	#BYTE PCL = 0xFF9
	#BYTE FSR2L = 0xFD9
	#BYTE TBLPTRU = 0xFF8
	#BYTE STATUS = 0xFD8
	#BYTE BAUDCON = 0xFB8
	#BYTE TBLPTR = 0xFF7
	#BYTE TMR0 = 0xFD7
	#BYTE PWM1CON = 0xFB7
	#BYTE TRISE = 0xF96
	#BYTE TBLPTRL = 0xFF6
	#BYTE TMR0L = 0xFD6
	#BYTE ECCP1AS = 0xFB6
	#BYTE TABLAT = 0xFF5
	#BYTE T0CON = 0xFD5
	#BYTE CVRCON = 0xFB5
	#BYTE TRISD = 0xF95
	#BYTE CMCON = 0xFB4
	#BYTE TRISC = 0xF94
	#BYTE PRODL = 0xFF4
	#BYTE PRODL = 0xFF3
	#BYTE OSCCON = 0xFD3
	#BYTE TMR3 = 0xFB3
	#BYTE TRISB = 0xF93
	#BYTE INTCON = 0xFF2
	#BYTE LVDCON = 0xFD2
	#BYTE TMR3L = 0xFB2
	#BYTE TRISA = 0xF92
	#BYTE INTCON2 = 0xFF1
	#BYTE WDTCON = 0xFD1
	#BYTE T3CON = 0xFB1
	#BYTE INTCON3 = 0xFF0
	#BYTE RCON = 0xFD0
	#BYTE SPBRG = 0xFB0
	#BYTE INDF0 = 0xFEF
	#BYTE TMR1 = 0xFCF
	#BYTE SPBRG = 0xFAF
	#BYTE POSTINC0 = 0xFEE
	#BYTE TMR1L = 0xFCE
	#BYTE RCREG = 0xFAE
	#BYTE POSTDEC0 = 0xFED
	#BYTE T1CON = 0xFCD
	#BYTE TXREG = 0xFAD
	#BYTE LATE = 0xF8D
	#BYTE TMR2 = 0xFCC
	#BYTE TXSTA = 0xFAC
	#BYTE LATD = 0xF8C
	#BYTE PREINC0 = 0xFEC
	#BYTE PLUSW0 = 0xFEB
	#BYTE PR2 = 0xFCB
	#BYTE RCSTA = 0xFAB
	#BYTE LATC = 0xF8B
	#BYTE FSR0 = 0xFEA
	#BYTE T2CON = 0xFCA
	#BYTE LATB = 0xF8A
	#BYTE FSR0L = 0xFE9
	#BYTE SSPBUF = 0xFC9
	#BYTE EEADR = 0xFA9
	#BYTE LATA = 0xF89
	#BYTE WREG = 0xFE8
	#BYTE SSPADD = 0xFC8
	#BYTE EEDATA = 0xFA8
	#BYTE INDF1 = 0xFE7
	#BYTE SSPSTAT = 0xFC7
	#BYTE EECON2 =0xFA7
	#BYTE POSTINC1 = 0xFE6
	#BYTE SSPCON1 = 0xFC6
	#BYTE EECON1 = 0xFA6
	#BYTE POSTDEC1 = 0xFE5
	#BYTE SSPCON2 = 0xFC5
	#BYTE PORTE = 0xF84
	#BYTE PREINC1 = 0xFE4
	#BYTE ADRES = 0xFC4
	#BYTE PLUSW1 = 0xFE3
	#BYTE ADRESL = 0xFC3
	#BYTE PORTD = 0xF83
	#BYTE FSR1 = 0xFE2
	#BYTE ADCON0 = 0xFC2
	#BYTE IPR2 = 0xFA2
	#BYTE PORTC = 0xF82
	#BYTE FSR1L = 0xFE1
	#BYTE ADCON1 = 0xFC1
	#BYTE PIR2 = 0xFA1
	#BYTE PORTB = 0xF81
	#BYTE BSR = 0xFE0
	#BYTE ADCON2 = 0xFC0
	#BYTE PIE2 =0xFA0
	#BYTE PORTA =0xF80

#endif
