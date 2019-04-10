1
"String"
["a" "vector" "of" "strings"]

; (operator operand1 operand2 ... operandn)
(+ 1 2 3)
; => 6
(str "It was the panda " "in the library " "with a dust buster")
; => "It was the panda in the library with a dust buster"

; Control Flow
;(if boolean-form
;  then-form
;  optional-else-form)
(if true
  "By Zeus's hammer!"
  "By Aquaman's trident!")
; => "By Zeus's hammer!"

(if false
  "By Zeus's hammer!"
  "By Aquaman's trident!")
; => "By Aquaman's trident!"

;do
; The do operator lets you wrap up multiple forms in parentheses and run each of them.
(if true
  (do (println "Success!")
      "By Zeus's hammer!")
  (do (println "Failure!")
      "By Aquaman's trident!"))
; => Success!
; => "By Zeus's hammer!"

; The when operator is like a combination of if and do, but with no else branch. Here’s an example:

(when true
  (println "Success!")
  "abra cadabra")
; => Success!
; => "abra cadabra"

; Both nil and false are used to represent logical falsiness,
(nil? 1)
; => false

(nil? nil)
; => true

; Clojure’s equality operator is =:
(= 1 1)
; => true

(= nil nil)
; => true

(= 1 2)
; => false

(or false nil :large_I_mean_venti :why_cant_I_just_say_large)
; => :large_I_mean_venti

(or (= 0 1) (= "yes" "no"))
; => false

(or nil)
; => nil

(and :free_wifi :hot_coffee)
; => :hot_coffee

(and :feelin_super_cool nil false)
; => nil

;You use def to bind a name to a value in Clojure:
(def failed-protagonist-names
  ["Larry Potter" "Doreen the Explorer" "The Incredible Bulk"])

failed-protagonist-names
; => ["Larry Potter" "Doreen the Explorer" "The Incredible Bulk"]

(defn error-message
  [severity]
  (str "OH GOD! IT'S A DISASTER! WE'RE "
       (if (= severity :mild)
         "MILDLY INCONVENIENCED!"
         "DOOOOOOOMED!")))

; Maps are similar to dictionaries or hashes in other languages.
; Empty map
{}
{:first-name "Charlie"
 :last-name "McFishwich"}

;Here we associate "string-key" with the + function:
{"string-key" +}

; Maps can be nested
{:name {:first "John" :middle "Jacob" :last "Jingleheimerschmidt"}}

; Besides using map literals, you can use the hash-map function to create a map:

(hash-map :a 1 :b 2)
; => {:a 1 :b 2}

;You can look up values in maps with the get function:
(get {:a 0 :b 1} :b)
; => 1

(get {:a 0 :b {:c "ho hum"}} :b)
; => {:c "ho hum"}

(get {:a 0 :b 1} :c)
; => nil

(get {:a 0 :b 1} :c "unicorns?")
; => "unicorns?"

; The get-in function lets you look up values in nested maps:
(get-in {:a 0 :b {:c "ho hum"}} [:b :c])
; => "ho hum"

; Another way
({:name "The Human Coffeepot"} :name)
; => "The Human Coffeepot"

; Keywords
:a
:rumplestiltsken
:34
:_?

(:a {:a 1 :b 2 :c 3})
; => 1

;You can provide a default value, as with get:
(:d {:a 1 :b 2 :c 3} "No gnome knows homes like Noah knows")
; => "No gnome knows homes like Noah knows"

;Vectors
;A vector is similar to an array, in that it’s a 0-indexed collection. For example, here’s a vector literal:

[3 2 1]

(get [3 2 1] 0)
; => 3

(get ["a" {:name "Pugsley Winterbottom"} "c"] 1)
; => {:name "Pugsley Winterbottom"}

; You can create vectors with the vector function:

(vector "creepy" "full" "moon")
; => ["creepy" "full" "moon"]

; Lists are similar to vectors in that they’re linear collections of values. But there are some differences.
; For example, you can’t retrieve list elements with get.

'(1 2 3 4)
; => (1 2 3 4)

(list 1 "two" {3 4})
; => (1 "two" {3 4})

; Elements are added to the beginning of a list:

