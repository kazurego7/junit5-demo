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
- [Spring boot vs Domain Driven Develop vs クリーンアーキテクチャの名前を言ってはいけない図](#spring-boot-vs-domain-driven-develop-vs-クリーンアーキテクチャの名前を言ってはいけない図)
  - [domain](#domain)
  - [usecase](#usecase)
  - [controller](#controller)
- [テスト分類の定義](#テスト分類の定義)
- [分類ごとのテスト実施の分離](#分類ごとのテスト実施の分離)


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
以下の命名規則は書籍「単体テストの考え方/使い方」において、**もっとも役に立たない命名規則**として挙げられている

>`{テスト対象メソッド}_{事前条件}_{想定する結果}`  
・`{テスト対象メソッド}`に検証するメソッド名を記述する。  
・`{事前条件}`にどのような条件でそのメソッドをテストするのかを記述する。  
・`{想定する結果}`に、この条件のもとでそのメソッドを実行した際の想定する結果を記述する。  
このような命名規則に実用性がない理由は、目を向けている部分が振る舞いではなく実装の詳細だからです。

また、命名規則を厳格にするのではなく、テスト・メソッド名の**指針**を定める方が良いとも記載されている

> 次の指針を守ることで表現が豊かで読みやすいテスト・メソッド名を付けられるようになります:  
・**厳格な命名規則に縛られないようにする** ―― 複雑なふるまいを厳格な命名規則に従って表現することには限界があるため、そのような命名規則を強要しないことで自由な表現を可能にする。  
・**問題領域のことに精通している非開発者に対してどのような検証をするのかが伝わるような名前を付ける** ―― ここで言う非開発者とはドメイン・エキスパートやビジネス・アナリストなどのことである。  
・**（英語の場合は）アンダースコア（「_」）を使って単語を区切るようにする** ―― 特に名前が長くなる場合に、読みやすさが向上する。

ただし、テスト・クラス名は`{クラス名}Test.java`とすることで、振る舞いの起点となるクラスを表せるようにする、という記載もある

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
  - 句点「、」はメソッド名に利用できないためアンダーバー「_」にする

以下のような懸念点はクリア
- `mvn test`コマンドでのテスト実行
- STS4 での部分テスト実行

## Spring boot vs Domain Driven Develop vs クリーンアーキテクチャの名前を言ってはいけない図

別にどれもVSになってないけど……  
クリーンアーキテクチャのあの図を下地として、DDDによるモデリングを実装に落とし込む際に齟齬が生まれないようにしたい  

![クリーンアーキテクチャ_名前を言ってはいけないあの図](/doc/クリーンアーキテクチャ_名前を言ってはいけないあの図.jpg)
![クリーンアーキテクチャ_クラス図](/doc/クリーンアーキテクチャ_クラス図.png)


まず、パッケージの分類から

### domain
業務領域(domain)に関するデータと処理をまとめたものを配置

- 業務上、不正となるデータの生成を防ぐ
  - entity と valueObject により業務領域のデータを型で表現し、データに対する処理をカプセル化する
- データの整合性を保つ
  - entity や valueObject 間の関係と、同時に保存されるべきトランザクションの単位(=集約)を設計する
  - 集約単位のデータの永続化を repository で表現する
- 業務として共通する処理をまとめる
  - entity や valueObject に属せない、業務として共通する処理を domainService に置く
  - 業務に関係ないユーティリティ的な処理は 別パッケージの utils に置く

- Tips
  - IDを自動生成する仕様にしたいときは`@GeneratedValue(strategy = GenerationType.UUID)`を使う
    - https://www.baeldung.com/java-hibernate-uuid-primary-key

### usecase
業務領域(domain)の一連の処理をまとめたものを配置

- 処理の内容例
  - 処理の入力と出力の仕様を定義する
    - 入力と出力の定義は model に置く
    - 入力バリデーションの仕様を`Jakarta Bean Validation`のアノテーションで記述する
    - 入力バリデーションの実装は Spring boot が注入してくれる
  - 業務領域の処理の流れを記述する
  - 業務領域に関する entity や valueObject を、レスポンスのためのDTOに変換する

- 共通する処理を usecase に置かないこと
  - 業務として共通する処理の場合:
    - 基本的には entity や valueObject に置く
    - 複数の repository の保存を含んだり、複数の entity や valueObject で共通させる処理は domainService に置く
  - 業務に関係ないユーティリティ的な処理の場合:
    - utils に置く

- usecase 自体を共通化しないこと
  - usecase を共通化したくなったのなら、そもそも URL 設計がおかしい可能性がある

### controller
HTTPリクエストを受け取り、usecase に橋渡しする処理を配置

- 処理の内容
  - HTTPリクエストと controller のメソッドへのマッピング
    - `@RequestMapping` などに対して Spring boot が自動実装
  - 入力バリデーションの実施
    - `@Validated`, `@Valid` などに対して Spring boot が自動実装
  - HTTPリクエスト文字列をユースケースの入力オブジェクトに変換
    - `@RequestBody`, `@RequestParam` などに対して Spring boot が自動実装
  - ユースケースの処理の実行
  - ユースケースの戻り値をHTTPレスポンス文字列に変換
    - `@Controller`, `@RestController` などに対して Spring boot が自動実装
  - HTTPレスポンス文字列を返す
    - `@Controller`, `@RestController` などに対して Spring boot が自動実装

- ほとんど Spring boot の自動実装やんけ！！
  - 実際にここに書くべき処理はほとんどない
  - 共通化できる処理は共通化して common に配置


## テスト分類の定義

- 単体テストと統合テストの分類に、明確な定義を与える
  - Google の提唱する [Test Sizes](https://testing.googleblog.com/2010/12/test-sizes.html) による定義
  - Small: 単体テスト(Unit Test)
  - Mediam: 統合テスト(Integration Test)
  - Large: E2Eテスト、システムテスト(E2E Test, System Test)

![テストサイズ](/doc/テストサイズ.png)


おおまかに分類する

- 単体テストと結合テストの分類
  - 単体テストの範囲
    - 業務領域に関する計算処理
    - 業務上の一連の処理(=ユースケース)
    - ユースケースの入力バリデーション
  - 結合テストの範囲
    - HTTPリクエストからコントローラーへのマッピング
    - 入力バリデーションの実施とレスポンス
    - DBへの更新処理
    - トランザクション処理

参考1: [変更容易性と理解容易性を支える自動テスト（2024/02版）](https://speakerdeck.com/twada/automated-test-knowledge-from-savanna-202402-yapc-hiroshima-edition?slide=18)


## 分類ごとのテスト実施の分離

- 単体テスト: 迅速なフィードバック(=テスト実施速度)が重要
  - ローカルでビルドのたびに動かしたい
- 統合テスト: 退行(regression)に対する保護が重要
  - リモートリポジトリへのpushのたびに、CIサーバーで動かしたい

- maven のビルドライフサイクル上では、単体テストと統合テストが明確に分離されている
  - maven のプラグインでテストの実行を分離させたい
  - `Failsafe Plugin`

参考1: [Running Unit and Integration Test Separately in Maven](https://medium.com/@vandernobrel/running-unit-and-integration-test-separately-in-maven-a3e82d25cb7d)