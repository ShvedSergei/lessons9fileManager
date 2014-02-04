import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by shved on 28.01.14.
 */

@WebServlet(name = "FileManager", urlPatterns = {"/fm", "/main"})
public class FileManager extends HttpServlet {
    Path rootPath;


    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        String sharedFolder = context.getRealPath(context.getInitParameter("sharedFolder"));
        rootPath = Paths.get(sharedFolder);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String path = request.getParameter("path");

        if (!(name == null || path == null || name.equals(""))) {
            Path creationPath = rootPath.resolve(URLDecoder.decode(path, StandardCharsets.UTF_8.displayName())).resolve(name).normalize();

            if (creationPath.startsWith(rootPath) && Files.notExists(creationPath)) {
                if (type.equals("dir")) {
                    Files.createDirectory(creationPath);

                } else if (type.equals("file")) {
                    Files.createFile(creationPath);

                }
            }
        }
        response.sendRedirect(request.getRequestURL().append("?").append(request.getQueryString()).toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Path currentPath;
        String path = request.getParameter("path");

        if (path == null) {
            currentPath = rootPath;
        } else {
            path = URLDecoder.decode(path, StandardCharsets.UTF_8.displayName());
            Path tempPath = rootPath.resolve(path).normalize();

            if (tempPath.startsWith(rootPath)) {
                currentPath = tempPath;

                if ("delete".equals(request.getParameter("act"))) {
                    Files.deleteIfExists(currentPath);
                    currentPath = currentPath.getParent();
                }
            } else {
                currentPath = rootPath;
            }
        }
        request.setAttribute("currentPath", currentPath);
        request.setAttribute("rootPath", rootPath);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/file_manager.jsp");
        dispatcher.forward(request, response);
    }




}