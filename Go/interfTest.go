package main

import "fmt"

type I interface {
	M()
	R()
}

type T struct {
	S string
	Z int
}

func (t *T) M() {
	if t == nil {
		fmt.Println("<nil>")
		return
	}
	fmt.Println(t.S, "M() method")
}

func (t *T) R() {
	fmt.Println(t.S, "R() method")
}

func main() {
	var i I

	var t *T
	i = t
	describe(i)
	i.M()

	i = &T{"hello", 0}
	describe(i)
	i.M()
	i.R()
}

func describe(i I) {
	fmt.Printf("(%v, %T)\n", i, i)
}
