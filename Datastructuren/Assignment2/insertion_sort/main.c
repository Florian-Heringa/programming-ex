#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <getopt.h>
#include <ctype.h>

#include "list.h"

struct config {
    // You can ignore these options until/unless you implement the
    // bonus features.

    // Set to 1 if -u is specified, 0 otherwise.
    int remove_duplicates;

    // Set to 1 if -S is specified, 0 otherwise.
    int add_sum;

    // Set to N if -s N is specified, 0 otherwise.
    int select_multiple;

    // Set to N if -x N is specified, 0 otherwise.
    int remove_multiple;

    // Set to N if -h N is specified, 0 otherwise.
    int show_first;

    // Set to N if -t N is specified, 0 otherwise.
    int show_last;

    // Set to 1 if -3 is specified, 0 otherwise.
    int scribble;
};

static
int parse_options(struct config *cfg, int argc, char *argv[]);

#define BUF_SIZE 1024
static char buf[BUF_SIZE];

int insertion_sort(struct list *l, struct node *n) {
  return 0;
}

int main(int argc, char *argv[]) {

    struct config cfg;
    if (parse_options(&cfg, argc, argv) != 0)
        return 1;

    // Initialies linked list datastructure
    struct list *sorted_list = list_init();
    if (sorted_list == NULL) {
      return 1;
    }

    while (fgets(buf, BUF_SIZE, stdin)) {

      // Skip over non-digit input lines
      if (!isdigit(*buf)) {
        continue;
      }

      //Make new node for the number
      struct node *current_node = node_init(NULL, NULL, (int)*buf);



      //Exit condition for debug
      if (*buf == 'q') {
        break;
      }
    }

    // ... SOME CODE MISSING HERE ...
    printf("test\n");

    list_cleanup(sorted_list);

    return 0;
}

int parse_options(struct config *cfg, int argc, char *argv[]) {
    memset(cfg, 0, sizeof(struct config));
    int c;
    while ((c = getopt (argc, argv, "uSs:x:h:t:3")) != -1)
        switch (c) {
        case 'u': cfg->remove_duplicates = 1; break;
        case 'S': cfg->add_sum = 1; break;
        case '3': cfg->scribble = 1; break;
        case 's': cfg->select_multiple = atoi(optarg); break;
        case 'x': cfg->remove_multiple = atoi(optarg); break;
        case 'h': cfg->show_first = atoi(optarg); break;
        case 't': cfg->show_last = atoi(optarg); break;
        default:
            fprintf(stderr, "invalid option: -%c\n", optopt);
            return 1;
        }
    if (cfg->show_first != 0 && cfg->show_last != 0) {
        fprintf(stderr, "cannot specify both -h and -t\n");
        return 1;
    }
    return 0;
}
