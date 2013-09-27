(ns clj-ta-lib.core
   (:import [com.tictactec.ta.lib.meta CoreMetaData PriceHolder]
            [com.tictactec.ta.lib MInteger])
   (:use [clj-ta-lib.yahoo]))

(defn getFunc [func]
  (CoreMetaData/getInstance func))

(defn getFunctionInputFlags [func]
  (.flags (.getInputParameterInfo (getFunc func) 0)))

(defn yahoo-price-holder 
  [ticker function]
	  (let [data (yahoo-vector ticker)
         flags (getFunctionInputFlags function)]
	    (PriceHolder. flags
	                  (double-array (nth data 1))
	                  (double-array (nth data 2))
	                  (double-array (nth data 3))
	                  (double-array (nth data 4))
	                  (double-array (nth data 5))
	                  (double-array (count (nth data 1))))))
  
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

(defn sma [window data-array]
  (let [func (getFunc "sma")
        input (double-array data-array)
        size (count input)
        begIndex (MInteger.)
        outNbElements (MInteger.)
        output (double-array size)]
    (.setInputParamReal func 0 input)
    (.setOutputParamReal func 0 output)
    (.setOptInputParamInteger func 0 window)
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
            
    
  