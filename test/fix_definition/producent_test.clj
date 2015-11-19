(ns fix-definition.producent-test
  (:require [clojure.test :refer :all]
            [fix-definition.core :as c]
            [fix-definition.utils :as u]
            [fix-definition.roles :as r]
            [clojure.java.io :as io]
            [clojure.zip :as zip]
            [clojure.xml :as xml]
            [clojure.data.xml :as d]
            [clojure.data.zip.xml :as z]
            ))

(def workflow-dir (io/file "/opt/edeposit/src/edeposit.policy/edeposit/policy/profiles/default/workflows"))

(deftest all-internal-roles-can-read-producent
  (testing "all roles test"
    (let [root (-> (io/file workflow-dir "edeposit_producent_workflow" "definition.xml")
                   io/input-stream
                   xml/parse)
          ]
      )
    )
  )

