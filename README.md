# 簡易仕様書

### 作者
朴 壯浩（パク・ジャンホ）
### アプリ名
RestaurantSearcher

#### コンセプト
地図と地域別に分けて自分に近い順、人気順などで店を探すことができます。

#### こだわったポイント
- 地図を選択すると最初に自分の位置が表示され、周りの店が検索半径を基準に地図に表示されます。
- 地図で自分が指定するところに位置マークが表示され、検索半径を基準に周りの店が地図に表示されます。
- 地域ボタンを押すと日本の都道府県を選択できます。
- 自分が選んだ都道府県の店が人気順に表示され、カナ順に並べ替えられます。
- リストや地図のお店マーカーをタッチすると、お店の詳細情報ページに進みます。
- 飲食名や区域名（例:新宿）で検索すると、その情報が含まれたお店のみ表示されます。
- 全体的にMVVMパターンでコードを作成するために努めました。

#### デザイン面でこだわったポイント
- ボタンや地図上に表示されるマーカーなどをわかりやすいよう実装するために努めました。
- レストランの詳細画面はレストランのイメージが大きく表示され、店舗名やジャンル名、その他にお店の住所やアクセスなどの情報がわかりやすいようにデザインしています。
- レストランのリスト画面は、リストが見やすいように最小限の情報だけが表示されるように作りました。

### 該当プロジェクトのリポジトリ URL（GitHub,GitLab など Git ホスティングサービスを利用されている場合）
https://github.com/P-Jangho/RestaurantSearcher

## 開発環境
### 開発環境
Android Studio Electric Eel | 2022.1.1 Patch 2

### 開発言語
Java

## 動作対象端末・OS
### 動作対象OS
Android

## 開発期間
9日間

## アプリケーション機能

### 機能一覧
- レストランMAP：ホットペッパーグルメサーチAPIを使用して、現在地周辺の飲食店が表示されます。地図で自分が指定するところの周辺の飲食店も表示されます。検索半径を指定することができます。（地図アプリ連携）
- レストランリスト：ホットペッパーグルメサーチAPIを使用して、選択した地域の飲食店がリストで表示されます。
- レストラン検索：キーワードを検索すると、その内容がその内容が含まれているお店のみ表示されます。
- レストラン情報取得：ホットペッパーグルメサーチAPIを使用して、飲食店の詳細情報を取得します。
- 電話アプリ連携：飲食店の電話番号を電話アプリに連携する。

### 画面一覧
- Main画面 ：地図か地域かを選択できます。
- 地図画面 ：現在地や自分が指定したところの周辺の飲食店が地図に表示されます。キーワードを検索すると、その内容がその内容が含まれているお店のみ表示されます。
- Area画面 ：日本の都道府県を選択することができます。
- List画面 ：選択した地域のお店が人気順に表示されます（カナ順に変更できます）。キーワードを検索すると、その内容がその内容が含まれているお店のみ表示されます。
- レストラン詳細情報画面 ：地図のお店のマーカーやリストをタッチすると、お店の詳細情報が表示されます。

### 使用しているAPI,SDK,ライブラリなど
- ホットペッパーグルメサーチAPI
- Google Play Services Maps
- OkHttp ライブラリ
- Glide ライブラリ
- AndroidX Lifecycle ViewModel ライブラリ

### アドバイスして欲しいポイント
アプリを作る時に追加すればよかった機能

## 自己評価
デザイン面や追加しなければならない機能面で、現在私が使っている配達アプリやグーグルマップアプリをある程度参照しながら作りました。 初めて作ってみた地図アプリなのでとても難しかったですが、この機会にたくさん勉強になりました。 コードを作成する上でもMVMパターンで書くために努力しましたが、きれいに整理されたようではないと思い、まだまだ勉強が必要だと思いました。 今後も必要な情報や追加すべき機能があれば、引き続き追加しながらより良いアプリを作るために積極的に取り組んでいきます。

## アウトプット
<div style="display: flex; justify-content: center;">
  <div style="display: flex;">
    <img width="200" alt="1" src="https://github.com/P-Jangho/RestaurantSearcher/assets/131533268/29d3a59e-b090-468e-981b-82ccd19c3a9e">
    <img width="200" alt="2" src="https://github.com/P-Jangho/RestaurantSearcher/assets/131533268/1c47fd29-e2bc-4730-9353-c5b4d1fcf211">
    <img width="200" alt="3" src="https://github.com/P-Jangho/RestaurantSearcher/assets/131533268/e6d6bf24-7476-4262-912c-a8350f38b796">
    <img width="200" alt="4" src="https://github.com/P-Jangho/RestaurantSearcher/assets/131533268/b246106f-3bec-4f5b-938e-7b16d1be9047">
  </div>
  
  <div style="display: flex;">
    <img width="200" alt="5" src="https://github.com/P-Jangho/RestaurantSearcher/assets/131533268/9ac2e829-c8a6-4801-86a2-bdb52aa1ab31">
    <img width="200" alt="6" src="https://github.com/P-Jangho/RestaurantSearcher/assets/131533268/3986c952-2104-4895-9a51-0f4e4197d644">
    <img width="200" alt="7" src="https://github.com/P-Jangho/RestaurantSearcher/assets/131533268/cdce4e5b-0180-4a5a-9261-d4166ccbaf7f">
    <img width="200" alt="8" src="https://github.com/P-Jangho/RestaurantSearcher/assets/131533268/8d44830c-57b6-4006-8856-76466bc3191a">
</div>

  <div style="display: flex;">
    <img width="200" alt="9" src="https://github.com/P-Jangho/RestaurantSearcher/assets/131533268/519be3df-e843-41cb-ae55-86cb6f5437c7">
  </div>
