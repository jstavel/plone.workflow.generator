(ns fix-definition.core-test
  (:require [clojure.test :refer :all]
            [fix-definition.core :as c]
            [fix-definition.utils :as u]
            [clojure.java.io :as io]
            [clojure.zip :as zip]
            [clojure.xml :as cx]
            [clojure.data.xml :as xml]
            [clojure.data.zip.xml :as zx]
            ))

(def workflow-dir (io/file "/opt/edeposit/src/edeposit.policy/edeposit/policy/profiles/default/workflows"))
(def originalfile-dir "edeposit_originalfile_workflow")
(def eperiodical-dir "edeposit_eperiodical_workflow")
(def acquisitor-role "E-Deposit: Acquisitor")
(def producent-editor-role "E-Deposit: Producent Editor")

(deftest permissions-for-state-role-test
  (testing "permissions for state role"
    (let [root (-> "definition.xml" io/resource io/input-stream cx/parse)]
      (is (= "dc-workflow" (-> root :tag name)))
      (let [state-loc (-> root 
                          zip/xml-zip
                          (zx/xml1-> :state)
                          )
            ]
        (is (= "declaration" (-> state-loc zip/node :attrs :state_id)))
        (is (some #{"Access contents information"}
                  (c/permissions-for state-loc "E-Deposit: Acquisitor")))
        (is (= (list "View History") (c/permissions-for state-loc "E-Deposit: Producent Editor")))
        )
      )
    )
  )

(deftest add-permission-for-state-role-test
  (testing "add permission for state role"
    (let [root (-> "definition.xml" io/resource io/input-stream cx/parse)]
      (is (= "View History" (-> root zip/xml-zip
                                (zx/xml1->
                                 :state
                                 (c/has-not-permission? producent-editor-role "Review portal content")
                                 (c/permissions-for producent-editor-role)
                                 )
                                )
             )
          )
      (is (= (list "View History" "Review portal content")
             (-> root 
                 zip/xml-zip 
                 (zx/xml1->  :state
                             (c/has-not-permission? producent-editor-role "Review portal content")
                             (c/add-permission producent-editor-role "Review portal content")
                             )
                 (c/permissions-for producent-editor-role)
                 )
             )
          )
      )
    )
  )

(deftest add-permission-to-all-states-test
  (testing "add permission to all states"
    (let [root (-> "definition.xml" io/resource io/input-stream cx/parse)]
      (is (= (list "View History" "View History") 
             (-> root zip/xml-zip (zx/xml-> :state (c/permissions-for producent-editor-role)))))
      (let [new-root
            (loop [loc (-> root zip/xml-zip)]
              (if-let [state-loc (zx/xml1-> loc
                                  :state 
                                  (c/has-not-permission? producent-editor-role "Review portal content")
                                  )]
                (recur (-> (zip/root (c/add-permission state-loc producent-editor-role "Review portal content"))
                           zip/xml-zip))
                (zip/root loc)))
            ]
        (is (= (list "View History" "Review portal content" "View History" "Review portal content")
               (-> new-root zip/xml-zip 
                   (zx/xml-> :state (c/permissions-for producent-editor-role)))))
        )
      )
    )
  )

(deftest xml-of-element-tree
  (testing "add read permission to all states for employee roles"
    (let [root (-> "definition.xml" io/resource io/input-stream cx/parse)]
      (is (= false (-> root zip/xml-zip (zx/xml1-> :state :permission-map :permission-role) u/has-tags?))) 
      (is (= true (-> root zip/xml-zip (zx/xml1-> :state :permission-map) u/has-tags?))) 

      (is (= "\n<permission-role>Owner</permission-role>" 
             (-> root zip/xml-zip (zx/xml1-> :state :permission-map :permission-role u/xml)))) 

      (is (= "\n<exit-transition transition_id=\"generateVoucher\"/>" 
             (-> root zip/xml-zip (zx/xml1-> :state :exit-transition u/xml))))
      (is (= "
<permission-map acquired=\"False\" name=\"Access contents information\">
  <permission-role>Owner</permission-role>
  <permission-role>E-Deposit: Acquisitor</permission-role>
  <permission-role>E-Deposit: Producent Administrator</permission-role>
  <permission-role>Site Administrator</permission-role>
  <permission-role>Manager</permission-role>
</permission-map>"
             (-> root zip/xml-zip (zx/xml1-> :state :permission-map u/xml))
             ))
      )
    )
  )

(deftest add-read-permissions-to-originalfile
  (testing "add read permissions to originalfile"
    (let [root (-> (io/file workflow-dir originalfile-dir "definition.xml") io/input-stream
                   cx/parse)]

      (let [new-root "aa"])
      )
    )
  )

(deftest periodical-members-has-read-at-all-states
  (testing "ePeriodical Dept Members has read permissions for all states"
    )
  )
