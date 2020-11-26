(ns advent-of-code-2019.day08)

(require '[clojure.string :as string])

;; Question 1

(defn read-space-image-file
  "Read 'Space Image' input file"
  [filename]
  (mapv #(- (int %) 48) (-> filename slurp clojure.string/trim)))

(defn layerize
  "Partition raw input into layers"
  [rows cols input]
  (partition (* rows cols) input))

(defn answer-part-1
  []
  (let [x (->> (read-space-image-file "resources/input/day08.txt")
               (layerize 6 25)
               (apply min-key #(count (filter zero? %))))]
    (* (count (filter #(= 1 %) x)) (count (filter #(= 2 %) x)))))

;; (answer-part-1)

;; Question 2

(defn combine-image-layers
  "Combine two image layers based on transparency rules"
  [top bottom]
  (map #(if (not= %1 2) %1 %2) top bottom))

(defn resolve-image
  "Resolve the layers of images"
  [layers]
  (reduce combine-image-layers layers))

(defn print-image
  "Print an image"
  [width image]
  (for [row (partition width image)]
    (->> row
         (map #(case % 0 "#" 1 "." 2 " "))
         (string/join)
         (println))))

(defn answer-part-2
  []
  (->> (read-space-image-file "resources/input/day08.txt")
       (layerize 25 6)
       (resolve-image)
       (print-image 25)))

;; (answer-part-2)
