package adcms;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.mysql.cj.result.Row;

import java.text.SimpleDateFormat;

import it6020002.ConnectionPool;
import it6020002.ConnectionPoolImpl;
import it6020002.objects.PgObject;
import it6020002.pg.Pg;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class ViewPg extends JFrame {
	private ConnectionPool connectionPool;
	
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    
    private boolean isTableDisplayed = false;
    private boolean isTableUpdate = false;
    
    
    
    //Hàm lấy dữ liệu ra items
    private ArrayList<PgObject> fetchDataFromSQL() {
        Pg pg = new Pg();
        ArrayList<PgObject> items = pg.getPgObject(null, (byte) 100);
        return items;
    }
    
    // Hàm lấy ngày hiện tại dưới định dạng chuỗi
    private String getCurrentDate() {
        java.util.Date currentDate = new java.util.Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
        return dateFormat.format(currentDate);
    }
    
    // Hàm trả về một giá trị ngẫu nhiên từ danh sách
//    private final Random random = new Random();
//    
    private String getRandomCategory() {
        String[] categories = {"Triet Hoc", "Sach Y Hoc",
                "Truyen Tranh", "Tieu Thuyet", "Sach Tam Li"};
        Random random = new Random();
        int index = random.nextInt(categories.length);
        return categories[index];
    }
    
    // Hàm tự động thêm dữ liệu
    public void addMultipleRecords(int numberOfRecords) {
        for (int i = 0; i < numberOfRecords; i++) {
            // Tạo dữ liệu ngẫu nhiên (đối với mục đích minh h`ọa)
            String name = getRandomCategory();
            String psId = String.valueOf(new Random().nextInt(10)); 
            String managerId = String.valueOf(new Random().nextInt(10)); 
            String notes = "Information of name";
            String deleted = String.valueOf(false); 
            String deletedDate =""; 
            String deletedAuthor = "";
            String modifiedDate = ""; 
            String createdDate = getCurrentDate(); 
            String enable = String.valueOf(true); 
            String nameEn = "Book";
            String createdAuthorId = String.valueOf(new Random().nextInt(10)); // Giả sử Created Author ID là số ngẫu nhiên từ 0 đến 9999
            String language = String.valueOf(new Random().nextInt(2)); // Giả sử Language là số ngẫu nhiên từ 0 đến 9

            // Thêm một bản ghi mới
            if (addRecord(name, psId, managerId, notes, deleted, deletedDate, deletedAuthor,
                    modifiedDate, createdDate, enable, nameEn, createdAuthorId, language)) {
                System.out.println("Đã thêm bản ghi thứ " + (i + 1));
            } else {
                System.out.println("Không thể thêm bản ghi thứ " + (i + 1));
            }
        }

        // Lấy danh sách sau khi thêm
        ArrayList<PgObject> items = fetchDataFromSQL();
        displayPgList(items);
    }

    
    
    
    
    

//  //***********************************HIỂN THỊ DANH SÁCH***********************************************
    public void ViewPgList(ArrayList<PgObject> items) {

        // Xóa dữ liệu cũ từ bảng
        tableModel.setRowCount(0);
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Tên*");
        tableModel.addColumn("ID Parent*");
        tableModel.addColumn("ID Manager*");
        tableModel.addColumn("Ghi chú");
        tableModel.addColumn("Đã xóa");
        tableModel.addColumn("Ngày xóa");
        tableModel.addColumn("Tác giả xóa");
        tableModel.addColumn("Ngày sửa đổi");
        tableModel.addColumn("Ngày tạo");
        tableModel.addColumn("Kích hoạt");
        tableModel.addColumn("Tên (tiếng Anh)");
        tableModel.addColumn("ID Tác giả tạo*");
        tableModel.addColumn("Ngôn ngữ*");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
      
        //Đặt độ rộng
        TableColumn column;
        for(int i=0; i < 14; i++) {
        	column = table.getColumnModel().getColumn(i);
        	if(i == 0) {
        		column.setPreferredWidth(40);
        		
        	}else {
        		if(i == 3 || i == 2 || i == 5 || i == 10 || i == 11 || i == 12 || i == 13 ) {
        			column.setPreferredWidth(80);
        		}else {
        			column.setPreferredWidth(150);
        		}
        	}
        }

        Color colorheader = new Color(185, 211, 238); 
        
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getHeight(), 32));
        header.setBackground(colorheader);
        
        table.setRowHeight(25);

        getContentPane().add(scrollPane);
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    
    // Hàm hiển thị dữ liệu ( dùng lại nhiều lần mỗi khi có thay đổi về dữ liệu )
    private void displayPgList(ArrayList<PgObject> items) {
        // Xóa dữ liệu cũ từ bảng
        tableModel.setRowCount(0);

        // Hiển thị danh sách mới
        for (PgObject item : items) {
            tableModel.addRow(new Object[]{
                    item.getPg_id(), 
                    item.getPg_name(), 
                    item.getPg_ps_id(), 
                    item.getPg_manager_id(),
                    item.getPg_notes(), 
                    item.isPg_delete(), 
                    item.getPg_deleted_date(),
                    item.getPg_deleted_author(), 
                    item.getPg_modified_date(), 
                    item.getPg_created_date(),
                    item.isPg_enable(), 
                    item.getPg_name_en(), 
                    item.getPg_created_author_id(),
                    item.getPg_language()
            });
        }
        
        // Đặt giá trị isTableDisplayed thành true khi bảng đã được hiển thị
        isTableDisplayed = true;
    }
