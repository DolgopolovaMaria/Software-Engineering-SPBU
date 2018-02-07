#include <stdlib.h>
#include "sets.h"

typedef struct SETS
{
	int *parents;
	int numberOfElements;
} Sets;


Sets *createUnionSets(int numberOfElements)
{
	Sets *sets = malloc(sizeof(Sets));
	sets->parents = malloc(sizeof(int) * numberOfElements);
	for (int i = 0; i < numberOfElements; i++)
	{
		sets->parents[i] = -1;
	}
	sets->numberOfElements = numberOfElements;
	return sets;
}

void deleteSets(Sets **sets)
{
	free((*sets)->parents);
	free(*sets);
}

int makeSet(Sets *sets, int x)
{
	if ((x < 0) || (x > sets->numberOfElements))
	{
		return -1;
	}
	sets->parents[x] = x;
	return 1;
}

int find(Sets *sets, int x)
{
	if (x == sets->parents[x])
	{
		return x;
	}
	return sets->parents[x] = find(sets, sets->parents[x]);
}

int findSet(Sets *sets, int x)
{
	if ((x < 0) || (x > sets->numberOfElements - 1) || (sets->parents[x] == -1))
	{
		return -1;
	}
	return find(sets, x);
}

int inSameSet(Sets *sets, int x, int y)
{
	if ((findSet(sets, x) == -1) || (findSet(sets, y) == -1))
	{
		return -1;
	}
	if (findSet(sets, x) == findSet(sets, y))
	{
		return 1;
	}
	return 0;
}

int unite(Sets *set, int x, int y)
{
	int xSet = findSet(set, x);
	int ySet = findSet(set, y);
	if ((xSet == -1) || (ySet == -1))
	{
		return -1;
	}

	if (rand() % 2 == 0)
	{
		set->parents[xSet] = ySet;
	}
	else
	{
		set->parents[ySet] = xSet;
	}

	return 1;
}