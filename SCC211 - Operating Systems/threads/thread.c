/************************************************************************
 *                                                                      *
 * file: thread.c                                                       *
 *                                                                      *
 * Module implementing non-preemptive user-level threads.               *
 * All 'threads' share the same stack - they just use different areas	*
 * of it. setjmp() and longjmp() are used to jump between different	*
 * threads (stack areas).						*
 * 									*
 ************************************************************************/

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "thread.h"

#define STACK_SIZE 2048

/* Global pointer to currently executing thread.
 */
Thread *current_thread  = (Thread *)0;

/* The two queues:
 * 1) ready_queue: threads that are runnable.
 * 2) recycle_queue: threads that have called thread_exit() and
 *    await garbage collection.
 */
Queue ready_queue;
Queue recycle_queue;

/* The context frozen in thread_init() which
 * all new threads jump back to in preparation
 * for starting their execution.
 */
CPUContext start_context;

int new_thread_launchpad();
Thread *schedule(void);
void sentinel_thread(int argc, char *argv[]);
int thread_block_and_switch(Queue *q);

/************************************************************************
 * INTERFACE: thread_init()                                             *
 *                                                                      *
 * Procedure to initialise thread system. Also converts original        *
 * caller -- i.e. an OS process -- into a thread with id = 0.    	*
 * Also creates a 'sentinel' thread which will have id=1.		*
 ************************************************************************/
int
thread_init()
{
	char space_for_main_thread[STACK_SIZE]; 
	/* The above allocates space space for the
 	 * stack area of the 'original thread'.
 	 */

	/* prevent array being optimised out of existence */
	space_for_main_thread[STACK_SIZE - 1] = 1; 

	/* Freeze the current context -- to be 'returned' to
	 * when new threads are created in thread_spawn().
	 */
	if (!context_save(start_context)) {
		/* First return from context_save() -- do some
		 * initialisation.
		 */
		queue_init(&ready_queue); 
		queue_init(&recycle_queue);

		/* Set up pseudo descriptor for current 'thread' (i.e.
		 * the original process thread).
		 */
		current_thread = (Thread *)malloc(sizeof(Thread));
		current_thread->procedure = (Proctype)0;
		current_thread->id = 0;
		current_thread->argc = 0;
		current_thread->argv = (char **)0;
		current_thread->next = (Thread *)0;
		/* 'CPUContext' of this thread is the original OS
		 * process' stack area so no need to mess with it.
		 */

		/* Create a sentinel thread that is always there
		 * for the scheduler to find. 
		 */
		thread_spawn(sentinel_thread, 0, (char **)0);
		return 1;
	} else {
		/* Second return happens when a new thread, which has 
		 * recently been created and put on ready_queue
		 * by thread_spawn(), is resumed by the scheduler in 
		 * thread_block_and_switch(). 
		 */
		new_thread_launchpad();
		/*NOTREACHED*/
	}
	return 1;
}

/************************************************************************
 * INTERFACE: thread_spawn()                                            *
 *                                                                      *
 * Create a new thread.                                                 *
 ************************************************************************/
int
thread_spawn(Proctype procedure, int argc, char *argv[])
{
	Thread *new_thread;
	static int next_id_number = 1;

	/* We only allocate a new Thread struct is there isn't one on
	 * the recycle queue. If we use one from the recycle queue we
	 * reuse its old id (and therefore its old stack area).
	 */
	if ((new_thread = (Thread *)queue_get(&recycle_queue)) == (Thread *)0) {
		new_thread = (Thread *)malloc(sizeof(Thread));
		new_thread->id = next_id_number++;
	}
	new_thread->procedure = procedure;
	new_thread->argc = argc;
	new_thread->argv = argv;
	new_thread->next = (Thread *)0;
	/* Set up its context so that when scheduled the new thread will 
 	 * jump back to thread_init() where it will
 	 * pick up its stack area and call its procedure.
 	 */
	memcpy(new_thread->context, start_context, CPUContext_size);

	/* Put the new thread on the ready queue (it will resume
	 * in thread_init() when scheduled).
	 */
	queue_put(&ready_queue, (Qitem *)new_thread);

	/* Force a schedule. */
	thread_yield();
	return 1;
}

