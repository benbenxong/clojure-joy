
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
;;(ns joy.ch2)

;;在joy.ch2里定义函数
(defn report-ns []
  (str "The current namespace is " *ns*))
;;(ns joy.another)后，report-ns不可见
;;但joy.ch2/report-ns可见，但这要求joy.ch2隐
;;式加载过或:require加载过才行。
(ns user)
;;:require 表空间
(ns joy.req-alias 
  (:require [clojure.set :as s])
  )

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

;;;;; 4 ;;;;;;;
;; 关键字 做键值
(def population {:zombies 2700,:humans 9})
(get population :zombies)

;; 关键字做函数
(:zombies population)

;; 作为枚举

;; 作为多重方法分发值

;; 作为指令
(defn pour [lb ub]
  (cond 
   (= ub :toujours) (iterate inc lb)
   :else (range lb ub)))

(pour 1 :toujours)
;; ...runs forever
;; :else 起到真值的作用。

;; 限定关键字
::not-in-ns
;;=> :user/not-in-ns

;; 符号解析
(identical? 'got 'got)
;;=> false
(identical? :got :got)
;;=> true
(= 'got 'got)
;;=> true

;;元数据
(let [x (with-meta 'goat {:ornery true})
      y (with-meta 'goat {:ornery false})]
  [(= x y)
   (identical? x y)
   (meta x)
   (meta y)])

;; lisp-1
(defn best [f xs]
  (reduce #(if (f %1 %2) %1 %2) xs))

;;正则表达式
(class #"example")
;;=>java.util.regex.Pattern

;; re-seq 
(re-seq #"\w+" "one-two/three")

;; re-seq cap-group
(re-seq #"\w+(\w)" "one-two/three")

;;;;; 5 ;;;;;;
;; 持久性
(def ds [:willie :barnabas :adam])
(def ds1 (replace {:barnabas :quentin} ds))
;;=> ds 没有变

;; seq api
;; first,rest nil,()
(seq []) 
(seq [1 2])

;; 等价划分
(= [1 2 3] '(1 2 3))
;; => true 因为它们都属于顺序的。
(= [1 2 3] #{1 2 3})
;; => false 因为一个属于set

;; 序列抽象
(seq (hash-map :a 1))
;;=> ([:a 1])
(seq (keys (hash-map :a 1)))
;;=> (:a)

;; 构建vector
(vec (range 10))

(let [my-vector [:a :b :c]]
  (into my-vector (range 10)))
;;如果想返回一个vector,into第一个参数必是vector.

;; 原生类型vector
(into (vector-of :int) [Math/PI 2 4.6])
;;=>[3 2 4]

;; vector有3个方面特别高效：1.右端添加删除；2.数字索引；3.反向遍历
(def a-to-j (vec (map char (range 65 75))))
(nth a-to-j 4)
(get a-to-j 4)
(a-to-j 4)
;;               nth        get      vector函数
;; vector为nil   ret nil    nil       error
;; 索引越界        error      nil       error
;; 支持未找到实参加  是          是         否
;;               (nth [] 11 :whoops)
(seq a-to-j)
(rseq a-to-j)
(assoc a-to-j 4 "no longer E")
(replace {2 :a 4 :b} [1 2 3 2 3 4])

(def matrix
  [[1 2 3]
   [4 5 6]
   [7 8 9]])
(get-in matrix [1 2])
;;=> 6
(assoc-in matrix [1 2] 'x)
(update-in matrix [1 2] * 100)

;;neighbors函数
(defn neighbors
  ([size yx] (neighbors [[-1 0] [1 0] [0 -1] [0 1]]
                        size
                        yx))
  ([deltas size yx]
   (filter (fn [new-yx]
             (every? #(< -1 % size) new-yx))
           (map #(vec (map + yx %))
                deltas))))

;; 可以用get-in找到具体项
(map #(get-in matrix %) (neighbors 3 [0 0]))

;; vector 当作栈
;; push/pop conj/pop
(def my-stack [1 2 3])
(peek my-stack)
;;=>3
(pop my-stack)
;;=>[1 2]
(conj my-stack 4)
;;=> [1 2 3 4]
(+ (peek my-stack) (peek (pop my-stack)))
;;=> 5

;; 使用vector而非reverse
(defn strict-map1 [f coll]
  (loop [coll coll, acc nil]
    (if (empty? coll)
      (reverse acc)
      (recur (next coll)
             (cons (f (first coll)) acc)))))

(strict-map1 - (range 5))

(defn strict-map2 [f coll]
  (loop [coll coll, acc []]
    (if (empty? coll)
      acc
      (recur (next coll)
             (conj acc (f (first coll)))))))
(strict-map2 - (range 5))

;; 子vector 是建立在原vector之上的
(subvec a-to-j 3 6)
;;=> [d e f]

;; vector当作MapEntry
(first {:w 10 :h 20 :d 15})
(rest {:w 10 :h 20 :d 15})
(doseq [[dimension amount] {:w 10 :h 20 :d 15}]
  (println (str (name dimension) ":") amount))

;; vector不是什么
;; 1.vector不可以插入删除，只能在尾部这样做。
;; 2.vector不是队列。用PersistentQueue
;; 3.vector不是set.contains?问的是集合中键值，而非值。

;; list ;;
(cons 1 '(2 3))
(conj '(2 3) 1)
;;正确方式是conj

;; list也可当作栈
;; peek pop (first / next)

;; list 不是什么
;; list 不能基于索引查找，这里vector做得很好。(nth coll n)
;; list 不是set
;; list 不是队列

;; 如何使用持久化队列
(defmethod print-method clojure.lang.PersistentQueue
  [q, w] ;;重载打印函数，打印的格式像条鱼
  (print-method '<- w)
  (print-method (seq q) w) ;;代理其他方法，打印队列内容
  (print-method '-< w))

clojure.lang.PersistentQueue/EMPTY

;; vector做队列的简易做法：
(def my-vec [1 2 3])
(peek my-vec)
(pop my-vec)
(conj my-vec 4)

;; 入队
(def schedule
  (conj clojure.lang.PersistentQueue/EMPTY
        :wake-up :shower :brush-teeth))
schedule
;; 获取
(peek schedule)
;; 出队
(pop schedule)
(rest schedule)
;;rest 返回的是seq，而非队列

;; 持久化set
;; 集合，无序且唯一
(#{:a :b :c :d} :c)
(get #{:a 1 :b 2} :z :nothing-doing)
;; 如果两个元素求值结果是相等的，那么set保持一个。与具体类型无关。
(into #{[]} [()])
(into #{#{} {} []} [()])

;;使用set和some在一个序列中查找项
(some #{1 :b} [:a 1 :b 2])
;;=> 1
;;set里任意值在序列里，这是一种惯用法。

;;contains 查询set中的值。
(contains? #{1 2 3 4} 4)
;;=>true
(contains? [1 2 4 3] 4)
;;=>false

;;5.5.4 clojure.set
(require 'clojure.set)
(clojure.set/intersection #{} #{})
(clojure.set/union #{} #{})
(clojure.set/difference #{} #{})

;;5.6 思考map
(hash-map :a 1 :b 2 :c 3)
(let [m {:a 1, 1 :b, [1 2 3] "4 5 6"}]
  [(get m :a) (get m [1 2 3])])
(seq {:a 1 :b 2})
(into {} [[:a 1] [:b 2]])
(into {} (map vec '[(:a 1) (:b 2)]))
(apply hash-map [:a 1 :b 2])
(zipmap [:a :b] [2 1])

(sorted-map :rt 11 :tr 2)
(sorted-map-by #(compare (subs %1 1) (subs %2 1)) "bac" 2 "adc" 3 "ccc" 4)

;; 用array-map 保证插入顺序
(seq (hash-map :a 1 :b 2 :c 3))
(seq (array-map :a 1 :b 2 :c 3))

;;5.7 pos函数设计
;;对顺序集合返回数字索引，对map,set返回关键字，否则返回nil.
(defn pos [e coll]
  (let [cmp (if (map? coll)
              #(= (second %1) %2)
              #(= %1 %2))]
    (loop [s coll idx 0]
      (when (seq s)
        (if (cmp (first s) e)
          (if (map? coll)
            (first (first s))
            idx)
          (recur (next s) (inc idx)))))))

(defn index [coll]
  (cond
   (map? coll) (seq coll)
   (set? coll) (map vector coll coll)
   :else (map vector (iterate inc 0) coll)))
(defn pos [e coll]
  (for [[i v] (index coll) :when (= e v)] i))

(defn pos [pred coll]
  (for [[i v] (index coll) :when (pred v)] i))
