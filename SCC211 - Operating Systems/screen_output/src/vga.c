#include "globals.h"
#include "vga.h"

static uint16_t * videoram = (uint16_t *) 0xb8000;

static const uint16_t ctrlreg = 0x03D4; // Address on I/O bus
static const uint16_t datareg = 0x03D5; // Address on I/O bus

static const int linelen   = 80;
static const int lines     = 24;

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

//
//   Clear the screen and move cursor to position 0,0
//
void vgainit ( ) {
   uint16_t * screenp = videoram; //alias for pointer to video ram
   
   int i;
   for (i = 0; i < 2000; i++) //loop over all of the pixels
   {
      *screenp++ = ' ' | FOREGROUND(WHITE) | BACKGROUND(BLUE); //write nothing to the screen
   }

   //set the cursor position for new
   x = 0; y = 0;
   setcursor(x, y);
}

void putchar (char c) {
   //
   //   Store character value (with colour information) at memory location
   //   of current x,y screen position and update x and y ready to move
   //   the cursor.
   //
   //   Remember that you'll need to handle reaching the end of lines and
   //   the bottom of the screen. Don't forget that, if you plan on having
   //   a status line you need to ensure you consider this when you're
   //   calculating the limit of the display area.
   //

   uint16_t * screenp = videoram; //alias for pointer to video ram

   *screenp = c | FOREGROUND(WHITE) | BACKGROUND(BLUE);

   setcursor (++x, ++y);
}

void
status ( char * str ) {

   if (!str) return;

   //
   //   Display string in status line. This should be a simpler version
   //   of what you have for putchar as it'll simply be overwriting what's
   //   there, so no scrolling.
   //
   //   Use different colours for the status line so 
   //
}
