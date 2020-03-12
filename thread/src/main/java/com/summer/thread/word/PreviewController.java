package com.summer.thread.word;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *附件预览Controller
 */
@Controller
public class PreviewController {

    /**
     * 模拟访问
     */
    @RequestMapping("/office/open")
    public String openOffice(HttpServletRequest request) throws IOException{
        String type = request.getParameter("type");
        String path = request.getParameter("path");
        if ("jpg".equalsIgnoreCase(type) || "bmp".equalsIgnoreCase(type)
                || "png".equalsIgnoreCase(type)
                || "jpeg".equalsIgnoreCase(type)) {//图片
            type = "image";
        } else if ("txt".equalsIgnoreCase(type)) {//txt文本
            type = "txt";
        }
        path = URLDecoder.decode(path,"UTF-8");
        request.setAttribute("path", path);
        request.setAttribute("type", type);
        if("doc".equalsIgnoreCase(type) || "docx".equalsIgnoreCase(type)) {
            return "word/wordView.word";
        } else if ("xlsx".equalsIgnoreCase(type) || "xls".equalsIgnoreCase(type)) {
            return "word/excelView.word";
        } else if ("pdf".equalsIgnoreCase(type)){
            return "word/pdfView.word";
        } else {
            return "word/ortherView.word";
        }
    }

    /**
     * excel文件页面显示
     */
    @RequestMapping("/office/openExcel")
    public void openExcel(HttpServletRequest request,HttpServletResponse res)throws Exception{
        String path = request.getParameter("path");
        path = URLDecoder.decode(path,"UTF-8");
        byte[] bdata = fileToBytes(path);
        if(bdata!=null){
            ServletOutputStream op;
            try {
                op = res.getOutputStream();
                op.write(bdata);
                op.close();
                if (op.equals(null)) {
                    res.flushBuffer();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * word文件页面显示
     */
    @RequestMapping("/office/openWord")
    public void openWord(HttpServletRequest request,HttpServletResponse res)throws Exception{
        String path = request.getParameter("path");
        path = URLDecoder.decode(path,"UTF-8");
        byte[] bdata = fileToBytes(path);
        if(bdata!=null){
            ServletOutputStream op;
            try {
                op = res.getOutputStream();
                op.write(bdata);
                op.close();
                if (op.equals(null)) {
                    res.flushBuffer();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     *图片预览
     */
    @RequestMapping("/office/openImage")
    public void openImage(HttpServletRequest request,HttpServletResponse res)throws Exception{
        String path = request.getParameter("path");
        path = URLDecoder.decode(path,"UTF-8");
        res.setHeader("Content-Disposition", "filename=" + "image");
        res.setContentType("image/*");
        byte[] bdata = fileToBytes(path);
        if(bdata!=null){
            ServletOutputStream op;
            try {
                op = res.getOutputStream();
                op.write(bdata);
                op.close();
                if (op.equals(null)) {
                    res.flushBuffer();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     *txt文本预览
     */
    @RequestMapping("/office/openOtherTxt")
    public void openOtherTxt(HttpServletRequest request,HttpServletResponse res)throws Exception{
        String path = request.getParameter("path");
        path = URLDecoder.decode(path,"UTF-8");
        byte[] bdata = fileToBytes(path);
        res.reset();
        //res.setCharacterEncoding("UTF-8");
        res.setContentType("text/plain;charset=gb2312");
        if(bdata!=null){
            OutputStream op;
            try {
                op = res.getOutputStream();
                op.write(bdata);
                op.close();
                if (op.equals(null)) {
                    res.flushBuffer();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 返回服务端PDF文件流
     */
    @RequestMapping("/office/openPdf")
    public void getPdfFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getParameter("path");
        path = URLDecoder.decode(path,"UTF-8");
        response.reset();
        response.setContentType("application/pdf;charset=UTF-8");// 定义输出类型
        byte[] bdata = fileToBytes(path);
        if(bdata!=null){
            ServletOutputStream op;
            try {
                op = response.getOutputStream();
                op.write(bdata);
                op.close();
                if (op.equals(null)) {
                    response.flushBuffer();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    @SuppressWarnings("resource")
    private byte[] fileToBytes(String path){
        byte[] buffer = null;
        File file = new File(path);

        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int n;

            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            buffer = bos.toByteArray();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}

