(ns potty-time.format.browser
  (:require [hiccup.page :refer [html5 include-css]]
            [clojure.string :as str]))

(def unknown-message "Unknown! Guess you'll have to go check for yourself")

(defn avail
  [state]
  (cond (= state "occupied") (str/upper-case state)
        (= state "free") (str/upper-case state)
        :else unknown-message))

(defn to-page
  [{:strs [first second third]}]
  (html5 {:lang "en"}
         [:body
          [:div
           [:h1 "Bathroom States"]
           [:h3 (str "First Floor Bathroom State: " (avail first))]
           [:h3 (str "Second Floor Bathroom State: " (avail second))]
           [:h3 (str "Third Floor Bathroom State: " (avail third))]]]))
