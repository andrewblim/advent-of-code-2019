(ns advent-of-code-2019.day07)

(require '[clojure.set :as set])

;; Question 1

;; Some copy and paste from day05

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

(defn advance-intcode-state
  "Given an intcode machine, a current offset, and input and output
  stacks, advances the machine one step, returning the new intcode,
  offset, input stack, and output stack."
  [intcode offset input output]
  (let [[op mode1 mode2 mode3] (split-opcode (get intcode offset))]
    (case op
      1 (let [res (+ (get-param intcode (+ 1 offset) mode1)
                     (get-param intcode (+ 2 offset) mode2))]
          [(assoc intcode (get intcode (+ 3 offset)) res)
           (+ 4 offset)
           input
           output])
      2 (let [res (* (get-param intcode (+ 1 offset) mode1)
                     (get-param intcode (+ 2 offset) mode2))]
          [(assoc intcode (get intcode (+ 3 offset)) res)
           (+ 4 offset)
           input
           output])
      3 (if (empty? input)
          [intcode offset input output]
          [(assoc intcode (get intcode (+ 1 offset)) (peek input))
           (+ 2 offset)
           (pop input)
           output])
      4 [intcode
         (+ 2 offset)
         input
         (conj output (get-param intcode (+ 1 offset) mode1))]
      5 [intcode
         (if (zero? (get-param intcode (+ 1 offset) mode1))
           (+ 3 offset)
           (get-param intcode (+ 2 offset) mode2))
         input
         output]
      6 [intcode
         (if (zero? (get-param intcode (+ 1 offset) mode1))
           (get-param intcode (+ 2 offset) mode2)
           (+ 3 offset))
         input
         output]
      7 [(if (< (get-param intcode (+ 1 offset) mode1)
                (get-param intcode (+ 2 offset) mode2))
           (assoc intcode (get intcode (+ 3 offset)) 1)
           (assoc intcode (get intcode (+ 3 offset)) 0))
         (+ 4 offset)
         input
         output]
       8 [(if (= (get-param intcode (+ 1 offset) mode1)
                 (get-param intcode (+ 2 offset) mode2))
            (assoc intcode (get intcode (+ 3 offset)) 1)
            (assoc intcode (get intcode (+ 3 offset)) 0))
          (+ 4 offset)
          input
          output]
       99 [intcode offset input output])))

(defn advance-connected-intcodes
  "Advance several intcode states in parallel, and then transfer the
  output of each machine into the input of others as indicated."
  ([states] (advance-connected-intcodes states {}))
  ([states connections]
   (let [computed-states (mapv #(apply advance-intcode-state %) states)]
     (reduce (fn [sts [out-pos in-pos]]
               (-> sts
                   (update-in [in-pos 2]
                              #(into (get (nth sts out-pos) 3) (reverse %)))
                   (assoc-in [out-pos 3] '())))
             computed-states
             connections))))

;; recurrently advance until the states stop changing

(defn process-connected-intcodes
  ([states] (process-connected-intcodes states {}))
  ([states connections]
   (let [new-states (advance-connected-intcodes states connections)]
     (if (= states new-states)
       ;; Get the last machine's output, which may have been sent
       ;; to some other machine's input
       (if-let [i (connections (dec (count states)))]
         (last (get (nth states i) 2))
         (last (get (last states) 3)))
       (recur new-states connections)))))

(defn run-amplifiers
  [intcode phases connections]
  (process-connected-intcodes
   [[intcode 0 (list (nth phases 0) 0) '()]
    [intcode 0 (list (nth phases 1)) '()]
    [intcode 0 (list (nth phases 2)) '()]
    [intcode 0 (list (nth phases 3)) '()]
    [intcode 0 (list (nth phases 4)) '()]]
   connections))

(defn max-output
  "Find the max possible output for an intcode program"
  [intcode possible-phases connections]
  (->> (for [x1 possible-phases
             x2 (set/difference possible-phases #{x1})
             x3 (set/difference possible-phases #{x1 x2})
             x4 (set/difference possible-phases #{x1 x2 x3})
             x5 (set/difference possible-phases #{x1 x2 x3 x4})]
         (list x1 x2 x3 x4 x5))
       (map #(run-amplifiers intcode % connections))
       (apply max)))

(defn read-intcode-file
  "Read input file of an intcode"
  [filename]
  (mapv read-string
        (-> filename slurp clojure.string/trim (clojure.string/split #","))))

(defn answer-part-1
  []
  (-> (read-intcode-file "resources/input/day07.txt")
      (max-output (set (range 5)) {0 1, 1 2, 2 3, 3 4})))

;; (answer-part-1)

;; Question 2

(defn answer-part-2
  []
  (-> (read-intcode-file "resources/input/day07.txt")
      (max-output (set (range 5 10)) {0 1, 1 2, 2 3, 3 4, 4 0})))

(answer-part-2)
