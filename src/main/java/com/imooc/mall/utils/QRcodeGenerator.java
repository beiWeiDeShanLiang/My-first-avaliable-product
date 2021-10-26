package com.imooc.mall.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/27日 2:27
 * ----------------------
 * ---------类描述--------
 **/
public class QRcodeGenerator {
    public static void generateQRCodeImage(String text,int width, int height,String filePath) throws WriterException, IOException {
        BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix,"PNG", path);

    }

    public static void main(String[] args) throws IOException, WriterException {
        generateQRCodeImage("李时汇帅的一笔",350,350,"F:\\上传位置中文版\\QRTest1.png");
    }
}
