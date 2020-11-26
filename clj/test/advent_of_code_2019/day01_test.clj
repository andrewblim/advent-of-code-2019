(ns advent-of-code-2019.day01-test
  (:require [clojure.test :refer :all]
            [advent-of-code-2019.day01 :refer :all]))

(deftest fuel-required-test
  (testing "Compute the correct fuel requirements"
    (is (= (fuel-required 12) 2))
    (is (= (fuel-required 14) 2))
    (is (= (fuel-required 1969) 654))
    (is (= (fuel-required 100756) 33583))))

(deftest full-fuel-required-test
  (testing "Compute the correct full fuel requirements"
    (is (= (full-fuel-required 12) 2))
    (is (= (full-fuel-required 14) 2))
    (is (= (full-fuel-required 1969) 966))
    (is (= (full-fuel-required 100756) 50346))))

