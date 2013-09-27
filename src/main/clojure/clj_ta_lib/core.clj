(ns clj-ta-lib.core
   (:import [com.tictactec.ta.lib.meta CoreMetaData]
            [com.tictactec.ta.lib MInteger]))

(defn getFunc [func]
  (CoreMetaData/getInstance func))

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

(defn sma [data-array time-period]
  (let [func (getFunc "sma")
        input (double-array data-array)
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

(defn ema [data-array time-period]
  (let [func (getFunc "ema")
        input (double-array data-array)
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
        input price-holder
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
            
(defn rsi [data-array time-period]
  (let [func (getFunc "rsi")
        input (double-array data-array)
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
  