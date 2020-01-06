(ns advent-of-code-2019.day12-test)

(require '[clojure.test :refer :all]
         '[advent-of-code-2019.day12 :refer :all])

(def moons1 [[[-1 0 2] [0 0 0]]
             [[2 -10 -7] [0 0 0]]
             [[4 -8 8] [0 0 0]]
             [[3 5 -1] [0 0 0]]])

(def moons2 [[[-8 -10 0] [0 0 0]]
             [[5 5 10] [0 0 0]]
             [[2 -7 3] [0 0 0]]
             [[9 -8 -3] [0 0 0]]])

(deftest test-update-moons
  (testing "moon systems update"
    (is (= (update-moons 0 moons1) moons1))
    (is (= (update-moons moons1)
           [[[2 -1 1] [3 -1 -1]]
            [[3 -7 -4] [1 3 3]]
            [[1 -7 5] [-3 1 -3]]
            [[2 2 0] [-1 -3 1]]]))
    (is (= (update-moons 10 moons1)
           [[[2 1 -3] [-3 -2 1]]
            [[1 -8 0] [-1 1 3]]
            [[3 -6 1] [3 2 -3]]
            [[2 0 4] [1 -1 -1]]]))
    (is (= (update-moons 2772 moons1) moons1))
    (is (= (update-moons 100 moons2)
           [[[8 -12 -9] [-7 3 0]]
            [[13 16 -3] [3 -11 -5]]
            [[-29 -11 -1] [-3 7 4]]
            [[16 -13 23] [7 1 1]]]))))

(deftest test-energy
  (testing "computes energy of moons"
    (is (= (energy (update-moons 10 moons1)) 179))
    (is (= (energy (update-moons 100 moons2)) 1940))))

(deftest test-steps-to-same
  (testing "computes steps-to-same"
    (is (= (steps-to-same moons1) 2772)))
  (testing "computes steps-to-same efficiently"
    (is (= (steps-to-same-efficient moons1) 2772))
    (is (= (steps-to-same-efficient moons2) 4686774924))))
