// Task5.cpp : Defines the entry point for the console application.
//

#include <stdio.h>
#include < math.h >


int readNumber()
{
	int input = 0;
	int value = 0;
	printf("Enter a number\n");
	while (!input)
	{
		char nextSymbol = '0';
		if ((scanf_s("%d%c", &value, &nextSymbol) == 2) && (value > 0) && (isspace(nextSymbol)) && ((int)sqrt(value) != sqrt(value)))
		{
			input = 1;
			return value;
		}
		else
		{
			if (nextSymbol != '\n')
			{
				while (getchar() != '\n');
			}
			printf("You entered an incorrect expression. Please, enter a valide number\n");
		}
	}
}

void printFractionPeriod(int value)
{
	int a0 = (int)sqrt(value);    // sqrt(value) = a0 + 1/x1;
	printf("Fraction = [ %d", a0);
	printf("; ");
	
	// a = [(sqrt(value) + top) / denominator]
	// x = d / (sqrt(value) + top - a * denominator) = (denominator(sqrt(value) + a * denominator - top) / (value - (a * denominator - top)^2)
	int topNumber = a0;    // top = a * denominator - top;
	int denominator = value - a0 * a0;    // denominator = (v - top^2) / denominator;
	int a = a0;
	int period = 0;
	while ((denominator != 1) || (topNumber != a0))
	{
		a = (int)(sqrt(value) + topNumber) / denominator;
		period++;
		printf("%d", a);
		printf(", ");
		topNumber = a * denominator - topNumber;
		denominator = (value - topNumber * topNumber) / denominator;
	}

	period++;
	printf("%d", a0 * 2);
	printf(" ]\n");
	printf("period = %d", period);
	putchar('\n');
}

int main()
{
	int value = readNumber();
	printFractionPeriod(value);
	return 0;
}

