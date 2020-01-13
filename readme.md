;; emacs 中文卡问题
;; Setting English Font
(set-face-attribute
 'default nil :font "Courier New-14")
;; Setting Chinese Font
(dolist (charset '(kana han symbol cjk-misc bopomofo))
(set-fontset-font (frame-parameter nil 'font)
charset
(font-spec :family "Microsoft Yahei" :size 16)))
