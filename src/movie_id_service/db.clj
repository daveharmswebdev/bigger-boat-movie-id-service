(ns movie-id-service.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [clojure.string :as string]))

(defn connect-to-mongodb []
  (let [{:keys [client db]} (mg/connect-via-uri "mongodb://admin:adminpassword@127.0.0.1:27017/movie_db?authSource=admin")]
    db))

(defn search-movies [search]
  (let [db (connect-to-mongodb)
        movies-collection (mc/find-maps db "movies")
        filtered-movies (filter #(string/includes? (string/lower-case (:original_title %))
                                                   (string/lower-case search))
                                movies-collection)]
filtered-movies))





;(def movies-db
;  [{:adult false, :id 3924, :original_title "Blondie", :popularity 3.142, :video false}
;   {:adult false, :id 6124, :original_title "Der Mann ohne Namen", :popularity 2.39, :video false}
;   {:adult false, :id 8773, :original_title "L'Amour à vingt ans", :popularity 4.919, :video false}
;   {:adult false, :id 25449, :original_title "New World Disorder 9: Never Enough", :popularity 4.087, :video false}
;   {:adult false, :id 31975, :original_title "Sesame Street: Elmo Loves You!", :popularity 0.002, :video true}
;   {:adult false, :id 2, :original_title "Ariel", :popularity 18.473, :video false}
;   {:adult false, :id 3, :original_title "Varjoja paratiisissa", :popularity 15.364, :video false}
;   {:adult false, :id 5, :original_title "Four Rooms", :popularity 40.071, :video false}
;   {:adult false, :id 6, :original_title "Judgment Night", :popularity 21.354, :video false}])

;(defn search-movies [search]
;  (filter #(string/includes? (string/lower-case (:original_title %))
;                             (string/lower-case search))
;          movies-db))