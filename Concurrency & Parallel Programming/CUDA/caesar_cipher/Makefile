debug		:= 0
include		./Makefile.inc


all: clean encrypt_mono_alphabetic encrypt_mono_alphabetic_onGPU encrypt_poly_alphabetic

encrypt_mono_alphabetic: encrypt_mono_alphabetic.cu
	$(NVCC) -c encrypt_mono_alphabetic.cu $(NVCCFLAGS) $(INCLUDES)
	$(LINKER) -o $(PROJ_BASE)/encrypt_mono_alphabetic encrypt_mono_alphabetic.o timer.cc $(INCLUDES) $(CUDA_LIBS) $(CFLAGS) $(CUDA_LDFLAGS)

encrypt_mono_alphabetic_onGPU: encrypt_mono_alphabetic_onGPU.cu
	$(NVCC) -c encrypt_mono_alphabetic_onGPU.cu $(NVCCFLAGS) $(INCLUDES)
	$(LINKER) -o $(PROJ_BASE)/encrypt_mono_alphabetic_onGPU encrypt_mono_alphabetic_onGPU.o timer.cc $(INCLUDES) $(CUDA_LIBS) $(CFLAGS) $(CUDA_LDFLAGS)

encrypt_poly_alphabetic: encrypt_poly_alphabetic.cu
	$(NVCC) -c encrypt_poly_alphabetic.cu $(NVCCFLAGS) $(INCLUDES)
	$(LINKER) -o $(PROJ_BASE)/encrypt_poly_alphabetic encrypt_poly_alphabetic.o timer.cc $(INCLUDES) $(CUDA_LIBS) $(CFLAGS) $(CUDA_LDFLAGS)

clean:
	rm -f ./encrypt_mono_alphabetic.o
	rm -f ./encrypt_mono_alphabetic_onGPU.o
	rm -f ./encrypt_poly_alphabetic.o
	rm -f $(PROJ_BASE)/encrypt_mono_alphabetic.o
	rm -f $(PROJ_BASE)/encrypt_mono_alphabetic_onGPU.o
	rm -f $(PROJ_BASE)/encrypt_poly_alphabetic.o
