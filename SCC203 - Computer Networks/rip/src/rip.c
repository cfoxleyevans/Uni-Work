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

//#include "ip.h" //the ip header implementation
#include "icmp.h" //the icmp header implementation
#include "rip.h" //the rip header implementation
#include "ll.h" //the linked linked list
#include "table_entry.h" //the table entry header
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Constants
////////////////////////////////////////////////////
#define BUFF_SIZE 1024 //size of the message buffer
#define RIP_PORT 520 //the RIP port
//#define ADDR "10.32.92.24"
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Convert ip address to a string for printing
////////////////////////////////////////////////////
char* iptos (uint32_t ipaddr) {
	static char ips[16];
	sprintf(ips, "%d.%d.%d.%d", (ipaddr >> 24), (ipaddr >> 16) & 0xff, 
		(ipaddr >> 8) & 0xff,(ipaddr) & 0xff );
   return ips;
}
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

	int optval = 1;
	
	if (setsockopt (fd, SOL_SOCKET, SO_BROADCAST,
		(void *)&optval, (socklen_t)sizeof(optval))){
			perror ("setsockopt( ADD BROADCAST )");
	}

	optval = 1;
	if (setsockopt (fd, SOL_SOCKET, SO_REUSEADDR,
		(void *)&optval, (socklen_t)sizeof(optval))) {
			perror ("setsockopt( REUSE ADDR )");
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
//Send rip v2 request message
////////////////////////////////////////////////////
void send_rip_v2_request(int skt, struct sockaddr_in dest){

	char buffer[BUFF_SIZE]; 
	struct rip_msg *msg;
	
	//write the message into the buffer
	memset(&buffer, 0, sizeof(buffer));
	msg = (struct rip_msg *)buffer;

	//construct rip message
	msg->command = (unsigned char)1;
	msg->version = (unsigned char)2;
	msg->zero= (unsigned short)0;

	//first entry
	msg->entries[0].rip2.addr_family = htons(0);
	msg->entries[0].rip2.route_tag = htons(0);
	//inet_pton(AF_INET, "0.0.0.0", &(msg->entries[0].rip2.ip_addr));
	//inet_pton(AF_INET, "0.0.0.0", &(msg->entries[0].rip2.ip_addr));
	//inet_pton(AF_INET, "0.0.0.0", &(msg->entries[0].rip2.next_hop));

	msg->entries[0].rip2.ip_addr = inet_addr("0.0.0.0");
	msg->entries[0].rip2.ip_addr = inet_addr("0.0.0.0");
	msg->entries[0].rip2.next_hop = inet_addr("0.0.0.0");

	msg->entries[0].rip2.metric = htonl(16);

	int update_length = sizeof(struct rip_msg) + 1 * sizeof(struct rip1ent);
	
	//send the request
	sendto (skt, buffer, update_length, 0,
		(struct sockaddr *)&dest, sizeof(dest));
}
///////////////////////////////////////////////////

////////////////////////////////////////////////////
//Send rip v1 request message
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
	
	msg->entries[0].rip1.addr_family = htons(0);
	msg->entries[0].rip1.zero = 0;
	inet_pton(AF_INET, "0.0.0.0", &(msg->entries[0].rip1.v4addr));
	msg->entries[0].rip1.metric = htonl(16);

	int update_length = sizeof(struct rip_msg) + 1 * sizeof(struct rip1ent);

	//send the request
	sendto (skt, buffer, update_length, 0,
		(struct sockaddr *)&dest, sizeof(dest));
}
///////////////////////////////////////////////////

