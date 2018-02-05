#pragma once

typedef struct SETS
{
	int parents[20];
} Sets;

Sets *createUnionSets();

int makeSet(Sets *sets, int x);

int findSet(Sets *sets, int x);

int unite(Sets *set, int x, int y);