/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.tuke.fei.kpi.Database;

import sk.tuke.fei.kpi.Logger.FileLogger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Trieda DBTrackFinder slúži na nájdenie záznamu o trase v tabuľke trás a na ziskávanie detailov trás.
 * @author Matej Pazdič
 */
public class DBTrackFinder {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    /**
     * Konštruktor triedy DBTrackFinder.
     * @throws Exception
     */
    public DBTrackFinder() throws Exception {
    try {

      Class.forName("com.mysql.jdbc.Driver").newInstance();
      connect = DriverManager
          .getConnection("jdbc:mysql://localhost:3306/GPSWebApp?useUnicode=true&characterEncoding=UTF-8","root","Www4dm1n#");
      
//      while (resultSet.next()) {
//        String user = resultSet.getString("user_email");
//        String number = resultSet.getString("user_pass");
//        System.out.println("User: " + user);
//        System.out.println("ID: " + number);
//      }
    } catch (Exception e) {
      throw e;
    } 
//    finally {
//      close();
//    }

  }

    /**
     * Metóda close je určená na ukončenie pripojenia k databázovému serveru.
     */
    public void close() {
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
     * Metóda getUserTrack slúži na vyhľadanie všetkých záznamov trás daného používateľa.
     * @param userID - ID daného používateľa
     * @return Návratová hodnota je zoznam názvov trás, ktoré patria danému používateľovi.
     */
    public ArrayList getUserTracks(int userID){
      ArrayList<String> tracks = new ArrayList<String>();
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_USER_ID = " + userID);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                String str = resultSet.getString("TRACK_NAME");
                //System.out.println("TRACKFINDER: "+str);
                tracks.add(str);
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD1!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getUserTracks.");
        }
        
        //close();
        return tracks;
  }

    /**
     * Metóda getUserTrackFiles je určená na vyhľadanie a vrátenie zoznamu ciest k trasám pre daného používateľa.
     * @param userID - ID daného používateľa
     * @return Návratová hodnota je zoznam ciest k trasám daného používateľa.
     */
    public ArrayList getUserTracksFiles(int userID){
      ArrayList<String> trackFiles = new ArrayList<String>();
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_USER_ID = " + userID);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                String str = resultSet.getString("TRACK_FILE");
                trackFiles.add(str);
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getUserTracksFiles.");
        }
        