////////////////////////////////////////////////////
//Catch rip v2 response message
////////////////////////////////////////////////////
void rec_rip_v2_respones(int skt, list *table){

	char buffer[BUFF_SIZE];
	struct sockaddr_in from;
	socklen_t from_length = sizeof(from);

	int received_bytes = recvfrom(skt, (char*)buffer, BUFF_SIZE,
      	0, (struct sockaddr*)&from, &from_length );
	
	if(received_bytes > 0){
		//read buffer as rip message	
		struct rip_msg *msg;
    	msg = (struct rip_msg *)buffer;

    	printf("Got a V2 Packet from %s\n", iptos(htonl(from.sin_addr.s_addr)));

    	//calculate the number of routes
		int entrys = (received_bytes - 2) / 20;
		
		//put entry into my routing table
    	int i = 0;
    	for(; i < entrys; i++){
    		table_entry *temp = malloc(sizeof(struct table_entry));
    		
    		temp->network = msg->entries[i].rip2.ip_addr;
    		temp->netmask = msg->entries[i].rip2.subnet_mask;
    		//printf("%s\n",iptos(ntohl(msg->entries[i].rip2.subnet_mask)));
    		temp->gateway = msg->entries[i].rip2.next_hop;
    		temp->metric = msg->entries[i].rip2.metric;
    		temp->ttl = 255;

    		if(list_contains_route_v2(table, temp->network, temp->netmask));
    		else
    			list_insert(table, temp);
    	}
    }
    list_print_rip_table(table);
}
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Catch rip v1 response message
////////////////////////////////////////////////////
void rec_rip_v1_response(int skt, list *table){

	char buffer[BUFF_SIZE];
	struct sockaddr_in from;
	socklen_t from_length = sizeof(from);

	int received_bytes = recvfrom(skt, (char*)buffer, BUFF_SIZE,
      	0, (struct sockaddr*)&from, &from_length );
	
	if(received_bytes > 0){
		//read buffer as rip message
		struct rip_msg *msg;
    	msg = (struct rip_msg *)buffer;

    	printf("Got a V1 Packet from %s\n", iptos(htonl(from.sin_addr.s_addr)));

    	//calculate the number of routes
		int entrys = (received_bytes - 2) / 20;
	
		//put entry into my routing table
    	int i = 0;
    	for(; i < entrys; i++){
    		table_entry *temp = malloc(sizeof(struct table_entry));
    		
    		temp->network = msg->entries[i].rip1.v4addr;
    		temp->metric = msg->entries[i].rip1.metric;

			//see if i allredy have the route in the table
    		if(list_contains_route_v1(table, temp->network));
    		else
    			list_insert(table, temp);
    	}
	}
	list_print_rip_table(table);
}
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Send out rip v2 announcment
////////////////////////////////////////////////////
void send_rip_v2_update(int skt, struct sockaddr_in dest){

	char buffer[BUFF_SIZE]; 
	struct rip_msg *msg;
	
	//write the message into the buffer
	memset(&buffer, 0, sizeof(buffer));
	msg = (struct rip_msg *)buffer;

	//construct rip message
	msg->command = (unsigned char)2;
	msg->version = (unsigned char)2;
	msg->zero= (unsigned short)0;

	//update entry
	msg->entries[0].rip2.addr_family = htons(AF_INET);
	msg->entries[0].rip2.route_tag = htons(0);
	inet_pton(AF_INET, "7.242.2.1", &(msg->entries[0].rip2.ip_addr));
	inet_pton(AF_INET, "255.255.0.0", &(msg->entries[0].rip2.subnet_mask));
	inet_pton(AF_INET, "0.0.0.0", &(msg->entries[0].rip2.next_hop));
	msg->entries[0].rip2.metric = htonl(4);

	//update entry 
	
	msg->entries[1].rip2.addr_family = htons(AF_INET);
	msg->entries[1].rip2.route_tag = htons(0);
	inet_pton(AF_INET, "7.242.2.128", &(msg->entries[1].rip2.ip_addr));
	inet_pton(AF_INET, "255.255.0.0", &(msg->entries[1].rip2.subnet_mask));
	inet_pton(AF_INET, "0.0.0.0", &(msg->entries[1].rip2.next_hop));
	msg->entries[1].rip2.metric = htonl(1);
	
	int update_length = sizeof(struct rip_msg) + 2 * sizeof(struct rip1ent);

	//send the request
	sendto (skt, buffer, update_length, 0,
		(struct sockaddr *)&dest, sizeof(dest));
}

