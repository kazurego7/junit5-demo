# README.md

junit5の他に、いろいろ試したいことを詰め込んだリポジトリ  
後で分割するかも、知らんけど

## 定数値の永続化

### 背景

定数値を文字列で直接扱いたくない
```java
// 何を比較しているのか一目で分からない
userEntity.getUserType().equals("A") // ユーザー種別が一般ユーザー("A")であるか比較

// 文字列の比較は参照型の比較のため「==」演算子が利用できない
userEntity.setUserType("A")
userEntity.getUserType() == "A" // false

// equalsメソッドは null セーフでない
userEntity.setUserType(null)
userEntity.getUserType().equals("A") // ぬるぽ
```

### 目的
1. 定数値を文字列でなく型で表現する
2. null セーフな定数値型の比較を行う
3. 定数値をデータベースに保存する際には文字列に変換して保存する

### 手法と比較
1. @Enumerated: Enum に対するマッピングを決める
   - EnumType.ORDINAL：Enum.ordinal() でマッピング
   - EnumType.STRING：Enum.name() でマッピング
   - 上記いずれも、Enum の保持する文字列をデータベースに変換して保存ができないため不適
2. Enum と String を相互に変換する Converter を作る
   - 欠点: ボイラーテンプレートなコードが必要になる

=> `2. Enum と String を相互に変換する Converter を作る` で決定

### 実装例
[kazurego7.junit5demo.domain.value.UserType](src\main\java\kazurego7\junit5demo\domain\value\UserType.java) を参照

### 利用例
```java
// ユーザー種別が一般ユーザーであるか比較
userEntity.setUserType(UserType.GENERAL)
userEntity.getUserType() == UserType.GENERAL // true

// null が入ってもOK
userEntity.setUserType(null)
userEntity.getUserType() == UserType.GENERAL // false
```