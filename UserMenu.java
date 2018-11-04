import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserMenu {
	
	
	public void userMenu() {
		
		String url = "jdbc:mysql://localhost:3306";
		Database db = new Database();
		db.connect(url, "root", "inserPassword");
		
		boolean flag;
		
		do {
			System.out.println("Welcome to our database!\n"
					+ "What would you like to do?\n"
					+ "Press c to insert someone in our databases.\n"
					+ "Press r to view something from our databases.\n" 
					+ "Press u to update our records.\n"
					+ "Press d to delete someone from our databases.\n");
			Scanner scan = new Scanner(System.in);
			String userinput = scan.next().toLowerCase();
			
			switch (userinput) {
			// Create entry in database
			case "c":
				System.out.println("Would you like to create a student or a faculty member?\n"
						+ "Press 1 for student or 2 for faculty member.\n");
				int userchoice = scan.nextInt();
				if (userchoice == 1) {
					System.out.println("Please enter the details of the student.\n"
							+ "Enter the name:\n");
					String username1 = scan.next();
					username1 = username1.substring(0,1).toUpperCase() + username1.substring(1).toLowerCase(); //convert the first letter to uppercase
					System.out.println("Enter the last name:\n");
					String userlastname1 = scan.next();
					userlastname1 = userlastname1.substring(0,1).toUpperCase() + userlastname1.substring(1).toLowerCase();
					System.out.println("Enter the faculty id");
					String facultyid = scan.next();
					while ((facultyid.length() > 10) || (facultyid.length() < 5) || facultyid.matches("^[a-zA-Z0-9]+$") != true) {
						System.out.println("Faculty number incorrect!"); //checks if the faculty id is between 5 and 10 characters
						System.out.println("Please enter faculty id."); //checks if the faculty id contains only alphanumeric characters
						String facid = scan.next();
						facultyid = facid;
					}
					String student = ("INSERT INTO humanity.students (student_id, first_name, last_name, faculty_id) values (?, ?, ?, ?)");
					PreparedStatement prestm;
					try {
						prestm = db.connection.prepareStatement(student);
						prestm.setString(1, null);
						prestm.setString(2, username1);
						prestm.setString(3, userlastname1);
						prestm.setString(4, facultyid);
						prestm.executeUpdate();
						System.out.println("Insert complete.");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					break;
				} else if (userchoice == 2){
					System.out.println("Please enter the details of the faculty member.\n"
							+ "Enter the name:\n");
					String username2 = scan.next();
					username2 = username2.substring(0,1).toUpperCase() + username2.substring(1).toLowerCase();
					System.out.println("Enter the last name:\n");
					String userlastname2 = scan.next();
					userlastname2 = userlastname2.substring(0,1).toUpperCase() + userlastname2.substring(1).toLowerCase();
					System.out.println("Enter the weekly salary.");
					double weekly = scan.nextDouble();
					while (weekly < 11) { //checks if weekly salary is less than 10
						System.out.println("Weekly salary too low. You should feel bad.");
						System.out.println("Please enter new weekly salary.");
						double weeksal = scan.nextDouble();
						weekly = weeksal;
						
					}
					System.out.println("Enter hours per day.");
					int hours = scan.nextInt();
					while (hours > 12) { //checks if daily hours are more than 12
						System.out.println("Too many hours. Give your employees some time off.");
						System.out.println("Enter hours per day.");
						int hourstemp = scan.nextInt();
						hours = hourstemp;
					}
					double hourly = weekly/(hours*5);
					String faculty = ("INSERT INTO humanity.workers (worker_id, first_name, last_name, week_salary, workhours_perday, salary_perhour) values (?, ?, ?, ?, ?, ?)");
					PreparedStatement prestm;
					try {
						prestm = db.connection.prepareStatement(faculty);
						prestm.setString(1, null);
						prestm.setString(2, username2);
						prestm.setString(3, userlastname2);
						prestm.setDouble(4, weekly);
						prestm.setInt(5, hours);
						prestm.setDouble(6, hourly);
						prestm.executeUpdate();
						System.out.println("Insert complete.");
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					break;
				}				
				break;
			// Read entries from database	
			case "r":
				System.out.println("Please select 1 to view all students, 2 to view all faculty members or 3 to search a specific member of the database.");
				String usersearch = scan.next();
				switch (usersearch) {
				case "1":
					db.executeSelectStudent("SELECT humanity.students.first_name, humanity.students.last_name, humanity.students.faculty_id FROM `humanity`.`students`");
					//Read all students
					break;
				case "2":
					db.executeSelectWorker("SELECT humanity.workers.first_name, humanity.workers.last_name, humanity.workers.week_salary, "
							+ "humanity.workers.workhours_perday, humanity.workers.salary_perhour FROM `humanity`.`workers`");
					//read all workers
					break;
				default:
					System.out.println("Would you like to view a student or a faculty member? Press s or f accordingly."); //Read specific student or worker
					String userinput3 = scan.next().toLowerCase();
					
					switch (userinput3) {
					case "s":
						System.out.println("Please enter the last name of the student you want to view.");
						String name = scan.next();
						name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
						String queryCheck = "SELECT * from humanity.students WHERE humanity.students.last_name = ?";
						PreparedStatement s1;
						try { //checks if the name exists
							s1 = db.connection.prepareStatement(queryCheck);
							s1.setString(1, name);
							ResultSet rsq = s1.executeQuery();
							if(rsq.next()) {
							    int count = rsq.getInt(1);
							} else {
								System.out.println("The name you are searching does not exist in our database. Try again:\n");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						String namesearch = ("SELECT * FROM `humanity`.`students` WHERE students.last_name =?");
						//Read Selected Student
						PreparedStatement prstm = null;
						try {
							prstm = db.connection.prepareStatement(namesearch);
							prstm.setString(1, name);
							ResultSet rs = prstm.executeQuery();
							while (rs.next())
						      {
						        int id = rs.getInt("student_id");
						        String firstName = rs.getString("first_name");
						        String lastName = rs.getString("last_name");
						        String facultyid = rs.getString("faculty_id");
						        
						        System.out.format("%s, %s, %s, %s\n", id, firstName, lastName, facultyid); //prints the student
						      }
						} catch (SQLException e2) {
							e2.printStackTrace();
						}
						break;
					case "f":
						System.out.println("Please enter the last name of the faculty member you want to view.");
						String name2 = scan.next();
						name2 = name2.substring(0,1).toUpperCase() + name2.substring(1).toLowerCase();
						String queryCheck2 = "SELECT * from humanity.students WHERE humanity.students.last_name = ?";
						PreparedStatement s2;
						try { //checks if the worker exists
							s2 = db.connection.prepareStatement(queryCheck2);
							s2.setString(1, name2);
							ResultSet rsq2 = s2.executeQuery();
							if(rsq2.next()) {
							    int count = rsq2.getInt(1);
							} else {
								System.out.println("The name you are searching does not exist in our database. Try again:\n");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						String namesearch2 = ("SELECT * FROM `humanity`.`workers` WHERE workers.last_name =?");
						//Read Selected Worker
						PreparedStatement prpstm;
						try {
							prpstm = db.connection.prepareStatement(namesearch2);
							prpstm.setString(1, name2);
							ResultSet rs2 = prpstm.executeQuery();
							while (rs2.next())
						      {
						        int id = rs2.getInt("worker_id");
						        String firstName = rs2.getString("first_name");
						        String lastName = rs2.getString("last_name");
						        double weeksalary = rs2.getDouble("week_salary");
						        int workhours = rs2.getInt("workhours_perday");
						        double hoursalary = rs2.getDouble("salary_perhour");
						        
						        //print selected worker
						        System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, weeksalary, workhours, hoursalary);
						      }
						} catch (SQLException e3) {
							e3.printStackTrace();
						}
						break;
					
					default:
						System.out.println("Incorrect choice. Try again.");
						break;
					}
					break;
				}
				break;
			//Update Students or Faculty Members
			case "u":
				System.out.println("Press s to update a student or f to update a faculty member.");
				String inputupdate = scan.next().toLowerCase();
				switch (inputupdate) {
				
				case "s":
					System.out.println("Please enter the last name of the student you want to update.");
					String nameupdate = scan.next();
					nameupdate = nameupdate.substring(0,1).toUpperCase() + nameupdate.substring(1).toLowerCase();
					String queryCheck3 = "SELECT * from humanity.students WHERE humanity.students.last_name = ?";
					PreparedStatement s3;
					try { //checks if student exists
						s3 = db.connection.prepareStatement(queryCheck3);
						s3.setString(1, nameupdate);
						ResultSet rsq = s3.executeQuery();
						if(rsq.next()) {
						    int count = rsq.getInt(1);
						} else {
							System.out.println("The name you are searching does not exist in our database. Try again:\n");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Enter the new First name.");
					String newfirst = scan.next();
					newfirst = newfirst.substring(0,1).toUpperCase() + newfirst.substring(1).toLowerCase();
					System.out.println("Enter the new Last name");
					String newlast = scan.next();
					newlast = newlast.substring(0,1).toUpperCase() + newlast.substring(1).toLowerCase();
					System.out.println("Enter the faculty id");
					String newfacultyid = scan.next();
					while ((newfacultyid.length() > 10) || (newfacultyid.length() < 5)) {
						System.out.println("Faculty number incorrect! Too many digits.");
						System.out.println("Please enter faculty id.");
						String facid2 = scan.next();
						newfacultyid = facid2;
					}
					String update1 = ("UPDATE humanity.students SET first_name= ?, last_name=?, faculty_id=? WHERE last_name=?");
					PreparedStatement stm;
					try {
						stm = db.connection.prepareStatement(update1);
						stm.setString(1, newfirst);
						stm.setString(2, newlast);
						stm.setString(3, newfacultyid);
						stm.setString(4, nameupdate);
						stm.executeUpdate();
					} catch (SQLException e4) {
						e4.printStackTrace();
					}
					break;
				//Delete Students or Faculty members			
				default:
					System.out.println("Please enter the last name of the faculty member you want to update.");
					String nameupdate1 = scan.next();
					nameupdate1 = nameupdate1.substring(0,1).toUpperCase() + nameupdate1.substring(1).toLowerCase();
					String queryCheck4 = "SELECT * from humanity.students WHERE humanity.students.last_name = ?";
					PreparedStatement s4;
					try {
						s4 = db.connection.prepareStatement(queryCheck4);
						s4.setString(1, nameupdate1);
						ResultSet rsq4 = s4.executeQuery();
						if(rsq4.next()) {
						    int count = rsq4.getInt(1);
						} else {
							System.out.println("The name you are searching does not exist in our database. Try again:\n");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Enter the new First name.");
					String newfirst1 = scan.next();
					newfirst1 = newfirst1.substring(0,1).toUpperCase() + newfirst1.substring(1).toLowerCase();
					System.out.println("Enter the new Last name");
					String newlast1 = scan.next();
					newlast1 = newlast1.substring(0,1).toUpperCase() + newlast1.substring(1).toLowerCase();
					System.out.println("Enter the weekly salary.");
					double newweekly = scan.nextDouble();
					while (newweekly < 11) {
						System.out.println("Weekly salary too low. You should feel bad.");
						System.out.println("Please enter new weekly salary.");
						double newweeksal = scan.nextDouble();
						newweekly = newweeksal;
					}
					System.out.println("Enter how many hours the faculty member works daily.");
					int newhours = scan.nextInt();
					while (newhours > 12) {
						System.out.println("Too many hours. Give your employees some time off.");
						System.out.println("Enter hours per day.");
						int hourstemp2 = scan.nextInt();
						newhours = hourstemp2;
					}
					double newhourly = newweekly/(newhours*5);
					String update2 = ("UPDATE humanity.workers SET first_name=?, last_name=?, week_salary=?, workhours_perday=?, salary_perhour=? WHERE last_name=?");
					PreparedStatement stm2;
					try {
						stm2 = db.connection.prepareStatement(update2);
						stm2.setString(1, newfirst1);
						stm2.setString(2, newlast1);
						stm2.setDouble(3, newweekly);
						stm2.setInt(4, newhours);
						stm2.setDouble(5, newhourly);
						stm2.setString(6, nameupdate1);
						stm2.executeUpdate();
						System.out.println("Update complete.");
					} catch (SQLException e5) {
						e5.printStackTrace();
					}
					break;
				}
			
			case "d":
				System.out.println("Press 1 if you want to delete a student or 2 if you want to delete a faculty member.\n");
				int inputdel = scan.nextInt();
				switch (inputdel) {
				
				case 1:
					System.out.println("Please enter the last name of the student you want to delete.");
					String searchname = scan.next();
					searchname = searchname.substring(0,1).toUpperCase() + searchname.substring(1).toLowerCase();
					String queryCheck5 = "SELECT * from humanity.students WHERE humanity.students.last_name = ?";
					PreparedStatement s5;
					try {
						s5 = db.connection.prepareStatement(queryCheck5);
						s5.setString(1, searchname);
						ResultSet rsq5 = s5.executeQuery();
						if(rsq5.next()) {
						    int count = rsq5.getInt(1);
						} else {
							System.out.println("The name you are searching does not exist in our database. Try again:\n");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					String delname = ("DELETE FROM humanity.students WHERE last_name=?");
					PreparedStatement stm;
					try {
						stm = db.connection.prepareStatement(delname);
						stm.setString(1, searchname);
						stm.executeUpdate();
						System.out.println("Delete complete.");
					} catch (SQLException e6) {
						e6.printStackTrace();
					}
					break;
				
				default:
					System.out.println("Please enter the last name of the faculty member you want to delete.");
					String searchname2 = scan.next();
					searchname2 = searchname2.substring(0,1).toUpperCase() + searchname2.substring(1).toLowerCase();
					String queryCheck6 = "SELECT * from humanity.students WHERE humanity.students.last_name = ?";
					PreparedStatement s6;
					try {
						s6 = db.connection.prepareStatement(queryCheck6);
						s6.setString(1, searchname2);
						ResultSet rsq6 = s6.executeQuery();
						if(rsq6.next()) {
						    int count = rsq6.getInt(1);
						} else {
							System.out.println("The name you are searching does not exist in our database. Try again:\n");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					String delname2 = ("DELETE FROM humanity.students WHERE last_name=?");
					PreparedStatement stm2;
					try {
						stm2 = db.connection.prepareStatement(delname2);
						stm2.setString(1, searchname2);
						stm2.executeUpdate();
						System.out.println("Delete complete.");
					} catch (SQLException e6) {
						e6.printStackTrace();
					}
					break;
				}
				
				
			default:
				System.out.println("Press 1 if you want to print all students or 2 if you want to print all faculty members.\n");
				int input = scan.nextInt();
				if (input == 1) {
					db.executeSelectStudent("SELECT * FROM `humanity`.`students`");
				} else 
					db.executeSelectWorker("SELECT * FROM `humanity`.`workers`");
				break;
			}
			String query = "SELECT COUNT(*) FROM humanity.students";
			PreparedStatement stmt;
			try {
				stmt = db.connection.prepareStatement(query);
				ResultSet rs1 = stmt.executeQuery();
				while (rs1.next()) {
					 int numberRow = rs1.getInt("count(*)");
					 System.out.println("Current number of students: " + numberRow + ".");
				} 
			} catch (Exception ex){
				ex.printStackTrace();
			}
			String query2 = "SELECT COUNT(*) FROM humanity.workers";
			PreparedStatement stmt2;
			try {
				stmt2 = db.connection.prepareStatement(query2);
				ResultSet rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					 int numberRow = rs2.getInt("count(*)");
					 System.out.println("Current number of workers: " + numberRow + ".");
				} 
			} catch (Exception ex){
				ex.printStackTrace();
			}
			System.out.println("Would you like to continue? Y/N");
			String cont = scan.next().toUpperCase();
			if (cont.equals("Y")) {
				flag = true;
			}
			else {
				System.out.println("Closing app.");
				break;
			}
		} 
		while (flag == true);
	}
}
