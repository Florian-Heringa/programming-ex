/*
 * simulate_sequential.c\
 * Florian Heringa
 * 10385835
 * 11-11-2016
 *
 * Sequential implementation of the wave equation.
 * Every time iteration (t) the buffers are swapped and the wave is recalculated,
 * in the space iterations (i) the code runs throug each point and calculates the new
 * value depending on the old value and it's neighbors.
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#include "simulate.h"

double *simulate(const int i_max, const int t_max, const int num_threads,
        double *old_array, double *current_array, double *next_array)
{

	// time loop
    for (int t = 0; i < t_max; t++)
    {
    	// space loop
        for (int i = 0; i < i_max; i++)
        {
        	// wave equation, new value is a function of old value and the neighbors.
            next_array[i] = 2 * current_array[i] - old_array[i] + 0.15 * (current_array[i-1] - (2 * current_array[i] - current_array[i+1]));
        }

        // swapping of buffers using a swap pointer
        double *copy_old_array = old_array;
        old_array = current_array;
        current_array = next_array;
        next_array = copy_old_array;
    }

    return current_array;
}

