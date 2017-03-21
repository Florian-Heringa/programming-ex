#include <check.h>
#include <stdlib.h>
#include <time.h>

#include "bitvec.h"

// For older versions of the check library
#ifndef ck_assert_ptr_nonnull
#define ck_assert_ptr_nonnull(X) _ck_assert_ptr(X, !=, NULL)
#endif
#ifndef ck_assert_ptr_null
#define ck_assert_ptr_null(X) _ck_assert_ptr(X, ==, NULL)
#endif

#define RESET_VAL(val) (val = 2)

/*
 * This variable is used as the size of the vectors that is being tested with. *
 * It should be at-least 1 and it should be less than `MAX_SIZE - 3 *
 * sizeof(long long)` as we need to test out of bounds. It should also be a
 * power of two as we use it as mod on a random number (this is to prevent
 * bias).
 */
#define ARRAY_SIZE (128)

#define RANDOM_PASSES (25)

/* Start of essential tests */
START_TEST(test_init)
{
    struct vec *vec = bitvec_alloc(10);
    ck_assert_ptr_nonnull(vec);
    bitvec_free(vec);
}
END_TEST

START_TEST(test_simple_switch)
{
    bit val;
    struct vec *vec = bitvec_alloc(ARRAY_SIZE);
    ck_assert_ptr_nonnull(vec);

    for (size_t i = 0; i < ARRAY_SIZE; i++) {
        bitvec_set(vec, i, 1);
    }

    for (size_t i = 0; i < ARRAY_SIZE; i++) {
        RESET_VAL(val);
        ck_assert_int_eq(bitvec_get(vec, i, &val), ARRAY_OK);
        ck_assert_uint_eq(val, 1);
    }

    for (size_t i = 0; i < ARRAY_SIZE; i++) {
        bitvec_set(vec, i, 0);
    }

    for (size_t i = 0; i < ARRAY_SIZE; i++) {
        RESET_VAL(val);
        ck_assert_int_eq(bitvec_get(vec, i, &val), ARRAY_OK);
        ck_assert_uint_eq(val, 0);
    }

    bitvec_free(vec);
}
END_TEST

START_TEST(test_init_value)
{
    bit val;
    struct vec *vec = bitvec_alloc(ARRAY_SIZE);
    ck_assert_ptr_nonnull(vec);

    for (size_t i = 0; i < ARRAY_SIZE; i++) {
        RESET_VAL(val);
        ck_assert_int_eq(bitvec_get(vec, i, &val), ARRAY_OK);
        ck_assert_uint_eq(val, 0);
    }

    bitvec_free(vec);
}
END_TEST

START_TEST(test_random_walk)
{
    char reference[ARRAY_SIZE];
    struct vec *vec;
    bit val;

    srand(time(NULL));

    for (size_t i = 0; i < RANDOM_PASSES; i++) {
        vec = bitvec_alloc(ARRAY_SIZE);
        ck_assert_ptr_nonnull(vec);

        for (size_t j = 0; j < ARRAY_SIZE; j++) {
            reference[j] = 0;
        }

        for (size_t j = 0; j < ARRAY_SIZE; j++) {
            size_t idx = rand() % ARRAY_SIZE;
            val = rand() % 2;
            reference[idx] = val;
            ck_assert_int_eq(bitvec_set(vec, idx, val), ARRAY_OK);
        }

        for (size_t j = 0; j < ARRAY_SIZE; j++) {
            RESET_VAL(val);
            ck_assert_int_eq(bitvec_get(vec, j, &val), ARRAY_OK);
            ck_assert_int_eq(reference[j], val);
        }

        bitvec_free(vec);
    }
}
END_TEST
/* End of essential tests */

/* Start of error capturing tests */
START_TEST(test_get_bit_out_of_bounds)
{
    struct vec *vec = bitvec_alloc(ARRAY_SIZE);
    ck_assert_ptr_nonnull(vec);

    ck_assert_int_eq(bitvec_get(vec, ARRAY_SIZE + 1, NULL),
                     ARRAY_OUT_OF_BOUNDS);

    bitvec_free(vec);
}
END_TEST

START_TEST(test_set_bit_out_of_bounds)
{
    struct vec *vec = bitvec_alloc(ARRAY_SIZE);
    ck_assert_ptr_nonnull(vec);

    ck_assert_int_eq(bitvec_set(vec, ARRAY_SIZE + 1, 1), ARRAY_OUT_OF_BOUNDS);
    ck_assert_int_eq(bitvec_set(vec, ARRAY_SIZE + 1, 0), ARRAY_OUT_OF_BOUNDS);

    bitvec_free(vec);
}
END_TEST

START_TEST(test_get_way_out_of_bounds)
{
    struct vec *vec = bitvec_alloc(ARRAY_SIZE);
    ck_assert_ptr_nonnull(vec);

    ck_assert_int_eq(bitvec_get(vec, ARRAY_SIZE + 2 * sizeof(long long), NULL),
                     ARRAY_OUT_OF_BOUNDS);

    bitvec_free(vec);
}
END_TEST

START_TEST(test_set_way_out_of_bounds)
{
    struct vec *vec = bitvec_alloc(ARRAY_SIZE);
    ck_assert_ptr_nonnull(vec);

    ck_assert_int_eq(bitvec_set(vec, ARRAY_SIZE + 2 * sizeof(long long), 1),
                     ARRAY_OUT_OF_BOUNDS);
    ck_assert_int_eq(bitvec_set(vec, ARRAY_SIZE + 2 * sizeof(long long), 0),
                     ARRAY_OUT_OF_BOUNDS);

    bitvec_free(vec);
}
END_TEST

START_TEST(test_set_illegal_value)
{
    bit val;
    struct vec *vec = bitvec_alloc(ARRAY_SIZE);
    ck_assert_ptr_nonnull(vec);

    ck_assert_int_eq(bitvec_set(vec, 0, 2), ARRAY_ILLEGAL_VARIABLE);

    RESET_VAL(val);
    ck_assert_int_eq(bitvec_get(vec, 0, &val), ARRAY_OK);
    ck_assert_int_eq(val, 0);

    bitvec_free(vec);
}
END_TEST

Suite *heap_suite(void)
{
    Suite *s;
    TCase *tc_core;
    TCase *tc_error;

    s = suite_create("bitvec");
    /* Core test case */
    tc_core = tcase_create("Core");
    tc_error = tcase_create("Error checking");

    /* Regular tests. */
    tcase_add_test(tc_core, test_init);
    tcase_add_test(tc_core, test_simple_switch);
    tcase_add_test(tc_core, test_random_walk);
    tcase_add_test(tc_core, test_init_value);

    /* Error tests. */
    tcase_add_test(tc_error, test_get_bit_out_of_bounds);
    tcase_add_test(tc_error, test_set_bit_out_of_bounds);
    tcase_add_test(tc_error, test_get_way_out_of_bounds);
    tcase_add_test(tc_error, test_set_way_out_of_bounds);
    tcase_add_test(tc_error, test_set_illegal_value);

    suite_add_tcase(s, tc_core);
    suite_add_tcase(s, tc_error);
    return s;
}

int main(void)
{
    int number_failed;
    Suite *s;
    SRunner *sr;

    s = heap_suite();
    sr = srunner_create(s);

    srunner_run_all(sr, CK_NORMAL);
    // srunner_run_all(sr, CK_VERBOSE);
    number_failed = srunner_ntests_failed(sr);
    srunner_free(sr);
    return (number_failed == 0) ? EXIT_SUCCESS : EXIT_FAILURE;
}
