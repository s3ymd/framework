フレームワークのようなもの

# Model機能

## モデル定義

モデルはModelクラスのサブクラスとして定義します。

@Tableで表名、@Columnで列名を指定します。

```java

import jp.co._3sss.orm.Model;

@Table("items")
class Item extends Model {

  @Column("id")
  private int id;
  
  @Column("name")
  private String name;
  
  // getter / setter ...
}
```

## ORマッパー(更新系)

Databaseクラスに、INSERT/UPDATE/DELETEに対応するメソッドが用意されています。

```java
import static jp.co._3sss.orm.Database.*;

Item item = new Item();

insert(item); // 追加
update(item); // 更新
delete(item); // 削除
```

## ORマッパー(検索系)

Databaseクラスに、SELECTに対応するメソッドが用意されています。

検索条件はwhere("式", パラメータ値...)の形で指定します。式ではプレースホルダ「?」が使用できます。

複数のwhere()を使用した場合はANDで結合されます。

```java
import static jp.co._3sss.orm.Database.*;

// ID = 1の商品を検索
Item item = db.select(Item.class, 1);

// 価格 <= 1000 の商品一覧を取得
List<Item> items = db.select(Item.class)
  .where("price <= ?", 1000)
  .toList();

// 価格 が 1000円以上 2000円以下、商品カテゴリ = 3 の商品一覧を取得
List<Item> items = db.select(Item.class)
  .where("price between ? and ?", 1000, 2000)
  .where("category_id = ?", 3)
  .toList();

// 価格 <= 1000 の商品を、登録日が新しい順に並べ、一覧を取得
List<Item> items = db.select(Item.class)
  .where("price <= ?", 1000)
  .order("created_at desc")
  .toList();
```

# Controller機能

## コントローラ

コントローラはControllerクラスのサブクラスとして実装します。

```java
class ItemsController extends Controller {

  public void index() {
    // /WEB-INF/jsp/items/new.jspにフォワード
	  render("items/new");
  }
  
  public void show() {
    // request.getParamter("id") に相当
    String id = parameter("id");
    
    // Itemオブジェクトを取得
    Item item = ...
    
    // request.setAttribute()に相当
    set("item", item);
    
	  render("items/show");
  }
  
  public void create() {
    // パラメータに基づき、Itemオブジェクトを作る
    Item item = ...
    
    // DBに保存
    create(item);
    
    // response.sendRedirect()に相当
    redirect("items/index");
  }
}
```

## ルーティング

アクセス

```java
import jp.co._3sss.web.Router;

public class AppRouter extends Router {
	public AppRouter() {
    // item/indexに対するGETアクセスを処理するクラスとメソッド
		addRoute("items/index", "get", "shop.ItemsController", "index");
	}
}
```

# View

今のところJSPのみ対応しています。

/WEB-INF/jsp/コントローラ名/アクション名.jsp
に配置してください。

