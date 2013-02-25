/*
 *   multiboot_header is the struct we insert at head of code (see multiboot.s)
 *   multiboot_info   is the struct GRUB or equiv. passes to our kernel (kmain)
 *
 *   Note: most of these values are only valid if corresponding flag set
 *
 *   See: http://www.gnu.org/software/grub/manual/multiboot/multiboot.html
 */
#include "globals.h"

#define MBTHDR_MAGIC       0x1BADB002
#define MBTBOOTLDR_MAGIC   0x2BADB002 /* Passed in x86 EAX register */

#define MBTMOD_ALIGN       0x1000
#define MBTINFO_ALIGN      0x0004

/*
 * Header flags
 */
#define MBTHDR_PAGEALIGN   0x0001
#define MBTHDR_MEMINFO     0x0002
#define MBTHDR_VIDEOMODE   0x0004
#define MBTHDR_AOUTKLUDGE  0x10000

/*
 * Info structure flags
 */
#define MBTINFO_MEMORY     0x0001
#define MBTINFO_BOOTDEV    0x0002
#define MBTINFO_CMDLINE    0x0004
#define MBTINFO_MODS       0x0008
#define MBTINFO_AOUT       0x0010
#define MBTINFO_ELF        0x0020
#define MBTINFO_MEMORYMAP  0x0040
#define MBTINFO_DRIVEINFO  0x0080
#define MBTINFO_CONFIGTBL  0x0100
#define MBTINFO_BTLDRNAME  0x0200
#define MBTINFO_APMTABLE   0x0400
#define MBTINFO_VIDEOINFO  0x0800

struct __attribute__((__packed__))
aoutkludge {
   uint32_t hdr_addr;
   uint32_t load_addr;
   uint32_t end_addr;
   uint32_t bssend_addr;
   uint32_t entry_addr;
};

struct __attribute__((__packed__))
videodata {
   uint32_t mode;   /* 0->(prefer) linear graphics, 1->EGA standard text mode */
   uint32_t width;  /* Pixels in graphics mode, chars in text mode (0 no pref)*/
   uint32_t height; /* Pixels in graphics mode, lines in text mode (0 no pref)*/
   uint32_t depth;  /* Bits per pixel in graphics, 0->text or no preference   */
};

struct __attribute__((__packed__))
multiboot_header {
   uint32_t magic;
   uint32_t flags;
   uint32_t checksum;
   struct aoutkludge aoutinfo;
   struct videodata  videoinfo;
};

struct __attribute__((__packed__))
aout_table {
   uint32_t tblsz;
   uint32_t strsz;
   uint32_t addr;
   uint32_t reserved;
};

struct __attribute__((__packed__))
elf_table {
   uint32_t num;
   uint32_t size;
   uint32_t addr;
   uint32_t index;
};

struct __attribute__((__packed__))
vbe {
   uint32_t ctrl_info;
   uint32_t mode_info;
   uint32_t mode;
   uint32_t if_seg;
   uint32_t if_off;
   uint32_t if_len;
};

struct __attribute__((__packed__))
multiboot_info {
   uint32_t flags;
   uint32_t mem_lower;     /* As found by BIOS */
   uint32_t mem_upper;     /* As found by BIOS */
   uint32_t bootdev;       /* Root partition   */
   uint32_t cmdline;       /* Likely /boot/kernel.bin */
   uint32_t mods_count;
   uint32_t mods_addr;
   union {
      struct aout_table elf;
      struct elf_table  aout;
   } symblhdr;
   uint32_t mmap_len;
   uint32_t mmap_addr;
   uint32_t drives_len;
   uint32_t drives_addr;
   uint32_t romcfg_tbl;    /* Address of ROM config table returned by BIOS  */
   uint32_t bootldrname;   /* GRUB or equiv. */
   uint32_t apm_tbl;       /* Advanced Power Management (APM) segment info. */
   struct vbe video_if;
};

struct __attribute__((__packed__))
module_list {
   uint32_t start;
   uint32_t end;
   uint32_t cmdline;
   uint32_t zero;
};

struct __attribute__((__packed__))
memorymap {
   uint32_t size;
   uint32_t base_addrl;
   uint32_t base_addrh;
   uint32_t lengthl;
   uint32_t lengthh;
   uint32_t type;      /* 1 -> Available, 2-> reserved. (any holes are empty) */
};

/*
 *   Note: the following doesn't seem to be populated by GRUB2
 *         so don't rely on this beyond legacy GRUB - probably
 *         better to probe controllers returned by PCI anyway.
 *         ... dropping to legacy I/O ports if no PCI support.
 */
struct __attribute__((__packed__))
driveinfo {
   uint32_t size;
   uint8_t  drivenum;  /* BIOS drive number */
   uint8_t  drivemode; /* 0->CHS (Cyl./Head/Sect.), 1->LBA Logicl Block Addr */
   uint16_t cylinders;
   uint8_t  heads;
   uint8_t  sectors;   /* Sectors per track */
   uint16_t ioports[]; /* I/O ports linked to drive access (incl. DMA etc.) */
};
