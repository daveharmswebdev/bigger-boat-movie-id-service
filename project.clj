(defproject movie-id-service "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [cheshire "5.11.0"]
                 [clj-http "3.13.0"]
                 [com.novemberain/monger "3.5.0"]
                 [ring/ring-core "1.13.0"]
                 [ring-cors "0.1.13"]
                 [ring/ring-jetty-adapter "1.9.6"]
                 [compojure "1.6.2"]
                 [ring/ring-defaults "0.3.3"]
                 [environ "1.2.0"]]
  :plugins [[lein-environ "1.2.0"]
            [lein-dotenv "RELEASE"]]
  :main ^:skip-aot movie-id-service.core
  :target-path "target/%s"
  :repl-options {:init-ns movie-id-service.core})
