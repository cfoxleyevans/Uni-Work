/************************************************************************
 *                                                                      *
 * file: msg.c                                                          *
 *                                                                      *
 * Module implementing message passing IPC for threads.                 *
 *                                                                      *
 ************************************************************************/

#include <stdio.h>
#include "thread.h"
#include "msg.h"
#include <stdlib.h> /* 2012 */
#include <string.h> //for the use of memcopy

/************************************************************************
 *  INTERFACE: create_chan()                                            *
 *                                                                      *
 * Make a channel and return a pointer to it. Return (Chan *)0 on       * 
 * failure. A channel is a struct which we allocate using malloc().     *
 * Internally, the channel consists of two semaphores which are made    * 
 * using create_sem(). We initalise the data field of the channel to 0. *
 * Failure occurs if either the malloc() fails, or we fail to create    *
 * one of both of the semaphores.                                       *
 ************************************************************************/
Chan * 
create_chan()
{
	//malloc new chan
	Chan *tempChan = (Chan *)malloc(sizeof(Chan));
	
	//if new chan has null pointer print error and return null
	if(tempChan == NULL)
	{
		printf("Channel not created malloc failed\n");
		return (Chan *)NULL;
	}

	//attempt to init sems
	tempChan->s1 = create_sem(0);
	tempChan->s2 = create_sem(0);

	//if the new sems have null pointers print error and return null
	if (tempChan->s1 == NULL || tempChan->s2 == NULL)
	{
		destroy_sem(tempChan->s1);
		destroy_sem(tempChan->s2);
		printf("Channel not created sem creation failed\n");
		return (Chan *)NULL;
	}

	//init data
	tempChan->data = 0;

	//return the new channel
	return tempChan;
}

/************************************************************************
 *  INTERFACE: destroy_chan()                                           *
 *                                                                      *
 * Destroy a channel. Should only be allowed to succeed if we are able  *
 * to successfully destroy the two semaphores. Must also handle the     *
 * where we can destroy one semaphore but not the other!                * 
 * Return -1 on failure; and 1 on success.                              *
 ************************************************************************/
int 
destroy_chan(Chan *c)
{

	//take copys of both of the chans sems
	 Sem *tempS1, *tempS2; 
	 (Sem * ) memcpy(c->s1, tempS1, sizeof(Sem));
	 (Sem * ) memcpy(c->s2, tempS2, sizeof(Sem));

	//try and destroy both of the chans sems
	if( destroy_sem( (Sem *) &c->s1) == 1 && destroy_sem( (Sem *) &c->s2) == 1 )
	{
		return 1;
	}

	//destroy failed replace both sems
	c->s1 = tempS1;
	c->s2 = tempS2;

	//print error and return fail
	printf("Semaphore not destroyed");
	return -1;
}

/************************************************************************
 *  INTERFACE: send()                                                   *
 *                                                                      *
 * Send a message on a channel.                                         *
 * This will involve, first, attaching the given int 'message' to the   *
 * channel, then telling receive() that we have a message to send       *
 * (by calling V() on one semaphore), and then waiting (by calling P()  * 
 * on the other semaphore) until receive() has taken the message and    *
 * told us we can proceed. We assume that send() cannot fail, so        * 
 * we always return 1 for success.                                      *
 ************************************************************************/
int 
send(Chan *c, int sentdata) 
{ 
	//set the data to send
	c->data = sentdata;

	//send the message
	V(c->s1);

	//wait for msg to be recived
	P(c->s2);

	return 1;
}

/************************************************************************
 *  INTERFACE: receive()                                                *
 *                                                                      *
 * Receive a message on a channel.                                      *
 * This will involve, first, waiting on one semaphore (using P())       *
 * until send() has given us something to receive; then making          * 
 * receivedmessage point at the message in the channel; then            * 
 * telling send() that it may proceed (using V() on the other           *
 * semaphore).                                                          *
 * We assume that receive() always succeeds and thus always return 1.   *
 ************************************************************************/
int 
receive(Chan *c, int *receiveddata)
{
	//wait to recive message
	P(c->s1);

	//get the data from the chan
	*receiveddata = c->data;
	
	//send signal that msg recived
	V(c->s2);

	return 1;
}

/* end file: msg.c */
