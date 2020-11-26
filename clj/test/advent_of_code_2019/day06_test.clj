(ns advent-of-code-2019.day06-test)

(require '[clojure.test :refer :all])
(require '[advent-of-code-2019.day06 :refer :all])

(def test-orbits
  {"B" ["COM"]
   "C" ["B"]
   "D" ["C"]
   "E" ["D"]
   "F" ["E"]
   "G" ["B"]
   "H" ["G"]
   "I" ["D"]
   "J" ["E"]
   "K" ["J"]
   "L" ["K"]})

(deftest test-orbit-counts
  (testing "Counts the orbits from a point correctly"
    (is (= (count-orbits-from-points test-orbits ["COM"])
           0))
    (is (= (count-orbits-from-points test-orbits ["L" "F" "I"])
           16))
    (is (= (count-orbits-from-points test-orbits)
           42))))

(deftest test-shortest-distances
  (testing "Gets the shortest distances from a point correctly"
    (is (= (shortest-distance-from-points test-orbits #{"I"})
           {"D" 1 "C" 2 "B" 3 "COM" 4}))
    (is (= (shortest-distance-from-points test-orbits #{"I" "H"})
           {"D" 1 "C" 2 "G" 1 "B" 2 "COM" 3}))
    (is (= (shortest-distance-from-points test-orbits #{"H" "G"})
           {"G" 1 "B" 1 "COM" 2}))))

(deftest test-shortest-connection
  (testing "Gets the shortest connection between two points correctly"
    (is (= (shortest-connection test-orbits "L" "K") 1))
    (is (= (shortest-connection test-orbits "L" "F") 2))
    (is (= (shortest-connection test-orbits "L" "I") 3))
    (is (= (shortest-connection test-orbits "H" "I") 3))))
