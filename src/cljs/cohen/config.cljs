(ns cohen.config)

(def debug?
  ^boolean js/goog.DEBUG)

(when debug?
  (enable-console-print!))

(def images )

(def youtube-ids ["eANTTBvIXmI"
                  "1PRsRagfrm0"
                  "rHLvuUmQwxc"
                  "6kc97rN4Af0"])

(def album-data
  [{:artist "Mr. Carmack"
    :album-name "Bang"
    :album-art-url "resources/public/audio/bang/cover.jpg"
    :songs [{:name "Bang"
             :url "resources/public/audio/bang/bang.mp3"}
            {:name "Fire"
             :url "resources/public/audio/bang/fire.mp3"}
            {:name "Drop"
             :url "resources/public/audio/bang/drop.mp3"}]}
   {:artist "Mr. Carmack"
    :album-name "Melodies"
    :album-art-url "resources/public/audio/melodies/cover.jpg"
    :songs [{:name "At Night"
             :url "resources/public/audio/melodies/at_night.mp3"}
            {:name "Paces"
             :url "resources/public/audio/melodies/paces.mp3"}
            {:name "2084"
             :url "resources/public/audio/melodies/2084.mp3"}]}
   {:artist "Mr. Carmack"
    :album-name "Red"
    :album-art-url "resources/public/audio/red/cover.png"
    :songs [{:name "Sanctify"
             :url "resources/public/audio/red/sanctify.mp3"}
            {:name "Trophies"
             :url "resources/public/audio/red/trophies.mp3"}
            {:name "Salva"
             :url "resources/public/audio/red/salva.mp3"}]}   
   {:artist "Mr. Carmack"
    :album-name "Reality"
    :album-art-url "resources/public/audio/reality/cover.jpg"
    :songs [{:name "Next Afternoon"
             :url "resources/public/audio/reality/next_afternoon.mp3"}
            {:name "Beez"
             :url "resources/public/audio/reality/beez.mp3" }
            {:name "Great Dane"
             :url "resources/public/audio/reality/great_dane.mp3"}]}])
