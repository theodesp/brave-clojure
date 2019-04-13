(defmacro backwards
  [form]
  (reverse form))

(backwards (" backwards" " am" "I" str))
; => "I am backwards"

(def addition-list (list + 1 2))
(eval addition-list)
; => 3

(eval (concat addition-list [10]))
; => 13

(eval (list 'def 'lucky-number (concat addition-list [10])))
; => #'user/lucky-number

lucky-number
; => 13

;The Reader
;The reader converts the textual source code you save in a file
; or enter in the REPL into Clojure data structures.

(read-string "(+ 1 2)")
; => (+ 1 2)

(list? (read-string "(+ 1 2)"))
; => true

(conj (read-string "(+ 1 2)") :zagglewag)
; => (:zagglewag + 1 2)

(eval (read-string "(+ 1 2)"))
; => 3

(read-string "#(+ 1 %)")
; => (fn* [p1__423#] (+ 1 p1__423#))

(read-string "'(a b c)")
; => (quote (a b c))

(read-string "@var")
; => (clojure.core/deref var)

(read-string "; ignore!\n(+ 1 2)")
; => (+ 1 2)

;The Evaluator

;You can think of Clojure’s evaluator as a function that
; takes a data structure as an argument, processes the data
; structure using rules corresponding to the data structure’s
; type, and returns a result.

;These Things Evaluate to Themselves
true
; => true

false
; => false

{}
; => {}

:huzzah
; => :huzzah

()
; => ()

(read-string ("+"))
; => +

(type (read-string "+"))
; => clojure.lang.Symbol

(list (read-string "+") 1 2)
; => (+ 1 2)

(eval (list (read-string "+") 1 2))
; => 3

(eval (read-string "()"))
; => ()

(read-string "(1 + 1)")
; => (1 + 1)

;Clojure will throw an exception if you try to make it evaluate this list:

(eval (read-string "(1 + 1)"))
; => ClassCastException java.lang.Long cannot be cast to clojure.lang.IFn

(let [infix (read-string "(1 + 1)")]
  (list (second infix) (first infix) (last infix)))
; => (+ 1 1)

(eval
  (let [infix (read-string "(1 + 1)")]
    (list (second infix) (first infix) (last infix))))
; => 2

;Macros give you a convenient way to manipulate lists before Clojure evaluates
; them. Macros are a lot like functions: they take arguments and return a value,
; just like a function would.

(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))

(ignore-last-operand (+ 1 2 10))

;; This will not print anything
(ignore-last-operand (+ 1 2 (println "look at me!!!")))
; => 3

; Inspect how the macros are expanded
(macroexpand '(ignore-last-operand (+ 1 2 10)))
; => (+ 1 2)

(macroexpand '(ignore-last-operand (+ 1 2 (println "look at me!!!"))))
; => (+ 1 2)

;Infix notation
(defmacro infix
  [infixed]
  (list (second infixed)
        (first infixed)
        (last infixed)))

(infix (1 + 2))
; => 3

;macros enable syntactic abstraction.

(defn read-resource
  "Read a resource into a string"
  [path]
  (read-string (slurp (clojure.java.io/resource path))))

; Thread macros
(defn read-resource
  [path]
  (-> path
      clojure.java.io/resource
      slurp
      read-string))