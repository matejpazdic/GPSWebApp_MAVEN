/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.tuke.fei.kpi.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Trieda DBFinder implementuje vyhľadávací nástroj portálu.
 * @author matej_000
 */
public class DBFinder {
    
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    
    /**
     * Konštruktor triedy DBFinder.
     */
    public DBFinder(){
        try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/GPSWebApp?useUnicode=true&characterEncoding=UTF-8","root","Www4dm1n#");
        } catch (SQLException ex) {
            Logger.getLogger(DBFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Metóda findStringAll slúži na vyhľádavanie zadaného reťazca v celej databáze portálu.
     * @param find - vyhľadavaný reťazec znakov
     * @return Návratová hodnota je zoznam trás, ktoré zapadajú do vyhľadávania.
     */
    public ArrayList<Integer> findStringAll(String find){
        ArrayList<Integer> results = new ArrayList<Integer>();
        try {  
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS FULL JOIN USERS ON USER_ID=TRACK_USER_ID where TRACK_ACCESS='Public' AND (USER_EMAIL like '%" + find + "%' OR TRACK_NAME like '%" + find + "%' OR TRACK_DESCRIPTION like '%" + find + "%' OR TRACK_ACTIVITY like '%" + find + "%' OR TRACK_STARTDATE like '%" + find + "%' OR TRACK_ENDDATE like '%" + find + "%' OR TRACK_START_ADDRESS like '%" + find + "%' OR TRACK_END_ADDRESS like '%" + find + "%')");
            resultSet = statement.executeQuery();
            System.out.println("SELECT * from TRACKS where TRACK_ACCESS='Public' AND (TRACK_NAME like '%" + find + "%' OR TRACK_DESCRIPTION like '%" + find + "%' OR TRACK_ACTIVITY like '%" + find + "%' OR TRACK_START_ADDRESS like '%" + find + "%' OR TRACK_END_ADDRESS like '%" + find + "%')");
            while(resultSet.next()){
                String str = resultSet.getString("TRACK_ID");
                int i = Integer.parseInt(str);
                results.add(i);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
    /**
     * Metóda findStringOnlyUsers slúži na vyhľadávanie trás, ktoré slúžia iba danému používateľovi.
     * @param find - vyhľadávaný reťazec znakov
     * @param userID - ID daného používateľa
     * @return Návratová hodnota je zoznam trás, ktoré zapadajú do vyhľadávania.
     * @deprecated Táto metóda sa ďalej nepoužíva, ale je plne funkčná.
     */
    @Deprecated
    public ArrayList<String> findStringOnlyUsers(String find, int userID){
        ArrayList<String> results = new ArrayList<String>();
        try {  
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS where TRACK_USER_ID=" + userID + " AND (TRACK_NAME like '%" + find + "%' OR TRACK_DESCRIPTION like '%" + find + "%' OR TRACK_ACTIVITY like '%" + find + "%' OR TRACK_START_ADDRESS like '%" + find + "%' OR TRACK_END_ADDRESS like '%" + find + "%')");
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                String str = resultSet.getString("TRACK_NAME");
                results.add(str);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
    /**
     * Metóda findStringInParsed slúži na vyhľadávanie iba v trasách, ktoré boli vytvorené na základe vstupného tracklog súboru.
     * @param find - vyhľadávaný reťazec znakov
     * @return Návratová hodnota je zoznam trás, ktoré zapadajú do vyhľadávania.
     * @deprecated Táto metóda sa ďalej nepoužíva, ale je plne funkčná.
     */
    @Deprecated
    public ArrayList<String> findStringInParsed(String find){
        ArrayList<String> results = new ArrayList<String>();
        try {  
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS FULL JOIN USERS ON USER_ID=TRACK_USER_ID where TRACK_CREATION_TYPE='Parsed' AND TRACK_ACCESS='Public' AND (USER_EMAIL like '%" + find + "%' OR TRACK_NAME like '%" + find + "%' OR TRACK_DESCRIPTION like '%" + find + "%' OR TRACK_ACTIVITY like '%" + find + "%' OR TRACK_START_ADDRESS like '%" + find + "%' OR TRACK_END_ADDRESS like '%" + find + "%')");
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                String str = resultSet.getString("TRACK_NAME");
                results.add(str);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
    /**
     * Metóda findStringInDrawed je metóda slúžiaca na vyhľadávanie iba v trasách, ktoré boli vytvorené nakreslením.
     * @param find - vyhľadavaný reťazec znakov
     * @return Návratová hodnota je zoznam trás, ktoré zapadajú do vyhľadávania.
     * @deprecated Táto metóda sa ďalej nepoužíva, ale je plne funkčná.
     */
    @Deprecated
    public ArrayList<String> findStringInDrawed(String find){
        ArrayList<String> results = new ArrayList<String>();
        try {  
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS FULL JOIN USERS ON USER_ID=TRACK_USER_ID where TRACK_CREATION_TYPE='Drawed' AND TRACK_ACCESS='Public' AND (USER_EMAIL like '%" + find + "%' OR TRACK_NAME like '%" + find + "%' OR TRACK_DESCRIPTION like '%" + find + "%' OR TRACK_ACTIVITY like '%" + find + "%' OR TRACK_START_ADDRESS like '%" + find + "%' OR TRACK_END_ADDRESS like '%" + find + "%')");
            resultSet = statement.executeQuery();
            //System.out.println("SELECT * from TRACKS where TRACK_CREATION_TYPE='Drawed' AND (TRACK_NAME like '%" + find + "%' OR TRACK_DESCRIPTION like '%" + find + "%' OR TRACK_ACTIVITY like '%" + find + "%' OR TRACK_START_ADDRESS like '%" + find + "%' OR TRACK_END_ADDRESS like '%" + find + "%')");
            while(resultSet.next()){
                String str = resultSet.getString("TRACK_NAME");
                results.add(str);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    /**
     * Metóda slúžiaca na vyhľadanie najnovsie pridaných n trás.
     * @param rowCount - počet položiek, ktoré sa majú vyhľadať
     * @return Návratová hodnota je zoznam trás, ktoré zapadajú do vyhľadávania.
     */
    public ArrayList<Integer> findNewNTracks(int rowCount){
        ArrayList<Integer> results = new ArrayList<Integer>();
        try {  
            PreparedStatement statement = connect.prepareStatement("SELECT * from TRACKS WHERE TRACK_ACCESS='Public' ORDER BY TRACK_DATE_UPDATED DESC LIMIT " + rowCount);
            resultSet = statement.executeQuery();
            //System.out.println("SELECT * from TRACKS where TRACK_CREATION_TYPE='Drawed' AND (TRACK_NAME like '%" + find + "%' OR TRACK_DESCRIPTION like '%" + find + "%' OR TRACK_ACTIVITY like '%" + find + "%' OR TRACK_START_ADDRESS like '%" + find + "%' OR TRACK_END_ADDRESS like '%" + find + "%')");
            while(resultSet.next()){
                String str = resultSet.getString("TRACK_ID");
                int id = Integer.parseInt(str);
                results.add(id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
}
