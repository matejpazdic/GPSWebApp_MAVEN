<%-- 
    Document   : DeleteUser
    Created on : 27.2.2014, 10:12:37
    Author     : matej_000
--%>

<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@page import="sk.tuke.fei.kpi.Logger.FileLogger"%>
<%@page import="sk.tuke.fei.kpi.Database.DBLoginEraser"%>
<%@page import="sk.tuke.fei.kpi.Database.DBTrackEraser"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="java.io.File"%>
<%@page import="sk.tuke.fei.kpi.File.FileImpl"%>
<%@page import="sk.tuke.fei.kpi.Parser.TLVLoader"%>
<%@page import="sk.tuke.fei.kpi.File.Video.YouTubeAgent"%>
<%@page import="java.util.ArrayList"%>
<%@page import="sk.tuke.fei.kpi.Database.DBLoginFinder"%>
<%@page import="sk.tuke.fei.kpi.Database.DBTrackFinder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    session.removeAttribute("trackFilename");
    session.removeAttribute("trackName");
    session.removeAttribute("trackDescr");
    session.removeAttribute("trackActivity");
    session.removeAttribute("access");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
                ArrayList<Integer> trackIDs = new ArrayList<Integer>();
                String user = session.getAttribute("username").toString();
                YouTubeAgent agent = new YouTubeAgent("skuska.api3@gmail.com", "skuskaapi3");
                String system = System.getProperty("os.name");
                if (user != null) {
                    DBLoginFinder loginFinder = new DBLoginFinder();
                    int userID = loginFinder.getUserId(user);
                    DBTrackFinder trackFinder = new DBTrackFinder();
                    trackIDs = trackFinder.getTracksIDs(userID);

                    for (int j = 0; j < trackIDs.size(); j++) {
                        int trkID = trackIDs.get(j);

                        String path = trackFinder.getTrackFilePath(trkID);
                        if (system.startsWith("Windows")) {
                            path = path.replaceAll("/", "\\\\");
                        }
                        out.println(path);

                        TLVLoader loader = new TLVLoader();
                        loader.readTLVFile(path, trackFinder.getTrackFileName(trkID));
                        ArrayList<FileImpl> files = loader.getMultimediaFiles();
                        System.out.println("Musim deletnut: " + files.size());

                        for (int i = 0; i < files.size(); i++) {
                            String filePath = files.get(i).getPath();
                            System.out.println("Som tu konecne!");
                            if (filePath.startsWith("YTB ")) {
                                String videoEntryID = filePath.substring(filePath.indexOf("YTB ") + 4);
                                System.out.println("DELETE: " + filePath + " ??? " + videoEntryID);
                                agent.deleteVideo(videoEntryID);
                            }
                        }

                        FileUtils.deleteDirectory(new File(path));

                        DBTrackEraser eraser = new DBTrackEraser();
                        eraser.eraseTrack(trkID);
                    }
                    DBLoginEraser loginEraser = new DBLoginEraser();
                    boolean isErased = loginEraser.eraseUser(userID);
                    String rootPath = null;
                    if(isErased){
                         if(system.startsWith("Windows")){
                             
                            FileReader namereader = new FileReader("C:\\path.pth");
                            BufferedReader in = new BufferedReader(namereader);
                    
                            String pathToUpl = in.readLine();
                             
                            rootPath = pathToUpl + user + "\\";
                        }else{
                            rootPath = "/usr/local/tomcat/webapps/ROOT/Logged/uploaded_from_server/" + user + "/";
                        }
                        
                        FileUtils.deleteDirectory(new File(rootPath));
                        session.removeAttribute("username");
                        session.removeAttribute("Pass");
                        session.invalidate();
                        FileLogger.getInstance().createNewLog("User " + user + "was successfuly erased from server.");
                        
                        response.sendRedirect("../LoginPage.jsp");
                    }else{
                        session.removeAttribute("username");
                        session.removeAttribute("Pass");
                        session.invalidate();
                        FileLogger.getInstance().createNewLog("ERROR: Cannot erase user " + user + "from server!!!");
                        
                        response.sendRedirect("../LoginPage.jsp");
                    }
                }
        %>
    </body>
</html>
