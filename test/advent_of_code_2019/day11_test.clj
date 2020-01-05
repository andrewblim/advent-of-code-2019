(ns advent-of-code-2019.day11-test)

(require '[clojure.test :refer :all]
         '[advent-of-code-2019.day11 :refer :all])

(deftest test-advance-robot-state
  (testing "advances robot state correctly"
    (is (= (advance-robot-state [[0 0] [0 1] {}] 0 0)
           [[-1 0] [-1 0] {[0 0] 0}]))
    (is (= (advance-robot-state [[0 0] [0 1] {}] 0 1)
           [[1 0] [1 0] {[0 0] 0}]))
    (is (= (advance-robot-state [[0 0] [0 1] {}] 1 0)
           [[-1 0] [-1 0] {[0 0] 1}]))
    (is (= (advance-robot-state [[0 0] [0 1] {}] 1 1)
           [[1 0] [1 0] {[0 0] 1}]))
    (is (= (advance-robot-state [[100 100] [1 0] {}] 0 0)
           [[100 101] [0 1] {[100 100] 0}]))
    (is (= (advance-robot-state [[100 100] [1 0] {}] 0 1)
           [[100 99] [0 -1] {[100 100] 0}]))
    (is (= (advance-robot-state [[100 100] [1 0] {}] 1 0)
           [[100 101] [0 1] {[100 100] 1}]))
    (is (= (advance-robot-state [[100 100] [1 0] {}] 1 1)
           [[100 99] [0 -1] {[100 100] 1}]))))

(deftest test-str-board
  (testing "creates a string representation of a board"
    (is (= (str-board {[0 0] 1 [1 1] 1 [-1 -1] 1 [0 1] 0})
           "..#\n.#.\n#.."))))
