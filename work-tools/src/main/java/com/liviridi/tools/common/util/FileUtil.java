package com.liviridi.tools.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.liviridi.tools.common.constants.SystemConstants;

public class FileUtil {

    /** line breaker : CRLF */
    public static final String LBRK_CRLF = "\r\n";

    /** line breaker : LF */
    public static final String LBRK_LF = "\n";

    /**
     * ファイル内容書き込む
     *
     * @param wrtCtnt
     *            書き込む内容
     * @param path
     *            ファイルパス
     * @throws IOException
     *             ファイル読込エラー
     */
    public static void writeFile(String wrtCtnt, String path) throws IOException {
        File outFile = new File(path);
        FileOutputStream outFs = null;
        try {
            outFs = new FileOutputStream(outFile);
            outFs.write(wrtCtnt.getBytes());
        } finally {
            if (outFs != null) {
                outFs.close();
            }
        }
    }

    /**
     * ファイル内容読込
     *
     * @param file
     *            読み込むファイル
     * @return 読み込んだファイル内容
     * @throws IOException
     *             ファイル読込エラー
     */
    public static String readFileContent(File file, String encode) throws IOException {
        // 読込内容格納
        StringBuffer fileContent = new StringBuffer();
        byte[] buffer = new byte[SystemConstants.READ_BUFFER_LENGTH];
        ByteArrayOutputStream byteOs = null;
        InputStream fileInStr = null;
        try {
            fileInStr = new FileInputStream(file);
            byteOs = new ByteArrayOutputStream();
            int readLen = 0;
            while ((readLen = fileInStr.read(buffer)) != -1) {
                byteOs.write(buffer, 0, readLen);
            }
            fileContent.append(new String(byteOs.toByteArray(), encode));
        } finally {
            if (fileInStr != null) {
                fileInStr.close();
            }
            if (byteOs != null) {
                byteOs.close();
            }
        }
        // DBに登録時"\0"が含まれたらエラーになるため、空文字列に置換
        return fileContent.toString().replace("\0", SystemConstants.STRING_EMPTY);
    }

}
