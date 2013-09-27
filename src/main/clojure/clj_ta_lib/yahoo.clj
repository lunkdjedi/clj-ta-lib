(ns clj-ta-lib.yahoo
  (:require [clj-time.core :as time]
            [clj-time.format :as fmt])
  (:import [com.tictactec.ta.lib.meta CoreMetaData PriceHolder])
  (:use [clojure.core memoize]
        [clojure.string :only (join split)]))
  
(def custom-formatter (fmt/formatter "yyyy-MM-dd"))

(defn to-milliseconds-from-epoch [x]
  (time/in-millis (time/interval (time/epoch) (fmt/parse custom-formatter x))))

(defn to-year [x]
  (time/year (fmt/parse custom-formatter x)))

(defn days-ago-in-millis [d]
  (time/in-millis (time/interval (time/epoch) (time/from-now (time/days (- 1 d))))))

(defn- yahoo-raw-data [symbol]
  (slurp (str "http://ichart.finance.yahoo.com/table.csv?s=" symbol)))

(def yahoo-raw-data (ttl yahoo-raw-data :ttl/threshold (* 4 60 60 1000)))

(defn yahoo-data 
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


(defn yahoo-vector [ticker]
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

