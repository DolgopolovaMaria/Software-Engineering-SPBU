// task7.cpp : Defines the entry point for the console application.
//

#include <stdio.h>
#include "HashTable.h"
#include "List.h"

int main()
{
	if (testList())
	{
		printf("List functions work correctly\n");
		if (testTable())
		{
			printf("HashTable functions work correctly\n");
		}
		else
		{
			printf("Error! HashTable functions don't work correctly\n");
		}
	}
	else
	{
		printf("Error! List functions don't work correctly\n");
	}
	return 0;
}

