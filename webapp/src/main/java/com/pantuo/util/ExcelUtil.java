package com.pantuo.util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tliu on 7/13/15.
 */
public class ExcelUtil {

    /**
     * dynamically merge cells based on values in column "baseColumnIndex"
     * @param sheet
     * @param baseColumnIndex
     * @param columnIndex
     */
    public static void dynamicMergeCells(HSSFSheet sheet, int baseColumnIndex, int... columnIndex) {
        List<Region> regions = new ArrayList<Region>();
        Iterator<Row> rowIter = sheet.rowIterator();

        String prevValue = null;
        int prevRowNum = -1;
        while (rowIter.hasNext()) {
            Row r = rowIter.next();
            Cell cell = r.getCell(baseColumnIndex);
            if (cell != null) {
                String currValue = cell.getStringCellValue();
                if (prevValue != null && !prevValue.equals(currValue)) {
                    if (r.getRowNum() - prevRowNum > 1) {
                        for (int index : columnIndex) {
                            regions.add(new Region(prevRowNum, (short)index, r.getRowNum()-1, (short)index));
                        }
                    }
                    prevValue = currValue;
                    prevRowNum = r.getRowNum();
                } else if (prevValue == null) {
                    prevValue = cell.getStringCellValue();
                    prevRowNum = r.getRowNum();
                }
            }
        }

        for (Region region : regions) {
           sheet.addMergedRegion(region);
        }
    }
}
