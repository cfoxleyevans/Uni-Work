#include "globals.h"
#include "multiboot.h"

void
getbootinfo( void* mbd, unsigned int magic ) {
   struct multiboot_info * bootinfo;
   struct memorymap *      mmap;
   struct memorymap *      mmap_end;

   /*
    * Note the magic number we get is different to that we have in our
    * MultiBootHeader (0x1BADB002)
    */
   if ( magic != 0x2BADB002 ) {
      kprintf ("Multiboot ptr: %x\n", mbd);
      kprintf ("Wrong Multiboot magic number");
      abort ( );
   }

   bootinfo = (struct multiboot_info *)  mbd;

   if (bootinfo->flags & MBTINFO_MEMORY) {
      kprintf ("Memory: %ud - %udKB\n",
         bootinfo->mem_lower, bootinfo->mem_upper);
   }
 
   if (bootinfo->flags & MBTINFO_CMDLINE) {
      kprintf ("Command line: %s\n", (char *) bootinfo->cmdline);
   }
 
   if (bootinfo->flags & MBTINFO_MEMORYMAP) {
      mmap     = (struct memorymap *)  bootinfo->mmap_addr;
      mmap_end = (struct memorymap *) (bootinfo->mmap_addr+bootinfo->mmap_len);

      while (mmap < mmap_end) {
         kprintf ("Memory: %08ux,%08uxh, Length: %08ux,%08uxh %s\n",
            mmap->base_addrh, mmap->base_addrl,
            mmap->lengthh,    mmap->lengthl,
            (mmap->type == 1) ? "Avail." : "Resvd.");

         mmap = (struct memorymap *)
                   (mmap->size + sizeof(mmap->size) + (uint32_t)mmap);
      }
   }
 
   if (bootinfo->flags & MBTINFO_BTLDRNAME) {
      kprintf ("Bootloader: %s\n", (char *) bootinfo->bootldrname);
   }
 
}