//  //***********************************END*****************************************
    
    
    

    
    
    
    
    
    
    
//  //***********************************THÊM SẢN PHẨM*************************************
    //view
    private JTextField nameField;
    private JTextField psIdField;
    private JTextField managerIdField;
    private JTextField notesField;
    private JTextField deletedField;
    private JTextField deletedDateField;
    private JTextField deletedAuthorField;
    private JTextField modifiedDateField;
    private JTextField createdDateField;
    private JTextField enableField;
    private JTextField nameEnField;
    private JTextField createdAuthorIdField;
    private JTextField languageField;
    
    // Hiển thị bảng thêm dữ liệu
    private void showAddDialog() {
        // Tạo JFrame cho cửa sổ nhập liệu
        JFrame addFrame = new JFrame("Thêm sản phẩm mới");
        addFrame.setSize(300, 400);
        addFrame.getContentPane().setLayout(new GridLayout(15, 2));

        // Tạo các thành phần nhập liệu và nhãn
        nameField = new JTextField();
        psIdField = new JTextField();
        managerIdField = new JTextField();
        notesField = new JTextField();
        deletedField = new JTextField();
        deletedDateField = new JTextField();
        deletedAuthorField = new JTextField();
        modifiedDateField = new JTextField();
        createdDateField = new JTextField();
        enableField = new JTextField();
        nameEnField = new JTextField();
        createdAuthorIdField = new JTextField();
        languageField = new JTextField();

        JLabel nameLabel = new JLabel("Tên:");
        JLabel psIdLabel = new JLabel("ID Parent:");
        JLabel managerIdLabel = new JLabel("ID Manager:");
        JLabel notesLabel = new JLabel("Ghi chú:");
        JLabel deletedLabel = new JLabel("Đã xóa:");
        JLabel deletedDateLabel = new JLabel("Ngày xóa:");
        JLabel deletedAuthorLabel = new JLabel("Tác giả xóa:");
        JLabel modifiedDateLabel = new JLabel("Ngày sửa đổi:");
        JLabel createdDateLabel = new JLabel("Ngày tạo:");
        JLabel enableLabel = new JLabel("Kích hoạt:");
        JLabel nameEnLabel = new JLabel("Tên (tiếng Anh):");
        JLabel createdAuthorIdLabel = new JLabel("ID Tác giả tạo:");
        JLabel languageLabel = new JLabel("Ngôn ngữ:");
        
        // Tạo nút "Đồng ý" để xác nhận nhập liệu
        JButton addButton = new JButton("Đồng ý");


        // Thêm các thành phần vào JFrame
        addFrame.getContentPane().add(nameLabel);
        addFrame.getContentPane().add(nameField);
        addFrame.getContentPane().add(psIdLabel);
        addFrame.getContentPane().add(psIdField);
        addFrame.getContentPane().add(managerIdLabel);
        addFrame.getContentPane().add(managerIdField);
        addFrame.getContentPane().add(notesLabel);
        addFrame.getContentPane().add(notesField);
        addFrame.getContentPane().add(deletedLabel);
        addFrame.getContentPane().add(deletedField);
        addFrame.getContentPane().add(deletedDateLabel);
        addFrame.getContentPane().add(deletedDateField);
        addFrame.getContentPane().add(deletedAuthorLabel);
        addFrame.getContentPane().add(deletedAuthorField);
        addFrame.getContentPane().add(modifiedDateLabel);
        addFrame.getContentPane().add(modifiedDateField);
        addFrame.getContentPane().add(createdDateLabel);
        addFrame.getContentPane().add(createdDateField);
        addFrame.getContentPane().add(enableLabel);
        addFrame.getContentPane().add(enableField);
        addFrame.getContentPane().add(nameEnLabel);
        addFrame.getContentPane().add(nameEnField);
        addFrame.getContentPane().add(createdAuthorIdLabel);
        addFrame.getContentPane().add(createdAuthorIdField);
        addFrame.getContentPane().add(languageLabel);
        addFrame.getContentPane().add(languageField);
        addFrame.getContentPane().add(new JLabel()); // Khoảng trắng
        addFrame.getContentPane().add(new JLabel()); // Khoảng trắng
        addFrame.getContentPane().add(new JLabel()); // Khoảng trắng
        addFrame.getContentPane().add(addButton);
        

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Set giá trị cho trường Ngày tạo trước khi thêm dữ liệu
                createdDateField.setText(getCurrentDate()); // Sử dụng hàm getCurrentDate()
                
            	addDataAndDisplay();

                // Đóng cửa sổ nhập liệu
                addFrame.dispose();
            }
        });
        
     

        // Đặt JFrame ở giữa màn hình
        addFrame.setLocationRelativeTo(null);
        // Hiển thị JFrame
        addFrame.setVisible(true);
    }
    
    
    
