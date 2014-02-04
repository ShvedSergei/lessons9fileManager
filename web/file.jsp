<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.nio.file.Path" %>
<%@ page import="java.awt.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: shved
  Date: 04.02.14
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>File Manager</title>
    <style>
        .center {
            width: 900px;
            padding: 10px;
            margin: auto;
            background: #00ff7f;
        }
    </style>
</head>
<body>
<div class="center">
    <center><h1>Edit file</h1></center>
    <%
        Path rootPath = (Path) request.getAttribute("rootPath");
        Path filePath = (Path) request.getAttribute("filePath");
        List<String> fileContent = (List<String>) request.getAttribute("fileContent");
        if(fileContent != null){
            if ("edit".equals(request.getParameter("act"))){
                out.print("<form action='./file?path=");
                out.print(URLEncoder.encode(rootPath.relativize(filePath).toString(), StandardCharsets.UTF_8.displayName()));
                out.println("' method='post'>");
                out.println("<p><textarea rows='30' cols='100' name='content'>");
                for(String line : fileContent){
                    out.println(line);
                }
                out.println("</textarea></p>");
                out.println("<p><input type='submit' value='Save' /></p>");
                out.println("</form>");
            }else{
                for(String line : fileContent){
                    out.print(line);
                    out.println("<br />");
                }
            }
        }
    %>
</div>
</body>
</html>
