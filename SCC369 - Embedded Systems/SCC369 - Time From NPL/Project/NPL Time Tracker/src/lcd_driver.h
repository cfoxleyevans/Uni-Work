//LCD specific information
#define	LCD_ENABLE				PIN_C6
#define	LCD_RS					PIN_C7
#define	LCD_DB7					PIN_C0
#define	LCD_DB6					PIN_C1
#define	LCD_DB5					PIN_C2
#define	LCD_DB4					PIN_C5

#define INSTRUCTION_REGISTER	0
#define DATA_REGISTER			1

#define LCD_LINES				2
#define LINE_TWO_ADDRESS		0x40


/******************************************************************************
* Function: init_lcd        												  *
* Purpose: This is used to set the lcd to a known state                       *
* Accepts: nothing  														  *
* Reuturns: nothing 														  *
******************************************************************************/
void init_lcd(void);

/******************************************************************************
* Function: write_nibble_to_lcd        										  *
* Purpose: This is used to write a nibble to the lcd                          *
* Accepts: 																      *
*			nibble - this is the nibble that should be written  			  *											  *
* Reuturns: nothing 														  *
******************************************************************************/
void write_nibble_lcd(int nibble);

/******************************************************************************
* Function: write_byte_to_lcd        										  *
* Purpose: This is used to write a nibble to the lcd                          *
* Accepts: 																      *
*			location - either INSTRUCTION_REGISTER or DATA_REGISTER  		  *
*			data - this is the byte that should be written  			  *											  *
* Reuturns: nothing 														  *
******************************************************************************/
void write_byte_lcd(int location, int data);

/******************************************************************************
* Function: clear_lcd        												  *
* Purpose: This is used to clear the lcd of characters                        *
* Accepts: nothing  														  *
* Reuturns: nothing 														  *
******************************************************************************/
void clear_lcd(void);

/******************************************************************************
* Function: move_to_pos_lcd        											  *
* Purpose: This is used to move the 'cursor' to a given place                 *
* Accepts: 																	  *
*			row - this is the row that we want to be on                       *
*			col - this is the col that we want to be on  					  *									  *
* Reuturns: nothing 														  *
******************************************************************************/
void move_cursor_lcd(int x, int y);

void putc_lcd(char value);

