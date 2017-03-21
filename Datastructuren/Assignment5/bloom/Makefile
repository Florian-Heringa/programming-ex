CC = gcc

CFLAGS = -fsanitize=address -std=c11 -g3 -Wall -Wextra
LD_FLAGS= -fsanitize=address -lm
TEST_LDFLAGS = $(LD_FLAGS) `pkg-config --libs check`

TEST_BITVEC = bitvec-test

PROG = dups

all: $(PROG)

bitvec.o: bitvec.c

dups: bitvec.o main.o hash.o bloom.o
	$(CC) -o $@  $^ $(LD_FLAGS)

bitvec-test: bitvec-test.o bitvec.o
	$(CC) -o $@  $^ $(TEST_LDFLAGS)

clean:
	rm -f *.o $(PROG) $(TEST_BITVEC)

tarball: bloom_submit.tar.gz

bloom_submit.tar.gz: bloom.c bloom.h bitvec.c bitvec.h hash.c hash.h main.c AUTHORS Makefile PARAMS
	tar -czf $@ $^

check: bloom_submit.tar.gz
	curl --insecure -F "file=@bloom_submit.tar.gz" http://51.15.56.180/upload6/
