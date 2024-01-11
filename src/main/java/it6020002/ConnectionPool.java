package it6020002;
import java.sql.*;
public interface ConnectionPool {
	//Phuong thuc xin ket noi
	public Connection getConnection(String objectName) throws SQLException;
	
	//phuong thuc yeu cau tra ve ket noi
	public void releaseConnection(Connection con, String objectName) throws SQLException;
}
