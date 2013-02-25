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
#define PING_COUNT 10

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

int main(int argc, char* argv[]) 
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
   

   struct hostent *he;
   struct in_addr **addr_list;
   struct sockaddr_in my_address; //hold the address details for my machine
   struct sockaddr_in dest_address; //hold the address details for the dest machine
   struct sockaddr_storage recv_address;
   socklen_t address_length; //the length of an address
   

   float rtt; //hold the round trip time
   

   //ping specific
   int packets_sent = 0;
   int packets_recived = 0;
   int total_rtt = 0;
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

   //start of the ping loop
   int i;
   for(i = 0; i < PING_COUNT; i++)
   {
      if (sequence != 0) //if this isnt the first packet then pasue 
            sleep(1);

      iphdr = (struct iphdr *)buffer; //make iphdr point at the empty buffer
         
      memset(&dest_address, 0, sizeof(struct sockaddr_in)); //zero out the dest address struct
      dest_address.sin_addr.s_addr = inet_addr(HOSTADDR); //set the dest address to be HOSTADDR
      dest_address.sin_family = AF_INET; //use IPv4
   
      memset(buffer, 0 , sizeof(buffer)); //zero out the buffer
      icmphdr = (struct icmphdr *)buffer; //cast the empty buffer to an icmphdr
      icmphdr->type = ECHO_REQ; //sets the packet type
      icmphdr->id = htons(getpid() & 0xffff); //sets the packet id
      icmphdr->seqNum = htons(sequence++); //sets the requests sequence number
      
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

      packets_sent++;

      timeout.tv_sec  = 1; //timeout options for the select
      timeout.tv_usec = 0;
            
      FD_ZERO(&rdfds); //clear out the discriptor set
      FD_SET (socket_fd, &rdfds); //add the socket to the discriptor set

      ready = select (socket_fd + 1, &rdfds, NULL, NULL, &timeout);
      if (ready < 0) 
      {
         printf("Error: file discriptor set error\n");
      }

      recvfrom (socket_fd, buffer, sizeof(buffer), 0, (struct sockaddr *) &recv_address, &address_length);

      packets_recived++;

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

      total_rtt += rtt;


      printf ("%ld bytes from %s: icmp_req=%d ttl=%d time=%0.1f ms\n",
      length,
      iptos(ntohl(iphdr->srcip)),
      ntohs(icmphdr->seqNum),
      iphdr->ttl,
      ((float)rtt) / 10);




   }

   printf("Packets sent:%i Packets Recived:%i\nPackets Retained:%0.1f%% Total Round Trip:%i ms\n", 
      packets_sent, 
      packets_recived, 
      (float)(PING_COUNT/packets_recived) * 100, total_rtt);
 

}
