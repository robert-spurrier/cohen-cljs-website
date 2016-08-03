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
             :url "audio/reality/great_dane.mp3"}]}])
