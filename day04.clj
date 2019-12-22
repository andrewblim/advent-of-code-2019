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

(defn no-decreasing-digits
  "Does the sequence of digits have any decreasing?"
  [digs]
  (every? (fn [[x y]] (<= x y)) (partition 2 1 digs)))

(defn has-double-digits
  "Does the sequence of digits have any consecutive repeats?"
  [digs]
  (some (fn [[x y]] (= x y)) (partition 2 1 digs)))

(defn number-ok?
  "Is the number OK as per no-decreasing-digits and has-double-digits?"
  [x]
  (let [digs (split-into-digits x)]
    (and (no-decreasing-digits digs)
         (has-double-digits digs))))

(defn answer-question-1
  []
  (->> (range 125730 (inc 579381))
       (filter number-ok?)
       (count)))

;; (answer-question-1)

;; Question2

(defn lengths-of-runs-of-digits
  "Get the lengths of runs of consecutive digits"
  [digs]
  (->> digs
       (partition-by identity)
       (map count)))

(defn has-run-of-length?
  "Does the sequence of digits have at least one with some length (exactly)?"
  [len digs]
  (->> (lengths-of-runs-of-digits digs)
       (some #(= % len))))

(defn number-ok-2?
  "Is the number OK as per revised criteria in question 2?"
  [x]
  (let [digs (split-into-digits x)]
    (and (no-decreasing-digits digs)
         (has-run-of-length? 2 digs))))

(defn answer-question-2
  []
  (->> (range 125730 (inc 579381))
       (filter number-ok-2?)
       (count)))

;; (answer-question-2)
