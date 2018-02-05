// heap.cpp : Defines the entry point for the console application.
//
#include <stdio.h>
#include "HeapFunctions.h"

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
	int testHeap1[10] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };			// test
	int heapSize = 0;
	addValues(testHeap1, &heapSize, 10, 5);
	addValues(testHeap1, &heapSize, 10, 9);
	addValues(testHeap1, &heapSize, 10, 1);
	addValues(testHeap1, &heapSize, 10, 4);
	addValues(testHeap1, &heapSize, 10, 3);
	addValues(testHeap1, &heapSize, 10, 7);
	addValues(testHeap1, &heapSize, 10, 2);
	printArray(testHeap1, heapSize);

	int testHeap2[10] = { 3, 5, 7, 6, 1, 2, 11, 8, 9, 4 };
	buildMaxHeap(testHeap2, 10);
	printArray(testHeap2, 10);

	int testHeap3[10] = { 5, 1, 10, 4, 2, 3, 9, 8, 6, 7 };
	heapSort(testHeap3, 10);
	printArray(testHeap3, 10);

	return 0;
}

