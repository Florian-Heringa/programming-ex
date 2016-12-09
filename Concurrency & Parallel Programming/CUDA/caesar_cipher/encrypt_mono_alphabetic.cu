/* Niek Kabel (11031174) and Florian Heringa. */
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


__global__ void encryptKernel(char* deviceDataIn, char* deviceDataOut, int key) {
    unsigned index = blockIdx.x * blockDim.x + threadIdx.x;
    deviceDataOut[index] = deviceDataIn[index] + key;
}

__global__ void decryptKernel(char* deviceDataIn, char* deviceDataOut, int key) {
    unsigned index = blockIdx.x * blockDim.x + threadIdx.x;
    deviceDataOut[index] = deviceDataIn[index] - key;
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

int EncryptSeq (int n, char* data_in, char* data_out, int key) 
{  
  int i;
  timer sequentialTime = timer("Sequential encryption");
  
  sequentialTime.start();
  for (i=0; i<n; i++) { data_out[i]=data_in[i] + key; }
  sequentialTime.stop();

  cout << fixed << setprecision(6);
  cout << "Encryption (sequential): \t\t" << sequentialTime.getElapsed() << " seconds." << endl;
  
  return 0; 
}

int DecryptSeq (int n, char* data_in, char* data_out, int key)
{
  int i;
  timer sequentialTime = timer("Sequential decryption");

  sequentialTime.start();
  for (i=0; i<n; i++) { data_out[i]=data_in[i] - key; }
  sequentialTime.stop();

  cout << fixed << setprecision(6);
  cout << "Decryption (sequential): \t\t" << sequentialTime.getElapsed() << " seconds." << endl;

  return 0;
}


int EncryptCuda (int n, char* data_in, char* data_out, int key) {
    int threadBlockSize = 512;

    // Allocate blocks for the plaintext and ciphertext on the GPU.
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

    timer kernelTime1 = timer("kernelTime");
    timer memoryTime = timer("memoryTime");

    // Copy the plaintext to the GPU.
    memoryTime.start();
    checkCudaCall(cudaMemcpy(deviceDataIn, data_in, n*sizeof(char), cudaMemcpyHostToDevice));
    memoryTime.stop();

    // Each kernel will shift a character of the ciphertext right by key positions.
    // The remainder of characters not shifted right by the GPU are shifted right
    // by the CPU, which is always less than one threadblock.
    kernelTime1.start();
    encryptKernel<<<n/threadBlockSize, threadBlockSize>>>(deviceDataIn, deviceDataOut, key);
    for (int i = n/threadBlockSize * threadBlockSize; i < n; i++) {
      data_out[i] = data_in[i] + key;
    }
    cudaDeviceSynchronize();
    kernelTime1.stop();

    // Check whether the kernel invocation was successful.
    checkCudaCall(cudaGetLastError());

    // Copy the ciphertext back.
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

int DecryptCuda (int n, char* data_in, char* data_out, int key) {
    int threadBlockSize = 512;

    // Allocate blocks for the ciphertext and plaintext on the GPU.
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

    timer kernelTime1 = timer("kernelTime");
    timer memoryTime = timer("memoryTime");

    // Copy the ciphertext to the GPU.
    memoryTime.start();
    checkCudaCall(cudaMemcpy(deviceDataIn, data_in, n*sizeof(char), cudaMemcpyHostToDevice));
    memoryTime.stop();

    // Each kernel will shift a character of the ciphertext left by key positions.
    // The remainder of characters not shifted left by the GPU are shifted left
    // by the CPU, which is always less than one threadblock.
    kernelTime1.start();
    decryptKernel<<<n/threadBlockSize, threadBlockSize>>>(deviceDataIn, deviceDataOut, key);
    for (int i = n/threadBlockSize * threadBlockSize; i < n; i++) {
      data_out[i] = data_in[i] - key;
    }
    cudaDeviceSynchronize();
    kernelTime1.stop();

    // Check whether the kernel invocation was successful.
    checkCudaCall(cudaGetLastError());

    // Copy the plaintext back.
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
    int n, key;

    if (argc < 2) {
      cout << "Usage: " << argv[0] << " key" << endl;
      exit(0);
    }
    key = atoi(argv[1]);

    n = fileSize();
    if (n == -1) {
      cout << "File not found! Exiting ... " << endl;
      exit(0);
    }

    char* data_in = new char[n];
    char* data_out = new char[n];
    readData("original.data", data_in);

    // The art of secret writing consists of stenography (hiding the message)
    // and cryptography (hiding the meaning of a message by transpositioning
    // and/or substituting characters (enciphering) or words (encrypting),
    // described by the cryptographic algorithm and (a)symmetric key, both of
    // which must be known for decrypting unless we enter the realm of code breaking).
    // We will implement a monoalphabetic substitution cipher called Caesar's
    // cipher but not constrained to only a-z to prevent word counting and such.

    /* Encipherment. */
    cout << "Encrypting a file of " << n << " characters." << endl;

    EncryptSeq(n, data_in, data_out, key);
    writeData(n, "sequential.data", data_out);
    EncryptCuda(n, data_in, data_out, key);
    writeData(n, "cuda.data", data_out);  

    readData("cuda.data", data_in);

    /* Decipherment. */
    cout << "Decrypting a file of " << n << "characters" << endl;
    DecryptSeq(n, data_in, data_out, key);
    writeData(n, "sequential_decrypted.data", data_out);
    DecryptCuda(n, data_in, data_out, key); 
    writeData(n, "recovered.data", data_out); 
 
    delete[] data_in;
    delete[] data_out;
    
    return 0;
}
