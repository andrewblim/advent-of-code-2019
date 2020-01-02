(ns advent-of-code-2019.day10-test)

(require '[clojure.test :refer :all]
         '[advent-of-code-2019.day10 :refer :all]
         '[clojure.string :as string])

(deftest test-visible-asteroids-from-point
  (testing "can find visible asteroids"
    (is (= (visible-asteroids-from-point [0 0]
                                         [[0 0] [1 1] [2 2] [1 0]])
           2))))

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
