(ns clj-globetrotter.web
  (:require react-dom
            [clojure.string :as str]
            [clj-globetrotter.core :as core]
            [clj-globetrotter.world :refer [world]]))

(defonce travel-emoji
  {:plane "✈️"
   :train "🚆"
   :ferry "⛴️"
   :bus "🚌"})

(defonce cities
  (apply sorted-set (map :source world)))

(defn keyword->name [s]
  (->> (str/split (name s) #"-")
       (map str/capitalize)
       (str/join " ")))

(.render js/ReactDOM
         (.createElement js/React "h2" nil "Hello, React!")
         (.getElementById js/document "app"))
