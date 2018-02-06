#pragma once

typedef struct SETS
{
	int *parents;
	int numberOfElements;
} Sets;

Sets *createUnionSets(int numberOfElements);

void deleteSets(Sets *sets);

int makeSet(Sets *sets, int x);

int findSet(Sets *sets, int x);

int unite(Sets *set, int x, int y);