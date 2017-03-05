(ns potty-time.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route] 
            [ring.adapter.jetty :as ring]
            [cheshire.core :as json]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [potty-time.format.cli :as cli]
            [potty-time.format.browser :as browser]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def potty-states (atom {}))

(defn get-response
  [req]
  (let [headers (:headers req)
        user-agent (get headers "user-agent")
        formatter (if (or (re-find #"curl" user-agent)
                          (re-find #"wget" user-agent))
                    cli/to-curl
                    browser/to-page)]
    (formatter @potty-states)))

(defroutes app-routes
  (GET "/potty" req
       (get-response req)
       #_{:status 200 :body {:state @potty-states}})
  (POST "/potty" req
        (let [{:keys [toilet state]} (:body req)]
          (swap! potty-states assoc toilet state)
          {:status 200 :body {:state @potty-states}}))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false)) 
      wrap-json-response))

(defn init []
  (println "Are you ready to potty!?"))

(defn start [port]
  (ring/run-jetty app {:port port
                       :join? false}))

(defn -main []
  (init)
  (let [port (Integer. (or (System/getenv "PORT") "90779"))]
    (start port)))
