#include "lcd_driver.h"

int const LCD_INIT_STRING[4] = { 
 	0x20 | (LCD_LINES << 2), // Func set: 4-bit, 2 lines, 5x8 dots 
 	0xc,                    // Display on 
 	1,                      // Clear display 
 	6                       // Increment cursor 
};

void init_lcd(void) {
	int i; 

	output_low(LCD_RS); 

	output_low(LCD_ENABLE); 

	delay_ms(15); 

	for(i=0 ;i < 3; i++) { 
    	write_nibble_lcd(0x03); 
    	delay_ms(5);
    } 

	write_nibble_lcd(0x02); 

	for(i=0; i < sizeof(I); i++) { 
    	write_byte_lcd(0, LCD_INIT_STRING[i]); 
    } 
}

void write_nibble_lcd(int nibble) {
	output_bit(LCD_DB4, !!(nibble & 1)); 
	output_bit(LCD_DB5, !!(nibble & 2));  
	output_bit(LCD_DB6, !!(nibble & 4));    
	output_bit(LCD_DB7, !!(nibble & 8));    

	delay_cycles(1); 
 	output_high(LCD_ENABLE); 
 	delay_us(5); 
 	output_low(LCD_ENABLE); 
}

void write_byte_lcd(int location, int data) {
	output_low(LCD_RS);

	if(location)
		output_high(LCD_RS);
	else
		output_low(LCD_RS);

 	delay_cycles(1); 

 	output_low(LCD_ENABLE);

	write_nibble_lcd(data >> 4); 
	write_nibble_lcd(data & 0xf); 
 }

void clear_lcd(void) {

}

void move_cursor_lcd(int x, int y) {
	int address; 

	if(y != 1) 
   		address = LINE_TWO_ADDRESS; 
	else 
   		address=0; 

	address += x-1; 
	write_byte_lcd(INSTRUCTION_REGISTER, (0x80 | address)); 
}


void putc_lcd(char value) {
	switch(value) { 
    	case '\f': 
	      write_byte_lcd(INSTRUCTION_REGISTER, 1); 
	      delay_ms(2); 
	      break; 
	    
	    case '\n': 
	       move_cursor_lcd(1,2); 
	       break; 
	    
	    case '\b': 
	       write_byte_lcd(INSTRUCTION_REGISTER, 0x10); 
	       delay_ms(2);
	       break; 
	    
	    default: 
	       write_byte_lcd(DATA_REGISTER, value);
	       delay_ms(2); 
	       break;
	} 	 
}