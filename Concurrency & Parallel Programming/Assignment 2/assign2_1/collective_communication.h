/*
 * collective_communication.h
 */

#pragma once
#include "mpi.h"

typedef struct Packet {
    void *message;
    int TTL;
} Packet;

int MYMPI_Bcast(void *buffer, int count, MPI_Datatype datatype, int root,
    MPI_Comm communicator);
