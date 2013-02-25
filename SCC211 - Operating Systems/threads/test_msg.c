/************************************************************************
 *                                                                      *
 * file test_msg.c							*
 *                                                                      *
 * Simple program to test message passing.				*
 *									*
 ************************************************************************/

#include        <stdlib.h>
#include        <stdio.h>
#include        "thread.h"
#include        "sem.h"
#include        "msg.h"

Chan *chan;
	
void producer(int argc, char *argv[])
{
	int i;
	
	for(i=0; i<100; i++) {
		send(chan, i);
		printf("producer: sending %d to channel\n", i);
	}
	printf("producer done\n");
}

void consumer(int argc, char *argv[])
{
	int i, n;
	
	for(i=0; i<100; i++) {
		receive(chan, &n);
		printf("consumer: received %d from channel\n", n);
	}
	printf("consumer done\n");
}

int main(int argc, char *argv[])
{
	thread_init();

	chan = create_chan();
		
	if (thread_spawn(producer, 0, (char **)0) == -1) {
		printf("thead_spawn() failed 1\n"); 
		exit(1);
	} 
	
	if (thread_spawn(consumer, 0, (char **)0) == -1) {
		printf("thead_spawn() failed 2\n"); 
		exit(1);
	}
	while(1) thread_yield();
	return 0;
}

/* end file: test_msg.c */
