(ns clj-ta-lib.util
  (:import [com.tictactec.ta.lib.meta CoreMetaData TaGrpService TaFuncService]
           [com.tictactec.ta.lib.meta.annotation OptInputParameterType]))

;This proxy is implementing an inteface method with a return value of void.
;With no return value from the method we must use side-effects to expose 
;the group name and function names within the group
(def group-service (proxy [TaGrpService] []
  (execute [group function-set] 
    (println (str "Group: " group))
		  (doseq [function (seq function-set)]
		    (println (str "   " (.name (.getFuncInfo function))))))))

(defn print-groups []
  (CoreMetaData/forEachGrp group-service))

(defn print-function [function]
  (println (str "Name: " (.name (.getFuncInfo function))))

    ;PRINT INPUTS
      (doseq [i (range (-> function .getFuncInfo .nbInput))]
        (let [pinfo (.getInputParameterInfo function i)]
          (println "  " (-> pinfo .paramName) (-> pinfo .type .name))))

    ;PRINT OPTIONAL INPUTS
      (doseq [i (range (-> function .getFuncInfo .nbOptInput))]
        (let [pinfo (.getOptInputParameterInfo function i)]
          (println "    " (-> pinfo .paramName) (-> pinfo .type .name))
          (cond 
            (= (-> pinfo .type) OptInputParameterType/TA_OptInput_RealRange) 
            (let [rrange (.getOptInputRealRange function i)] 
              (println "     "
                (str " min=" (.min rrange) 
	                   " max=" (.max rrange) 
	                   " precision=" (.precision rrange) 
	                   " default=" (.defaultValue rrange))))
                                                           
            (= (-> pinfo .type) OptInputParameterType/TA_OptInput_RealList) 
            (let [rlist (.getOptInputRealList function i)] 
              (println "      " (map #(str %1 "(" %2 ")") (.string rlist) (.value rlist))))
            
            (= (-> pinfo .type) OptInputParameterType/TA_OptInput_IntegerRange) 
            (let [irange (.getOptInputIntegerRange function i)] 
              (println "     " 
                (str " min=" (.min irange) 
                     " max=" (.max irange) 
                     " default=" (.defaultValue irange))))

            (= (-> pinfo .type) OptInputParameterType/TA_OptInput_IntegerList) 
            (let [ilist (.getOptInputIntegerList function i)]
              (println "      " (map  #(str %1 "(" %2 ")") (.string ilist) (.value ilist))))
            )
          ))
    
    ;PRINT OUTPUTS
      (doseq [i (range (-> function .getFuncInfo .nbOutput))]
        (let [pinfo (.getOutputParameterInfo function i)]
          (println "  " (-> pinfo .paramName) (-> pinfo .type .name)))))

  
(def function-service (proxy [TaFuncService] []
  (execute [function]
    (print-function function))))


(defn print-functions []
  (CoreMetaData/forEachFunc function-service))