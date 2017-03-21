all: encode decode print-test

clean:
	rm -f encode decode print-test
	rm -f *.o *~

CFLAGS = -D_POSIX_C_SOURCE=200809L -std=c11 -g3 -Wall -Wextra
LDFLAGS = -g3

SOURCES = \
	read_data.c load_tree.c compute_frequencies.c compute_tree.c \
	print_tree.c compute_code_table.c print_code_table.c encode_data.c \
	count_tree.c make_tree.c fixed_tree.c print_code.c stack.c

HEADERS = huffman.h stack.h

%.o: %.c $(HEADERS)
	$(CC) $(CFLAGS) -c $<

encode: encode.o $(SOURCES:.c=.o)
	$(CC) $(LDFLAGS) -o $@ $^

decode: decode.o $(SOURCES:.c=.o)
	$(CC) $(LDFLAGS) -o $@ $^

print-test: print_test.o $(SOURCES:.c=.o)
	$(CC) $(LDFLAGS) -o $@ $^

