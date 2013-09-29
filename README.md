# `clj-ta-lib`
==========

Clojure Wrapper of TA-LIB

## Bugs and Enhancements

Please open issues against the [official clj-ta-lib repo on Github](https://github.com/lunkdjedi/clj-ta-lib/issues).

## Usage

### clj-ta-lib.core

The main namespace for ta-helper operations in the `clj-ta-lib` library is `clj-ta-lib.core`.

``` clj
=> (use 'clj-ta-lib.core)
``` 

There is also an optional library to fetch historical prices from Yahoo. 

``` clj
=> (use 'clj-ta-lib.yahoo)
=> (yahoo-price-holder "INTC")
#<PriceHolder com.tictactec.ta.lib.meta.PriceHolder@3823bdd1>
=>
``` 


