(ns birthday_slack_bot.config
    (:require [clojure.java.io :as io]
              [clojure.edn :as edn]
              [clojure.tools.logging :as log]
              [clj-logging-config.log4j :as log-conf]))


(defn read-config [config-file]
  (let [path (or config-file
                 (io/resource "config.edn"))]
    (when-not path
        (log/error "Config file not found")
        (System/exit 1))
    (edn/read-string (slurp path))))

(def levels {0 :warn
             1 :info
             2 :debug
             3 :trace})

(defn config-log [options]
  (log-conf/set-logger! "birthday_slack_bot"
                        :level (levels (:verbosity options))))