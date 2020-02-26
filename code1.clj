
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
;;(ns joy.ch2)

;;��joy.ch2�ﶨ�庯��
(defn report-ns []
  (str "The current namespace is " *ns*))
;;(ns joy.another)��report-ns���ɼ�
;;��joy.ch2/report-ns�ɼ�������Ҫ��joy.ch2��
;;ʽ���ع���:require���ع����С�
(ns user)
;;:require ��ռ�
(ns joy.req-alias 
  (:require [clojure.set :as s])
  )

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

;;��Ҫ��boolean ����
(def evil-false (Boolean. "false"))
(if evil-false :true :false)
;;=> :true

;; ��ȷ����
(if (Boolean/valueOf "false") :true :false)
;;=> :false

;; ����nil��false ���� true
;; nil? false?
(when (nil? nil) "nil, not false")

;; nil˫��
(seq [])
;;=> nil

(defn print-seq [s]
  (when (seq s)
    (prn (first s))
    (recur (rest s))))
;; rest([]) ���� ()

;; �⹹
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

;; �⹹�����ķ������ֵͬ��
(let [{:keys [f-name l-name m-name]} guys-name-map]
  (str l-name ", " f-name " " m-name))

(let [{f-name :f-name :as whole-name} guys-name-map]
  (println "First name is " f-name)
  (println "Whole name is below:")
  whole-name)

;; δ�󶨵ķ����ṩĬ��ֵ:
(let [{:keys [title f-name m-name l-name],
       :or {title "Mr."}} guys-name-map]
  (println title f-name m-name l-name))

;; ��������map�ṹҲ������list
(defn whole-name [& args]
  (let [{:keys [f-name m-name l-name]} args]
    (str l-name ", " f-name " " m-name)))
(whole-name :f-name "Guy" :m-name "Lewis" :l-name "Steele")

;; map �⹹ list
(let [{first-thing 0, last-thing 3} [1 2 3 4]]
  [first-thing last-thing])

;; �⹹��������
(defn print-last-name [{:keys [l-name]}]
  (println l-name))
(print-last-name guys-name-map)

;; ʵ��
(for [x (range 2) y (range 2)] [x y])

(find-doc "xor");;ֻ���ڽ���ģʽ��ִ��

;;;;; 4 ;;;;;;;
;; �ؼ��� ����ֵ
(def population {:zombies 2700,:humans 9})
(get population :zombies)

;; �ؼ���������
(:zombies population)

;; ��Ϊö��

;; ��Ϊ���ط����ַ�ֵ

;; ��Ϊָ��
(defn pour [lb ub]
  (cond 
   (= ub :toujours) (iterate inc lb)
   :else (range lb ub)))

(pour 1 :toujours)
;; ...runs forever
;; :else ����ֵ�����á�

;; �޶��ؼ���
::not-in-ns
;;=> :user/not-in-ns

;; ���Ž���
(identical? 'got 'got)
;;=> false
(identical? :got :got)
;;=> true
(= 'got 'got)
;;=> true

