<%-- 
    Document   : skuska
    Created on : 1.11.2013, 19:36:17
    Author     : matej_000
--%>

<%@page import="sk.tuke.fei.kpi.Logger.FileLogger"%>
<%@page import="sk.tuke.fei.kpi.Database.DBLoginFinder"%>
<%@page import="sk.tuke.fei.kpi.File.Video.YouTubeAgent"%>
<%@page import="sk.tuke.fei.kpi.File.FileImpl"%>
<%@page import="java.util.ArrayList"%>
<%@page import="sk.tuke.fei.kpi.Parser.TLVLoader"%>
<%@page import="sk.tuke.fei.kpi.Database.DBTrackEraser"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.commons.io.FileUtils"%>
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
            YouTubeAgent agent = new YouTubeAgent("skuska.api3@gmail.com", "skuskaapi3");
            String system = System.getProperty("os.name");
            int trkID = Integer.parseInt(request.getParameter("trkID"));
            
            DBLoginFinder loginFinder = new DBLoginFinder();
            int userID = loginFinder.getUserId(session.getAttribute("username").toString());
            DBTrackFinder trackFinder = new DBTrackFinder();
            int realTrkID = trackFinder.getTrackUserID(trkID);
            
            if(realTrkID == userID){
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
                    response.sendRedirect("ShowTracks.jsp");
            }else{
                FileLogger.getInstance().createNewLog("Warning: User " + session.getAttribute("username") + " tried to delete userID " + realTrkID + " track!!!");
                response.sendRedirect("ShowTracks.jsp");
            }
            
        %>
            
    </body>
</html>
