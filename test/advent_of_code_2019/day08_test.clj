(ns advent-of-code-2019.day08-test)

(require '[clojure.test :refer :all]
         '[advent-of-code-2019.day08 :refer :all])

(deftest test-resolve-image
  (testing "Resolves an image correctly"
    (is (= (resolve-image '((0 2 2 2)
                            (1 1 2 2)
                            (2 2 1 2)
                            (0 0 0 0)))
           '(0 1 1 0)))))
