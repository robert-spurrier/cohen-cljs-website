(ns cohen.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body {:color "black"}]
  [:#app {:height "100%"}]
  [:.level1 {:color "black"}]
  [:.about {:line-height "1.5em"
            :font-size "16px"
            :color "#363636"
            }]
  [:.quote {:font-size "15px"
            :color "#363636"
            :font-style "italic"
            }])
