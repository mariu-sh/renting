package com.mariuszf.flatconfig

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlatconfigApplication

fun main(args: Array<String>) {
	runApplication<FlatconfigApplication>(*args)
}
