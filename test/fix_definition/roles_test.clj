(ns fix-definition.roles-test
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

(deftest all-roles-test
  (testing "all roles test"
    (let [root (-> (io/file workflow-dir "edeposit_originalfile_workflow" "definition.xml")
                   io/input-stream
                   xml/parse)
          ]
      (is (= (list "E-Deposit: Acquisitor"
                   "E-Deposit: Acquisition Administrator"
                   "Site Administrator"
                   "Manager"
                   "E-Deposit: System"
                   "E-Deposit: ISBN Agency Member"
                   "E-Deposit: Librarian"
                   "E-Deposit: Descriptive Cataloguing Administrator"
                   "E-Deposit: Descriptive Cataloguer"
                   "E-Deposit: Descriptive Cataloguing Reviewer"
                   "E-Deposit: Subject Cataloguing Administrator"
                   "E-Deposit: Subject Cataloguer"
                   "E-Deposit: Subject Cataloguing Reviewer"
                   "E-Deposit: Periodical Department Member"
                   "E-Deposit: Periodical Department Administrator"
                   )
             (r/internal-roles root)
             )
          )
      )
    )
  )

