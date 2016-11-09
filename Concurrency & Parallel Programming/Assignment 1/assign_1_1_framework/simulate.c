/*
 * simulate.c
 *
 * Implement your (parallel) simulation here!
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#include "simulate.h"


/* Add any global variables you may need. */

/* Data structure for the individual threads to use */
struct data {
    int i_min, i_max;
    double *old_array, *current_array, *next_array;
};

/* Add any functions you may need (like a worker) here. */
// 

/* Worker function for thread, executes a part of the wave equation */
void *wave_worker(void *arg)
{

    struct data* wave_part = (struct data *) arg;

    for (int i = wave_part->i_min; i < wave_part->i_max; i++)
    {
        wave_part->next_array[i] = 2 * wave_part->current_array[i] - wave_part->old_array[i] + 0.15 * (wave_part->current_array[i - 1] - (2 * wave_part->current_array[i] - wave_part->current_array[i + 1]));
    }

    free(wave_part);

    return 0;
}

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

    pthread_t thread_ids[num_threads];
    void *results;

    int size_of = i_max / num_threads;

    for (int t = 0; t < t_max; t++)
    {
        for (int i = 0; i < num_threads - 1; i++)
        {
            struct data* wave_part = (struct data*)malloc(sizeof(*wave_part));
            wave_part->i_min = i * size_of + 1;
            wave_part->i_max = wave_part->i_min + size_of -1;
            wave_part->old_array = old_array;
            wave_part->current_array = current_array;
            wave_part->next_array = next_array;
            pthread_create(&thread_ids[i],
                NULL,
                &wave_worker,
                wave_part);
        }

        struct data* wave_part = (struct data*)malloc(sizeof(*wave_part));
        wave_part->i_min = (num_threads - 1) * size_of + 1;
        wave_part->i_max = wave_part->i_min + size_of - 1;
        wave_part->old_array = old_array;
        wave_part->current_array = current_array;
        wave_part->next_array = next_array;
        pthread_create(thread_ids + num_threads - 1,
                NULL,
                &wave_worker,
                wave_part);

        for (int i = 0; i < num_threads; i++)
        {
            pthread_join(thread_ids[i],
                &results);
        }
    }

    // for (int t = 0; t < t_max; t++)
    // {
    //     /* Initially, next_array is memset to 0 and after rotating pointing to
    //      * old_array, constrained by "The amplitude at locations with the least
    //      * and the greatest index is fixed to zero", therefore next_array is
    //      * constrained by the same constraint.
    //      */
    //     for (int i = 1; i < i_max - 1; i++)
    //     {
    //         // TODO: threading.
    //         next_array[i] = 2 * current_array[i] - old_array[i] + 0.15 * (current_array[i - 1] - (2 * current_array[i] - current_array[i + 1]));


    //     }

    //     /*
    //      * After each timestep, you should swap the buffers around. Watch out none
    //      * of the threads actually use the buffers at that time.
    //      */
    //     double *copy_old_array = old_array;
    //     old_array = current_array;
    //     current_array = next_array;
    //     next_array = copy_old_array;
    // }

    return current_array;
}

