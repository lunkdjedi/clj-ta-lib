(ns clj-ta-lib.core
   (:import [com.tictactec.ta.lib.meta CoreMetaData PriceHolder]
            [com.tictactec.ta.lib.meta.annotation InputFlags]
            [com.tictactec.ta.lib MInteger]))

(defn getFunc [func]
  (CoreMetaData/getInstance func))

(defn addflags [price-holder flags]
  (let [bean (bean price-holder)]
		(PriceHolder. flags
		             (:o bean);open
		             (:h bean);high
		             (:l bean);low
		             (:c bean);close
		             (:v bean);volume
		             (:i bean);open interest
		            )))

(defn getFunctionInputFlags [func]
  (let [flags (.flags (.getInputParameterInfo func 0))] 
    (if (zero? flags)
      (bit-or InputFlags/TA_IN_PRICE_OPEN 
              InputFlags/TA_IN_PRICE_HIGH 
              InputFlags/TA_IN_PRICE_LOW 
              InputFlags/TA_IN_PRICE_CLOSE 
              InputFlags/TA_IN_PRICE_VOLUME 
              InputFlags/TA_IN_PRICE_OPENINTEREST)
      flags)))

(defn acos [data-array]
  (let [func (getFunc "acos")
        input (double-array data-array)
        size (count input)
        begIndex (MInteger.)
        outNbElements (MInteger.)
        output (double-array size)]
    (.setInputParamReal func 0 input)
    (.setOutputParamReal func 0 output)
    (.callFunc func 0 (- size 1) begIndex outNbElements)
    (with-meta 
      [(vec output)] 
      {:begIndex (.value begIndex) :nbElements (.value outNbElements) :lookback (.getLookback func)})))

(defn sma [price-holder time-period & {:keys [column] :or {column :c}}]
  (let [func (getFunc "sma")
        input (column (bean price-holder))
        size (count input)
        begIndex (MInteger.)
        outNbElements (MInteger.)
        output (double-array size)]
    (.setInputParamReal func 0 input)
    (.setOutputParamReal func 0 output)
    (.setOptInputParamInteger func 0 time-period)
    (.callFunc func 0 (- size 1) begIndex outNbElements)
    (with-meta 
      [(vec output)] 
      {:begIndex (.value begIndex) :nbElements (.value outNbElements)  :lookback (.getLookback func)})))

(defn ema [price-holder time-period & {:keys [column] :or {column :c}}]
  (let [func (getFunc "ema")
        input (column (bean price-holder))
        size (count input)
        begIndex (MInteger.)
        outNbElements (MInteger.)
        output (double-array size)]
    (.setInputParamReal func 0 input)
    (.setOutputParamReal func 0 output)
    (.setOptInputParamInteger func 0 time-period)
    (.callFunc func 0 (- size 1) begIndex outNbElements)
    (with-meta 
      [(vec output)] 
      {:begIndex (.value begIndex) :nbElements (.value outNbElements)  :lookback (.getLookback func)})))

(defn rsi [price-holder time-period & {:keys [column] :or {column :c}}]
  (let [func (getFunc "rsi")
        input (column (bean price-holder))
        size (count input)
        begIndex (MInteger.)
        outNbElements (MInteger.)
        output (double-array size)]
    (.setInputParamReal func 0 input)
    (.setOutputParamReal func 0 output)
    (.setOptInputParamInteger func 0 time-period)
    (.callFunc func 0 (- size 1) begIndex outNbElements)
    (with-meta 
      [(vec output)] 
      {:begIndex (.value begIndex) :nbElements (.value outNbElements)  :lookback (.getLookback func)})))

(defn willr [price-holder & {:keys [time-period] :or {time-period 14}}]
  (let [func (getFunc "willr")
        input (addflags price-holder (getFunctionInputFlags func))
        size (count (:c (bean input)))
        begIndex (MInteger.)
        outNbElements (MInteger.)
        output (double-array size)]
    (.setInputParamPrice func 0 input)
    (.setOutputParamReal func 0 output)
    (.setOptInputParamInteger func 0 time-period)
    (.callFunc func 0 (- size 1) begIndex outNbElements)
    (with-meta 
      [(vec output)] 
      {:begIndex (.value begIndex) :nbElements (.value outNbElements)  :lookback (.getLookback func)})))
            

  