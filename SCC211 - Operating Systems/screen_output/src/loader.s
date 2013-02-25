.global loader				# making entry point visible to linker

.set STACKSIZE,	0x8000			# that is, 32k.

loader:
	mov	$(stack + STACKSIZE), %esp	# set up the stack
	push	%eax			# Multiboot magic number
	push	%ebx			# Multiboot data structure

	call	kmain			# call kernel proper

	cli				# Disable interrupts
hang:
	hlt				# halt machine should kernel return
	jmp	hang

# reserve initial kernel stack space
.comm stack,	STACKSIZE, 32		# reserve 32k stack on quadword boundary
