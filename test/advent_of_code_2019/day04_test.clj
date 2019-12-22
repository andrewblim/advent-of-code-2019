(ns advent-of-code-2019.day04-test
  (:require [clojure.test :refer :all]
            [advent-of-code-2019.day04 :refer :all]))

(deftest test-split-digits
  (testing "Make sure that digits split"
    (is (= (split-into-digits 123456)
           '(1 2 3 4 5 6)))))

(deftest test-number-ok?
  (testing "Test the the given numbers work"
    (is (number-ok? 111111))
    (is (not (number-ok? 223450)))
    (is (not (number-ok? 123789)))))
