/*
 * simulate_parallel.c\
 * Florian Heringa
 * 10385835
 * 11-11-2016
 *
 * Parallel implementation of the wave equation using pthreads.
 * a wave struct is defined where the data per thread is determined.
 * 
 */


#include <stdio.h>
#include <stdlib.h>

#include "simulate.h"
#include "omp.h"

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
    
    // Set number of threads to input
    omp_set_num_threads(num_threads);

    for (int t = 0; t < t_max; t++){

        // parallelize the inner loop
        # pragma omp parallel for
        for (int i = 0; i < min(num_threads, discrete_amplitude_points); i++) {
            next_array[i] = 2 * current_array[i] - old_array[i] + 0.15 * (current_array[i-1] - (2 * current_array[i] - current_array[i+1]));
        }

        double *copy_old_array = old_array;
        old_array = current_array;
        current_array = next_array;
        /* Initially, next_array is memset to 0 and after rotating pointing to
        old_array, constrained by "The amplitude at locations with the least
        and the greatest index should be fixed to zero", therefore
        next_array is constrained by the same constraint. */
        next_array = copy_old_array;
    }
    
    return current_array;
}
