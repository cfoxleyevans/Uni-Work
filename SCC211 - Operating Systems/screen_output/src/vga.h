#define BLACK      0x00
#define BLUE       0x01
#define GREEN      0x02
#define CYAN       0x03
#define RED        0x04
#define MAGENTA    0x05
#define BROWN      0x06
#define LT_GREY    0x07
#define DK_GREY    0x08
#define LT_BLUE    0x09
#define LT_GREEN   0x0a
#define LT_CYAN    0x0b
#define LT_RED     0x0c
#define LT_MAGENTA 0x0d
#define LT_BROWN   0x0e
#define WHITE      0x0f

#define BACKGROUND(C) ((C) << 12)
#define FOREGROUND(C) ((C) <<  8)
