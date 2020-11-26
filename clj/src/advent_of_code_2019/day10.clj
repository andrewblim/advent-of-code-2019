(ns advent-of-code-2019.day10)

(require '[clojure.string :as string]
         '[medley.core])

;; Question 1

(defn distance
  "Return the squared distance between two points"
  [[x1 y1] [x2 y2]]
  (let [a1 (- x2 x1) a2 (- y2 y1)]
    (+ (* a1 a1) (* a2 a2))))

(defn angle
  "Return the angle between two points (nil if they are identical)"
  [[x1 y1] [x2 y2]]
  (let [a1 (- x2 x1) a2 (- y2 y1)]
    (if-not (and (zero? a1) (zero? a2)) (Math/atan2 a2 a1))))

(defn asteroid-angles-from-point
  "Group asteroids by their angle from a given point"
  [point asteroids]
  (reduce (fn [m [k v]] (assoc m k (sort-by #(distance % point) v)))
          {}
          (-> (group-by #(angle point %) asteroids)
              (dissoc nil))))

(defn asteroid-map-to-coords
  "Parse an asteroid map into a set of coordinates"
  [asteroid-map]
  (vec
   (for [[line y] (map list (string/split asteroid-map #"\n") (range))
         [ch x] (map list line (range))
         :when (= ch \#)]
     [x y])))

(defn visible-asteroids
  "Get a map of the count of visible asteroids at several points. By
  default the asteroids themselves will serve as the points from which
  we compute visibility."
  ([asteroids] (visible-asteroids asteroids asteroids))
  ([points asteroids]
   (->> points
        (map #(count (asteroid-angles-from-point % asteroids)))
        (zipmap points)
        (into (sorted-map)))))

(defn answer-part-1
  []
  (->> (slurp "resources/input/day10.txt")
       string/trim
       asteroid-map-to-coords
       visible-asteroids
       (apply max-key val)
       second))

;; (answer-part-1)

;; Question 2

(defn rotate-angle
  "Rotate angle back by the specified amount (assuming rotation < 2pi)"
  [angle rotation]
  (let [new-angle (- angle rotation)]
    (if (< new-angle (- Math/PI))
      (+ new-angle (* 2 Math/PI))
      new-angle)))

(defn sort-asteroid-angles
  "Rekeys and sorts asteroid angle map with the 'upwards' angle first
  (a vector [0 -1], since y increases going downward) and proceeding
  counterclockwise."
  [angle-map]
  (into (sorted-map-by >)
        (map (fn [[k v]]
               [(- (rotate-angle k (/ Math/PI 2))) v])
             angle-map)))

(defn asteroids-by-destruction-from-point
  "Given a point and asteroids, return the asteroids that will be
  destroyed in order, starting 'upwards' and sweeping clockwise.
  Will not destroy the point itself if it is an asteroid."
  [point asteroids]
  (->> (asteroid-angles-from-point point asteroids)
       sort-asteroid-angles
       vals
       (apply medley.core/interleave-all)))

(defn answer-part-2
  []
  (let [asteroids (->> (slurp "resources/input/day10.txt")
                        string/trim
                        asteroid-map-to-coords)
        start (->> asteroids
                   visible-asteroids
                   (apply max-key val)
                   first)
        [x y] (->> (asteroids-by-destruction-from-point start asteroids)
                   (drop 199)
                   (first))]
    (+ (* 100 x) y)))

;; (answer-part-2)
