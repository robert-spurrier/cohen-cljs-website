(ns cohen.views
  (:require   
   [reagent.core :as r]
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [cohen.album-player :refer [album-player]]
   [cohen.config :refer [album-data youtube-ids]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn copyright []
  [re-com/box
   :align :center
   :child "© Copyright 2016 Tom Cohen"])

(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label (str "Hello from " @name ". This is the Home Page.")
       :level :level1])))

(defn nav-link [label href]
  (let [highlight? (re-frame/subscribe [:nav-highlight label])]
    (fn []
      [re-com/box
       :height "100%"       
       :justify :center
       :align :center
       :attr {:on-mouse-over #(re-frame/dispatch [:nav-mouse-over [label true]])
              :on-mouse-out  #(re-frame/dispatch [:nav-mouse-over [label false]])}
       :style {:border-radius "20px"
               :background-color (if @highlight? "#addaff" "")}
       :child [re-com/hyperlink-href
               :style {:text-decoration "none"}
               :label label
               :href  href]])))

(defn about-panel []
  [re-com/scroller
   :v-scroll :auto
   :child
   [re-com/v-box
    :padding "50px 0px 0px 0px"
    :align :center
    :children [[re-com/box
                :max-width "800px"
                :child [:p.quote "A drummer of creative agitation. 
                       Karl Stark, The Philadelphia Inquirer"]]
               [re-com/gap :size "1em"]
               [re-com/box
                :max-width "800px"               
                :child [:p.quote "Beautifully furious...Cohen plays with abandon, pushing musicians. Cadence Magazine"]]
               [re-com/gap :size "60px"]
               [re-com/box
                :child [:img {:src "resources/public/images/TomCohen.jpg"}]]
               [re-com/gap :size "60px"]
               [re-com/box
                :width "66.6667%"
                :child [:p.about "Drummer Tom Cohen is a seasoned veteran of the Philadelphia music scene having carved out a successful career in a town of many excellent musicians."]]
               [re-com/gap :size "1px"]
               [re-com/box
                 :width "66.6667%"
                :child [:p.about "Though primarily a jazz musician, Tom has been busy as a freelance drummer performing in an array of music styles from Funk and R&B to Classic Rock, Latin and Middle-Eastern music."]]
               [re-com/gap :size "1px"]
                [re-com/box
                 :width "66.6667%"
                 :child [:p.about "In addition to various venues on the East coast, Tom performs select gigs throughout the US and Europe."]]]]])

(defn player-pairs [album-data context]
  (for [[config-left config-right] (partition 2 album-data)]
      [re-com/h-box
       :justify :around
       :gap "1em"
       :children [[(album-player context config-left)]
                  [(album-player context config-right)]]]))

(defn music-panel []
  (let [context (re-frame/subscribe [:audio-context])]
    [re-com/scroller
     :v-scroll :auto
     :child [re-com/v-box
             :gap "5em"
             :padding "50px 50px 50px 50px"
             :children (player-pairs album-data @context)]]))

(def youtube 
  (r/adapt-react-class js/YouTube))

(defn youtube-component [id]
  [youtube {:video-id id}])

(defn youtube-box [id]
  [re-com/box
     :style {:background-color "#000000"}
     :child  (youtube-component id)])

(defn videos-panel []
  [re-com/scroller
   :v-scroll :auto
   :child [re-com/v-box
           :align :center
           :padding "50px 0px 0px 0px"
           :gap "1em"
           :children (map youtube-box youtube-ids)]])

(def Gallery
  (r/adapt-react-class js/Gallery))

(def images ["cat" "cats" "chameleon" "dog" "ducks" "goat" "ostrich" "pigeon" "pigs" "seagulls" "wasp" "yawn"])
(defn image-map [img]
  {:src (str "./resources/public/images/800-" img ".jpg")
   :width 800
   :height 600
   :aspectRatio 1
   :lightboxImage
   {:src (str "./resources/public/images/thumbnail-" img ".jpg")
    :srcset [(str "./resources/public/images/1024-" img ".jpg 1024w")
             (str "./resources/public/images/800-" img ".jpg 800w")
             (str "./resources/public/images/500-" img ".jpg 500w")
             (str "./resources/public/images/320-" img ".jpg 320w")]}})

(defn gallery-component []
  [Gallery {:photos (clj->js (into [] (map image-map images)))}])

(defn photos-panel []
  [re-com/scroller
   :v-scroll :auto
   :child [re-com/box
           :align :center
           :padding "50px 50px 50px 50px"
           :child [gallery-component]]])

(defn contact-panel []
  [re-com/box
   :padding "50px"
   :align :center
   :child [re-com/v-box
           :justify :start
           :gap "1em"
           :children [[re-com/box
                       :justify :start
                       :child [re-com/input-text
                               :model ""
                               :placeholder "Your email address (required)"
                               :on-change #()]]
                      [re-com/input-textarea
                       :width "500px"
                       :model ""
                       :on-change #()
                       :placeholder "Message"]
                      [re-com/button
                       :label "Submit"]]]])

(defmulti panels identity)
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :music-panel [] [music-panel])
(defmethod panels :photos-panel [] [photos-panel])
(defmethod panels :videos-panel [] [videos-panel])
(defmethod panels :contact-panel [] [contact-panel])

(defmethod panels :default [] [:div])

(defn timer-component []
  (let [seconds-elapsed (r/atom 0)]     ;; setup, and local state
    (fn []        ;; inner, render function is returned
      (js/setTimeout #(swap! seconds-elapsed inc) 1000)
      [re-com/label :label (str "Seconds Elapsed: " @seconds-elapsed)])))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
          [re-com/h-box
           :height "100%"
           :justify :center
           :style {:background "#000000"}
           :children [[re-com/v-box                   
                       :width "75%"
                       :style {:background "#ffffff"}
                       :children [[re-com/h-box                                  
                                   :justify :center
                                   :gap "10px"
                                   :children [[re-com/box
                                               :child [re-com/label
                                                       :label "TOM"
                                                       :style {:font-size "100px"
                                                               :font-weight "100"}]]
                                              [re-com/line]
                                              [re-com/box
                                               :child [re-com/label                                      
                                                       :label "COHEN"
                                                       :style {:font-size "100px"
                                                               :color "#addaff"}]]]]
                                  [re-com/h-box
                                   :style {:font-size "20px"
                                           :background "#f5f5f5"}
                                   :height "60px"
                                   :align :center
                                   :justify :center
                                   :gap "10px"
                                   :children [[(nav-link "ABOUT" "#/about")]
                                              [(nav-link "MUSIC" "#/music")]
                                              [(nav-link "PHOTOS" "#/photos")]
                                              [(nav-link "VIDEOS" "#/videos")]
                                              [(nav-link "CONTACT" "#/contact")]]]
                                  (panels @active-panel)]]]])))
