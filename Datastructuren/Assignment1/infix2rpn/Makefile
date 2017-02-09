#CC = clang
CC = gcc

CFLAGS = -std=c11 -g3 -fsanitize=address -Wall -Wextra
LDFLAGS = -fsanitize=address

PROG = infix2rpn

all: $(PROG)

stack.o: stack.c stack.h

infix2rpn: infix2rpn.o stack.o
	$(CC) -o $@ $^ $(LDFLAGS)

clean:
	rm -f *~ *.o $(PROG)

tarball: infix2rpn.tar.gz

infix2rpn.tar.gz: infix2rpn.c stack.c stack.h AUTHORS Makefile
	tar -czf $@ $^

check: infix2rpn.tar.gz
	curl --insecure -F "file=@infix2rpn.tar.gz" http://51.15.56.180/upload1/
