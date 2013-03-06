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
   vgainit();	

   status ("SCC211 Operating Systems kernel");

   
   int i = 0;
   for(; i < 25; i++){
      kprintf("This is line %i\n", i);
   }
   

   //getbootinfo(mbd, magic);
   
   //abort();
}
