package jp.co._3sss.orm;

public class Database {
	public static Query select(Class<? extends Model> modelClass) {
		return new Query(modelClass);
	}

	@SuppressWarnings("unchecked")
	public static <T> T select(Class<? extends Model> modelClass, int id) {
		return (T) new Query(modelClass).where("id = ?", id).toList().get(0);
	}

	public static void insert(Model model) {
		model.insert();
	}

	public static void update(Model model) {
		model.update();
	}

	public static void delete(Model model) {
		model.delete();
	}
}