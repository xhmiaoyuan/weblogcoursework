package uk.ac.ncl.cs.csc8101.weblogcoursework;

import java.util.Date;


import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class WriterSessionData {
	private static Cluster cluster=new Cluster.Builder().addContactPoint("localhost").build();
	private static Session session;
	private static  PreparedStatement insertPS;


	
	
	public static void create(){

		final Session bootstrapSession = cluster.connect();
	    bootstrapSession
				.execute("CREATE KEYSPACE IF NOT EXISTS test WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
		bootstrapSession.shutdown();

		session = cluster.connect("test");
		//session.execute("drop table IF NOT EXISTS data_session");

		session.execute("CREATE TABLE IF NOT EXISTS data_session (clientid int,starttime timestamp,endtime timestamp,times bigint,URLtimes bigint, PRIMARY KEY (clientid,starttime) )");
		insertPS= session
				.prepare("INSERT INTO data_session (clientid,starttime,endtime,times,URLtimes) VALUES (?,?,?,?,?)");
	}
	
	public static void inside(SiteSession sitesession){
		

		final BatchStatement batchStatement = new BatchStatement(
				BatchStatement.Type.UNLOGGED);
		batchStatement.add(new BoundStatement(insertPS).bind(Integer.parseInt(sitesession.getId()),new Date(sitesession.getFirstHitMillis()),new Date(sitesession.getLastHitMillis()),sitesession.getHitCount(),sitesession.getHyperLogLog().cardinality()));		
		session.execute(batchStatement);
		

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
