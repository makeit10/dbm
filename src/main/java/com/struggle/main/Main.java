package com.struggle.main;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.struggle.dbm.core.Configuration;
import com.struggle.dbm.core.Query;
import com.struggle.dbm.core.Session;
import com.struggle.dbm.core.SessionFactory;
import com.struggle.dbm.core.TableMetaData;

/**
 * @Project <CL-Allocation tool>
 * @version <1.0>
 * @Author  <LIXIAOYU>
 * @Date    <2015-6-17>
 * @description 
 */
public class Main {
	@Test
	public void testProxy()throws SQLException {
		Configuration Configuration=new Configuration().configure();
		SessionFactory sessionFactory = Configuration.buildSessionFactory("SMDB");
		
		Session session=sessionFactory.getCurrentSession();
		long time1=System.currentTimeMillis();
		Query query = new Query();
		query.setSql("select count(*) from bmc_core_bmc_computersystem");
		query.setResultSetHandler(new ScalarHandler<BigDecimal>());
		BigDecimal count=session.query(query);
		System.out.println(count);
	}
	public  void test1() throws SQLException {
		Configuration Configuration=new Configuration().configure();
		final SessionFactory sessionFactory = Configuration.buildSessionFactory("SMDB");
		final Session session1=sessionFactory.getCurrentSession();
		final Session session2=sessionFactory.getCurrentSession();
		System.out.println("session1==session2:"+(session1==session2));
		new Thread(new Runnable() {
			
			public void run() {
				try {
					Session session3=sessionFactory.getCurrentSession();
					Session session4=sessionFactory.getCurrentSession();
					System.out.println("session1==session3:"+(session1==session3));
					System.out.println("session3==session4:"+(session3==session4));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();;
	}
	@Test
	public void test2()throws SQLException {
		Configuration Configuration=new Configuration().configure();
		final SessionFactory sessionFactory = Configuration.buildSessionFactory("SMDB");
		final Session session=sessionFactory.getCurrentSession();
		long time1=System.currentTimeMillis();
		Query query = new Query();
		query.setSql("select count(*) from bmc_core_bmc_computersystem");
		query.setResultSetHandler(new ScalarHandler<BigDecimal>());
		BigDecimal count=session.query(query);
		System.out.println(count);
	}
	/*@Test
	public void test3() throws SQLException {
		Runnable r =new Runnable() {
			public void run() {
				Configuration Configuration=new Configuration().configure();
				final SessionFactory sessionFactory = Configuration.buildSessionFactory("SMDB");
				Session session;
				System.out.println(new Main().getRequestId(null));
				try {
					session = sessionFactory.getCurrentSession();
					long time1=System.currentTimeMillis();
					Query query = new Query("insert into CCB_CMDB_NormalizationMessage (request_Id,Submitter,InstanceId,Status,Short_Description,Assigned_To,Create_Date,Last_Modified_By,Modified_Date) values(?,?,?,?,?,?,?,?,?)");
					for(int i=0;i<10000;i++){
						List param=new ArrayList();
						param.add(new Main().getRequestId(""+i));
						param.add("SYSTEM");
						param.add(UUID.randomUUID().toString());
						param.add(0);
						param.add("当前属性\"\"值 不在标准化数据字典中，请修正。");
						param.add("BMC.CORE:BMC_ComputerSystem");
						param.add(System.currentTimeMillis()/1000);
						param.add("SYSTEM");
						param.add(System.currentTimeMillis()/1000);
						query.addParam(param);
					}
					session.batch(query);
					long time2=System.currentTimeMillis();
					System.out.println("初始化，花费："+(time2-time1));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(r).start();
		System.out.println("100000004785508");
		System.out.println(System.currentTimeMillis());
		System.out.println(Thread.currentThread().getId());
		System.out.println("100000004785508");
		
	}*/
/*	@Test
	public String getRequestId(String id)  {
			Configuration Configuration=new Configuration().configure();
			final SessionFactory sessionFactory = Configuration.buildSessionFactory("SMDB");
			final Object lock = new Object();
			Runnable r =new Runnable() {
				public void run() {
					synchronized (lock) {
						try {
							Session session = sessionFactory.openSession();
							session.beginTransaction();
//							session.getTransaction().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							Query query = new Query("select nextId from table1 where name=?");
							query.addParam("a");
							query.setResultSetHandler(new ScalarHandler<String>());
							int id=Integer.parseInt((String) session.query(query));
							System.out.println(id);
							Query query1 = new Query("update table1 set nextId=? where name=?");
							query1.addParam(id+1);
							query1.addParam("a");
							session.update(query1);
							session.getTransaction().commit();
							session.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
					}
				}
			};
			new Thread(r).start();
			new Thread(r).start();
			new Thread(r).start();
			new Thread(r).start();
			new Thread(r).start();
			return null;
	}
	@Test
	public void test5() throws SQLException{
		 Connection conn = null;  
	        String url = "jdbc:mysql://localhost:3306/smdb";  
	        String jdbcDriver = "com.mysql.jdbc.Driver";  
	        String user = "root";  
	        String password = "password";  
	        DbUtils.loadDriver(jdbcDriver);  
	        conn = DriverManager.getConnection(url, user, password);  
	        QueryRunner qr = new QueryRunner();  
	        String inSql = "insert into history (instanceId) values (?)";
	        System.out.println(qr.insert(conn, inSql,new ScalarHandler(),new Object[]{UUID.randomUUID().toString()}));
	}*/
	@Test
	public void testCreate() throws SQLException {
				Configuration Configuration=new Configuration().configure();
				final SessionFactory sessionFactory = Configuration.buildSessionFactory("SMDB");
				Session session;
					session = sessionFactory.getCurrentSession();
					session.beginTransaction();
					Query query = new Query("insert into CCB_CMDB_NormalizationMessage (request_Id,Submitter,InstanceId,Status,Short_Description,Assigned_To,Create_Date,Last_Modified_By,Modified_Date) values(?,?,?,?,?,?,?,?,?)");
					for(int i=30000;i<40000;i++){
						List param=new ArrayList();
						param.add("1"+StringUtils.leftPad(i+"", 14));
						param.add("SYSTEM");
						param.add(UUID.randomUUID().toString());
						param.add(0);
						param.add("当前属性\"\"值 不在标准化数据字典中，请修正。");
						param.add("BMC.CORE:BMC_ComputerSystem");
						param.add(System.currentTimeMillis()/1000);
						param.add("SYSTEM");
						param.add(System.currentTimeMillis()/1000);
						query.addParam(param);
					}
					long time1=System.currentTimeMillis();
					session.batch(query);
					long time2=System.currentTimeMillis();
					System.out.println("初始化，花费："+(time2-time1));
					session.getTransaction().commit();
	}
	@Test
	public void testTableMeta()throws SQLException {
		Configuration Configuration=new Configuration().configure("/dbm1.xml");
		SessionFactory sessionFactory = Configuration.buildSessionFactory("SMDB");
		Session session=sessionFactory.getCurrentSession();
		TableMetaData t =session.getTableMetaData("test");
		System.out.println(t);
		/*session.beginTransaction();
		session.update(new Query("alter table student drop column age"));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		session.getTransaction().rollback();*/
	}
}
