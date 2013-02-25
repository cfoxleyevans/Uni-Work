/* file: thread.h -- public interface to threads. */

#ifndef THREAD_DEFINED
#define THREAD_DEFINED

#include "queue.h"
#include <setjmp.h>

#define CPUContext jmp_buf
#define CPUContext_size (sizeof(jmp_buf))
#define context_save setjmp
#define context_restore longjmp

typedef void (*Proctype)(int argc, char *argv[]);

typedef struct thread {
        struct thread *next;    /* for ready, zombie and sem queues */
        int id;          	/* unique id of thread */
        Proctype procedure;     /* procedure the thread will run */
        int argc;               /* args passed to the thread's procedure */
        char **argv;		/* args passed to the thread's procedure */
        CPUContext context;     /* Machine dependent thread context */
} Thread;

int thread_init(void);
int thread_spawn(Proctype procedure, int argc, char *argv[]);
int thread_getid(void);
void thread_yield(void);
void thread_exit(void);

#endif
/* end file: thread.h */

