(ns clj-ta-lib.util
  (:import [com.tictactec.ta.lib.meta CoreMetaData TaGrpService TaFuncService]))

;This proxy is implementing an inteface method with a return value of void.
;With no return value from the method we must use side-effects to expose 
;the group name and function names within the group
(def group-service (proxy [TaGrpService] []
  (execute [group s] 
    (println (str "Group: " group))
		  (doseq [x (seq s)]
		    (println (str "   " (.name (.getFuncInfo x))))))))

(defn print-groups []
  (CoreMetaData/forEachGrp group-service))
