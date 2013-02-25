/* file: sem.h -- definitions for semaphores. */

#ifndef SEM_DEFINED
#define SEM_DEFINED

/* Include public interface. */
#include "queue.h"
#include "thread.h"

typedef struct semtype {
        int val;
        Queue queue;
} Sem;

Sem *create_sem(int val);
int destroy_sem(Sem *s);
void P(Sem *s);
void V(Sem *s);

#endif
/* end file: sem.h */
