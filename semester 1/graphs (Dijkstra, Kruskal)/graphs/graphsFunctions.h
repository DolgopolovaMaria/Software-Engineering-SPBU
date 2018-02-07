#pragma once
typedef struct GRAPH Graph;

Graph *createGraph(int number);

void deleteGraph(Graph **graph);

void putLength(Graph *graph, int x, int y, int length);

void printMatrix(Graph *graph);

void Dijkstra(Graph *graph, int **lengths, int **parents, int start);

void printMinPath(Graph *graph, int start, int final);

Graph *Kruscal(Graph *graph);		// for oriented graph