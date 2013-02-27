////////////////////////////////////////////////////////////////////
// Author : Chris Foxley-Evans
// Name : kprintf
// Start Date : 19th Feb 2013
// Finish Date : 
// Version : 1.0
// Function: To implement a version of the C function
// printf that uses non of the standard C functions
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
// INCLUDES
#include <stdio.h>
#include <stdarg.h>
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
// strlen : counts the number of chars in a string
// char* string : The string to count
// return : the number of chars in the string
////////////////////////////////////////////////////////////////////
int strlen(char* string){
	int num = 0;
	while (string[num] != 0){
		num++;
	}
	return num;
}
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
// itoa : returns a strinf of the given number in the base provided
// unsigned long number : the number that is to be converted
// int sign : should the output be singned
// unsigned int base : the base of the output stiring
// return : a string representing the number
////////////////////////////////////////////////////////////////////
char* itoa(unsigned long number, unsigned int base){
	
	if(base < 2 || base > 16) return (char*)0; //make sure that i have a valid base
	
	static char buf[32] = {0}; //buffer of 0's 32 long
	//convert the number
	int i;
	for(i = 30; number && i ; --i){ //work backwards so i dont have to reverse it
		
		buf[i] = "0123456789abcdef"[number % base]; //insert the char into the buffer
		number /= base; //remove the digit
	}
	return &buf[i+1]; //return the string	
}
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
// outstr : prints a string
// char* string : the string that is to be printed
////////////////////////////////////////////////////////////////////
void outstr(char *string){
	int i;
	for (i = 0; i < strlen(string); i++){
		putchar(string[i]);
	}
}
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
// kprintf : prints a format string
// char* string : the string that is to per printed
////////////////////////////////////////////////////////////////////
void kprintf(char* string, ...){

	int length = strlen(string); //length of the string that im prinnting
	char *s; // hold any chars or strings that i have to print	
	int number; //hold any integers that i might have to print
	va_list args; //variable argument list
	
	va_start(args, string); //init list
	
	int i; //loop index
	for(i = 0; i < length; i++){
		
		//output chars
		if (string[i]!= '%')
			putchar(string [i]);
		
		//deal with percent formatting
		else{
			switch(string[i + 1]){
				case 99 : s = va_arg(args, int); putchar((int)s); i++; break; //c
				case 105 : number = va_arg(args, int); outstr(itoa(number, 10)); i++; break; //i
				case 98 : number = va_arg(args, int); outstr(itoa(number, 2)); i++; break; //b
				case 111 : number = va_arg(args, int); outstr(itoa(number, 8)); i++; break; //o
				case 120 : number = va_arg(args, int); outstr(itoa(number, 16)); i++; break; //x
				case 115 : s = va_arg(args, char*); outstr(s); i++; break; //s
				case 37 : putchar(37); //%
			}	
		}
	}
	va_end(args);
}
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
// main : The entry point to execution
// int argc : The number of arguments passed to the function
// char** argv : The arguments passed to the function
////////////////////////////////////////////////////////////////////
int main(int argc, char** argv){
	
	kprintf("This is a string: %s\n", "Test String");
	kprintf("This is a binary number: %b\n", 23);
	kprintf("This is a octal number: %o\n", 45);
	kprintf("This is a integer number %i\n", 101);
	kprintf("This is a hexadecimal number %x\n", 13456);
	kprintf("This inserts a tab \t in the middle\n");
	
	return 0;
}
////////////////////////////////////////////////////////////////////
//end of file kprintf.c