(ns birthday-bot.parser
    (:require [net.cgrand.enlive-html :as html]
              [clj-http.client :as http]
              [clj-time.core :as time]
              [clj-time.format :as format]
              [clojure.string :as str]
              [clojure.tools.logging :as log]))


(defn get-day []
  (let [day-format (format/formatter "MMM-d")]
    (format/unparse day-format (time/now))))

(defn get-page [config]
  (let [my-cs (clj-http.cookies/cookie-store)]
    (try
      (http/post (:login-url config)
                 {:form-params {:os_username (:username config)
                                :os_password (:password config)}
                  :cookie-store my-cs})
      (http/get (:url config) {:cookie-store my-cs})
      (catch Exception e
        (log/error "Unable to open URL:" (:status (ex-data e)))
        (System/exit 1)))))

(defn get-data [data]
  (log/trace "Got the following data to parse:" data)
  (html/select
    (html/html-resource (java.io.StringReader. (:body data)))
    [:.confluenceTable :tr]))


(defn merge-people [content]
  (log/debug "merge-people:" content)
  (cond
    (nil? content) " "
    (string? content) (str (str/trim content) " ")
    (map? content) (merge-people (:content content))
    (coll? content) (clojure.string/join (map merge-people content))
    :else (str content)))

(defn get-people [config]
  (let [data (get-data (get-page config))
        day (get-day)]
    (log/info "Running for" day)
    (str/trim (merge-people (:content (first (html/select
      (for [tr (html/select data [:tr])
           :when (str/includes? tr day)]
      tr) [:td])))))))
