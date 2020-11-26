(ns advent-of-code-2019.day07-test)

(require '[clojure.test :refer :all])
(require '[advent-of-code-2019.day07 :refer :all])

;; re-testing things from day02/day05 after refactor

(deftest test-advance-intcode-state
  (testing "it advances intcode state properly"
    (is (= (apply advance-intcode-state
                  [[1 0 0 0 99] 0 '() '()])
           [[2 0 0 0 99] 4 '() '()]))))

(deftest test-advance-connected-intcodes
  (testing "it advances connected intcodes' states properly"
    (is (= (advance-connected-intcodes [[[1 0 0 0 99] 0 '() '()]
                                        [[3 0 4 0 99] 0 '(123) '()]])
           [[[2 0 0 0 99] 4 '() '()]
            [[123 0 4 0 99] 2 '() '()]]))))

(deftest test-process-connected-intcodes
  (testing "it runs intcodes to completion and returns the last output"
    (is (= (process-connected-intcodes [[[3 0 4 0 99] 0 '(123) '()]
                                        [[3 0 4 0 99] 0 '() '()]]
                                       {0 1})
           123))
    (is (= (process-connected-intcodes [[[3 0 4 0 99] 0 '(123) '()]
                                        [[3 0 4 0 99] 0 '() '()]]
                                       {})
           nil))))

(def machine1
  [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0])

(def machine2
  [3 23 3 24 1002 24 10 24 1002 23 -1 23 101 5 23 23 1 24 23 23 4 23 99 0 0])

(def machine3
  [3 31 3 32 1002 32 10 32 1001 31 -2 31 1007 31 0 33 1002 33 7 33 1 33 31 31 1 32 31 31 4 31 99 0 0 0])

(deftest test-amplifiers
  (testing "it produces the right output for phase settings"
    (is (= (run-amplifiers machine1 '(4 3 2 1 0) {0 1, 1 2, 2 3, 3 4})
           43210))
    (is (= (run-amplifiers machine2 '(0 1 2 3 4) {0 1, 1 2, 2 3, 3 4})
           54321))
    (is (= (run-amplifiers machine3 '(1 0 4 3 2) {0 1, 1 2, 2 3, 3 4})
           65210))))

(deftest test-max-output-1
  (testing "it finds the correct max output over possible phase settings for question 1"
    (is (= (max-output machine1 (set (range 5)) {0 1, 1 2, 2 3, 3 4})
           43210))
    (is (= (max-output machine2 (set (range 5)) {0 1, 1 2, 2 3, 3 4})
           54321))
    (is (= (max-output machine3 (set (range 5)) {0 1, 1 2, 2 3, 3 4})
           65210))))

(def machine4
  [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26
   27 4 27 1001 28 -1 28 1005 28 6 99 0 0 5])

(def machine5
  [3 52 1001 52 -5 52 3 53 1 52 56 54 1007 54 5 55 1005 55 26 1001 54
   -5 54 1105 1 12 1 53 54 53 1008 54 0 55 1001 55 1 55 2 53 55 53 4
   53 1001 56 -1 56 1005 56 6 99 0 0 0 0 10])

(deftest test-max-output-2
  (testing "it finds the correct max output over possible phase settings for question 2, with recursive hookups"
    (is (= (max-output machine4 (set (range 5 10)) {0 1, 1 2, 2 3, 3 4, 4 0})
           139629729))
    (is (= (max-output machine5 (set (range 5 10)) {0 1, 1 2, 2 3, 3 4, 4 0})
           18216))))

