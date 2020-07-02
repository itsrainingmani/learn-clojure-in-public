(ns ground-up.rocket
  (:gen-class))

(defn -main
  "This program does nothing"
  [& args]
  (println "Welcome to Chapter 8 - Modeling a Rocket in Flight"))

;; Representing the state of a Rocket
;; Data - weight, fuel mass, position, velocity, time

(defn atlas-v
  []
  {:dry-mass      50050
   :fuel-mass     284450
   :time          0
   :isp           3050
   :max-fuel-rate (/ 284450 253)
   :max-thrust    4.152e6})

;; Calculate mass
(defn mass
  "The total mass of a craft"
  [craft]
  (+ (:dry-mass craft) (:fuel-mass craft)))

(def earth-equatorial-radius
  "Radius of the earth, in meter"
  6378137)

(def earth-day
  "Length of an earth day in seconds"
  86400)

;; Launch pad is at equator y=0
(def earth-equatorial-speed
  "How fast a point on the equator moves, relative to the center of the earth"
  (/ (* 2 Math/PI earth-equatorial-radius)
     earth-day))

;; 

(def initial-space-center
  "The initial position and velocity of the launch facility"
  {:time     0
   :position {:x earth-equatorial-radius
              :y 0
              :z 0}
   :velocity {:x 0
              :y earth-equatorial-speed
              :z 0}})


(defn prepare
  "Prepares a craft for launch from an equatorial space center"
  [craft]
  (merge craft initial-space-center))

;; Forces
;; Gravity pulls the rocket towards the earth at a constant rate 9.8 m/s.

(defn magnitude
  "What's the radius of a given set of cartesian coordinates"
  [c]
  (Math/sqrt (+ (Math/pow (:x c) 2)
                (Math/pow (:y c) 2)
                (Math/pow (:z c) 2))))

(defn cart->sphere
  "Converts a map of cartesian coords to spherical coords :r :theta :phi"
  [c]
  (let [r (magnitude c)]
    {:r     r
     :phi   (Math/acos (/ (:z c) r))
     :theta (Math/atan (/ (:y c) (:x c)))}))

(defn sphere->cart
  "Converts a map of spherical coords to cartesian"
  [c]
  {:x (* (:r c) (Math/sin (:theta c)) (Math/cos (:phi c)))
   :y (* (:r c) (Math/sin (:theta c)) (Math/sin (:phi c)))
   :z (* (:r c) (Math/cos (:phi c)))})

;; Computing the gravitational acceleration is easy. Take the spherical coordinates of the spacecraft and replace the radius with the total force due to gravity. Then we can transform that spherical force back into Cartesian coordinates

(def g -9.8)

(defn gravity-force
  "The force vector, each component in Newtons, due to gravity"
  [craft]
  (let [total-force (* g (mass craft))]
    (-> craft
        :position              ; Take the craft's pos
        cart->sphere           ; Convert to spherical
        (assoc :r total-force) ; Replace the radius with gravitational force
        sphere->cart)))        ; Convert back to Cartesian

;; Rockets consume fuel to produce thrust.
(defn fuel-rate
  "How fast is fuel, in kg/s, consumed by the craft"
  [craft]
  (if (pos? (:fuel-mass craft))
    (:max-fuel-rate craft)
    0))

;; Compute the force the rocket produces. The force is mass consumed per second times the exhaust velocity (the specific impulse :isp)

(defn thrust
  "Force in Newtons produced by the rocket engines"
  [craft]
  (* (fuel-rate craft) (:isp craft)))

(defn engine-force
  "The force vector, each component in Newton, due to the rocket engine"
  [craft]
  (let [t (thrust craft)]
    {:x t
     :y 0
     :z 0}))

;; Total force on a craft is sum of gravity and thrust. merge-with combines common fields in maps using a function.

(defn total-force
  [craft]
  (merge-with +
              (engine-force craft)
              (gravity-force craft)))

;; Acceleration is force divided by mass. Given {:x 1 :y 2 :z 4} we want to apply a function to each number.

;; Convert a map into a seq with (seq {:x 1 :y 2 :z 4}) => [[:z 3] [:y 2] [:x 1]]
;; Convert a key value pair into a map with (into {} [[:x 4] [:y 5]]

(defn map-values
  "Applies f to every value in map m"
  [f m]
  (into {}
        (map (fn [pair]
               [(key pair) (f (val pair))])
             m)))

;; Scale a set of coordinates by some factor
(defn scale
  [factor coordinates]
  (map-values (partial * factor) coordinates))

;; partial takes a function and some arguments and returns a new function. This new function calls the original function with the orignal arguments passed to partial along with the args passed to the new function.

;; Args are in order. New arg comes at the end

(defn acceleration
  [craft]
  (let [m (mass craft)]
    (scale (/ m) (total-force craft))))

;; Function to apply changes in acceleration and fuel consumption over time

(defn step
  [craft dt]
  (assoc craft
         :time (+ dt (:time craft))
         :fuel-mass (- (:fuel-mass craft) (* dt (fuel-rate craft)))
         :position (merge-with +
                               (:position craft)
                               (scale dt (:velocity craft)))
         :velocity (merge-with +
                               (:velocity craft)
                               (scale dt (acceleration craft)))))

;; (use 'ground-up.rocket :reload)
;; use is a shorthand for (:require ... :refer :all)
;; :reload asks the repl to re-read the file

;; (pst *e) to print the stackstrace of the current exception

;; Flight

(defn trajectory
  [dt craft]
  "Returns all future states of the craft, at dt intervals"
  (iterate #(step % 1) craft))

(defn altitude
  "Height of the rocket above surface of the earth"
  [craft]
  (-> craft
      :position
      cart->sphere
      :r
      (- earth-equatorial-radius)))

(defn above-ground?
  "Is the craft at or above the surface?"
  [craft]
  (<= 0 (altitude craft)))

(defn flight
  "The above-ground portion of a trajectory."
  [trajectory]
  (take-while above-ground? trajectory))

(defn crashed?
  "Does this trajectory crash into the surface before 100 hours are up?"
  [trajectory]
  (let [time-limit (* 100 3600)] ; 1 hour
    (not (every? above-ground?
                 (take-while #(<= (:time %) time-limit) trajectory)))))

(defn crash-time
  "Given a trajectory, returns the time the rocket impacted the ground."
  [trajectory]
  (:time (last (flight trajectory))))

(defn apoapsis
  "The highest altitude achieved during a trajectory."
  [trajectory]
  (apply max (map altitude trajectory)))

(defn apoapsis-time
  "The time of apoapsis"
  [trajectory]
  (:time (apply max-key altitude (flight trajectory))))
