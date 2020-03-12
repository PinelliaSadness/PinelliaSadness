package com.summer.thread.word;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ExcelPreview {

    /**
     * @param response
     * @param wb excel生成的Workbook （根据业务创建Workbook）
     * @throws IOException
     */
    public void writToAjax(HttpServletResponse response, HSSFWorkbook wb) throws IOException {
        try {
            ExcelToHtmlConverter ethc = new ExcelToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            ethc.setOutputColumnHeaders(false);
            ethc.setOutputRowNumbers(false);
            ethc.processWorkbook(wb);

            Document htmlDocument = ethc.getDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(out);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            out.close();

            String htmlStr = new String(out.toByteArray(), "UTF-8");

            htmlStr = htmlStr.replace("<h2>Sheet1</h2>", "").replace("<h2>Sheet2</h2>", "")
                    .replace("<h2>Sheet3</h2>", "").replace("<h2>Sheet4</h2>", "").replace("<h2>Sheet5</h2>", "");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter pw = response.getWriter();
            pw.print(htmlStr);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
