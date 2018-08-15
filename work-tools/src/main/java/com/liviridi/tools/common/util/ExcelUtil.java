package com.liviridi.tools.common.util;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.NumberToTextConverter;

import com.liviridi.tools.common.log.CommonLogger;

/**
 * Excelファイル作成、読込用ユーティリティ
 *
 * @author yunli
 * @version 2017-08-25 03:29:40
 */
public class ExcelUtil {

    /** ログ */
    private static CommonLogger logger = CommonLogger.getLogger(ExcelUtil.class);

    /**
     * シート内指定されたセールの値を取得
     *
     * @param sheet
     *            シート
     * @param cellAddr
     *            シート内セールのアドレス
     * @return セールの値
     */
    public static String getCellValue(Sheet sheet, CellAddress cellAddr) {
        // シートが無い場合、null返却
        if (sheet == null) {
            logger.debug("空シートで、nullを返却");
            return null;
        }
        // アドレスに対する行取得
        Row row = sheet.getRow(cellAddr.getRow());
        // 空行の場合、null返却
        if (row == null) {
            logger.debug("「" + cellAddr.getRow() + "」行目は空行で、nullを返却");
            return null;
        }
        // セールを取得
        Cell cell = row.getCell(cellAddr.getColumn());
        // 空セールの場合、null返却
        if (cell == null) {
            logger.debug("セール「" + cellAddr + "」は空セールで、nullを返却");
            return null;
        }
        String cellResult = null;
        // セールのタイプに対し、値を取得
        switch (cell.getCellTypeEnum()) {
            // 数字、Dateの場合
            case NUMERIC:
            case FORMULA: {
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Dateの場合
                    Date date = cell.getDateCellValue();
                    cellResult = date.toString();
                } else {
                    // 数字の場合
                    cellResult = NumberToTextConverter.toText(cell.getNumericCellValue());
                }
                break;
            }
            // 文字列の場合
            case STRING:
            case BLANK:
                cellResult = cell.getRichStringCellValue().getString();
                break;
            // boolの場合
            case BOOLEAN:
                cellResult = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                cellResult = String.valueOf(cell.getErrorCellValue());
                break;
            default:
                break;
        }
        return cellResult;
    }

    /**
     * シート内指定されたセールの値を取得
     *
     * @param sheet
     *            シート
     * @param cellAddr
     *            シート内セールのアドレス(example:AA4)
     * @return セールの値
     */
    public static String getCellValue(Sheet sheet, String cellAddr) {
        CellAddress address = new CellAddress(cellAddr);
        return getCellValue(sheet, address);
    }
}
