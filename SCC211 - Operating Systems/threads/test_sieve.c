/************************************************************************
 *                                                                      *
 * file sieve.c								*
 *                                                                      *
 * Implementation of Sieve of Eratosthenes. 				*
 *                                                                      *
 ************************************************************************/

#include        <stdio.h>
#include        "thread.h"
#include        "sem.h"
#include        "msg.h"
#include 		"queue.h"

#define NUMBERS_TO_CHECK 250 //numbers to check

Chan* chans[NUMBERS_TO_CHECK];	//array to hold all of the required chans		


//***************************************************************************************//
// Filter function that handels the checking of numbers passed to it on a chanel.
// If the number is possibly prime it is then passed on to another filter for further 
// processing.
// The first number passed to a filter is allways prime so this is printed automatically
//***************************************************************************************//
void Filter(int pid, char *argv[])
{
	int numbers_checked; //number of numbers checkd so far
	int first = 0;	//the first number the filter recived
	int recived = 0;	//data received from the channel
	int new_thread = 0; //flag for another thread
	
	//loop till we have checkd all of the numbers
	for(numbers_checked = 0; numbers_checked < NUMBERS_TO_CHECK - pid; numbers_checked++) 
	{
		receive(chans[pid], &recived); //get data from the filter above
		
		if(recived == -1) //see if we have the last value
			break;
		
		if (numbers_checked == 0) //this is the first number to this filter
		{
			first = recived; //first number is allways prime
			printf("Prime Number: %d\n", first); //print out the prime
		}
		else
		{
			if (recived % first != 0) //is this a poss prime
			{
				if (!new_thread) //if we dont have a thread below
				{
					if (thread_spawn(Filter, pid + 1, NULL) == -1) //create new thread
					{
						printf("Error: Problem creatinthread");
						exit(0);
					}
					new_thread = 1; //we now do have a new thread
				}
				send(chans[pid + 1], recived); //send data to the new thread
			}	
		}
	}
	exit(0);
}

//***************************************************************************************//
// Main method that serves as entry point for execuation.
// the main method creates all of the chanels that i will need and stores them
// in an array.
// The methods then spawns the first thread and starts to send the numbers
// to it on the first channel. 
//***************************************************************************************//
int main(int argc, char *argv[])
{
    thread_init(); //initiates thread system
    
    int chans_created;
	for(chans_created = 0; chans_created < NUMBERS_TO_CHECK; chans_created++) //inint all of the required chans
	{
		chans[chans_created] = create_chan(); 
		if(chans[chans_created] == NULL)
			printf("Error: Problem creating channel\n");
	}
		
	if (thread_spawn(Filter, 0, NULL) == -1) //spawn the first thread to recive the numbers
	{
		printf("Error: Problem creating thread");
		exit(0);
	}
	
	int number;
	for(number = 2; number < NUMBERS_TO_CHECK; number++) //loop and send all of the numbers to the first thread
		send(chans[0], number);
	send(chans[0], -1);
	
	thread_exit();
	exit(0);
}