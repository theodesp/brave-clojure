;Futures
(future (Thread/sleep 4000)
        (println "I'll print after 4 seconds"))
(println "I'll print immediately")

(let [result (future (println "this prints once")
                     (+ 1 1))]
        (println "deref: " (deref result))
        (println "@: " @result))
; => "this prints once"
; => deref: 2
; => @: 2

(let [result (future (Thread/sleep 3000)
                     (+ 1 1))]
        (println "The result is: " @result)
        (println "It will be at least 3 seconds before I print"))
; => The result is: 2
; => It will be at least 3 seconds before I print

; Wait at most 10ms before unblock
;This code tells deref to return the value 5 if the future doesn’t return a value within 10 milliseconds.

(deref (future (Thread/sleep 1000) 0) 10 5)
; => 5

; Check if resolved
(realized? (future (Thread/sleep 1000)))
; => false

(let [f (future)]
        @f
        (realized? f))
; => true

;Delays

(def jackson-5-delay
        (delay (let [message "Just call my name and I'll be there"]
                       (println "First deref:" message)
                       message)))
; force behaves identically to deref
(force jackson-5-delay)
; => First deref: Just call my name and I'll be there
; => "Just call my name and I'll be there"

@jackson-5-delay
; => "Just call my name and I'll be there"

(def gimli-headshots ["serious.jpg" "fun.jpg" "playful.jpg"])
(defn email-user
        [email-address]
        (println "Sending headshot notification to" email-address))
(defn upload-document
        "Needs to be implemented"
        [headshot]
        true)
(let [notify (delay (email-user "and-my-axe@gmail.com"))]
        (doseq [headshot gimli-headshots]
                (future (upload-document headshot)
                        (force notify))))

;Promises
(def my-promise (promise))
(deliver my-promise (+ 1 2))
@my-promise
; => 3

(defmacro wait
        "Sleep `timeout` seconds before evaluating body"
        [timeout & body]
        `(do (Thread/sleep ~timeout) ~@body))

(defmacro enqueue
           ([q concurrent-promise-name concurrent serialized]
                       `(let [~concurrent-promise-name (promise)]
                                  (future (deliver ~concurrent-promise-name ~concurrent))
                                         (deref ~q)
                                  ~serialized
                                  ~concurrent-promise-name))
           ([concurrent-promise-name concurrent serialized]
                    `(enqueue (future) ~concurrent-promise-name ~concurrent ~serialized)))

(time @(-> (enqueue saying (wait 200 "'Ello, gov'na!") (println @saying))
           (enqueue saying (wait 400 "Pip pip!") (println @saying))
           (enqueue saying (wait 100 "Cheerio!") (println @saying))))
; => 'Ello, gov'na!
; => Pip pip!
; => Cheerio!
; => "Elapsed time: 401.635 msecs"

