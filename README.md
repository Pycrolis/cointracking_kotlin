# Cointraking API with Kotlin

## Usage
```bash
Usage: cointracking [-p] [-e=<end>] [-l=<limit>] [-m=<method>] [-o=<order>]
                    [-s=<start>]
Access your Cointracking account data via API. Need the following environment
variables: API_KEY and API_SECRET.
  -e, --end=<end>         UNIX timestamp as trade end date
                            Default:
  -l, --limit=<limit>     Number of trades
                            Default:
  -m, --method=<method>   Refresh data from the Cointracking API
                            Default:
  -o, --order=<order>     ASC or DESC order by trade time
                            Default:
  -p, --trade-prices      Show the value of every buy and sell in BTC and in
                            your account currency
  -s, --start=<start>     UNIX timestamp as trade start date
                            Default:
```

## Environment variables
- `API_KEY`: your API key.
- `API_SECRET`: your API secret.

## Quick run

### Get all trades

``` bash
./gradlew bootRun --args='-m=getTrades'
```
### Get the 5700 first trades

``` bash
./gradlew bootRun --args='-m=getTrades -l=5700 -o=ASC'
```

### Get the trades from 2023-01-01 00:00:00 UTC to 2023-12-31 23:59:59 UTC

``` bash
./gradlew bootRun --args='-m=getTrades -s=1672531200 -e=1704067199 -o=ASC'
```

### Get balance

``` bash
./gradlew bootRun --args='-m=getBalance'
```