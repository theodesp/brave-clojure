(macroexpand '(when boolean-expression
                expression-1
                expression-2
                expression-3))
; => (if boolean-expression
(do expression-1
    expression-2
    expression-3))

; You can also use argument destructuring in macro definitions, just like you can with functions:

(defmacro infix-2
  [[operand1 op operand2]]
  (list op operand1 operand2))

;Building Lists for Evaluation
(defmacro my-print
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result))

(defmacro unless
  "Inverted 'if'"
  [test & branches]
  (conj (reverse branches) test 'if))

;Syntax Quoting

; Quoting does not include a namespace if your code doesn’t include a namespace:

'+
; => +

;Syntax quoting will always include the symbol’s full namespace:

`+
; => clojure.core/+

`(+ 1 2)
; => (clojure.core/+ 1 2)

;The other difference between quoting and syntax quoting is that the latter allows you to unquote forms using the tilde, ~

`(+ 1 ~(inc 1))
; => (clojure.core/+ 1 2)

`(+ 1 (inc 1))
; => (clojure.core/+ 1 (clojure.core/inc 1))

(defmacro code-critic
  "Phrases are courtesy Hermes Conrad from Futurama"
  [bad good]
  (list 'do
        (list 'println
              "Great squid of Madrid, this is bad code:"
              (list 'quote bad))
        (list 'println
              "Sweet gorilla of Manila, this is good code:"
              (list 'quote good))))

(code-critic (1 + 1) (+ 1 1))
; => Great squid of Madrid, this is bad code: (1 + 1)
; => Sweet gorilla of Manila, this is good code: (+ 1 1)

(defmacro code-critic
  "Phrases are courtesy Hermes Conrad from Futurama"
  [bad good]
  `(do (println "Great squid of Madrid, this is bad code:"
                (quote ~bad))
       (println "Sweet gorilla of Manila, this is good code:"
                (quote ~good))))

(defn criticize-code
  [criticism code]
  `(println ~criticism (quote ~code)))


;Refactoring a Macro and Unquote Splicing
(defmacro code-critic
  [bad good]
  `(do ~(criticize-code "Cursed bacteria of Liberia, this is bad code:" bad)
       ~(criticize-code "Sweet sacred boa of Western and Eastern Samoa, this is good code:" good)))

`(+ ~(list 1 2 3))
; => (clojure.core/+ (1 2 3))

; unuote splicing
`(+ ~@(list 1 2 3))
; => (clojure.core/+ 1 2 3)

(defmacro code-critic
  [{:keys [good bad]}]
  `(do ~@(map #(apply criticize-code %)
              [["Sweet lion of Zion, this is bad code:" bad]
               ["Great cow of Moscow, this is good code:" good]])))

(code-critic (1 + 1) (+ 1 1))
; => Sweet lion of Zion, this is bad code: (1 + 1)
; => Great cow of Moscow, this is good code: (+ 1 1)

;Things to Watch Out For

;Variable Capture

(def message "Good job!")
(defmacro with-mischief
  [& stuff-to-do]
  (concat (list 'let ['message "Oh, big deal!"])
          stuff-to-do))

(with-mischief
  (println "Here's how I feel about that thing you did: " message))
; => Here's how I feel about that thing you did: Oh, big deal!

(gensym)
; => G__655

(gensym)
; => G__658

(gensym 'message)
; => message4760

(gensym 'message)
; => message4763

(defmacro without-mischief
  [& stuff-to-do]
  (let [macro-message (gensym 'message)]
    `(let [~macro-message "Oh, big deal!"]
       ~@stuff-to-do
       (println "I still need to say: " ~macro-message))))

(without-mischief
  (println "Here's how I feel about that thing you did: " message))
; => Here's how I feel about that thing you did:  Good job!
; => I still need to say:  Oh, big deal!

;Double Evaluation
(defmacro report
  [to-try]
  `(if ~to-try
     (println (quote ~to-try) "was successful:" ~to-try)
     (println (quote ~to-try) "was not successful:" ~to-try)))

;; Thread/sleep takes a number of milliseconds to sleep for
(report (do (Thread/sleep 1000) (+ 1 1)))

; Solution
(defmacro report
  [to-try]
  `(let [result# ~to-try]
     (if result#
       (println (quote ~to-try) "was successful:" result#)
       (println (quote ~to-try) "was not successful:" result#))))

;Macros All the Way Down

(def order-details
  {:name "Mitchard Blimmons"
   :email "mitchard.blimmonsgmail.com"})

(def order-details-validations
  {:name
   ["Please enter a name" not-empty]

   :email
   ["Please enter an email address" not-empty

    "Your email address doesn't look like an email address"
    #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
  "Return a seq of error messages"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))

(error-messages-for "" ["Please enter a name" not-empty])
; => ("Please enter a name")

(defn validate
  "Returns a map with a vector of errors for each key"
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[fieldname validation-check-groups] validation
                  value (get to-validate fieldname)
                  error-messages (error-messages-for value validation-check-groups)]
              (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
          {}
          validations))

(validate order-details order-details-validations)
; => {:email ("Your email address doesn't look like an email address")}

(defmacro if-valid
  "Handle validation more concisely"
  [to-validate validations errors-name & then-else]
  `(let [~errors-name (validate ~to-validate ~validations)]
     (if (empty? ~errors-name)
       ~@then-else)))

(macroexpand
  '(if-valid order-details order-details-validations my-error-name
             (println :success)
             (println :failure my-error-name)))
(let*
  [my-error-name (user/validate order-details order-details-validations)]
  (if (clojure.core/empty? my-error-name)
    (println :success)
    (println :failure my-error-name)))