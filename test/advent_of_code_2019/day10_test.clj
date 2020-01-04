(ns advent-of-code-2019.day10-test)

(require '[clojure.test :refer :all]
         '[advent-of-code-2019.day10 :refer :all]
         '[clojure.string :as string])

(deftest test-asteroid-angles-from-point
  (testing "computes asteroid angles, sorted by distance"
    (is (= (asteroid-angles-from-point [0 0] [[0 0] [2 2] [1 1] [1 0]])
           {(Math/atan2 1 1) [[1 1] [2 2]]
            (Math/atan2 0 1) [[1 0]]}))))

(deftest test-asteroid-map-to-coords
  (testing "can convert a string to asteroids"
    (is (= (asteroid-map-to-coords "##..\n..##")
           [[0 0] [1 0] [2 1] [3 1]]))))

(def map1 (string/trim "
.#..#
.....
#####
....#
...##"))

(def map2 (string/trim "
......#.#.
#..#.#....
..#######.
.#.#.###..
.#..#.....
..#....#.#
#..#....#.
.##.#..###
##...#..#.
.#....####"))

(def map3 (string/trim "
#.#...#.#.
.###....#.
.#....#...
##.#.#.#.#
....#.#.#.
.##..###.#
..#...##..
..##....##
......#...
.####.###."))

(def map4 (string/trim "
.#..#..###
####.###.#
....###.#.
..###.##.#
##.##.#.#.
....###..#
..#.#..#.#
#..#.#.###
.##...##.#
.....#.#.."))

(def map5 (string/trim "
.#..##.###...#######
##.############..##.
.#.######.########.#
.###.#######.####.#.
#####.##.#.##.###.##
..#####..#.#########
####################
#.####....###.#.#.##
##.#################
#####.##.###..####..
..######..##.#######
####.##.####...##..#
.#####..#.######.###
##...#.##########...
#.##########.#######
.####.#.###.###.#.##
....##.##.###..#####
.#.#.###########.###
#.#.#.#####.####.###
###.##.####.##.#..##"))

(deftest testing-best-asteroid
  (testing "finds the best asteroid"
    (is (= (->> map1
                asteroid-map-to-coords
                visible-asteroids
                (apply max-key val))
           [[3 4] 8]))
    (is (= (->> map2
                asteroid-map-to-coords
                visible-asteroids
                (apply max-key val))
           [[5 8] 33]))
    (is (= (->> map3
                asteroid-map-to-coords
                visible-asteroids
                (apply max-key val))
           [[1 2] 35]))
    (is (= (->> map4
                asteroid-map-to-coords
                visible-asteroids
                (apply max-key val))
           [[6 3] 41]))
    (is (= (->> map5
                asteroid-map-to-coords
                visible-asteroids
                (apply max-key val))
           [[11 13] 210]))))


(deftest test-asteroid-destruction
  (testing "computes the correct asteroids to destroy"
    (let [test-order
          (vec (asteroids-by-destruction-from-point
                [1 1]
                [[0 0] [1 0] [2 0]
                 [0 1] [1 1] [2 1]
                 [0 2] [1 2] [2 2]
                 [0 3] [1 3] [2 3] [3 3]]))]
      (is (= test-order
             [[1 0] [2 0] [2 1] [2 2] [2 3] [1 2] [0 3] [0 2] [0 1] [0 0]
              [3 3] [1 3]])))
    (let [map5-order
          (vec (asteroids-by-destruction-from-point
                [11 13]
                (asteroid-map-to-coords map5)))]
      (is (= (take 3 map5-order)
             [[11 12] [12 1] [12 2]]))
      (is (= (first (drop 49 map5-order))
             [16 9]))
      (is (= (first (drop 199 map5-order))
             [8 2]))
      (is (= (first (drop 298 map5-order))
             [11 1]))
      (is (= (first (drop 299 map5-order))
             nil)))))
