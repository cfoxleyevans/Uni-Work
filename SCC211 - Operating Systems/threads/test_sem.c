/************************************************************************
 *                                                                      *
 * file test_sem.c							*
 *                                                                      *
 ************************************************************************/

#include        <stdlib.h>
#include        <stdio.h>
#include        "thread.h"
#include        "sem.h"
#define 	SIZE 10

Sem *space_avail, *item_avail, *mutex;
int in = 0;
int out = 0;
int buffer[SIZE];
	
void producer(int argc, char *argv[])
{
	int i;
	
	for(i=0; i<100; i++) {
		P(space_avail);
		P(mutex); 
		printf("producer: putting %d in slot %d\n", i, in %SIZE);
		buffer[in++ % SIZE] = i;
		V(mutex); 	
		V(item_avail);
	}
	printf("producer done\n");
}

void consumer(int argc, char *argv[])
{
	int i, n;
	
	for(i=0; i<100; i++) {
		P(item_avail);
		P(mutex); 
		n = buffer[out++ % SIZE];
		printf("consumer: got %d from slot %d\n", n, (out-1)%SIZE);
		V(mutex); 
		V(space_avail);
	}
	printf("consumer done\n");
}

int main(int argc, char *argv[])
{
		thread_init();

		if ((item_avail = create_sem(0)) == (Sem *)0) {
			printf("create_sem() failed\n"); 
			exit(1);
		} 
		if ((space_avail = create_sem(SIZE)) == (Sem *)0) {
			printf("create_sem() failed\n"); 
			exit(1);
		} 
		if ((mutex = create_sem(1)) == (Sem *)0) { 
			printf("create_sem() failed\n"); 
			exit(1);
		} 

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

/* end file: test_sem.c */
