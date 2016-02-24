/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.tuke.fei.kpi.Database;

import sk.tuke.fei.kpi.Logger.FileLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Trieda DBLoginUpdater slúži na obnovovanie a zmenu používateľských detailov v tabuľke používateľov.
 * @author Matej Pazdič
 */
public class DBLoginUpdater {
    
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    
    /**
     * Konštruktor triedy DBLoginUpdater.
     */
    public DBLoginUpdater(){
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/GPSWebApp?useUnicode=true&characterEncoding=UTF-8", "root", "Www4dm1n#");

        } catch (Exception e) {
            FileLogger.getInstance().createNewLog("ERROR: Cannot create DB connection in DBLoginUpdater!!!");
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
    
    /**
     * Metóda updateUserData slúži na zmenu detailov daného používateľa.
     * @param currentEmail - pôvodný používateľský email
     * @param newEmail - nový email daného používateľa
     * @param firstName - nové krstné meno daného používateľa
     * @param lastName - nové priezvisko daného používateľa
     * @param activity - nová preferovaná aktivita daného používateľa
     * @param oldPassword - pôvodné heslo daného používateľa
     * @param newPassword - nové heslo daného používateľa
     * @param age - nový vek daneho používateľa
     * @return Navratová hodnota je "0" ak zmena údaajov prebehla v poriadku. "1" ak používateľ zadal ako nový email už existujúci email. "2" ak používateľ zadal zlé heslo od svojho účtu. "-1" ak nastala chyba komunikácie s databázov.
     */
    public int updateUserData(String currentEmail, String newEmail, String firstName, String lastName, String activity,String oldPassword, String newPassword, int age){
        try {
            DBLoginFinder finder = new DBLoginFinder();
            boolean existing = finder.isExistingLoginNonLog(newEmail);
            DBLoginFinder finder1 = new DBLoginFinder();
            
            System.out.println("terajsi email " + currentEmail + " novyemail " + newEmail + " meno " + firstName + " priezvisko " + lastName + " aktivita " + activity + " stareHeslo " + oldPassword + " noveHeslo " + newPassword + " rok " + age);
            
            ArrayList<String> info = finder1.getUserInformation(currentEmail);
          
            
           
            if (oldPassword.equals(info.get(5))) {
                if (!existing || currentEmail.equals(newEmail)) {
                    Statement statement = connect.createStatement();
                    statement.executeUpdate("UPDATE USERS set USER_EMAIL='" + newEmail + "',USER_FIRST_NAME='" + firstName + "',USER_LAST_NAME='" + lastName + "',USER_ACTIVITY='" + activity + "',USER_AGE=" + age + ",USER_PASS='" + newPassword + "' WHERE USER_EMAIL='" + currentEmail + "'");
                    FileLogger.getInstance().createNewLog("Successfuly updated user data for old user " + currentEmail + " to new email " + newEmail + ".");
                    return 0;
                } else {
                    FileLogger.getInstance().createNewLog("ERROR: User " + info.get(4) +  " has entered existing new email!!! Cannot update user data!!!");
                    return 1;
                }
            }else{
                FileLogger.getInstance().createNewLog("ERROR: User " + info.get(4) +  " has entered wrong password!!! Cannot update user data!!!");
                return 2;
            }
            
            
        } catch (Exception ex) {
            FileLogger.getInstance().createNewLog("ERROR: Cannot connect to DB in DBLoginUpdater!!!");
            this.close();
            return -1;
        }
    }
    
    /**
     * Metóda acceptUser slúži na potvrdenie používateľa po úspešnej registrácii.
     * @param email - používateľský email
     * @param token - jedinečný  používateľský token
     * @return Návratovou hodnotou je "True" ak daný používateľ je úspešne potvrdený, alebo "False" ak nie.
     */
    public boolean acceptUser(String email, String token){
        try {
            DBLoginFinder finder = new DBLoginFinder();
            String userToken = finder.getUserToken(email);
            boolean isUserAccepted = finder.isUserAccepted(email);
            
            if(isUserAccepted != true && userToken.equals(token)){
                Statement statement = connect.createStatement();
                statement.executeUpdate("UPDATE USERS set USER_ACCEPTED ='" + 1 + "' WHERE USER_EMAIL='" + email + "'");
                FileLogger.getInstance().createNewLog("Successfuly ACCEPTED user " + email + ".");
                return true;
            } else{
                FileLogger.getInstance().createNewLog("ERROR: Cannot ACCEPT user " + email + " with userToken " + token + ".");
                return false;
            }
        } catch (Exception ex) {
            FileLogger.getInstance().createNewLog("ERROR: Cannot ACCEPT user " + email + " with userToken " + token + ".");
            return false;
        }
    }
    
}
