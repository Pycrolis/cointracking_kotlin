package net.tubylaris.cointracking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CointrackingApplication

fun main(args: Array<String>) {
	runApplication<CointrackingApplication>(*args)
}
