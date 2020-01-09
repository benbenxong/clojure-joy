;;for ����һ������seq, doseqֻΪ���������á�
(for [x [:a :b], y (range 5) :when (odd? y)]
  [x y])
(doseq [x [:a :b], y (range 5) :when (odd? y)]
  (prn x y))

(def numbers [1 2 3 4 5 6 7 8 9 10])
(apply + numbers)

;;;;; 2 ;;;;;;
;;���֡��ַ������ؼ�����������ֵ�ġ�

[127 0x7f 0177 32r3v 2r01111111]
[1.17 +1.22 -2. 366e7 32e-14 10.7e-3]

;;list () ;;��һ��Ϊ�������������form
;;vector [] 
;;map {1 "one", 2 "two"}
;;set {1 2 "three" :four}
;;(),[],{},#{}������nil

(vector 1 2 3)

;;var
(def x 42)

;;no name fn
(fn [x y]
     (println "Making a set")
     #{x y})

((fn [x y]
   (println "Making a set")
   #{x y}) 1 2)

;;Making named function
;;defn
(defn make-set
  "Take tow values and make a set from them."
  [x y]
  (println "Making a set")
  #{x y})

(defn make-set
  ([x] #{x})
  ([x y] #{x y}))

(defn arity2+ [first second & more]
  (vector first second more))

(arity2+ 1 2)
(arity2+ 1 2 3 4)
(arity2+ 1)

(def make-list0 #(list))
(def make-list2 #(list %1 %2))
(def make-list2+ #(list %1 %2 %&))

(do
  (def x 5)
  (def y 4)
  (+ x y)
  [x y])

(let [r 5
      pi 3.1415
      r-squ (* r  r)]
  (println "radius is " r)
  (* pi r-squ))

(defn print-down-from [x]
  (when (pos? x)
    (println x)
    (recur (dec x))))

(defn sum-down-from [sum x]
  (if (pos? x)
    (recur (+ sum x) (dec x))
    sum))
;;��when����������ʽ��do����Ҫ�����á�û��else�Ӿ䡣

(defn sum-down-from [init-x]
  (loop [sum 0 x init-x]
    (if (pos? x)
      (recur (+ sum x) (dec x))
      sum)))

;;quote
(cons 1 [ 2 3])
(cons 1 '(2 3))

;;�﷨quote `
`(1 2 3)
`map
;;clojure.core/map

;;��quote
`(+ 10 (* 3 2))
;;(+ 10 (* 3 2))
`(+ 10 ~(* 3 2))
;;(+ 10 6)
(let [x '(2 3)] `(1 ~x))

;;��quoteƴ��
(let [x '(2 3)] `(1 ~@x))
;;(1 2 3)

;;��̬������java.lang��
(Math/sqrt 9)
;;3.0

;;�����ú��ļ������ʼ��java������
;;�����.��Ҫ���ù��캯����
(new java.util.HashMap {"foo" 42 "bar" 9})
(java.util.HashMap. {"foo" 42,"bar" 9})

;;.���������javaʵ����Ա��
;;����public��������Ҫ������ǰ����.�����ַ�-
(.-x (java.awt.Point. 10 20))
(.x (java.awt.Point. 10 20))

;;ʵ����Ϊ��һ��������
(.divide (java.math.BigDecimal. "42") 2M)

;;����javaʵ������
(let [origin (java.awt.Point. 0 0)]
  (set! (.-x origin) 15)
  (str origin))

;;(.endsWith (.toString (java.util.Date.)) "2020")
;;..��
(.. (java.util.Date.) toString (endsWith "2020"))

;;doto ��
(doto (java.util.HashMap.)
  (.put "HOME" "/home/me")
  (.put "SRC" "src")
  (.put "BIN" "classes"))

;;�쳣����
(throw (Exception. "I done throwd"))

(defn throw-catch [f]
  [(try 
     (f)
     (catch ArithmeticException e "No dividing by zero!")
     (catch Exception e (str "You are so bad " (.getMessage e)))
     (finally (println "returning... ")))])
(throw-catch #(/ 10 5))
(throw-catch #(/ 10 0))
(throw-catch #(throw (Exception. "Crybaby")))

;;�����ռ�
(ns joy.ch2)

;;��joy.ch2�ﶨ�庯��
(defn report-ns []
  (str "The current namespace is " *ns*))
;;(ns joy.another)��report-ns���ɼ�
;;��joy.ch2/report-ns�ɼ�������Ҫ��joy.ch2��
;;ʽ���ع���:require���ع����С�

;;:require ��ռ�
(ns joy.req-alias 
  (:require [clojure.set :as s]))

;;:refer ӳ�䡣ֻ��capitalize������ӳ�䵽�ˡ�
;;���ܿ���ʹ��:refer :all��:use ������������ô����
(ns joy.use-ex
  (:require [clojure.string :refer (capitalize)]))
(map capitalize ["kilgore" "trout"])

;; referҲ����
(ns joy.yet-another
  (:refer joy.ch2))

(ns joy.yet-another
  (:refer clojure.set :rename {union onion}))

;;:import ���� java �ࡣjava.lang���Զ����롣
(ns joy.java
  (:import [java.util HashMap]
           [java.util.concurrent.atomic AtomicLong]))
(HashMap. {"happy?" true})

;;;; 3 ;;;;;

