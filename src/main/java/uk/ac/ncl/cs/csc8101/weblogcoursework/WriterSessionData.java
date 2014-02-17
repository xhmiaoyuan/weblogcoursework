package uk.ac.ncl.cs.csc8101.weblogcoursework;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class WriterSessionData {
	private static Cluster cluster=new Cluster.Builder().addContactPoint("localhost").build();
	private static Session session= cluster.connect("test");
	private static final PreparedStatement insertPS = session
			.prepare("INSERT INTO data_session (clientid,starttime,endtime,times,URLtimes) VALUES (?,?,?,?,?)");


	
	
	public static void create(){
		session.execute("drop table data_session");

		session.execute("CREATE TABLE IF NOT EXISTS data_session (clientid int,starttime timestamp,endtime timestamp,times bigint,URLtimes bigint, PRIMARY KEY (clientid,starttime) )");
	}
	
	public static void inside(SiteSession sitesession){
		

		final BatchStatement batchStatement = new BatchStatement(
				BatchStatement.Type.UNLOGGED);
		batchStatement.add(new BoundStatement(insertPS).bind(Integer.parseInt(sitesession.getId()),new Date(sitesession.getFirstHitMillis()),new Date(sitesession.getLastHitMillis()),sitesession.getHitCount(),sitesession.getHyperLogLog().cardinality()));		
		session.execute(batchStatement);
		System.out.println("____");
		

	}
	
	public static void endconnection(){
		session.shutdown();
     	cluster.shutdown();
	}
	public static void main(String arg[]){
		create();
		endconnection();
		
	}
	

}
