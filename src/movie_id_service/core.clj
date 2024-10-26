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

(defn -main []
  (run-jetty app {:port 3000 :join? false}))