#pragma once
typedef struct NODE Node;

typedef struct QUEUE Queue;


Queue *createQueue(int maxLength);

void deleteQueue(Queue** queue);

int valueWithMinPriority(Queue *queue);

int insert(Queue *queue, int value, int priprity);

int extractMin(Queue *queue);

void printQueue(Queue *queue);

void decreasePriority(Queue *queue, int x, int newPriority);

int length(Queue *queue);

int value(Queue *queue, int i);

int priority(Queue *queue, int i);