/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.tuke.fei.kpi.Database;

import sk.tuke.fei.kpi.Logger.FileLogger;

import java.sql.*;

/**
 * Trieda BDTrackEraser je určená na vymazávanie zázanmov trás z tabuľky trás.
 * @author Matej Pazdič
 */
public class DBTrackEraser {
    
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    /**
     * KKnštruktor triedy DBTrackEraser.
     * @throws Exception
     */
    public DBTrackEraser() throws Exception {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
      connect = DriverManager
          .getConnection("jdbc:mysql://localhost:3306/GPSWebApp?useUnicode=true&characterEncoding=UTF-8","root","Www4dm1n#");
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Metóda eraseTrack slúži na samotné vymazanie zázamu o trase, ktorý je uložený v tabuľke trás.
     * @param trackID - ID danej trasy, ktorú chceme vymazať
     */
    public void eraseTrack(int trackID){
        try {
            statement =  connect.createStatement();
            //statement.executeQuery();
            String stat = "DELETE FROM TRACKS where TRACK_ID=" + trackID;
            statement.executeUpdate(stat);
            FileLogger.getInstance().createNewLog("Track with trackID " + trackID + " was successfuly deleted from DB.");
            close();
        } catch (SQLException ex) {
            System.out.println("Nevymazal som z DB!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot delete track with trackID " + trackID + " from DB!!!");
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