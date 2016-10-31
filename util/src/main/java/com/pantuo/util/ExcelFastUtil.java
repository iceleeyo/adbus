package com.pantuo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <b><code>ExcelFastUtil</code></b>
 * <p>
 * 经测试jxl 是最快的  hssf 第二 ,采用对象直接封装模版形式的 最慢，慢好几倍
 * </p>
 * <b>Creation Time:</b> 2015年12月16日 下午8:11:08
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class ExcelFastUtil {
	private static Logger log = LoggerFactory.getLogger(ExcelFastUtil.class);
	private HSSFWorkbook workbook = null;
	private HSSFSheet sheet = null;
	private HSSFRow row = null;
	private HSSFCell cell = null;
	private String headTitles = null;
	private String sheetName = null;
	private int rowNum = 0;
	private String[] head_titles;

	public ExcelFastUtil(String headTitles, String sheetName) {
		this.headTitles = headTitles;
		this.sheetName = sheetName;
		this.head_titles = headTitles.split(",");
		workbook = new HSSFWorkbook();
		createSheet(sheetName);
	}

	/** 
	 * 创建一个workbook sheet 
	 */
	public void createSheet(String sheetName) {
		sheet = workbook.createSheet(sheetName);
	}

	/** 
	 * 创建一行，并把当前行加1  
	 */
	public void createRow() {
		row = sheet.createRow(rowNum++);
	}

	/** 
	 * 创建一个单元格 
	 * @param cellNum 
	 * 单元格所属位置 
	 */
	public void createCell(int cellNum) {
		cell = row.createCell((short) cellNum);
	}

	/** 
	 * 设置一个单元格内容 
	 * @param data 
	 *          单元格内容 
	 * @param cellNum 
	 *          单元格所属位置 
	 */
	public void setCell(String data, int cellNum) {
		createCell(cellNum);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(data);
	}

	private static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public void setDynamicCell(Object data, int cellNum) {
		createCell(cellNum);
		if (data == null) {
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(StringUtils.EMPTY);
		} else {
			boolean isNumber = true;
			if (data instanceof String) {
				if (!isNumeric((String) data)) {
					isNumber = false;
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue((String) data);
				}
			}
			if (data instanceof Number || isNumber) {
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(NumberUtils.toDouble(String.valueOf(data)));
			}
		}

	}

	public void setNumberCell(double data, int cellNum) {
		createCell(cellNum);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(data);
	}

	public HSSFCellStyle getDateCellType() {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));// HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));
		return cellStyle;
	}

	public void setDateCell(Date data, int cellNum, HSSFCellStyle cellStyle) {
		if (data != null) {
			createCell(cellNum);
			cell.setCellValue(data);
			cell.setCellStyle(cellStyle);
		}
	}

	public HSSFCellStyle getFontColor(short color) {
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		org.apache.poi.ss.usermodel.Font font = workbook.createFont();
		// cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  //填充单元格
		//  cellStyle.setFillForegroundColor(HSSFColor.DARK_RED.index);    //填暗红色
		//  font.setFontHeightInPoints((short)24); //字体大小
		// font.setFontName("楷体");
		//font.setBoldweight(Font.BOLDWEIGHT_BOLD); //粗体
		font.setColor(color); //绿字
		cellStyle.setFont(font);
		return cellStyle;

	}

	public void setFontColor(HSSFCellStyle cellStyle) {
		cell.setCellStyle(cellStyle);
		;
	}

	public void createHead() {
		createRow();
		for (int i = 0; i < head_titles.length; i++) {
			setCell(head_titles[i], i);
		}
	}

	public String exportFileByInation(String file_name, String rootPath) {
		return exportFileByInation0(file_name, rootPath, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), true);
	}

	/**
	 * @param file_name
	 * @param rootPath 工程根的目录
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public String exportFileByInation0(String file_name, String rootPath, String uniqName, boolean haveDateSubPath) {
		String exlpath = null;
		try {
			String export = new File("").getAbsolutePath();
			if (StringUtils.isNotBlank(rootPath)) {
				export = rootPath;
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			String xlsPath = "/export/" + (haveDateSubPath ? formatter.format(new Date()) : StringUtils.EMPTY);
			file_name = file_name + uniqName;
			String fileDir = export + xlsPath;
			FileHelper.buildDir(fileDir);
			String excelPath = fileDir + "/" + file_name + ".xls";
			exlpath = xlsPath + "/" + file_name + ".xls";
			log.info(excelPath);
			FileOutputStream fout = new FileOutputStream(excelPath);
			workbook.write(fout);
			fout.flush();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exlpath;
	}

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public HSSFRow getRow() {
		return row;
	}

	public void setRow(HSSFRow row) {
		this.row = row;
	}

	public HSSFCell getCell() {
		return cell;
	}

	public void setCell(HSSFCell cell) {
		this.cell = cell;
	}

	public String getHeadTitles() {
		return headTitles;
	}

	public void setHeadTitles(String headTitles) {
		this.headTitles = headTitles;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public String[] getHead_titles() {
		return head_titles;
	}

	public void setHead_titles(String[] head_titles) {
		this.head_titles = head_titles;
	}

}
