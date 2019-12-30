(ns advent-of-code-2019.day09)

;; Question 1

;; Some copy and paste from day07 with some modifications

(defn split-opcode
  "Split an opcode into its constitutent parts"
  [x]
  [(-> x (rem 100))
   (-> x (/ 100) (int) (rem 10))
   (-> x (/ 1000) (int) (rem 10))
   (-> x (/ 10000) (int) (rem 10))])

(defn read-value
  "Read the value of an intcode indicated by pos, dereferencing it in
  position mode (0), returning the value itself in immediate mode (1), or
  dereferencing it vs. rbase in relative mode (2)"
  [intcode pos mode rbase]
  (let [x (get intcode pos 0)]
    (case mode
      0 (get intcode x 0)
      1 x
      2 (get intcode (+ x rbase) 0)
      (throw (Exception. (str "Unhandled mode " mode))))))

(defn write-value
  "Write the value into an intcode indicated by pos, dereferencing it
  in position mode (0), or dereferencing it vs. rebase in relative mode
  (2). Returns the revised intcode"
  [intcode pos mode rbase val]
  (case mode
    0 (assoc intcode (get intcode pos 0) val)
    2 (assoc intcode (+ (get intcode pos 0) rbase) val)
    (throw (Exception. (str "Unhandled mode " mode)))))

(defn advance-intcode-state
  "Given an intcode machine, a current offset, and input and output
  stacks, advances the machine one step, returning the new intcode,
  offset, input stack, and output stack."
  [intcode offset input output rbase]
  (let [[op mode1 mode2 mode3] (split-opcode (get intcode offset))]
    (case op
      1 (let [res (+ (read-value intcode (+ 1 offset) mode1 rbase)
                     (read-value intcode (+ 2 offset) mode2 rbase))]
          [(write-value intcode (+ 3 offset) mode3 rbase res)
           (+ 4 offset)
           input
           output
           rbase])
      2 (let [res (* (read-value intcode (+ 1 offset) mode1 rbase)
                     (read-value intcode (+ 2 offset) mode2 rbase))]
          [(write-value intcode (+ 3 offset) mode3 rbase res)
           (+ 4 offset)
           input
           output
           rbase])
      3 (if (empty? input)
          [intcode offset input output rbase]
          [(write-value intcode (+ 1 offset) mode1 rbase (peek input))
           (+ 2 offset)
           (pop input)
           output
           rbase])
      4 [intcode
         (+ 2 offset)
         input
         (conj output (read-value intcode (+ 1 offset) mode1 rbase))
         rbase]
      5 [intcode
         (if (zero? (read-value intcode (+ 1 offset) mode1 rbase))
           (+ 3 offset)
           (read-value intcode (+ 2 offset) mode2 rbase))
         input
         output
         rbase]
      6 [intcode
         (if (zero? (read-value intcode (+ 1 offset) mode1 rbase))
           (read-value intcode (+ 2 offset) mode2 rbase)
           (+ 3 offset))
         input
         output
         rbase]
      7 [(if (< (read-value intcode (+ 1 offset) mode1 rbase)
                (read-value intcode (+ 2 offset) mode2 rbase))
           (write-value intcode (+ 3 offset) mode3 rbase 1)
           (write-value intcode (+ 3 offset) mode3 rbase 0))
         (+ 4 offset)
         input
         output
         rbase]
       8 [(if (= (read-value intcode (+ 1 offset) mode1 rbase)
                 (read-value intcode (+ 2 offset) mode2 rbase))
            (write-value intcode (+ 3 offset) mode3 rbase 1)
            (write-value intcode (+ 3 offset) mode3 rbase 0))
          (+ 4 offset)
          input
          output
          rbase]
       9 [intcode
          (+ 2 offset)
          input
          output
          (+ rbase (read-value intcode (+ 1 offset) mode1 rbase))]
       99 [intcode offset input output rbase]
       (throw (Exception. (str "Unrecognized op " op))))))

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

(defn process-connected-intcodes
  "Recurrently advance through several intcode states until the machine
  states do not change. Get the output of the last machine"
  ([states] (process-connected-intcodes states {}))
  ([states connections]
   (let [new-states (advance-connected-intcodes states connections)]
     (if (= states new-states)
       ;; Get the last machine's output, which may have been sent
       ;; to some other machine's input
       (if-let [i (connections (dec (count states)))]
         (reverse (get (nth states i) 2))
         (reverse (get (last states) 3)))
       (recur new-states connections)))))

(defn process-single-intcode
  "Process a single intcode with reasonable defaults"
  [intcode input]
  (process-connected-intcodes [[intcode 0 input '() 0]]))

(defn intcode-v-to-h
  "Convert a vector intcode into a sorted-map intcode, sequencing memory
  locations from 0 on up. The map format is needed so we can reference
  memory locations much higher than the size of the intcode (which
  default to 0). Sorting is not necessary but helped debugging."
  [intcode]
  (into (sorted-map) (zipmap (range (count intcode)) intcode)))

(defn read-intcode-file
  "Read input file of an intcode"
  [filename]
  (mapv read-string
        (-> filename slurp clojure.string/trim (clojure.string/split #","))))

(defn answer-part-1
  []
  (-> (read-intcode-file "resources/input/day09.txt")
      (intcode-v-to-h)
      (process-single-intcode '(1))))

;; (answer-part-1)

