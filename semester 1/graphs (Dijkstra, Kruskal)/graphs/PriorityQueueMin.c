#include <stdlib.h>
#include <stdio.h>
#include "PriorityQueueMin.h"


typedef struct NODE
{
	int value;
	int priority;
}Node;

typedef struct QUEUE
{
	Node *nodes;
	int maxLength;
	int length;
}Queue;


int parentIndex(Queue *queue, int x)
{
	if ((x <= 0) || (x >= queue->maxLength))
	{
		return -1;
	}
	return (x + 1) / 2 - 1;
}

int leftIndex(Queue *queue, int x)
{
	int res = 2 * x + 1;
	if ((x < 0) || (res >= queue->maxLength))
	{
		return -1;
	}
	return res;
}

int rightIndex(Queue *queue, int x)
{
	int res = 2 * x + 2;
	if ((x < 0) || (res >= queue->maxLength))
	{
		return -1;
	}
	return res;
}

int minChildIndex(Queue *queue, int x)
{
	if ((leftIndex(queue, x) == -1) && (rightIndex(queue, x) == -1))
	{
		return -1;
	}
	if ((leftIndex(queue, x) == -1) || (queue->nodes[rightIndex(queue, x)].priority < queue->nodes[leftIndex(queue, x)].priority))
	{
		return rightIndex(queue, x);
	}
	return leftIndex(queue, x);
}

Queue *createQueue(int maxLength)
{
	Queue *queue = malloc(sizeof(Queue));
	queue->length = 0;
	queue->maxLength = maxLength;
	queue->nodes = malloc(sizeof(Node) * maxLength);

	return queue;
}

void deleteQueue(Queue** queue)
{
	free((*queue)->nodes);
	free(*queue);
}

int valueWithMinPriority(Queue *queue)
{
	if (queue->length == 0)
	{
		return -1;
	}
	return queue->nodes[0].value;
}

void swap(Queue *queue, int x, int y)
{
	double temp = queue->nodes[x].value;
	queue->nodes[x].value = queue->nodes[y].value;
	queue->nodes[y].value = temp;

	int tmp = queue->nodes[x].priority;
	queue->nodes[x].priority = queue->nodes[y].priority;
	queue->nodes[y].priority = tmp;
}

int insert(Queue *queue, int value, int priority)
{
	if (queue->length == queue->maxLength)
	{
		return -1;
	}

	queue->nodes[queue->length].value = value;
	queue->nodes[queue->length].priority = priority;

	int i = queue->length;
	while ((i > 0) && (queue->nodes[i].priority < queue->nodes[parentIndex(queue, i)].priority))
	{
		swap(queue, i, parentIndex(queue, i));
		i = parentIndex(queue, i);
	}

	queue->length++;
	return 1;
}

int minheapify(Queue *queue, int i)
{
	if ((i < 0) || (i >= queue->maxLength))
	{
		return -1;
	}

	int k = minChildIndex(queue, i);
	while ((k > -1) && (queue->nodes[k].priority < queue->nodes[i].priority))
	{
		swap(queue, i, k);
		i = k;
		k = minChildIndex(queue, i);
	}
	return 1;
}

int extractMin(Queue *queue)
{
	int min = queue->nodes[0].value;
	queue->length--;
	queue->nodes[0].value = queue->nodes[queue->length].value;
	queue->nodes[0].priority = queue->nodes[queue->length].priority;
	minheapify(queue, 0);
	return min;
}

void printQueue(Queue *queue)
{
	for (int i = 0; i < queue->length; i++)
	{
		printf("value = %d%s%d%s", queue->nodes[i].value, ", priority = ", queue->nodes[i].priority, ";  ");
	}
	putchar('\n');
}

void decreasePriority(Queue *queue, int x, int newPriority)
{
	queue->nodes[x].priority = newPriority;
	int i = x;
	while ((i > 0) && (queue->nodes[parentIndex(queue, i)].priority > queue->nodes[i].priority))
	{
		swap(queue, i, parentIndex(queue, i));
		i = parentIndex(queue, i);
	}
}

int length(Queue *queue)
{
	return queue->length;
}

int value(Queue *queue, int i)
{
	return queue->nodes[i].value;
}

int priority(Queue *queue, int i)
{
	return queue->nodes[i].priority;
}

