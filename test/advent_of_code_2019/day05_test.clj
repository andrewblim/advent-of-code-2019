(ns advent-of-code-2019.day05-test
  (:require [clojure.test :refer :all]
            [advent-of-code-2019.day05 :refer :all]))

(deftest test-split-opcode
  (testing "Make sure we split opcodes correctly"
    (is (= (split-opcode 2) [2 0 0 0]))
    (is (= (split-opcode 99) [99 0 0 0]))
    (is (= (split-opcode 1002) [2 0 1 0]))
    (is (= (split-opcode 102) [2 1 0 0]))))

(deftest test-get-param
  (testing "Make sure we get params correctly"
    (is (= (get-param [1 2 99] 1 0) 99))
    (is (= (get-param [1 2 99] 1 1) 2))))

(deftest test-process-simple-intcode
  (testing "Make sure processing on a simple test intcode works"
    (is (= (process-intcode [3 0 4 0 104 456 99] '(123))
           {:intcode [123 0 4 0 104 456 99]
            :offset 6
            :input '()
            :output '(123 456)}))))

(deftest test-process-example-intcodes
  (testing "Test the 'input = 8?' and 'input < 8?' intcodes in question 2"
    (let [intcode [3 9 8 9 10 9 4 9 99 -1 8]]
      (is (= (:output (process-intcode intcode '(8))) '(1)))
      (is (= (:output (process-intcode intcode '(7))) '(0)))
      (is (= (:output (process-intcode intcode '(999))) '(0))))
    (let [intcode [3 9 7 9 10 9 4 9 99 -1 8]]
      (is (= (:output (process-intcode intcode '(8))) '(0)))
      (is (= (:output (process-intcode intcode '(7))) '(1)))
      (is (= (:output (process-intcode intcode '(999))) '(0))))
    (let [intcode [3 3 1108 -1 8 3 4 3 99]]
      (is (= (:output (process-intcode intcode '(8))) '(1)))
      (is (= (:output (process-intcode intcode '(7))) '(0)))
      (is (= (:output (process-intcode intcode '(999))) '(0))))
    (let [intcode [3 3 1107 -1 8 3 4 3 99]]
      (is (= (:output (process-intcode intcode '(8))) '(0)))
      (is (= (:output (process-intcode intcode '(7))) '(1)))
      (is (= (:output (process-intcode intcode '(999))) '(0)))))

  (testing "Test the 'input zero/non-zero?' intcodes in question 2"
    (let [intcode [3 12 6 12 15 1 13 14 13 4 13 99 -1 0 1 9]]
      (is (= (:output (process-intcode intcode '(0))) '(0)))
      (is (= (:output (process-intcode intcode '(999))) '(1))))
    (let [intcode [3 3 1105 -1 9 1101 0 0 12 4 12 99 1]]
      (is (= (:output (process-intcode intcode '(0))) '(0)))
      (is (= (:output (process-intcode intcode '(999))) '(1)))))

  (testing "Test the big test intcode in question 2"
    (let [intcode [3 21 1008 21 8 20 1005 20 22 107 8 21 20 1006 20 31
                   1106 0 36 98 0 0 1002 21 125 20 4 20 1105 1 46 104
                   999 1105 1 46 1101 1000 1 20 4 20 1105 1 46 98 99]]
      (is (= (:output (process-intcode intcode '(7))) '(999)))
      (is (= (:output (process-intcode intcode '(8))) '(1000)))
      (is (= (:output (process-intcode intcode '(9))) '(1001))))))

