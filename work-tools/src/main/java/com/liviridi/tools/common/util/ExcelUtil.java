package com.liviridi.tools.common.util;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.NumberToTextConverter;

/**
 * Excel read utility
 *
 */
public class ExcelUtil {

    /**
     * get the value of the cell with the address in this sheet
     *
     * @param sheet
     *            excel sheet
     * @param cellAddr
     *            cell's excel address(example:AA4)
     * @return value of the cell
     */
    public static String getCellValue(Sheet sheet, CellAddress cellAddr) {
        // empty sheet object
        if (sheet == null) {
            return null;
        }
        // get row object
        Row row = sheet.getRow(cellAddr.getRow());
        // empty row
        if (row == null) {
            return null;
        }
        // get cell object
        Cell cell = row.getCell(cellAddr.getColumn());
        if (cell == null) {
            return null;
        }
        String cellResult = null;
        // get value by cell type
        switch (cell.getCellTypeEnum()) {
        case NUMERIC:
        case FORMULA: {
            if (DateUtil.isCellDateFormatted(cell)) {
                // date
                Date date = cell.getDateCellValue();
                cellResult = date.toString();
            } else {
                // number
                cellResult = NumberToTextConverter.toText(cell.getNumericCellValue());
            }
            break;
        }
        // string
        case STRING:
        case BLANK:
            cellResult = cell.getRichStringCellValue().getString();
            break;
        // boolean
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
     * get the value of the cell with the address in this sheet
     *
     * @param sheet
     *            excel sheet
     * @param cellAddr
     *            cell's excel address(example:AA4)
     * @return value of the cell
     */
    public static String getCellValue(Sheet sheet, String cellAddr) {
        CellAddress address = new CellAddress(cellAddr);
        return getCellValue(sheet, address);
    }

    public static String nextRowStr(Sheet sheet, String cellAddr) {
        CellAddress address = new CellAddress(cellAddr);
        return nextRowStr(sheet, address);
    }

    public static String nextRowStr(Sheet sheet, CellAddress cellAddr) {
        return nextRowAddr(sheet, cellAddr).toString();
    }

    public static CellAddress nextRowAddr(Sheet sheet, String cellAddr) {
        return nextRowAddr(sheet, new CellAddress(cellAddr));
    }

    public static CellAddress nextRowAddr(Sheet sheet, CellAddress cellAddr) {
        int row = cellAddr.getRow();
        return new CellAddress(row + 1, cellAddr.getColumn());
    }

    public static CellAddress nextColAddr(Sheet sheet, String cellAddr) {
        return nextColAddr(sheet, new CellAddress(cellAddr));
    }

    public static CellAddress nextColAddr(Sheet sheet, CellAddress cellAddr) {
        int col = cellAddr.getColumn();
        return new CellAddress(cellAddr.getRow(), col + 1);
    }
}
