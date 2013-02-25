CC=gcc -g -I. -ggdb   
OBJS=thread.o queue.o sem.o msg.o 

all: test_concept test_sieve test_queue test_queue_menu test_sem test_msg

test_concept: test_concept.o 
	$(CC) -o test_concept test_concept.o 

test_sieve: test_sieve.o libthread.a
	$(CC) -o test_sieve test_sieve.o -L. -lthread
	
test_queue: test_queue.o libthread.a
	$(CC) -o test_queue test_queue.o -L. -lthread
	
test_queue_menu: test_queue_menu.o queue.o 
	$(CC) -o test_queue_menu test_queue_menu.o queue.o 
	
test_sem: test_sem.o libthread.a
	$(CC) -o test_sem test_sem.o -L. -lthread
	
test_msg: test_msg.o libthread.a
	$(CC) -o test_msg test_msg.o -L. -lthread
	
libthread.a: $(OBJS)
	ar rv libthread.a  $(OBJS)
	ranlib libthread.a

thread.o: thread.c thread.h queue.h 
	$(CC) -c thread.c

queue.o: queue.c queue.h 
	$(CC) -c queue.c

sem.o: sem.c sem.h 
	$(CC) -c sem.c

msg.o: msg.c msg.h 
	$(CC) -c msg.c

test_queue_menu.o: test_queue_menu.c 
	$(CC) -c test_queue_menu.c

clean:
	rm *.o 
	
wipe:
	rm *.o libthread.a test_concept test_sieve test_queue test_queue_menu test_sem test_msg
