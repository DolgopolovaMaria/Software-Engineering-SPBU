#include <stdlib.h>
#include "HeapFunctions.h"

int indexOfParent(int heap[], int heapSize, int index)
{
	if (index <= 0)
	{
		return -1;
	}
	if (index >= heapSize)
	{
		return -1;
	}
	return (index + 1) / 2 - 1;
}

int indexOfLeft(int heap[], int heapSize, int index)
{
	int left = 2 * (index + 1) - 1;
	if (left >= heapSize)
	{
		return -1;
	}
	return left;
}

int indexOfRight(int heap[], int heapSize, int index)
{
	int right = 2 * (index + 1);
	if (right >= heapSize)
	{
		return -1;
	}
	return right;
}

int indexOfMaxChild(int heap[], int heapSize, int index)
{
	int left = indexOfLeft(heap, heapSize, index);
	int right = indexOfRight(heap, heapSize, index);
	if ((left == -1) && (right == -1))
	{
		return -1;
	}
	if (right == -1)
	{
		return left;
	}
	if (heap[left] > heap[right])
	{
		return left;
	}
	return right;
}

void swapValues(int heap[], int heapSize, int firstIndex, int secondIndex)
{
	int temp = heap[firstIndex];
	heap[firstIndex] = heap[secondIndex];
	heap[secondIndex] = temp;
}

int addValues(int heap[], int *heapSize, int arraySize, int value)
{
	if (*heapSize + 1 >= arraySize)
	{
		return -1;
	}

	heap[*heapSize] = value;
	*heapSize = *heapSize + 1;

	int temp = *heapSize - 1;
	int parent = indexOfParent(heap, *heapSize, temp);

	while ((parent > -1) && (heap[temp] > heap[parent]))
	{
		swapValues(heap, *heapSize, temp, parent);
		temp = parent;
		parent = indexOfParent(heap, *heapSize, temp);
	}

	return 1;
}

int maxheapify(int heap[], int heapSize, int index)
{
	if ((index < 0) || (index >= heapSize))
	{
		return -1;
	}

	int temp = index;
	int maxChild = indexOfMaxChild(heap, heapSize, temp);
	while ((maxChild > -1) && (heap[temp] < heap[maxChild]))
	{
		swapValues(heap, heapSize, temp, maxChild);
		temp = maxChild;
		maxChild = indexOfMaxChild(heap, heapSize, temp);
	}

	return 1;
}

int buildMaxHeap(int arrayForHeap[], int length)
{
	for (int i = length / 2; i >= 0; i--)
	{
		maxheapify(arrayForHeap, length, i);
	}
}

void heapSort(int arrayForSort[], int length)
{
	buildMaxHeap(arrayForSort, length);
	int heapSize = length;
	for (int i = length - 1; i > 0; i--)
	{
		swapValues(arrayForSort, heapSize, 0, i);
		heapSize--;
		maxheapify(arrayForSort, heapSize, 0);
	}
}