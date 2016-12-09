#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <iostream>
#include "timer.h"

using namespace std;

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


__global__ void encryptKernel(char* deviceDataIn, char* deviceDataOut, int *keys, int *num_keys) {
    unsigned index = blockIdx.x * blockDim.x + threadIdx.x;
    deviceDataOut[index] = deviceDataIn[index] + keys[index % *num_keys];
}

__global__ void decryptKernel(char* deviceDataIn, char* deviceDataOut, int *keys, int *num_keys) {
    unsigned index = blockIdx.x * blockDim.x + threadIdx.x;
    deviceDataOut[index] = deviceDataIn[index] - keys[index % *num_keys];
}

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

int writeData(int size, char *fileName, char *data) {
  ofstream file (fileName, ios::out|ios::binary|ios::trunc);
  if (file.is_open())
  {
    file.write (data, size);
    file.close();

    cout << "The entire file content was written to file." << endl;
    return 0;
  }
  else cout << "Unable to open file";

  return -1; 
}

int EncryptSeq (int n, char* data_in, char* data_out, int *keys, int num_keys) 
{  
  int i;
  timer sequentialTime = timer("Sequential encryption");
  
  sequentialTime.start();
  for (i=0; i<n; i++) { data_out[i]=data_in[i] + keys[i % num_keys]; }
  sequentialTime.stop();

  cout << fixed << setprecision(6);
  cout << "Encryption (sequential): \t\t" << sequentialTime.getElapsed() << " seconds." << endl;
  
  return 0; 
}

int DecryptSeq (int n, char* data_in, char* data_out, int *keys, int num_keys)
{
  int i;
  timer sequentialTime = timer("Sequential decryption");

  sequentialTime.start();
  for (i=0; i<n; i++) { data_out[i]=data_in[i] - keys[i % num_keys]; }
  sequentialTime.stop();

  cout << fixed << setprecision(6);
  cout << "Decryption (sequential): \t\t" << sequentialTime.getElapsed() << " seconds." << endl;

  return 0;
}


int EncryptCuda (int n, char* data_in, char* data_out, int *keys, int num_keys) {
    int threadBlockSize = 512;

    // allocate the vectors on the GPU
    char* deviceDataIn = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceDataIn, n * sizeof(char)));
    if (deviceDataIn == NULL) {
        cout << "could not allocate memory!" << endl;
        return -1;
    }
    char* deviceDataOut = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceDataOut, n * sizeof(char)));
    if (deviceDataOut == NULL) {
        checkCudaCall(cudaFree(deviceDataIn));
        cout << "could not allocate memory!" << endl;
        return -1;
    }
    int* deviceKeys = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceKeys, num_keys * sizeof(int)));
    if (deviceDataOut == NULL) {
        checkCudaCall(cudaFree(deviceDataIn));
	checkCudaCall(cudaFree(deviceDataOut));
        cout << "could not allocate memory!" << endl;
        return -1;
    }
    int* deviceNumKeys = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceNumKeys, sizeof(int)));
    if (deviceNumKeys == NULL) {
        checkCudaCall(cudaFree(deviceDataIn));
	checkCudaCall(cudaFree(deviceDataOut));
        checkCudaCall(cudaFree(deviceKeys));
        cout << "could not allocate memory!" << endl;
        return -1;
    }

    timer kernelTime1 = timer("kernelTime");
    timer memoryTime = timer("memoryTime");

    // copy the original vectors to the GPU
    memoryTime.start();
    checkCudaCall(cudaMemcpy(deviceDataIn, data_in, n*sizeof(char), cudaMemcpyHostToDevice));
    checkCudaCall(cudaMemcpy(deviceKeys, keys, num_keys*sizeof(int), cudaMemcpyHostToDevice));
    checkCudaCall(cudaMemcpy(deviceNumKeys, &num_keys, sizeof(int), cudaMemcpyHostToDevice));
    memoryTime.stop();

    // execute kernel
    kernelTime1.start();
    encryptKernel<<<n/threadBlockSize, threadBlockSize>>>(deviceDataIn, deviceDataOut, deviceKeys, deviceNumKeys);
    for (int i = n/threadBlockSize * threadBlockSize; i < n; i++) {
      data_out[i] = data_in[i] + keys[i % num_keys];
    }
    cudaDeviceSynchronize();
    kernelTime1.stop();

    // check whether the kernel invocation was successful
    checkCudaCall(cudaGetLastError());

    // copy result back
    memoryTime.start();
    checkCudaCall(cudaMemcpy(data_out, deviceDataOut, n/threadBlockSize * threadBlockSize * sizeof(char), cudaMemcpyDeviceToHost));
    memoryTime.stop();

    checkCudaCall(cudaFree(deviceDataIn));
    checkCudaCall(cudaFree(deviceDataOut));

    cout << fixed << setprecision(6);
    cout << "Encrypt (kernel): \t\t" << kernelTime1.getElapsed() << " seconds." << endl;
    cout << "Encrypt (memory): \t\t" << memoryTime.getElapsed() << " seconds." << endl;

   return 0;
}

