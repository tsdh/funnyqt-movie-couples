(ns ^{:pattern-expansion-context :emf}
  funnyqt-movie-couples.core
  (:require [clojure.set      :as set]
            [funnyqt.emf      :as emf]
            [funnyqt.in-place :as ip]))

(defn three-common-movies? [p1 p2]
  (>= (count (set/intersection
              (into #{} (emf/eget-raw p1 :movies))
              (into #{} (emf/eget-raw p2 :movies))))
      3))

(ip/defrule ^:forall make-couples [m]
  [p1<Person> -<movies>-> <> -<persons>-> p2
   :when (and (not (identical? p1 p2))
              (three-common-movies? p1 p2))
   :as #{p1 p2}
   :distinct]
  (emf/ecreate! m 'Couple :p1 p1 :p2 p2))
