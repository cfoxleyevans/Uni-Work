////////////////////////////////////////////////////
//Name : btree.h
//Author : Chris Foxley-Evans
//Date : 11/3/2013
//Purpose : header file for ll.c
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//The struct that will make up a node
////////////////////////////////////////////////////
typedef struct node{

	int size;
 	char status;

 	struct node *left;
 	struct node *right;
 	struct node *parent;
} node;
////////////////////////////////////////////////////

////////////////////////////////////////////////////
//The struct that will encapsulate the tree
////////////////////////////////////////////////////
typedef struct btree{

	struct node *root;
} btree;
////////////////////////////////////////////////////
