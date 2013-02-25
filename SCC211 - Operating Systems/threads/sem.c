/************************************************************************
 *                                                                      *
 * file: sem.c                                                          *
 *                                                                      *
 * Module implementing semaphores.                                      *
 * All routines whould call thread_yield() where possible to encourage  *
 * fair execution.                                                      *
 *                                                                      *
 ************************************************************************/

#include "sem.h"
#include <stdlib.h> /* 2012 */
#include <stdio.h> 

/* External variables defined in thread.c.
 */
extern Queue ready_queue;
extern Thread current_thread;
extern int thread_block_and_switch(Queue *q);

/************************************************************************
 *  INTERFACE: create_sem()                                             *
 *                                                                      * 
 * Make and return a semaphore (i.e. return a pointer to a Sem struct). *
 * This will involve allocating a Sem structure using malloc(),         * 
 * creating/initialising a queue for threads blocked on the             *
 * semaphore and also initialising the semaphore to the value specified *
 * by our caller. This function can only fail if malloc() fails - in    *
 * this case we return (Sem *)0.                                        *
 ************************************************************************/
Sem *create_sem(int val)
{
	//malloc new sem
	Sem *tempSem = (Sem *)malloc(sizeof(Sem));
	
	//if new sem null print error and return null pointer
	if(tempSem == NULL)
	{
		printf("Semaphore not created\n");
		return (Sem *)NULL;
	}

	// make sure the  val is more then 0
	//init the val of the sem and the sems run queue
	if(val >= 0)
	{
		tempSem->val = val;
		queue_init(&tempSem->queue);
		return tempSem;
	}

	return (Sem *)NULL;
}

/************************************************************************
 *  INTERFACE: destroy_sem()                                            *
 *                                                                      *
 * Destroy a semaphore.                                                 *
 * Forbid destruction (return -1) if the semaphore has waiting threads	*
 ************************************************************************/
int 
destroy_sem(Sem *s)
{
	//if the sem has an empty queue then free it
	if(queue_empty(&s->queue))
	{
		free(s);
		return 1;	
	}
	//return -1 stuff still in the queue
	return -1;
}

/************************************************************************
 *  INTERFACE: P()                                                      *
 *                                                                      *
 * Do a P on a semaphore(!)                                             *
 * That is, decrement the value of the semaphore and if this takes      *
 * its value below 0 block the calling thread on the semaphore's queue  *
 * using thread_block_and_switch().                                     *
 ************************************************************************/
void 
P(Sem *s)
{
	//decrement the sems val
	s->val--;	
	//if val less than 0
	if(s->val < 0)
	{
		thread_block_and_switch(&s->queue);
	}
	thread_yield();

}

/************************************************************************
 *  INTERFACE: V()                                                      *
 *                                                                      *
 * Do a V on a semaphore.                                               *
 * That is, increment the value of the semaphore. If there were threads *
 * blocked on the semaphore (i.e. the value after incrementing the	*
 * semaphore is <= 0), we get the first thread from the            	*
 * semaphore's queue and put it on the thread package's ready queue     *
 * using queue_put().                                                   *
 ************************************************************************/
void 
V(Sem *s)
{
	s->val++;
	if(s->val <= 0)
	{
		queue_put(&ready_queue, queue_get(&s->queue));
	}
	thread_yield();
}

/* end file: sem.c */
