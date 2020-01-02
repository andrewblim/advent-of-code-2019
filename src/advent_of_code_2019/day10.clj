(ns advent-of-code-2019.day10)

(require '[clojure.string :as string])

(defn distance
  "Return the distance between two points"
  [[x1 y1] [x2 y2]]
   (let [a1 (- x2 x1) a2 (- y2 y1)]
     (Math/sqrt (+ (* a1 a1) (* a2 a2)))))

(defn angle
  "Return the angle between two points (nil if they are identical)"
  [[x1 y1] [x2 y2]]
  (let [a1 (- x2 x1) a2 (- y2 y1)]
    (if-not (and (zero? a1) (zero? a2)) (Math/atan2 a2 a1))))

(defn visible-asteroids-from-point
  "Get the count of all visible asteroids from a starting point"
  [point asteroids]
  (-> (group-by #(angle point %) asteroids)
      (dissoc nil) ; don't count point itself if it's an asteroid
      count))

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
        (map #(visible-asteroids-from-point % asteroids))
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
