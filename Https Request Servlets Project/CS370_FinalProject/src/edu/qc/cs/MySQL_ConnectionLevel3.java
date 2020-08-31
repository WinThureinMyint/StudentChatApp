package edu.qc.cs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL_ConnectionLevel3 {

	public MySQL_ConnectionLevel3() {

	}

	Connection connectDB() {

		Connection connect = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String dbName = "CS370Db";
			String dbUserName = "win";
			String dbPassword = "password";
			String serverAddress = "54.84.136.251";
			String ks = new String(getClass().getClassLoader().getResource("keystore.ks").toString());
			System.out.println(ks);
			System.setProperty("javax.net.ssl.keyStore", ks);
			System.setProperty("javax.net.ssl.keyStorePassword", "password");

			String connectionString = "jdbc:mysql://" + serverAddress + "/" + dbName + "?user=" + dbUserName
					+ "&password=" + dbPassword + "&useUnicode=true&characterEncoding=UTF-8";

			connect = DriverManager.getConnection(connectionString);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connect;

	}

}
