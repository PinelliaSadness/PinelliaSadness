package com.summer.thread.word;


import com.itextpdf.text.pdf.PdfReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransformOffice2Image {

    public static void main(String[] args) {
        String outPutPath = "D:/万顺叫车简介.pdf";
        String dstImgFolder = "D:/";
        long start = System.currentTimeMillis();
//        pdf2Image(outPutPath, dstImgFolder, 96);
        pdf2OneImage(outPutPath, dstImgFolder, 96);
        long end = System.currentTimeMillis();
        System.out.println("总共耗时：" + (end - start));
    }

    /***
     * PDF文件转PNG/JPEG图片
     * @param PdfFilePath   pdf整路径
     * @param dstImgFolder   图片存放的文件夹
     * @param dpi   越大转换后越清晰，相对转换速度越慢,一般电脑默认96dpi
     */
    public static void pdf2Image(String PdfFilePath,
                                     String dstImgFolder, int dpi) {
        File file = new File(PdfFilePath);
        PDDocument pdDocument;
        try {
            String imgPDFPath = file.getParent();
            int dot = file.getName().lastIndexOf('.');
            // 获取图片文件名
            String imagePDFName = file.getName().substring(0, dot);
            String imgFolderPath;
            if (dstImgFolder.equals("")) {
                // 获取图片存放的文件夹路径
                imgFolderPath = imgPDFPath + File.separator + imagePDFName;
            } else {
                imgFolderPath = dstImgFolder + File.separator + imagePDFName;
            }

            if (createDirectory(imgFolderPath)) {
                pdDocument = PDDocument.load(file);
                PDFRenderer renderer = new PDFRenderer(pdDocument);
                PdfReader reader = new PdfReader(PdfFilePath);
                int pages = reader.getNumberOfPages();// 获取PDF页数
                System.out.println("PDF page number is:" + pages);
                StringBuffer imgFilePath;
                for (int i = 0; i < pages; i++) {
                    String imgFilePathPrefix = imgFolderPath
                            + File.separator + imagePDFName;
                    imgFilePath = new StringBuffer();
                    imgFilePath.append(imgFilePathPrefix);
                    imgFilePath.append("_");
                    imgFilePath.append(i + 1);
                    imgFilePath.append(".png");// PNG
                    File dstFile = new File(imgFilePath.toString());
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                    ImageIO.write(image, "png", dstFile);// PNG
                }
                System.out.println("PDF文档转PNG图片成功！");
            } else {
                System.out.println("PDF文档转PNG图片失败："+ "创建" + imgFolderPath + "失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pdf2OneImage(String PdfFilePath, String dstImgFolder, int dpi) {
        File file = new File(PdfFilePath);
        PDDocument pdDocument;
        try {
            String imgPDFPath = file.getParent();
            int dot = file.getName().lastIndexOf('.');
            // 获取图片文件名
            String imagePDFName = file.getName().substring(0, dot);
            String imgFolderPath;
            if (dstImgFolder.equals("")) {
                // 获取图片存放的文件夹路径
                imgFolderPath = imgPDFPath + File.separator + imagePDFName;
            } else {
                imgFolderPath = dstImgFolder + File.separator + imagePDFName;
            }

            List<BufferedImage> piclist = new ArrayList<>();

            if (createDirectory(imgFolderPath)) {
                pdDocument = PDDocument.load(file);
                PDFRenderer renderer = new PDFRenderer(pdDocument);
                PdfReader reader = new PdfReader(PdfFilePath);
                int pages = reader.getNumberOfPages();// 获取PDF页数
                System.out.println("PDF page number is:" + pages);
                for (int i = 0; i < pages; i++) {
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                    piclist.add(image);
                }
                String imgFilePathPrefix = imgFolderPath + File.separator + imagePDFName;
                StringBuffer imgFilePath = new StringBuffer();
                imgFilePath.append(imgFilePathPrefix);
                imgFilePath.append(".png");// PNG

                outOneImage(piclist,imgFilePath.toString());
                System.out.println("PDF文档转PNG图片成功！");
            } else {
                System.out.println("PDF文档转PNG图片失败："+ "创建" + imgFolderPath + "失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将宽度相同的图片，竖向追加在一起，经过修改，没有了相同宽度这一限制
     * @param piclist 文件流数组
     * @param outPath 输出路径
     */
    public static void outOneImage(List<BufferedImage> piclist, String outPath){
        // 纵向处理图片
        if (piclist == null || piclist.size() <= 0) {
            //Trace.logInfo(Trace.MODULE_ACTION, "图片数组为空!");
        }
        int height = 0, // 总高度
            _width = 0, // 临时宽度
            maxWidth = 0, // 最大宽度
            _height = 0, // 临时的高度 , 或保存偏移高度
            __height = 0, // 临时的高度，主要保存每个高度
            picNum = piclist.size();// 图片的数量
        int[] heightArray = new int[picNum]; // 保存每个文件的高度
        int[] widthArray = new int[picNum]; // 保存每个文件的宽度
        BufferedImage buffer = null; // 保存图片流
        List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
        int[] _imgRGB; // 保存一张图片中的RGB数据
        for (int i = 0; i < picNum; i++) {
            buffer = piclist.get(i);
            if (buffer.getWidth() > maxWidth) {
                // 获取最大宽度
                maxWidth = buffer.getWidth();
            }
        }
        for (int i = 0; i < picNum; i++) {
            buffer = piclist.get(i);
            heightArray[i] = _height = buffer.getHeight();// 图片高度
            widthArray[i] = _width = buffer.getWidth();// 图片宽度
            height += _height; // 获取总高度
            _imgRGB = new int[_width * _height];// 从图片中读取RGB
            _imgRGB = buffer.getRGB(0, 0, _width, _height, _imgRGB, 0, _width);
            imgRGB.add(_imgRGB);
        }
        _height = 0; // 设置偏移高度为0
        // 生成新图片
        BufferedImage imageResult = new BufferedImage(maxWidth, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < picNum; i++) {
            __height = heightArray[i];
            _width = widthArray[i];
            if (i != 0){
                _height += heightArray[i - 1]; // 计算偏移高度 ，因高度不一致，所以i-1
            }
            // 居中，前两个参数为起始的宽度、高度
            imageResult.setRGB((maxWidth-_width)/2,_height,_width,__height,imgRGB.get(i),0,_width);//写入流中
        }
        File dstFile = new File(outPath);
        try {
            ImageIO.write(imageResult, "png", dstFile);// 写图片
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }


}
