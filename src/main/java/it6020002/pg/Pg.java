package it6020002.pg;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it6020002.*;
import it6020002.objects.PgObject;

public class Pg {

	private Connection con;

	private ConnectionPool cp;

	public Pg() {
		this.cp = new ConnectionPoolImpl();

		try {
			this.con = this.cp.getConnection("Pg");

			if (this.con.getAutoCommit()) {
				this.con.setAutoCommit(false);// Cham dut che do thuc thi tu dong cua ket noi
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	


	
//  //**********************************Lấy dữ liệu từ SQL***************************************************************
	public ArrayList<PgObject> getPgObject(PgObject similar, byte total) {
		ArrayList<PgObject> items = new ArrayList<>();

		PgObject item;

		String sql = "SElECT * FROM tblpg ";
		sql += "";
		sql += "ORDER BY pg_id ASC ";
		sql += "LIMIT ?";

		try {
			PreparedStatement pre = this.con.prepareStatement(sql);
			// truyền tổng số bản ghi vừa lấy
			pre.setByte(1, total);

			// Lấy danh sách bản ghi
			ResultSet rs = pre.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					item = new PgObject();
					// item.setPg_id(rs.getShort(1));
					item.setPg_id(rs.getShort("pg_id"));
					item.setPg_name(rs.getString("pg_name"));
	                item.setPg_ps_id(rs.getByte("pg_ps_id"));
	                item.setPg_manager_id(rs.getInt("pg_manager_id"));
	                item.setPg_notes(rs.getString("pg_notes"));
	                item.setPg_delete(rs.getBoolean("pg_delete"));
	                item.setPg_deleted_date(rs.getString("pg_deleted_date"));
	                item.setPg_deleted_author(rs.getString("pg_deleted_author"));
	                item.setPg_modified_date(rs.getString("pg_modified_date"));
	                item.setPg_created_date(rs.getString("pg_created_date"));
	                item.setPg_enable(rs.getBoolean("pg_enable"));
	                item.setPg_name_en(rs.getString("pg_name_en"));
	                item.setPg_created_author_id(rs.getInt("pg_created_author_id"));
	                item.setPg_language(rs.getByte("pg_language"));

					// đưa vào tập hợp
					items.add(item);
				}
				// đóng tập kết quả
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();

			try {
				this.con.rollback();// trở về trạng thái an toàn của kết nối
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return items;
	}
//  //***********************************END***************************************************************
	
	
	

	
	

//  //***********************************HIỂN THỊ DANH SÁCH SẢN PHẨM***************************************************************
	public void displayPgList(ArrayList<PgObject> pgList) {
	    System.out.println("Danh sách các đối tượng PgObject:");

	    if (pgList.isEmpty()) {
	        System.out.println("Danh sách trống.");
	    } else {
	        // In header của bảng
	        System.out.printf("%-5s %-20s %-10s %-15s %-10s %-15s %-20s %-20s %-20s %-10s %-20s %-10s %-10s %-5s%n",
	                "ID", "Tên", "ID PS", "ID Quản lý", "Ghi chú", "Đã xóa", "Ngày xóa", "Người xóa", "Ngày sửa đổi",
	                "Ngày tạo", "Kích hoạt", "Tên (EN)", "Người tạo", "Ngôn ngữ");

	        // In từng đối tượng PgObject
	        for (PgObject pg : pgList) {
	            System.out.printf("%-5s %-20s %-10s %-15s %-10s %-15s %-20s %-20s %-20s %-10s %-20s %-10s %-10s %-5s%n",
	                    pg.getPg_id(), pg.getPg_name(), pg.getPg_ps_id(), pg.getPg_manager_id(),
	                    pg.getPg_notes(), pg.isPg_delete(), pg.getPg_deleted_date(), pg.getPg_deleted_author(),
	                    pg.getPg_modified_date(), pg.getPg_created_date(), pg.isPg_enable(), pg.getPg_name_en(),
	                    pg.getPg_created_author_id(), pg.getPg_language());
	        }
	    }
	}
//  //***********************************END***************************************************************
	

	
	


	

//  //***********************************THÊM SẢN PHẨM***************************************************************
	public boolean addPg(PgObject item) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO tblpg(");
		sql.append("pg_name, pg_ps_id, pg_manager_id,");
		sql.append("pg_notes, pg_delete, pg_deleted_date,");
		sql.append("pg_deleted_author, pg_modified_date, pg_created_date,");
		sql.append("pg_enable, pg_name_en,");
		sql.append("pg_created_author_id, pg_language) ");
		sql.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");

		// biên dịch
		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			// truyền giá trị
			pre.setString(1, item.getPg_name());
			pre.setInt(2, item.getPg_ps_id());
			pre.setInt(3, item.getPg_manager_id());
			pre.setString(4, item.getPg_notes());
			pre.setBoolean(5, item.isPg_delete());
			pre.setString(6, item.getPg_deleted_date());
			pre.setString(7, item.getPg_deleted_author());
			pre.setString(8, item.getPg_modified_date());
			pre.setString(9, item.getPg_created_date());
			pre.setBoolean(10, item.isPg_enable());
			pre.setString(11, item.getPg_name_en());
			pre.setInt(12, item.getPg_created_author_id());
			pre.setByte(13, item.getPg_language());

			int result = pre.executeUpdate();// thực thi
			if (result == 0) {
				this.con.rollback();
				return false;
			}

			// Xác nhận thực thi sau cùng
			this.con.commit();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}
//  //***********************************END***************************************************************
	
	
	
	
	
	
	
//  //***********************************UPDATE***************************************************************
	public boolean updatePg(PgObject item) {
	    StringBuilder sql = new StringBuilder();
	    sql.append("UPDATE tblpg SET ");
	    sql.append("pg_name=?, pg_ps_id=?, pg_manager_id=?, ");
	    sql.append("pg_notes=?, pg_delete=?, pg_deleted_date=?, ");
	    sql.append("pg_deleted_author=?, pg_modified_date=?, pg_created_date=?, ");
	    sql.append("pg_enable=?, pg_name_en=?, ");
	    sql.append("pg_created_author_id=?, pg_language=? ");
	    sql.append("WHERE pg_id=?");

	    try {
	        PreparedStatement pre = this.con.prepareStatement(sql.toString());
	        // Truyền giá trị
	        pre.setString(1, item.getPg_name());
	        pre.setInt(2, item.getPg_ps_id());
	        pre.setInt(3, item.getPg_manager_id());
	        pre.setString(4, item.getPg_notes());
	        pre.setBoolean(5, item.isPg_delete());
	        pre.setString(6, item.getPg_deleted_date());
	        pre.setString(7, item.getPg_deleted_author());
	        pre.setString(8, item.getPg_modified_date());
	        pre.setString(9, item.getPg_created_date());
	        pre.setBoolean(10, item.isPg_enable());
	        pre.setString(11, item.getPg_name_en());
	        pre.setInt(12, item.getPg_created_author_id());
	        pre.setByte(13, item.getPg_language());

	        // Đặt giá trị cho điều kiện WHERE
	        pre.setShort(14, item.getPg_id());

	        int result = pre.executeUpdate(); // Thực thi
	        if (result == 0) {
	            this.con.rollback();
	            return false;
	        }

	        // Xác nhận thực thi sau cùng
	        this.con.commit();
	        return true;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            this.con.rollback();
	        } catch (SQLException e1) {
	            e1.printStackTrace();
	        }
	    }
	    return false;
	}
//  //*********************************END***************************************************************

	
	
	

	
	
//  //*********************************XÓA SẢN PHẨM***********************************************************	
	public boolean deletePg(short pgId) {
        // Câu lệnh SQL DELETE
        String sql = "DELETE FROM tblpg WHERE pg_id = ?";

        try {
            // Chuẩn bị câu lệnh SQL
            PreparedStatement pre = this.con.prepareStatement(sql);

            // Truyền giá trị cho tham số trong câu lệnh SQL
            pre.setShort(1, pgId);

            // Thực hiện câu lệnh DELETE
            int result = pre.executeUpdate();

            // Nếu có bản ghi bị xóa, result > 0
            if (result > 0) {
                this.con.commit();
                return true;
            } else {
                this.con.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                this.con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }
	
	
	

//  //************************TÌM KIẾM SẢN PHẨM THEO TÊN***************************************************************
	public ArrayList<PgObject> searchPgByName(String name) {
        ArrayList<PgObject> searchResults = new ArrayList<>();

        // Câu lệnh SQL SELECT với điều kiện tìm kiếm theo tên
        String sql = "SELECT * FROM tblpg WHERE pg_name LIKE ?";

        try {
            // Chuẩn bị câu lệnh SQL
            PreparedStatement pre = this.con.prepareStatement(sql);

            // Truyền giá trị cho tham số trong câu lệnh SQL
            pre.setString(1, "%" + name + "%");

            // Thực hiện truy vấn SELECT
            ResultSet rs = pre.executeQuery();

            // Xử lý kết quả truy vấn
            while (rs.next()) {
                PgObject pg = new PgObject();
                pg.setPg_id(rs.getShort("pg_id"));
                pg.setPg_name(rs.getString("pg_name"));
                pg.setPg_ps_id(rs.getByte("pg_ps_id"));
                pg.setPg_manager_id(rs.getInt("pg_manager_id"));
                pg.setPg_notes(rs.getString("pg_notes"));
                pg.setPg_delete(rs.getBoolean("pg_delete"));
                pg.setPg_deleted_date(rs.getString("pg_deleted_date"));
                pg.setPg_deleted_author(rs.getString("pg_deleted_author"));
                pg.setPg_modified_date(rs.getString("pg_modified_date"));
                pg.setPg_created_date(rs.getString("pg_created_date"));
                pg.setPg_enable(rs.getBoolean("pg_enable"));
                pg.setPg_name_en(rs.getString("pg_name_en"));
                pg.setPg_created_author_id(rs.getInt("pg_created_author_id"));
                pg.setPg_language(rs.getByte("pg_language"));

                // Thêm đối tượng PgObject vào danh sách kết quả
                searchResults.add(pg);
            }

            // Đóng tập kết quả
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }
	
	
	//Thống kê
	public Map<Byte, Integer> statisticsPgByLanguage() {
        Map<Byte, Integer> languageStatistics = new HashMap<>();

        // Câu lệnh SQL SELECT và GROUP BY ngôn ngữ
        String sql = "SELECT pg_language, COUNT(*) AS language_count FROM tblpg GROUP BY pg_language";

        try {
            // Chuẩn bị câu lệnh SQL
            PreparedStatement pre = this.con.prepareStatement(sql);

            // Thực hiện truy vấn SELECT
            ResultSet rs = pre.executeQuery();

            // Xử lý kết quả truy vấn
            while (rs.next()) {
                byte language = rs.getByte("pg_language");
                int count = rs.getInt("language_count");

                // Thêm vào bản đồ thống kê
                languageStatistics.put(language, count);
            }

            // Đóng tập kết quả
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return languageStatistics;
    }

	
	// main
	public static void main(String[] args) {
	    Pg s = new Pg();
	    ArrayList<PgObject> items = s.getPgObject(null, (byte) 100);
	    s.displayPgList(items);
	}

}
