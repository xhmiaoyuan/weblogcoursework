package uk.ac.ncl.cs.csc8101.weblogcoursework;

/*
Copyright 2014 Red Hat, Inc. and/or its affiliates.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

import com.datastax.driver.core.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Simple integration tests for cassandra server v2 / CQL3 via datastax
 * java-driver
 * 
 * @author Jonathan Halliday (jonathan.halliday@redhat.com)
 * @since 2014-01
 */
public class Connection {

	private static Cluster cluster;
	private static Session session;

//	//@BeforeClass
//	public static void staticSetup() {
//
//		cluster = new Cluster.Builder().addContactPoint("localhost").build();
//
//		final Session bootstrapSession = cluster.connect();
//		bootstrapSession
//				.execute("CREATE KEYSPACE IF NOT EXISTS test WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
//		bootstrapSession.shutdown();
//
//		session = cluster.connect("test");
//
//		session.execute("CREATE TABLE IF NOT EXISTS test_data_table (k bigint, v text, PRIMARY KEY (k) )");
//		session.execute("CREATE TABLE IF NOT EXISTS test_counter_table (k bigint, v counter, PRIMARY KEY (k) )");
//	}
//
//	//@AfterClass
//	public static void staticCleanup() {
//		session.shutdown();
//		cluster.shutdown();
//	}
//
//	@Test
//	public void insertAndReadBackSync() {
//
//		final PreparedStatement insertPS = session
//				.prepare("INSERT INTO test_data_table (k, v) VALUES (?, ?)");
//		session.execute(new BoundStatement(insertPS).bind(1L, "test"));
//
//		final PreparedStatement selectPS = session
//				.prepare("SELECT v FROM test_data_table WHERE k=?");
//		ResultSet resultSet = session.execute(new BoundStatement(selectPS)
//				.bind(1L));
//
//		final String v = resultSet.one().getString(0);
//
//		assertEquals("test", v);
//	}
//
//	@Test
//	public void insertAndReadBackAsync() {
//
//		final long now = System.currentTimeMillis();
//
//		final PreparedStatement insertPS = session
//				.prepare("UPDATE test_counter_table SET v=v+? WHERE k=?");
//		final ResultSetFuture mutationFuture = session
//				.executeAsync(new BoundStatement(insertPS).bind(10L, now));
//		mutationFuture.getUninterrup
//
//		final PreparedStatement selectPS = session
//				.prepare("SELECT v FROM test_counter_table WHERE k=?");
//		final ResultSetFuture queryFuture = session
//				.executeAsync(new BoundStatement(selectPS).bind(now));
//		ResultSet resultSet = queryFuture.getUninterruptibly();
//
//		final long v = resultSet.one().getLong(0);
//
//		assertEquals(10L, v);
//	}
//
//	@Test
//	public void insertAndReadBackBatch() {
//
//		final PreparedStatement insertPS = session
//				.prepare("INSERT INTO test_data_table (k, v) VALUES (?, ?)");
//		final BatchStatement batchStatement = new BatchStatement(
//				BatchStatement.Type.UNLOGGED);
//
//		batchStatement.add(new BoundStatement(insertPS).bind(100L,
//				"batch-item-one"));
//		batchStatement.add(new BoundStatement(insertPS).bind(200L,
//				"batch-item-two"));
//
//		session.execute(batchStatement);
//
//		final PreparedStatement selectPS = session
//				.prepare("SELECT v FROM test_data_table WHERE k IN (?, ?)");
//		ResultSet resultSet = session.execute(new BoundStatement(selectPS)
//				.bind(100L, 200L));
//
//		List<Row> rows = resultSet.all();
//		assertEquals(2, rows.size());
//		assertEquals("batch-item-one", rows.get(0).getString(0));
//		assertEquals("batch-item-two", rows.get(1).getString(0));
//	}
	
	public static void create(){

		try {
		session.execute("drop table data_table");
		session.execute("drop table data_table1");

			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.execute("CREATE TABLE IF NOT EXISTS data_table (userId int,time timestamp,URL text, PRIMARY KEY (userId,time) )");
		session.execute("CREATE TABLE IF NOT EXISTS data_table1 (userId int,time timestamp,URL text, PRIMARY KEY (URL,time) )");
	}
	
	public static void inside(){
		final PreparedStatement insertPS = session
				.prepare("INSERT INTO data_table (userId,time,URL) VALUES (?,?,?)");
		final PreparedStatement insertPS2 = session
				.prepare("INSERT INTO data_table1 (userId,time,URL) VALUES (?,?,?)");

		final BatchStatement batchStatement = new BatchStatement(
				BatchStatement.Type.UNLOGGED);
		final String[] websit={"www.baidu.com","www.google.com","www.ncl.ac.uk","www.bbc.co.uk"};
		final int[] nu={1,2,3,4,5};
		for(int i=0;i<=100;i++){
		Random su=new Random();
		int day=su.nextInt(24);
		int min=su.nextInt(60);
		int se=su.nextInt(60);
		Calendar c = Calendar.getInstance();
		c.set(1990, 1,day,min,se);
		batchStatement.add(new BoundStatement(insertPS).bind(nu[su.nextInt(4)],c.getTime(),websit[su.nextInt(4)]));
		batchStatement.add(new BoundStatement(insertPS2).bind(nu[su.nextInt(4)],c.getTime(),websit[su.nextInt(4)]));
		}
	
		
		session.execute(batchStatement);

	}
	
	public static void search(){
		ResultSet resultSet = session.execute("SELECT * FROM data_table");
		for(Row row:resultSet){
			System.out.println(row);
		}
		System.out.println(1);
		
	}
	public static void main(String arg[]){
		cluster = new Cluster.Builder().addContactPoint("localhost").build();
		session = cluster.connect("test");

		create();
		inside();
		search();
		session.shutdown();
     	cluster.shutdown();
		
		
	}
}
