(ns potty-time.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route] 
            [ring.adapter.jetty :as ring]
            [cheshire.core :as json]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

#_(defn get-response
  [req]
  (let [headers (:headers req)
        user-agent (get headers "user-agent")
        formatter (if (or (re-find #"curl" user-agent)
                          (re-find #"wget" user-agent))
                    cli/format
                    browser/format)]
    (formatter (:params req))))

#_(def post-response
  [req]
    (let []))

(def state (atom {}))

(defroutes app-routes
  (GET "/potty" [tid]
       {:status 200 :body {:state (get @state tid)}})
  (POST "/potty" [tid state]
        (swap! state assoc tid state)
        {:status 200 :body {:state state}})
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
