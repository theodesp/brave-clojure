; a random number generator cannot be referentially transparent:
(defn year-end-evaluation
  []
  (if (> (rand) 0.5)
    "You get a raise!"
    "Better luck next year!"))

;If your function reads from a file, it’s not referentially transparent because the file’s contents can change.

(defn analyze-file
  [filename]
  (analysis (slurp filename)))

(defn analysis
  [text]
  (str "Character count: " (count text)))


(def great-baby-name "Rosanthony")
great-baby-name
; => "Rosanthony"

(let [great-baby-name "Bloodthunder"]
  great-baby-name)
; => "Bloodthunder"

great-baby-name
; => "Rosanthony"

(defn sum
  ; arity overloading to provide a default value of 0 for accumulating-total
     ([vals] (sum vals 0))
  ([vals accumulating-total]
          (if (empty? vals)
             accumulating-total
             (sum (rest vals) (+ (first vals) accumulating-total)))))

(sum [39 5 1]) ; single-arity body calls two-arity body
(sum [39 5 1] 0)
(sum [5 1] 39)
(sum [1] 44)
(sum [] 45) ; base case is reached, so return accumulating-total
; => 45

;Note that you should generally use recur when doing recursion for performance reasons.
;The reason is that Clojure doesn’t provide tail call optimization,

(defn sum
  ([vals]
   (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals) (+ (first vals) accumulating-total)))))

; function composition.
(require '[clojure.string :as s])
(defn clean
  [text]
  (s/replace (s/trim text) #"lol" "LOL"))

(clean "My boa constrictor is so sassy lol!  ")
; => "My boa constrictor is so sassy LOL!"

;comp, for creating a new function from the composition of any number of functions
((comp inc *) 2 3)
; => 7

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})
(def c-int (comp :intelligence :attributes))                ;extract :attributes->:intelligence
(def c-str (comp :strength :attributes))                    ;extract :attributes->:strength
(def c-dex (comp :dexterity :attributes))                   ;extract :attributes->:dexterity

(c-int character)
; => 10

(c-str character)
; => 4

(c-dex character)
; => 5

(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2))))

(spell-slots character)
; => 6

;Here’s how you could do the same thing with comp:
(def spell-slots-comp (comp int inc #(/ % 2) c-int))

;memoize
(defn sleepy-identity
  "Returns the given value after 1 second"
  [x]
  (Thread/sleep 1000)
  x)
(sleepy-identity "Mr. Fantastico")
; => "Mr. Fantastico" after 1 second

(sleepy-identity "Mr. Fantastico")
; => "Mr. Fantastico" after 1 second

(def memo-sleepy-identity (memoize sleepy-identity))
(memo-sleepy-identity "Mr. Fantastico")
; => "Mr. Fantastico" after 1 second

(memo-sleepy-identity "Mr. Fantastico")
; => "Mr. Fantastico" immediately