#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <setjmp.h>

jmp_buf trampoline, jb1, jb2;

int thread1()
{
        int count = 0; /* thread specific state */
        while(1) {
                printf("thread 1; count = %d\n", count);
		if (++count == 100) count = 0;
                if (!setjmp(jb1)) {
			/* printf("Step 8, 12, 16, 20, ... (thread 1)\n");*/
			longjmp(jb2, 1);
			/* NOTREACHED */
		} else {
			/* printf("Step 11, 15, 19, 23, ... (thread 1)\n");*/
		}
        }
}

int thread2()
{
        int count = 100; /* thread specific state */
        while(1) {
                printf("thread 2; count = %d\n", count);
		if (--count == 0) count = 100;
                if (!setjmp(jb2)) {
			/* printf("Step 6, 10, 14, 18, ... (thread 2)\n");*/
			longjmp(jb1, 1);
			/* NOTREACHED */
		} else {
			/* printf("Step 9, 13, 17, 21, ... (thread 2)\n");*/
		}
        }
}

int thread2_launchpad()
{
        /* We create a new stack area for thread2 to run in by doing
         * a dummy allocation of space (an array) on the stack.
         * The reason for assigning 1 to the last element of the 
         * array is simply to avoid the compiler optimising away the 
         * array (as it would otherwise never be used).
         */
        char space_for_thread[4096]; space_for_thread[4096-1] = 1;
	/* printf("Step 5\n");*/
        thread2();
}

int main(int argc, char* argv[])
{
	/* printf("Step 1\n");*/
        if (!setjmp(trampoline)) {
                /* First return from trampoline */
		/* printf("Step 2\n");*/
                if (!setjmp(jb1)) {
			/* printf("Step 3\n");*/
                        /* First return from setjmp */
			/* Go to 2nd return of trampoline to start thread2 */
                        longjmp(trampoline, 1); 
			/* NOTREACHED */
                } else {
                        /* We get here when we have been resumed 
                         * by thread2 */
			/* printf("Step 7\n");*/
                        thread1(); /* run thread1 */
                }
        } else {
		/* printf("Step 4\n");*/
                /* Second return of trampoline caused by longjmp() above */
                thread2_launchpad(); /* launchpad for thread2 to 
                                * run in distinct stack area */
        }
}
