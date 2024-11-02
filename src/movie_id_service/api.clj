(ns movie-id-service.api
  (:require [cheshire.core :as json]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as response]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [movie-id-service.db :as db]))

(defn get-movies-ids [search]
  (let [movies (db/search-movies search)
        filtered-movies (map #(select-keys % [:id :original_title]) movies)
        movies-json (json/generate-string filtered-movies {:pretty false})]
    (-> (response/response movies-json)
        (response/content-type "application/json"))))

(defroutes app-routes
           (GET "/get-movie-ids" [search]
             (get-movies-ids search))
           (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-cors
        :access-control-allow-origin [#".*"]
        :access-control-allow-methods [:get :post :put :delete]
        :access-control-allow-headers ["Content-Type" "Authorization"])
      (wrap-defaults site-defaults)))