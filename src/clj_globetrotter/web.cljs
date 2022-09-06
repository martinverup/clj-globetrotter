(ns clj-globetrotter.web
  (:require react-dom
            [clj-globetrotter.core :as core]))

(.render js/ReactDOM
  (.createElement js/React "h2" nil "Hello, React!")
  (.getElementById js/document "app"))
