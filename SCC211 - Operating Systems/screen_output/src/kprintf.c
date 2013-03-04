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
#include <stdarg.h>
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
// strlen : counts the number of chars in a string
// char* string : The string to count
// return : the number of chars in the string
////////////////////////////////////////////////////////////////////
int my_strlen(char* string){
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
// isdigit : returns true is the passed value is a digit
// char test : the char that is to be tested
// return : 1 if test is digit 0 if not
////////////////////////////////////////////////////////////////////
int is_digit(char test){
	if (test >='0' && test <='9') 
		return 1;
	else 
		return 0;
}
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
// outstr : prints a string
// char* string : the string that is to be printed
////////////////////////////////////////////////////////////////////
void outstr(char *string, int pad_width, char pad_char){
	int i;
	if (my_strlen(string) < pad_width)
		while(pad_width-- > my_strlen(string))
			putchar(pad_char);

	for (i = 0; i < my_strlen(string); i++){
		putchar(string[i]);
	}
}
////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////
// kprintf : prints a format string
// char* string : the string that is to per printed
////////////////////////////////////////////////////////////////////
void kprintf(char* string, ...){
	int length = my_strlen(string); //length of the string that im prinnting

	char *s; // hold any chars or strings that i have to print	
	int number; //hold any integers that i might have to print

	int pad_width = 0; //the width of the padding
	int pad_char = 's'; //the padding char
	
	va_list args; //variable argument list
	
	va_start(args, string); //init list
	
	int i = 0; //loop index
	while (i < length){
		
		//is this a normal char ?????
		if (string[i] != '%')
			putchar(string[i++]); //yes it is so print it
		

		//we have found a percent so parse the fmt code
		else{
			i++; //move past the percent
			
			if(is_digit(string[i])){
				
				//check if i am using spaces or 0
				if(string[i] == '0'){
					pad_char = '0';
					i++;
				}

				//now calculate the padding
				while(is_digit(string[i])){
						pad_width *= 10;
						pad_width += (string[i] - 48);
						i++;
				}
			}

			//printf("pad char : %c\n", pad_char);
			//printf("pad width : %i\n", pad_width);
		
			switch(string[i]){
				case 99 : s = va_arg(args, int); putchar(s); i++; break; //c
				case 105 : number = va_arg(args, int); outstr(itoa(number, 10), pad_width, pad_char); i++; break; //i
				case 98 : number = va_arg(args, int); outstr(itoa(number, 2), pad_width, pad_char); i++; break; //b
				case 111 : number = va_arg(args, int); outstr(itoa(number, 8), pad_width, pad_char); i++; break; //o
				case 120 : number = va_arg(args, int); outstr(itoa(number, 16), pad_width, pad_char); i++; break; //x
				case 115 : s = va_arg(args, char*); outstr(s, pad_width, pad_char); i++; break; //s
				case 37 : putchar(37); i++; break;//%
			}	
		}
	}
	va_end(args);
}
////////////////////////////////////////////////////////////////////
//end of file kprintf.c
