/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.tuke.fei.kpi.Logger;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Trieda FileLogger predstavuje implementáciu zapisovača činnosti a chýb portálu do logovacieho súboru. 
 * @author Matej Pazdič
 */
public class FileLogger {
    
    private static FileLogger logger = null;
    private String system = System.getProperty("os.name");
    private final String fileName = "GPSWebAppLog.log";
    private String path;
    private final File logFile;
    
    /**
     * Konštrukttor triedy FileLogger.
     */
    protected FileLogger(){
        
        
        if(system.startsWith("Windows")){
                        
            FileReader namereader = null;
            try {
                namereader = new FileReader("C:\\path.pth");
                BufferedReader in = new BufferedReader(namereader);
                String pathToUpl = in.readLine();
                path = pathToUpl;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    namereader.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }else{
            path = "/usr/local/tomcat/webapps/ROOT/Logged/uploaded_from_server/";
        }
        
        logFile = new File(path, fileName);
    }
    
    /**
     * Metóda getInstance predstavuje implementáciu Konštruktora triedy FileLOgger ako singleton.
     * @return Návratová hodnota je objekt triedy FileLogger.
     */
    public static FileLogger getInstance(){
        if(logger == null){
            logger = new FileLogger();
        }
        return logger;
    }
    
    private void checkLogFileIsCreated(){
        if(!logFile.exists()){
            try {
                //logFile.mkdirs();
                logFile.createNewFile();
            } catch (IOException ex) {
                System.out.println("ERROR: Cannot create log file!!!");
            }
        }
    }
    
    /**
     * Metóda createNewLog je určená na zápis novej správy do logovacieho súboru.
     * @param message - reťazec znakov predstavujúci samotnú správu
     */
    public void createNewLog(String message){
        this.checkLogFileIsCreated();
        this.checkNewDay();
        FileWriter writer = null;
        try {
            writer = new FileWriter(logFile, true);
            
            BufferedWriter buf = new BufferedWriter(writer);
            
            writer.append("\n" + new Date().toString() + " >>> " + message);
            
            buf.close();
        } catch (IOException ex) {
            System.out.println(system);
            System.out.println("ERROR: Cannot write into a log file!!!");
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
               System.out.println("ERROR: Cannot write into a log file!!!");
            }
        }
        
    }
    
    private void createNewLogForCheck(String message){
        this.checkLogFileIsCreated();
   
        FileWriter writer = null;
        try {
            writer = new FileWriter(logFile, true);
            
            BufferedWriter buf = new BufferedWriter(writer);
            
            writer.append("\n" + new Date().toString() + " >>> " + message);
            
            buf.close();
        } catch (IOException ex) {
            System.out.println(system);
            System.out.println("ERROR: Cannot write into a log file!!!");
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
               System.out.println("ERROR: Cannot write into a log file!!!");
            }
        }
        
    }
    
    private void checkNewDay() {
    
        BufferedReader br = null;

        String currentLine = null;
        String lastLine = null;

        try {
            br = new BufferedReader(new FileReader(logFile));

            while((currentLine = br.readLine()) != null){
                lastLine = currentLine;
            }
            
            String stringDate = lastLine.substring(0, lastLine.lastIndexOf(" >>> "));
            
            //System.out.println(stringDate);
            
            DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
            Date lastDate = df.parse(stringDate);
            Date currentDate = new Date();
            
            
            if(currentDate.getDay() > lastDate.getDay()){
                System.out.println("MAM NOVY RIADOK!!! " + currentDate + " " + lastDate);
                this.createNewLogForCheck("New Date\n");
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
          //  try {
                //br.close();
          //  } catch (IOException ex) {
           //     Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
           // }
        }
    }
    
    /**
     * Metóda resetLogFile je určená na vymazanie obsahu logovacieho súboru.
     */
    public void resetLogFile(){
        if(logFile.exists()){
            logFile.delete();
            this.checkLogFileIsCreated();
        }
    }
    
}
