/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.tuke.fei.kpi.Database;

import sk.tuke.fei.kpi.Logger.FileLogger;

import java.sql.*;

/**
 * Trieda DBLoginCreator slúži na vytváranie nových záznamov o používateľoch.
 * @author Matej Pazdič
 */
public class DBLoginCreator {
    
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
      connect = DriverManager
          .getConnection("jdbc:mysql://localhost:3306/GPSWebApp","root","Www4dm1n#");

//      while (resultSet.next()) {
//        String user = resultSet.getString("user_email");
//        String number = resultSet.getString("user_pass");
//        System.out.println("User: " + user);
//        System.out.println("ID: " + number);
//      }
        } catch (Exception e) {
            FileLogger.getInstance().createNewLog("ERROR: Cannot connect to Database in DBLoginCreator!!!");
            throw e;
        }
//    finally {
//      close();
//    }

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
            //statement.executeQuery();
            statement.executeUpdate("INSERT INTO USERS (USER_EMAIL, USER_PASS, USER_TOKEN) VALUES ('"+ email +"' ,'" + password + "' ,'" + userToken + "')");
            
            FileLogger.getInstance().createNewLog("User " + email + " was successfuly created!");
            
            close();
        } catch (SQLException ex) {
            System.out.println("Nezapisal som do DB!!!");
            FileLogger.getInstance().createNewLog("ERROR: User " + email + " was NOT successfuly created!");
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
            //statement.executeQuery();
            statement.executeUpdate("INSERT INTO USERS (USER_EMAIL, USER_FIRST_NAME, USER_LAST_NAME , USER_AGE, USER_ACTIVITY, USER_PASS, USER_TOKEN) VALUES ('"+ email +"' ,'" + firstName + "' ,'"+ lastName +"' ," + age + " ,'"+ activity +"' ,'" + password + "' ,'" + userToken + "')");
            
            FileLogger.getInstance().createNewLog("User " + email + " was successfuly created!");
            
            close();
        } catch (SQLException ex) {
            System.out.println("Nezapisal som do DB!!!");
            FileLogger.getInstance().createNewLog("ERROR: User " + email + " was NOT successfuly created!");
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
