(ns swarmpit.component.menu
  (:require [swarmpit.material :as material :refer [svg]]
            [swarmpit.controller :as ctrl]
            [rum.core :as rum]
            [clojure.string :as string]))

(enable-console-print!)

(defonce state (atom {:opened true}))

(def drawer-container-closed-style
  #js{:width     "70px"
      :transform "translate(0px, 0px)"})

(def drawer-container-opened-style
  #js{:width "200px"})

(rum/defc drawer-category < rum/static [name opened?]
  (let [class (if opened? "drawer-item-category"
                          "drawer-item-category closed")]
    (material/menu-item
      #js {:className   class
           :primaryText name
           :disabled    true})))

(rum/defc drawer-item < rum/static [name icon opened?]
  (let [text (if opened? name nil)
        lname (string/lower-case name)]
    (material/menu-item
      #js {:className     "drawer-item"
           :innerDivStyle #js {:paddingLeft "50px"}
           :primaryText   text
           :href          (str "/#/" lname)
           :leftIcon      (svg icon)})))

(rum/defc drawer < rum/reactive []
  (let [{:keys [opened]} (rum/react state)
        domain (rum/react ctrl/domain)
        drawer-container-style (if opened
                                 drawer-container-opened-style
                                 drawer-container-closed-style)
        drawer-appbar-icon (if opened
                             (material/icon-button nil (svg material/view-compact-icon))
                             (material/icon-button nil (svg material/view-confy-icon)))]
    (print domain)
    (material/theme
      (material/drawer
        #js {:open               opened
             :containerStyle     drawer-container-style
             :containerClassName "drawer-container"}
        (material/app-bar
          #js{:className                "drawer-appbar"
              :iconElementLeft          drawer-appbar-icon
              :onLeftIconButtonTouchTap (fn [] (swap! state assoc :opened (not opened)))})
        (material/menu #js {:style #js {:height   "100%"
                                        :overflow "auto"}}
                       (drawer-category "BUILD" opened)
                       (drawer-item "Repositories" material/repositories-icon opened)
                       (drawer-category "APPLICATIONS" opened)
                       ;(drawer-item "Stacks" material/stacks-icon opened)
                       (drawer-item "Services" material/services-icon opened)
                       (drawer-item "Containers" material/containers-icon opened)
                       (drawer-category "INFRASTRUCTURE" opened)
                       (drawer-item "Nodes" material/nodes-icon opened)
                       (drawer-item "Networks" material/networks-icon opened))))))