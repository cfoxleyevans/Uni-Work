#ifndef __ICMP_H__
#define __ICMP_H__

struct icmphdr {
   uint8_t   type;          // See RFC 792
   uint8_t   code;          // See RFC 792
   uint16_t  checksum;      // IP style one's complement checksum
   uint16_t  id;            // Unique identifier for sender - e.g. process id
   uint16_t  seqNum;        // Returned by target device
   uint32_t  data[ ];       // Returned by target device
} __attribute__((__packed__));


char * messages[ ] = {
   "Echo reply",
   "Type 1",
   "Type 2",
   "Destination unreachable",
   "Source quench",
   "Redirect",
   "Type 6",
   "Type 7",
   "Echo request",
   "Router advertisement",
   "Router discovery",
   "Time exceeded",
   "Parameter problem",
   "Timestamp request",
   "Timestamp reply",
   "Information request (obsol.)",
   "Information reply (obsol.)",
   "Address mask request",
   "Address mask reply"
};

#endif // __ICMP_H__