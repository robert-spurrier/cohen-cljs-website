(ns cohen.album-player
  (:require
   [reagent.core :as r]
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [goog.net.XhrIo]
   [cljs.core.async :as async :refer [<! >! into chan close! poll!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn decode-audio-data
  [context data]
  (let [ch (chan)]
    (.decodeAudioData context
                        data
                        (fn [buffer]
                          (go (>! ch buffer)
                              (close! ch))))
    ch))

(defn get-audio [url]
  (let [ch (chan)]
    (doto (goog.net.XhrIo.)
      (.setResponseType "arraybuffer")
      (.addEventListener goog.net.EventType.COMPLETE
                         (fn [event]
                           (let [res (-> event .-target .getResponse)]
                             (go (>! ch res)
                                 (close! ch)))))
      (.send url "GET"))
    ch))

(defn create-buffer [context url]
  (go
    (let [response (<! (get-audio url))
          buffer (<! (decode-audio-data context response))]
      buffer)))

(defn create-source [player-state context name]
  (let [buffer (get-in @player-state [:buffers name])
        source (doto (.createBufferSource context)
                 (aset "buffer" buffer))]
    source))

(defn playlist-item [player-state context artist album-name album-art-url {:keys [name]}]
  [re-com/h-box
   :class (str "playlist-item" (if (and (= name (:name (:selected @player-state)))
                                        (= :playing (:status (:selected @player-state))))
                                 " amplitude-active-song-container"
                                 ""))
   :attr {:on-click #(let [playing (:selected @player-state)]
                       (if (= name (:name playing))
                         (do (.stop (:source playing))
                             (swap! player-state assoc :selected nil))
                         (do
                            (when playing
                             (.stop (:source playing))
                             (swap! player-state assoc :selected nil))
                           (if (get-in @player-state [:buffers name])
                             (when-let [source (create-source player-state context name)]
                               (.connect source (.-destination context))
                               (swap! player-state assoc :selected {:status :playing
                                                                    :name name
                                                                    :source source
                                                                    :start-time (.-currentTime context)
                                                                    :start-offset 0})
                               (.start source))
                             (print "Track buffer is not loaded yet.")))))}
       :children [[re-com/box
                   :child [:img.album-art {:src album-art-url}]]
                  [re-com/v-box
                   :class "playlist-meta"
                   :children [[re-com/label
                               :class "now-playing-title"
                               :label name]
                              [re-com/label
                               :class "album-information"
                               :label (str artist " - " album-name)]]]
                  [re-com/box
                   :style {:clear "both"}
                   :child ""]]])

(defn load-songs [context state songs]
  (go (loop [songs songs]
        (when-let [song (first songs)]
          (do (swap! state assoc-in [:buffers (:name song)] (<! (create-buffer context (:url song))))
              (recur (rest songs)))))))

(defn start-offset [context selected]
  (+ (:start-offset selected)
     (- (.-currentTime context)
        (:start-time selected))))

(defn album-player [context {:keys [artist album-name album-art-url songs]}]
  (let [player-state (r/atom {:buffers {}
                              :selected nil})
        ch (load-songs context player-state songs)]    
    (fn []
          [re-com/v-box
           :children [[re-com/v-box
                       :attr {:id "top-header"}
                       :style {:display "block"}
                       :children [[re-com/box
                                   :class "now-playing-title"
                                   :child ""]
                                  [re-com/h-box
                                   :class "album-information"
                                   :children [[re-com/label
                                               :label artist]
                                              [re-com/label
                                               :label album-name]]]]]
                      [re-com/box
                       :attr {:id "top-large-album"}
                       :style {:border "none"}
                       :child [:img#large-album-art {:src album-art-url ;get art path
                                                     :amplitude-song-info "cover"}]]
                      [re-com/box
                       :attr {:id "small-player"}
                       :child
                       [re-com/v-box
                        :attr {:id "small-player-full-bottom"}
                        :style {:display "block"}
                        :children [
                                   [re-com/h-box
                                    :attr {:id "small-player-full-bottom-controls"}
                                    :children [[re-com/box
                                                :class "amplitude-prev"
                                                :attr {:id "middle-bottom-previous"}
                                                :child ""]
                                               (let [selected (:selected @player-state)]
                                                 [re-com/box
                                                  :class (str "amplitude-play-pause "
                                                              (if (= :playing (:status selected))
                                                                "amplitude-playing"
                                                                "amplitude-paused"))                                                
                                                  :attr {:id "small-player-bottom-play-pause"
                                                         :on-click #(cond
                                                                      (= :playing (:status selected)) (do
                                                                                                       (swap! player-state assoc-in [:selected :status] :paused)
                                                                                                       (swap! player-state assoc-in [:selected :start-offset] (start-offset context selected))
                                                                                                       (.stop (:source selected)))
                                                                      (= :paused (:status selected)) (do
                                                                                                       (let [source (create-source player-state context (:name selected))
                                                                                                             current-time (.-currentTime context)
                                                                                                             start-offset (:start-offset selected)
                                                                                                             duration (.-duration (.-buffer source))
                                                                                                             start-time (mod start-offset duration)]
                                                                                                         (.connect source (.-destination context))
                                                                                                         (swap! player-state assoc :selected {:status :playing
                                                                                                                                              :name (:name selected)
                                                                                                                                              :source source
                                                                                                                                              :start-time current-time
                                                                                                                                              :start-offset start-offset})
                                                                                                         (.start source 0 start-time))))}
                                                  :child ""])
                                               [re-com/box
                                                :class "amplitude-next"
                                                :attr {:id "middle-top-next"}
                                                :child ""]]]
                                   [re-com/h-box
                                    :attr {:id "small-player-full-bottom-info"}
                                    :align :center
                                    :children [[re-com/h-box
                                                :class "current-time"
                                                :children [[re-com/label
                                                            :class "amplitude-current-minutes"
                                                            :label "0"]
                                                           [re-com/label
                                                            :label ":"]
                                                           [re-com/label
                                                            :class "amplitude-current-seconds"
                                                            :label "00"]]]
                                               [re-com/box
                                                :class "amplitude-song-time-visualization"
                                                :attr {:id "song-time-visualization-large"}
                                                :child ""]
                                               [re-com/h-box
                                                :class "time-duration"
                                                :children [[re-com/label
                                                            :class "amplitude-duration-minutes"
                                                            :label "0"]
                                                           [re-com/label
                                                            :label ":"]
                                                           [re-com/label
                                                            :class "amplitude-duration-seconds"
                                                            :label "00"]]]]]]]]              
                      [re-com/box
                       :attr {:id "small-player-playlist"}
                       :style {:display "block"}
                       :child [re-com/v-box
                               :children
                               [[re-com/box
                                 :class "information"
                                 :child [re-com/label
                                         :label "Playlist"]]
                                [re-com/line]
                                [re-com/v-box
                                 :children (for [song songs]
                                             (playlist-item player-state context artist album-name album-art-url song))]]]]]]))) 
