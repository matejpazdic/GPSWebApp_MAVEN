/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.tuke.fei.kpi.Database;

import sk.tuke.fei.kpi.Logger.FileLogger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Trieda DBLoginFinder slúži na samotný výber záznamu používateľa z tabuľky používateľov a získanie detailov o používateľovi.
 * @author Matej Pazdič
 */
public class DBLoginFinder {
  private Connection connect = null;
  private Statement statement = null;
  private ResultSet resultSet = null;

    /**
     * Konštruktor triedy DBLoginFinder
     * @throws Exception
     */
    public DBLoginFinder() throws Exception {
    try {

      Class.forName("com.mysql.jdbc.Driver").newInstance();
      connect = DriverManager
          .getConnection("jdbc:mysql://localhost:3306/GPSWebApp","root","Www4dm1n#");
      PreparedStatement statement = connect
          .prepareStatement("SELECT * from USERS");

      resultSet = statement.executeQuery();
//      while (resultSet.next()) {
//        String user = resultSet.getString("user_email");
//        String number = resultSet.getString("user_pass");
//        System.out.println("User: " + user);
//        System.out.println("ID: " + number);
//      }
    } catch (Exception e) {
        FileLogger.getInstance().createNewLog("ERROR: Cannot load users from DB in DBLoginFinder!!!");
      throw e;
    } 
//    finally {
//      close();
//    }

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

    /**
     * Metóda isCorrectLogin slúži na zistenie či daný používateľ zadal správne heslo pri prihlásení.
     * @param username - email používateľa potrebný na prihlásenie
     * @param userpass - zadané heslo od používateľa
     * @return Návratová hodnota je buď "True", ak je prihlásenie správne, alebo "False" ak nieje správne.
     * @throws Exception
     */
    public boolean isCorrectLogin(String username, String userpass) throws Exception{
     
      while (resultSet.next()) {
        String email = resultSet.getString("user_email");
        String pass = resultSet.getString("user_pass");
        if(email.equals(username) && pass.equals(userpass)){
            close();
            return true;
        }
        
  }
      close();
      FileLogger.getInstance().createNewLog("ERROR: Cannot log user " + username + " !!!");
      return false;
  }
  
    /**
     * Metóda isExistingLogin slúži na zistenie či daný používateľský email patrí niejakému používateľovi z tabuľky používateľov.
     * @param username - používateĺský email
     * @return Návratová hodnota je buď "True" ak existuje, alebo "False" ak daný používateľ sa nenachádza v tabuľke používateľov.
     * @throws Exception
     */
    public boolean isExistingLogin(String username) throws Exception {
        while (resultSet.next()) {
            String email = resultSet.getString("user_email");
            if (email.equals(username)) {
                //close();
                return true;
            }

        }
        close();
        FileLogger.getInstance().createNewLog("ERROR: User " + username + " is non existing user!!!");
        return false;
    }
    
    /**
     * Metóda isExistingLoginNonLog slúži na zistenie či daný používateľský email patrí niejakému používateľovi z tabuľky používateľov, ale nezapiše stav do Log súboru.
     * @param username - používateĺský email
     * @return Návratová hodnota je buď "True" ak existuje, alebo "False" ak daný používateľ sa nenachádza v tabuľke používateľov.
     * @throws Exception
     */
    public boolean isExistingLoginNonLog(String username) throws Exception {
        while (resultSet.next()) {
            String email = resultSet.getString("user_email");
            if (email.equals(username)) {
                //close();
                return true;
            }

        }
        close();
        //FileLogger.getInstance().createNewLog("ERROR: User " + username + " is non existing user!!!");
        return false;
    }

    /**
     * Metóda getUserId slúži na vrátenie ID daného používateľa, podľa jeho emailu.
     * @param username - používateľský email
     * @return Návratová hodnota je používateľovo ID uložené v tabuľke používateľov, alebo "-1" ak sa daný používateľ nenašiel.
     * @throws SQLException
     */
    public int getUserId(String username) throws SQLException {
        while (resultSet.next()) {
            String email = resultSet.getString("user_email");
            if (email.equals(username)) {
                int userID = Integer.parseInt(resultSet.getString("USER_ID"));
                close();
                return userID;
            }

        }
        close();
        FileLogger.getInstance().createNewLog("ERROR: Cannot find userID of user " + username + " !!!");
        return -1;
    }
    
    /**
     * Metóda isUserStatus slúži na zistenie či je daný používateľ zapísaný ako rola "USER".
     * @param username - používateľský email
     * @return Návratová hodnota je "True" ak je daný používateľ "USER", alebo "False" ak inak.
     * @throws Exception
     */
    public boolean isUserStatus(String username) throws Exception {
        boolean condition = true;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connect1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/GPSWebApp", "root", "Www4dm1n#");
            PreparedStatement statement1 = connect1.prepareStatement("SELECT * from USERS where USER_EMAIL='" + username + "'");
            ResultSet resultSet1 = statement1.executeQuery();

            resultSet1.next();
            String userStatus = resultSet1.getString("USER_STATUS");
            connect1.close();
            statement1.close();
            resultSet1.close();
            if(userStatus.equals("USER")){
                condition = true;
                return condition;
            }else{
                condition = false;
                return condition;
            }
        } catch (Exception e) {
            FileLogger.getInstance().createNewLog("ERROR: Cannot load user status from DB in isUserStatus!!!");
            return condition;
        }
    }
    
