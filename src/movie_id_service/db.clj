(ns movie-id-service.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [environ.core :refer [env]])
  (:import (java.util.regex Pattern)))

(defonce db-connection (atom nil))

(defn connect-to-mongodb []
  (let [connection-string (env :db-uri)]
    (if (nil? connection-string)
      (throw (Exception. "MongoDB connection string is not set"))
      (let [{:keys [db]} (mg/connect-via-uri connection-string)]
        (reset! db-connection db)))))

(defn get-db []
  (if (nil? @db-connection)
    (do
      (println "Database connection is nil, attempting to reconnect...")
      (connect-to-mongodb)                                  ;; attempt to reconnect
      (if (nil? @db-connection)
        (throw (Exception. "Database connection could not be established."))
        @db-connection))
    @db-connection))


(defn search-movies [search]
  (let [db (get-db)
        query {:original_title {$regex (str "(?i).*" (Pattern/quote search) ".*")}}]
    (mc/find-maps db "movies" query)))
