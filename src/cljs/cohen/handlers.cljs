(ns cohen.handlers
  (:require [re-frame.core :as re-frame]            
            [cohen.db :as db]
            ))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :initialize-context
 (fn [db [_ context]]
   (assoc db :audio-context context)))

(re-frame/register-handler
 :initialize-song-buffers
 (fn [db [_ song-urls]]
   (assoc db :song-buffers (create-buffers (:context db) song-urls)) ))

(re-frame/register-handler
 :nav-mouse-over
 (fn [db [_ [label highlight?]]]
   (assoc-in db [:nav-highlight label] highlight?)))

(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))
