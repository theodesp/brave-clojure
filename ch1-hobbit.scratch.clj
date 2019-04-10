(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])



(defn matching-part
  ;Create matching part if string starts with `left-`
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

;(matching-part {:name "left-hand", :size 1})
;=> {:name "right-hand", :size 1}

(defn symmetrize-body-parts
  "
  Expects a seq of maps that have a :name and :size

  Iterates over a list of parts and creates a new list with the matching-parts
  "
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts               ; loop
         final-body-parts []]
    (if (empty? remaining-asym-parts)                       ; check if we finish
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]        ; else grab the first part and try to find the matching part
        (recur remaining                                    ; recur over remaining
               (into final-body-parts
                     (set [part (matching-part part)]))))))) ; assign to final-body-parts the matching part if any

; let binds names to values.
(let [x 3]
  x)
; => 3

(def dalmatian-list
  ["Pongo" "Perdita" "Puppy 1" "Puppy 2"])
(let [dalmatians (take 2 dalmatian-list)]
  dalmatians)
; => ("Pongo" "Perdita")

; let also introduces a new scope
(def x 0)
(let [x 1] x)
; => 1

(let [[pongo & dalmatians] dalmatian-list]
  [pongo dalmatians])
; => ["Pongo" ("Perdita" "Puppy 1" "Puppy 2")]

; into and set
(into [] (set [:a :a]))
; => [:a]

;loop
(loop [iteration 0]
  (println (str "Iteration " iteration))
  (if (> iteration 3)
    (println "Goodbye!")
    (recur (inc iteration))))

(defn recursive-printer
  ([]
   (recursive-printer 0))
  ([iteration]
   (println iteration)
   (if (> iteration 3)
     (println "Goodbye!")
     (recursive-printer (inc iteration)))))
(recursive-printer)

;Regular Expressions
(re-find #"^left-" "left-eye")
; => "left-"

(re-find #"^left-" "cleft-chin")
; => nil

(re-find #"^left-" "wongleblart")
; => nil

;; sum with reduce
(reduce + [1 2 3 4])
; => 10

(defn my-reduce
  ([f initial coll]
   (loop [result initial
          remaining coll]
     (if (empty? remaining)
       result
       (recur (f result (first remaining)) (rest remaining)))))
  ([f [head & tail]]
   (my-reduce f head tail)))

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

(defn hit
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
            accumulated-size (:size part)]
       (if (> accumulated-size target)
         part
         (recur remaining (+ accumulated-size (:size (first remaining))))))))

(hit asym-hobbit-body-parts)
; => {:name "right-upper-arm", :size 3}

(hit asym-hobbit-body-parts)
; => {:name "chest", :size 10}

(hit asym-hobbit-body-parts)
; => {:name "left-eye", :size 1}