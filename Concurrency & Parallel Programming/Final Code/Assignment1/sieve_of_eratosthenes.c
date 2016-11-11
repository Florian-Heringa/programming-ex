/*
 * sieve_of_eratosthenes.c\
 * Florian Heringa
 * 10385835
 * 11-11-2016
 *
 * Parallel implementation of the sieve of Eratosthenes algorithm for finding
 * prime numbers. A bounded queue is used to communicate the prime candidates
 * from thread to thread. Threads are created for each new prime number, and are
 * then used to find out if any of the next numbers are a multimple of the prime
 * corresponding with the thread.
 */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <pthread.h>

// maximum size of the bounded queue
const int MAX_CANDIDATES = 100;

/* global because of availability in terminating threads and waiting on the last thread,
 * not used since the code should run infinitely
 */
pthread_t next_filter_thread_id;

/* Since the buffer between threads can potentially be written and read at the same time
 * (and probably will most of the time in practice) we need a way to prefent race conditions
 * and trying reads on data that is not available, or writes to a full buffer.
 * For this we can use mutex locks and conditional variables signalling whennn the buffer is
 * ready for reads and writes.
 */
struct BoundedQueue {
    int max_candidates;
    int *next_primes;
    int candidates;
    int next_candidate_in;
    int next_candidate_out;
    pthread_mutex_t adding_to_queue;
    pthread_cond_t new_available;
    pthread_cond_t candidate_propagated;
};

// create a new bounded queue, with size defined by the size variable
struct BoundedQueue *bounded_queue_init(int size)
{
    struct BoundedQueue *bounded_queue = (struct BoundedQueue *)malloc(sizeof(struct BoundedQueue));
    if (bounded_queue)
    {
        bounded_queue->max_candidates = size;
        bounded_queue->next_primes = (int *)malloc(size * sizeof(int));
        bounded_queue->candidates = 0;
        bounded_queue->next_candidate_in = 0;
        bounded_queue->next_candidate_out = 0;
        pthread_mutex_init(&(bounded_queue->adding_to_queue), NULL);
        pthread_cond_init(&(bounded_queue->new_available), NULL);
        pthread_cond_init(&(bounded_queue->candidate_propagated), NULL);
    }

    return bounded_queue;
}

/* Removes the bounded queue and frees up memory space,
 * not used in this particular code since an infinite list of primes was needed.
 */
void bounded_queue_destroy(struct BoundedQueue *bounded_queue)
{
    pthread_mutex_destroy(&(bounded_queue->adding_to_queue));
    pthread_cond_destroy(&(bounded_queue->new_available));
    pthread_cond_destroy(&(bounded_queue->candidate_propagated));

    free(bounded_queue->next_primes);
    free(bounded_queue);

    return;
}

// writes the next candidate tot the queue given by the pointer to outpu_queue
void produce_candidate(struct BoundedQueue *output_queue, int next_prime)
{
    // Locks the buffer
    pthread_mutex_lock(&(output_queue->adding_to_queue));

    // Wait until the buffer has free space, signalling with conditional variables
    while (!(output_queue->candidates < output_queue->max_candidates))
    {
        pthread_cond_wait(&(output_queue->candidate_propagated), &(output_queue->adding_to_queue));
    }

    /* output queue should have free space, now write the new candidate to the queue
     * and update the position the next prime candidate should take in the queue 
     */
    assert(output_queue->candidates < MAX_CANDIDATES);
    output_queue->next_primes[output_queue->next_candidate_in] = next_prime;
    output_queue->next_candidate_in = (output_queue->next_candidate_in + 1) % output_queue->max_candidates;
    output_queue->candidates += 1;

    /* Conditional variable to let other thread know that a new candidate is available
     * this can activate a thread waiting for candidates
     */

    pthread_cond_signal(&(output_queue->new_available));
    pthread_mutex_unlock(&(output_queue->adding_to_queue));

    return;
}

// Works in the same way as the producer function, but takes an element from the queue instead of adding it
int consume_candidate(struct BoundedQueue *input_queue)
{
    pthread_mutex_lock(&(input_queue->adding_to_queue));

    while (!(input_queue->candidates > 0))
    {
        pthread_cond_wait(&(input_queue->new_available), &(input_queue->adding_to_queue));
    }

    assert(input_queue->candidates > 0);
    int next_prime = input_queue->next_primes[input_queue->next_candidate_out];
    input_queue->next_candidate_out = (input_queue->next_candidate_out + 1) % input_queue->max_candidates;
    input_queue->candidates -= 1;

    pthread_cond_signal(&(input_queue->candidate_propagated));
    pthread_mutex_unlock(&(input_queue->adding_to_queue));

    return next_prime;
}

/* A pointer to a queue (the first one) takes the prime candidates and
 * places them in the buffer.
 */
void prime_generator(struct BoundedQueue *output_queue)
{
    for (int next_prime = 2;; next_prime++)
    {
        produce_candidate(output_queue, next_prime);
    }
}

/* pre: a pointer to a bounded input queue.
 * post: filter out any multiples of the first prime candidate from
 * all other prime candidates read from the input queue or intuitively,
 * narrow down the set of candidate prime numbers to one still not proven
 * not prime, the first of which by definition must be prime. Repeat.
 */
void *filter_next_primes(void *arg)
{
    struct BoundedQueue *input_queue = (struct BoundedQueue *)arg;
    struct BoundedQueue *output_queue = bounded_queue_init(MAX_CANDIDATES);

    int next_prime;
    int prime_number = 1;
    int is_first_candidate = 1;

    for (;;)
    {
        next_prime = consume_candidate(input_queue);

        if (prime_number == 1)
        {
            prime_number = next_prime;
            printf("%d\n", prime_number);
        }

        if (next_prime % prime_number != 0)
        {
            if (is_first_candidate == 1)
            {
                is_first_candidate = 0;
                pthread_create(&next_filter_thread_id, NULL, &filter_next_primes, output_queue);
            }
            produce_candidate(output_queue, next_prime);
        }
    }

    pthread_exit(NULL);
}

// Create first filter thread and generate primes
int main(int argc, char *argv[])
{
    struct BoundedQueue *output_queue = bounded_queue_init(MAX_CANDIDATES);

    // create first filter thread
    pthread_create(&next_filter_thread_id, NULL, &filter_next_primes, output_queue);

    // prime generator places new primes in the queue
    prime_generator(output_queue);

    return EXIT_SUCCESS;
}

