// task3.cpp : Defines the entry point for the console application.
//

#include <math.h>
#include <stdio.h>


int triangle(float x, float y, float z)
{
	return ((x + y > z) && (x + z > y) && (z + y > x));
}

float calculateAngle(float a, float b, float c)
{
	return acos((pow(a, 2) + pow(b, 2) - pow(c, 2)) / (2 * a * b)) * 180.0 / 3.14159;
}

void angles(float firstSide, float secondSide, float thirdSide, float *firstAngle, float *secondAngle, float *thirdAngle)
{
	*firstAngle = calculateAngle(secondSide, thirdSide, firstSide);
	*secondAngle = calculateAngle(firstSide, thirdSide, secondSide);
	*thirdAngle = calculateAngle(firstSide, secondSide, thirdSide);
}

void minutesSeconds(float number, int *degrees, int *minutes, int *seconds)
{
	float temp = 0;
	float afterPoint = modff(number, &temp);
	*degrees = number - afterPoint;
	float min = afterPoint * 60;
	afterPoint = modff(min, &temp);
	*minutes = min - afterPoint;
	*seconds = afterPoint * 60;
}

int readNumber(char name, int *value)
{
	int input = 0;
	while (!input)
	{
		printf("%c", name);
		printf(" = ");
		char nextSymbol = '0';
		if ((scanf_s("%f%c", &*value, &nextSymbol) == 2) && (*value > 0) && (isspace(nextSymbol)))
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

void printResult(int degrees, int minutes, int seconds)
{
	printf("%d", degrees);
	printf("*%d", minutes);
	printf("'%d", seconds);
	printf("\"\n");
}

int main()
{
	printf("enter 3 numbers\n");
	float x = 0;
	readNumber('x', &x);
	float y = 0;
	readNumber('y', &y);
	float z = 0;
	readNumber('z', &z);

	if (triangle(x, y, z))
	{
		printf("Draw a triangle is possible\n");

		float firstAngle = 0;
		float secondAngle = 0;
		float thirdAngle = 0;
		angles(x, y, z, &firstAngle, &secondAngle, &thirdAngle);

		int degrees = 0;
		int minutes = 0;
		int seconds = 0;

		minutesSeconds(firstAngle, &degrees, &minutes, &seconds);
		printf("first angle = ");
		printResult(degrees, minutes, seconds);

		minutesSeconds(secondAngle, &degrees, &minutes, &seconds);
		printf("second angle = ");
		printResult(degrees, minutes, seconds);

		minutesSeconds(thirdAngle, &degrees, &minutes, &seconds);
		printf("third angle = ");
		printResult(degrees, minutes, seconds);
	}
	else
	{
		printf("Draw a triangle is not possible\n");
	}
	return 0;
}
