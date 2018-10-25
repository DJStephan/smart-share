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

	public byte[] getFile(DownloadRequestDto downloadRequest) {
		if(!(this.fileInDatabase(downloadRequest.getFileName()))) {
			return new byte[0];
		}
		String dbPassword = null;
		Timestamp experiation = new Timestamp(System.currentTimeMillis());;
		int maxDownloads = 0;
		int totalDownloads = 0;
		byte[] file = null;
		Timestamp requestTime = new Timestamp(System.currentTimeMillis());
        
        try (
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        ) {
            String sqlQuery = "SELECT * FROM smartshare.files WHERE file_name = ?";
            PreparedStatement preparedStatment = connection.prepareStatement(sqlQuery);
            preparedStatment.setString(1, downloadRequest.getFileName());
            ResultSet resultSet = preparedStatment.executeQuery();
            
            while (resultSet.next()) {
               dbPassword = resultSet.getString(8);
               experiation = resultSet.getTimestamp("expiry_time");
               maxDownloads = resultSet.getInt("max_downloads");
               totalDownloads = resultSet.getInt("total_downloads");
               file = resultSet.getBytes("file");
               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(dbPassword.equals(downloadRequest.getPassword())){
        	if(totalDownloads < maxDownloads && requestTime.before(experiation)) {
        		this.updateDownloadCount(downloadRequest.getFileName(), ++totalDownloads);
        		if(totalDownloads >= maxDownloads) {
        			this.deleteFile(downloadRequest.getFileName());
        		}
        		return file;
        	}else {
        		this.deleteFile(downloadRequest.getFileName());
        	}
        }
        return new byte[0];
    }

	public boolean saveFile(UploadRequestDto uploadRequest) {
		int totalDownloads = 0;
		Timestamp ct = new Timestamp(System.currentTimeMillis());
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
			// Create and populate a prepared statement
			String sql = "INSERT INTO smartshare.files (file_name, file, time_created, expiry_time, max_downloads, total_downloads, password) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, uploadRequest.getFileName());
			preparedStatement.setBytes(2, uploadRequest.getFile());
			preparedStatement.setTimestamp(3, ct);
			preparedStatement.setTimestamp(4,
					new Timestamp(System.currentTimeMillis() + uploadRequest.getExpiration() * 1000 * 60));
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

	public boolean deleteFile(String fileName) {
		int deleted = 0;

		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
			String sqlString = "DELETE FROM smartshare.files WHERE file_name = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
			preparedStatement.setString(1, fileName);
			deleted = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return deleted > 0;

	}

	public boolean fileInDatabase(String fileName) {
		System.out.println("checking for file");
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
			System.out.println("building query");
			String sqlQuery = "SELECT * FROM smartshare.files WHERE file_name = ?";
			PreparedStatement preparedStatment = connection.prepareStatement(sqlQuery);
			preparedStatment.setString(1, fileName);
			ResultSet resultSet = preparedStatment.executeQuery();
//			System.out.println("query executed");
			if (resultSet.next()) {
				System.out.println("file already exists");
				return true;

			}else {
				System.out.println("file does not exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateDownloadCount(String fileName, int newCount) {
		int updated = 0;
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
			String sqlQuery = "UPDATE smartshare.files SET total_downloads = ? WHERE file_name = ?";
			PreparedStatement preparedStatment = connection.prepareStatement(sqlQuery);
			preparedStatment.setInt(1, newCount);
			preparedStatment.setString(2, fileName);
			updated = preparedStatment.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return updated > 0;
	}
	
	public String View(String fileName, String password) {
		String dbPassword = null;
		int dbDownloads = 0;
		int maxDownloads = 0;
		Timestamp expiration = null;
		Timestamp created = null;
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
			System.out.println("building query");
			String sqlQuery = "SELECT * FROM smartshare.files WHERE file_name = ?";
			PreparedStatement preparedStatment = connection.prepareStatement(sqlQuery);
			preparedStatment.setString(1, fileName);
			ResultSet resultSet = preparedStatment.executeQuery();
//			System.out.println("query executed");
			while(resultSet.next()) {
				dbPassword = resultSet.getString(8);
				dbDownloads = resultSet.getInt(7);
				expiration = resultSet.getTimestamp(5);
				created = resultSet.getTimestamp(4);
				maxDownloads = resultSet.getInt(6);
			}
			
			if(dbPassword.equals(password)) {
				long timeLeft = expiration.compareTo(created)/1000/60; //minutes left
				int downloadsLeft = maxDownloads - dbDownloads;
				return fileName + " was created at " + created + " , there are " + downloadsLeft + " , and " + timeLeft + "minutes to download file.";
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "File information not found";
	}

}
