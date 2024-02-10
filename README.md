# README.md

junit5の他に、いろいろ試したいことを詰め込んだリポジトリ  
後で分割するかも、知らんけど

- [1. 定数値の永続化](#1-定数値の永続化)
  - [1.1. 背景](#11-背景)
  - [1.2. 目的](#12-目的)
  - [1.3. 手法と比較](#13-手法と比較)
  - [1.4. 実装例](#14-実装例)
  - [1.5. 利用例](#15-利用例)
- [2. 入力バリデーションのテスト](#2-入力バリデーションのテスト)
  - [2.1. 背景と目的](#21-背景と目的)
  - [2.2. 実装例](#22-実装例)
  - [2.3. 注目ポイント](#23-注目ポイント)
    - [2.3.1. `@SpringBootTest`と`@Autowired`によるDI](#231-springboottestとautowiredによるdi)
    - [2.3.2. `@SpringBootTest`でBean生成をバリデーション構成に絞る](#232-springboottestでbean生成をバリデーション構成に絞る)
    - [2.3.3. `jakarta.validation.Validator`を直接定義](#233-jakartavalidationvalidatorを直接定義)
- [テストの命名](#テストの命名)
  - [アンチパターンと指針](#アンチパターンと指針)
  - [日本語を使うことの是非](#日本語を使うことの是非)


## 1. 定数値の永続化

### 1.1. 背景

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

### 1.2. 目的
1. 定数値を文字列でなく型で表現する
2. null セーフな定数値型の比較を行う
3. 定数値をデータベースに保存する際には文字列に変換して保存する

### 1.3. 手法と比較
1. @Enumerated: Enum に対するマッピングを決める
   - EnumType.ORDINAL：Enum.ordinal() でマッピング
   - EnumType.STRING：Enum.name() でマッピング
   - 上記いずれも、Enum の保持する文字列をデータベースに変換して保存ができないため不適
2. Enum と String を相互に変換する Converter を作る
   - 欠点: ボイラーテンプレートなコードが必要になる

=> `2. Enum と String を相互に変換する Converter を作る` で決定

### 1.4. 実装例
[kazurego7.junit5demo.domain.valueObject](src\main\java\kazurego7\junit5demo\domain\valueObject\UserType.java) を参照

### 1.5. 利用例
```java
// ユーザー種別が一般ユーザーであるか比較
userEntity.setUserType(UserType.GENERAL)
userEntity.getUserType() == UserType.GENERAL // true

// null が入ってもOK
userEntity.setUserType(null)
userEntity.getUserType() == UserType.GENERAL // false
```

## 2. 入力バリデーションのテスト

### 2.1. 背景と目的
- controller のテストは Integration Test (結合テスト)で実施する
- しかし、controller の入力バリデーションのみで単体テストをしたい

### 2.2. 実装例
[kazurego7.junit5demo.controller.model](src\test\java\kazurego7\junit5demo\controller\model\CreateUserRequestBodyTest.java) を参照

### 2.3. 注目ポイント
テスト実行の高速化を図っている

1. `@SpringBootTest`と`@Autowired`によるDI
2. `@SpringBootTest`でBean生成をバリデーション構成に絞る
3. `jakarta.validation.Validator`を直接定義

**最速は「3.`jakarta.validation.Validator`を直接定義」**

#### 2.3.1. `@SpringBootTest`と`@Autowired`によるDI
```java
@SpringBootTest()
public class CreateUserRequestBodyTest {

        @Autowired
        private Validator validator;
```

```log
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.932 s -- in kazurego7.junit5demo.controller.model.CreateUserRequestBodyTest
```

#### 2.3.2. `@SpringBootTest`でBean生成をバリデーション構成に絞る
```java
@SpringBootTest(classes = ValidationAutoConfiguration.class)
public class CreateUserRequestBodyTest {

        @Autowired
        private Validator validator;
```


```log
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.302 s -- in kazurego7.junit5demo.controller.model.CreateUserRequestBodyTest
```

#### 2.3.3. `jakarta.validation.Validator`を直接定義
```java
public class CreateUserRequestBodyTest {

        private final Validator validator =
                        Validation.buildDefaultValidatorFactory().getValidator();
```

```log
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.363 s -- in kazurego7.junit5demo.controller.model.CreateUserRequestBodyTest
```


## テストの命名

### アンチパターンと指針
以下は、書籍「単体テストの考え方/使い方」において、**もっとも役に立たない命名規則**として挙げられている
`{テスト対象メソッド}_{事前条件}_{想定する結果}`
- `{テスト対象メソッド}`に検証するメソッド名を記述する。
- `{事前条件}`にどのような条件でそのメソッドをテストするのかを記述する。
- `{想定する結果}`に、この条件のもとでそのメソッドを実行した際の想定する結果を記述する

命名規則を厳格にするのではなく、表現が豊かで読みやすいテスト・メソッド名をつけられる以下のような指針を定める方が良い
- 厳格な命名規則に縛られないようにする
- 問題領域のことに精通している非開発者に対してどのような検証をするのかが伝わるような名前を付ける
- (英語の場合は)アンダースコア(「_」)を使って単語を区切るようにする

### 日本語を使うことの是非

Java はソースコードに日本語が使える(クラス名、メソッド名、変数名など)

以下の利点がある
- 何のテストか一目でわかりやすく、テスト自体の誤りを防ぎやすい
- テストケース一覧が見やすくなり、テストの抜け漏れを防ぎやすい

以下の欠点がある
- プロジェクトが多国籍で運用される場合に改修が必要
  - ⇒ その前にドキュメントからコメントまで全部英語にする必要あるよね？無理でしょ
- 利用するツールやフレームワークの問題
  - ⇒ 単体テストを直接呼び出すようなツールは使わないためOK
- アサーションライブラリが英語の文法を意識しており、日本語の変数名と合わない
  - ⇒ メソッド名だけ日本語にして、中身は英語にする

以下のような懸念点はクリア
- `mvn test`コマンドでのテスト実行
- STS4 での部分テスト実行