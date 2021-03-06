// priorityQueue.cpp : Defines the entry point for the console application.
//
#include <stdio.h>
#include "queue.h"


int main()
{
	Queue *queue = createQueue(20);
	
	insert(queue, 15, 5);
	insert(queue, 17, 7);
	insert(queue, 11, 1);
	insert(queue, 14, 4);
	insert(queue, 16, 6);
	insert(queue, 19, 9);
	insert(queue, 12, 2);
	printQueue(queue);

	printf("%f", valueWithMaxPriority(queue));
	putchar('\n');

	int correct;
	printf("%f", extractMax(queue, &correct));
	putchar('\n');
	printQueue(queue);
	
	deleteQueue(&queue);
	return 0;
}

