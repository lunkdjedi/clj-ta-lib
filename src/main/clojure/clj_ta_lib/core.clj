(ns clj-ta-lib.core
   (:import [com.tictactec.ta.lib.meta CoreMetaData PriceHolder]
            [com.tictactec.ta.lib MInteger]))

(defn getFunc [func]
  (CoreMetaData/getInstance func))


(defn price-holder [flags open high low close volume]
      (PriceHolder. flags 
                    (double-array open)
                    (double-array high)
                    (double-array low)
                    (double-array close)
                    (double-array volume)
                    (double-array (count open))))

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
      {:begIndex (.value begIndex) :nbElements (.value outNbElements)})))

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
      {:begIndex (.value begIndex) :nbElements (.value outNbElements)})))
    
  