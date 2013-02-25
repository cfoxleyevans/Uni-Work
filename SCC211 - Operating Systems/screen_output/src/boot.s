# setting up the Multiboot header - see GRUB docs for details
.set ALIGN,	1<<0			# align modules on page boundaries
.set MEMINFO,	1<<1			# provide memory map
.set FLAGS,	ALIGN | MEMINFO		# this is the Multiboot 'flag' field
.set MAGIC,	0x1BADB002	# MB1	# 'magic no.' so bootloader finds header
#		0xe85250d6	# MB2
.set CHECKSUM,	-(MAGIC + FLAGS)	# checksum required

.align 4
MultiBootHeader:
	.long MAGIC
	.long FLAGS
	.long CHECKSUM

	jmp loader
