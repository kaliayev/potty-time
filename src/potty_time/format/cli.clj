(ns potty-time.format.cli
  (:require [clojure.string :as str]
            [io.aviso.ansi :as colour]))

(def unknown-message (str (colour/bold-yellow (str/upper-case "Unknown")) " - Guess you'll have to walk all the way there to know."))

(def horizontal-line
  (apply str (repeat 80 "-")))

(defn cap [text]
  (let [str-l (- (count text) 3)]
    (str "+" (apply str (repeat str-l "-")) "+\n")))

(defn header-frame
  [s]
  (let [source-title (str "| " (str/upper-case s) " |\n") 
        formatted-title (str "| " (colour/bold-magenta (str/upper-case s)) " |\n") 
        cap (cap source-title)]
    (str "\n" cap formatted-title cap)))

(defn avail
  [state]
  (cond (= state "occupied") (colour/bold-red (str/upper-case state))
        (= state "free") (colour/bold-cyan (str/upper-case state))
        :else unknown-message))

(defn to-curl
  [{:strs [first second third]}]
  (let [header (header-frame "Bathroom Availability")]
    (str header
         horizontal-line
         "\n\nFirst Floor: " (avail first)
         "\n\nSecond Floor: " (avail second)
         "\n\nThird Floor: " (avail third) "\n\n"
         horizontal-line)))
