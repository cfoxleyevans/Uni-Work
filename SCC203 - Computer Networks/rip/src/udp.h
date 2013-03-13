struct udphdr {
uint16_t srcport;
uint16_t dstport;
uint16_t length;
uint16_t checksum;
} __attribute__((__packed__));

struct v4pseudo_hdr {
uint32_t srcip;
uint32_t dstip;
uint8_t zero; // Always set to zero
uint8_t protocol; // Decimal 17 (UDP)
uint16_t udp_length;
} __attribute__((__packed__));
//end of file udp.h