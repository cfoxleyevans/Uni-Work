/* file: msg.h -- public interface to send/ recv functions. */

#ifndef MSG_DEFINED
#define MSG_DEFINED

#include "sem.h"
#include "thread.h"

typedef struct {
        Sem *s1;
        Sem *s2;
        int data;
} Chan;

Chan *create_chan(void);
int destroy_chan(Chan *chan);
int send(Chan *chan, int sentdata);
int receive(Chan *chan, int *receiveddata);

#endif
/* end file: msg.h */
