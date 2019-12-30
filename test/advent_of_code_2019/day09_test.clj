(ns advent-of-code-2019.day09-test)

(require '[clojure.test :refer :all]
         '[advent-of-code-2019.day09 :refer :all])

(def quine [109 1 204 -1 1001 100 1 100 1008 100 16 101 1006 101 0 99])

(deftest test-process-single-intcode
  (testing "process-single-intcode processes intcodes correctly"
    (is (= (process-single-intcode (intcode-v-to-h quine) '()) quine))
    (is (= (process-single-intcode (intcode-v-to-h [104 1125899906842624 99]) '())
           '(1125899906842624)))
    (is (= (process-single-intcode (intcode-v-to-h [1102 34915192 34915192 7 4 7 99 0]) '())
           (list (* 34915192 34915192))))))
