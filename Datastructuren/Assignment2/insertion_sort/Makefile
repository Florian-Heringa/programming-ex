#CC = clang
CC = gcc

CFLAGS = -std=c11 -g3 -fsanitize=address -Wall -Wextra
LDFLAGS = -fsanitize=address

PROG = mysort

all: $(PROG)

list.o: list.c list.h

mysort: main.o list.o
	$(CC) -o $@ $^ $(LDFLAGS)

clean:
	rm -f *.o $(PROG)

tarball: insertion_sort_submit.tar.gz

insertion_sort_submit.tar.gz: main.c list.c list.h AUTHORS Makefile
	tar -czf $@ $^

check: insertion_sort_submit.tar.gz
	curl --insecure -F "file=@insertion_sort_submit.tar.gz" http://51.15.56.180/upload2/
