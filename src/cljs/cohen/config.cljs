(ns cohen.config)

(def debug?
  ^boolean js/goog.DEBUG)

(when debug?
  (enable-console-print!))

(def about-panel-config {:about-img-url "images/TomCohen.jpg"
                         :quotes ["A drummer of creative agitation. Karl Stark, The Philadelphia Inquirer"
                                  "Beautifully furious...Cohen plays with abandon, pushing musicians. Cadence Magazine"]
                         :abouts ["Drummer Tom Cohen is a seasoned veteran of the Philadelphia music scene having carved out a successful career in a town of many excellent musicians."
                                  "Though primarily a jazz musician, Tom has been busy as a freelance drummer performing in an array of music styles from Funk and R&B to Classic Rock, Latin and Middle-Eastern music."
                                  "In addition to various venues on the East coast, Tom performs select gigs throughout the US and Europe."]})

(def music-panel-config
  {:albums
   [{:artist "Mr. Carmack"
      :album-name "Bang"
      :album-art-url "audio/bang/cover.jpg"
      :songs [{:name "Bang"
               :url "audio/bang/bang.mp3"}
              {:name "Fire"
               :url "audio/bang/fire.mp3"}
              {:name "Drop"
               :url "audio/bang/drop.mp3"}]}
     {:artist "Mr. Carmack"
      :album-name "Melodies"
      :album-art-url "audio/melodies/cover.jpg"
      :songs [{:name "At Night"
               :url "audio/melodies/at_night.mp3"}
              {:name "Paces"
               :url "audio/melodies/paces.mp3"}
              {:name "2084"
               :url "audio/melodies/2084.mp3"}]}
     {:artist "Mr. Carmack"
      :album-name "Red"
      :album-art-url "audio/red/cover.png"
      :songs [{:name "Sanctify"
               :url "audio/red/sanctify.mp3"}
              {:name "Trophies"
               :url "audio/red/trophies.mp3"}
              {:name "Salva"
               :url "audio/red/salva.mp3"}]}   
     {:artist "Mr. Carmack"
      :album-name "Reality"
      :album-art-url "audio/reality/cover.jpg"
      :songs [{:name "Next Afternoon"
               :url "audio/reality/next_afternoon.mp3"}
              {:name "Beez"
               :url "audio/reality/beez.mp3" }
              {:name "Great Dane"
               :url "audio/reality/great_dane.mp3"}]}]})

(def photos-panel-config {:image-urls ["cat"
                                       "cats"
                                       "chameleon"
                                       "dog"
                                       "ducks"
                                       "goat"
                                       "ostrich"
                                       "pigeon"
                                       "pigs"
                                       "seagulls"
                                       "wasp"
                                       "yawn"]} )

(def videos-panel-config {:youtube-ids ["eANTTBvIXmI"
                                        "1PRsRagfrm0"
                                        "rHLvuUmQwxc"
                                        "6kc97rN4Af0"]})
