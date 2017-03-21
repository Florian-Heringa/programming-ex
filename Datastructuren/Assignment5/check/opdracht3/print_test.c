#include <stdio.h>
#include <stdlib.h>

#include "huffman.h"

int main(void) { 
    tree_t a = make_tree('a', 0, 0, 0);
    tree_t b = make_tree('b', 0, 0, 0);
    tree_t i = make_tree(0, 0, a, b);
    tree_t c = make_tree('c', 0, 0, 0);
    tree_t t1 = make_tree(0, 0, i, c);
    print_tree(t1);
    printf("\n");

    tree_t d = make_tree('d', 0, 0, 0);
    tree_t e = make_tree('e', 0, 0, 0);
    tree_t j = make_tree(0, 0, d, e);
    tree_t f = make_tree('f', 0, 0, 0);
    tree_t g = make_tree('g', 0, 0, 0);
    tree_t h = make_tree(0, 0, f, g);
    tree_t t2 = make_tree(0, 0, j, h);
    print_tree(t2);
    printf("\n");

    tree_t k = make_tree('k', 0, 0, 0);
    tree_t l = make_tree('l', 0, 0, 0);
    tree_t m = make_tree(0, 0, l, k);
    tree_t n = make_tree('n', 0, 0, 0);
    tree_t o = make_tree(0, 0, n, m);
    tree_t p = make_tree('p', 0, 0, 0);
    tree_t q = make_tree(0, 0, p, o);
    tree_t r = make_tree('r', 0, 0, 0);
    tree_t t3 = make_tree(0, 0, r, q);
    print_tree(t3);
    printf("\n");
    
    tree_t s = make_tree('s', 0, 0, 0);
    tree_t t = make_tree('t', 0, 0, 0);
    tree_t u = make_tree('u', 0, 0, 0);
    tree_t v = make_tree('v', 0, 0, 0);
    tree_t w = make_tree('w', 0, 0, 0);
    tree_t x = make_tree(0, 0, u, v);
    tree_t y = make_tree(0, 0, s, t);
    tree_t z = make_tree(0, 0, x, w);
    tree_t t4 = make_tree(0, 0, y, z);
    print_tree(t4);
    printf("\n");

    free(a);
    free(b);
    free(c);
    free(d);
    free(e);
    free(f);
    free(g);
    free(h);
    free(i);
    free(j);
    free(k);
    free(l);
    free(m);
    free(n);
    free(o);
    free(p);
    free(q);
    free(r);
    free(s);
    free(t);
    free(u);
    free(v);
    free(w);
    free(x);
    free(y);
    free(z);
    free(t1);
    free(t2);
    free(t3);
    free(t4);
} 
