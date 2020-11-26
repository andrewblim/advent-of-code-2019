(ns advent-of-code-2019.day02-test
  (:require [clojure.test :refer :all]
            [advent-of-code-2019.day02 :refer :all]))

(deftest process-intcode-test
  (testing "Update the intcode correctly"
    (is (= (process-intcode [1 0 0 0 99])
           [2 0 0 0 99]))
    (is (= (process-intcode [2 3 0 3 99])
           [2 3 0 6 99]))
    (is (= (process-intcode [2 4 4 5 99 0])
           [2 4 4 5 99 9801]))
    (is (= (process-intcode [1 1 1 4 99 5 6 0 99])
           [30 1 1 4 2 5 6 0 99]))))

