(ns advent-of-code-2019.day12)

(require '[clojure.math.combinatorics :as combo])

(defn gravity
  "Apply gravity between two moons"
  [[p1 v1] [p2 v2]]
  (let [v-change (map compare p1 p2)
        new-v1 (mapv - v1 v-change)
        new-v2 (mapv + v2 v-change)]
    [[p1 new-v1] [p2 new-v2]]))

(defn velocity
  "Apply velocity to a moon"
  [[p v]]
  [(mapv + p v) v])

(defn gravity-all
  "Apply gravity between many moons"
  [moons]
  (reduce (fn [memo [i1 i2]]
            (let [[m1 m2] (gravity (get memo i1) (get memo i2))]
              (-> memo (assoc i1 m1) (assoc i2 m2))))
          moons
          (combo/combinations (range (count moons)) 2)))

(defn velocity-all
  "Apply velocity over many moons"
  [moons]
  (mapv velocity moons))

(defn update-moons
  "Apply gravity and velocity over many moons a certain number of times
  (default 1)"
  ([moons] (update-moons 1 moons))
  ([n moons]
   (if (<= n 0)
     moons
     (recur (dec n) (-> moons gravity-all velocity-all)))))

(defn energy
  "Compute energy in a moon system"
  [moons]
  (apply + (map (fn [[p v]] (* (apply + (map #(Math/abs %) p))
                               (apply + (map #(Math/abs %) v))))
                moons)))

;; just copying the small input file manually

(def initial-state
  [[[-4 -14 8] [0 0 0]]
   [[1 -8 10] [0 0 0]]
   [[-15 2 1] [0 0 0]]
   [[-17 -17 16] [0 0 0]]])

(defn answer-part-1
  []
  (->> initial-state
       (update-moons 1000)
       energy))

;; (answer-part-1)

(defn steps-to-same
  "Returns the number of steps it takes the moon system to return to its
  original state"
  [moons]
  (first (first (drop-while #(not= (second %) moons)
                            (iterate (fn [[i m]] [(inc i) (update-moons m)])
                                     [1 (update-moons moons)])))))

(defn gcd [a b] (if (zero? b) a (recur b (mod a b))))

(defn lcm [a b] (/ (* a b) (gcd a b)))

(defn steps-to-same-efficient
  "Computes steps-to-same efficiently by doing it separately on each
  coordinate and getting the lcm of the results"
  [moons]
  (let [n-coords (count (first (first moons)))]
    (reduce lcm
            (mapv (fn [i]
                    (steps-to-same (mapv (fn [[p v]]
                                           [[(get p i)] [(get v i)]])
                                         moons)))
                  (range n-coords)))))

(defn answer-part-2
  []
  (steps-to-same-efficient initial-state))

;; (answer-part-2)
