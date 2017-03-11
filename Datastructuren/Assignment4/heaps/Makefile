#CC = clang

CFLAGS = -fsanitize=address -std=c11 -g3 -Wall -Wextra
LD_FLAGS= -fsanitize=address

PROG = queue

all: $(PROG)

heap.o: heap.c prioq.h array.h

queue: heap.o main.o array.o
	$(CC) -o $@  $^ $(LD_FLAGS)

clean:
	rm -f *.o $(PROG)

tarball: prioq.tar.gz

prioq.tar.gz: main.c heap.c array.h array.c prioq.h AUTHORS Makefile
	tar -czf $@ $^

check: prioq.tar.gz
	curl --insecure -F "file=@prioq.tar.gz" http://51.15.56.180/upload4/