/************************************************************************
 *  INTERFACE: thread_getid()                                           *
 *                                                                      *
 * Return integer identifier of current thread.                         *
 ************************************************************************/
int
thread_getid()
{
	thread_yield();
	return current_thread->id;
}

/************************************************************************
 *  INTERFACE: thread_yield()                                           *
 *                                                                      *
 * Deschedule caller and put on ready_queue. thread_block_and_switch()  *
 * will return when the calling thread is resumed.                      *
 ************************************************************************/
void
thread_yield()
{
	thread_block_and_switch(&ready_queue);
}

/************************************************************************
 *  INTERFACE: thread_exit()                                            *
 *                                                                      *
 * Add the descriptor to the recycle queue and do a reschedule.         *
 * Note that this routine NEVER RETURNS.                                *
 ************************************************************************/
void
thread_exit()
{
	thread_block_and_switch(&recycle_queue);
	/*NOTREACHED*/
}

/************************************************************************
 * thread_block_and_switch()						*
 *                                                                      *
 * Freeze context of calling thread (current_thread) and put it         *
 * on the specified queue for later. Then call scheduler to             *
 * get next thread and run it (there will always be a 'next thread'     *
 * due to the presence of the sentinel thread).                         *
 ************************************************************************/
int thread_block_and_switch(q)
        Queue *q;
{
    	if (!context_save(current_thread->context)) {
		/* First return: we are descheduling ourselves... */
		queue_put(q, (Qitem *)current_thread);
		current_thread = schedule();
		context_restore(current_thread->context, 1);
		/*NOTREACHED*/
	} else {
		/* Second return: just been resumed by the scheduler.
		 */
	}
	return 1;
}


/************************************************************************
 *                              PRIVATE ROUTINES                        *
 ************************************************************************/

/************************************************************************
 * new_thread_launchpad()                                               *
 *                                                                      *
 * Launch pad for a new thread. Called from thread_spawn()->		*
 * thread_init() in a context from the very start of execution		*
 * i.e. that resulting from the original call of context_save() in 	*
 * thread_init(). We allocate an array on the stack at an appropriate 	*
 * offset from the current position in this ancestor context so that 	*
 * the new thread (which has been set up as current_thread in 		*
 * thread_spawn()) starts off executing its procedure in its own stack 	*
 * area. We calculate the offset of the required stack area by 		*
 * multiplying the id of the thread by the size of a stack area. 	*
 ************************************************************************/
int new_thread_launchpad()
{
	char space_for_threads[current_thread->id * STACK_SIZE];
	
 	/* assign a value to location to prevent the		
	 * compiler optimising the array away						
	 */
	space_for_threads[(current_thread->id * STACK_SIZE) - 1] = 1; 

	/* call the new thread's procedure in its own stack area */
	(*(current_thread->procedure)) (current_thread->argc, 
					current_thread->argv);
	thread_exit();
	/*NOTREACHED*/
}

/************************************************************************
 * sentinel_thread()                                                    *
 *                                                                      *
 * Procedure to be run by a sentinel thread there to ensure that the    *
 * scheduler always has something to schedule!                          *
 ************************************************************************/
void
sentinel_thread(int argc, char *argv[])
{
	while(1) thread_yield();
}

/************************************************************************
 * schedule()                                                           *
 *                                                                      *
 * Apply a scheduling policy to remove and return the chosen thread     *
 * from off the ready queue.                                            *
 * Assumes there is always something on ready_queue(!) but this is a	*
 * safe assumption as the sentinel thread is always on ready_queue when *
 * someone else calls schedule(). And sentinel_thread only ever calls	*
 * thread_yield(), which puts it on ready_queue before calling this	*
 * routine (via thread_yield()).					*
 ************************************************************************/
Thread *
schedule()
{
        Thread *t;

        t = (Thread *)queue_get(&ready_queue);
	if (!queue_empty(&ready_queue)) {
		/* there remains another thread on the queue */
		if (t->id == 1) {
			/* the thread we pulled off is the sentinel thread */
			/* put the sentinel back and get another instead */
                	queue_put(&ready_queue, (Qitem *)t);
			t = (Thread *)queue_get(&ready_queue);
		}
	}
	return t;
}


/* end file: thread.c */

