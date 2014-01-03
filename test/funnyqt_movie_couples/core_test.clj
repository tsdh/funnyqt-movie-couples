(ns funnyqt-movie-couples.core-test
  (:require [clojure.test :refer :all]
            [funnyqt-movie-couples.core :refer :all]
            [funnyqt.emf :as emf]))

(emf/load-metamodel "movies.ecore")

(deftest test-all-models-algo
  (println "Warming up...")
  (make-couples (emf/load-model "example_0850000.xmi"))
  (println "Warmup done...")
  (doseq [file (sort (-> (java.io.File. ".") (.list)))
          :when (.endsWith file ".xmi")]
    (println "TESTING MODEL:" file)
    (System/gc)
    (let [m (emf/load-model file)]
      (funnyqt.utils/timing "  RULE: %R couples/%s nodes => %T"
                            (count (make-couples m))
                            (count (emf/eallobjects m))))))
