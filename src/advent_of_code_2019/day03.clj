(ns advent-of-code-2019.day03)

(require '[clojure.set :as set])
(require '[clojure.string :as string])

;; Question 1

;; Define a point as [x y dist], where x and y are the x-coordinate and
;; y-coordinate and dist is a measure of cumulative distance traveled,
;; which will be used in question 2.

(defn points-visited-on-move
  "Return the points visited on a move from start (excluding start itself)"
  [[x y dist] move]
  (let [move-dir (first move)
        move-amount (Integer. (subs move 1))]
    (case move-dir
      \U (map #(vector x (+ y %) (+ dist %)) (range 1 (inc move-amount)))
      \D (map #(vector x (- y %) (+ dist %)) (range 1 (inc move-amount)))
      \L (map #(vector (- x %) y (+ dist %)) (range 1 (inc move-amount)))
      \R (map #(vector (+ x %) y (+ dist %)) (range 1 (inc move-amount)))
      (throw (Exception. (str "Unrecognized direction " move-dir))))))

(defn points-visited-on-wire
  "Returns the points visited on a wire from (and excluding) a start"
  [[x y dist] wire]
  (subvec (reduce #(into %1 (points-visited-on-move (last %1) %2))
                  [[x y dist]]
                  wire)
          1))

(defn wire-intersections
  "Returns a map whose keys are [x y] points of intersection of the two wires
  and whose values are the two min distances traveled along the wire to that
  point"
  [[x y] wire1 wire2]
  (let [pts1 (points-visited-on-wire [x y 0] wire1)
        pts2 (points-visited-on-wire [x y 0] wire2)
        ptsmap1 (reduce #(merge-with min %1 %2) {}
                        (map (fn [[x y d]] {[x y] d}) pts1))
        ptsmap2 (reduce #(merge-with min %1 %2) {}
                        (map (fn [[x y d]] {[x y] d}) pts2))]
    (->> (merge-with vector ptsmap1 ptsmap2)
         (filter #(vector? (second %)))
         (into {})
         )))

(wire-intersections
 [0 0]
 ["R8" "U5" "L5" "D3"]
 ["U7" "R6" "D4" "L4"])

(defn manhattan
  "Returns Manhattan distance between two [x y] points"
  [[x1 y1] [x2 y2]]
  (+ (Math/abs (- x1 x2)) (Math/abs (- y1 y2))))

(defn manhattan-of-closest-wire-intersection
  "Returns the Manhattan distance of the closest wire intersection"
  [[x y] wire1 wire2]
  (let [isects (wire-intersections [x y] wire1 wire2)]
    (when (not (empty? isects))
      (apply min (map #(manhattan % [0 0]) (keys isects))))))

(defn read-wire-file
  "Read file with a pair of wires"
  [filename]
  (let [lines (-> filename slurp string/trim (string/split #"\n"))]
    [(string/split (first lines) #",")
     (string/split (second lines) #",")]))

(defn answer-part-1
  []
  (let [[wire1 wire2] (read-wire-file "resources/input/day03.txt")]
    (manhattan-of-closest-wire-intersection [0 0] wire1 wire2)))

;; (answer-part-1)

;; Question 2

(defn distance-traveled-of-closest-wire-intersection
  "Returns the combined distance traveled of the nearest (by wire travel)
  wire intersection"
  [[x y] wire1 wire2]
  (let [isects (wire-intersections [x y] wire1 wire2)]
    (when (not (empty? isects))
      (apply min (map #(apply + %) (vals isects))))))

(defn answer-part-2
  []
  (let [[wire1 wire2] (read-wire-file "resources/input/day03.txt")]
    (distance-traveled-of-closest-wire-intersection [0 0] wire1 wire2)))

