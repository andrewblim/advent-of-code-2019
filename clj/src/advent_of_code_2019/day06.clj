(ns advent-of-code-2019.day06)

(require '[clojure.string :as string])
(require '[clojure.set :as set])

;; Question 1

;; No assumption that one object cannot orbit two other objects, so
;; orbits map from satellite to an array of hubs. There is, however,
;; an assumption that there won't be any cycles in order for this code
;; to work. 

(defn add-orbit
  "Add a new orbit to a hash mapping satellites to hubs"
  [orbits hub sat]
  (merge-with conj orbits {sat [hub]}))

(defn count-orbits-from-points
  "Count the number of direct and indirect orbits from a set of points.
  If a point is listed more than once, it will be counted more than once.
  By default counts all orbits."
  ([orbits] (count-orbits-from-points orbits (keys orbits)))
  ([orbits points] (count-orbits-from-points orbits points 0))
  ([orbits points total]
   (let [next-points (flatten (remove nil? (map #(get orbits %) points)))]
     (if (empty? next-points)
       total
       (recur orbits next-points (+ total (count next-points)))))))

(defn read-orbits-file
  "Read a file containing orbits into a hash from satellites to hubs"
  [filename]
  (let [lines (-> filename slurp string/trim (string/split #"\n"))]
    (->> lines
         (map #(string/split % #"\)"))
         (reduce #(add-orbit %1 (first %2) (second %2)) {}))))

(defn answer-part-1
  []
  (-> (read-orbits-file "resources/input/day06.txt")
      (count-orbits-from-points)))

;; (answer-part-1)

;; Question 2

(defn shortest-distance-from-points
  "Compute the shortest distance from a given set of points to any other
  reachable points. Returns a map of reachable points to shortest
  distances. Unreachable points do not show up in the returned map. Note
  that the initial points themselves do not show up as distance 0
  (deliberate, due to the wording of the question)."
  ([orbits points]
   (shortest-distance-from-points orbits points {} 0))
  ([orbits points dists cur-dist]
   (let [next-points (flatten (remove nil? (map #(get orbits %) points)))
         next-unvisited (set/difference (set next-points)
                                        (set (keys dists)))]
     (if (empty? next-unvisited)
       dists
       (recur orbits
              next-unvisited
              (reduce #(assoc %1 %2 (inc cur-dist)) dists next-unvisited)
              (inc cur-dist))))))

(defn shortest-connection
  "Get the shortest connection between what two points are orbiting
   (not the two points themselves)"
  [orbits p1 p2]
  (let [dists1 (shortest-distance-from-points orbits #{p1})
        dists2 (shortest-distance-from-points orbits #{p2})
        dists (merge-with +
                          (select-keys dists1 (keys dists2))
                          (select-keys dists2 (keys dists1)))]
    ; -2 because we want the distance between what they orbit
    (- (apply min (vals dists)) 2)))

(defn answer-part-2
  []
  (-> (read-orbits-file "resources/input/day06.txt")
      (shortest-connection "YOU" "SAN")))

;; (answer-part-2)
