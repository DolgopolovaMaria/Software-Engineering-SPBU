#include <stdlib.h>
#include <stdio.h>
#include "graphsFunctions.h"
#include "PriorityQueueMin.h"
#include "sets.h"


typedef struct GRAPH
{
	int **matrix;
	int numberOfVertexes;
	int numberOfEdges;
}Graph;

Graph *createGraph(int number)
{
	Graph *graph = malloc(sizeof(Graph));
	graph->matrix = malloc(sizeof(int*) * number);
	for (int i = 0; i < number; i++)
	{
		graph->matrix[i] = malloc(sizeof(int) * number);
	}
	for (int i = 0; i < number; i++)
	{
		for (int j = 0; j < number; j++)
		{
			graph->matrix[i][j] = 0;
		}
	}

	graph->numberOfVertexes = number;
	graph->numberOfEdges = 0;

	return graph;
}

void deleteGraph(Graph **graph)
{
	for (int i = 0; i < (*graph)->numberOfVertexes; i++)
	{
		free((*graph)->matrix[i]);
	}
	free((*graph)->matrix);
	free(*graph);
}

void putLength(Graph *graph, int x, int y, int length)
{
	graph->matrix[x][y] = length;
	if ((x != y) && (graph->matrix[y][x] == 0))
	{
		graph->numberOfEdges++;
	}
}

void printMatrix(Graph *graph)
{
	for (int i = 0; i < graph->numberOfVertexes; i++)
	{
		for (int j = 0; j < graph->numberOfVertexes; j++)
		{
			printf(" %d", graph->matrix[i][j]);
		}
		putchar('\n');
	}
	putchar('\n');
}

void Dijkstra(Graph *graph, int **lengths, int **parents, int start)
{
	*lengths = malloc(sizeof(int)*graph->numberOfVertexes);
	*parents = malloc(sizeof(int)*graph->numberOfVertexes);
	(*parents)[start] = start;

	Queue *queue = createQueue(graph->numberOfVertexes);
	for (int i = 0; i < graph->numberOfVertexes; i++)
	{
		insert(queue, i, 10000);
		(*lengths)[i] = 10000;
	}
	decreasePriority(queue, start, 0);
	(*lengths)[start] = 0;

	int tmp = 0;
	while (length(queue) > 0)
	{
		int minVertex = extractMin(queue);

		for (int i = 0; i < length(queue); i++)
		{
			if (graph->matrix[minVertex][value(queue, i)] > 0)
			{
				int newLength = graph->matrix[minVertex][value(queue, i)] + (*lengths)[minVertex];
				if (newLength < (*lengths)[value(queue, i)])
				{
					(*lengths)[value(queue, i)] = newLength;
					(*parents)[value(queue, i)] = minVertex;
					decreasePriority(queue, i, newLength);
				}
			}
		}
	}
	deleteQueue(&queue);
}

void printMinPath(Graph *graph, int start, int final)
{
	int *parents = NULL;
	int *lengths = NULL;
	Dijkstra(graph, &lengths, &parents, start);
	
	printf("length of path = %d", lengths[final]);
	putchar('\n');
	printf("Path: ");
	int i = final;
	while (i != start)
	{
		printf("%d%s", i, " <- ");
		i = parents[i];
	}
	printf("%d", i);
	putchar('\n');
	free(lengths);
	free(parents);
}

typedef struct PAIR
{
	int x;
	int y;
	int lengthXY;
}Pair;

int compare(Pair *a, Pair *b)
{
	if (a->lengthXY == b->lengthXY)
	{
		return 0;
	}
	if (a->lengthXY < b->lengthXY)
	{
		return -1;
	}
	return 1;
}

Graph *Kruscal(Graph *graph)		// for oriented graph
{
	Pair *arrayOfEdges = malloc(sizeof(Pair) * (graph->numberOfEdges));
	
	int k = 0;
	for (int i = 0; i < graph->numberOfVertexes; i++)
	{
		for (int j = i; j < graph->numberOfVertexes; j++)
		{
			if ((i != j) && (graph->matrix[i][j] > 0))
			{
				arrayOfEdges[k].x = i;
				arrayOfEdges[k].y = j;
				arrayOfEdges[k].lengthXY = graph->matrix[i][j];
				k++;
			}
		}
	}
	qsort(arrayOfEdges, graph->numberOfEdges, sizeof(Pair), compare);

	Sets *sets = createUnionSets(graph->numberOfVertexes);
	for (int i = 0; i < graph->numberOfVertexes; i++)
	{
		makeSet(sets, i);
	}

	Graph *result = createGraph(graph->numberOfVertexes);
	
	for (int i = 0; i < graph->numberOfEdges; i++)
	{
		if (!inSameSet(sets, arrayOfEdges[i].x, arrayOfEdges[i].y))
		{
			unite(sets, arrayOfEdges[i].x, arrayOfEdges[i].y);
			putLength(result, arrayOfEdges[i].x, arrayOfEdges[i].y, arrayOfEdges[i].lengthXY);
			putLength(result, arrayOfEdges[i].y, arrayOfEdges[i].x, arrayOfEdges[i].lengthXY);
		}
	}

	deleteSets(&sets);
	free(arrayOfEdges);
	return result;
}