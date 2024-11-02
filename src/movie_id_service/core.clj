(ns movie-id-service.core
  (:require [movie-id-service.db :as service-db]
            [movie-id-service.loader :as loader]
            [movie-id-service.api :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]]))

;(defn -main [& _]
;  (let [db (service-db/connect-to-mongodb)]
;    (println "Connected to MongoDB.")
;    (println "Dropping and recreating collection, and then inserting movie data...")
;    (loader/insert-movies db)
;    (println "Insertion complete.")))

(defn load-movies []
  (try
    (let [db (service-db/get-db)]
      (loader/insert-movies db)
      (println "Movies loaded successfully."))
    (catch Exception e
      (println "Error occurred while loading movies" (.getMessage e)))))

(defn start-server []
  (run-jetty app {:port 3000 :join? false}))

(defn -main []
  (service-db/connect-to-mongodb)
  (start-server))