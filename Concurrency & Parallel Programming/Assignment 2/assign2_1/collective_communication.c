/* Assignment 2.3: Collective communication.
 * Name: Niek Kabel and Florian Heringa
 * E-mail address: niek.kabel@student.uva.nl
 * Student ID: 11031174
 * Description: Implementation of the broadcast function on
 * an (imaginary) ring network topology.
 * TODO: Optimalise by introducing shortcuts of length diameter
 * (to the furthest way process) in the ring.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "collective_communication.h"
#include "mpi.h"

int MYMPI_Bcast(void *buffer, int count, MPI_Datatype datatype, int root, MPI_Comm communicator)
{
    int communicator_size, rank_in_communicator;
    int left_neighbour, right_neighbour, forward_to;
    int element_size, buffer_size;

    const int packet_fields = 2;
    int block_lengths[] = {count, 1};
    MPI_Aint displacements[] = {offsetof(Packet, message), offsetof(Packet, TTL)};
    MPI_Datatype MPI_Packettype, types[] = {datatype, MPI_INT};
    Packet pkt;

    MPI_Status stat;

    /* Query for number of MPI processes in and task ID with respect to
    the given communicator. */
    MPI_Comm_size(communicator, &communicator_size);
    MPI_Comm_rank(communicator, &rank_in_communicator);

    /* We cannot broadcast to ourselves. */
    assert(communicator_size > 1);

    /* We assume a 1-dimensional ring topology where each node only has
    communication links with its two direct neighbours with circularly
    increasing and decreasing MPI process ids. */
    left_neighbour = (rank_in_communicator - 1 + communicator_size) % communicator_size;
    right_neighbour = (rank_in_communicator + 1) % communicator_size;

    /* The number of bytes occupied by an element of the specified datatype. */
    MPI_Type_size(datatype, &element_size);
    buffer_size = count * element_size;

    /* Describe a complex datatype in terms of blocks
    with a size, displacement and datatype from a general set of datatypes. */
    MPI_Type_create_struct(packet_fields, block_lengths, displacements, types, &MPI_Packettype);
    /* A datatype object has to be committed before it can be used in communication. */
    MPI_Type_commit(&MPI_Packettype);

    /* The root MPI process initiates the broadcast to send data to all
    other processes of the communicator, while any non-root process listens for
    the broadcast to receive the corresponding data from the root process. */
    if (rank_in_communicator == root)
    {
        /* Send packet to both neighbours/in both directions in the ring. */
        pkt.message = buffer;
        /* In each direction we only need to cover half of the circle.
        However, if the communicator size is even, then we must broadcast to
        an odd number of processes and one half is smaller by one. */
        pkt.TTL = communicator_size >> 1;
        MPI_Send(&pkt, 1, MPI_Packettype, left_neighbour, root, communicator);
        pkt.TTL -= (communicator_size & 1) ^ 1;
        MPI_Send(&pkt, 1, MPI_Packettype, right_neighbour, root, communicator);
    }
    else
    {
        MPI_Recv(&pkt, 1, MPI_Packettype, MPI_ANY_SOURCE, root, communicator, &stat);

        if (stat.MPI_SOURCE == left_neighbour || stat.MPI_SOURCE == right_neighbour)
        {
            /* A clever trick to flip from the direct neighbour from whom
            we received the broadcast to the other direct neighbour
            to preserve the direction of the packet. */
            forward_to = left_neighbour + right_neighbour - stat.MPI_SOURCE;
            /* At every hop we decrement the time-to-live by one such that
            no packet can cycle around forever. */
            pkt.TTL -= 1;

            /* Forward a packet in the direction it came from iff TTL > 0. */
            if (pkt.TTL > 0)
            {
                MPI_Send(&pkt, 1, MPI_Packettype, forward_to, root, communicator);
            }

            /* Copy the received data to the buffer
            iff this is the broadcast we were listening for
            iff the broadcast is initiated from the root process and
            received from a direct neighbour. */
            memcpy(buffer, pkt.message, buffer_size);
        }
    }

    return EXIT_SUCCESS;
}
