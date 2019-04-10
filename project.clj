(defproject brave-clojure "0.1.0-SNAPSHOT"
  :description "Learning Clojure with Brave Clojure"
  :url "https://github.com/theodesp/brave-clojure"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :main ^:skip-aot brave-clojure.core
  :repl-options {:init-ns brave-clojure.core})
