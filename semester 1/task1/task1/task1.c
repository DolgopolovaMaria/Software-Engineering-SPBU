// task1.cpp : Defines the entry point for the console application.
//

#include <stdio.h>


const int lengthOfInt = sizeof(int) * 8;
const int lengthOfLong = sizeof(long long) * 8;


void binaryCodeInt(int number, int binaryArray[])
{
	int binary = 1;
	for (int i = lengthOfInt - 1; i >= 0; i--)
	{
		binaryArray[i] = (number & binary) == 0 ? 0 : 1;
		binary = binary << 1;
	}
}

void binaryCodeLong(long long number, int binaryArray[])
{
	long long binary = 1;
	for (int i = lengthOfLong - 1; i >= 0; i--)
	{
		binaryArray[i] = (number & binary) == 0 ? 0 : 1;
		binary = binary << 1;
	}
}

void printArray(int array[], int length, int firstIndex, int lastIndex)
{
	if ((firstIndex < 0) || (firstIndex > lastIndex) || (lastIndex >= length))
	{
		return;
	}
	for (int i = firstIndex; i <= lastIndex; i++)
	{
		printf("%d", array[i]);
	}
}

int lengthOfBinaryCode(int binaryArray[], int lengthOfArray)
{
	int numberOfFirstZeros = 0;
	int i = 0;
	while ((binaryArray[i] == 0) && (i < lengthOfArray - 1))
	{
		numberOfFirstZeros++;
		i++;
	}
	
	if (binaryArray[lengthOfArray - 1] == 0)
	{
		numberOfFirstZeros++;
	}
	
	return lengthOfArray - numberOfFirstZeros;
}

int main()
{
	int surname = 11;    // Dolgopolova
	int name = 5;    // Maria
	int patronymic = 9;    // Andreevna
	
	int composition = surname * name * patronymic;

	printf("Dolgopolova Maria Andreevna\n");
	printf("%d", composition);
	putchar('\n');

	printf("A) ");
	int binaryArray[sizeof(int) * 8];
	binaryCodeInt(-composition, binaryArray);
	printArray(binaryArray, lengthOfInt, 0, lengthOfInt - 1);
	putchar('\n');
	
	printf("B) ");
	binaryCodeInt(composition, binaryArray);
	printf("%d", 0);
	int exponent[sizeof(int) * 8];
	binaryCodeInt(lengthOfBinaryCode(binaryArray, lengthOfInt) - 1 + 127, exponent);
	printArray(exponent, lengthOfInt, lengthOfInt - 8, lengthOfInt - 1);
	printArray(binaryArray, lengthOfInt, lengthOfInt - 23, lengthOfInt - 1);
	putchar('\n');

	printf("C) ");
	printf("%d", 1);
	binaryCodeInt(lengthOfBinaryCode(binaryArray, lengthOfInt) - 1 + 1023, exponent);
	printArray(exponent, lengthOfInt, lengthOfInt - 11, lengthOfInt - 1);
	int binaryArrayLong[sizeof(long long) * 8];
	binaryCodeLong(composition, binaryArrayLong);
	printArray(binaryArrayLong, lengthOfLong, lengthOfLong - 52, lengthOfLong - 1);
	putchar('\n');
	
	return 0;
}

