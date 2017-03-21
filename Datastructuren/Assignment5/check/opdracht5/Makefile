CC = clang

CFLAGS = -fsanitize=address -std=c11 -g3 -Wall -Wextra
CFLAGS_ERROR = $(CFLAGS) -Werror
LD_FLAGS= -fsanitize=address -lm
TEST_LDFLAGS = $(LD_FLAGS) `pkg-config --libs check`

TEST_BITVEC = bitvec-test

PROG = dups

all: $(TEST_BITVEC) $(PROG)

all_error: CFLAGS += -W -Werror
all_error: clean all

bitvec.o: bitvec.c

dups: bitvec.o main.o hash.o bloom.o
	$(CC) -o $@  $^ $(LD_FLAGS)

bitvec-test: bitvec-test.o bitvec.o
	$(CC) -o $@  $^ $(TEST_LDFLAGS)

clean:
	rm -f *.o $(PROG) $(TEST_BITVEC)

local-check: all $(TEST_BITVEC)
	@echo "Testing heap"
	./$(TEST_BITVEC)
