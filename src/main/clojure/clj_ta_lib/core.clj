(ns clj-ta-lib.core
  (:import [com.tictactec.ta.lib.meta CoreMetaData PriceHolder]
           [com.tictactec.ta.lib.meta.annotation InputFlags OptInputParameterType InputParameterType OutputParameterType]
           [com.tictactec.ta.lib MInteger]
           [java.lang Exception])
  (:use [clj-ta-lib util]))

(defn- getFunc [func]
  (CoreMetaData/getInstance func))

(defn- addflags [price-holder flags]
  (let [bean (bean price-holder)]
		(PriceHolder. flags
		             (:o bean);open
		             (:h bean);high
		             (:l bean);low
		             (:c bean);close
		             (:v bean);volume
		             (:i bean);open interest
		            )))

(defn- getFunctionInputFlags [func]
  (let [flags (.flags (.getInputParameterInfo func 0))] 
    (if (zero? flags)
      (bit-or InputFlags/TA_IN_PRICE_OPEN 
              InputFlags/TA_IN_PRICE_HIGH 
              InputFlags/TA_IN_PRICE_LOW 
              InputFlags/TA_IN_PRICE_CLOSE 
              InputFlags/TA_IN_PRICE_VOLUME 
              InputFlags/TA_IN_PRICE_OPENINTEREST)
      flags)))

(defn get-out-array [func ticks]
  (let [output (atom [])]
	  (doseq [i (range (-> func .getFuncInfo .nbOutput))]
		  (let [pinfo (.getOutputParameterInfo func i)]
		    (cond
					(= (-> pinfo .type) OutputParameterType/TA_Output_Real)
					(swap! output conj (double-array ticks))
					
					(= (-> pinfo .type) OutputParameterType/TA_Output_Integer)
					(swap! output conj (int-array ticks)))))
   
   @output))
        
(defn ta 
  ([name]
    (print-function (getFunc name)))
  ([name input & options]
	  (let [func (getFunc name)
	        nbOptInputs (-> func .getFuncInfo .nbOptInput)
	        nbInputs (-> func .getFuncInfo .nbInput)
	        nbOutputs (-> func .getFuncInfo .nbOutput)
	        begIndex (MInteger.)
	        outNbElements (MInteger.)
	        inputSize (atom nil)
	        output (atom nil)]
	
	    ;Set Options
	    (if (= (count options) nbOptInputs)
	      (doseq [i (range nbOptInputs)]
	        (let [pinfo (.getOptInputParameterInfo func i)]
	          (cond 
	           (or 
	             (= (-> pinfo .type) OptInputParameterType/TA_OptInput_RealRange)
	             (= (-> pinfo .type) OptInputParameterType/TA_OptInput_RealList))
	           (.setOptInputParamReal func i (str (nth options i)))
	           
	           (or 
	             (= (-> pinfo .type) OptInputParameterType/TA_OptInput_IntegerRange) 
	             (= (-> pinfo .type) OptInputParameterType/TA_OptInput_IntegerList))
	           (.setOptInputParamInteger func i (nth options i))
	           
	           :else
	           (throw (Exception. "InvalidArgument - Options")))))
	      ((clj-ta-lib.util/print-function func)
	      (throw (Exception. "Invalid number of options"))))
	    
	    ;Set Inputs
	    (if (= (count input) nbInputs)
				(doseq [i (range nbInputs)]
				  (let [pinfo (.getInputParameterInfo func i)]
				    (cond
				      (= (-> pinfo .type) InputParameterType/TA_Input_Price)
				      (.setInputParamPrice func i (addflags (nth input i) (getFunctionInputFlags func)))
				      
				      (= (-> pinfo .type) InputParameterType/TA_Input_Real)
				      (.setInputParamReal func i (nth input i))
				      
				      (= (-> pinfo .type) InputParameterType/TA_Input_Integer)
				      (.setInputParamInteger func i (nth input i)))))
				((clj-ta-lib.util/print-function func) 
				(throw (Exception. "Invalid number of inputs"))))
	    
	    ; At this point we need the size or number of ticks of the inputs
	    (let [pinfo (.getInputParameterInfo func 0)]
		    (if (= (-> pinfo .type) InputParameterType/TA_Input_Price)
		      (compare-and-set! inputSize nil (count (:c (bean (first input)))))
		      (compare-and-set! inputSize nil (count (first input)))))
	    
	    ;Construct output arrays
	    (compare-and-set! output nil (get-out-array func @inputSize))
	    
	    ;Set Output Paramters
	    (doseq [i (range nbOutputs)]
	      (let [pinfo (.getOutputParameterInfo func i)]
	        (cond
	          (= (-> pinfo .type) OutputParameterType/TA_Output_Real)
	          (.setOutputParamReal func i (nth @output i))
	          
	          (= (-> pinfo .type) OutputParameterType/TA_Output_Integer)
	          (.setOutputParamInteger func i (nth @output i))
	          
	          )))
	    
	    (.callFunc func 0 (- @inputSize 1) begIndex outNbElements)
	    
	    (with-meta
	      (map vec @output)
	      {:begIndex (.value begIndex) :nbElements (.value outNbElements)  :lookback (.getLookback func)})))
        
    )