/************************************************************************
 *                                                                      *
 * file: queue.c                                                      	*
 *                                                                      *
 * Module implementing queue manipulation functions. The module can     *
 * hold structures of any type as long as the user of the module        *
 * does the appropriate casting -- all it assumes is that the first     *
 * member of the structure is a pointer, 'next', to a structure.        *
 *                                                                      *
 ************************************************************************/

#include "queue.h"
#include <stdio.h>

/************************************************************************
 * queue_init()                                                         *
 *                                                                      *
 * Initialise a Queue struct.                                           *
 * We assume this will never fail - so always return 1 for success.     *
 ************************************************************************/
int
queue_init(Queue *q)
{
	//init the front and back the null pointers to Qitem
	q->front = (Qitem *) NULL;
	q->back = (Qitem *) NULL;
	return 1;
}

/************************************************************************
 * queue_empty()                                                        *
 *                                                                      * 
 * Return TRUE (i.e. non-zero) if queue is empty; else FALSE (i.e. 0). *
 ************************************************************************/
int
queue_empty(Queue *q)
{
	//if the front of thw queue points to a null Qitem reurn 1 for empty
	if (q->front == (Qitem *) NULL)
		return 1;
	else
        return 0;
}

/************************************************************************
 * queue_put()                                                          *
 *                                                                      *
 * Add a new item descriptor to the back of the specified queue.        *
 * Make sure the next pointer of the new item is 0!                     *
 * We assume that given Qitem can be used directly by us and does not   *
 * need to be copied. We assume this will never fail so always return   *
 * 1 for success.                                                       *
 ************************************************************************/
int
queue_put(Queue *q, Qitem *new_item)
{
	//make the next field for the new item point to a null Qitem
	new_item->next = (Qitem *) NULL;
	
	//if the queue is empty point the front at the new item
	if(queue_empty(q))
	{
		q->front = new_item;
	}
	//make the current backs next pointer point to the new item
	else
	{
		q->back->next = new_item;
	}
	
	//make the back point to the new item
	q->back = new_item;
	return 1;
}

/************************************************************************
 * queue_get()                                                          *
 *                                                                      *
 * Remove the front Qitem struct from the specified queue and return    *
 * a pointer to it. When we remove and return the struct we pass all    * 
 * future responsibility for it to our caller. Return (Qitem *)0 if     * 
 * queue is empty.                                                      *
 ************************************************************************/
Qitem *
queue_get(Queue *q)
{
	//get a temp ref to the front will be null if nothing there
	Qitem* temp = q->front;
	
	//make sure we dont have an empty queue
	if(!queue_empty(q))
		//set the front to the new front
		q->front = q->front->next;
	
	//return the old front	
	return temp;
}

/* end file: queue.c */
