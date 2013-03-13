////////////////////////////////////////////////////
//Name : rip.c
//Author : Chris Foxley-Evans
//Date : 11/3/2013
//Purpose : implements the rip protocol
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Includes
////////////////////////////////////////////////////
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>

#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

#include "ip.h" //the ip header implementation
#include "icmp.h" //the icmp header implementation
#include "rip.h" //the rip header implementation
#include "ll.h" //the linked linked list
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Constants
////////////////////////////////////////////////////
#define BUFF_SIZE 1024 //size of the message buffer
#define RIP_PORT 520 //the RIP port
//#define ADDR "10.32.92.24"
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Creates a udp socket and returns the fd
////////////////////////////////////////////////////
int create_udp_socket(){

	int fd = socket(AF_INET, SOCK_DGRAM, 0);
	if (fd < 0) {
		perror("Error: problem creating socket");
		exit(1);
	}

	return fd; 
}
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Creates a multicast socket and returns the fd
////////////////////////////////////////////////////
int create_multicast_socket(){

	int fd = socket(AF_INET, SOCK_DGRAM, 0);
	if(fd < 0){
		perror("Error: problem creating socket");
		exit(1);
	}

	return fd;
}
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Calculates the packet checksum
////////////////////////////////////////////////////
uint16_t sum (uint16_t initial, void * buffer, int bytes){
   
   uint32_t   total;
   uint16_t * ptr;
   int        words;

   total = initial;
   ptr   = (uint16_t *) buffer;
   words = (bytes + 1) / 2; // +1 & truncation on / handles any odd byte at end

   while (words--) total += *ptr++;

   while (total & 0xffff0000) total = (total >> 16) + (total & 0xffff);

   return ~(uint16_t) total;
}
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Sendrip request message
////////////////////////////////////////////////////
void send_rip_v1_request(int skt, struct sockaddr_in dest){

	char buffer[BUFF_SIZE]; 
	struct rip_msg *msg;
	
	//write the message into the buffer
	memset(&buffer, 0, sizeof(buffer));
	msg = (struct rip_msg *)buffer;

	//construct rip message
	msg->command = (unsigned char)1;
	msg->version = (unsigned char)1;
	msg->zero= (unsigned short)0;

	//first entry
	//msg->entries[0].rip1.addr_family = AF_INET;
	//msg->entries[0].rip1.zero = 0;
	//inet_pton(AF_INET, "7.15.0.0", &(msg->entries[0].rip1.v4addr));
	//msg->entries[0].rip1.metric = 16;

	int update_length = sizeof(struct rip_msg) + 1 * sizeof(struct rip1ent);

	bind(skt,(struct sockaddr *)&dest, sizeof(dest));

	//send the request
	sendto (skt, buffer, update_length, 0,
		(struct sockaddr *)&dest, sizeof(dest));
}
///////////////////////////////////////////////////

///////////////////////////////////////////////////
//The main program function
////////////////////////////////////////////////////
int main(int argc, char const **argv){

	int skt = create_udp_socket();
	struct sockaddr_in dest; //destination address
	
	//set up the dest address
	memset(&dest, 0, sizeof(dest));
	inet_pton(AF_INET, "10.37.211.102", &(dest.sin_addr));
	dest.sin_family = AF_INET;
	dest.sin_port = htons(RIP_PORT);

	send_rip_v1_request(skt, dest);

	//recive response with the tables

	return 0;
}
////////////////////////////////////////////////////
//end of file rip.c