////////////////////////////////////////////////////
//CDesd out rip v1 announcment
////////////////////////////////////////////////////
void send_rip_v1_update(int skt, struct sockaddr_in dest){
	char buffer[BUFF_SIZE]; 
	struct rip_msg *msg;
	
	//write the message into the buffer
	memset(&buffer, 0, sizeof(buffer));
	msg = (struct rip_msg *)buffer;

	//construct rip message
	msg->command = (unsigned char)2;
	msg->version = (unsigned char)1;
	msg->zero= (unsigned short)0;

	//update entry
	msg->entries[0].rip1.addr_family = htons(AF_INET);
	msg->entries[0].rip1.zero = 0;
	inet_pton(AF_INET, "7.242.1.1", &(msg->entries[0].rip1.v4addr));
	msg->entries[0].rip1.metric = htonl(2);

	//update entry
	msg->entries[1].rip1.addr_family = htons(AF_INET);
	msg->entries[1].rip1.zero = 0;
	inet_pton(AF_INET, "7.242.1.128", &(msg->entries[1].rip1.v4addr));
	msg->entries[1].rip1.metric = htonl(5);


	int update_length = sizeof(struct rip_msg) + 2 * sizeof(struct rip1ent);
	
	//send the request
	sendto (skt, buffer, update_length, 0,
		(struct sockaddr *)&dest, sizeof(dest));
}

///////////////////////////////////////////////////
//The main program function
////////////////////////////////////////////////////
int main(int argc, char const **argv){

	//make sure i am root for some functions
	if(getuid() != 0){
		printf("ERROR : NEED TO BE ROOT\n");
		exit(1);
	}

	//set up routing tables for v1 and v2
	list *table = list_init();
	
	//create sockets for v1 and v2
	int skt = create_udp_socket();
	int mskt = create_multicast_socket();

	struct sockaddr_in v1_dest; //v1_dest address
	struct sockaddr_in v2_dest; //v2_dest address

	//set up the v1_dest address to use the broadcast address
	memset(&v1_dest, 0, sizeof(v1_dest));
	v1_dest.sin_family = AF_INET;
	v1_dest.sin_port = htons(RIP_PORT);
	v1_dest.sin_addr.s_addr = inet_addr("255.255.255.255");
	bind(skt,(struct sockaddr *)&v1_dest, sizeof(v1_dest));

	//set up the v2_dest address to use the multicast address
	memset(&v2_dest, 0, sizeof(v2_dest));
	v2_dest.sin_family= AF_INET;
	v2_dest.sin_port = htons(RIP_PORT);
	v2_dest.sin_addr.s_addr = inet_addr("224.0.0.9");
	bind(mskt,(struct sockaddr *)&v2_dest, sizeof(v2_dest));

	//join the multicast group
	struct ip_mreqn multaddr;
	multaddr.imr_multiaddr.s_addr = inet_addr("224.0.0.9");
	multaddr.imr_address.s_addr = INADDR_ANY;
	multaddr.imr_ifindex = 0;
	if (setsockopt (mskt, IPPROTO_IP, IP_ADD_MEMBERSHIP,
		(void *)&multaddr, sizeof(multaddr))) {
			perror ("setsockopt( JOIN GROUP )");
	}	

	//insert default entry int table
	table_entry *default_entry = malloc(sizeof(table_entry));
	default_entry->network = inet_addr("0.0.0.0");
	default_entry->netmask = inet_addr("0.0.0.0");
	default_entry->gateway = inet_addr("0.0.0.0");
	default_entry->interface = 0;
	default_entry->metric = 0;
	default_entry->ttl = 255;

	list_insert(table, default_entry);


	//send_rip_v2_update(mskt, v2_dest);
	//send_rip_v2_update(skt, v1_dest);

	//program actions
	
	while(1){
		rec_rip_v2_respones(mskt, table);
		rec_rip_v1_response(skt, table);
	}
	
	return 0;
}
////////////////////////////////////////////////////
//end of file rip.c