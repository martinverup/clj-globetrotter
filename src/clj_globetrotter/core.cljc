(ns clj-globetrotter.core
  (:require #?(:clj [clojure.data.priority-map :refer [priority-map-keyfn]]
               :cljs [tailrecursion.priority-map :refer [priority-map-keyfn]])
            [clojure.string :as str]))

(def ^:private infinity-ish 99999999)

(defn- dijkstra-step [graph heuristic to-visit]
  (let [[current {:keys [path distance]}] (peek to-visit)
        update-neighbours (fn [distances [target route]]
                            (update distances target
                                    (comp first #(sort-by :distance [%1 %2]))
                                    {:distance (+ distance (heuristic route))
                                     :path (conj (or path []) route)}))
        neighbors (filter #(and (= current (:source %))
                                (contains? (set (keys to-visit)) (:target %)))
                          graph)
        new-distances (reduce update-neighbours
                              to-visit
                              (into {} (map #(vector (:target %) %) neighbors)))]
    (dissoc new-distances current)))

(defn shortest-path [graph start end heuristic]
  (->> (-> (into (priority-map-keyfn :distance)
                 (map vector (set (map :source graph)) (repeat {:distance infinity-ish})))
           (assoc start {:distance 0}))
       (iterate (partial dijkstra-step graph heuristic))
       (some (fn [to-visit]
               (when (or (= end (-> to-visit peek first))
                         (every? #(= infinity-ish (:distance %)) (vals to-visit)))
                 (let [end-node (to-visit end)]
                   {:path (:path end-node) :distance (or (:distance end-node) nil)}))))))

(defn pretty-print [{:keys [path distance]}]
  (let [travel-emoji {:plane "✈️"
                      :train "🚆"
                      :ferry "⛴️"
                      :bus "🚌"}
        keyword->name #(->> (str/split (name %) #"-")
                            (map str/capitalize)
                            (str/join " "))]
    (doseq [{:keys [source target time price method]} path]
      (println (keyword->name source)
               (travel-emoji method)
               (keyword->name target)
               ", £:"
               price
               ", H:"
               time)))
  distance)
