#include <stdlib.h>
#include "sets.h"


Sets *createUnionSets()
{
	Sets *sets = malloc(sizeof(Sets));
	for (int i = 0; i < 20; i++)
	{
		sets->parents[i] = -1;
	}
	return sets;
}

int makeSet(Sets *sets, int x)
{
	if ((x < 0) || (x > 19))
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
	if ((x < 0) || (x > 19) || (sets->parents[x] == -1))
	{
		return -1;
	}
	return find(sets, x);
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
