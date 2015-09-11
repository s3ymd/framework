package jp.co._3sss.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Model {

	public void insert() {
		Object[] params = getParams();
		String sql = getInsertSQL();
		new SqlRunner().execute(sql, params);
	}

	public void update() {
		List<Object> params = new ArrayList<>();
		Collections.addAll(params, getNonIdFieldValues());
		params.add(getId());
		String sql = getUpdateSQL();
		new SqlRunner().execute(sql, params.toArray());
	}

	public void delete() {
		Object[] params = { getId() };
		String sql = getDeleteSQL();
		new SqlRunner().execute(sql, params);
	}

	private Field getIdField() {
		return getColumnFields()//
				.filter(f -> "id".equals(f.getName()))//
				.findFirst().get();
	}

	private Stream<Field> getNonIdFields() {
		return getColumnFields()//
				.filter(f -> !"id".equals(f.getName()));
	}

	private int getId() {
		return (Integer) getFieldValue(this, getIdField());
	}

	private Object[] getNonIdFieldValues() {
		return getNonIdFields().map(f -> getFieldValue(this, f)).toArray();
	}

	private String getInsertSQL() {
		int fields = (int) getColumnFields().count();
		return String.format("insert into %s (%s) values (%s)", //
				getTableName(), getColumnNames(), getPlaceholders(fields));
	}

	private String getUpdateSQL() {
		String updateFields = getNonIdFields()//
				.map(f -> String.format("%s = ?", f.getName()))//
				.collect(Collectors.joining(", "));
		return String.format("update %s set %s where id = ?", getTableName(), updateFields);
	}

	private String getDeleteSQL() {
		return String.format("delete from %s where id = ?", getTableName());
	}

	private String getTableName() {
		return getClass().getAnnotation(Table.class).value();
	}

	private Stream<Field> getColumnFields() {
		return Arrays.stream(getClass().getDeclaredFields())//
				.filter(f -> f.getAnnotation(Column.class) != null);
	}

	private String getColumnNames() {
		return getColumnFields().map(f -> f.getName())//
				.collect(Collectors.joining(", "));
	}

	private Object[] getParams() {
		return getColumnFields().map(f -> getFieldValue(this, f)).toArray();
	}

	private Object getFieldValue(Object obj, Field f) {
		try {
			f.setAccessible(true);
			return f.get(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getTableName(Class<? extends Model> modelClass) {
		return modelClass.getAnnotation(Table.class).value();
	}

	private String getPlaceholders(int fields) {
		String[] s = new String[fields];
		Arrays.fill(s, "?");
		return Stream.of(s).collect(Collectors.joining(", "));
	}

}
