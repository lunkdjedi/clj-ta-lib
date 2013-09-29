(ns clj-ta-lib.yahoo
  (:import [com.tictactec.ta.lib.meta PriceHolder])
  (:require clojure.core.memoize)
  (:use [clojure.string :only (join split)]))

(defn- yahoo-raw-data [symbol]
  (slurp (str "http://ichart.finance.yahoo.com/table.csv?s=" symbol)))

;memoize raw data for five minutes
;(def yahoo-raw-data 
;  (clojure.core.memoize/ttl yahoo-raw-data :ttl/threshold (* 5 60 1000)))

(defn- yahoo-data 
"
Returns a sequence of vectors 
where each vector represents daily open high low close and volume data
([date open high low close volume adjclose])
"
[ticker]
    (let [raw-data (yahoo-raw-data ticker)]
    ;(let [raw-data (slurp (str "table-" ticker ".csv"))]
    (map #(split % #",") 					 ; Split each line by comma
        	(reverse                 ; Reverse Line order for proper date order
            (drop 1             	 ; Drop first non-parsable header line
            	(split raw-data #"\n")))))) ; Split the raw data by new line character


(defn- yahoo-vector [ticker]
"
Returns a vector of index-aligned vectors for stock history of provided ticker
Dates maintain default string format while other values are converted to BigDecimal 
[ [date] [open] [high] [low] [close] [volume] ]
"
    (let [column-data (apply map vector (yahoo-data ticker))]
    [   (into [] (nth column-data 0))                   ;Date
        (into [] (map read-string (nth column-data 1))) ;Open
        (into [] (map read-string (nth column-data 2))) ;High
        (into [] (map read-string (nth column-data 3))) ;Low
        (into [] (map read-string (nth column-data 4))) ;Close
        (into [] (map read-string (nth column-data 5))) ;Volume
    ]))

(defn- yahoo-price-holder 
    ([ticker]
	  (let [data (yahoo-vector ticker)]
	    (PriceHolder. (double-array (nth data 1));open
	                  (double-array (nth data 2));high
	                  (double-array (nth data 3));low
	                  (double-array (nth data 4));close
	                  (double-array (nth data 5));volume
	                  (double-array (count (nth data 1));open interest
                                 )))))

; memoize price holder data structure for 4 hours
(def price-holder 
  (clojure.core.memoize/ttl yahoo-price-holder :ttl/threshold (* 4 60 60 1000)))

(defn open [price-holder] (:o (bean price-holder)))
(defn high [price-holder] (:h (bean price-holder)))
(defn low [price-holder] (:l (bean price-holder)))
(defn close [price-holder] (:c (bean price-holder)))
(defn volume [price-holder] (:v (bean price-holder)))
