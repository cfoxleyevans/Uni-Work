#ifndef __GLOBALS__
#define __GLOBALS__

#define BREAK(); asm volatile ( "xchg %bx, %bx;" ); /* Bochs magic break code */
#define NULL 0

//
//	======================== TYPES ========================
//
typedef unsigned char      uint8_t;
typedef unsigned short     uint16_t;
typedef unsigned long      uint32_t;
typedef unsigned long long uint64_t;

typedef unsigned long      size_t;

//typedef char      int8_t;
typedef short     int16_t;
typedef long      int32_t;
typedef long long int64_t;

//
//	=================== PUBLIC FUNCTIONS ===================
//

// kernel.c
void kmain    ( void *, unsigned int );
void abort    ( void );

// io.c
void     outb    ( uint16_t, uint8_t  );

// multiboot.c
void getbootinfo (void *, unsigned int );

// printk.c
char * itoa   (unsigned long number, unsigned int base);
int my_strlen (char* string);
void kprintf  ( char *, ... );

// vga.c
void putchar  ( char   );
void vgainit  ( void   );
void status   ( char * );

#endif //__GLOBALS__
