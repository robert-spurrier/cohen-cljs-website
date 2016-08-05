(ns cohen.views
  (:require   
   [reagent.core :as r]
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [cohen.album-player :refer [album-player]]
   [cohen.config :refer [about-panel-config
                         music-panel-config
                         photos-panel-config
                         videos-panel-config]])
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
  (let [photo-url (:about-img-url about-panel-config)
        quotes (:quotes about-panel-config)
        abouts (:abouts about-panel-config)]
    [re-com/scroller
     :v-scroll :auto
     :child
     [re-com/v-box
      :padding "50px 0px 0px 0px"
      :align :center
      :children [[re-com/box
                  :max-width "800px"
                  :child [:p.quote (get quotes 0)]]
                 [re-com/gap :size "1em"]
                 [re-com/box
                  :max-width "800px"               
                  :child [:p.quote (get quotes 1)]]
                 [re-com/gap :size "60px"]
                 [re-com/box
                  :child [:img {:src photo-url}]]
                 [re-com/gap :size "60px"]
                 [re-com/box
                  :width "66.6667%"
                  :child [:p.about (get abouts 0)]]
                 [re-com/gap :size "1px"]
                 [re-com/box
                  :width "66.6667%"
                  :child [:p.about (get abouts 1)]]
                 [re-com/gap :size "1px"]
                 [re-com/box
                  :width "66.6667%"
                  :child [:p.about (get abouts 2)]]]]]))

(defn player-pairs [album-data context]
  (for [[config-left config-right] (partition 2 album-data)]
      [re-com/h-box
       :justify :around
       :gap "1em"
       :children [[(album-player context config-left)]
                  [(album-player context config-right)]]]))

(defn music-panel []
  (let [albums (:albums music-panel-config)
        context (re-frame/subscribe [:audio-context])]
    [re-com/scroller
     :v-scroll :auto
     :child [re-com/v-box
             :gap "5em"
             :padding "50px 50px 50px 50px"
             :children (player-pairs albums @context)]]))

(def youtube 
  (r/adapt-react-class js/YouTube))

(defn youtube-component [id]
  [youtube {:video-id id}])

(defn youtube-box [id]
  [re-com/box
     :style {:background-color "#000000"}
     :child  (youtube-component id)])

(defn videos-panel []
  (let [ids (:youtube-ids videos-panel-config)]
    [re-com/scroller
     :v-scroll :auto
     :child [re-com/v-box
             :align :center
             :padding "50px 0px 0px 0px"
             :gap "1em"
             :children (map youtube-box ids)]]))

(def Gallery
  (r/adapt-react-class js/Gallery))

(defn image-map [img]
  {:src (str "./images/800-" img ".jpg")
   :width 800
   :height 600
   :aspectRatio 1
   :lightboxImage {:src (str "./images/1024-" img ".jpg")}})

(defn gallery-component [urls]
  (fn []
    [Gallery {:photos (clj->js (into [] (map image-map urls)))}]))

(defn photos-panel []
  (let [urls (:image-urls photos-panel-config)]
    [re-com/scroller
     :v-scroll :auto
     :child [re-com/box
             :align :center
             :padding "50px 50px 50px 50px"
             :child [(gallery-component urls)]]]))

(def ses (js/AWS.SES.))

(defn contact-panel
  []
  (let [email-address (r/atom nil)
        email-content (r/atom nil)]
    (fn
      []
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
                                   :on-change #(reset! email-address %)]]
                          [re-com/input-textarea
                           :width "500px"
                           :model ""
                           :on-change #(reset! email-content %)
                           :placeholder "Message"]
                          [re-com/button
                           :label "Submit"
                           :on-click #()]]]])))

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
