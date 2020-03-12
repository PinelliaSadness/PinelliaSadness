//package com.summer.thread.word;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.poi.POIXMLDocumentPart;
//import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
//import org.apache.poi.hssf.usermodel.HSSFPicture;
//import org.apache.poi.hssf.usermodel.HSSFShape;
//import org.apache.poi.hssf.usermodel.HSSFShapeContainer;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
//import org.apache.poi.xssf.usermodel.XSSFDrawing;
//import org.apache.poi.xssf.usermodel.XSSFPicture;
//import org.apache.poi.xssf.usermodel.XSSFShape;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//
//public class POIExtend {
//
//    public static List<PicturesInfo> getAllPictureInfos(Sheet sheet, boolean onlyInternal) throws Exception {
//        return getAllPictureInfos(sheet, null, null, null, null, onlyInternal);
//    }
//
//    public static List<PicturesInfo> getAllPictureInfos(Sheet sheet, Integer minRow, Integer maxRow, Integer minCol,
//                                                        Integer maxCol, boolean onlyInternal) throws Exception {
//        if (sheet instanceof HSSFSheet) {
//            return getXLSAllPictureInfos((HSSFSheet)sheet, minRow, maxRow, minCol, maxCol, onlyInternal);
//        } else if (sheet instanceof XSSFSheet) {
//            return getXLSXAllPictureInfos((XSSFSheet)sheet, minRow, maxRow, minCol, maxCol, onlyInternal);
//        } else {
//            throw new Exception("未处理类型，没有为该类型添加：GetAllPicturesInfos()扩展方法！");
//        }
//    }
//
//    private static List<PicturesInfo> getXLSAllPictureInfos(HSSFSheet sheet, Integer minRow, Integer maxRow,
//                                                            Integer minCol, Integer maxCol, Boolean onlyInternal) {
//        List<PicturesInfo> picturesInfoList = new ArrayList<>();
//
//        HSSFShapeContainer shapeContainer = sheet.getDrawingPatriarch();
//        if (null != shapeContainer) {
//            List<HSSFShape> shapeList = shapeContainer.getChildren();
//            for (HSSFShape shape : shapeList) {
//                if (shape instanceof HSSFPicture && shape.getAnchor() instanceof HSSFClientAnchor) {
//                    HSSFPicture picture = (HSSFPicture) shape;
//                    HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
//
//                    if (isInternalOrIntersect(minRow, maxRow, minCol, maxCol, anchor.getRow1(), anchor.getRow2(),
//                            anchor.getCol1(), anchor.getCol2(), onlyInternal)) {
//                        picturesInfoList.add(
//                                new PicturesInfo(anchor.getRow1(), anchor.getRow2(), anchor.getCol1(), anchor.getCol2(),
//                                        picture.getPictureData().getData(), picture.getPictureData().getMimeType()));
//                    }
//                }
//            }
//        }
//
//        return picturesInfoList;
//    }
//
//    private static List<PicturesInfo> getXLSXAllPictureInfos(XSSFSheet sheet, Integer minRow, Integer maxRow,
//                                                             Integer minCol, Integer maxCol, Boolean onlyInternal) {
//        List<PicturesInfo> picturesInfoList = new ArrayList<>();
//
//        List<POIXMLDocumentPart> documentPartList = sheet.getRelations();
//        for (POIXMLDocumentPart documentPart : documentPartList) {
//            if (documentPart instanceof XSSFDrawing) {
//                XSSFDrawing drawing = (XSSFDrawing) documentPart;
//                List<XSSFShape> shapes = drawing.getShapes();
//                for (XSSFShape shape : shapes) {
//                    if (shape instanceof XSSFPicture) {
//                        XSSFPicture picture = (XSSFPicture) shape;
//                        XSSFClientAnchor anchor = picture.getPreferredSize();
//
//                        if (isInternalOrIntersect(minRow, maxRow, minCol, maxCol, anchor.getRow1(), anchor.getRow2(),
//                                anchor.getCol1(), anchor.getCol2(), onlyInternal)) {
//                            picturesInfoList.add(new PicturesInfo(anchor.getRow1(), anchor.getRow2(), anchor.getCol1(),
//                                    anchor.getCol2(), picture.getPictureData().getData(),
//                                    picture.getPictureData().getMimeType()));
//                        }
//                    }
//                }
//            }
//        }
//
//        return picturesInfoList;
//    }
//
//    private static boolean isInternalOrIntersect(Integer rangeMinRow, Integer rangeMaxRow, Integer rangeMinCol,
//                                                 Integer rangeMaxCol, int pictureMinRow, int pictureMaxRow, int pictureMinCol, int pictureMaxCol,
//                                                 Boolean onlyInternal) {
//        int _rangeMinRow = rangeMinRow == null ? pictureMinRow : rangeMinRow;
//        int _rangeMaxRow = rangeMaxRow == null ? pictureMaxRow : rangeMaxRow;
//        int _rangeMinCol = rangeMinCol == null ? pictureMinCol : rangeMinCol;
//        int _rangeMaxCol = rangeMaxCol == null ? pictureMaxCol : rangeMaxCol;
//
//        if (onlyInternal) {
//            return (_rangeMinRow <= pictureMinRow && _rangeMaxRow >= pictureMaxRow && _rangeMinCol <= pictureMinCol
//                    && _rangeMaxCol >= pictureMaxCol);
//        } else {
//            return ((Math.abs(_rangeMaxRow - _rangeMinRow) + Math.abs(pictureMaxRow - pictureMinRow) >= Math
//                    .abs(_rangeMaxRow + _rangeMinRow - pictureMaxRow - pictureMinRow))
//                    && (Math.abs(_rangeMaxCol - _rangeMinCol) + Math.abs(pictureMaxCol - pictureMinCol) >= Math
//                    .abs(_rangeMaxCol + _rangeMinCol - pictureMaxCol - pictureMinCol)));
//        }
//    }
//}