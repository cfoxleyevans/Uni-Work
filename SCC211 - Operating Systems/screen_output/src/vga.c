#include "globals.h"
#include "vga.h"

static uint16_t * videoram = (uint16_t *) 0xb8000;

static const uint16_t ctrlreg = 0x03D4; // Address on I/O bus
static const uint16_t datareg = 0x03D5; // Address on I/O bus

static const int linelen   = 80;
static const int lines     = 24;

static int x = 0;
static int y = 0;

static int chars_on_line = 0; //keep track of how man chars on the current line
static int total_chars = 0; //how many line have we written

//
//   Move cursor to position x,y
//   - Notice this uses isolated I/O
//
void
setcursor (int x, int y) {
   uint16_t posn;

   posn = y * linelen + x;
   outb (ctrlreg, 14);
   outb (datareg, posn >> 8);
   outb (ctrlreg, 15);
   outb (datareg, posn &  0xff);
}

//
//   Clear the screen and move cursor to position 0,0
//
void vgainit ( ) {
   uint16_t *screenp = videoram; //alias for pointer to video ram
   
   int i;
   for (i = 0; i < 2000; i++) //loop over all of the pixels
   {
      *screenp++ = ' ' | FOREGROUND(GREEN) | BACKGROUND(BLACK); //write nothing to the screen
   }

   //set the cursor position for new
   x = 0; y = 0;
   setcursor(x, y);
}

void putchar (char c) {
   //   Store character value (with colour information) at memory location
   //   of current x,y screen position and update x and y ready to move
   //   the cursor.
   //
   //   Remember that you'll need to handle reaching the end of lines and
   //   the bottom of the screen. Don't forget that, if you plan on having
   //   a status line you need to ensure you consider this when you're
   //   calculating the limit of the display area.
   //
   uint16_t *screenptr = videoram; //take copy of pointer to start of vram
   screenptr += total_chars; //move pointer to current pos

   //deal with new line chars
   switch(c){ 
      //the char for new line
      case '\n': 
         total_chars += (80-chars_on_line); //increase the total by 80 - what we allredy have 
         chars_on_line = 0; //reset the char count
         x = 0; //reset the x pos
         y++; //move down a line
         setcursor(x,y); //set the cursor
         return;
   } 


   if (chars_on_line < (linelen - 1){ 
      //we arnt at the end of the line so print this char
      *screenptr = c | FOREGROUND(GREEN) | BACKGROUND(BLACK); 
      
      chars_on_line++; //increase the number of chars on the line
      
      x++; //increase x pos of cursor
      
      total_chars++; //increase the total number of chars we have printed
      
      //now that we have printed the char are we at the end of the line
      if(chars_on_line >= 80){
         //yes so reset the counter and move the y value
         chars_on_line = 0; 
         y++;
      }
   }
   
   setcursor(x, y);
   
}

void
status (char *str ) {

   if (!str) return; //not a valid string return

   uint16_t *screenptr = videoram; //take a copy of the pointer to the start of vram
   screenptr += 1920; //move to the start of the last line

   int length = my_strlen(str); //length of the status string
   
   //print the statuc string
   int i = 0;
   for(; i < length; i++){
      *screenptr = str[i] | FOREGROUND(WHITE) | BACKGROUND(BLUE);
      screenptr++;;
   }
}
