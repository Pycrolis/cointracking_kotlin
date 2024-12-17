package net.tubylaris.cointracking

import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.util.concurrent.Callable

@Command(
    name = "cointracking",
    description = ["Access your Cointracking account data via API. Need the following environment variables: API_KEY and API_SECRET."],
    showDefaultValues = true,
)
class MainCommand: Callable<Int> {
    @Option(names = ["-m", "--method"], description = ["Refresh data from the Cointracking API"])
    var method = ""

    @Option(names = ["-o", "--order"], description = ["ASC or DESC order by trade time"])
    var order: String = ""

    @Option(names = ["-l", "--limit"], description = ["Number of trades"])
    var limit: String = ""

    @Option(names = ["-s", "--start"], description = ["UNIX timestamp as trade start date"])
    var start: String = ""

    @Option(names = ["-e", "--end"], description = ["UNIX timestamp as trade end date"])
    var end: String = ""

    @Option(names = ["-p", "--trade-prices"], description = ["Show the value of every buy and sell in BTC and in your account currency"])
    var tradePrices: Boolean = false

    override fun call(): Int {
        val json = when (method) {
            "getTrades" -> {
                val parameters = mutableMapOf<String, String>()
                if (order.isNotBlank()) parameters["order"] = order
                if (limit.isNotBlank()) parameters["limit"] = limit
                if (start.isNotBlank()) parameters["start"] = start
                if (end.isNotBlank()) parameters["end"] = end
                if (tradePrices) parameters["trade_prices"] = "1"

                getTrades(parameters)
            }
            "getBalance" -> getBalance()
            "getHistoricalCurrency" -> getHistoricalCurrency()
            else -> return 1
        }

        when (json.isEmpty()) {
            true -> {
                println("json is empty")
            }
            false -> {
                println(json)
            }
        }

        return 0
    }

}