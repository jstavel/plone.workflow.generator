(ns fix-definition.core
  (:require [clojure.zip :as zip]
            [clojure.xml :as cx]
            [clojure.data.xml :as xml]
            [clojure.data.zip.xml :as zx]
            [fix-definition.roles :as r]
            )
  )



(defn permissions-for
  ([loc role] (zx/xml->
               loc
               :permission-map
               [:permission-role (zx/text= role)]
               (zx/attr :name)))

  ([role]  (fn [loc] (permissions-for loc role)))
  )

(defn has-not-permission? [role permission]
  (fn [loc]
    (not (some #{permission} (permissions-for loc role))))
  )

(defn no-permission [loc role permission]
  (not (some #{permission} (permissions-for loc role)))
  )

(defn add-permission
  ([loc role permission]
   (if-let [perm-loc (zx/xml1-> loc :permission-map (zx/attr= :name permission))]
     (zip/up (zip/insert-child perm-loc {:tag :permission-role :attrs nil :content [role]}))
     loc
     )
   )
  
  ([role permission] (fn [loc] (add-permission loc role permission)))
  )

(defn add-permission-to-all-states
  [root role permission]
  (loop [loc (-> root zip/xml-zip)]
    (if-let [state (zx/xml1-> loc :state (has-not-permission? role permission))]
      (recur (-> (add-permission state role permission) zip/root zip/xml-zip))
      (zip/root loc)
      )
    )
  )

(defn add-read-permissions-to-all-states
  [root role]
  (loop [loc (-> root zip/xml-zip)
         [role & rest] r/internal-roles]
    )
  )
