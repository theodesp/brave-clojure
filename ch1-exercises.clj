;Write a function that takes a number and adds 100 to it.
(defn add100
  [num]
  (+ num 100))

(add100 1)
;=> 101

;Write a function, dec-maker, that works exactly like the function inc-maker except with subtraction:

(defn dec-maker
  [dec-by]
  (fn [x] (- dec-by x)))

(def dec9 (dec-maker 9))
(dec9 10)
; => 1

; Write a function, mapset, that works like map except the return value is a set:

(defn mapset
  [f iter]
  (set (map f iter)))

(mapset inc [1 1 2 2])
; => #{2 3}
