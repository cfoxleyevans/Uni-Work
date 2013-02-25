/************************************************************************
 *                                                                      *
 * file test_queue.c							*
 *                                                                      *
 * Simple test program for non preemptive user level threads package	*
 * - mainly to conform correctness of queue module.           		*
 *                                                                      *
 ************************************************************************/


 #include        <stdio.h>
 #include        "thread.h"
 #include        "sem.h"
 #include        "msg.h"

void thread_func(int argc, char *argv[])
{
	while(1) {
		printf("This is thread %d\n", thread_getid());
		thread_yield();
	}
}


main(int argc, char *arcv[])
{
        int i;

        thread_init();
        
        for(i=0; i<100; i++) {
        	thread_spawn(thread_func, i, (char **)0);
        }
	thread_exit();  
		
}

/* end file: test_queue.c */
