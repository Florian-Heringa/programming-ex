/*
 * simulate.c
 *
 * Implement your (parallel) simulation here!
 */

#include <stdio.h>
#include <stdlib.h>

#include "simulate.h"


/* Add any global variables you may need. */


/* Add any functions you may need (like a worker) here. */


/*
 * Executes the entire simulation.
 *
 * Implement your code here.
 *
 * i_max: how many data points are on a single wave
 * t_max: how many iterations the simulation should run
 * num_threads: how many threads to use (excluding the main threads)
 * old_array: array of size i_max filled with data for t-1
 * current_array: array of size i_max filled with data for t
 * next_array: array of size i_max. You should fill this with t+1
 */
double *simulate(const int i_max, const int t_max, const int num_threads,
        double *old_array, double *current_array, double *next_array)
{
   

    for t in range(0, t_max):
        for i in range(1, i_max-2):
            next_array[i] = 2 * current_array[i] - old_array[i] + 0.15 * (current_array[i-1] - (2 * current_array[i] - current_array[i+1]))
        old_array = current_array
        current_array = next_array
        next_array = old_array

    /*
     * After each timestep, you should swap the buffers around. Watch out none
     * of the threads actually use the buffers at that time.
     */


    /* You should return a pointer to the array with the final results. */
    return current_array;
}
