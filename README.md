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
Name: ADD
Input:  inReal0 TA_Input_Real
Input:  inReal1 TA_Input_Real
Output: outReal TA_Output_Real

user=> (ta "SIN")
Name: SIN
Input:  inReal TA_Input_Real
Output: outReal TA_Output_Real

user=> (ta "WILLR")
Name: WILLR
Input:  inPriceHLC TA_Input_Price
Option: optInTimePeriod min=2 max=100000 default=14
Output: outReal TA_Output_Real

user=> (ta "BBANDS")
Name: BBANDS
Input:  inReal TA_Input_Real
Option: optInTimePeriod min=2 max=100000 default=5
Option: optInNbDevUp min=-3.0E37 max=3.0E37 precision=2 default=2.0
Option: optInNbDevDn min=-3.0E37 max=3.0E37 precision=2 default=2.0
Option: optInMAType(0-SMA 1-EMA 2-WMA 3-DEMA 4-TEMA 5-TRIMA 6-KAMA 7-MAMA 8-T3) default= 0
Output: outRealUpperBand TA_Output_Real
Output: outRealMiddleBand TA_Output_Real
Output: outRealLowerBand TA_Output_Real

user=> (ta "sma")
Name: SMA
Input:  inReal TA_Input_Real
Option: optInTimePeriod min=2 max=100000 default=30
Output: outReal TA_Output_Real
nil
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
user=> (map #(take 2 %) (ta "bbands" [(close INTC)] 5 2.0 2.0 0))
``` 

### Functions

You can view all of the functions by calling ``(clj-ta-lib.util/print-groups)`` and ``(clj-ta-lib.util/print-functions)``

```
Group: Cycle Indicators
   HT_DCPERIOD
   HT_DCPHASE
   HT_PHASOR
   HT_SINE
   HT_TRENDMODE
Group: Math Operators
   ADD
   DIV
   MAX
   MAXINDEX
   MIN
   MININDEX
   MINMAX
   MINMAXINDEX
   MULT
   SUB
   SUM
Group: Math Transform
   ACOS
   ASIN
   ATAN
   CEIL
   COS
   COSH
   EXP
   FLOOR
   LN
   LOG10
   SIN
   SINH
   SQRT
   TAN
   TANH
Group: Momentum Indicators
   ADX
   ADXR
   APO
   AROON
   AROONOSC
   BOP
   CCI
   CMO
   DX
   IMI
   MACD
   MACDEXT
   MACDFIX
   MFI
   MINUS_DI
   MINUS_DM
   MOM
   PLUS_DI
   PLUS_DM
   PPO
   ROC
   ROCP
   ROCR
   ROCR100
   RSI
   STOCH
   STOCHF
   STOCHRSI
   TRIX
   ULTOSC
   WILLR
Group: Overlap Studies
   ACCBANDS
   BBANDS
   DEMA
   EMA
   HT_TRENDLINE
   KAMA
   MA
   MAMA
   MAVP
   MIDPOINT
   MIDPRICE
   SAR
   SAREXT
   SMA
   T3
   TEMA
   TRIMA
   WMA
Group: Pattern Recognition
   CDL2CROWS
   CDL3BLACKCROWS
   CDL3INSIDE
   CDL3LINESTRIKE
   CDL3OUTSIDE
   CDL3STARSINSOUTH
   CDL3WHITESOLDIERS
   CDLABANDONEDBABY
   CDLADVANCEBLOCK
   CDLBELTHOLD
   CDLBREAKAWAY
   CDLCLOSINGMARUBOZU
   CDLCONCEALBABYSWALL
   CDLCOUNTERATTACK
   CDLDARKCLOUDCOVER
   CDLDOJI
   CDLDOJISTAR
   CDLDRAGONFLYDOJI
   CDLENGULFING
   CDLEVENINGDOJISTAR
   CDLEVENINGSTAR
   CDLGAPSIDESIDEWHITE
   CDLGRAVESTONEDOJI
   CDLHAMMER
   CDLHANGINGMAN
   CDLHARAMI
   CDLHARAMICROSS
   CDLHIGHWAVE
   CDLHIKKAKE
   CDLHIKKAKEMOD
   CDLHOMINGPIGEON
   CDLIDENTICAL3CROWS
   CDLINNECK
   CDLINVERTEDHAMMER
   CDLKICKING
   CDLKICKINGBYLENGTH
   CDLLADDERBOTTOM
   CDLLONGLEGGEDDOJI
   CDLLONGLINE
   CDLMARUBOZU
   CDLMATCHINGLOW
   CDLMATHOLD
   CDLMORNINGDOJISTAR
   CDLMORNINGSTAR
   CDLONNECK
   CDLPIERCING
   CDLRICKSHAWMAN
   CDLRISEFALL3METHODS
   CDLSEPARATINGLINES
   CDLSHOOTINGSTAR
   CDLSHORTLINE
   CDLSPINNINGTOP
   CDLSTALLEDPATTERN
   CDLSTICKSANDWICH
   CDLTAKURI
   CDLTASUKIGAP
   CDLTHRUSTING
   CDLTRISTAR
   CDLUNIQUE3RIVER
   CDLUPSIDEGAP2CROWS
   CDLXSIDEGAP3METHODS
Group: Price Transform
   AVGDEV
   AVGPRICE
   MEDPRICE
   TYPPRICE
   WCLPRICE
Group: Statistic Functions
   BETA
   CORREL
   LINEARREG
   LINEARREG_ANGLE
   LINEARREG_INTERCEPT
   LINEARREG_SLOPE
   STDDEV
   TSF
   VAR
Group: Volatility Indicators
   ATR
   NATR
   TRANGE
Group: Volume Indicators
   AD
   ADOSC
   OBV
```




