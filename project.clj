(defproject birthday-bot "0.2.1"
  :description "Parse our company birthday list and send out slack message for today's birthdays."
  :url "https://github.com//MarinoPoletine/birthday_slack_bot"
  :license {:name "The MIT License"
            :url "https://github.com/MarinoPoletine/birthday_slack_bot/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.3"]
                 [clj-time "0.11.0"]
                 [enlive "1.1.6"]
                 [clj-http "2.1.0"]
                 [org.julienxx/clj-slack "0.5.2.1"]
                 [org.clojure/tools.logging "0.3.1"]
                 [log4j/log4j "1.2.17"]
                 [clj-logging-config "1.9.12"]]
  :main ^:skip-aot birthday-bot.core
  :resource-paths ["resources"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :test {:dependencies [[clj-http-fake "1.0.2"]]
                    :plugins [[jonase/eastwood "0.2.3"]
                              [lein-ancient "0.6.8"]
                              [lein-kibit "0.1.2"]
                              [lein-cloverage "1.0.6"]
                              [lein-bikeshed "0.2.0"]]
                    :aliases {"omni"
                      ["with-profile" "test"
                      ["do" ["eastwood" "{:exclude-linters [:unlimited-use]}"]
                      ["kibit"] ["bikeshed"] ["ancient"] ["cloverage"]]]
                    }}})