//    // Hàm lấy ngày hiện tại dưới định dạng chuỗi
//    private String getCurrentDate() {
//        java.util.Date currentDate = new java.util.Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
//        return dateFormat.format(currentDate);
//    }
    
    
    // lấy dữ liệu nhập vào truyền vào hàm add dữ liệu vào SQl(addRecord) ở PG và hiển thị dữ liệu
    public void addDataAndDisplay() {
    	// Lấy dữ liệu từ các trường nhập liệu
        String name = nameField.getText();
        String psId = psIdField.getText();
        String managerId = managerIdField.getText();
        String notes = notesField.getText();
        String deleted = deletedField.getText();
        String deletedDate = deletedDateField.getText();
        String deletedAuthor = deletedAuthorField.getText();
        String modifiedDate = modifiedDateField.getText();
        String createdDate = createdDateField.getText();
        String enable = enableField.getText();
        String nameEn = nameEnField.getText();
        String createdAuthorId = createdAuthorIdField.getText();
        String language = languageField.getText();

        // Thêm một bản ghi mới
        if (addRecord(name, psId, managerId, notes, deleted, deletedDate, deletedAuthor,
                modifiedDate, createdDate, enable, nameEn, createdAuthorId, language)) {
        	System.out.println("\\n-----Thành công-----\\n");

            // Lấy danh sách 
            ArrayList<PgObject> items = fetchDataFromSQL();
            displayPgList(items);
            
        } else {
            System.out.println("\n-----Không thành công-----\n");
        }
    }
    
    
    // Add dữ liệu vào MySQL
    private boolean addRecord(String name, String psId, String managerId, String notes, String deleted,
		            String deletedDate, String deletedAuthor, String modifiedDate, String createdDate,
		            String enable, String nameEn, String createdAuthorId, String language) {
		PgObject npg = new PgObject();
		npg.setPg_name(name);
		npg.setPg_ps_id(Byte.parseByte(psId));
		npg.setPg_manager_id(Integer.parseInt(managerId));
		npg.setPg_notes(notes);
		npg.setPg_delete(Boolean.parseBoolean(deleted));
		npg.setPg_deleted_date(deletedDate);
		npg.setPg_deleted_author(deletedAuthor);
		npg.setPg_modified_date(modifiedDate);
		npg.setPg_created_date(createdDate);
		npg.setPg_enable(Boolean.parseBoolean(enable));
		npg.setPg_name_en(nameEn);
		npg.setPg_created_author_id(Integer.parseInt(createdAuthorId));
		npg.setPg_language(Byte.parseByte(language));
		
		Pg pg = new Pg();
		return pg.addPg(npg);
	}
    
