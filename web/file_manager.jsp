<%@ page import="java.nio.file.Path" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.io.*" %>
<%@ page import="java.io.File" %>

<%--
  Created by IntelliJ IDEA.
  User: shved
  Date: 31.01.14
  Time: 18:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>File Manager - <% Path currentPath = (Path) request.getAttribute("currentPath");
        out.println(currentPath.getFileName()); %></title>
    <style>
        .center {
            width: 600px;
            padding: 10px;
            margin: auto;
            background: #00ff7f;
        }
    </style>
</head>
<body>
<div class="center">
    <center><h1>File Manager</h1></center>
    <table width='600'>
        <tr><th>Name</th><th>Size</th><th>Options</th></tr>
        <% Path rootPath = (Path) request.getAttribute("rootPath");
            if (!currentPath.equals(rootPath)) {
            out.print("<tr><td><a href='./main?path=");
            out.print(URLEncoder.encode(rootPath.relativize(currentPath.getParent()).toString(), StandardCharsets.UTF_8.displayName()));
            out.println("'>..</a></td><td></td><td></td></tr");
        }
        File[] files = currentPath.toFile().listFiles();

            for (File file : files) {
                if (file.isDirectory()) {
                    String filePath = rootPath.relativize(file.toPath()).toString();
                    String encodedName = URLEncoder.encode(filePath, StandardCharsets.UTF_8.displayName());

                    out.print("<tr><td><a href='./main?path=");
                    out.print(encodedName);
                    out.print("'>");
                    out.print(file.getName());
                    out.println("</a></td>");

                    out.println("<td>DIR</td>");

                    out.print("<td><a href='./main?path=");
                    out.print(encodedName);
                    out.println("&act=delete'>Delete</a></td></tr>");
                }
            }

            for (File file : files) {
                if (file.isFile()) {
                    String filePath = rootPath.relativize(file.toPath()).toString();
                    String encodedName = URLEncoder.encode(filePath, StandardCharsets.UTF_8.displayName());

                    out.print("<tr><td><a href='./file?path=");
                    out.print(encodedName);
                    out.print("'>");
                    out.print(file.getName());
                    out.println("</a></td>");

                    out.print("<td>");
                    out.print(file.length());
                    out.println("</td>");

                    out.print("<td><a href='./file?path=");
                    out.print(encodedName);
                    out.println("&act=edit'>Edit</a> ");
                    out.print("<a href='./main?path=");
                    out.print(encodedName);
                    out.println("&act=delete'>Delete</a></td></tr>");
                }
            }
        %>
    </table>
    <p>Create directory or file:</p>
    <form action='./main?path=<%URLEncoder.encode(rootPath.relativize(currentPath).toString(), StandardCharsets.UTF_8.displayName());%>' method='post'>
        <p><label>Name: <input type='text' name='name' size='40' required /></label></p>
        <p><label><input type='radio' name='type' value='dir' checked />Directory </label><label><input type='radio' name='type' value='file' />File</label></p>
        <p><input type='submit' value='Create' /></p>
    </form>
</div>
</body>
</html>
