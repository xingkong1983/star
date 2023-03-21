package com.xingkong1983.star.core.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

import lombok.Data;

@Data
public class DBTest {

	@Test
	void DateTest() throws SQLException {
		String url = "jdbc:mysql://127.0.0.1:3306/";
		String db = "union-dev";
		String urlParam = "?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true";
		String userName = "root";
		String password = "keep";
		Connection conn = DriverManager.getConnection(url + db + urlParam, userName, password);
		Statement smt = conn.createStatement();
		String currentDate = DateTool.getCurrentDateTime();
		OsTool.print("当前时间是:" + currentDate);
		String sql = "insert into tb_date (create_date, create_stamp) values('" + currentDate + "','" + currentDate
				+ "')";
		OsTool.print(sql);
		int result = smt.executeUpdate(sql);
		OsTool.print("执行结果是" + result);
		conn.close();
	}
}
