/* Assignment 1.3: Sieve of Eratosthenes.
 * Name: Niek Kabel
 * E-mail address: niek.kabel@student.uva.nl
 * Student ID: 11031174
 * Description:
 */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <pthread.h>
#include <sys/time.h>
#include <signal.h>

/*
new filter thread: initialize queue, create thread with queue as argument.
write 2 to inf. to the queue
each filter thread will; 
upon receiving its first number, print it to standard output
discard numbers multiple of the first number
else, the first time we ve got a prime number; new filter thread, else we forward to the output queue.
*/

const int MAX_CANDIDATES = 100;
const int MAX_PRIME_NUMBER = 134;
int filter = 1;

/* A bounded queue is defined by a buffer, its size, how much elements
are written to, the next element to write to and read from and in a
parallel programming paradigm we need mechanisms to avoid
race conditions and repeatedly acquiring locks although buffer is empty or full,
respectively mutex- and condition variables. */
struct BoundedQueue {
    int max_candidates;
    int *prime_candidates;
    int candidates;
    int next_candidate_in;
    int next_candidate_out;
    pthread_mutex_t new_candidate;
    pthread_cond_t produced_candidate;
    pthread_cond_t consumed_candidate;
};

struct BoundedQueue *initialize_default_bounded_queue(int size)
{
    struct BoundedQueue *bounded_queue = (struct BoundedQueue *)malloc(sizeof(struct BoundedQueue));
    if (bounded_queue)
    {
        bounded_queue->max_candidates = size;
        bounded_queue->prime_candidates = (int *)malloc(size * sizeof(int));
        bounded_queue->candidates = 0;
        bounded_queue->next_candidate_in = 0;
        bounded_queue->next_candidate_out = 0;
        pthread_mutex_init(&(bounded_queue->new_candidate), NULL);
        pthread_cond_init(&(bounded_queue->produced_candidate), NULL);
        pthread_cond_init(&(bounded_queue->consumed_candidate), NULL);
    }

    return bounded_queue;
}

void destroy_bounded_queue(struct BoundedQueue *bounded_queue)
{
    pthread_mutex_destroy(&(bounded_queue->new_candidate));
    pthread_cond_destroy(&(bounded_queue->produced_candidate));
    pthread_cond_destroy(&(bounded_queue->consumed_candidate));

    free(bounded_queue->prime_candidates);
    free(bounded_queue);

    return;
}

void produce_candidate(struct BoundedQueue *output_queue, int prime_candidate)
{
    pthread_mutex_lock(&(output_queue->new_candidate));
    while (!(output_queue->candidates < MAX_CANDIDATES))
    {
        pthread_cond_wait(&(output_queue->consumed_candidate), &(output_queue->new_candidate));
    }

    assert(output_queue->candidates < MAX_CANDIDATES);

    output_queue->prime_candidates[output_queue->next_candidate_in] = prime_candidate;
    //printf("Produced prime candidate %d\n", prime_candidate);
    output_queue->next_candidate_in = (output_queue->next_candidate_in + 1) % output_queue->max_candidates;
    output_queue->candidates += 1;

    pthread_cond_signal(&(output_queue->produced_candidate));
    pthread_mutex_unlock(&(output_queue->new_candidate));

    return;
}

int consume_candidate(struct BoundedQueue *input_queue)
{
    pthread_mutex_lock(&(input_queue->new_candidate));
    while (!(input_queue->candidates > 0) && filter == 1)
    {
        struct timespec timeToWait;
        struct timeval now;

        gettimeofday(&now, NULL);

        timeToWait.tv_sec = now.tv_sec+1;

        pthread_cond_timedwait(&(input_queue->produced_candidate), &(input_queue->new_candidate), &timeToWait);
    }

    //assert(input_queue->candidates > 0);

    int prime_candidate = input_queue->prime_candidates[input_queue->next_candidate_out];
    //printf("Consumed prime candidate: %d\n", prime_candidate);
    input_queue->next_candidate_out = (input_queue->next_candidate_out + 1) % input_queue->max_candidates;
    input_queue->candidates -= 1;

    pthread_cond_signal(&(input_queue->consumed_candidate));
    pthread_mutex_unlock(&(input_queue->new_candidate));

    return prime_candidate;
}

void generate_prime_candidates(struct BoundedQueue *output_queue)
{
    /* Produce candidate prime numbers from unbounded sequence
    of natural numbers starting with the least prime number, 2.*/
    // produce(output_queue, natural number)

    for (int prime_candidate = 2; prime_candidate <= MAX_PRIME_NUMBER; prime_candidate++)
    {
        produce_candidate(output_queue, prime_candidate);
    }
}

void *filter_prime_candidates(void *arg)
{
    /* Of those candidate prime numbers consumed,
    filter out any multiples of the assigned prime number or intuitively,
    narrow down the set of candidate prime numbers. */
    // candidate = consume(input_queue); if not a multiple: produce(output_queue, candidate);
    struct BoundedQueue *input_queue = (struct BoundedQueue *)arg;
    struct BoundedQueue *output_queue = initialize_default_bounded_queue(MAX_CANDIDATES);

    int prime_candidate;
    int prime_number = 1;
    int is_first_candidate = 1;

    pthread_t next_filter_thread_id;
    void *result;

    while (filter == 1)
    {
        prime_candidate = consume_candidate(input_queue);

        if (prime_number == 1)
        {
            prime_number = prime_candidate;
            printf("%d\n", prime_number);

            if (prime_number == MAX_PRIME_NUMBER)
            {
                filter = 0;
            }
        }

        if (prime_candidate % prime_number != 0)
        {
            if (is_first_candidate == 1)
            {
                is_first_candidate = 0;
                pthread_create(&next_filter_thread_id, NULL, &filter_prime_candidates, output_queue);
            }
            produce_candidate(output_queue, prime_candidate);
        }
    }

    if (is_first_candidate == 0)
    {
        pthread_join(next_filter_thread_id, &result);
    }

    destroy_bounded_queue(output_queue);
    pthread_exit(NULL);
}

void *signal_handler(void *arg)
{
    sigset_t *set = (sigset_t *)arg;
    int sig;

    for (;;)
    {
        sigwait(set, &sig);
        switch (sig)
        {
            case SIGTERM: case SIGINT: case SIGQUIT: case SIGKILL: case SIGHUP:
                filter = 0;
                printf("GOTYA\n");
                break;
        }
        printf("Signal handling thread got signal %d\n", sig);
    }

    pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
    struct BoundedQueue *output_queue = initialize_default_bounded_queue(MAX_CANDIDATES);

    pthread_t first_filter_thread_id, signal_handler_thread_id;
    void *result;

    sigset_t set;
    sigemptyset(&set);
    sigaddset(&set, SIGINT);
    pthread_sigmask(SIG_BLOCK, &set, NULL);
    pthread_create(&signal_handler_thread_id, NULL, &signal_handler, &set);

    pthread_create(&first_filter_thread_id, NULL, &filter_prime_candidates, output_queue);
    generate_prime_candidates(output_queue);

    pthread_join(first_filter_thread_id, &result);
    destroy_bounded_queue(output_queue);

    return EXIT_SUCCESS;
}