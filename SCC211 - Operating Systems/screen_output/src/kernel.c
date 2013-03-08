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
   
   //testing scrolling with this
   //int i = 0;
   //while(i++ < 30000){
      //kprintf("Hello this is line %i\n", i);
   //}
   
   getbootinfo(mbd, magic);

   abort();
}
