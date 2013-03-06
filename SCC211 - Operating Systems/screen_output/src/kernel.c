#include "globals.h"
#include "multiboot.h"

void
abort( ) {
   kprintf ( "\n**** ABORT! ****\n");
   kprintf ( "Processor halted\n");
   asm ( "hlt;\n\t" );
}

void
kmain( void* mbd, unsigned int magic ) {
   vgainit  ( );	

   status ("SCC211 Operating Systems kernel");

   //kprintf("Hello this is a int: %08i\n", 45);
   //kprintf("Hello this is a hex: %08x\n", 45);
   //kprintf("Hello this is a oct: %08o\n", 45);
   //kprintf("Hello this is a bin: %08b\n", 45);
   //kprintf("Hello this is a string: %s\n", "String 001");
   //kprintf("Hello this is a percent: %%\n");

   getbootinfo (mbd, magic);
   
   abort( );
}
