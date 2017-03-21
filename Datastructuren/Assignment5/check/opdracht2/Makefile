CC = clang
#CC = gcc

CFLAGS = -std=c99 -Wall -Wextra -Wno-unused-function
LDFLAGS = -lcheck_pic -pthread -lrt -lm -lsubunit

TESTS = list-test
PROGS = list-sort

all: $(PROGS) $(TESTS)

list.o: list.c list.h
dlist.o: dlist.c list.h

list-sort: main.o list.o
	$(CC) -o $@ $^ $(LDFLAGS)

list-test: list-test.o list.o
	$(CC) -o $@ $^ $(LDFLAGS)

check: all
	@for t in $(TESTS); do echo $$t; ./$$t; echo ; done
	./sort-test.sh

clean:
	rm -f *.o $(PROGS) $(TESTS)

