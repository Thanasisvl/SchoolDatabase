import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	Connection connection;
	DriverManager dm;
	Statement stm;
	
	public Database() {
		// TODO Auto-generated constructor stub
	}
	
	public java.sql.Connection connect(String _DB_URL, String _username, String _password) {
		try {
			connection = DriverManager.getConnection(_DB_URL, _username, _password);
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	//search database students
	public void executeSelectStudent(String sql) {
		try {
			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next())
		      {
		        String firstName = rs.getString("first_name");
		        String lastName = rs.getString("last_name");
		        String facultyid = rs.getString("faculty_id");
		        
		        System.out.format("%s, %s, %s\n", firstName, lastName, facultyid);
		      }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void executeSelectWorker(String sql) {
		try {
			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next())
		      {
		        String firstName = rs.getString("first_name");
		        String lastName = rs.getString("last_name");
		        double weeksalary = rs.getDouble("week_salary");
		        int workhours = rs.getInt("workhours_perday");
		        double hoursalary = rs.getDouble("salary_perhour");
		        
		        
		        System.out.format("%s, %s, %s, %s, %s\n", firstName, lastName, weeksalary, workhours, hoursalary);
		      }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//insert to database tables
	public void executeUpdate(String sql) {
		try {
			stm = connection.createStatement();
			stm.executeUpdate(sql);
			System.out.println("Database update complete.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