;;Ԫ����
(let [x (with-meta 'goat {:ornery true})
      y (with-meta 'goat {:ornery false})]
  [(= x y)
   (identical? x y)
   (meta x)
   (meta y)])

;; lisp-1
(defn best [f xs]
  (reduce #(if (f %1 %2) %1 %2) xs))

;;������ʽ
(class #"example")
;;=>java.util.regex.Pattern

;; re-seq 
(re-seq #"\w+" "one-two/three")

;; re-seq cap-group
(re-seq #"\w+(\w)" "one-two/three")

;;;;; 5 ;;;;;;
;; �־���
(def ds [:willie :barnabas :adam])
(def ds1 (replace {:barnabas :quentin} ds))
;;=> ds û�б�

;; seq api
;; first,rest nil,()
(seq []) 
(seq [1 2])

;; �ȼۻ���
(= [1 2 3] '(1 2 3))
;; => true ��Ϊ���Ƕ�����˳��ġ�
(= [1 2 3] #{1 2 3})
;; => false ��Ϊһ������set

;; ���г���
(seq (hash-map :a 1))
;;=> ([:a 1])
(seq (keys (hash-map :a 1)))
;;=> (:a)

;; ����vector
(vec (range 10))

(let [my-vector [:a :b :c]]
  (into my-vector (range 10)))
;;����뷵��һ��vector,into��һ����������vector.

;; ԭ������vector
(into (vector-of :int) [Math/PI 2 4.6])
;;=>[3 2 4]

;; vector��3�������ر��Ч��1.�Ҷ����ɾ����2.����������3.�������
(def a-to-j (vec (map char (range 65 75))))
(nth a-to-j 4)
(get a-to-j 4)
(a-to-j 4)
;;               nth        get      vector����
;; vectorΪnil   ret nil    nil       error
;; ����Խ��        error      nil       error
;; ֧��δ�ҵ�ʵ�μ�  ��          ��         ��
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

;;neighbors����
(defn neighbors
  ([size yx] (neighbors [[-1 0] [1 0] [0 -1] [0 1]]
                        size
                        yx))
  ([deltas size yx]
   (filter (fn [new-yx]
             (every? #(< -1 % size) new-yx))
           (map #(vec (map + yx %))
                deltas))))

;; ������get-in�ҵ�������
(map #(get-in matrix %) (neighbors 3 [0 0]))

;; vector ����ջ
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

;; ʹ��vector����reverse
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

;; ��vector �ǽ�����ԭvector֮�ϵ�
(subvec a-to-j 3 6)
;;=> [d e f]

;; vector����MapEntry
(first {:w 10 :h 20 :d 15})
(rest {:w 10 :h 20 :d 15})
(doseq [[dimension amount] {:w 10 :h 20 :d 15}]
  (println (str (name dimension) ":") amount))

;; vector����ʲô
;; 1.vector�����Բ���ɾ����ֻ����β����������
;; 2.vector���Ƕ��С���PersistentQueue
;; 3.vector����set.contains?�ʵ��Ǽ����м�ֵ������ֵ��

;; list ;;
(cons 1 '(2 3))
(conj '(2 3) 1)
;;��ȷ��ʽ��conj

;; listҲ�ɵ���ջ
;; peek pop (first / next)

;; list ����ʲô
;; list ���ܻ����������ң�����vector���úܺá�(nth coll n)
;; list ����set
;; list ���Ƕ���

;; ���ʹ�ó־û�����
(defmethod print-method clojure.lang.PersistentQueue
  [q, w] ;;���ش�ӡ��������ӡ�ĸ�ʽ������
  (print-method '<- w)
  (print-method (seq q) w) ;;����������������ӡ��������
  (print-method '-< w))

clojure.lang.PersistentQueue/EMPTY

;; vector�����еļ���������
(def my-vec [1 2 3])
(peek my-vec)
(pop my-vec)
(conj my-vec 4)

;; ���
(def schedule
  (conj clojure.lang.PersistentQueue/EMPTY
        :wake-up :shower :brush-teeth))
schedule
;; ��ȡ
(peek schedule)
;; ����
(pop schedule)
(rest schedule)
;;rest ���ص���seq�����Ƕ���

;; �־û�set
;; ���ϣ�������Ψһ
(#{:a :b :c :d} :c)
(get #{:a 1 :b 2} :z :nothing-doing)
;; �������Ԫ����ֵ�������ȵģ���ôset����һ��������������޹ء�
(into #{[]} [()])
(into #{#{} {} []} [()])

;;ʹ��set��some��һ�������в�����
(some #{1 :b} [:a 1 :b 2])
;;=> 1
;;set������ֵ�����������һ�ֹ��÷���

;;contains ��ѯset�е�ֵ��
(contains? #{1 2 3 4} 4)
;;=>true
(contains? [1 2 4 3] 4)
;;=>false

;;5.5.4 clojure.set
(require 'clojure.set)
(clojure.set/intersection #{} #{})
(clojure.set/union #{} #{})
(clojure.set/difference #{} #{})

;;5.6 ˼��map
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

;; ��array-map ��֤����˳��
(seq (hash-map :a 1 :b 2 :c 3))
(seq (array-map :a 1 :b 2 :c 3))

;;5.7 pos�������
;;��˳�򼯺Ϸ���������������map,set���عؼ��֣����򷵻�nil.
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

;; 6 ;;;;
(defn xconj [t v] ;;����ֵΪv����
  (cond
   (nil? t) {:val v, :L nil, :R nil}
   (< v (:val t)) {:val (:val t),
                   :L (xconj (:L t) v),
                   :R (:R t)}
   :else {:val (:val t),
          :L (:L t),
          :R (xconj (:R t) v)}))

(xconj nil 5)
(def tree1 (xconj nil 5))
(def tree1 (xconj tree1 3))
(def tree1 (xconj tree1 2))
(defn xseq [t]
  (when t
    (concat (xseq (:L t)) [(:val t)] (xseq (:R t)))))
(xseq tree1)
(def tree2 (xconj tree1 7))
(identical? (:L tree1) (:L tree2))

;; 6.3 ����
;;lazy-seq
(defn lz-rec-step [s]
  (lazy-seq
   (if (seq s)
     [(first s) (lz-rec-step (rest s))]
     [])))
(lz-rec-step [1 2 3 4])
(class (lz-rec-step [1 2 3 4]))
(dorun (lz-rec-step (range 200000)))
;; ����ͷ
(let [r (range 1e9)]
  (first r)
  (last r))
(let [r (range 1e9)]
  (last r)
  (first r))
;; ����ڴ��������Ϊlast r

;; ��������
(defn triangle [n]
  (/ (* n (+ n 1)) 2))
(triangle 10)
(map triangle (range 1 11))

(def tri-nums (map triangle (iterate inc 1)))
(take 10 tri-nums)
(take 10 (filter even? tri-nums))
(nth tri-nums 99)
(double (reduce + (take 1000 (map / tri-nums))))
(take 2 (drop-while #(< % 10000) tri-nums))

;; 6.3.5 delay �� force��
