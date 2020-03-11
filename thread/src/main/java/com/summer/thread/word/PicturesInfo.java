package com.summer.thread.word;

public class PicturesInfo {

    private int minRow;
    private int maxRow;
    private int minCol;
    private int maxCol;
    private String ext;
    private byte[] pictureData;

    public PicturesInfo(int minRow, int maxRow, int minCol, int maxCol, byte[] pictureData, String ext) {
        this.minRow = minRow;
        this.maxRow = maxRow;
        this.minCol = minCol;
        this.maxCol = maxCol;
        this.ext = ext;
        this.pictureData = pictureData;
    }

    public byte[] getPictureData() {
        return pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
    }

    public int getMinRow() {
        return minRow;
    }

    public void setMinRow(int minRow) {
        this.minRow = minRow;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    public int getMinCol() {
        return minCol;
    }

    public void setMinCol(int minCol) {
        this.minCol = minCol;
    }

    public int getMaxCol() {
        return maxCol;
    }

    public void setMaxCol(int maxCol) {
        this.maxCol = maxCol;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

}