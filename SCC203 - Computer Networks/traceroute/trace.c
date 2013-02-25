//includes
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

struct timeval timeout; //used in the call to select to set the time out 
struct timeval reciveTime; //the time that we recive the packet
struct timeval sentTime; //the time that we sent the packet set just b4 send()

//constants
#define BUFF_SIZE  1024 //the size of the buffer
#define ICMP       1
#define ECHO_REQ   8
#define ECHO_REPL  0
//#define HOSTADDR   "89.32.148.93" //ip address that i will trace
#define MAX_HOPS   30 //the max amout of hops


/*******************************************************************/
//Function that will create socket and return its file discriptor
/*******************************************************************/
int createRawSocket()
{
	int fd = socket(AF_INET, SOCK_RAW, IPPROTO_ICMP); //create a raw socket 
	if (fd < 0) //make sure that i have no error
	{
		printf("Error: createRawSocket()"); //display erroe
	}

	return fd; //return the nrew sockets fd
}
/*******************************************************************/

/*******************************************************************/
//Function that wil calculate the ip checksum
/*******************************************************************/
uint16_t sum (uint16_t initial, void * buffer, int bytes) 
{
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
/*******************************************************************/

/*******************************************************************/
//Function that will convert ip to string for printing
/*******************************************************************/
char *iptos (uint32_t ipaddr) 
{
   static char ips[16];

   sprintf(ips, "%d.%d.%d.%d", (ipaddr >> 24), (ipaddr >> 16) & 0xff, (ipaddr >> 8) & 0xff, (ipaddr) & 0xff );
   return ips;
}
/*******************************************************************/

int main(int argc , char **argv)
{
	/*******************************************************************/
	//Variables
	/*******************************************************************/
	int socket_fd; //the file discriptor for the socket
	int ready; //used in select to make sure the socket is ready to be read
	fd_set rdfds; //a file discriptor set	
	
   char buffer[BUFF_SIZE]; //the buffer used to hold the packet data
   char *HOSTADDR;

	struct iphdr *iphdr; //the ip header
	struct icmphdr *icmphdr; //the icmp header
	int sequence = 0; //the packets sequence number
	long int length; //length of the packet
	
   	struct hostent *hp;
   	struct hostent *he;
   	struct in_addr **addr_list;
   	struct sockaddr_in my_address; //hold the address details for my machine
	struct sockaddr_in dest_address; //hold the address details for the dest machine
	struct sockaddr_storage recv_address;
	socklen_t address_length; //the length of an address
	
	int ttl = 0; //the current time to live value
	float rtt; //hold the round trip time
	int hops = 1; // the number of hops that i have done
	int timeout_flag = 0; //timeout flag set to x if i have timed out
	int hostname = 0; //i have got sa hostname for this hop
	/*******************************************************************/
	

	/*******************************************************************/
	//Main Program
	/*******************************************************************/
	
	if (getuid() != 0) //make sure that we are root
	{
		printf("Error: Need to be root\n"); //diaply error message
      exit(0);
	}

	//parse cli arguments here	

   	if (argc != 2) //make sure i have enough arguments
   	{
   		printf("Usage: sudo ./trace example.com\n");
      	exit(0);
   	}

  	//parse the cli args
   	he = gethostbyname(argv[1]);
   	if (he != NULL)
   	{
   		addr_list = (struct in_addr **)he->h_addr_list;
   		HOSTADDR = inet_ntoa(*addr_list[0]); //grab the first one out the list
   	}
   	else
   	{
   		printf("Error: %s is not a valid hostname\n", argv[1]);
   		exit(0);
   	}
	
	socket_fd = createRawSocket(); //create the csocket

	address_length = sizeof(struct sockaddr_storage); //set the size of the address

	my_address.sin_family = AF_INET; //use IPv4
	my_address.sin_port = 0; //use any port
	my_address.sin_addr.s_addr = INADDR_ANY; //use the lowest interface ip address
	
	int bind_result = bind(socket_fd, (struct sockaddr *)&my_address, sizeof(struct sockaddr_in)); //try to bind the socket
	if (bind_result < 0) //make sure it worked
	{
		printf("Error: cant bind socket\n"); //display error messge
	}

	//*******************************************************************/
    //start of the trace loop
    /*******************************************************************/
   	for (ttl = 1; ttl < MAX_HOPS; ttl++) //loop for my max hops
   	{
   		if (sequence != 0) //if this isnt the first packet then pasue 
   			;//sleep(1);
   		
   		int ping_count = 0; //keep track of the number of pings for this hop
   		int i; //loop index 
   		for (i  = 0; i < 3; i++) //loop 3 times like the real traceroute
   		{
   			iphdr = (struct iphdr *)buffer; //make iphdr point at the empty buffer
			
			memset(&dest_address, 0, sizeof(struct sockaddr_in)); //zero out the dest address struct
			dest_address.sin_addr.s_addr = inet_addr(HOSTADDR); //set the dest address to be HOSTADDR
			dest_address.sin_family = AF_INET; //use IPv4
	
			memset(buffer, 0 , sizeof(buffer)); //zero out the buffer
			
			icmphdr = (struct icmphdr *)buffer; //cast the empty buffer to an icmphdr
			icmphdr->type = ECHO_REQ; //sets the packet type
			icmphdr->id = htons(getpid() & 0xffff); //sets the packet id
			icmphdr->seqNum = htons(sequence++); //sets the requests sequence number
		
			setsockopt(socket_fd, IPPROTO_IP, IP_TTL, &ttl, address_length); //set the current time to live
   			
   			icmphdr->checksum = sum(0, buffer, address_length); //generate the checksum
   			
   			memset(&sentTime, 0 , sizeof(struct timeval));
   			if (gettimeofday ((struct timeval *)&sentTime, NULL)) //get the time that we are sending the packet
			{
				printf("Error: problem establishing time before sending the request");
			}

			//send the packet
			if (sendto (socket_fd, buffer, address_length, 0, (struct sockaddr *) &dest_address, sizeof(struct sockaddr_in)) <= 0) 
			{
				printf("Error: failed to sent the packet");
			}
	
			timeout.tv_sec  = 1; //timeout options for the select
   			timeout.tv_usec = 0;
   			
   			FD_ZERO(&rdfds); //clear out the discriptor set
   			FD_SET (socket_fd, &rdfds); //add the socket to the discriptor set

   			ready = select (socket_fd + 1, &rdfds, NULL, NULL, &timeout);
			if (ready < 0) //error
			{
   				printf("Error: file discriptor set error\n");
			}
			else if (ready == 0) //the time out has lapsed
			{
				timeout_flag = 1; //this request has timed out set my flag
			}
			else //we can use the socket
			{
				//recive from the socket
				recvfrom (socket_fd, buffer, sizeof(buffer), 0, (struct sockaddr *) &recv_address, &address_length);	
			}

         	length = ntohs(iphdr->length) - iphdr->hdrlen * 4;

   			memset(&reciveTime, 0, sizeof(struct timeval));
   			if (gettimeofday((struct timeval *)&reciveTime, NULL))
   			{   
   				printf("Error: can't establish time of receipt"); //print error message
  			}

			icmphdr = (struct icmphdr *)((uint32_t *)iphdr + iphdr->hdrlen); //find the icmp header

			//calculate the round trip time
   			if ((rtt = (reciveTime.tv_usec - sentTime.tv_usec) / 100) < 0)
      			rtt += 10000;  
   			rtt += (reciveTime.tv_sec - sentTime.tv_sec) * 10000; 
   			
   	   		//grab th host name of the hop
   			hp = gethostbyaddr((const void *)&iphdr->srcip, sizeof(struct in_addr), AF_INET);

			if (hp != NULL) //if the host names not null
   				hostname = 1; //set my flag so i know ive got it

			//see if we have hopped to the address that we are looking for
			if (strcmp(iptos(ntohl(iphdr->srcip)), HOSTADDR) == 0) 	
			{
   				printf ("%i %s (%s)\n", hops, hp->h_name, iptos(ntohl(iphdr->srcip)));
   				exit(0); //we are her so exit
   			}
	
			if (timeout_flag == 0) //if the we didnt time out
   			{
   				if(ping_count == 0 && hostname == 1) //if this is the first ping and we have a host name
   					printf ("%i %s (%s) %0.1f ms ", hops, hp->h_name, iptos(ntohl(iphdr->srcip)), ((float)rtt) / 10);
   				else if (ping_count == 0) // if this is the first ping and we dont have a host name
   					printf ("%i %s (%s) %0.1f ms ", hops, iptos(ntohl(iphdr->srcip)), iptos(ntohl(iphdr->srcip)), ((float)rtt) / 10);
   				else //if the second or third append just the time
   					printf("%0.1f ms ", rtt);
   			}
   			
   			else //if we did time out
   			{
   				if(ping_count == 0) //if this is the first ping
   				printf ("%i * ", hops);
   			
            	else //if the second or third ping
   					printf("* ");
   			}
		
			rtt = 0;
			timeout_flag = 0; //reset the time out flag
			ping_count++; //so we dont print it all again
		}
   	ping_count = 0; //reset the ping count now that we have finished this hop
   	hops++; //increment the amount of hops
   	hostname = 0; //reset the host name flag
   	printf("\n"); //print a blank line
   	}
}