        //close();
        return trackFiles;
  }

    /**
     * Metóda getTracksIDs je určená na vyhľadanie a návrat zoznamu ID (primárny identifikátor) trás, ktoré patria danému používateľovi.
     * @param userID - ID daného používateľa
     * @return Návratová hodnota je zoznam ID trás, ktoré patria danému používateľovi.
     */
    public ArrayList getTracksIDs(int userID){
      ArrayList<Integer> trackFiles = new ArrayList<Integer>();
        try {
            //System.out.println(userID);
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_USER_ID = " + userID);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                String str = resultSet.getString("TRACK_ID");
                trackFiles.add(Integer.parseInt(str));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTracksIDs.");
        }
        
        //close();
        return trackFiles;
  }

    /**
     * Metóda getTrackUserID je určená na zistenie vlastníka trasy, presnejšie na určenie ID používateľa.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je ID daného používateľa (vlastníka trasy).
     */
    public Integer getTrackUserID(int trackID){
      Integer id = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            id = Integer.parseInt(resultSet.getString("TRACK_USER_ID"));
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTrackUserID.");
        }
        return id;
  }

    /**
     * Metóda getTrackFilePath slúži na zistenie cesty k adresárovej štruktúre danej trasy.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je raťazec znakov, ktorý predstavuje adresu k adresárovej štruktúre danej trasy.
     */
    public String getTrackFilePath(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_FILE");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTrackFilePath.");
        }
        return str;
  }

    /**
     * Metóda getTrackFileName je určená na získanie názvu trasy, resp. názvu tracklog súboru.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je zeťazec znakov predstavujúci názov trasy a taktiež aj názov tracklog súborov priliehajúcich k danej trase.
     */
    public String getTrackFileName(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_NAME");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from DB!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTrackFileName.");
        }
        return str;
  }

    /**
     * Metóda getTrackDescription slúži na zistenie popisu danej trasy.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je popis danej trasy.
     */
    public String getTrackDescription(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_DESCRIPTION");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTrackDescription.");
        }
        return str;
  }

    /**
     * Metóda getTrackActivity je určená na získanie prevažujúcej aktivity na danej trase.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je reťazec znakov, ktorý predstavuje aktivitu vykonávanú na danej trase.
     */
    public String getTrackActivity(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_ACTIVITY");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTrackActivity.");
        }
        return str;
  }

    /**
     * Metóda getTrackStartDate je určená na získanie dátumu a času prvého bodu trasy.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je dátum a čas prvého bodu danej trasy, ktorý je vo formáte reťazca znakov.
     */
    public String getTrackStartDate(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_STARTDATE");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTrackStartDate.");
        }
        return str;
  }

    /**
     * Metóda getTrackEndDate slúži na zistenie dátumu a času posledného bodu danej trasy.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je dátum a čas posledného bodu danej trasy, ktorý je vo formáte reťazca znakov.
     */
    public String getTrackEndDate(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_ENDDATE");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTrackEndDate.");
        }
        return str;
  }

    /**
     * Metóda getUploadedDate slúži na získanie dátumu a času, kedy bola daná trasa vytvorená v databáze trás.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je dátum a čas kedy bola daná trasa vytvorená v tabuľke trás.
     */
    public String getUploadedDate(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_DATE_CREATED");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getUploadedDate.");
        }
        return str;
  }
   
    /**
     * Metóda getChangeDate je určená na zistenie dátumu a času, kedy bola daná trasa zmenená (upravená) v tabuľke trás.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je dátum a čas poslednej úpravy danej trasy, ktorý je vo formáte zeťazca znakov.
     */
    public String getChangeDate(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_DATE_UPDATED");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getChangeDate.");
        }
        return str;
  }
    
    /**
     * Metóda getStartAddress je určená na získanie adresy prvého bodu danej trasy.
     * @param trackID - ID danej trasy
     * @return Navratová hodnota je adresa prvého bodu danej trasy, ktorá predstavuje reťazec znakov.
     */
    public String getStartAddress(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_START_ADDRESS");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getStartAddress.");
        }
        return str;
  }
    
    /**
     * Metóda getEndAddress je určená na získanie adresy posledného bodu danej trasy.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je reťazec znakov, ktorý predstavuje adresu posledného bodu danej trasy.
     */
    public String getEndAddress(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_END_ADDRESS");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getEndAddress.");
        }
        return str;
  }
    
    /**
     * Metóda getAccess slúži na zistenie či je daná trasa určená k voľnému sledovaniu, alebo je súkromná ("Public" alebo "Private").
     * @param trackID - ID danej trasy
     * @return Návratová adresa je "Public" ak je daná trasa verejná, alebo "Private" ak je daná trasa určena iba na zobrazenie pre jej majiteľa.
     */
    public String getAccess(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_ACCESS");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getAccess.");
        }
        return str;
  }
    
    /**
     * Metóda getTrackLengthKm slúži na návrat počtu prejdených kilometrov na danej trase.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je počet kilometrov danej trasy, ktorý je vo formáte reťazca znakov.
     */
    public String getTrackLengthKm(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_LENGTH_KM");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTrackLengthKm.");
        }
        return str;
  }
    
    /**
     * Metóda getMinElevation slúži na získanie minimálnej nadmorskej výšky danej trasy.
     * @param trackID - ID danej tarsy
     * @return Návratová hodnota je minimálna nadmorská výška, ktorá je reprezentovaná reťazcom znakov. 
     */
    public String getMinElevation(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_MIN_ELEVATION");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getMinElevation.");
        }
        return str;
  }
    
    /**
     * Metóda getMaxElevation je určená na zistenie maximálnej nadmorskej výšky danej trasy.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je maximálna nadmorská výška danje trasy, ktorá je vo formáte reťazca znakov.
     */
    public String getMaxElevation(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_MAX_ELEVATION");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getMaxElevation.");
        }
        return str;
  }
    
    /**
     * Metóda getHeightDifference je určená na získanie prevýšenia danej trasy.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je prevýšenie danej trasy, ktoré je reprezentované ako reťazec znakov.
     */
    public String getHeightDifference(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_HEIGHT_DIFF");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getHeightDifference.");
        }
        return str;
  }
    
    /**
     * Metóda getTrackDuration slúži na získanie doby trvania danej trasy.
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je reťazec znakov, ktorý reprezentuje dobu trvania danej trasy.
     */
    public String getTrackDuration(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_DURATION");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTrackDuration.");
        }
        return str;
  }
    
    /**
     * Metóda getTrackCreationType je určená na zistenie príznakuu vytvorenia danej trasy ("Drawed" alebo "Parsed").
     * @param trackID - ID danej trasy
     * @return Návratová hodnota je buď "Parsed" ak bola daná trasa vytvorená pomocou vstupného tracklog súboru, alebo "Drawed" ak bola vytvorená nakreslením.
     */
    public String getTrackCreationType(int trackID){
      String str = null;
        try {
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_ID = " + trackID);
            resultSet = statement.executeQuery();
            resultSet.next();
            str = resultSet.getString("TRACK_CREATION_TYPE");
        } catch (SQLException ex) {
            System.out.println("ERROR: Cannot read table TRACKS from BD!!!");
            FileLogger.getInstance().createNewLog("ERROR: Cannot read table TRACKS from DB!!! In getTrackCreationType.");
        }
        return str;
  }

} 
