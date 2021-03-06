// graphs.cpp : Defines the entry point for the console application.
//
#include <stdlib.h>
#include <stdio.h>
#include "graphsFunctions.h"


int main()
{
	Graph *graph = createGraph(5);
	putLength(graph, 0, 1, 10);
	putLength(graph, 0, 2, 2);
	putLength(graph, 0, 3, 20);
	putLength(graph, 2, 1, 1);
	putLength(graph, 2, 3, 5);
	putLength(graph, 3, 4, 1);
	putLength(graph, 4, 0, 3);
	printMatrix(graph);
	
	int *parents = NULL;
	int *lengths = NULL;
	Dijkstra(graph, &lengths, &parents, 0);
	for (int i = 0; i < 5; i++)
	{
		printf(" %d", lengths[i]);
	}
	putchar('\n');
	free(parents);
	free(lengths);

	printMinPath(graph, 2, 4);
	printMinPath(graph, 0, 4);

	deleteGraph(&graph);

	putchar('\n');
	Graph *graph1 = createGraph(5);
	putLength(graph1, 0, 1, 10);
	putLength(graph1, 0, 2, 2);
	putLength(graph1, 0, 3, 20);
	putLength(graph1, 0, 4, 5);
	putLength(graph1, 4, 0, 5);
	putLength(graph1, 1, 4, 1);
	putLength(graph1, 1, 2, 3);
	putLength(graph1, 2, 3, 20);
	putLength(graph1, 2, 4, 1);
	putLength(graph1, 3, 4, 5);
	putLength(graph1, 3, 2, 20);
	putLength(graph1, 1, 0, 10);
	putLength(graph1, 2, 0, 2);
	putLength(graph1, 3, 0, 20);
	putLength(graph1, 4, 1, 1);
	putLength(graph1, 2, 1, 3);
	putLength(graph1, 4, 2, 1);
	putLength(graph1, 4, 3, 5);
	printMatrix(graph1);
	
	Graph *minTree = Kruscal(graph1);
	printMatrix(minTree);

	deleteGraph(&graph1);
	deleteGraph(&minTree);
	return 0;
}

