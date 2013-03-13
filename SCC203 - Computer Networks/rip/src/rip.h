//
//      RIP:      RFC 2453
//
//      RIP port:   UDP/520
//      RIP-2 Multicast address: 224.0.0.9
//
//      Multi-byte numbers stored in big-endian format
//

//
//      Note RIP-1 doesn't pass subnet masks
//      - Subnet entries must therefore not be passed outside of the subnet
//      - to which they belong - thus ensuring that all receivers know the
//      - subnet mask to apply. Filtering must therefore be done by border
//      - routers. RIP-2 doesn't have this restriction.
//
struct rip1ent {
   unsigned short   addr_family;  // RIP-1 only supports AF_INET
   unsigned short   zero;
   unsigned long    v4addr;
   unsigned long    null[2];
   unsigned long    metric;       // 1 - 15, or 16 for infinity
} __attribute__((__packed__));

struct rip2ent {
   unsigned short   addr_family;  // RIP-1 only supports AF_INET
   unsigned short   route_tag;    // Available for IGB/EGP, AS number, etc.
   unsigned long    ip_addr;
   unsigned long    subnet_mask;
   unsigned long    next_hop;     // 0 -> sender should be next hop
   unsigned long    metric;       // 1 - 15, or 16 for infinity
} __attribute__((__packed__));

//
//      Only widely recognised AUTH is type 2: Plain text password
//      - left justified and null padded to 16 bytes.
//
struct authent {
   unsigned short   authid;       // = 0xffff
   unsigned short   auth_type;
   unsigned char    auth[16];
} __attribute__((__packed__));

struct rip_msg {
   unsigned char    command;
   unsigned char    version;
   unsigned short   zero;
   union {
      struct authent auth;        // af_family = 0xffff  - RIP 2 only
      struct rip1ent rip1;
      struct rip2ent rip2;
   }                entries[ ];   // 1 - 25 inclusive RIP entries
} __attribute__((__packed__));
//end of file rip.h