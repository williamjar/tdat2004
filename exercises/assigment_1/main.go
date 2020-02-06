package main

import (
	"fmt"
	"math"
)

func main() {
	numberOfPrimes := 100000

	jobs := make(chan int, numberOfPrimes)
	results := make(chan int, numberOfPrimes)

	go worker(jobs, results)

	for i := 0; i < numberOfPrimes; i++ {
		jobs <- i
	}

	close(jobs)

	for j := 0; j < numberOfPrimes; j++ {
		fmt.Println(<-results)
	}
}

func worker(jobs <-chan int, results chan<- int) {
	for n := range jobs {
		results <- findPrimes(n)
	}
}

func findPrimes(n int) int {
	for i := 2; i <= int(math.Floor(float64(n)/2)); i++ {
		if n%i == 0 {
			return 0
		}
	}
	return n

}
