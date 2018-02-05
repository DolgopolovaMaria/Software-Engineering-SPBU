#pragma once
typedef struct NODE Node;

typedef struct QUEUE Queue;


Queue *createQueue();

void deleteQueue(Queue** queue);

double valueWithMaxPriority(Queue *queue);

int insert(Queue *queue, double value, int priprity);

double extractMax(Queue *queue, int *correct);

void printQueue(Queue *queue);