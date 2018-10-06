package com.liviridi.tools.wow.skills;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.liviridi.tools.common.exception.UnableToInitializeException;

public class ArrangeMain {

    private static final String DEFAULT_DIR = "E:\\MyDocument\\WoW\\";

    public static void main(String[] args) throws IOException {
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");

        String fileName = "skills.xlsx";
        String sheetName = "shaman skills";
        if (args.length == 2) {
            fileName = args[0];
            sheetName = args[1];
        }

        File inputFile = new File(DEFAULT_DIR + fileName);
        XSSFWorkbook readBook = null;
        SkillArranger skillArr = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inputFile);
            readBook = new XSSFWorkbook(fis);
            skillArr = new SkillArranger(readBook.getSheet(sheetName));
            skillArr.doArrangement();
        } catch (IOException ioe) {
            // file path error
            System.out.println("file path : [" + inputFile.getAbsolutePath() + "] error!!");
        } catch (UnableToInitializeException e) {
            // can not find the sheet
            System.out.println("sheet : [" + sheetName + "] can not be found!!");
        }

        FileOutputStream ous = null;
        try {
            if (skillArr == null)
                return;

            skillArr.doOutput();
            ous = new FileOutputStream(inputFile);
            readBook.write(ous);
        } finally {
            if (ous != null) {
                ous.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (readBook != null) {
                readBook.close();
            }
        }

    }

}
