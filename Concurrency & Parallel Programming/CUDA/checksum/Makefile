debug		:= 0
include		./Makefile.inc


all: clean checksum

checksum: checksum.cu
	$(NVCC) -c checksum.cu $(NVCCFLAGS) $(INCLUDES)
	$(LINKER) -o $(PROJ_BASE)/checksum checksum.o timer.cc $(INCLUDES) $(CUDA_LIBS) $(CFLAGS) $(CUDA_LDFLAGS)


clean:
	rm -f ./checksum.o
	rm -f $(PROJ_BASE)/checksum