int DecryptCuda (int n, char* data_in, char* data_out, int *keys, int num_keys) {
    int threadBlockSize = 512;

    // allocate the vectors on the GPU
    char* deviceDataIn = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceDataIn, n * sizeof(char)));
    if (deviceDataIn == NULL) {
        cout << "could not allocate memory!" << endl;
        return -1;
    }
    char* deviceDataOut = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceDataOut, n * sizeof(char)));
    if (deviceDataOut == NULL) {
        checkCudaCall(cudaFree(deviceDataIn));
        cout << "could not allocate memory!" << endl;
        return -1;
    }
    int* deviceKeys = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceKeys, num_keys * sizeof(int)));
    if (deviceDataOut == NULL) {
        checkCudaCall(cudaFree(deviceDataIn));
	checkCudaCall(cudaFree(deviceDataOut));
        cout << "could not allocate memory!" << endl;
        return -1;
    }
    int* deviceNumKeys = NULL;
    checkCudaCall(cudaMalloc((void **) &deviceNumKeys, sizeof(int)));
    if (deviceNumKeys == NULL) {
        checkCudaCall(cudaFree(deviceDataIn));
	checkCudaCall(cudaFree(deviceDataOut));
        checkCudaCall(cudaFree(deviceKeys));
        cout << "could not allocate memory!" << endl;
        return -1;
    }

    timer kernelTime1 = timer("kernelTime");
    timer memoryTime = timer("memoryTime");

    // copy the original vectors to the GPU
    memoryTime.start();
    checkCudaCall(cudaMemcpy(deviceDataIn, data_in, n*sizeof(char), cudaMemcpyHostToDevice));
    checkCudaCall(cudaMemcpy(deviceKeys, keys, num_keys*sizeof(int), cudaMemcpyHostToDevice));
    checkCudaCall(cudaMemcpy(deviceNumKeys, &num_keys, sizeof(int), cudaMemcpyHostToDevice));
    memoryTime.stop();

    // execute kernel
    kernelTime1.start();
    decryptKernel<<<n/threadBlockSize, threadBlockSize>>>(deviceDataIn, deviceDataOut, deviceKeys, deviceNumKeys);
    for (int i = n/threadBlockSize * threadBlockSize; i < n; i++) {
      data_out[i] = data_in[i] - keys[i % num_keys];
    }
    cudaDeviceSynchronize();
    kernelTime1.stop();

    // check whether the kernel invocation was successful
    checkCudaCall(cudaGetLastError());

    // copy result back
    memoryTime.start();
    checkCudaCall(cudaMemcpy(data_out, deviceDataOut, n/threadBlockSize * threadBlockSize * sizeof(char), cudaMemcpyDeviceToHost));
    memoryTime.stop();

    checkCudaCall(cudaFree(deviceDataIn));
    checkCudaCall(cudaFree(deviceDataOut));

    cout << fixed << setprecision(6);
    cout << "Decrypt (kernel): \t\t" << kernelTime1.getElapsed() << " seconds." << endl;
    cout << "Decrypt (memory): \t\t" << memoryTime.getElapsed() << " seconds." << endl;

   return 0;
}

int main(int argc, char* argv[]) {
    int n, num_keys;
    int *keys;

    num_keys = argc - 1;
    keys = new int[num_keys];

    for (int i = 1; i < argc; i++) {
      keys[i] = atoi(argv[i]);
    }

    if (argc < 2) {
      cout << "Usage: ./" << argv[0] << " key1 key2 .." << endl;
      exit(0);
    }

    n = fileSize();
    if (n == -1) {
      cout << "File not found! Exiting ... " << endl; 
      exit(0);
    }

    char* data_in = new char[n];
    char* data_out = new char[n];    
    readData("original.data", data_in); 

    cout << "Encrypting a file of " << n << " characters." << endl;

    EncryptSeq(n, data_in, data_out, keys, num_keys);
    writeData(n, "sequential.data", data_out);
    EncryptCuda(n, data_in, data_out, keys, num_keys);
    writeData(n, "cuda.data", data_out);  

    readData("cuda.data", data_in);

    cout << "Decrypting a file of " << n << "characters" << endl;
    DecryptSeq(n, data_in, data_out, keys, num_keys);
    writeData(n, "sequential_decrypted.data", data_out);
    DecryptCuda(n, data_in, data_out, keys, num_keys);
    writeData(n, "recovered.data", data_out); 
 
    delete[] data_in;
    delete[] data_out;
    
    return 0;
}

