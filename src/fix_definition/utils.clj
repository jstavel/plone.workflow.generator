(ns fix-definition.utils
  (:require [clojure.zip :as zip]
            [clojure.xml :as cx]
            [clojure.data.xml :as xml]
            [clojure.data.zip.xml :as zx]
            [clojure.data.zip :as dz]
            [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [clojure.string :as s]
            )
  )

(defn has-tags? [loc]
  (some? (some :tag (-> loc zip/node :content)))
  )

(defn xml [loc & {:keys [indent-level] :or {indent-level 0}}]
  (let [indent (s/join (repeat indent-level "  "))
        tag (-> loc zip/node :tag name)
        attrs (-> loc zip/node :attrs)
        ]
    (if (-> loc has-tags? not)
      (let [content (-> loc zx/text)
            attrs-formated (s/join " " (for [[k v] attrs] (format "%s=\"%s\"" (name k) v)))
            ]
        (if (s/blank? attrs-formated)
          (if (s/blank? content)
            (format "\n%s<%s/>" indent tag)
            (format "\n%s<%s>%s</%s>" indent tag content tag)
            )
          (if (s/blank? content)
            (format "\n%s<%s %s/>" indent tag attrs-formated)
            (format "\n%s<%s %s>%s</%s>" indent tag attrs-formated content tag)
            )
          )
        )
      (let [attrs-formated (s/join " " (for [[k v] attrs] (format "%s=\"%s\"" (name k) v)))
            content (apply str (for [child (-> loc zip/down dz/right-locs)] 
                                 (xml child :indent-level (inc indent-level))))
            ]
        (if (s/blank? attrs-formated)
          (format "\n%s<%s>%s\n%s</%s>" indent tag content indent tag)
          (format "\n%s<%s %s>%s\n%s</%s>" indent tag attrs-formated content indent tag)
          )
        )
      )
    )
  )
