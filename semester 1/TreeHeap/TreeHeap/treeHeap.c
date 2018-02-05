#include <stdlib.h>
#include <stdio.h>
#include "treeHeap.h"


void split(treeNode *root, treeNode **left, treeNode **right, int key)
{
	if (root == NULL)
	{
		*left = NULL;
		*right = NULL;
		return;
	}
	if (key > root->key)
	{
		split(root->rightChild, &(root->rightChild), &*right, key);
		*left = root;
	}
	else
	{
		split(root->leftChild, &*left, &(root->leftChild), key);
		*right = root;
	}
}

void merge(treeNode **newTree, treeNode *left, treeNode *right)
{
	if (right == NULL)
	{
		*newTree = left;
		return;
	}
	if (left == NULL)
	{
		*newTree = right;
		return;
	}

	if (left->prior > right->prior)
	{
		merge(&(left->rightChild), left->rightChild, right);
		*newTree = left;
	}
	else
	{
		merge(&(right->leftChild), left, right->leftChild);
		*newTree = right;
	}
}

void insert(treeNode **root, int key, int prior)
{
	treeNode *left = NULL;
	treeNode *right = NULL;
	split(*root, &left, &right, key);
	treeNode *newNode = malloc(sizeof(treeNode));
	newNode->key = key;
	newNode->prior = prior;
	newNode->leftChild = NULL;
	newNode->rightChild = NULL;
	merge(&left, left, newNode);
	merge(*&root, left, right);
}

int deleteElement(treeNode **root, int key)
{
	if (*root == NULL)
	{
		return 0;
	}
	if ((*root)->key == key)
	{
		merge(*&root, (*root)->leftChild, (*root)->rightChild);
		return 1;
	}
	if ((*root)->key < key)
	{
		deleteElement(&((*root)->rightChild), key);
	}
		else
	{
		deleteElement(&((*root)->leftChild), key);
	}
}

int contains(treeNode *root, int key)
{
	if (root == NULL)
	{
		return 0;
	}
	if (root->key == key)
	{
		return 1;
	}
	if (key > root->key)
	{
		contains(root->rightChild, key);
	}
	else
	{
		contains(root->leftChild, key);
	}
}

void inorderPrint(treeNode *root)
{
	if (root == NULL)
	{
		return;
	}
	
	inorderPrint(root->leftChild);
	
	printf("key = %d", root->key);
	printf(", prior = %d", root->prior);
	putchar('\n');

	inorderPrint(root->rightChild);
}

void deleteTree(treeNode **root)
{
	if (*root == NULL)
	{
		return;
	}

	deleteTree(&((*root)->leftChild));
	deleteTree(&((*root)->rightChild));
	free(*root);
}