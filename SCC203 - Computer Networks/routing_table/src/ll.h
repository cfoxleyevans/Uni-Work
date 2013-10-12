////////////////////////////////////////////////////
//Name : ll.h
//Author : Chris Foxley-Evans
//Date : 11/3/2013
//Purpose : header file for ll.c
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//The struct that will make up a node
////////////////////////////////////////////////////
typedef struct node{

	int index;
	struct table_entry *entry;
	struct node *next;
} node;
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//The struct that will encapsulate the nodes
////////////////////////////////////////////////////
typedef struct list{

	struct node *head;
	struct node *tail;
} list;

////////////////////////////////////////////////////
//Method prototypes for linked list
////////////////////////////////////////////////////
list* list_init();
int list_is_empty(list *list);
int list_insert(list *list, struct table_entry *entry); 
int list_remove(list *list, int index);
void list_print(list *list); 
void list_find_next_hop(list *list, long ip);
////////////////////////////////////////////////////
//end of file ll.h