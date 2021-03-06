// task9.cpp : Defines the entry point for the console application.
//
#include "memory.h"


int main()
{
	init();
	
	printf("Malloc test:\n");
	int *testArrayOfInt = myMalloc(sizeof(int) * 10);
	printf("elements of testArrayOfInt: ");
	for (int i = 0; i < 10; i++)
	{
		testArrayOfInt[i] = i + 1;
		printf("%d ", testArrayOfInt[i]);
	}
	putchar('\n');
	printf("address of testArrayOfInt: %p", testArrayOfInt);
	putchar('\n');
	putchar('\n');

	int *testArrayOfDouble = myMalloc(sizeof(double) * 3);
	printf("elements of testArrayOfDouble: ");
	for (int i = 0; i < 3; i++)
	{
		testArrayOfDouble[i] = i + 1;
		printf("%d ", testArrayOfDouble[i]);
	}
	putchar('\n');
	printf("address of testArrayOfDouble: %p", testArrayOfDouble);
	putchar('\n');
	putchar('\n');
	putchar('\n');

	
	printf("Realloc test:\n");
	testArrayOfInt = myRealloc(testArrayOfInt, sizeof(int) * 13);
	testArrayOfInt[10] = 11;
	testArrayOfInt[11] = 12;
	testArrayOfInt[12] = 13;
	printf("Now testArrayOfInt has 13 elements. Elements of testArrayOfInt: ");
	for (int i = 0; i < 13; i++)
	{
		printf("%d ", testArrayOfInt[i]);
	}
	putchar('\n');
	printf("address of testArrayOfInt: %p", testArrayOfInt);
	putchar('\n');
	putchar('\n');

	testArrayOfInt = myRealloc(testArrayOfInt, sizeof(int) * 7);
	printf("Now testArrayOfInt has 7 elements. Elements of testArrayOfInt: ");
	for (int i = 0; i < 7; i++)
	{
		printf("%d ", testArrayOfInt[i]);
	}
	putchar('\n');
	printf("address of testArrayOfInt: %p", testArrayOfInt);
	putchar('\n');

	printf("Check testArrayOfDouble");
	putchar('\n');
	printf("elements of testArrayOfDouble: ");
	for (int i = 0; i < 3; i++)
	{
		printf("%d ", testArrayOfDouble[i]);
	}
	putchar('\n');
	putchar('\n');
	putchar('\n');


	printf("Free test:\n");
	myFree(testArrayOfDouble);
	printf("Now testArrayOfDouble is free. Check testArrayOfInt\n");
	printf("elements of testArrayOfInt: ");
	for (int i = 0; i < 7; i++)
	{
		printf("%d ", testArrayOfInt[i]);
	}
	putchar('\n');

	myFree(testArrayOfInt);

	memoryEnd();

	return 0;
}

