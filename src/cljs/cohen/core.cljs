(ns cohen.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [cohen.handlers]
              [cohen.subs]
              [cohen.routes :as routes]
              [cohen.views :as views]
              [cohen.config :as config]))

(when config/debug?
  (println "dev mode"))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn create-context []
  (let [AudioContext (or (.-AudioContext js/window)
                         (.-webkitAudioContext js/window))]
    (AudioContext.)))

(defn ^:export init [] 
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (re-frame/dispatch-sync [:initialize-context (create-context)])
  (mount-root))
