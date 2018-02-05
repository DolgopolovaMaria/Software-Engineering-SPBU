// Task2.cpp : Defines the entry point for the console application.
//

#include < math.h > 
#include <stdio.h>


int pythagoreanTriple(int x, int y, int z)
{
	return ((pow(x, 2) + pow(y, 2) == pow(z, 2)) || (pow(x, 2) + pow(z, 2) == pow(y, 2)) || (pow(z, 2) + pow(y, 2) == pow(x, 2)));
}

void swap(int *x, int *y)
{
	int k = *x;
	*x = *y;
	*y = k;
}

int relativePrime(int x, int y, int z)
{
	if (x > y)
	{
		swap(&x, &y);
	}

	if (x > z)
	{
		swap(&x, &z);
	}

	if ((y % x == 0) && (z % x == 0))
	{
		return 0;
	}

	int i = 2;
	while (i <= sqrt(x))
	{
		if ((x % i == 0) && (y % i == 0) && (z % i == 0))
		{
			return 0;
		}
		i++;
	}
	if ((y % x == 0) && (z % x == 0))
	{
		return 0;
	}

	return 1;
}

int primitivePythagoreanTriple(int x, int y, int z)
{
	return relativePrime(x, y, z);
}

int readNumber(char name, int *value)
{
	int input = 0;
	while (!input)
	{
		printf("%c", name);
		printf(" = ");
		char nextSymbol = '0';
		if ((scanf_s("%d%c", &*value, &nextSymbol) == 2) && (*value > 0) && (isspace(nextSymbol)))
		{
			input = 1;
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

int main()
{
	printf("enter 3 natural numbers\n");
	int x = 0;
	readNumber('x', &x);
	int y = 0;
	readNumber('y', &y);
	int z = 0;
	readNumber('z', &z);

	if (pythagoreanTriple(x, y, z))
	{
		printf("These numbers are Pythagorean triple\n");
		if (primitivePythagoreanTriple(x, y, z))
		{
			printf("These numbers are primitive Pythagorean triple\n");
		}
		else
		{
			printf("These numbers are not primitive Pythagorean triple\n");
		}
	}
	else
	{
		printf("These numbers are not Pythagorean triple\n");
	}

	return 0;
}