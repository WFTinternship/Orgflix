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
    private static final String UPLOAD_DIRECTORY = "upload";
    private static final String UPLOAD_FILE_DIRECTORY = UPLOAD_DIRECTORY + File.separator + "New Folder";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/pages/upload.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (ServletFileUpload.isMultipartContent(request)) {
            // constructs the directory path to store upload file
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_FILE_DIRECTORY;
            // creates the directory if it does not exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            try {
                // parses the request's content to extract file data
                List<FileItem> multiParts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request);
                if (multiParts != null && multiParts.size() > 0) {

                    for (FileItem item : multiParts) {
                        if (!item.isFormField()) {
                            String fileName = new File(item.getName()).getName();
                            String mimeType = getServletContext().getMimeType(fileName);
                            if (mimeType.startsWith("image/")) {
                                // It's an image.
                                String filePath = uploadPath + File.separator + 1 + ".jpg";
                                File storeFile = new File(filePath);
                                item.write(storeFile);
                                request.setAttribute("message", "File Uploaded Successfully " + filePath);
                            } else {
                                request.setAttribute("message", "Upload only jpeg files");

                            }
                        }
                    }
                }
            } catch (Exception e) {
                request.setAttribute("message", "File Upload Failed due to " + e);
            }
        } else {
            request.setAttribute("message", "Sorry this Servlet only handles file upload request");
        }
        request.getRequestDispatcher("WEB-INF/pages/result.jsp").forward(request, response);
    }
}
