// disjointSetUnion.cpp : Defines the entry point for the console application.
//
#include <stdlib.h>
#include <stdio.h>
#include "sets.h"


void printArray(int arr[], int length)
{
	for (int i = 0; i < length; i++)
	{
		printf(" %d", arr[i]);
	}
	putchar('\n');
}

int main()
{
	Sets *testSets = createUnionSets(20);
	
	makeSet(testSets, 2);
	makeSet(testSets, 5);
	makeSet(testSets, 10);
	makeSet(testSets, 19);
	makeSet(testSets, 0);
	makeSet(testSets, 3);
	makeSet(testSets, 1);
	makeSet(testSets, 18);
	makeSet(testSets, 17);
	printArray(testSets->parents, 20);

	unite(testSets, 0, 1);
	unite(testSets, 2, 3);
	unite(testSets, 0, 2);
	unite(testSets, 17, 18);
	unite(testSets, 18, 19);
	unite(testSets, 19, 10);
	printArray(testSets->parents, 20);

	printf(" %d", findSet(testSets, 0));
	printf(" %d", findSet(testSets, 1));
	printf(" %d", findSet(testSets, 2));
	printf(" %d", findSet(testSets, 3));
	putchar('\n');
	printf(" %d", findSet(testSets, 19));
	printf(" %d", findSet(testSets, 6));
	putchar('\n');
	printArray(testSets->parents, 20);

	deleteSets(testSets);
	return 0;
}

