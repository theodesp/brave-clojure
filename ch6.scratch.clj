;namespaces are objects of type clojure.lang.Namespace,
;refer to the current namespace with *ns*, and you can get its name with (ns-name *ns*):

(ns-name *ns*)
;=> brave-clojure.core

inc
; => #<core$inc clojure.core$inc@30132014>

'inc
; => inc

(map inc [1 2])
; => (2 3)

'(map inc [1 2])
; => (map inc [1 2])

;Storing Objects with def

(def great-books ["East of Eden" "The Glass Bead Game"])
; => #'user/great-books

great-books
; => ["East of Eden" "The Glass Bead Game"]

(ns-interns *ns*)
; => {great-books #'user/great-books}

;You can use the get function to get a specific var:

(get (ns-interns *ns*) 'great-books)
; => #'user/great-books

(deref #'user/great-books)
; => ["East of Eden" "The Glass Bead Game"]

;normally, you would just use the symbol:

great-books
; => ["East of Eden" "The Glass Bead Game"]

;Creating and Switching to Namespaces

(create-ns 'cheese.taxonomy)
; => #<Namespace cheese.taxonomy>

(ns-name (create-ns 'cheese.taxonomy))
; => cheese-taxonomy

; creates the namespace if it doesn’t exist and switches to it

(in-ns 'cheese.analysis)
; => #<Namespace cheese.analysis>

cheese.analysis=> (in-ns 'cheese.taxonomy)
cheese.taxonomy=> (def cheddars ["mild" "medium" "strong" "sharp" "extra sharp"])
cheese.taxonomy=> (in-ns 'cheese.analysis)

cheese.analysis=> cheddars
; => Exception: Unable to resolve symbol: cheddars in this context

cheese.analysis=> cheese.taxonomy/cheddars
; => ["mild" "medium" "strong" "sharp" "extra sharp"]

;refer
user=> (in-ns 'cheese.taxonomy)
cheese.taxonomy=> (def cheddars ["mild" "medium" "strong" "sharp" "extra sharp"])
cheese.taxonomy=> (def bries ["Wisconsin" "Somerset" "Brie de Meaux" "Brie de Melun"])
cheese.taxonomy=> (in-ns 'cheese.analysis)
cheese.analysis=> (clojure.core/refer 'cheese.taxonomy)
cheese.analysis=> bries
; => ["Wisconsin" "Somerset" "Brie de Meaux" "Brie de Melun"]

cheese.analysis=> cheddars
; => ["mild" "medium" "strong" "sharp" "extra sharp"]

cheese.analysis=> (clojure.core/get (clojure.core/ns-map clojure.core/*ns*) 'bries)
; => #'cheese.taxonomy/bries

cheese.analysis=> (clojure.core/get (clojure.core/ns-map clojure.core/*ns*) 'cheddars)
; => #'cheese.taxonomy/cheddars

cheese.analysis=> (clojure.core/refer 'cheese.taxonomy :only ['bries])
cheese.analysis=> bries
; => ["Wisconsin" "Somerset" "Brie de Meaux" "Brie de Melun"]
cheese.analysis=> cheddars
; => RuntimeException: Unable to resolve symbol: cheddars

cheese.analysis=> (clojure.core/refer 'cheese.taxonomy :exclude ['bries])
cheese.analysis=> bries
; => RuntimeException: Unable to resolve symbol: bries
cheese.analysis=> cheddars
; => ["mild" "medium" "strong" "sharp" "extra sharp"]

cheese.analysis=> (clojure.core/refer 'cheese.taxonomy :rename {'bries 'yummy-bries})
cheese.analysis=> bries
; => RuntimeException: Unable to resolve symbol: bries
cheese.analysis=> yummy-bries
; => ["Wisconsin" "Somerset" "Brie de Meaux" "Brie de Melun"]

;private functions

(in-ns 'cheese.analysis)
;; Notice the dash after "defn"
(defn- private-function
       "Just an example function that does nothing"
       [])

cheese.analysis=> (in-ns 'cheese.taxonomy)
cheese.taxonomy=> (clojure.core/refer-clojure)
➊ cheese.taxonomy=> (cheese.analysis/private-function)
➋ cheese.taxonomy=> (refer 'cheese.analysis :only ['private-function])

;alias
;let you shorten a namespace name for using fully qualified symbols

cheese.analysis=> (clojure.core/alias 'taxonomy 'cheese.taxonomy)
cheese.analysis=> taxonomy/bries
; => ["Wisconsin" "Somerset" "Brie de Meaux" "Brie de Melun"]

;Real Project Organization

(ns the-divine-cheese-code.visualization.svg)

(defn latlng->point
      "Convert lat/lng map to comma-separated string"
      [latlng]
      (str (:lat latlng) "," (:lng latlng)))

(defn points
      [locations]
      (clojure.string/join " " (map latlng->point locations)))

(ns the-divine-cheese-code.core)
;; Ensure that the SVG code is evaluated
(require 'the-divine-cheese-code.visualization.svg)
;; Refer the namespace so that you don't have to use the
;; fully qualified name to reference svg functions
(refer 'the-divine-cheese-code.visualization.svg)

(def heists [{:location "Cologne, Germany"
              :cheese-name "Archbishop Hildebold's Cheese Pretzel"
              :lat 50.95
              :lng 6.97}
             {:location "Zurich, Switzerland"
              :cheese-name "The Standard Emmental"
              :lat 47.37
              :lng 8.55}
             {:location "Marseille, France"
              :cheese-name "Le Fromage de Cosquer"
              :lat 43.30
              :lng 5.37}
             {:location "Zurich, Switzerland"
              :cheese-name "The Lesser Emmental"
              :lat 47.37
              :lng 8.55}
             {:location "Vatican City"
              :cheese-name "The Cheese of Turin"
              :lat 41.90
              :lng 12.45}])

(defn -main
      [& args]
      (println (points heists)))

(require '[the-divine-cheese-code.visualization.svg :as svg])
; is equivalent to this:
(require 'the-divine-cheese-code.visualization.svg)
(alias 'svg 'the-divine-cheese-code.visualization.svg)
; or this
(use 'the-divine-cheese-code.visualization.svg)

(use '[the-divine-cheese-code.visualization.svg :as svg])
(= svg/points points)
; => true

(= svg/latlng->point latlng->point)
; => true

;The takeaway here is that require and use load files and optionally
; alias or refer their namespaces.

(ns the-divine-cheese-code.core
    (:refer-clojure :exclude [println]))

(ns the-divine-cheese-code.core
    (:require the-divine-cheese-code.visualization.svg))
;is equivalent to this:

(in-ns 'the-divine-cheese-code.core)
(require 'the-divine-cheese-code.visualization.svg)

(ns the-divine-cheese-code.core
    (:require [the-divine-cheese-code.visualization.svg :as svg]
      [clojure.java.browse :as browse]))

;You can also refer all symbols (notice the :all keyword):
(ns the-divine-cheese-code.core
    (:require [the-divine-cheese-code.visualization.svg :refer :all]))

(zipmap [:a :b] [1 2])
; => {:a 1 :b 2}

(merge-with - {:lat 50 :lng 10} {:lat 5 :lng 5})
; => {:lat 45 :lng 5}



