package com.tpm.mobile.results.servlet;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class VideoStreamServlet
 */
@WebServlet("/videostream")
public class VideoReportStream extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(VideoReportStream.class);

    /**
     * Default constructor. 
     */
    public VideoReportStream() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    	String filename=request.getParameter("video");
        String range = request.getHeader("range");
        String browser = request.getHeader("User-Agent");
        String contextPath =request.getServletContext().getRealPath("");
    	
    	String videopath = contextPath+"\\videos";
    	videopath=videopath.replace("/", "\\");

       
        log.info(browser);
        if(browser.indexOf("Firefox") != -1){
            log.info("==========ITS FIREFOX=============");
            byte[] data = getBytesFromFile(new File(videopath+filename));
            response.setContentType("video/ogg");
            response.setContentLength(data.length);
            response.setHeader("Content-Range", range + Integer.valueOf(data.length-1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            byte[] content = new byte[1024];
            BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
            OutputStream os = response.getOutputStream();
            while (is.read(content) != -1) {
                //log.info("... write bytes");
                os.write(content);
            }
            is.close();
            os.close();
        }

        else if(browser.indexOf("Chrome") != -1){
            log.info("==========ITS Chrome=============");
            byte[] data = getBytesFromFile(new File(videopath+filename));
            String diskfilename = "final.mp4";
            response.setContentType("video/mp4");
            //response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"" );
            log.info("data.length " + data.length);
            response.setContentLength(data.length);
            response.setHeader("Content-Range", range + Integer.valueOf(data.length-1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            byte[] content = new byte[1024];
            BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
            OutputStream os = response.getOutputStream();
            while (is.read(content) != -1) {
                //log.info("... write bytes");
                os.write(content);
            }
            is.close();
            os.close();
        }

        else if(browser.indexOf("MSIE") != -1) {
            log.info("==========ITS IE9=============");
            byte[] data = getBytesFromFile(new File(videopath+filename));
            String diskfilename = "final.mp4";
            response.setContentType("video/mpeg");
            //response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"" );
            log.info("data.length " + data.length);
            response.setContentLength(data.length);
            response.setHeader("Content-Range", range + Integer.valueOf(data.length-1));
            response.setHeader("Accept-Ranges", "text/x-dvi");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            byte[] content = new byte[1024];
            BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
            OutputStream os = response.getOutputStream();
            while (is.read(content) != -1) {
                //log.info("... write bytes");
                os.write(content);
            }
            is.close();
            os.close();
        }
        else if( browser.indexOf("CoreMedia") != -1) {
            log.info("============ Safari=============");
            byte[] data = getBytesFromFile(new File(videopath+filename));
            String diskfilename = "final.mp4";
            response.setContentType("video/mpeg");
            //response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"" );
            log.info("data.length " + data.length);
            //response.setContentLength(data.length);
            //response.setHeader("Content-Range", range + Integer.valueOf(data.length-1));
           // response.setHeader("Accept-Ranges", " text/*, text/html, text/html;level=1, */* ");
           // response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            byte[] content = new byte[1024];
            BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
            OutputStream os = response.getOutputStream();
            while (is.read(content) != -1) {
                //log.info("... write bytes");
                os.write(content);
            }
            is.close();
            os.close();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }   
    @SuppressWarnings("resource")
	private static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        //log.info("\nDEBUG: FileInputStream is " + file);
        // Get the size of the file
        long length = file.length();
        //log.info("DEBUG: Length of " + file + " is " + length + "\n");
        /*
         * You cannot create an array using a long type. It needs to be an int
         * type. Before converting to an int type, check to ensure that file is
         * not loarger than Integer.MAX_VALUE;
         */
        if (length > Integer.MAX_VALUE) {
            log.info("File is too large to process");
            return null;
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while ( (offset < bytes.length)
                &&
                ( (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) ) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

}