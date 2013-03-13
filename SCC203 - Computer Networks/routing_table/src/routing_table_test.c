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
	test_entry->network = 1004;
	test_entry->netmask = 255;
	test_entry->gateway = 4573;
	test_entry->interface = 1;
	test_entry->metric = 1;
	test_entry->ttl = 255;

	struct table_entry *test_entry_2 = malloc(sizeof(table_entry));
	test_entry_2->network = 1234;
	test_entry_2->netmask = 255;
	test_entry_2->gateway = 1256;
	test_entry_2->interface = 0;
	test_entry_2->metric = 1;
	test_entry_2->ttl = 255;

	struct table_entry *test_entry_3 = malloc(sizeof(table_entry));
	test_entry_3->network = 2456;
	test_entry_3->netmask = 255;
	test_entry_3->gateway = 1860;
	test_entry_3->interface = 0;
	test_entry_3->metric = 1;
	test_entry_3->ttl = 255;

	
	list *table = list_init();

	list_insert(table, test_entry);
	list_insert(table, test_entry_2);
	list_insert(table, test_entry_3);


	list_print(table);

	list_find_next_hop(table, 1234);

	return 0;
}
