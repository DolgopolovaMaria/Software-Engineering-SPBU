// task8.cpp : Defines the entry point for the console application.
//

#include <stdio.h>
#include <stdlib.h>


int readNumber()
{
	printf("Enter natural number\n");
	
	while (1)
	{
		int value = 0;
		char nextSymbol = '0';
		if ((scanf_s("%d%c", &value, &nextSymbol) == 2) && (value >= 0) && (isspace(nextSymbol)))
		{
			return value;
		}
		else
		{
			if (nextSymbol != '\n')
			{
				while (getchar() != '\n');
			}
			printf("You entered an incorrect expression. Please, enter natural number\n");
		}
	}
}

int getNumberOfWays(int amountOfMoney)
{
	int coins[8] = { 1, 2, 5, 10, 20, 50, 100, 200 };
	int *ways[8];
	for (int i = 0; i < 8; i++)
	{
		ways[i] = calloc(amountOfMoney + 1, sizeof(int*));
		ways[i][0] = 1;
	}
	for (int i = 0; i < amountOfMoney + 1; i++)
	{
		ways[0][i] = 1;
	}

	for (int indexOfMaxCoin = 1; indexOfMaxCoin < 8; indexOfMaxCoin++)
	{
		for (int currentAmountOfMoney = 1; currentAmountOfMoney <= amountOfMoney; currentAmountOfMoney++)
		{
			for (int i = 0; i * coins[indexOfMaxCoin] <= currentAmountOfMoney; i++)
			{
				ways[indexOfMaxCoin][currentAmountOfMoney] = ways[indexOfMaxCoin][currentAmountOfMoney] + ways[indexOfMaxCoin - 1][currentAmountOfMoney - i * coins[indexOfMaxCoin]];
			}
		}
	}
	
	int result = ways[7][amountOfMoney];
	for (int i = 0; i < 8; i++)
	{
		free(ways[i]);
	}
	return result;
}

int main()
{
	int amountOfMoney = readNumber();
	int ways = getNumberOfWays(amountOfMoney);
	printf("Number of ways = %d", ways);
	putchar('\n');
	
	return 0;
}

