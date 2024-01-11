package it6020002;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import com.mysql.cj.jdbc.Driver;

public class ConnectionPoolImpl implements ConnectionPool {
	//Trinh dieu khien lam viec voi MySQL
	private String driver;
	
	//Duong dan thuc thi cua MySQL
	private String url;
	
	//Tai khoan lam viec
	private String username;
	private String userpass;
	
	//Doi tuong luu tru cac ket noi
	public Stack<Connection> pool;
	
	public ConnectionPoolImpl() {
		//Xac dinh trinh dieu khien
		this.driver = "com.mysql.cj.jdbc.Driver";
		
		//Nap trinh dieu khien
		try {
			Class.forName(this.driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Xac dinh duong dan thuc thi
		this.url = "jdbc:mysql://localhost:3306/javanangcao";
		
		//Xac dinh tai khoan lam viec
		this.username = "tuantd_java";
		this.userpass = "123456";
		
		//Khoi tao bo nho cho doi tuong luwu tru
		this.pool = new Stack<>();
		
		
		
	}
	public Connection getConnection(String objectName) throws SQLException {
		// TODO Auto-generated method stub
		if(this.pool.isEmpty()) {
			System.out.println(objectName + "have created a new Connection!");
			return DriverManager.getConnection(this.url, this.username, this.userpass);
		} else {
			System.out.println(objectName + "have popped the Connection!");
			return this.pool.pop();
		} 
	}

	@Override
	public void releaseConnection(Connection con, String objectName) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println(objectName + "have pushed the Connection!");
		this.pool.push(con);
	}
	
	protected void finalize() throws Throwable {
		//Loai bo cac ket noi trong pool
		this.pool.clear();
		this.pool = null;
		
		System.out.println("ConnectionPool is closed");
	}

}
