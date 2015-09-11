package jp.co._3sss.orm;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class SqlRunner {

	private String url = "jdbc:h2:tcp://localhost/~/test";
	private String username = "sa";
	private String password = "";

	public void execute(String sql, Object[] params) {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try (Connection c = DriverManager.getConnection(url, username, password);
				PreparedStatement ps = c.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public <T> List<T> query(Class<? extends Model> modelClass, String sql, List<Object> params) {

		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try (Connection c = DriverManager.getConnection(url, username, password);
				PreparedStatement ps = c.prepareStatement(sql)) {
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {

				List<T> list = new ArrayList<>();
				ResultSetMetaData md = rs.getMetaData();
				while (rs.next()) {
					T o = (T)modelClass.newInstance();
					for (int i = 0; i < md.getColumnCount(); i++) {
						String columnName = md.getColumnName(i + 1).toLowerCase();
						setFieldValue(o, columnName, rs.getObject(i + 1));
					}
					list.add(o);
				}
				return list;
			}


		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void setFieldValue(Object o, String columnName, Object value) throws Exception {
		Field f = o.getClass().getDeclaredField(columnName);
		f.setAccessible(true);
		f.set(o, value);
	}

}
