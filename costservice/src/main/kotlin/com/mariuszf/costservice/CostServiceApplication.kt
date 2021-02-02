package com.mariuszf.costservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CostServiceApplication

fun main(args: Array<String>) {
	runApplication<CostServiceApplication>(*args)
}
