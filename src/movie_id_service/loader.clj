(ns movie-id-service.loader
  (:import [java.time LocalDate]
           [java.time.format DateTimeFormatter]
           [java.util.zip GZIPInputStream])
  (:require [clj-http.client :as http]
            [clojure.java.io :as io]
            [monger.collection :as mc]
            [cheshire.core :as json]))

;; create a URL:
; http://files.tmdb.org/p/exports/movie_ids_10_22_2024.json.gz
; but today's date
(defn generate-url []
  (let [today (LocalDate/now)
        formatter (DateTimeFormatter/ofPattern "MM_dd_yyyy")
        formatted-date (.format today formatter)]
    (str "http://files.tmdb.org/p/exports/movie_ids_" formatted-date ".json.gz")))

;; call the movie db api with the url and get lists of movies as a gzip file
(defn download-gzip []
  (let [url (generate-url)
        output-file (io/file "movie_ids.json.gz")]
    (println "Downloading from:" url)
    (with-open [input-stream (:body (http/get url {:as :stream}))]
      (io/copy input-stream output-file))
    output-file))

;; unzip the file
(defn decompress-gzip-fil []
  (let [compressed-file (download-gzip)
        uncompressed-file (io/file "movie_ids.json")]
    (with-open [input-stream (GZIPInputStream. (io/input-stream compressed-file))
                output-stream (io/output-stream uncompressed-file)]
      (io/copy input-stream output-stream))
    uncompressed-file))

;; iterate over the file which is newline delimited json objects and upsert into the mongodb
(defn insert-movies [db]
  (let [file (decompress-gzip-fil)
        coll-name "movies"
        batch-size 500]
    ;; drop collection if it exists
    (mc/remove db coll-name)
    ;; process the file and insert all records in batches
    (with-open [reader (io/reader file)]
      (loop [lines (line-seq reader)
             batch []]
        (if (seq lines)
          (let [line (first lines)
                movie-data (json/parse-string line true)]
            (if (< (count batch) batch-size)
              (recur (rest lines) (conj batch movie-data))
              (do
                ;; perform bulk insert
                (mc/insert-batch db coll-name batch)
                (println "Inserted" batch-size "records")
                ;; continue with next batch
                (recur (rest lines) []))))
          (when (seq batch)
            (mc/insert-batch db coll-name batch)))))))
