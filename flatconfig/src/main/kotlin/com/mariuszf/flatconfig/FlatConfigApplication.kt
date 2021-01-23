package com.mariuszf.flatconfig

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlatConfigApplication

fun main(args: Array<String>) {
	runApplication<FlatConfigApplication>(*args)
}