    /**
     * Metóda getUserInformation slúži na ziskávanie kompletných detailov daného používateľa.
     * @param username - používateľský email
     * @return Návratová hodnota je zoznam detailov o danom používateľovi, pričom jej veľkosť je stále 7.
     * @throws Exception
     */
    public ArrayList<String> getUserInformation(String username) throws Exception {
        ArrayList<String> information = new ArrayList<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connect1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/GPSWebApp", "root", "Www4dm1n#");
            PreparedStatement statement1 = connect1.prepareStatement("SELECT * from USERS where USER_EMAIL='" + username + "'");
            ResultSet resultSet1 = statement1.executeQuery();
            resultSet1.next();
            
            information.add(resultSet1.getString("USER_FIRST_NAME"));
            information.add(resultSet1.getString("USER_LAST_NAME"));
            information.add(resultSet1.getString("USER_AGE"));
            information.add(resultSet1.getString("USER_ACTIVITY"));
            information.add(resultSet1.getString("USER_EMAIL"));
            information.add(resultSet1.getString("USER_PASS"));
            information.add(resultSet1.getString("USER_STATUS"));
            
            connect1.close();
            statement1.close();
            resultSet1.close();
            
            return information;
        } catch (Exception e) {
            FileLogger.getInstance().createNewLog("ERROR: Cannot load user status from DB in getUserInformation!!!");
            return information;
        }
    }
    
    /**
     * Metóda getUserToken slúži na získanie jedinečného 32 znakového identifikátora používateľa, potrebného pre overenie registrácie paužívateľa.
     * @param username - používateľský email
     * @return Návratová hodnota je 32 znakový reťazec.
     */
    public String getUserToken(String username){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connect1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/GPSWebApp", "root", "Www4dm1n#");
            PreparedStatement statement1 = connect1.prepareStatement("SELECT * from USERS where USER_EMAIL='" + username + "'");
            ResultSet resultSet1 = statement1.executeQuery();
            resultSet1.next();
            
            String token = resultSet1.getString("USER_TOKEN");
            
            connect1.close();
            statement1.close();
            resultSet1.close();
            
            return token;
        } catch (Exception e) {
            FileLogger.getInstance().createNewLog("ERROR: Cannot load user TOKEN from DB in getUserToken!!!");
            return null;
        }
    }
    
    /**
     * Metóda isUserAccepted slúži na zistenie, či je daný používateľ už overený na prihlásenie alebo nie.
     * @param username - používateľský email
     * @return Návratová hodnota je "True" ak daný používateľ je overený, alebo "False" ak daný používateľ nieje overený.
     */
    public boolean isUserAccepted(String username){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connect1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/GPSWebApp", "root", "Www4dm1n#");
            PreparedStatement statement1 = connect1.prepareStatement("SELECT * from USERS where USER_EMAIL='" + username + "'");
            ResultSet resultSet1 = statement1.executeQuery();
            resultSet1.next();
            
            int isAccepted = Integer.parseInt(resultSet1.getString("USER_ACCEPTED"));
            
            connect1.close();
            statement1.close();
            resultSet1.close();
            
            if(isAccepted == 0){
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            FileLogger.getInstance().createNewLog("ERROR: Cannot load user TOKEN from DB in getUserToken!!!");
            return false;
        }
    }
    
    /**
     * Metóda getUserEmail slúži na zistenie používateľského emailu podľa jeho ID.
     * @param userID - používateľské ID
     * @return Návratová hodnota je reťazec znakov, ktorý predstavuje email daného používateľa.
     * @throws Exception
     */
    public String getUserEmail(int userID) throws Exception {
        boolean condition = true;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connect1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/GPSWebApp", "root", "Www4dm1n#");
            PreparedStatement statement1 = connect1.prepareStatement("SELECT * from USERS where USER_ID=" + userID);
            ResultSet resultSet1 = statement1.executeQuery();

            resultSet1.next();
            String userEmail = resultSet1.getString("USER_EMAIL");
            connect1.close();
            statement1.close();
            resultSet1.close();
            return userEmail;
        } catch (Exception e) {
            FileLogger.getInstance().createNewLog("ERROR: Cannot load user status from DB in isUserStatus!!!");
            return null;
        }
    }
    
} 