(conj '(1 2 3) 4)
; => (4 1 2 3)

; Sets
#{"kurt vonnegut" 20 :icicle}

(hash-set 1 1 2 2)
; => #{1 2}

(conj #{:a :b} :b)
; => #{:a :b}

; You can also create sets from existing vectors and lists by using the set function:
(set [3 3 3 4 4])
; => #{3 4}

(contains? #{:a :b} :a)
; => true

(contains? #{:a :b} 3)
; => false

(contains? #{nil} nil)
; => true

(:a #{:a :b})
; => :a

(get #{:a :b} :a)
; => :a

(get #{:a nil} nil)
; => nil

(get #{:a :b} "kurt vonnegut")
; => nil

;It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures.
;—Alan Perlis

;Functions

;Calling Functions
(+ 1 2 3 4)
(* 1 2 3 4)
(first [1 2 3 4])

(or + -)
; => #<core$_PLUS_ clojure.core$_PLUS_@76dace31>

((or + -) 1 2 3)
; => 6

((and (= 1 1) +) 1 2 3)
; => 6

((first [+ 0]) 1 2 3)
; => 6

; these aren’t valid function calls, because numbers and strings aren’t functions:

(1 2 3 4)
("test" 1 2 3)

(inc 1.1)
; => 2.1

(map inc [0 1 2 3])
; => (1 2 3 4)

(defn no-params
  []
  "I take no parameters!")
(defn one-param
  [x]
  (str "I take one parameter: " x))
(defn two-params
  [x y]
  (str "Two parameters! That's nothing! Pah! I will smoosh them "
       "together to spite you! " x y))

(defn multi-arity
  ;; 3-arity arguments and body
  ([first-arg second-arg third-arg]
   (do-things first-arg second-arg third-arg))
  ;; 2-arity arguments and body
  ([first-arg second-arg]
   (do-things first-arg second-arg))
  ;; 1-arity arguments and body
  ([first-arg]
   (do-things first-arg)))

(defn codger-communication
  [whippersnapper]
  (str "Get off my lawn, " whippersnapper "!!!"))

(defn codger
    [& whippersnappers]
  (map codger-communication whippersnappers))

(codger "Billy" "Anne-Marie" "The Incredible Bulk")
; => ("Get off my lawn, Billy!!!"
"Get off my lawn, Anne-Marie!!!"
"Get off my lawn, The Incredible Bulk!!!")

(defn favorite-things
  [name & things]
  (str "Hi, " name ", here are my favorite things: "
       (clojure.string/join ", " things)))

(favorite-things "Doreen" "gum" "shoes" "kara-te")
; => "Hi, Doreen, here are my favorite things: gum, shoes, kara-te"


; Destructurin
;; Return the first element of a collection
(defn my-first
  [[first-thing]] ; Notice that first-thing is within a vector
  first-thing)

(my-first ["oven" "bike" "war-axe"])
; => "oven"

(defn chooser
  [[first-choice second-choice & unimportant-choices]]
  (println (str "Your first choice is: " first-choice))
  (println (str "Your second choice is: " second-choice))
  (println (str "We're ignoring the rest of your choices. "
                "Here they are in case you need to cry over them: "
                (clojure.string/join ", " unimportant-choices))))

(chooser ["Marmalade", "Handsome Jack", "Pigpen", "Aquaman"])
; => Your first choice is: Marmalade
; => Your second choice is: Handsome Jack
; => We're ignoring the rest of your choices. Here they are in case \
; you need to cry over them: Pigpen, Aquaman

(defn announce-treasure-location
     [{lat :lat lng :lng}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng)))

(announce-treasure-location {:lat 28.22 :lng 81.33})
; => Treasure lat: 100
; => Treasure lng: 50


; Shorter
(defn announce-treasure-location
  [{:keys [lat lng]}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng)))

; Keep access to the original map
(defn receive-treasure-location
  [{:keys [lat lng] :as treasure-location}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng))

  ;; One would assume that this would put in new coordinates for your ship
  (steer-ship! treasure-location))

(defn number-comment
  [x]
  (if (> x 6)
    "Oh my gosh! What a big number!"
    "That number's OK, I guess"))

(number-comment 5)
; => "That number's OK, I guess"

(number-comment 7)
; => "Oh my gosh! What a big number!"

;Anonymous Functions

(map (fn [name] (str "Hi, " name))
     ["Darth Vader" "Mr. Magoo"])
; => ("Hi, Darth Vader" "Hi, Mr. Magoo")

((fn [x] (* x 3)) 8)
; => 24

; Compact form
#(* % 3)

(#(str %1 " and " %2) "cornbread" "butter beans")

;Rest
(#(identity %&) 1 "blarg" :yip)

;Returning Functions
(defn inc-maker
  "Create a custom incrementor"
  [inc-by]
  #(+ % inc-by))

(def inc3 (inc-maker 3))

(inc3 7)