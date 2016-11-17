/* Assignment 2.1: Parallel simulation of a wave equation in MPI.
 * Name: Niek Kabel
 * E-mail address: niek.kabel@student.uva.nl
 * Student ID: 11031174
 * Description: Explicit domain decomposition with halo cells.
 */

#include <stdio.h>
#include <stdlib.h>

#include "simulate.h"
#include "mpi.h"

/* A specific wave equation as recurrence relation. */
inline double recurrence_relation(const double *const old_array, const double *const current_array, int i)
{
    static const float spatial_impact = 0.15;
    return (2 * current_array[i] - old_array[i] + spatial_impact *
        (current_array[i - 1] - (2 * current_array[i] - current_array[i + 1])));
}

/* Wave equation simulation
 * For each time step, run the simulation of the equation of a wave
 * parallelized over all amplitude points using multiple compute nodes
 * in a distributed memory system.
 *
 * Arguments:
 * i_max: how many data points are on a single wave.
 * t_max: how many iterations the simulation should run.
 * old_array: array of size i_max filled with data for t-1.
 * current_array: array of size i_max filled with data for t.
 * next_array: array of size i_max that should be filled with data for t+1
 * for each iteration/time step.
 */
double *simulate(const int i_max, const int t_max, double *old_array,
        double *current_array, double *next_array)
{
    int communicator_size, rank_in_communicator;
    int left_neighbour, right_neighbour;
    MPI_Request dont_care, req_left_halo_cell, req_right_halo_cell;
    int total_points, points_per_process, points_left_over, from, to, spread, slice[2], range;
    const int farmer = 0, tag = 0;

    /* Query for number of MPI processes in and task ID with respect to
    the standard communicator. */
    MPI_Comm_size(MPI_COMM_WORLD, &communicator_size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank_in_communicator);

    /* A property of the workload distribution algorithm. */
    left_neighbour = (rank_in_communicator - 1 + communicator_size) % communicator_size;
    right_neighbour = (rank_in_communicator + 1) % communicator_size;

    /* The farmer will distribute the workload equally over all workers
    and spread the left-over as much as possible (fine grained because
    left-over < communicator_size) over all MPI processes, weighted on rank. */
    if (rank_in_communicator == farmer)
    {
        /* The amplitude at locations with the least and the greatest index
        should be fixed to zero and therefore are not considered. */
        total_points = i_max - 2;
        points_per_process = total_points / communicator_size;
        points_left_over = total_points % communicator_size;
        
        from = 1;
        to = points_per_process;
        spread = points_left_over;
        for (int rank = 1; rank < communicator_size; rank++)
        {
            if (spread > 0)
            {
                to += 1;
                spread -= 1;
            }

            slice[0] = from;
            slice[1] = to;
            MPI_Send(slice, 2, MPI_INT, rank, tag, MPI_COMM_WORLD);

            from = to + 1;
            to += points_per_process;
        }
    }
    else
    {
        MPI_Recv(slice, 2, MPI_INT, farmer, tag, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        from = slice[0];
        to = slice[1];
    }
    /* Either the range [from, to] is defined by the slice received from the farmer,
    or, for the farmer himself, a by-product of the incremental work distribution. */

    for (int t = 0; t < t_max; t++)
    {
        /* One is allowed to call MPI_WAIT with a null or inactive request argument.
        In this case the operation returns immediately with empty status. */
        req_left_halo_cell = MPI_REQUEST_NULL;
        req_right_halo_cell = MPI_REQUEST_NULL;

        // TODO: Optimalise by only updating halo cells if t > 0. Tested, yields higher execution times.
        /* Communication is expensive overhead solved by overlapping
        communication and computation, updating the halo cells and computing
        the amplitude of every point in between, respectively. */
        if (rank_in_communicator != 1)
        {
            /* Initiate communication as early as possible. */
            MPI_Isend(&current_array[from], 1, MPI_DOUBLE, left_neighbour, tag, MPI_COMM_WORLD, &dont_care);
            MPI_Request_free(&dont_care);
            MPI_Irecv(&current_array[from - 1], 1, MPI_DOUBLE, left_neighbour, tag, MPI_COMM_WORLD, &req_left_halo_cell);
        }
        if (rank_in_communicator != 0)
        {
            MPI_Isend(&current_array[to], 1, MPI_DOUBLE, right_neighbour, tag, MPI_COMM_WORLD, &dont_care);
            MPI_Request_free(&dont_care);
            MPI_Irecv(&current_array[to + 1], 1, MPI_DOUBLE, right_neighbour, tag, MPI_COMM_WORLD, &req_right_halo_cell);
        }

        for (int i = from + 1; i < to; i++)
        {
            next_array[i] = recurrence_relation(old_array, current_array, i);
        }

        /* Complete communication as late as possible.
        We do not need to wait for the completion of sending
        since the order of the messages is preserved. */
        MPI_Wait(&req_left_halo_cell, MPI_STATUS_IGNORE);
        next_array[from] = recurrence_relation(old_array, current_array, from);
        MPI_Wait(&req_right_halo_cell, MPI_STATUS_IGNORE);
        next_array[to] = recurrence_relation(old_array, current_array, to);

        /* After each time step, rotate the buffers around. */
        double *copy_old_array = old_array;
        old_array = current_array;
        current_array = next_array;
        /* Initially, next_array is memset to 0 and after rotating pointing to
        old_array, constrained by "The amplitude at locations with the least
        and the greatest index should be fixed to zero", therefore
        next_array is constrained by the same constraint. */
        next_array = copy_old_array;
    }

    // TODO: Optimalise by receiving slices in any order / from MPI_ANY_SOURCE. However, the incremental version of computing the range of every slice relies on order.
    /* The farmer will reduce all distributed slices of the amplitudes at every
    point in that slice to one complete array if all the workers send theirs. */ 
    if (rank_in_communicator == farmer)
    {
        from = 1;
        to = points_per_process;
        spread = points_left_over;
        for (int rank = 1; rank < communicator_size; rank++)
        {
            if (spread > 0)
            {
                to += 1;
                spread -= 1;
            }

            range = to - from + 1;
            MPI_Recv(&current_array[from], range, MPI_DOUBLE, rank, tag, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            from = to + 1;
            to += points_per_process;
        }
    }
    else
    {
        range = to - from + 1;
        MPI_Send(&current_array[from], range, MPI_DOUBLE, farmer, tag, MPI_COMM_WORLD);
    }

    return current_array;
}
