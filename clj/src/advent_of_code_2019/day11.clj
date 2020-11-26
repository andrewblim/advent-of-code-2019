(ns advent-of-code-2019.day11)

(require '[clojure.string :as string])

;; Some copy and paste from day09 with some modifications
;; Note - argument order changed from day09 as that was kind of
;; bugging me

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
  (case mode
    0 (get intcode (get intcode pos 0) 0)
    1 (get intcode pos 0)
    2 (get intcode (+ (get intcode pos 0) rbase) 0)
    (throw (Exception. (str "Unhandled read mode " mode)))))

(defn write-value
  "Write the value into an intcode indicated by pos, dereferencing it
  in position mode (0), or dereferencing it vs. rebase in relative mode
  (2). Returns the revised intcode"
  [intcode pos mode rbase val]
  (case mode
    0 (assoc intcode (get intcode pos 0) val)
    2 (assoc intcode (+ (get intcode pos 0) rbase) val)
    (throw (Exception. (str "Unhandled write mode " mode)))))

(defn advance-intcode-state
  "Given an intcode machine, offset and relative base info, and input
  and output buffers, advances the machine one step, returning a new
  set of args."
  [intcode offset rbase input output]
  (let [[op mode1 mode2 mode3] (split-opcode (get intcode offset))]
    (case op
      1 (let [res (+ (read-value intcode (+ 1 offset) mode1 rbase)
                     (read-value intcode (+ 2 offset) mode2 rbase))]
          [(write-value intcode (+ 3 offset) mode3 rbase res)
           (+ 4 offset)
           rbase
           input
           output])
      2 (let [res (* (read-value intcode (+ 1 offset) mode1 rbase)
                     (read-value intcode (+ 2 offset) mode2 rbase))]
          [(write-value intcode (+ 3 offset) mode3 rbase res)
           (+ 4 offset)
           rbase
           input
           output])
      3 (if (empty? input)
          [intcode offset rbase input output]
          [(write-value intcode (+ 1 offset) mode1 rbase (peek input))
           (+ 2 offset)
           rbase
           (pop input)
           output])
      4 [intcode
         (+ 2 offset)
         rbase
         input
         (conj output (read-value intcode (+ 1 offset) mode1 rbase))]
      5 [intcode
         (if (zero? (read-value intcode (+ 1 offset) mode1 rbase))
           (+ 3 offset)
           (read-value intcode (+ 2 offset) mode2 rbase))
         rbase
         input
         output]
      6 [intcode
         (if (zero? (read-value intcode (+ 1 offset) mode1 rbase))
           (read-value intcode (+ 2 offset) mode2 rbase)
           (+ 3 offset))
         rbase
         input
         output]
      7 [(if (< (read-value intcode (+ 1 offset) mode1 rbase)
                (read-value intcode (+ 2 offset) mode2 rbase))
           (write-value intcode (+ 3 offset) mode3 rbase 1)
           (write-value intcode (+ 3 offset) mode3 rbase 0))
         (+ 4 offset)
         rbase
         input
         output]
       8 [(if (= (read-value intcode (+ 1 offset) mode1 rbase)
                 (read-value intcode (+ 2 offset) mode2 rbase))
            (write-value intcode (+ 3 offset) mode3 rbase 1)
            (write-value intcode (+ 3 offset) mode3 rbase 0))
          (+ 4 offset)
          rbase
          input
          output]
       9 [intcode
          (+ 2 offset)
          (+ rbase (read-value intcode (+ 1 offset) mode1 rbase))
          input
          output]
       99 [intcode offset rbase input output]
       (throw (Exception. (str "Unrecognized op " op))))))

(defn read-robot
  "Reads the panel that the robot is currently on"
  [pos board]
  (get board pos 0))

(defn advance-robot-state
  "Updates the robot/board state with the specified color and move"
  [[[pos-x pos-y] [dir-x dir-y] board] color move]
  (let [new-board (assoc board [pos-x pos-y] color)
        [new-dir-x new-dir-y]
         (case move
               0 [(- dir-y) dir-x]
               1 [dir-y (- dir-x)]
               (throw (Exception. (str "Unrecognized move " move))))
        new-pos-x (+ pos-x new-dir-x)
        new-pos-y (+ pos-y new-dir-y)]
    [[new-pos-x new-pos-y] [new-dir-x new-dir-y] new-board]))

(defn advance-robot
  "Reads the robot's panel, supplies it as input to the intcode and runs
   it, takes the output, updates robot state and empties the output if
   it has length 2, returns revised intcode and robot state"
  [[intcode offset rbase input output]
   [[pos-x pos-y] [dir-x dir-y] board]]
  (let [[new-intcode new-offset new-rbase new-input new-output]
        (advance-intcode-state intcode offset rbase
                               [(read-robot [pos-x pos-y] board)]
                               output)
        new-robot-state
        (if (= (count new-output) 2)
          (advance-robot-state [[pos-x pos-y] [dir-x dir-y] board]
                               (first new-output)
                               (second new-output))
          [[pos-x pos-y] [dir-x dir-y] board])]
    [[new-intcode new-offset new-rbase new-input
      (if (= (count new-output) 2) [] new-output)]
     new-robot-state]))

(defn process-robot
  "Advances the robot until it halts"
  [intcode-state robot-state]
  (let [[new-intcode-state new-robot-state]
        (advance-robot intcode-state robot-state)]
    (if (and (= new-intcode-state intcode-state)
             (= new-robot-state robot-state))
      [intcode-state robot-state]
      (recur new-intcode-state new-robot-state))))

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
  (let [intcode (-> (read-intcode-file "resources/input/day11.txt")
                    (intcode-v-to-h))
        [final-intcode-state final-robot-state]
        (process-robot [intcode 0 0 [] []]
                       [[0 0] [0 1] {}])]
    (count (last final-robot-state))))

;; (answer-part-1)

;; Question 2

(defn str-board
  "Turn a board into a string representation"
  [board]
  (let [ks (keys board)
        min-x (apply min (map first ks))
        max-x (apply max (map first ks))
        min-y (apply min (map second ks))
        max-y (apply max (map second ks))]
    (->> (map (fn [y]
                (->> (map (fn [x]
                            (if (= (get board [x y] 0) 0) "." "#"))
                          (range min-x (inc max-x)))
                     (string/join)))
              (range max-y (dec min-y) -1))
         (string/join "\n"))))

(defn answer-part-2
  []
  (let [intcode (-> (read-intcode-file "resources/input/day11.txt")
                    (intcode-v-to-h))
        [final-intcode-state final-robot-state]
        (process-robot [intcode 0 0 [] []]
                       [[0 0] [0 1] {[0 0] 1}])
        final-board (last final-robot-state)]
    (println (str-board final-board))))

;; (answer-part-2)
