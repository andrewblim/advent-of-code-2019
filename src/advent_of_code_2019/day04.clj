(ns advent-of-code-2019.day04)

;; Question 1

(defn split-into-digits
  ([x] (split-into-digits x 10))
  ([x base]
   (->> (range (inc (Math/ceil (/ (Math/log x) (Math/log base)))))
        (map #(quot x (int (Math/pow base %))))
        (partition 2 1)
        (map (fn [[x y]] (- x (* base y))))
        (reverse))))

(split-into-digits 1234567)

(defn no-decreasing-digits
  [digs]
  (every? (fn [[x y]] (<= x y)) (partition 2 1 digs)))

(no-decreasing-digits [1 2 1])

(defn has-double-digits
  [digs]
  (some (fn [[x y]] (= x y)) (partition 2 1 digs)))

(defn number-ok?
  [x]
  (let [digs (split-into-digits x)]
    (and (no-decreasing-digits digs)
         (has-double-digits digs))))

(number-ok? 111111)
(number-ok? 223450)
(number-ok? 123789)

(->> (range 125730 (inc 579381))
     (filter number-ok?)
     (count))

