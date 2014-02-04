import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@WebServlet(name = "File", urlPatterns = {"/file"})
public class File extends HttpServlet {
    Path rootPath;

    public void init() throws ServletException {
        ServletContext context = getServletContext();
        String sharedFolder = context.getRealPath(context.getInitParameter("sharedFolder"));
        rootPath = Paths.get(sharedFolder);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String content = request.getParameter("content");
        String path = request.getParameter("path");
        Path filePath = null;

        if ((content != null) && (path != null)){
            filePath = rootPath.resolve(URLDecoder.decode(path, StandardCharsets.UTF_8.displayName())).normalize();

            if (filePath.startsWith(rootPath) && Files.exists(filePath) && Files.isRegularFile(filePath)){
                try(BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                    writer.write(content);
                }
            }
        }

        String redirectPath = rootPath.relativize(filePath.getParent()).toString();
        response.sendRedirect("./main?path=" + URLEncoder.encode(redirectPath, StandardCharsets.UTF_8.displayName()));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        List<String> fileContent = null;
        Path filePath = null;


        if(path != null){
            filePath = rootPath.resolve(URLDecoder.decode(path, StandardCharsets.UTF_8.displayName())).normalize();

            if(filePath.startsWith(rootPath) && Files.exists(filePath)){
                fileContent = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            }
        }

        request.setAttribute("fileContent", fileContent);
        request.setAttribute("rootPath", rootPath);
        request.setAttribute("filePath", filePath);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/file.jsp");
        dispatcher.forward(request, response);
    }
}
