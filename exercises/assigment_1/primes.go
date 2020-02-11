package main

import (
	"fmt"
	"sort"
)

func main() {
	startPrimes := 1
	endPrimes := 20000
	primeArray := []int{}

	jobs := make(chan int, endPrimes-startPrimes)
	results := make(chan int, endPrimes-startPrimes)

	go worker(1, jobs, results)
	go worker(2, jobs, results)
	go worker(3, jobs, results)
	go worker(4, jobs, results)
	go worker(5, jobs, results)
	go worker(6, jobs, results)
	go worker(7, jobs, results)
	go worker(8, jobs, results)

	for i := startPrimes; i < endPrimes; i++ {
		jobs <- i
	}
	close(jobs)

	for j := startPrimes; j < endPrimes; j++ {
		res := <-results
		if res > 0 {
			primeArray = append(primeArray, res)
		}
	}

	sort.Ints(primeArray)
	fmt.Println(primeArray)
}

func worker(i int, jobs <-chan int, results chan<- int) {
	fmt.Println("worker ", i, " start")

	for n := range jobs {

		if checkIfPrime(n) {
			results <- n
			fmt.Println("worker ", i, " found ", n)
		} else {
			results <- 0
		}

	}
	fmt.Println("worker ", i, " end")

}

func checkIfPrime(n int) bool {
	if n == 0 {
		return false
	}

	if n <= 3 {
		return n > 1
	} else if n%2 == 0 || n%3 == 0 {
		return false
	}

	i := 5

	for i*i <= n {
		if n%i == 0 || n%(i+2) == 0 {
			return false
		}
		i = i + 6
	}

	return true
}
