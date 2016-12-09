/* Niek Kabel (11031174) and Florian Heringa. */
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <iostream>
#include "timer.h"

using namespace std;

int fileSize() {
  int size; 

  ifstream file ("original.data", ios::in|ios::binary|ios::ate);
  if (file.is_open())
  {
    size = file.tellg();
    file.close();
  }
  else {
    cout << "Unable to open file";
    size = -1; 
  }
  return size; 
}

int readData(char *fileName, char *data) {

  streampos size;

  ifstream file (fileName, ios::in|ios::binary|ios::ate);
  if (file.is_open())
  {
    size = file.tellg();
    file.seekg (0, ios::beg);
    file.read (data, size);
    file.close();

    cout << "The entire file content is in memory." << endl;
  }
  else cout << "Unable to open file" << endl;
  return 0;
}

/* Utility function, use to do error checking.
   Use this function like this:
   checkCudaCall(cudaMalloc((void **) &deviceRGB, imgS * sizeof(color_t)));
   And to check the result of a kernel invocation:
   checkCudaCall(cudaGetLastError());
*/
static void checkCudaCall(cudaError_t result) {
    if (result != cudaSuccess) {
        cerr << "cuda error: " << cudaGetErrorString(result) << endl;
        exit(1);
    }
}

__global__ void checksumKernel(char* deviceChilds, char *deviceParents, int threads) {
    unsigned index = blockIdx.x * blockDim.x + threadIdx.x;
    if (index < threads) {
        // We write the parent of two neighbour nodes on the position of
        // the pair's index, thereby constructing a right-angled triangle
        // which will prove usefull for adding dont-care parents.
        deviceParents[index] = deviceChilds[2 * index] + deviceChilds[2 * index + 1];
        // We can simply always add a dont-care parent to avoid GPU branching
        // because it is always written in the second half which we disregard
        // in subsequent levels because of the right-angled triangle structure.
        deviceParents[threads] = 0;
    }
}

int checksumCuda (char *filedata, int filesize) {
    int threadBlockSize = 512;

    // Allocate a block for file data on the GPU.
    // We can only neatly replace the childs with their parents if
    // the kernels executed in order. However we do not know the order in
    // which kernels are scheduled to execute and therefore
    // must split the parents from the childs and rotate between them.
    char* deviceChilds = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceChilds, filesize * sizeof(char)));
    if (deviceChilds == NULL) {
        cerr << "Could not allocate memory" << endl;
        return -1;
    }
    char* deviceParents = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceParents, filesize * sizeof(char)));
    if (deviceParents == NULL) {
        checkCudaCall(cudaFree(deviceChilds));
        cerr << "Could not allocate memory" << endl;
        return -1;
    }
    char* tmp;

    timer kernelTime = timer("kernelTime");
    timer memoryTime = timer("memoryTime");

    // Copy the file data to the GPU.
    memoryTime.start();
    checkCudaCall(cudaMemcpy(deviceChilds, filedata, filesize * sizeof(char), cudaMemcpyHostToDevice));
    memoryTime.stop();

    // Execute kernel: we keep reducing from leafs to the root's childs by
    // invoking half as many kernels as nodes, each of which will reduce
    // two neighbour nodes by taking their sum, to construct the parent node.
    // We correct for an odd number of parents by adding a dont-care parent.
    // Initially we assume the number of leafs is divisible by two.
    // Finally we conjecture that the process of dividing a number if even or
    // increment by one if odd will eventually reach one (not as difficult as
    // Collatz conjecture). Its proof proves the finiteness of below loop.
    kernelTime.start();
    for (int threads = filesize / 2; threads > 1; threads /= 2) {
        checksumKernel<<<threads / threadBlockSize + 1, threadBlockSize>>>(deviceChilds, deviceParents, threads);
        if (threads & 1 == 1) {
            threads++;
        }
        cudaDeviceSynchronize();
        tmp = deviceChilds;
        deviceChilds = deviceParents;
        deviceParents = tmp;
    }
    kernelTime.stop();

    // Check whether the kernel invocation was successful.
    checkCudaCall(cudaGetLastError());

    // Copy the smallest level (the root's childs) of the binary tree back.
    memoryTime.start();
    checkCudaCall(cudaMemcpy(filedata, deviceChilds, 2 * sizeof(char), cudaMemcpyDeviceToHost));
    memoryTime.stop();

    checkCudaCall(cudaFree(deviceChilds));
    checkCudaCall(cudaFree(deviceParents));

    cout << fixed << setprecision(6);
    cout << "Encrypt (kernel): \t\t" << kernelTime.getElapsed() << " seconds." << endl;
    cout << "Encrypt (memory): \t\t" << memoryTime.getElapsed() << " seconds." << endl;

    return 0;
}

int main(int argc, char* argv[]) {
    int filesize;
    char *filename, *filedata;

    if (argc < 2) {
      cerr << "Usage: " << argv[0] << " filename" << endl;
      exit(1);
    }
    filename = argv[1];

    filesize = fileSize();
    if (!filesize > 0) {
      cerr << "Filename " << filename << " not found or empty" << endl; 
      exit(1);
    }
    // Rule: when bottom-up constructing a binary tree or reducing from
    // leafs (finest-grained) - character values - to root - their total sum,
    // the amount of nodes must be divisible by two.
    // We can only assume this rule always holds when either we
    // 1. correct at every level by adding a dont-care node if necessary (odd number of nodes) or
    // 2. round the number of leafs to the nearest factor of two.
    // The former will introduce more GPU branching while
    // the latter will stress the CPU to GPU already-botleneck communication.
    // We opted for the former because we optimalised the branching away.
    if (filesize & 1 == 1) {
        filesize++;
    }

    filedata = new char[filesize];
    // If we added a dont-care node, make sure its value is irrelevant for the total sum.
    filedata[filesize - 1] = 0;  
    readData(filename, filedata);

    cout << "Calculating the checksum of " << filename << " (" << filesize << " characters)" << endl;

    checksumCuda(filedata, filesize);
    // Reduce the smallest level of the binary tree on the CPU.
    cout << "Checksum: " << (int)(filedata[0] + filedata[1]) << endl;
 
    delete[] filedata;
    
    return 0;
}