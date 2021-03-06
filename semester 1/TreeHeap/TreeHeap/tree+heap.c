// TreeHeap.cpp : Defines the entry point for the console application.
//
#include <stdlib.h>
#include <stdio.h>
#include "treeHeap.h"


int main()
{
	treeNode *tree = NULL;			// Test
	insert(&tree, 15, 5);
	insert(&tree, 11, 9);
	insert(&tree, 17, 3);
	insert(&tree, 19, 6);
	insert(&tree, 12, 1);
	insert(&tree, 13, 8);
	insert(&tree, 18, 4);
	inorderPrint(tree);
	putchar('\n');

	deleteElement(&tree, 13);
	deleteElement(&tree, 18);
	deleteElement(&tree, 15);
	inorderPrint(tree);
	putchar('\n');

	printf(" %d", contains(tree, 12));
	printf(" %d", contains(tree, 19));
	printf(" %d", contains(tree, 18));
	printf(" %d", contains(tree, 14));
	putchar('\n');

	deleteTree(&tree);
	
	return 0;
}

