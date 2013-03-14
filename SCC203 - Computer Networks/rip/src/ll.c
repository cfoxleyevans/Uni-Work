////////////////////////////////////////////////////
//Name : ll.c
//Author : Chris Foxley-Evans
//Date : 11/3/2013
//Purpose : implement a linked list
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Includes
////////////////////////////////////////////////////
#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <stdint.h>
#include "ll.h"
#include "table_entry.h"

////////////////////////////////////////////////////



////////////////////////////////////////////////////
//Creates a new list and retruns a ptr
//return : a ptr to the new list
////////////////////////////////////////////////////
list* list_init(){

	list *list = malloc(sizeof(list));

	list->head = malloc(sizeof(node)); 
	list->head = NULL;

	list->tail = malloc(sizeof(node)); 
	list->tail = NULL;
	
	return list;
}

////////////////////////////////////////////////////
//Checks if the list is empty
//list *list : the list to check 
//return : 1 if the list is empty 0 if not 
////////////////////////////////////////////////////
int list_is_empty(list *list){

	if(list->head == NULL)
		return 1;
	else 
		return 0;
}	

////////////////////////////////////////////////////
//Inserts a new node into the list
//list *list : the list to insert into
//return : 0 for success
////////////////////////////////////////////////////
int list_insert(list *list, table_entry *entry){

	node *new_node = malloc(sizeof(node));
	new_node->entry = entry;
	
	if(list_is_empty(list)){
		new_node->index = 1;
		list->head = new_node;
		list->tail = new_node;
		
	}
	else{
		new_node->index = list->tail->index + 1;
		list->tail->next = new_node;
		list->tail = new_node;
	}
	return 0;
}
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Removes item at index from the list
//list *list : the list that is to be removed from
//int index : the index to be removed
//return : 1 if the remove is succsesful
////////////////////////////////////////////////////
int list_remove(list *list, int index){
	
	//empty list
	if(list_is_empty(list)){ 
		perror("LIST EMPTY");
	}

	//index is the head
	if(list->head->index == index){ 
		node *removed = list->head;
		list->head = list->head->next;
		free(removed);
		return 1;
	}
	//check the rest of the list
	else{
		node *ptr = list->head->next;
		node *previous = list->head;
		node *removed = NULL;
	
		while(ptr->next != NULL){
			if(ptr->index == index){
				removed = ptr;
				previous->next = ptr->next;
				break;
			}	
			previous = ptr;
			ptr = ptr->next;
			free(removed);
		}
		return 1;
	}

	//not found the index
	perror("INDEX NOT FOUND");
	return 0;
}

////////////////////////////////////////////////////
//Prints all of the nodes in a list in v2 format
//list *list : the list that is to be printed
//return : void 
////////////////////////////////////////////////////
void list_print_rip_v2(list *list){

	if(list_is_empty(list)){
		printf("ERROR : EMPTY LIST\n");
	}
	
	else{
		node *ptr = list->head;

		printf("|     %s     |     %s     |     %s     | %s | %s | %s |\n", "Network", "Netmask", "Gateway", "Interface", "Metric", "TTL");
		printf("|   %s       |   %s  |   %s  |     %i     |    %i   | %i |\n", iptos(ptr->entry->network), iptos(ptr->entry->netmask),
				iptos(ptr->entry->gateway), ptr->entry->interface, ptr->entry->metric, ptr->entry->ttl);

		while(ptr->next != NULL){
			ptr = ptr->next;
			printf("|   %s  |   %s  |   %s  |     %i     |    %i   | %i |\n", iptos(ptr->entry->network), iptos(ptr->entry->netmask),
				iptos(ptr->entry->gateway), ptr->entry->interface, ptr->entry->metric, ptr->entry->ttl);
			
		}
		printf("\n");
	}
}
////////////////////////////////////////////////////


////////////////////////////////////////////////////
//Prints all of the nodes in a list in v1 format
//list *list : the list that is to be printed
//return : void 
////////////////////////////////////////////////////
void list_print_rip_v1(list *list){

	if(list_is_empty(list)){
		printf("ERROR : EMPTY LIST\n");
	}
	
	else{
		node *ptr = list->head;

		printf("|     %s     |     %s     |     %s     | %s | %s | %s |\n", "Network", "Netmask", "Gateway", "Interface", "Metric", "TTL");
		printf("|   %s       |   %s  |   %s  |     %i     |    %i   | %i |\n", iptos(ptr->entry->network), iptos(ptr->entry->netmask),
				iptos(ptr->entry->gateway), ptr->entry->interface, ptr->entry->metric, ptr->entry->ttl);

		while(ptr->next != NULL){
			ptr = ptr->next;
			printf("|   %s  |   %s  |   %s  |     %i     |    %i   | %i |\n", iptos(ptr->entry->network), iptos(ptr->entry->netmask),
				iptos(ptr->entry->gateway), ptr->entry->interface, ptr->entry->metric, ptr->entry->ttl);
			
		}
		printf("\n");
	}
}
////////////////////////////////////////////////////





////////////////////////////////////////////////////
//Finds and prints the the gateway
//list *list : the list that is to be searched
//return : void 
////////////////////////////////////////////////////
void list_find_next_hop(list *list, long ip){

	//node *ptr = list->head;

	/*
	if(ptr->entry->network == ip){
		printf("Next hop for %li is: %li\n", ip, ptr->entry->gateway);
		return;
	}

	while(ptr->next != NULL){
		ptr= ptr->next;
		if(ptr->entry->network == ip){
			printf("Next hop for %li is: %li\n", ip, ptr->entry->gateway);
			return;
		}
	}
	*/
	perror("Network not found!!");

}


//end of file ll.c










