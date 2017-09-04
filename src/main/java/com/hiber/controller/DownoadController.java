/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiber.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author SHWETA
 */
@Controller
public class DownoadController {

    @Autowired
    ServletContext context;

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public ModelAndView saveimage(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filePath = "C:\\Users\\SHWETA\\Documents\\mycv.pdf";
            File file = new File(filePath);
            FileInputStream fin = new FileInputStream(file);
            String mimeType = context.getMimeType(filePath);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.setContentLength((int) file.length());

            //forces download
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
            response.setHeader(headerKey, headerValue);

            OutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int byteRead = -1;
            while ((byteRead = fin.read(buffer)) != -1) {
                outStream.write(buffer, 0, byteRead);
            }
            fin.close();
            outStream.close();
            return new ModelAndView("index", "filesuccess", "Downloaded Successfully");
        } catch (FileNotFoundException ex) {
            return new ModelAndView("index", "filesuccess", "No such file found");
            
        } catch (IOException ex) {
            return new ModelAndView("index", "filesuccess", "Error Occured, try again");
            
        }
    }
}
