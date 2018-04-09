(ns birthday-bot.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.tools.logging :as log]
            [clj-slack.chat :as slack]
            [birthday-bot.config :as config]
            [birthday-bot.parser :as parser]
            [clojure.tools.logging :as log]
            [clojure.string :as str])
  (:gen-class))


(defn send-message [config people]
  (let [message (:message config)]
    (log/info "Sending to slack"
              (:channel message)
              " message:"
              (format (:message message) people))
    (slack/post-message (:slack config)
                        (:channel message)
                        (format (:message message) people)
                        {:username "birthday-bot"
                         :icon_emoji ":birthday:"
                         :parse "full"
                         :link_names "1"})))

(def cli-options
  [["-c" "--config FILE" "Config File"]
   ["-v" nil "Logging Verbosity level"
    :id :verbosity
    :default 0
    :assoc-fn (fn [m k _] (update-in m [k] inc))]
   ["-h" "--help"]])

(defn exit [status message]
  (log/warn "System exit:" message)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]}
      (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (str "birthday-bot.\n\n"
                                   "Options:\n"
                                   summary))
      errors (exit 1 errors))
    (config/config-log options)
    (let [config (config/read-config (:config options))
          people (parser/get-people (:birthday-webpage config))]
      (if (str/blank? people)
        (log/info "No birthdays today")
        (send-message config people)))))
