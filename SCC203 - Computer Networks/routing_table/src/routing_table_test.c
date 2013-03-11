////////////////////////////////////////////////////
//Name : routing_table_test.c
//Author : Chris Foxley-Evans
//Date : 11/3/2013
//Purpose : tests building a routing table
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//Includes
////////////////////////////////////////////////////
#include <stdlib.h>
#include <stdio.h>
#include "ll.h"
#include "table_entry.h"
////////////////////////////////////////////////////


int main(int argc, char **argv)
{
	struct table_entry *test_entry = malloc(sizeof(table_entry));
	test_entry->network = 19216821;
	test_entry->netmask = 2552552550;
	test_entry->gateway = 19216802;
	test_entry->interface = 0;
	test_entry->metric = 1;
	test_entry->ttl = 255;
	
	list *table = list_init();

	list_insert(table, test_entry);
	list_insert(table, test_entry);
	list_insert(table, test_entry);
	list_insert(table, test_entry);
	list_insert(table, test_entry);
	list_insert(table, test_entry);
	list_insert(table, test_entry);
	list_insert(table, test_entry);
	list_insert(table, test_entry);

	list_print(table);

	return 0;
}
