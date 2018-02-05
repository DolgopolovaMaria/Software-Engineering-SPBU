#pragma once
typedef struct TREENODE
{
	int key;
	int prior;
	struct TREENODE *leftChild;
	struct TREENODE *rightChild;
} treeNode;

void insert(treeNode **root, int key, int prior);

int deleteElement(treeNode **root, int key);

int contains(treeNode *root, int key);

void inorderPrint(treeNode *root);

void deleteTree(treeNode **root);