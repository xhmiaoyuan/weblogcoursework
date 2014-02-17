package uk.ac.ncl.cs.csc8101.weblogcoursework;

import java.util.Calendar;
import java.util.Date;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class Search {
	private static Cluster cluster;
	private static Session session;


	public static void main(String arg[]){
		cluster = new Cluster.Builder().addContactPoint("localhost").build();
		session = cluster.connect("test");
		final String[] urls={"www.baidu.com","www.google.com"};
		Calendar c = Calendar.getInstance();
		c.set(1990, 1,1,6,9);
		Date date1=c.getTime();
		c.set(1990, 1,17,6,9);
		Date date2=c.getTime();
		getClient(1,date1,date2);
		System.out.println("_________________________________________________________");
		getURL(urls,date1,date2);
		
	}
	
	public static void getClient(int clientid,Date date1,Date date2){

		final PreparedStatement selectPS = session
				.prepare("SELECT * FROM data_table WHERE userId=? and time>=? and time<=?");
		ResultSet resultSet = session.execute(new BoundStatement(selectPS)
				.bind(clientid,date1,date2));
		for(Row row:resultSet){
			System.out.println(row);
		}
		
	}
	
	public static void getURL(String[] urls,Date date1,Date date2){
		Long num=0L;
		final PreparedStatement selectPS = session
				.prepare("SELECT COUNT(*) FROM data_table1 WHERE URL=? and time>=? and time<=?");
		for(String url:urls){
		ResultSet resultSet = session.execute(new BoundStatement(selectPS)
				.bind(url,date1,date2));
		Long n=resultSet.all().get(0).getLong(0);
		num=n+num;
	}
		System.out.println(num);
	}
	
}
