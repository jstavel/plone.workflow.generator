(ns fix-definition.roles
  (:require [clojure.zip :as zip]
            [clojure.xml :as cx]
            [clojure.data.xml :as xml]
            [clojure.data.zip.xml :as zx]
            )
  )

(def roles 
  (list ["Owner" :tags ["producent"]] 
        ["E-Deposit: Producent" :tags ["producent"]]
        ["E-Deposit: Producent Administrator" :tags ["producent"]]
        ["E-Deposit: Producent Editor" :tags ["producent"]]
        ["E-Deposit: Acquisitor" :tags ["internal" "acquisition"]]
        ["E-Deposit: Acquisition Administrator" :tags ["internal" "acquisition"]]
        ["Site Administrator" :tags ["internal" "system"]]
        ["Manager" :tags ["internal" "system"]]
        ["E-Deposit: System" :tags ["internal" "system"]]
        ["E-Deposit: ISBN Agency Member" :tags ["internal" "isbn"]]
        ["E-Deposit: Librarian" :tags ["internal"]]
        ["E-Deposit: Descriptive Cataloguing Administrator" :tags ["internal"]]
        ["E-Deposit: Descriptive Cataloguer" :tags ["internal"]]
        ["E-Deposit: Descriptive Cataloguing Reviewer" :tags ["internal"]]
        ["E-Deposit: Subject Cataloguing Administrator" :tags ["internal"]]
        ["E-Deposit: Subject Cataloguer" :tags ["internal"]]
        ["E-Deposit: Subject Cataloguing Reviewer" :tags ["internal"]]
        ["E-Deposit: Periodical Department Member" :tags ["internal" "eperiodic"]]
        ["E-Deposit: Periodical Department Administrator" :tags ["internal" "eperiodic"]]
        )
  )

(defn internal-roles 
  [root]
  (->> roles 
       (filter (fn [[role & {:keys [tags]}]] (some #{"internal"} tags)))
       (map (fn [[role & {:keys [tags]}]] role))
       )
  )
