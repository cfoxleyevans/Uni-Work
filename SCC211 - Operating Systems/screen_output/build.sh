# cd /home/lancs/task2

SRC=src
OBJ=obj
ISO=isofiles
CPATH=/usr/local/cross/bin

if [ -e bootable.iso ]; then
   /bin/rm bootable.iso
fi
if [ -e kernel.bin ]; then
   /bin/rm kernel.bin
fi
if [ -d ${ISO} ]; then
   /bin/rm -r ${ISO}/
fi
if [ -d ${OBJ} ]; then
   /bin/rm -r ${OBJ}/
fi
/bin/mkdir -p ${OBJ}
/bin/mkdir -p ${ISO}/boot/grub

AS=${CPATH}/i586-elf-as
CC=${CPATH}/i586-elf-gcc
LD=${CPATH}/i586-elf-ld
CFLAGS="-Wall -Wextra  -fno-builtin"
LDFLAGS="-nostdlib -nostartfiles -nodefaultlibs"

${AS} -o ${OBJ}/boot.o   ${SRC}/boot.s
${AS} -o ${OBJ}/loader.o ${SRC}/loader.s

${CC} ${CFLAGS} -o ${OBJ}/io.o        -c ${SRC}/io.c
${CC} ${CFLAGS} -o ${OBJ}/kernel.o    -c ${SRC}/kernel.c
${CC} ${CFLAGS} -o ${OBJ}/kprintf.o   -c ${SRC}/kprintf.c
${CC} ${CFLAGS} -o ${OBJ}/multiboot.o -c ${SRC}/multiboot.c
${CC} ${CFLAGS} -o ${OBJ}/vga.o       -c ${SRC}/vga.c

${LD} ${LDFLAGS} -T ${SRC}/linker.ld -o kernel.bin \
	${OBJ}/boot.o \
	${OBJ}/loader.o \
	${OBJ}/kernel.o \
	${OBJ}/io.o \
	${OBJ}/kprintf.o \
	${OBJ}/multiboot.o \
	${OBJ}/vga.o

/bin/cp grub/stage2_eltorito ${ISO}/boot/grub
/bin/cat > ${ISO}/boot/grub/menu.lst << __ENDMENU__
default 0
timeout 1
 
color white/green yellow/blue

title SCC211 Kernel
kernel /boot/kernel.bin cmdline=any kernel parameters
__ENDMENU__
/bin/cp kernel.bin ${ISO}/boot
/usr/bin/genisoimage -A "SCC211" -input-charset utf-8\
	-R -b boot/grub/stage2_eltorito -no-emul-boot -boot-load-size 4 \
	-boot-info-table -o bootable.iso ${ISO}

/usr/bin/qemu-system-i386 -boot order=d -cdrom bootable.iso
