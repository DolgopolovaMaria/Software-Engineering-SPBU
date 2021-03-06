// task11.cpp : Defines the entry point for the console application.
//

#include <stdio.h>
#include <malloc.h>


int digitalRoot(int value)
{
	if (value % 9 == 0)
	{
		return 9;
	}
	return value % 9;
}


int main()
{
	unsigned int *MDRS = (unsigned int *)malloc(sizeof(unsigned int) * 1000000);
	
	unsigned int sum = 0;
	for (int i = 2; i <= 999999; i++)
	{
		MDRS[i] = digitalRoot(i);
		int maxDigitalRoot = 0;
		for (int j = 2; j * j <= i; j++)
		{
			if ((i % j == 0) && (digitalRoot(j) + MDRS[i / j] > maxDigitalRoot))
			{
				maxDigitalRoot = digitalRoot(j) + MDRS[i / j];
			}
		}
		if (maxDigitalRoot > MDRS[i])
		{
			MDRS[i] = maxDigitalRoot;
		}
		
		sum = sum + MDRS[i];
	}

	printf("result = %u", sum);
	putchar('\n');

	free(MDRS);
	return 0;
}

