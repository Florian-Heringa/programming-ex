#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <iostream>
#include "timer.h"
#include "file.h"

using namespace std;

typedef float (*func_t)(float x);

/*
 * Simple gauss with mu=0, sigma^1=1
 */
float gauss(float x)
{
    return exp((-1 * x * x) / 2);
}

/*
 * Fills a given array with samples of a given function. This is used to fill
 * the initial arrays with some starting data, to run the simulation on.
 *
 * The first sample is placed at array index `offset'. `range' samples are
 * taken, so your array should be able to store at least offset+range doubles.
 * The function `f' is sampled `range' times between `sample_start' and
 * `sample_end'.
 */
void fill(float *array, int offset, int range, float sample_start,
        float sample_end, func_t f)
{
    int i;
    float dx;

    dx = (sample_end - sample_start) / range;
    for (i = 0; i < range; i++) {
        array[i + offset] = f(sample_start + i * dx);
    }
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

__global__ void simulateWaveKernel(float* deviceA, float* deviceB, float* deviceResult, int i_max) {
    const float spatial_impact = 0.15;
    // Do not compute the first and last amplitude points.
    unsigned index = blockIdx.x * blockDim.x + threadIdx.x + 1;
    if (index < i_max - 1) {
        deviceResult[index] = (2 * deviceB[index] - deviceA[index] + spatial_impact *
            (deviceB[index - 1] - (2 * deviceB[index] - deviceB[index + 1])));
    }
}

void simulateWaveCuda(int i_max, int t_max, float* old, float* current, float* next) {
    int threadBlockSize = 512;

    float* deviceA = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceA, i_max * sizeof(float)));
    if (deviceA == NULL) {
        cerr << "could not allocate memory!" << endl;
        return;
    }
    float* deviceB = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceB, i_max * sizeof(float)));
    if (deviceB == NULL) {
        checkCudaCall(cudaFree(deviceA));
        cerr << "could not allocate memory!" << endl;
        return;
    }
    float* deviceResult = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceResult, i_max * sizeof(float)));
    if (deviceResult == NULL) {
        checkCudaCall(cudaFree(deviceA));
        checkCudaCall(cudaFree(deviceB));
        cerr << "could not allocate memory!" << endl;
        return;
    }

    cudaEvent_t start, stop;
    cudaEventCreate(&start);
    cudaEventCreate(&stop);

    // copy the original vectors to the GPU
    checkCudaCall(cudaMemcpy(deviceA, old, i_max * sizeof(float), cudaMemcpyHostToDevice));
    checkCudaCall(cudaMemcpy(deviceB, current, i_max * sizeof(float), cudaMemcpyHostToDevice));

    // execute kernel
    cudaEventRecord(start, 0);
    for (int t = 1; t < t_max; t++) {
        simulateWaveKernel<<<(i_max - 2) / threadBlockSize + 1, threadBlockSize>>>(deviceA, deviceB, deviceResult, i_max);
        float *cdeviceA = deviceA;
        deviceA = deviceB;
        deviceB = deviceResult;
        deviceResult = cdeviceA;
    }
    cudaEventRecord(stop, 0);

    // check whether the kernel invocation was successful
    checkCudaCall(cudaGetLastError());

    // copy result back
    checkCudaCall(cudaMemcpy(current, deviceResult, i_max * sizeof(float), cudaMemcpyDeviceToHost));

    checkCudaCall(cudaFree(deviceA));
    checkCudaCall(cudaFree(deviceB));
    checkCudaCall(cudaFree(deviceResult));

    // print the time the kernel invocation took, without the copies!
    float elapsedTime;
    cudaEventElapsedTime(&elapsedTime, start, stop);
    
    cout << "kernel invocation took " << elapsedTime << " milliseconds" << endl;
}

int main(int argc, char* argv[]) {
    float *old, *current, *next;
    int i_max, t_max;

    if (argc < 3) {
        cerr << "Usage: " << argv[0] << " i_max t_max [initial_data]" << endl;
        return EXIT_FAILURE;
    }

    i_max = atoi(argv[1]);
    t_max = atoi(argv[2]);

    if (i_max < 3) {
        cerr << "Argument error: i_max should be > 2." << endl;
        return EXIT_FAILURE;
    }
    if (t_max < 1) {
        cerr << "Argument error: t_max should be >= 1." << endl;
        return EXIT_FAILURE;
    }

    old = new float[i_max];
    current = new float[i_max];
    next = new float[i_max];

    memset(old, 1, i_max * sizeof(float));
    memset(current, 2, i_max * sizeof(float));
    memset(next, 0, i_max * sizeof(float));

    /* How should we will our first two generations? */
    if (argc > 3) {
        if (strcmp(argv[3], "sin") == 0) {
            fill(old, 1, i_max/4, 0, 2*3.14, sin);
            fill(current, 2, i_max/4, 0, 2*3.14, sin);
        } else if (strcmp(argv[3], "sinfull") == 0) {
            fill(old, 1, i_max-2, 0, 10*3.14, sin);
            fill(current, 2, i_max-3, 0, 10*3.14, sin);
        } else if (strcmp(argv[3], "gauss") == 0) {
            fill(old, 1, i_max/4, -3, 3, gauss);
            fill(current, 2, i_max/4, -3, 3, gauss);
        } else if (strcmp(argv[3], "file") == 0) {
            if (argc < 6) {
                cerr << "No files specified!" << endl;
                return EXIT_FAILURE;
            }
            file_read_float_array(argv[4], old, i_max);
            file_read_float_array(argv[5], current, i_max);
        } else {
            cerr << "Unknown initial mode: " << argv[3] << endl;
            return EXIT_FAILURE;
        }
    } else {
        /* Default to sinus. */
        fill(old, 1, i_max/4, 0, 2*3.14, sin);
        fill(current, 2, i_max/4, 0, 2*3.14, sin);
    }

    timer simulateWaveTimer("simulate wave timer");
    simulateWaveTimer.start();
    simulateWaveCuda(i_max, t_max, old, current, next);
    simulateWaveTimer.stop();
    cout << simulateWaveTimer;

    file_write_float_array("result.txt", current, i_max);
            
    delete[] old;
    delete[] current;
    delete[] next;
    
    return EXIT_SUCCESS;
}
