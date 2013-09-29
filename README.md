# `clj-ta-lib`
==========

Clojure Wrapper of TA-LIB

## Bugs and Enhancements

Please open issues against the [official clj-ta-lib repo on Github](https://github.com/lunkdjedi/clj-ta-lib/issues).

## Usage

### Running from Maven

This project can be used directly from maven with a repl.

```
$ mvn package clojure:repl
```

### clj-ta-lib.core

The main namespace for ta-helper operations in the `clj-ta-lib` library is `clj-ta-lib.core`.

``` clj
user=> (use 'clj-ta-lib.core)
``` 

Get information about ta function

``` clj
user=> (ta "ADD")
user=> (ta "SIN")
user=> (ta "WILLR")
user=> (ta "BBANDS")
``` 

The input to every ta function must be placed in a clojure vector. The input types into the vector can be double-arrays or PriceHolders. Output is always a sequence of vectors.

``` clj
user=> (ta "SIN" [ (double-array [1 2 3]) ])
([0.8414709848078965 0.9092974268256817 0.1411200080598672])
user=> (ta "ADD" [ (double-array [10 100 1000]) (double-array [1 2 3]) ])
([11.0 102.0 1003.0])
```

PriceHolders are a datatype from the ta-lib java code and I have included an optional library to get complete historical PriceHolders from Yahoo. 

``` clj
user=> (use 'clj-ta-lib.yahoo)
user=> (def INTC (price-holder "INTC"))
#'user/INTC
user=> (ta "sma" [(close INTC)] 50)
user=> (meta (ta "sma" [(close INTC)] 50))
{:begIndex 49, :nbElements 6816, :lookback 49}
user=> (ta "ema" [(close INTC)] 20)
user=> (ta "rsi" [(close INTC)] 20)
user=> (ta "willr" [INTC] 20)
user=> (ta "bbands" [(close INTC)] 5 2.0 2.0 0)
``` 


