package com.summer.thread.word;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

public class ExcelForPDF {
    public static byte[] excelToPdf(String excelPath, String pdfPath) throws Exception {
        InputStream in = new FileInputStream(excelPath);

        Workbook workbook;
        if (excelPath.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(in);
        } else {
            workbook = new HSSFWorkbook(in);
        }

        Sheet sheet = workbook.getSheetAt(0);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);//此处根据excel大小设置pdf纸张大小
        PdfWriter writer = PdfWriter.getInstance(document, stream);
        document.setMargins(0, 0, 15, 15);//设置也边距
        document.open();
        float[] widths = getColWidth(sheet);

        PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(90);
        int colCount = widths.length;
        BaseFont baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);//设置基本字体

        for (int r = sheet.getFirstRowNum(); r < sheet.getPhysicalNumberOfRows(); r++) {
            Row row = sheet.getRow(r);
            if (row != null) {
                for (int c = row.getFirstCellNum(); (c < row.getLastCellNum() || c < colCount) && c > -1; c++) {
                    if (c >= row.getPhysicalNumberOfCells()) {
                        PdfPCell pCell = new PdfPCell(new Phrase(""));
                        pCell.setBorder(0);
                        table.addCell(pCell);
                        continue;
                    }
                    Cell excelCell = row.getCell(c);
                    String value = "";

                    if (excelCell != null) {
                        value = excelCell.toString().trim();
                        if (value != null && value.length() != 0) {
                            String dataFormat = excelCell.getCellStyle().getDataFormatString();//获取excel单元格数据显示样式
                            if (dataFormat != "General" && dataFormat != "@") {
                                try {
                                    String numStyle = getNumStyle(dataFormat);
                                    value = numFormat(numStyle, excelCell.getNumericCellValue());
                                } catch (Exception e) {

                                }
                            }
                        }
                    }
                    org.apache.poi.ss.usermodel.Font excelFont = getExcelFont(workbook, excelCell, excelPath);

                    //HSSFFont excelFont = excelCell.getCellStyle().getFont(workbook);
                    Font pdFont = new Font(baseFont, excelFont.getFontHeightInPoints(),
                            excelFont.getBoldweight() == 700 ? Font.BOLD : Font.NORMAL, BaseColor.BLACK);//设置单元格字体

                    PdfPCell pCell = new PdfPCell(new Phrase(value, pdFont));
                    List<PicturesInfo> infos = POIExtend.getAllPictureInfos(sheet, r, r, c, c, false);
                    if (!infos.isEmpty()) {
                        pCell = new PdfPCell(Image.getInstance(infos.get(0).getPictureData()));
                        PicturesInfo info = infos.get(0);
                        System.out.println("最大行：" + info.getMaxRow() + "最小行：" + info.getMinRow() + "最大列:" + info.getMaxCol() + "最小列：" + info.getMinCol());
                        ;
                    }

                    boolean hasBorder = hasBorder(excelCell);
                    if (!hasBorder) {
                        pCell.setBorder(0);
                    }
                    pCell.setHorizontalAlignment(getHorAglin(excelCell.getCellStyle().getAlignment()));
                    pCell.setVerticalAlignment(getVerAglin(excelCell.getCellStyle().getVerticalAlignment()));

                    pCell.setMinimumHeight(row.getHeightInPoints());
                    if (isMergedRegion(sheet, r, c)) {
                        int[] span = getMergedSpan(sheet, r, c);
                        if (span[0] == 1 && span[1] == 1) {//忽略合并过的单元格
                            continue;
                        }
                        pCell.setRowspan(span[0]);
                        pCell.setColspan(span[1]);
                        c = c + span[1] - 1;//合并过的列直接跳过
                    }

                    table.addCell(pCell);

                }
            } else {
                PdfPCell pCell = new PdfPCell(new Phrase(""));
                pCell.setBorder(0);
                pCell.setMinimumHeight(13);
                table.addCell(pCell);
            }
        }
        document.add(table);
        document.close();

        byte[] pdfByte = stream.toByteArray();
        stream.flush();
        stream.reset();
        stream.close();

        FileOutputStream outputStream = new FileOutputStream(pdfPath);
        outputStream.write(pdfByte);
        outputStream.flush();
        outputStream.close();

        return pdfByte;
    }

    //获取字体  
    private static org.apache.poi.ss.usermodel.Font getExcelFont(Workbook workbook, Cell cell, String excelName) {
        if (excelName.endsWith(".xls")) {
            return ((HSSFCell) cell).getCellStyle().getFont(workbook);
        }
        return ((XSSFCell) cell).getCellStyle().getFont();
    }

    /**
     *      * 判断excel单元格是否有边框
     *      * @param excelCell
     *      * @return
     *     
     */
    private static boolean hasBorder(Cell excelCell) {
        short top = excelCell.getCellStyle().getBorderTop();
        short bottom = excelCell.getCellStyle().getBorderBottom();
        short left = excelCell.getCellStyle().getBorderLeft();
        short right = excelCell.getCellStyle().getBorderRight();
        return top + bottom + left + right > 2;
    }



    /**
     *      * 获取excel单元格数据显示格式
     *      * @param dataFormat
     *      * @return
     *      * @throws Exception
     *     
     */
    private static String getNumStyle(String dataFormat) throws Exception {
        if (dataFormat == null || dataFormat.length() == 0) {
            throw new Exception("");
        }
        if (dataFormat.indexOf("%") > -1) {
            return dataFormat;
        } else {
            return dataFormat.substring(0, dataFormat.length() - 2);
        }
    }


    /**
      * 判断单元格是否是合并单元格
      * @param sheet
      * @param row
      * @param column
      * @return
      */
    private static boolean isMergedRegion(Sheet sheet, int row, int column) {
    int sheetMergeCount = sheet.getNumMergedRegions();
    for (int i = 0; i < sheetMergeCount; i++) {
        CellRangeAddress range = sheet.getMergedRegion(i);
        int firstColumn = range.getFirstColumn();
        int lastColumn = range.getLastColumn();
        int firstRow = range.getFirstRow();
        int lastRow = range.getLastRow();
        if (row >= firstRow && row <= lastRow) {
            if (column >= firstColumn && column <= lastColumn) {
                return true;
            }
        }
    }
    return false;
    }



    /**
      * 计算合并单元格合并的跨行跨列数
      * @param sheet
      * @param row
      * @param column
      * @return
      */
    private static int[] getMergedSpan(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        int[] span = { 1, 1 };
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (firstColumn == column && firstRow == row) {
                span[0] = lastRow - firstRow + 1;
                span[1] = lastColumn - firstColumn + 1;
                break;
            }
        }
        return span;
    }


    /**
      * 获取excel中每列宽度的占比
      * @param sheet
      * @return
      */
    private static float[] getColWidth(Sheet sheet) {
        int rowNum = getMaxColRowNum(sheet);
        Row row = sheet.getRow(rowNum);
        int cellCount = row.getPhysicalNumberOfCells();
        int[] colWidths = new int[cellCount];
        int sum = 0;

        for (int i = row.getFirstCellNum(); i < cellCount; i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                colWidths[i] = sheet.getColumnWidth(i);
                sum += sheet.getColumnWidth(i);
            }
        }

        float[] colWidthPer = new float[cellCount];
        for (int i = row.getFirstCellNum(); i < cellCount; i++) {
            colWidthPer[i] = (float) colWidths[i] / sum * 100;
        }
        return colWidthPer;
    }

    /**
      * 获取excel中列数最多的行号
      * @param sheet
      * @return
      */
    private static int getMaxColRowNum(Sheet sheet) {
        int rowNum = 0;
        int maxCol = 0;
        for (int r = sheet.getFirstRowNum(); r < sheet.getPhysicalNumberOfRows(); r++) {
            Row row = sheet.getRow(r);
            if (row != null && maxCol < row.getPhysicalNumberOfCells()) {
                maxCol = row.getPhysicalNumberOfCells();
                rowNum = r;
            }
        }
        return rowNum;
    }


    /**
      * excel垂直对齐方式映射到pdf对齐方式
      * @param aglin
      * @return
      */
    private static int getVerAglin(int aglin) {
        switch (aglin) {
            case 1:
                return com.itextpdf.text.Element.ALIGN_MIDDLE;
            case 2:
                return com.itextpdf.text.Element.ALIGN_BOTTOM;
            case 3:
                return com.itextpdf.text.Element.ALIGN_TOP;
            default:
                return com.itextpdf.text.Element.ALIGN_MIDDLE;
        }
    }


    /**
      * excel水平对齐方式映射到pdf水平对齐方式
      * @param aglin
      * @return
      */
private static int getHorAglin(int aglin) {
        switch (aglin) {
            case 2:
                return com.itextpdf.text.Element.ALIGN_CENTER;
            case 3:
                return com.itextpdf.text.Element.ALIGN_RIGHT;
            case 1:
                return com.itextpdf.text.Element.ALIGN_LEFT;
            default:
                return com.itextpdf.text.Element.ALIGN_CENTER;
        }
    }


        /**
      * 格式化数字
      * @param pattern
      * @param num
      * @return
      */
    private static String numFormat(String pattern, double num) {
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(num);
    }

}