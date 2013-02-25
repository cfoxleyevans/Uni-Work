#ifndef __IP_H__
#define __IP_H__

struct iphdr {
   /*
    *   Note: on x86 GCC packing is from 0 up so packed fields seem reversed
    */
   uint8_t   hdrlen:4;    // In DWORDS, i.e. multiply by 4 to get byte count
   uint8_t   version:4;
   uint8_t   ecn:2;       // Explicit Congestion Notification
   uint8_t   dscp:6;      // DiffServ Code Point
   uint16_t  length;
   uint16_t  ident;
   uint16_t  fragoff:13;
   uint16_t  flags:3;
   uint8_t   ttl;
   uint8_t   protocol;
   uint16_t  checksum;
   uint32_t  srcip;
   uint32_t  dstip;
   uint32_t  options[ ];  // Present if hdrlen > 5
} __attribute__((__packed__));

#endif // __IP_H__