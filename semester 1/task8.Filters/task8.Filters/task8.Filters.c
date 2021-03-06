// task8.Filters.cpp : Defines the entry point for the console application.
//
#include <stdio.h>
#include "Filters.h"

int main(int argc, char* argv[])
{
	if (argc != 4)
	{
		printf("Error! Incorrect input");
		return 0;
	}

	FILE *startFile;
	fopen_s(&startFile, argv[1], "rb");
	if (startFile == NULL)
	{
		printf("Error! Incorrect name of input file");
		return 0;
	}

	FILE *finalFile;
	fopen_s(&finalFile, argv[3], "wb");
	if (finalFile == NULL)
	{
		printf("Error! Incorrect name of output file");
		fclose(startFile);
		return 0;
	}

	char *filter = argv[2];
	int correct = 0;
	if (strcmp(filter, "median") == 0)
	{
		correct = applyFilter(startFile, 1, finalFile);
	}
	else if (strcmp(filter, "gauss") == 0)
	{
		correct = applyFilter(startFile, 2, finalFile);
	}
	else if (strcmp(filter, "sobelX") == 0)
	{
		correct = applyFilter(startFile, 3, finalFile);
	}
	else if (strcmp(filter, "sobelY") == 0)
	{
		correct = applyFilter(startFile, 4, finalFile);
	}
	else if (strcmp(filter, "grey") == 0)
	{
		correct = applyFilter(startFile, 5, finalFile);
	}
	else
	{
		printf("Error! Incorrect input");
		fclose(startFile);
		fclose(finalFile);
		return 0;
	}

	if (!correct)
	{
		printf("Error! Incorrect input");
		fclose(startFile);
		fclose(finalFile);
	}

	return 0;
}

