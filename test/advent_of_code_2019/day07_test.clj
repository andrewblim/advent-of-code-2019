(ns advent-of-code-2019.day07-test)

(require '[clojure.test :refer :all])
(require '[advent-of-code-2019.day07 :refer :all])

(def machine1
  [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 0 0])

(def machine2
  [3 23 3 24 1002 24 10 24 1002 23 -1 23 101 5 23 23 1 24 23 23 4 23 99 0 0])

(def machine3
  [3 31 3 32 1002 32 10 32 1001 31 -2 31 1007 31 0 33 1002 33 7 33 1 33 31 31 1 32 31 31 4 31 99 0 0 0])

(deftest test-amplifiers
  (testing "it produces the right output for phase settings"
    (is (= (run-amplifiers machine1 '(4 3 2 1 0))
           '(43210)))
    (is (= (run-amplifiers machine2 '(0 1 2 3 4))
           '(54321)))
    (is (= (run-amplifiers machine3 '(1 0 4 3 2))
           '(65210)))))

(deftest test-max-output
  (testing "it finds the correct max output over possible phase settings"
    (is (= (max-output machine1) 43210))
    (is (= (max-output machine2) 54321))
    (is (= (max-output machine3) 65210))))

