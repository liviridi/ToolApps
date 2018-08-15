package com.liviridi.tools.fileanalyze.main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SrcTestMain {

    public static void main(String[] args) throws IOException {
//        File inF = new File("/Users/yunli/work/test/NGAIA/artf221112/insFindResult.txt");
//        String outPath = "/Users/yunli/work/test/NGAIA/artf221112/uploadTest.txt";
//        uploadCsvFile(inF, outPath);
//        byte[] res = new byte[1024];
//        FileInputStream is = null;
//        try {
//            is = new FileInputStream("src/resources/test.txt");
//            int len = is.read(res);
//            for (int i = 0; i < len; i++) {
//                System.out.print(res[i]);
//            }
//            System.out.println();
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//        }
//        
//        ByteArrayOutputStream bos = null;
//        try {
//            is = new FileInputStream("src/resources/test.txt");
//            bos = new ByteArrayOutputStream();
//            int len = is.read(res);
//            bos.write(res, 0, len);
//            String m = new String(bos.toByteArray(), "MS932");
//            byte[] prtA = m.getBytes();
//            byte[] prtB = m.getBytes("UTF-8");
//            for (int i = 0; i < prtA.length; i++) {
//                System.out.print(prtA[i]);
//            }
//            System.out.println();
//            for (int i = 0; i < prtB.length; i++) {
//                System.out.print(prtB[i]);
//            }
//            System.out.println();
//            System.out.println(new String(prtA));
//            System.out.println(new String(prtB));
//            
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//            if (bos != null) {
//                bos.close();
//            }
//            
//        }
//        
        String path = "C:\\Ver_GAIA1_WorkSpace\\NGN\\NGNOPS\\web\\WEB-INF\\jsp\\aer\\SC_G06.part.aer_pair_search_condition.jsp";
        String[] dds = path.replace("\\", "/").split("/");
        System.out.print(dds[dds.length - 1]);

    }

    /**
     * CSVファィルをアップロードする <p/> upload CSV file
     *
     * @param file    アップロードするファイル
     * @param fileDir CSVファイルパス
     * @throws IOException          exception
     * @throws NgnBusinessException exception
     */
    public static void uploadCsvFile(File file, String fileDir)
            throws IOException {
        if (file == null) {
            return;
        }
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream br = null;
        FileOutputStream fos = null;
        try {
            br = new BufferedInputStream(fis);
            fos = new FileOutputStream(new File(fileDir));
            int len;
            while ((len = br.read()) != -1) {
                fos.write(len);
            }
            fos.flush();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ignore) {
                // ignore exception
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception ignore) {
                // ignore exception
            }
        }
    }
    
}
