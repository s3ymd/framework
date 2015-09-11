package jp.co._3sss.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Query {
	private Class<? extends Model> modelClass;

	private List<String> whereClause = new ArrayList<>();
	private List<String> orderClause = new ArrayList<>();
	private List<Object> params = new ArrayList<>();

	public Query(Class<? extends Model> modelClass) {
		this.modelClass = modelClass;
	}

	public Query where(String expression, Object... args) {
		whereClause.add(expression);
		Collections.addAll(params, args);
		return this;
	}

	public Query order(String columnName) {
		orderClause.add(columnName);
		return this;
	}

	public <T> List<T> toList() {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from %s");
		
		join(sb, " where ", whereClause, " and ");
		join(sb, " order by ", orderClause, ", ");

		String rawSql = sb.toString();
		String sql = String.format(rawSql, Model.getTableName(modelClass));

		return new SqlRunner().query(modelClass, sql, params);
	}
	
	private void join(StringBuilder sb, String conjunction, Collection<?> args, String separator) {
		if (args.isEmpty()) {
			return;
		}
		sb.append(conjunction).append(whereClause.stream().collect(Collectors.joining(separator)));
	}
}