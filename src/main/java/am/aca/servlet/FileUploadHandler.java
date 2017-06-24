package am.aca.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 */
@WebServlet("/upload")
public class FileUploadHandler extends HttpServlet {
    private String UPLOAD_DIRECTORY;



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UPLOAD_DIRECTORY = System.getProperty("user.home");
        String uploadPathName = "uploads";
        boolean success;
        if (!Files.exists(Paths.get(System.getProperty("user.home")+"\\"+uploadPathName))) {
            success = new java.io.File(System.getProperty("user.home"), uploadPathName).mkdirs();
        } else success = true;

        if (ServletFileUpload.isMultipartContent(request) && success) {
            try {
                List<FileItem> multiParts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request);
                for (FileItem item : multiParts) {
                    if (!item.isFormField()) {
                        String[] name = item.getName().split("\\.");
                        if(getServletContext().getMimeType(item.getName()).startsWith("image/")) {
                            int index = name.length - 1;
                            item.write(new File(UPLOAD_DIRECTORY + "\\" + uploadPathName + File.separator + 1 + "." + name[index]));
                        } else throw new RuntimeException("The upload file is not image type");
                    }
                }
                request.setAttribute("message", null);
            } catch (Exception e) {
                request.setAttribute("message", "File Upload Failed due to " + e);
            }
        } else {
            request.setAttribute("message", "Sorry this Servlet only handles file upload request");
        }
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }

}
