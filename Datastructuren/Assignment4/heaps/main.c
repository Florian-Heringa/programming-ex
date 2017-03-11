#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <getopt.h>

#include "prioq.h"

#define BUF_SIZE 1024

static char buf[BUF_SIZE];

struct patient {
    char *name;
    int age;
    int duration;
};

struct config {
    // Set to 1 if -y is specified, 0 otherwise.
    int year;
};

void print_patient2(struct patient *p) {
    printf("name: %s, age: %d, duration: %d\n", p->name, p->age, p->duration);
}

static
int parse_options(struct config *cfg, int argc, char *argv[]);

// Patient related functions
void read_and_add(prioq *queue, int end, int *skips);
struct patient* patient_from_input(char* input);
void print_patient(struct patient *p);
void free_patient(void *p);
void empty_waiting_room(prioq *p);

// Comparison functions
int cmp_by_name(const void *a, const void *b);
int cmp_by_name_rec(char* p1, char* p2, int depth);
int cmp_by_age(const void *a, const void *b);

int main(int argc, char *argv[]) {

    prioq *queue;
    struct config cfg;
    struct patient *p;
    int skips = 0;

    if (parse_options(&cfg, argc, argv) != 0) {
        return EXIT_FAILURE;
    }

    if (cfg.year) {
        queue = prioq_init(cmp_by_age);
    } else {
        queue = prioq_init(cmp_by_name);
    }

    for (int iterations = 0;;) {
        
        read_and_add(queue, 0, &skips);
        // Remove_min on queue if there is a patient
        p = prioq_pop(queue);

        if (p && p->duration) {
            skips += p->duration - 1;
            for (int i = 0; i < p->duration - 1; i++) {
                if (iterations == 9) { break; }
                printf(".\n");
                iterations++;
            }
        }

        if (p) { print_patient(p); }
        printf(".\n"); // End turn.

        // if 10 hours passed, day is over
        if (++iterations == 10) {
            empty_waiting_room(queue);
            break;
        }
    }

    prioq_cleanup(queue, free_patient);

    return EXIT_SUCCESS;
}

int parse_options(struct config *cfg, int argc, char *argv[]) {
    memset(cfg, 0, sizeof(struct config));
    int c;
    while ((c = getopt (argc, argv, "y")) != -1) {
        switch (c) {
            case 'y': cfg->year = 1; break;
            default:
                fprintf(stderr, "invalid option: -%c\n", optopt);
                return 1;
        }
    }
    return 0;
}


// Simple comparison function of patients by age
int cmp_by_age(const void *a, const void *b) {

    int p1_age = ((struct patient*)a)->age;
    int p2_age = ((struct patient*)b)->age;

    return p1_age > p2_age ? 1 : (p1_age < p2_age ? -1: 0);
}

// Uses recursion to sort by entire name, and not by only first letter
int cmp_by_name_rec(char* p1, char* p2, int depth) {

    if (p1[depth] == '\0') {
        return 1;
    } else if  (p2[depth] == '\0') {
        return -1;
    } 

    // Compare name by character
    char lead1 = p1[depth];
    char lead2 = p2[depth];

    /* If characters are not the same, return first in alphabet,
     * else return recursive step
     */
    return lead1 > lead2 ? 1 : (lead1 < lead2 ? -1: cmp_by_name_rec(p1, p2, depth + 1));
}

// Setup for recursive step
int cmp_by_name(const void *a, const void *b) {

    char *p1 = ((struct patient*)a)->name;
    char *p2 = ((struct patient*)b)->name;

    return cmp_by_name_rec(p1, p2, 0);
}

/* Reads input from buffer and adds it to the queue if it is a valid 
 * patient format. If end == 1, the end of the day has been reached.
 * in this case all remaining patients (until EOF) are read into the
 * queue. This way they can be printed in alphabetical order later.
 */
void read_and_add(prioq *queue, int end, int *skips) {

    while (1) {
        char *s = fgets(buf, BUF_SIZE, stdin);
        if (s == NULL) {
            if (end) { break; }
            fprintf(stderr, "Unexpected end of file. Exiting\n");
            exit(1);
        }

        // Time is passing, no new patients for now
        if (buf[0] == '.') {
            if (end) { continue; }
            else if (*skips) { (*skips)--; continue; }
            else { break; }
        }

        // Put new patient into queue with insert
        struct patient *p = patient_from_input(s);

        if (!p) {
            fprintf(stderr, "Something went wrong\n");
            exit(1);
        }
        prioq_insert(queue, p);
    }
}


struct patient* patient_from_input(char* input) {

    struct patient *p = malloc(sizeof(struct patient));
    char *token;

    if (!p) {
        return NULL;
    }

    // Populate fields of patient struct
    token = strtok(input, " \n");
    if (!token) {
        free(p);
        return NULL;
    }
    // Make sure strcpy has enough space
    p->name = malloc(sizeof(token) + 2 * sizeof(char));
    strcpy(p->name, token);

    token = strtok(NULL, " \n");
    if (!token) {
        free(p->name);
        free(p);
        return NULL;
    }
    p->age = atoi(token);

    token = strtok(NULL, " \n");
    if (token) {
        p->duration = atoi(token);
    } else {
        p->duration = 0;
    }

    return p;
}

void print_patient(struct patient *p) {
    if (!p) {
        return;
    }
    printf("%s\n", p->name);
    free_patient(p);
}

void free_patient(void *p) {

    free(((struct patient *)p)->name);
    free((struct patient *)p);
}

void empty_waiting_room(prioq *queue) {

    read_and_add(queue, 1, 0);
    struct patient *p = prioq_pop(queue);

    // Pop off all remaining patients
    while (p) {
        print_patient(p);
        p = prioq_pop(queue);
    }
}