package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.UploadRequestDto;

public class SmartShareDao {
	private static final String URL = "jdbc:postgresql://localhost:5432/postgres/smartshare";
	private static final String USER = "postgres";
	private static final String PASSWORD = "bondstone";

	public SmartShareDao() {

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to load the postgreSQL Driver.");
		}

	}

//	public byte[] getFile(DownloadRequestDto downloadRequest) {
//		String dbPassword;
//		Timestamp experiation;
//		int maxDownloads;
//		int totalDownloads;
//		byte[] file;
//        
//        try (
//            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//        ) {
//            String sqlQuery = "SELECT * FROM smartshare.files WHERE file_name = ?";
//            PreparedStatement preparedStatment = connection.prepareStatement(sqlQuery);
//            preparedStatment.setString(1, fileName);
//            ResultSet resultSet = preparedStatment.executeQuery();
//            while (resultSet.next()) {
//               dbPassword = resultSet.getNString("password");
//               experiation = resultSet.getTimestamp("expiry_time");
//               maxDownloads = resultSet.getInt("max_downloads");
//               totalDownloads = resultSet.getInt("total_downloads");
//               file = resultSet.getBytes("file");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if(dbPassword.equals(downloadRequest.getPassword())){
//        	if(totalDownloads < maxDownloads && downloadRequest.getRequestTime().before(experiation)) {
//        		return file;
//        	}else {
//        		//this.deleteFile(fileName);
//        	}
//        }
//        return new byte[0];
//    }
	
    public boolean saveFile (byte[] file, UploadRequestDto uploadRequest) {
    	int totalDownloads = 0;
    	Timestamp ct = new Timestamp(System.currentTimeMillis());
        try (
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        ) {
            // Create and populate a prepared statement
            String sql = "INSERT INTO smartshare.files (file_name, file, time_created, expiry_time, max_downloads, total_downloads, password) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uploadRequest.getFileName());
            preparedStatement.setBytes(2, file);
            preparedStatement.setTimestamp(3, ct);
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis() + uploadRequest.getExpiration() * 1000*60));
            preparedStatement.setInt(5, uploadRequest.getMaxdownloads());
            preparedStatement.setInt(6, totalDownloads);
            preparedStatement.setString(7, uploadRequest.getPassword());

            // Execute the prepared statement to save the user to the database
            preparedStatement.executeUpdate();

          

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
