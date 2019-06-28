package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class ConnectionSetup {
    	final static String DB_NAME = "THRIFTY_DB";
    	final static String TABLE_NAME = "VEHICLES";
    	final static String TABLE_NAME2 = "RENTAL_RECORDS";
    	
    public static void start() {
          //use try-with-resources Statement
          try (Connection con = getConnection(DB_NAME)) {
              
              System.out.println("Connection to database " + DB_NAME + " created successfully");
              
          } catch (Exception e) {
              System.out.println(e.getMessage());
          }
    }
    
    public static void checkTable(String table) {
		// use try-with-resources Statement
		try (Connection con = ConnectionSetup.getConnection(DB_NAME)) {

			DatabaseMetaData dbm = con.getMetaData();
			ResultSet tables = dbm.getTables(null, null, table.toUpperCase(), null);
			
			if(tables != null) {
				if (tables.next()) {
					System.out.println("Table " + table + " exists.");
				}
				else {
					System.out.println("Table " + table + " does not exist.");
				}	
				tables.close();
			} else {
				System.out.println(("Problem with retrieving database metadata"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    public static void deleteTable(String table) {
		try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			int result = stmt.executeUpdate("DROP TABLE " +table);
			
			if(result == 0) {
				System.out.println("Table " + table + " has been deleted successfully");
			} else {
				System.out.println("Table " + table + " was not deleted");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }

    //Create Vehicles table.
    public static void createTableV() {
		try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			int result = stmt.executeUpdate("CREATE TABLE VEHICLES ("
					+ "vehicle_type VARCHAR(3) NOT NULL,"
					+ "vehicleID VARCHAR(20) NOT NULL,"
					+ "Year INT NOT NULL,"
					+ "Make VARCHAR(20) NOT NULL,"
					+ "Model VARCHAR(20) NOT NULL,"
					+ "Num_seats INT NOT NULL," 
					+ "Status VARCHAR(12) NOT NULL,"
					+ "image VARCHAR(20) NULL,"
					+ "PRIMARY KEY (vehicleID))");
			if(result == 0) {
				System.out.println("Table " + TABLE_NAME + " has been created successfully");
			} else {
				System.out.println("Table " + TABLE_NAME + " is not created");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
	
    //Create Rental Records table.
    public static void createTableRental() {
		try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			int result = stmt.executeUpdate("CREATE TABLE RENTAL_RECORDS ("
					+ "recordId VARCHAR(40) NOT NULL,"
					+ "customerId VARCHAR(20) NOT NULL,"
					+ "rentDate VARCHAR(20) NOT NULL," 
					+ "estimatedReturnDate VARCHAR(15) NULL,"
					+ "actualReturnDate VARCHAR(20) NULL,"
					+ "rentalFee DECIMAL NULL,"
					+ "lateFee DECIMAL NULL,"
					+ "PRIMARY KEY (recordId))");
			if(result == 0) {
				System.out.println("Table " + TABLE_NAME2 + " has been created successfully");
			} else {
				System.out.println("Table " + TABLE_NAME2 + " is not created");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
    }
    
    //Queries some elements of the Vehicles table.
    public static void Query() {
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while(resultSet.next()) {
					System.out.printf("Vehicle ID: %s, | RentDate: %d | Number of seats: %d", resultSet.getString("vehicleID"), resultSet.getInt("Year"), resultSet.getInt("num_seats"));
					System.out.println();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    //Queries some values of the Records table.
    public static void Query2() {
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME2;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while(resultSet.next()) {
					System.out.printf("Record ID: %s, | Estimated Return Date: %s | Actual Return Date: %s", resultSet.getString("recordId"), resultSet.getString("estimatedReturnDate"), resultSet.getString("actualReturnDate"));
					System.out.println();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    //Queries Vehicles and returns a list
    public static ObservableList<String> QueryVehicle() {
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				ObservableList <String> currentDB = FXCollections.observableArrayList();
				while(resultSet.next()) {
					currentDB.add(resultSet.getString("vehicleID") + ":" + resultSet.getInt("Year") + ":" + resultSet.getString("Make") + ":" + resultSet.getString("Model")+ ":" + resultSet.getInt("Num_seats")+ ":" + resultSet.getString("Status")+ ":" + resultSet.getString("image"));
				}
				return currentDB;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    //Queries Records and returns a list
    public static ObservableList<String> QueryRental() {
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME2;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				ObservableList <String> currentDB = FXCollections.observableArrayList();
				while(resultSet.next()) {
					currentDB.add(resultSet.getString("recordId") + ":" + resultSet.getString("rentDate") + ":" + resultSet.getString("estimatedReturnDate") + ":" + resultSet.getString("actualReturnDate") + ":" + resultSet.getInt("rentalFee") + ":" + resultSet.getInt("lateFee"));
				}
				return currentDB;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    
    public static Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {
        //Registering the HSQLDB JDBC driver
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
            
        /* Database files will be created in the "database"
         * folder in the project. If no username or password is
         * specified, the default SA user and an empty password are used */
        Connection con = DriverManager.getConnection
                ("jdbc:hsqldb:file:database/" + dbName, "SA", "");
        return con;
    }
    
    public static void UpdateVehicle(String vehicleID, String col, String val) {
    	String ID = "'" + vehicleID + "'";
    	String column = col;
    	
		try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "UPDATE " + TABLE_NAME +
					" SET " + column + "= " + "'" + val + "'" +
					" WHERE vehicleID LIKE "+ ID;
			
			int result = stmt.executeUpdate(query);
			
			System.out.println("Update table " + TABLE_NAME + " executed successfully");
			System.out.println(result + " row(s) affected");
	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }

    public static void UpdateRental(String recordID, String col, String val) {
    	String ID = "'" + recordID + "'";
    	String column = col;
    	
		try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "UPDATE " + TABLE_NAME +
					" SET " + column + "= " + "'" + val + "'" +
					" WHERE recordId LIKE "+ ID;
			
			int result = stmt.executeUpdate(query);
			
			System.out.println("Update table " + TABLE_NAME + " executed successfully");
			System.out.println(result + " row(s) affected");
	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    //refreshes the current Thrifty system vehicles based on the current DB.
    public static void refreshVehicles() {
    	Model.getCurrentInstance().getValues().clear();
    	
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while(resultSet.next()) {
					String ID = resultSet.getString("vehicleID");
					int year = resultSet.getInt("Year");
					String make = resultSet.getString("Make");
					String model = resultSet.getString("Model");
					int no_seats = resultSet.getInt("Num_seats");
					Status status = getStatus(resultSet.getString("Status"));
	
					if (resultSet.getString("vehicle_type").equalsIgnoreCase("Car")) {
						Model.getCurrentInstance().getValues().add(new Car(ID, year, make, model, no_seats, status));
					} else if (resultSet.getString("vehicle_type").equalsIgnoreCase("Van")) {
						Model.getCurrentInstance().getValues().add(new Van(ID, year, make, model, no_seats, status));
					}
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	System.out.println("Vehicles table refreshed.");
    }
    
  //refreshes the current Thrifty system records based on the current DB.
    public static void refreshRecords() {
    	ObservableList<AbstractVehicle> currentFleet = Model.getCurrentInstance().getValues();
    	try (Connection con = ConnectionSetup.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "SELECT * FROM " + TABLE_NAME2;
			
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while(resultSet.next()) {
					String vehicleId = resultSet.getString("recordId").split("\\_")[0] + "_" + resultSet.getString("recordId").split("\\_")[1];
					for (AbstractVehicle a : currentFleet) {
						if (a.getId().equalsIgnoreCase(vehicleId)) {
							String customerId = resultSet.getString("customerId");
							DateTime rentDate = convertToDT(resultSet.getString("rentDate"));
							DateTime estimatedReturnDate = convertToDT(resultSet.getString("estimatedReturnDate"));
							DateTime actualReturnDate = convertToDT(resultSet.getString("actualReturnDate"));						
							int rentalFee = resultSet.getInt("rentalFee");
							int lateFee = resultSet.getInt("lateFee");
							a.setRecord(vehicleId,
										customerId,
										rentDate,
										estimatedReturnDate,
										actualReturnDate,
										rentalFee, 
										lateFee);
						}
					}
				}
			}
    	} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	System.out.println("Records table refreshed.");
    	
    }
    
    public static void addtoDB() throws FileNotFoundException {

    	System.out.println("Initializing Database...");
    	final String FILE_NAME = "22sampleimport.txt";
    	
    	InputStream inputStream = new FileInputStream(FILE_NAME);
    	Scanner scanner = new Scanner(inputStream);
    	
    	while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] strArr = line.split("\\_");
			String[] strArr2 = line.split("\\:");
			
			//check first if any value shows 'none'.
			for (int i = 0; i < strArr2.length; i++) {
				if (strArr2[i].equals("none")) {
					strArr2[i] = "0";
				}
			}
			
			//check first if this is a 'record' by how many underscores the inputstream line has. If > 2, it is a record.
			if ((strArr.length -1) > 2) {
				String rental_id = strArr2[0];
				String vehicleID = strArr[0] + "_" + strArr[1];		
				DateTime rentDate = convertToDT(strArr2[1]);
				DateTime estDate = convertToDT(strArr2[2]);
				DateTime actDate = convertToDT(strArr2[3]);
				RentalRecord r = new RentalRecord(vehicleID, strArr[2],  rentDate, estDate, actDate, Double.parseDouble(strArr2[4]), Double.parseDouble(strArr2[5])); 
				
				
				try (Connection con = ConnectionSetup.getConnection(DB_NAME);
						Statement stmt = con.createStatement();
				) {
					//String query = "INSERT INTO " + TABLE_NAME + " VALUES ('Car', 'C_ar', 2019, 'Honda', 'Civic', 7, 'Available', 'CivicGood.png')";
					String query = String.format("INSERT INTO RENTAL_RECORDS VALUES ('%s', '%s', '%s', '%s', '%s', %s, %s)", rental_id, strArr[2], r.getRentDate(), r.getEstimatedReturnDate(), r.getActualReturnDate(), r.getRentalFee(), r.getLateFee());
					System.out.println(query);
					int result = stmt.executeUpdate(query);
					con.commit();
					System.out.println("Insert into table " + TABLE_NAME2 + " executed successfully");
					System.out.println(result + " row(s) affected");
					System.out.println();
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
						
				//The below is for vehicles
			} else {
				Status status = getStatus(strArr2[5]);
				Car c = new Car(strArr2[0], Integer.parseInt(strArr2[1]), strArr2[2], strArr2[3], Integer.parseInt(strArr2[4]), status);
				Model.getCurrentInstance().getValues().add(c);
				System.out.println();
				System.out.println("The database currently has:");
				System.out.println(Model.getCurrentInstance().getValues());
				
				try (Connection con = ConnectionSetup.getConnection(DB_NAME);
						Statement stmt = con.createStatement();
				) {
					//String query = "INSERT INTO " + TABLE_NAME + " VALUES ('Car', 'C_ar', 2019, 'Honda', 'Civic', 7, 'Available', 'CivicGood.png')";
					String query = String.format("INSERT INTO VEHICLES VALUES ('Car', '%s', %d, '%s', '%s', %d, '%s', '%s')", c.getId(), c.getYear(), c.getMake(), c.getModel(), c.NumOfSeats(), c.getStatus().toString(), strArr2[6]);
					System.out.println(query);
					int result = stmt.executeUpdate(query);
					con.commit();
					System.out.println("Insert into table " + TABLE_NAME + " executed successfully");
					System.out.println(result + " row(s) affected");
					System.out.println();
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
			}	
    	}  	
    	refreshRecords();
    }
    
    public static DateTime convertToDT(String datetime) {
    	DateTime DTdate;
    	int day1 = Integer.parseInt(datetime.substring(0,2));
		int month1 = Integer.parseInt(datetime.substring(3,5));
		int year1 = Integer.parseInt(datetime.substring(6,10));
		return DTdate = new DateTime(day1, month1, year1);
    }
    
    public static Status getStatus(String status) {
    	if (status.equalsIgnoreCase("Available")) {
			return Status.AVAILABLE;
		} else if (status.equalsIgnoreCase("Rented")) {
			return Status.RENTED;
		} else if (status.equalsIgnoreCase("Maintenance")) {
			return Status.MAINTENANCE;
		}
		return null;
    }
    
}