//  //***********************************END***************************************************************
    
 
    
    
    
    
    
    
//  //***********************************CẬP NHẬT**********************************************************
    //private void showUpdateDialog(int selectedRow) {
    private void showUpdateDialog() {
    	int selectedRow = table.getSelectedRow();
    	if (selectedRow >= 0) {
	    	
	        JDialog updateDialog = new JDialog(this, "Cập nhật", true);
	        updateDialog.setSize(300, 400);
	        updateDialog.getContentPane().setLayout(new GridLayout(15, 2));
	        
	        JLabel nameLabel = new JLabel("Tên:");
	        JTextField nameField = new JTextField();
	        nameField.setText(table.getValueAt(selectedRow, 1).toString());
	
	        JLabel psIdLabel = new JLabel("ID Parent:");
	        JTextField psIdField = new JTextField();
	        psIdField.setText(table.getValueAt(selectedRow, 2).toString());
	
	        JLabel managerIdLabel = new JLabel("ID Manager:");
	        JTextField managerIdField = new JTextField();
	        managerIdField.setText(table.getValueAt(selectedRow, 3).toString());
	
	        // Tạo JLabel và JTextField cho trường dữ liệu "Ghi chú"
	        JLabel notesLabel = new JLabel("Ghi chú:");
	        JTextField notesField = new JTextField();
	        notesField.setText(table.getValueAt(selectedRow, 4).toString());
	
	        // Tạo JLabel và JTextField cho trường dữ liệu "Đã xóa"
	        JLabel deletedLabel = new JLabel("Đã xóa:");
	        JTextField deletedField = new JTextField();
	        deletedField.setText(table.getValueAt(selectedRow, 5).toString());
	
	        // Tạo JLabel và JTextField cho trường dữ liệu "Ngày xóa"
	        JLabel deletedDateLabel = new JLabel("Ngày xóa:");
	        JTextField deletedDateField = new JTextField();
	        deletedDateField.setText(table.getValueAt(selectedRow, 6).toString());
	
	        // Tạo JLabel và JTextField cho trường dữ liệu "Tác giả xóa"
	        JLabel deletedAuthorLabel = new JLabel("Tác giả xóa:");
	        JTextField deletedAuthorField = new JTextField();
	        deletedAuthorField.setText(table.getValueAt(selectedRow, 7).toString());
	
	        // Tạo JLabel và JTextField cho trường dữ liệu "Ngày sửa đổi"
	        JLabel modifiedDateLabel = new JLabel("Ngày sửa đổi:");
	        JTextField modifiedDateField = new JTextField();
	        modifiedDateField.setText(table.getValueAt(selectedRow, 8).toString());
	
	        // Tạo JLabel và JTextField cho trường dữ liệu "Ngày tạo"
	        JLabel createdDateLabel = new JLabel("Ngày tạo:");
	        JTextField createdDateField = new JTextField();
	        createdDateField.setText(table.getValueAt(selectedRow, 9).toString());
	
	        // Tạo JLabel và JTextField cho trường dữ liệu "Kích hoạt"
	        JLabel enableLabel = new JLabel("Kích hoạt:");
	        JTextField enableField = new JTextField();
	        enableField.setText(table.getValueAt(selectedRow, 10).toString());
	
	        // Tạo JLabel và JTextField cho trường dữ liệu "Tên (tiếng Anh)"
	        JLabel nameEnLabel = new JLabel("Tên (tiếng Anh):");
	        JTextField nameEnField = new JTextField();
	        nameEnField.setText(table.getValueAt(selectedRow, 11).toString());
	
	        // Tạo JLabel và JTextField cho trường dữ liệu "ID Tác giả tạo"
	        JLabel createdAuthorIdLabel = new JLabel("ID Tác giả tạo:");
	        JTextField createdAuthorIdField = new JTextField();
	        createdAuthorIdField.setText(table.getValueAt(selectedRow, 12).toString());
	
	        // Tạo JLabel và JTextField cho trường dữ liệu "Ngôn ngữ"
	        JLabel languageLabel = new JLabel("Ngôn ngữ:");
	        JTextField languageField = new JTextField();
	        languageField.setText(table.getValueAt(selectedRow, 13).toString());
	
	        
	        // Tạo nút "OK" cho cửa sổ cập nhật
	        JButton okButton = new JButton("Đồng ý");
	        okButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // Lấy giá trị từ các JTextField và cập nhật dữ liệu vào bảng
	            	String newName = nameField.getText();
	                String newPsId = psIdField.getText();
	                String newManagerId = managerIdField.getText();
	
	                // Lấy giá trị từ các JTextField
	                String newNotes = notesField.getText();
	                String newDeleted = deletedField.getText();
	                String newDeletedDate = deletedDateField.getText();
	                String newDeletedAuthor = deletedAuthorField.getText();
	                String newModifiedDate = getCurrentDate();
	                String newCreatedDate = createdDateField.getText();
	                String newEnable = enableField.getText();
	                String newNameEn = nameEnField.getText();
	                String newCreatedAuthorId = createdAuthorIdField.getText();
	                String newLanguage = languageField.getText();
	
	                // Cập nhật dữ liệu trong bảng
	                tableModel.setValueAt(newName, selectedRow, 1);
	                tableModel.setValueAt(newPsId, selectedRow, 2);
	                tableModel.setValueAt(newManagerId, selectedRow, 3);
	                tableModel.setValueAt(newNotes, selectedRow, 4);
	                tableModel.setValueAt(newDeleted, selectedRow, 5);
	                tableModel.setValueAt(newDeletedDate, selectedRow, 6);
	                tableModel.setValueAt(newDeletedAuthor, selectedRow, 7);
	                tableModel.setValueAt(newModifiedDate, selectedRow, 8);
	                tableModel.setValueAt(newCreatedDate, selectedRow, 9);
	                tableModel.setValueAt(newEnable, selectedRow, 10);
	                tableModel.setValueAt(newNameEn, selectedRow, 11);
	                tableModel.setValueAt(newCreatedAuthorId, selectedRow, 12);
	                tableModel.setValueAt(newLanguage, selectedRow, 13);
	
	                // Cập nhật dữ liệu trong MySQL sử dụng ConnectionPool
	                updateDataInDatabase(selectedRow, newName, newPsId, newManagerId, newNotes, newDeleted,
	                    newDeletedDate, newDeletedAuthor, newModifiedDate, newCreatedDate, newEnable,
	                    newNameEn, newCreatedAuthorId, newLanguage);
	                
	             // Set giá trị cho trường Ngày tạo trước khi thêm dữ liệu
//	                modifiedDateField.setText(getCurrentDate()); // Sử dụng hàm getCurrentDate()
	
	                updateDialog.dispose();
	            }
	            
	        });
	
	        // Thêm các JLabel và JTextField vào updateDialog
	        updateDialog.getContentPane().add(nameLabel);
	        updateDialog.getContentPane().add(nameField);
	        updateDialog.getContentPane().add(psIdLabel);
	        updateDialog.getContentPane().add(psIdField);
	        updateDialog.getContentPane().add(managerIdLabel);
	        updateDialog.getContentPane().add(managerIdField);
	        updateDialog.getContentPane().add(notesLabel);
	        updateDialog.getContentPane().add(notesField);
	        updateDialog.getContentPane().add(deletedLabel);
	        updateDialog.getContentPane().add(deletedField);
	        updateDialog.getContentPane().add(deletedDateLabel);
	        updateDialog.getContentPane().add(deletedDateField);
	        updateDialog.getContentPane().add(deletedAuthorLabel);
	        updateDialog.getContentPane().add(deletedAuthorField);
	        updateDialog.getContentPane().add(modifiedDateLabel);
	        updateDialog.getContentPane().add(modifiedDateField);
	        updateDialog.getContentPane().add(createdDateLabel);
	        updateDialog.getContentPane().add(createdDateField);
	        updateDialog.getContentPane().add(enableLabel);
	        updateDialog.getContentPane().add(enableField);
	        updateDialog.getContentPane().add(nameEnLabel);
	        updateDialog.getContentPane().add(nameEnField);
	        updateDialog.getContentPane().add(createdAuthorIdLabel);
	        updateDialog.getContentPane().add(createdAuthorIdField);
	        updateDialog.getContentPane().add(languageLabel);
	        updateDialog.getContentPane().add(languageField);
	        updateDialog.getContentPane().add(new JLabel()); // Khoảng trắng
	        updateDialog.getContentPane().add(new JLabel()); // Khoảng trắng
	        updateDialog.getContentPane().add(new JLabel()); // Khoảng trắng
	        updateDialog.getContentPane().add(okButton); // Thêm nút "OK" vào cửa sổ cập nhật
	        
	        updateDialog.setLocationRelativeTo(this);
	        updateDialog.setVisible(true);
        
    	} else {
    		JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm để cập nhật.", "Message", JOptionPane.INFORMATION_MESSAGE);
    	}
    }

    private void updateDataInDatabase(int selectedRow, String newName, String newPsId, String newManagerId,
		            String newNotes, String newDeleted, String newDeletedDate, String newDeletedAuthor,
		            String newModifiedDate, String newCreatedDate, String newEnable, String newNameEn,
		            String newCreatedAuthorId, String newLanguage) {
		Connection conn = null;
		try {
		// Lấy kết nối từ ConnectionPool
		conn = connectionPool.getConnection("viewPg");
		
		// Tạo câu truy vấn UPDATE SQL dựa trên thông tin nhập từ form
		String updateQuery = "UPDATE tblpg SET pg_name = ?, pg_ps_id = ?, pg_manager_id = ?, pg_notes = ?, " +
		"pg_delete = ?, pg_deleted_date = ?, pg_deleted_author = ?, pg_modified_date = ?, pg_created_date = ?, " +
		"pg_enable = ?, pg_name_en = ?, pg_created_author_id = ?, pg_language = ? WHERE pg_id = ?";
		
		PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
		preparedStatement.setString(1, newName);
		preparedStatement.setByte(2, Byte.parseByte(newPsId));
		preparedStatement.setInt(3, Integer.parseInt(newManagerId));
		preparedStatement.setString(4, newNotes);
		preparedStatement.setBoolean(5, Boolean.parseBoolean(newDeleted));
		preparedStatement.setString(6, newDeletedDate);
		preparedStatement.setString(7, newDeletedAuthor);
		preparedStatement.setString(8, newModifiedDate);
		preparedStatement.setString(9, newCreatedDate);
		preparedStatement.setBoolean(10, Boolean.parseBoolean(newEnable));
		preparedStatement.setString(11, newNameEn);
		preparedStatement.setInt(12, Integer.parseInt(newCreatedAuthorId));
		preparedStatement.setByte(13, Byte.parseByte(newLanguage));
		preparedStatement.setShort(14, (short) tableModel.getValueAt(selectedRow, 0));
		
		// Thực hiện truy vấn UPDATE
		int rowsUpdated = preparedStatement.executeUpdate();
		
//		 Kiểm tra nếu cập nhật thành công
		if (rowsUpdated > 0) {
			JOptionPane.showMessageDialog(this, "Dữ liệu đã được cập nhật.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Không có dữ liệu nào được cập nhật.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
		
		// Trả kết nối về ConnectionPool
		connectionPool.releaseConnection(conn, "viewPg");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật dữ liệu trong cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
//  //***********************************END***************************************************************
    
    
    
    
    
    
    
    
    
    
    
    
    
//  //***********************************TÌM KIẾM***********************************************
    // Xử lí name, nếu có thì chạy hàm tìm kiếm dữ liệu theo name và hiển thị ra dữ liệu
    private void handleSearchButton() {
        // Hiển thị hộp thoại để nhập tên sản phẩm
        String pgName = JOptionPane.showInputDialog(this, "Nhập tên sản phẩm:");
        if (pgName != null) {
        	Pg pg = new Pg();
            // Thực hiện tìm kiếm trong dữ liệu trong Pg
            ArrayList<PgObject> searchResults = pg.searchPgByName(pgName);
            if(!searchResults.isEmpty()) {
            	displayPgList(searchResults);
            }else {
            	JOptionPane.showMessageDialog(this, "Sản phẩm không có trong danh sách.");
            }
            // Cập nhật bảng hiển thị với kết quả tìm kiếm
            //displayPgList(searchResults);
        }
    }
//  //***********************************END****************************************************

    
    
    
    
    
    
//  //***********************************XÓA SẢN PHẨM***********************************************
    private void handleDeleteButton() {
    	int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int option = JOptionPane.showConfirmDialog(this,"Bạn có chắc chắn muốn xóa sản phẩm không?","Xác nhận xóa sản phẩm",JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                short pgId = (short) tableModel.getValueAt(selectedRow, 0);

                boolean deleteSuccess = deleteProductFromDatabase(pgId);

                if (deleteSuccess) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, "Bạn đã xóa thành công.");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa không thành công.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để xóa.");
        }
    }
    
    // Gọi vào hàm xóa sản phẩm theo id trong Pg
    private boolean deleteProductFromDatabase(int pgId) {
        Pg s = new Pg();
        return s.deletePg((short) pgId);
    }
//  //***********************************END*************************************
    
    
    
    
    
    
    
    
    
    
    
//  //***********************************THỐNG KÊ*************************************
    private void calculateStatistics() {
        int rowCount = tableModel.getRowCount(); // Số dòng trong bảng
        searchField.setText("Số sản phẩm: " + rowCount); // Hiển thị kết quả thống kê
        // Tạo một Font mới với kích thước chữ là 18
        Font font = new Font("Arial", Font.PLAIN, 15);
        
        // Đặt Font cho JTextField
        searchField.setFont(font);
    }
//  //***********************************END*************************************
    
    
    
    
    
    
//  //***********************************Constructor ViewPG*************************************
    public ViewPg() {
    	connectionPool = new ConnectionPoolImpl();
    	

        

    	
        setTitle("Product Group");
        //setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        URL iconUrl = ViewPg.class.getResource("haui.png");
        Image iconImage = Toolkit.getDefaultToolkit().createImage(iconUrl);
        this.setIconImage(iconImage);
        

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        
        Font font = new Font("Arial", Font.BOLD, 14);
        
        Color colorbutton = new Color(0, 206, 209); 
        
        ImageIcon displayIcon = new ImageIcon("haui.png");
        
        JButton addMultipleRecordsButton = new JButton("Thêm Nhiều");
        buttonPanel.add(addMultipleRecordsButton);
        addMultipleRecordsButton.setFont(font);
        addMultipleRecordsButton.setBackground(colorbutton);
        addMultipleRecordsButton.setPreferredSize(new Dimension(160, 35));
        
        JButton displayButton = new JButton("Hiển thị");
        buttonPanel.add(displayButton);
        displayButton.setFont(font);
        displayButton.setBackground(colorbutton);
        displayButton.setPreferredSize(new Dimension(100, 35));
        
        JButton addButton = new JButton("Thêm");
        buttonPanel.add(addButton);
        addButton.setFont(font);
        addButton.setBackground(colorbutton);
        addButton.setPreferredSize(new Dimension(100, 35));
        
        JButton updateButton = new JButton("Cập nhật");
        buttonPanel.add(updateButton);
        updateButton.setFont(font);
        updateButton.setBackground(colorbutton);
        updateButton.setPreferredSize(new Dimension(100, 35));

        JButton searchButton = new JButton("Tìm kiếm");
        buttonPanel.add(searchButton);
        searchButton.setFont(font);
        searchButton.setBackground(colorbutton);
        searchButton.setPreferredSize(new Dimension(100, 35));

        JButton deleteButton = new JButton("Xóa");
        buttonPanel.add(deleteButton);
        deleteButton.setFont(font);
        deleteButton.setBackground(colorbutton);
        deleteButton.setPreferredSize(new Dimension(100, 35));
        

        JButton statsButton = new JButton("Thống kê");
        buttonPanel.add(statsButton);
        statsButton.setFont(font);
        statsButton.setBackground(colorbutton);
        statsButton.setPreferredSize(new Dimension(100, 35));

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30)); // Thiết lập kích thước mới
        buttonPanel.add(searchField);
        
        
     // Sự kiện khi nút "Thêm Nhiều Bản Ghi" được nhấn
        addMultipleRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Yêu cầu người dùng nhập số lượng bản ghi
                String input = JOptionPane.showInputDialog("Nhập số lượng bản ghi cần thêm:");

                // Kiểm tra xem người dùng đã nhập hay chưa và xử lý nếu có
                if (input != null && !input.isEmpty()) {
                    try {
                        int numberOfRecords = Integer.parseInt(input);
                        // Gọi hàm để thêm nhiều bản ghi
                        addMultipleRecords(numberOfRecords);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập số nguyên hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Pg pg = new Pg();
            	ArrayList<PgObject> items = fetchDataFromSQL();
                // Gọi hàm hiển thị dữ liệu lên bảng
                displayPgList(items);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	showAddDialog();
            }
        });
        

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isTableDisplayed) {
                	UIManager.put("OptionPane.messageForeground", Color.RED);
                    JOptionPane.showMessageDialog(null, "Bạn phải hiển thị bảng dữ liệu trước khi cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                } else {
                	showUpdateDialog();
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	handleSearchButton();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!isTableDisplayed) {
                    JOptionPane.showMessageDialog(null, "Bạn phải hiển thị bảng dữ liệu trước khi xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                } else {
                	handleDeleteButton();
                }
            }
        });
        

        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!isTableDisplayed) {
                    JOptionPane.showMessageDialog(null, "Bạn phải hiển thị bảng dữ liệu trước khi thống kê.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                } else {
                	calculateStatistics();
                }
            }
        });
    }
    
    

    public static void main(String[] args) {
        Pg pg = new Pg();
        ArrayList<PgObject> items = pg.getPgObject(null, (byte) 100);
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ViewPg viewPg = new ViewPg();
                viewPg.setVisible(true);

//                ViewPgList để hiển thị bảng
                viewPg.ViewPgList(items);
            }
        });
    }

}
