////////////////////////////////////////////////////
//Name : buddy.c
//Author : Chris Foxley-Evans
//Date : 20/3/2013
//Purpose : implment the operations needed for alloc and free
////////////////////////////////////////////////////

////////////////////////////////////////////
#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>

#include "btree.h"
////////////////////////////////////////////

////////////////////////////////////////////
#define MEM_SIZE 4096 
////////////////////////////////////////////

////////////////////////////////////////////////////
//this function was found at the web link bewlow
//http://stackoverflow.com/questions/466204/rounding-off-to-nearest-power-of-2
////////////////////////////////////////////////////
int calculate_block_size(int v)
{
    v--;
    v |= v >> 1;
    v |= v >> 2;
    v |= v >> 4;
    v |= v >> 8;
    v |= v >> 16;
    v++;
    return v;
}
////////////////////////////////////////////////////

////////////////////////////////////////////
btree* init_tree(int size){

	btree *tree = malloc(sizeof(btree));
	tree->root = malloc(sizeof(node));

	tree->root->parent = NULL;
	// tree->root->left = malloc(sizeof(node));
	// tree->root->right  = malloc(sizeof(node));
	tree->root->left = NULL;
	tree->root->right = NULL;

	tree->root->size = size;
	tree->root->status = 'F';

	return tree;
}
////////////////////////////////////////////

////////////////////////////////////////////
node* init_node(node *parent){

	node *node = malloc(sizeof(struct node));

	node->parent = parent;
	node->left = NULL;
	node->right  = NULL;

	node->size = parent->size / 2;
	node->status = 'F';

	return node;
}
////////////////////////////////////////////

////////////////////////////////////////////
void print_tree(node *root){
	if(root != NULL){
		print_tree(root->left);
		print_tree(root->right);
		if(root->status == 'A' || root->status == 'F'){
			printf("Block Size: %d \t Status: %c\n", root->size, root->status);
		}
	}
}
////////////////////////////////////////////

////////////////////////////////////////////
int split_node(node *node, int child_size){

	node->status = 'I';
	if(!node->left) node->left = init_node(node);
	if(!node->right) node->right = init_node(node);

	if(child_size == (node->left->size) && node->left->status == 'F'){
		node->left->status = 'A';
		return 1;
	}

	if(child_size == node->right->size && node->right->status == 'F'){
		node->right->status = 'A';
		return 1;
	}

	if(node->left->size > child_size && node->left->status == 'F'){
		int value =  split_node(node->left, child_size);
		if(value == 1)
			return 1;
	}

	if(node->right->size > child_size && node->right->status == 'F'){
		int value =  split_node(node->right, child_size);
		if(value == 1)
			return 1;
	}
}
////////////////////////////////////////////


////////////////////////////////////////////
node* scan_tree(node *root){
	if(root != NULL){
		if(root->status == 'I' || root->status == 'A'){
    	scan_tree(root->left);
    	scan_tree(root->right);
    }
    if(root->status == 'F'){
    	return root;
    }
}
}
////////////////////////////////////////////

////////////////////////////////////////////
int insert_node(node *root, int size){

    int block_size = calculate_block_size(size);

    printf("%s %i %s\n","Allocating", block_size, "bytes");

    if(root->size == block_size && root->status == 'F'){
    	root->status ='A';
    	return 1;
    }

    if(root->left == NULL) 
    	root->left = init_node(root);
	if(root->right == NULL) 
		root->right = init_node(root);
    
    node* empty = scan_tree(root);
    
    if(empty->size > block_size && empty->status == 'F')
    	split_node(empty, block_size);
}
////////////////////////////////////////////

////////////////////////////////////////////
int main(int argc, char const *argv[])
{
	btree *map = init_tree(MEM_SIZE);
	
	//print_tree(map->root);	

	insert_node(map->root, 128);
	insert_node(map->root, 128);

	// insert_node(map->root, 512);
	//insert_node(map->root, 512);
	//insert_node(map->root, 512);




	print_tree(map->root);

		
	return 0;
}
////////////////////////////////////////////
//end of file buddy.c