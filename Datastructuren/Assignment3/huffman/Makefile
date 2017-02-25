all: encode decode

clean:
	rm -f encode decode
	rm -f *.o *~

CFLAGS = -fsanitize=address -D_POSIX_C_SOURCE=200809L -std=c11 -g3 -Wall -Wextra
LDFLAGS = -fsanitize=address -g3

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

tarball: huffman_submit.tar.gz

huffman_submit.tar.gz: $(SOURCES) $(HEADERS) AUTHORS Makefile
	tar -czf $@ $^

check: huffman_submit.tar.gz
	curl --insecure -F "file=@huffman_submit.tar.gz" http://51.15.56.180/upload3/
