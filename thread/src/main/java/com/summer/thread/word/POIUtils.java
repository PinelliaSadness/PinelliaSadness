package com.summer.thread.word;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import fr.opensagres.poi.xwpf.converter.core.FileURIResolver;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import fr.opensagres.poi.xwpf.converter.core.BasicURIResolver;
import fr.opensagres.poi.xwpf.converter.core.FileImageExtractor;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;

public class POIUtils {
    private static final Logger log = LoggerFactory.getLogger(POIUtils.class.getName());

    /**
     * docx to html
     *
     * @param in 输入流
     * @return
     * @throws IOException
     */
    public static InputStream docxToHtml(InputStream in, String filePath) {
        XWPFDocument document = null;
        try {
            document = new XWPFDocument(in);
            XHTMLOptions options = XHTMLOptions.create();
            options.setIgnoreStylesIfUnused(false);
            options.setFragment(true);
            filePath = filePath+"image"+File.separator;
            options.setExtractor(new FileImageExtractor(new File(filePath)));
            options.URIResolver(new BasicURIResolver("image"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XHTMLConverter.getInstance().convert(document, out, options);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return in;
    }

    /**
     * doc to html
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream docToHtml(InputStream in) {
        try {
            HWPFDocument wordDocument = new HWPFDocument(in);
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(outStream);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer serializer = factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            outStream.close();
            return new ByteArrayInputStream(outStream.toByteArray());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return in;
    }

    public static InputStream docToHtml(Map<String, Object> request, InputStream in) {
        String fileName = (String) request.get("name");
        if (StringUtils.isEmpty(fileName)) {
            return in;
        }
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String prevName = fileName.substring(0, fileName.lastIndexOf("."));
        if ("html".equalsIgnoreCase(extensionName)) {
            return in;
        }
        if (extensionName.equalsIgnoreCase("doc")) {
            request.put("name", prevName + ".html");
            return docToHtml(in);
        }
        if (extensionName.equalsIgnoreCase("docx")) {
            request.put("name", prevName + ".html");
            return docxToHtml(in, (String)request.get("filePath"));
        }
        return in;
    }

    public static void inputStreamToFile(InputStream inputStream, String newPath) {
//		InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            /**
             String userDir = System.getProperty("user.dir");
             String path = userDir + "/bin/file.xml";
             inputStream = new FileInputStream(path);
             */

//			String newPath = userDir + "/bin/file-new.xml";
            File file = new File(newPath);
            outputStream = new FileOutputStream(file);

            int bytesWritten = 0;
            int byteCount = 0;

            byte[] bytes = new byte[1024];

            while ((byteCount = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, bytesWritten, byteCount);
//				bytesWritten += byteCount;
            }

            System.out.println("Done!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public static void main(String[] args) throws Exception {
//        try {
//            String fileName = "分支机构后台管理系统计划书.docx";
//            String newfileName = "分支机构后台管理系统计划书.html";
//            String filePath = "D:/";
//            Map<String, Object> request = new HashMap<String, Object>();
//            request.put("name", fileName);
//            request.put("filePath", filePath);
//            InputStream in = new FileInputStream(filePath + fileName);// 读取文件的数据。
//            InputStream result = docToHtml(request, in);
//            inputStreamToFile(result, filePath+newfileName);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Word2007ToHtml();
//        Word2003ToHtml();

    }




    /**
     * 2007版本word转换成html
     * @throws IOException
     */
    public static void Word2007ToHtml() throws IOException {
        String filepath = "D:/";
        String fileName = "分支机构后台管理系统计划书.docx";
        String htmlName = "分支机构后台管理系统计划书.html";
        final String file = filepath + fileName;
        File f = new File(file);
        if (!f.exists()) {
            System.out.println("Sorry File does not Exists!");
        } else {
            if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX")) {

                // 1) 加载word文档生成 XWPFDocument对象
                InputStream in = new FileInputStream(f);
                XWPFDocument document = new XWPFDocument(in);

                // 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
                File imageFolderFile = new File(filepath);
                XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
                options.setExtractor(new FileImageExtractor(imageFolderFile));
                options.setIgnoreStylesIfUnused(false);
                options.setFragment(true);

                // 3) 将 XWPFDocument转换成XHTML
                OutputStream out = new FileOutputStream(new File(filepath + htmlName));
                XHTMLConverter.getInstance().convert(document, out, options);

                //也可以使用字符数组流获取解析的内容
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                XHTMLConverter.getInstance().convert(document, baos, options);
//                String content = baos.toString();
//                System.out.println(content);
//                 baos.close();
            } else {
                System.out.println("Enter only MS Office 2007+ files");
            }
        }
    }






    /**
     * /**
     * 2003版本word转换成html
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public static void Word2003ToHtml() throws IOException, TransformerException, ParserConfigurationException {
        final String imagepath = "F:/";//解析时候如果doc文件中有图片  图片会保存在此路径
        String filepath = "D:/";
        String fileName = "分支机构后台管理系统计划书.docx";
        String htmlName = "分支机构后台管理系统计划书.html";
        final String file = filepath + fileName;
        InputStream input = new FileInputStream(new File(file));
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        //设置图片存放的位置
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
//                File imgPath = new File(imagepath);
//                if(!imgPath.exists()){//图片目录不存在则创建
//                    imgPath.mkdirs();
//                }
//                File file = new File(imagepath + suggestedName);
//                try {
//                    OutputStream os = new FileOutputStream(file);
//                    os.write(content);
//                    os.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return imagepath + suggestedName;
                return "http://t8.baidu.com/it/u=3571592872,3353494284&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1584525947&t=55c75eb73ea1d9063065981842168752";
            }
        });

        //解析word文档
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();

        File htmlFile = new File(filepath + htmlName);
        OutputStream outStream = new FileOutputStream(htmlFile);

        //也可以使用字符数组流获取解析的内容
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        OutputStream outStream = new BufferedOutputStream(baos);

        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");

        serializer.transform(domSource, streamResult);

        //也可以使用字符数组流获取解析的内容
//        String content = baos.toString();
//        System.out.println(content);
//        baos.close();
        outStream.close();
    }

    //官方是这样写的
    public static void wordToHtml () throws Exception{
        HWPFDocumentCore wordDocument = WordToHtmlUtils.loadDoc(new FileInputStream("D:/分支机构后台管理系统计划书.doc"));

        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
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

        String result = new String(out.toByteArray());
        System.out.println(result);
    }
}
