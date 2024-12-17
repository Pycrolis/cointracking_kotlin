package net.tubylaris.cointracking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import picocli.CommandLine
import kotlin.system.exitProcess

@SpringBootApplication
class CointrackingApplication

fun main(args: Array<String>) {
	runApplication<CointrackingApplication>(*args)

	val exitCode = CommandLine(MainCommand()).execute(*args)
	exitProcess(exitCode)
}
