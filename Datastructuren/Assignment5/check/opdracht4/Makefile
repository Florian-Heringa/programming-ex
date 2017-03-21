#CC = clang

CFLAGS = -std=c11 -g3 -Wall -Wextra
LD_FLAGS= 
TEST_LDFLAGS = $(LD_FLAGS) `pkg-config --libs check`

PROG = queue
TEST_HEAP = heap-test

all: $(PROG) $(TEST_HEAP)

heap.o: heap.c prioq.h array.h

queue: heap.o main.o array.o
	$(CC) -o $@  $^ $(LD_FLAGS)

heap-test: heap-test.o heap.o array.o
	$(CC) -o $@  $^ $(TEST_LDFLAGS)

clean:
	rm -f *.o $(PROG) $(TEST_HEAP)

tarball: prioq_submit.tar.gz

prioq.tar.gz: main.c heap.c array.h array.c prioq.h AUTHORS Makefile
	tar -czf $@ $^

check: prioq.tar.gz
	curl --insecure -F "file=@prioq.tar.gz" http://51.15.56.180/upload4/

local-check: all $(TEST_HEAP)
	@echo "Testing heap"
	./$(TEST_HEAP)
