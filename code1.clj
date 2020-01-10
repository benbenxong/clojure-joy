;; emacs 中文卡问题
;; Setting English Font
(set-face-attribute
 'default nil :font "Courier New-14")
;; Setting Chinese Font
(dolist (charset '(kana han symbol cjk-misc bopomofo))
(set-fontset-font (frame-parameter nil 'font)
charset
(font-spec :family "Microsoft Yahei" :size 16)))

;;for 返回一个惰性seq, doseq只为产生副作用。
(for [x [:a :b], y (range 5) :when (odd? y)]
  [x y])
(doseq [x [:a :b], y (range 5) :when (odd? y)]
  (prn x y))

(def numbers [1 2 3 4 5 6 7 8 9 10])
(apply + numbers)

;;;;; 2 ;;;;;;
;;数字、字符串、关键字是自我求值的。

[127 0x7f 0177 32r3v 2r01111111]
[1.17 +1.22 -2. 366e7 32e-14 10.7e-3]

;;list () ;;第一项为函数、宏或特殊form
;;vector [] 
;;map {1 "one", 2 "two"}
;;set {1 2 "three" :four}
;;(),[],{},#{}都不是nil

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
;;用when的条件：隐式的do，需要副作用。没有else子句。

(defn sum-down-from [init-x]
  (loop [sum 0 x init-x]
    (if (pos? x)
      (recur (+ sum x) (dec x))
      sum)))

;;quote
(cons 1 [ 2 3])
(cons 1 '(2 3))

;;语法quote `
`(1 2 3)
`map
;;clojure.core/map

;;反quote
`(+ 10 (* 3 2))
;;(+ 10 (* 3 2))
`(+ 10 ~(* 3 2))
;;(+ 10 6)
(let [x '(2 3)] `(1 ~x))

;;反quote拼接
(let [x '(2 3)] `(1 ~@x))
;;(1 2 3)

;;静态方法，java.lang下
(Math/sqrt 9)
;;3.0

;;可以用核心集合类初始化java集合类
;;后面加.是要调用构造函数。
(new java.util.HashMap {"foo" 42 "bar" 9})
(java.util.HashMap. {"foo" 42,"bar" 9})

;;.运算符访问java实例成员。
;;返回public变量，需要在属性前加上.和连字符-
(.-x (java.awt.Point. 10 20))
(.x (java.awt.Point. 10 20))

;;实例作为第一个参数。
(.divide (java.math.BigDecimal. "42") 2M)

;;设置java实例属性
(let [origin (java.awt.Point. 0 0)]
  (set! (.-x origin) 15)
  (str origin))

;;(.endsWith (.toString (java.util.Date.)) "2020")
;;..宏
(.. (java.util.Date.) toString (endsWith "2020"))

;;doto 宏
(doto (java.util.HashMap.)
  (.put "HOME" "/home/me")
  (.put "SRC" "src")
  (.put "BIN" "classes"))

;;异常处理
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

;;命名空间
(ns joy.ch2)

;;在joy.ch2里定义函数
(defn report-ns []
  (str "The current namespace is " *ns*))
;;(ns joy.another)后，report-ns不可见
;;但joy.ch2/report-ns可见，但这要求joy.ch2隐
;;式加载过或:require加载过才行。

;;:require 表空间
(ns joy.req-alias 
  (:require [clojure.set :as s]))

;;:refer 映射。只有capitalize函数能映射到此。
;;尽管可以使用:refer :all或:use 。但不建议这么做。
(ns joy.use-ex
  (:require [clojure.string :refer (capitalize)]))
(map capitalize ["kilgore" "trout"])

;; refer也可以
(ns joy.yet-another
  (:refer joy.ch2))

(ns joy.yet-another
  (:refer clojure.set :rename {union onion}))

;;:import 加载 java 类。java.lang包自动导入。
(ns joy.java
  (:import [java.util HashMap]
           [java.util.concurrent.atomic AtomicLong]))
(HashMap. {"happy?" true})

;;;; 3 ;;;;;

;;不要建boolean 对象
(def evil-false (Boolean. "false"))
(if evil-false :true :false)
;;=> :true

;; 正确作法
(if (Boolean/valueOf "false") :true :false)
;;=> :false

;; 除了nil和false 都是 true
;; nil? false?
(when (nil? nil) "nil, not false")

;; nil双关
(seq [])
;;=> nil

(defn print-seq [s]
  (when (seq s)
    (prn (first s))
    (recur (rest s))))
;; rest([]) 返回 ()

;; 解构
(let [[a b c & more] (range 10)]
  (println "a b c are:" a b c)
  (println "more is: " more))

(let [range-vec (vec (range 10))
      [a b c & more :as all] range-vec]
  (println "a b c : " a b c)
  (println "more : " more)
  (println "all :" all))

(def guys-name-map
  {:f-name "Gug" :m-name "Lewis" :l-name "Steele"})
(let [{f-name :f-name, m-name :m-name, l-name :l-name} guys-name-map]
  (str l-name ", " f-name " " m-name))

;; 解构出来的符号与键值同名
(let [{:keys [f-name l-name m-name]} guys-name-map]
  (str l-name ", " f-name " " m-name))

(let [{f-name :f-name :as whole-name} guys-name-map]
  (println "First name is " f-name)
  (println "Whole name is below:")
  whole-name)

;; 未绑定的符号提供默认值:
(let [{:keys [title f-name m-name l-name],
       :or {title "Mr."}} guys-name-map]
  (println title f-name m-name l-name))

;; 所有用于map结构也适用于list
(defn whole-name [& args]
  (let [{:keys [f-name m-name l-name]} args]
    (str l-name ", " f-name " " m-name)))
(whole-name :f-name "Guy" :m-name "Lewis" :l-name "Steele")

;; map 解构 list
(let [{first-thing 0, last-thing 3} [1 2 3 4]]
  [first-thing last-thing])

;; 解构函数参数
(defn print-last-name [{:keys [l-name]}]
  (println l-name))
(print-last-name guys-name-map)

;; 实例
(for [x (range 2) y (range 2)] [x y])

(find-doc "xor");;只能在交互模式下执行
