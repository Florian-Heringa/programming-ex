package main

import (
	"fmt"
	"math"
)

const MaxUint = ^uint(0)
const MaxInt = int(MaxUint >> 1) 

func Sqrt(x float64) (z float64) {

	z = float64(1)
	var zn, dz float64 = 0, float64(MaxInt)
	
	fmt.Println(z, zn, dz)

	for dz > 0.0001 {
		zn = z - (z*z - x) / (2 * z)
		dz = math.Abs(z - zn)
		z = zn
	}
	
	return
}

func main() {
	fmt.Println(Sqrt(2))
	fmt.Println(math.Sqrt(2))
}