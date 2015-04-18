(defproject clj-ta-lib "0.1.0-SNAPSHOT"
  :description "Clojure Technical Analysis Library"
  :dependencies [
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/core.memoize "0.5.6"]
                 [junit/junit "4.11"]]
  
  :repl-options {:init-ns clj-ta-lib.core}
  :ring {:handler dataloader.photos/handler}
  :java-source-paths ["src/main/java"]
  :jvm-opts ["-Xms1024m" "-Xmx2048m" "-XX:PermSize=256m" "-XX:MaxPermSize=512m"])
