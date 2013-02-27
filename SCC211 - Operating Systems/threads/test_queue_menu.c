/************************************************************************
 *                                                                      *
 * file test_queue_menu.c						*
 *                                                                      *
 * Simple menu-driven test harness for queue module.			*
 *                                                                      *
 ************************************************************************/

#include        <stdio.h>
#include        <stdlib.h>
#include        "queue.h"

Queue q;

typedef struct qti {
	struct qti *next; /* This has to be here to be compliant with QItem */
	int data; /* This is independent of QItem */
} QTestItem;

QTestItem *pqi;

main(int argc, char *arcv[])
{
        int i;
	int keep_going = 1;

	queue_init(&q);  
	while(keep_going) {
		do {
			printf("\nPress ...\n"); 
			printf("\t1 to put\n"); 
			printf("\t2 to get\n"); 
			printf("\t3 to test for empty\n"); 
			printf("\t4 to quit\n\n"); 
			scanf("%d", &i);
		} while (!((i == 1) || (i == 2) || (i == 3) || (i == 4)));
		if (i == 1) {
			pqi = (QTestItem *)malloc(sizeof(QTestItem));
			printf("Enter number to put into queue: ");
			scanf("%d", &(pqi->data));
			queue_put(&q, (Qitem *)pqi); 
			printf("Put %d into queue\n", pqi->data);
		} else if (i == 2) {
			pqi = (QTestItem *)queue_get(&q);
			if (pqi == (QTestItem *)0) {
				printf("Can't get from empty queue\n");
			} else {
				printf("Got %d from queue\n", pqi->data);
				free(pqi);
			}
		} else if (i == 3) {
			if (queue_empty(&q))
				printf("Queue empty\n");
			else
				printf("Queue not empty\n");
		} else {
			printf("Done!\n");
			keep_going = 0;
		}	
	}
}
			
/* end file: test_queue_menu.c */
