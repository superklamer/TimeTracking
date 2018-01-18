package com.superklamer.serialize;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import java.sql.PreparedStatement;

import java.sql.Connection;

public class SerializerTest {
	
	public static void main(String[] args) {
		// create dummy data
		TimeWorked tw1 = new TimeWorked(new Date(), 8.5);
		TimeWorked tw2 = new TimeWorked(new Date(), 5);
		TimeWorked tw3 = new TimeWorked(new Date(), 0.5);
		
		try {
			Serializer.serialize(tw1);
			Serializer.serialize(tw2);
			Serializer.serialize(tw3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Properties properties = new Properties();
		
		try {
			properties.load(new FileInputStream("database.properties"));
		} catch (FileNotFoundException fnf) {
			System.err.println(fnf.getMessage());
		} catch (SecurityException se) {
			System.err.println(se.getMessage());
		} catch (IOException io) {
			System.err.println(io.getMessage());
		}
		
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		String dburl = properties.getProperty("dburl");
		
		// connect to db
		try (Connection conn = DriverManager.getConnection(dburl, user, password)) {		
			System.out.println("DB connection successful to: " + dburl.substring(33));
			

			PreparedStatement preparedStatement = null;
			String sql = "UPDATE Tickets.Time SET Time=? WHERE TicketNumber=23455";
			preparedStatement = conn.prepareStatement(sql);

			InputStream input = new ByteArrayInputStream(Serializer.serialize(tw1));
			preparedStatement.setBinaryStream(1, input);

			preparedStatement.executeUpdate();
			
			
		} catch (SQLException | IOException sqle) {
			sqle.printStackTrace();
		}
		
		
	}

}
