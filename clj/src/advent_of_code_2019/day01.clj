(ns advent-of-code-2019.day01)

;; Question 1

(defn fuel-required
  "Compute fuel required for a given mass"
  [mass]
  (-> mass (/ 3) Math/floor int (- 2) (max 0)))

(defn read-mass-input-file
  "Read input file of module masses, 1 per line"
  [filename]
  (mapv read-string
        (-> filename slurp clojure.string/trim (clojure.string/split #"\n"))))

(defn answer-part-1
  []
  (reduce + (mapv fuel-required
                  (read-mass-input-file "resources/input/day01.txt"))))

;; (answer-part-1)

;; Question 2

(defn full-fuel-required
  "Compute fuel required for a given mass and, recursively, for the fuel."
  [mass]
  (loop [cur-mass mass
         total-fuel 0]
    (if (<= cur-mass 0)
      total-fuel
      (let [addl-fuel (fuel-required cur-mass)]
        (recur addl-fuel (+ total-fuel addl-fuel))))))

(defn answer-part-2
  []
  (reduce + (mapv full-fuel-required
                  (read-mass-input-file "resources/input/day01.txt"))))

;; (answer-part-2)
