(ns advent-of-code-2019.day05)

(defn split-opcode
  "Split an opcode into its constitutent parts"
  [x]
  [(-> x (rem 100))
   (-> x (/ 100) (int) (rem 10))
   (-> x (/ 1000) (int) (rem 10))
   (-> x (/ 10000) (int) (rem 10))])

(defn get-param
  "Get the value of the intcode indicated at pos, dereferencing it in
  position mode (0) or just returning it in immediate mode (1)"
  [intcode pos mode]
  (let [x (get intcode pos)]
    (case mode
      0 (get intcode x)
      1 x
      (throw (Exception. (str "Unrecognized mode " mode))))))

(defn process-intcode
  "Process the operations in an intcode, returning the updated intcode.
  Input and output should be supplied as lists"
  ([intcode input] (process-intcode intcode 0 input '()))
  ([intcode offset input output]
   (let [[op mode1 mode2 mode3] (split-opcode (get intcode offset))]
     (case op
       1 (let [res (+ (get-param intcode (+ 1 offset) mode1)
                      (get-param intcode (+ 2 offset) mode2))]
           (recur (assoc intcode (get intcode (+ 3 offset)) res)
                  (+ 4 offset)
                  input
                  output))
       2 (let [res (* (get-param intcode (+ 1 offset) mode1)
                      (get-param intcode (+ 2 offset) mode2))]
           (recur (assoc intcode (get intcode (+ 3 offset)) res)
                  (+ 4 offset)
                  input
                  output))
       3 (recur (assoc intcode (get intcode (+ 1 offset)) (peek input))
                (+ 2 offset)
                (pop input)
                output)
       4 (recur intcode
                (+ 2 offset)
                input
                (cons (get-param intcode (+ 1 offset) mode1) output))
       5 (recur intcode
                (if (zero? (get-param intcode (+ 1 offset) mode1))
                  (+ 3 offset)
                  (get-param intcode (+ 2 offset) mode2))
                input
                output)
       6 (recur intcode
                (if (zero? (get-param intcode (+ 1 offset) mode1))
                  (get-param intcode (+ 2 offset) mode2)
                  (+ 3 offset))
                input
                output)
       7 (recur (if (< (get-param intcode (+ 1 offset) mode1)
                       (get-param intcode (+ 2 offset) mode2))
                  (assoc intcode (get intcode (+ 3 offset)) 1)
                  (assoc intcode (get intcode (+ 3 offset)) 0))
                (+ 4 offset)
                input
                output)
       8 (recur (if (= (get-param intcode (+ 1 offset) mode1)
                       (get-param intcode (+ 2 offset) mode2))
                  (assoc intcode (get intcode (+ 3 offset)) 1)
                  (assoc intcode (get intcode (+ 3 offset)) 0))
                (+ 4 offset)
                input
                output)
       99 {:intcode intcode
           :offset offset
           :input input
           :output (reverse output)}))))

(defn read-intcode-file
  "Read input file of an intcode"
  [filename]
  (mapv read-string
        (-> filename slurp clojure.string/trim (clojure.string/split #","))))

(defn answer-part-1
  []
  (-> "resources/input/day05.txt"
      read-intcode-file
      (process-intcode '(1))
      (:output)))

;; (answer-part-1)

(defn answer-part-2
  []
  (-> "resources/input/day05.txt"
      read-intcode-file
      (process-intcode '(5))
      (:output)))

;; (answer-part-2)

