(ns birthday-bot.parser-test
  (:require [clojure.test :refer :all]
            [birthday-bot.parser :refer :all]
            [clj-http.client :as client])
  (:use clj-http.fake))


(deftest get-day-test
  (testing "get-day returns in proper format"
    (is (re-matches (re-pattern (str "(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|"
                                     "Sep|Oct|Nov|Dec)-[1-9][0-9]?"))
                    (get-day)))))

(deftest get-page-test
  (testing "get-page makes expected http calls to configured URLs"
    (let [config {:login-url "http://test.test/login"
                  :url "http://test.test/data"
                  :username "dude"
                  :password "sweet"}]
      (with-fake-routes {(:login-url config)
                          {:post (fn [request] {:status 200
                                         :headers {}
                                         :body "Logged in"})}
                         (:url config)
                          (fn [request] {:status 200
                                         :headers {}
                                         :body "Some data"})}
        (is (= (:body (get-page config)) "Some data"))))))

(deftest get-people-test
  (let [data {:body (str "<p><strong>JANUARY</strong></p><div>"
                         "<table class=\"confluenceTable\"><tbody><tr>"
                         "<td colspan=\"1\" class=\"confluenceTd\">"
                         "Single Person</td><td colspan=\"1\" "
                         "class=\"confluenceTd\">Jan-4</td></tr><tr>"
                         "<td class=\"confluenceTd\"><p>Person 1 &amp;"
                         "</p><p>Person 2 &amp;</p><p>Person 3</p></td>"
                         "<td class=\"confluenceTd\"><p>Jan-5</p></td></tr>"
                         "<tr><td colspan=\"1\" class=\"confluenceTd\">"
                         "Person 1 &amp; <span>Person 2</span></td>"
                         "<td colspan=\"1\" class=\"confluenceTd\">"
                         "Jan-6</td></tr>"
                         "</tbody></table></div>")}]
    (testing "get-people with a single person's birthday"
      (with-redefs-fn {#'birthday-bot.parser/get-page (fn [config] data)
                       #'birthday-bot.parser/get-day (fn [] "Jan-4")}
        #(is (= (get-people {}) "Single Person"))))
    (testing "get-people with a multiple people's birthdays"
      (with-redefs-fn {#'birthday-bot.parser/get-page (fn [config] data)
                       #'birthday-bot.parser/get-day (fn [] "Jan-5")}
        #(is (= (get-people {}) (str "Person 1 & Person 2 & Person 3")))))
    (testing "get-people with a two people's birthdays on same line"
      (with-redefs-fn {#'birthday-bot.parser/get-page (fn [config] data)
                       #'birthday-bot.parser/get-day (fn [] "Jan-6")}
        #(is (= (get-people {}) (str "Person 1 & Person 2")))))))
