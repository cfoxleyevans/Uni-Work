/* file: queue.h -- definitions for queue manipulation routines. */

#ifndef QUEUE_DEFINED
#define QUEUE_DEFINED

typedef struct qitem {
        struct qitem *next; /* this must always be first; can cast to any struct with this first */
} Qitem;

typedef struct queue {
        Qitem *front, *back;
} Queue;

int queue_init(Queue *q);
int queue_empty(Queue *q);
int queue_put(Queue *q, Qitem *new_item);
Qitem *queue_get(Queue *q);

#endif
/* end file: queue.h */

