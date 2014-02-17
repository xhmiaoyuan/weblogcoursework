package uk.ac.ncl.cs.csc8101.weblogcoursework;

import java.util.Calendar;
import java.util.Date;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class SearchClient {
	private static Cluster cluster;
	private static Session session;


	public static void main(String arg[]){
		cluster = new Cluster.Builder().addContactPoint("localhost").build();
		session = cluster.connect("test");
		getClient(9719);
		
	}
	
	public static void getClient(int num){

		final PreparedStatement selectPS = session
				.prepare("SELECT * FROM data_session");
		ResultSet resultSet = session.execute(new BoundStatement(selectPS)
				);
		for(Row row:resultSet){
			System.out.println(row);
		}
		
	}
}
