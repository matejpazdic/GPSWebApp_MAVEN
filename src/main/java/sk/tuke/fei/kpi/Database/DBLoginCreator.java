/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.tuke.fei.kpi.Database;

import sk.tuke.fei.kpi.Logger.FileLogger;
import sk.tuke.fei.kpi.utils.hash.HashingException;
import sk.tuke.fei.kpi.utils.hash.StringHasher;

import java.sql.*;

/**
 * Trieda DBLoginCreator slúži na vytváranie nových záznamov o používateľoch.
 * @author Matej Pazdič
 */
public class DBLoginCreator {

    // FIXME: 07.03.2016 Create setting table in database foor selecting the global setting for hashing algorithm
    private static final StringHasher.HashingType HASHING_TYPE = StringHasher.HashingType.MD5;
    
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    /**
     * Konštruktor triedy DBLoginCreator.
     * @throws Exception
     */
    public DBLoginCreator() throws Exception {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
	        // FIXME: 07.03.2016 Move credentials to properties file or some...
	        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/GPSWebApp","root","Www4dm1n#");
        } catch (Exception e) {
            FileLogger.getInstance().createNewLog("ERROR: Cannot connect to Database in DBLoginCreator!!!");
            throw e;
        }

    }
    
    /**
     * Metóda createNewLogin slúži na vytvorenie nového používateľa v databáze používateľov.
     * @param email - požívateľský email
     * @param password - heslo používateľa
     * @param userToken - jedinečný používateľský token, ktorý je vytváraný pomocu triedy EmailSender v balíku Security
     */
    public void createNewLogin(String email, String password, String userToken){
        try {
            statement =  connect.createStatement();
	        final String hashedPassword = StringHasher.toHash(password, HASHING_TYPE);
	        statement.executeUpdate("INSERT INTO USERS (USER_EMAIL, USER_PASS, USER_TOKEN) VALUES ('"+ email +"' ,'" + hashedPassword + "' ,'" + userToken + "')");
            
            FileLogger.getInstance().createNewLog("User " + email + " was successfuly created!");
        } catch (SQLException ex) {
	        FileLogger.getInstance().createNewLog("ERROR: User " + email + " was NOT successfuly created!");
        } catch (HashingException e) {
	        FileLogger.getInstance().createNewLog("ERROR: There was something wrong while hashing the new user password, the email is: " + email + ", and the message is: " + e.getMessage());
        } finally {
	        close();
        }
    }
    
    /**
     * Metóda createNewLogin slúži na vytvorenie nového používateľa v databáze používateľov.
     * @param email - Používateľský email
     * @param firstName - Krstné meno používateľa
     * @param lastName - Priezvisko používateľa
     * @param age - Vek používateľa
     * @param activity - Oblúbená aktivita používateľa
     * @param password - Heslo používateľa
     * @param userToken - jedinečný používateľský token, ktorý je vytváraný pomocu triedy EmailSender v balíku Security
     */
    public void createNewLogin(String email, String firstName, String lastName, String age, String activity, String password, String userToken){
        try {
            if(age.isEmpty()){
                age = "-1";
            }
            statement =  connect.createStatement();
	        final String hashedPassword = StringHasher.toHash(password, HASHING_TYPE);
            statement.executeUpdate("INSERT INTO USERS (USER_EMAIL, USER_FIRST_NAME, USER_LAST_NAME , USER_AGE, USER_ACTIVITY, USER_PASS, USER_TOKEN) VALUES ('"+ email +"' ,'" + firstName + "' ,'"+ lastName +"' ," + age + " ,'"+ activity +"' ,'" + hashedPassword + "' ,'" + userToken + "')");
            
            FileLogger.getInstance().createNewLog("User " + email + " was successfuly created!");

        } catch (SQLException ex) {
            FileLogger.getInstance().createNewLog("ERROR: User " + email + " was NOT successfuly created!");
        } catch (HashingException e) {
	        FileLogger.getInstance().createNewLog("ERROR: There was something wrong while hashing the new user password, the email is: " + email + ", and the message is: " + e.getMessage());
        } finally {
	        close();
        }
    }
    
    private void close() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }
      if (statement != null) {
        statement.close();
      }
      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {

    }
  }
}
