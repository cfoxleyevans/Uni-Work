#include "globals.h"
#include "vga.h"

static uint16_t * videoram = (uint16_t *) 0xb8000;

static const uint16_t ctrlreg = 0x03D4; // Address on I/O bus
static const uint16_t datareg = 0x03D5; // Address on I/O bus

static const int linelen   = 80;
static const int lines     = 25;

static int x = 0;
static int y = 0;

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

void scroll(){

      uint16_t *screenptr = videoram; //pointer to the start
      uint16_t *copy = videoram + 80; //

      //copy the data 
      int i = 0;
      while(i++ < 1840){
         *screenptr++ = *copy++;
      }

      x = 0; y = 23;
      setcursor(x,y);
   }


//
//   Clear the screen and move cursor to position 0,0
//
void vgainit(){
   uint16_t *screenptr = videoram; //take copy of pointer to start of vram
   
   int i;
   for (i = 0; i < 2000; i++) //loop over all of the pixels
   {
      *screenptr++ = ' ' | FOREGROUND(WHITE) | BACKGROUND(BLUE); //write nothing to the screen
   }

   //set the cursor position for new writes
   x = 0; y = 0;
   setcursor(x, y);
}

void putchar(char c){
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
   screenptr += (y * 80) + x; //move to the current pos; 

   if(y >= 24){
      scroll();
      return;
   }

   if(y <= 23){
      switch(c){
         case '\n': x = 0; y++; 
         setcursor(x,y); 
         return;
      }
   }
   else{
      scroll(); 
      return;
   }
   
   if (x < 80){
      *screenptr = c | FOREGROUND(WHITE) | BACKGROUND(BLUE); 
      x++; //increase x pos of cursor
      
      //now that we have printed the char are we at the end of the line
      if(x == 80){
         y++; x = 0; setcursor(x,y); return;
      }
     setcursor(x,y);
   }
}

void
status (char *str ) {

   if (!str) return; //not a valid string return

   uint16_t *screenptr = videoram; //take a copy of the pointer to the start of vram
   screenptr += 1920; //move to the start of the last line

   int length = my_strlen(str); //length of the status string
   
   //print the status string
   int i = 0;
   for(; i < length; i++){
      *screenptr++ = str[i] | FOREGROUND(BLUE) | BACKGROUND(WHITE);
   }
}


