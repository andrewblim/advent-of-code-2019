(ns advent-of-code-2019.day02)

;; Question 1

(defn process-intcode
  "Process the operations in an intcode, returning the updated intcode"
  ([intcode] (process-intcode intcode 0))
  ([intcode offset]
   (let [opcode (nth intcode offset nil)
         x1 (nth intcode (+ 1 offset) nil)
         x2 (nth intcode (+ 2 offset) nil)
         target (nth intcode (+ 3 offset) nil)]
     (case opcode
       1 (recur (assoc intcode target (+ (nth intcode x1) (nth intcode x2)))
                (+ 4 offset))
       2 (recur (assoc intcode target (* (nth intcode x1) (nth intcode x2)))
                (+ 4 offset))
       99 intcode
       (throw (Exception. (str "Unrecognized opcode " opcode)))))))

(defn read-intcode-file
  "Read input file of an intcode"
  [filename]
  (mapv read-string
        (-> filename slurp clojure.string/trim (clojure.string/split #","))))

(defn answer-part-1
  []
  (-> "resources/input/day02.txt"
      read-intcode-file
      (assoc 1 12)
      (assoc 2 2)
      process-intcode
      first))

;; (answer-part-1)

;; Question 2

(defn find-intcode-inputs-for-target
  "Find inputs that generate a given target at position 0 in the intcode"
  [filename target]
  (let [intcode (read-intcode-file filename)]
    (for [noun (range 100) verb (range 100)
          :let [result (-> intcode
                           (assoc 1 noun)
                           (assoc 2 verb)
                           process-intcode
                           first)]
          :when (= result target)]
      {:noun noun :verb verb})))

(defn answer-part-2
  []
  (let [{:keys [noun verb]}
        (first (find-intcode-inputs-for-target "resources/input/day02.txt" 19690720))]
    (+ (* 100 noun) verb)))

;; (answer-part-2)
