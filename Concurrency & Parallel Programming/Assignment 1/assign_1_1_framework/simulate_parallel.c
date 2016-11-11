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
#include <pthread.h>

#include "simulate.h"

inline int min(int a, int b)
{
    return (a < b ? a : b);
}

// Wave struct for distribution of data between threads
struct wave
{
    double (*wave_equation)(const double *const old_array, const double *const current_array, int i);
    double *old_array, *current_array, *next_array;
    int i_min, i_max;
};

// The function with the wave equation, used in the threads
double wave_equation(const double *const old_array, const double *const current_array, int i)
{
    double c = 0.15;
    return (2 * current_array[i] - old_array[i] + c * (current_array[i - 1] - (2 * current_array[i] - current_array[i + 1])));
}

// The total function used in per thread, executing an instance of wave_equation every iteration
void *simulate_wave_equation(void *arg)
{
    struct wave *interval = (struct wave *)arg;

    for (int i = interval->i_min; i <= interval->i_max; i++)
    {
        interval->next_array[i] = interval->wave_equation(interval->old_array, interval->current_array, i);
    }

    free(interval);
    pthread_exit(NULL);
}

/*
 * Executes the entire simulation.
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
    // make a list of thread ids
    pthread_t thread_ids[num_threads];

    /* The amplitude at locations with the least and the greatest index
    should be fixed to zero and therefore are not considered. */
    const int discrete_amplitude_points = i_max - 2;
    const int discrete_amplitude_points_per_thread = discrete_amplitude_points / num_threads;
    const int discrete_amplitude_points_left_over = discrete_amplitude_points % num_threads;
    int from, to, spread;
    void *result;

    for (int t = 0; t < t_max; t++)
    {
        // divide the work equally in same sized chunks initially
        from = 1;
        to = discrete_amplitude_points_per_thread;
        spread = discrete_amplitude_points_left_over;
        for (int i = 0; i < min(num_threads, discrete_amplitude_points); i++)
        {
            // reserve memory for thread information (data pointers and range)
            struct wave *interval = (struct wave *)malloc(sizeof(struct wave));
            if (interval)
            {
                interval->wave_equation = &wave_equation;
                interval->old_array = old_array;
                interval->next_array = next_array;
                interval->current_array = current_array;

                // if the points are not equally divisible by number of threads, divide them over lower threads
                if (spread)
                {
                    to += 1;
                    spread -= 1;
                }
                interval->i_min = from;
                interval->i_max = to;
                from = to + 1;
                to += discrete_amplitude_points_per_thread;
                ///////////////////////////////printf("[%d, %d]", interval->i_min, interval->i_max);
                pthread_create(thread_ids + i, NULL, &simulate_wave_equation, interval);
            }
        }

        /* Wait for all threads to terminate so that we can safely rotate
        the buffers and simulate the next wave. */
        for (int i = 0; i < min(num_threads, discrete_amplitude_points); i++)
        {
            pthread_join(thread_ids[i], &result);
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