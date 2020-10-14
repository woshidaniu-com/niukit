package com.woshidaniu.fastxls.jexcel;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jxl.BooleanCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.ErrorCell;
import jxl.ErrorFormulaCell;
import jxl.FormulaCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.biff.CellReferenceHelper;
import jxl.biff.formula.FormulaException;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.io.utils.FileUtils;

/**
 * 
 * @className: JXLWorkbookHelper
 * @description: 构建xls文件内容
 * @author : kangzhidong
 * @date : 下午3:26:41 2014-11-21
 * @modify by:
 * @modify date :
 * @modify description :
 */
public abstract class JXLWorkbookReader {

	 /**
     * The following patterns are used in {@link #isADateFormat(int, String)}
     */
    private static final Pattern date_ptrn1 = Pattern.compile("^\\[\\$\\-.*?\\]");
    private static final Pattern date_ptrn2 = Pattern.compile("^\\[[a-zA-Z]+\\]");
    private static final Pattern date_ptrn3 = Pattern.compile("^[yYmMdDhHsS\\-/,. :\"\\\\]+0?[ampAMP/]*$");
    //日期格式对象
 	protected static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
 	protected static Logger LOG = LoggerFactory.getLogger(JXLWorkbookReader.class);
 	
	public static Workbook getWorkbook(ArgumentsModel arguments) throws IOException{
		//优先从模板加载Workbook
		String filePath = arguments.getTemplatePath();
		if(!BlankUtils.isBlank(filePath)&&FileUtils.isExists(filePath)){
			return getWorkbook(filePath);
		}
		return null;
	} 
	
	public static Workbook getWorkbook(String path) throws IOException {
		return getWorkbook(new File(path));
	}
	
	/**
	 * 
	 * @description: 从文件中读取Workbook
	 * @author : kangzhidong
	 * @date 下午5:39:03 2014-11-22 
	 * @param file
	 * @return
	 * @throws IOException
	 * @return  Workbook 返回类型
	 * @throws  
	 */
	public static Workbook getWorkbook(File file) throws IOException {
		try {
			return Workbook.getWorkbook(file);
		} catch (BiffException e) {
			LOG.error(e.getMessage(),e.getCause());
			return null;
		}
	}

	/**
	 * 
	 * @description: 从文件中读取Sheet
	 * @author : kangzhidong
	 * @date 下午5:38:33 2014-11-22 
	 * @param path
	 * @return
	 * @throws IOException
	 * @return  Sheet[] 返回类型
	 * @throws  
	 */
	public static Sheet[] getSheets(String path) throws IOException {
		return getWorkbook(path).getSheets();
	}

	/**
	 * 
	 * @description: 从文件中读取Sheet
	 * @author : kangzhidong
	 * @date 下午5:38:33 2014-11-22 
	 * @param file
	 * @return
	 * @throws IOException
	 * @return  Sheet[] 返回类型
	 * @throws  
	 */
	public static Sheet[] getSheets(File file) throws IOException {
		return getWorkbook(file).getSheets();
	}
	
	public static Sheet[] getSheets(Workbook wb){
		Sheet[] sheets = null;
		if(wb!=null){
			sheets = wb.getSheets();
		}
		return sheets;
	}
	
	public static Sheet getSheet(String filepath, int sheetIndex) throws IOException {
		return getWorkbook(filepath).getSheet(sheetIndex);
	}
	
	public static Sheet getSheet(String filepath, String sheetName) throws IOException {
		return getWorkbook(filepath).getSheet(sheetName);
	}
	
	public static Sheet getSheet(Workbook wb, int sheetIndex) {
		Sheet sheet = null;
		if(wb!=null){
			sheet = wb.getSheet(sheetIndex);
		}
		return sheet;
	}
	
	public static Sheet getSheet(Workbook wb, String sheetName) {
		Sheet sheet = null;
		if(wb!=null){
			sheet = wb.getSheet(sheetName);
		}
		return sheet;
	}
	
	public static List<Cell[]> getRows(Sheet sheet, int fromIndex, int toIndex) {
		fromIndex = fromIndex >= sheet.getRows() ? sheet.getRows() : fromIndex;
		toIndex = toIndex >= sheet.getRows() ? sheet.getRows() : toIndex;
		List<Cell[]> rows = null;
		if (toIndex > fromIndex) {
			rows = new ArrayList<Cell[]>();
			for (int row_index = fromIndex; row_index < toIndex; row_index++) {
				rows.add(sheet.getRow(row_index));
			}
		}
		return rows;
	}
	
	public static Cell[] getRow(String filepath, int sheetIndex, int rowIndex) throws IOException {
		Sheet sheet = getSheet(filepath, sheetIndex);
		return getRow(sheet, rowIndex);
	}

	public static Cell[] getRow(String filepath, String sheetName, int rowIndex) throws IOException {
		Sheet sheet = getSheet(filepath, sheetName);
		return getRow(sheet, rowIndex);
	}
	
	public static Cell[] getRow(Sheet sheet, int rowIndex) {
		Cell[] row = null;
		if(sheet!=null){
			row = sheet.getRow(rowIndex);
		}
		return row;
	}
	
	public static Cell[] getColumn(Sheet sheet, int colIndex) {
		Cell[] column = null;
		if(sheet!=null){
			column = sheet.getColumn(colIndex);
		}
		return column;
	}

	public static List<Cell> getCells(Cell[] row) {
		List<Cell> cells = null;
		if(row!=null){
			cells = new ArrayList<Cell>();
			for (int i = 0; i < row.length; i++) {
				cells.add(row[i]);
			}
		}
		return cells;
	}
	
	public static Cell getCell(Sheet sheet, int rowIndex, int colIndex) {
		Cell[] row = getRow(sheet, rowIndex);
		return getCell(row, colIndex);
	}
	
	public static Cell getCell(Cell[] row, int colIndex) {
		Cell cell = null;
		if(row!=null){
			cell = row[colIndex];
		}
		return cell;
	}

	public static String getCellValue(Cell cell){
		String value = "";
		if (cell == null) {
			value = "";
		} else {
			if (cell.getType() == CellType.BOOLEAN) {
				value = ((BooleanCell) cell).getValue() + "";
			} else if (cell.getType() == CellType.DATE) {
				DateFormat format = ((DateCell) cell).getDateFormat();
				value = format.format(((DateCell) cell).getDate());
			} else if (cell.getType() == CellType.EMPTY) {
				value = "";
			} else if (cell.getType() == CellType.ERROR) {
				value = ((ErrorCell) cell).getContents();
				value = ((ErrorFormulaCell) cell).getErrorCode() + "";
			} else if (cell.getType() == CellType.LABEL) {
				value = ((LabelCell) cell).getString();
			} else if (cell.getType() == CellType.NUMBER) {
				// 数字日期型
				if (isCellDateFormatted(cell)){
					String formatString = cell.getCellFormat().getFormat().getFormatString();
					jxl.write.DateFormat format = new jxl.write.DateFormat(formatString);
					// 日期格式
					value = format.getDateFormat().format(cell.getContents());
				}else {
					value = BigDecimal.valueOf(((NumberCell) cell).getValue()).toPlainString(); // 數字格式, 避免出現科學符號
				}
			} else if (cell.getType() == CellType.NUMBER_FORMULA
					|| cell.getType() == CellType.STRING_FORMULA
					|| cell.getType() == CellType.BOOLEAN_FORMULA
					|| cell.getType() == CellType.DATE_FORMULA
					|| cell.getType() == CellType.FORMULA_ERROR) {
				value = cell.getContents();
				FormulaCell nfc = (FormulaCell) cell;
				StringBuffer sb = new StringBuffer();
				CellReferenceHelper.getCellReference(cell.getColumn(), cell.getRow(),sb);
				try {
					System.out.println(" formula: " + nfc.getFormula() + " ; Formula in "  + sb.toString() +  " ;value:  " + cell.getContents());
				} catch (FormulaException e) {
					LOG.error(e.getMessage(),e.getCause());
				}
			} else {
				value = "";
			}
		}
		return value;
	}
	
	 /**
     *  Check if a cell contains a date
     *  Since dates are stored internally in Excel as double values
     *  we infer it is a date if it is formatted as such.
     *  @see #isADateFormat(int, String)
     *  @see #isInternalDateFormat(int)
     */
    public static boolean isCellDateFormatted(Cell cell) {
        if (cell == null) return false;
        boolean bDate = false;
        double d = ((NumberCell) cell).getValue();
        if ( isValidExcelDate(d) ) {
        	CellFormat cellFormat = cell.getCellFormat();
        	if(cellFormat==null) return false;
        	String formatString =  cellFormat.getFormat().getFormatString();
        	NumberFormat format = new NumberFormat(formatString);
            int i = format.getFormatIndex();
            String f = format.getFormatString();
            bDate = isADateFormat(i, f);
        }
        return bDate;
    }
    
    /**
     * Given a double, checks if it is a valid Excel date.
     *
     * @return true if valid
     * @param  value the double value
     */

    public static boolean isValidExcelDate(double value)
    {
        return (value > -Double.MIN_VALUE);
    }
    
    /**
     * Given a format ID and its format String, will check to see if the
     *  format represents a date format or not.
     * Firstly, it will check to see if the format ID corresponds to an
     *  internal excel date format (eg most US date formats)
     * If not, it will check to see if the format string only contains
     *  date formatting characters (ymd-/), which covers most
     *  non US date formats.
     *
     * @param formatIndex The index of the format, eg from ExtendedFormatRecord.getFormatIndex
     * @param formatString The format string, eg from FormatRecord.getFormatString
     * @see #isInternalDateFormat(int)
     */
    public static boolean isADateFormat(int formatIndex, String formatString) {
        // First up, is this an internal date format?
        if(isInternalDateFormat(formatIndex)) {
            return true;
        }

        // If we didn't get a real string, it can't be
        if(formatString == null || formatString.length() == 0) {
            return false;
        }

        String fs = formatString;
        StringBuilder sb = new StringBuilder(fs.length());
        for (int i = 0; i < fs.length(); i++) {
            char c = fs.charAt(i);
            if (i < fs.length() - 1) {
                char nc = fs.charAt(i + 1);
                if (c == '\\') {
                    switch (nc) {
                        case '-':
                        case ',':
                        case '.':
                        case ' ':
                        case '\\':
                            // skip current '\' and continue to the next char
                            continue;
                    }
                } else if (c == ';' && nc == '@') {
                    i++;
                    // skip ";@" duplets
                    continue;
                }
            }
            sb.append(c);
        }
        fs = sb.toString();
        
        // If it starts with [$-...], then could be a date, but
        //  who knows what that starting bit is all about
        fs = date_ptrn1.matcher(fs).replaceAll("");
        // If it starts with something like [Black] or [Yellow],
        //  then it could be a date
        fs = date_ptrn2.matcher(fs).replaceAll("");
        // You're allowed something like dd/mm/yy;[red]dd/mm/yy
        //  which would place dates before 1900/1904 in red
        // For now, only consider the first one
        if(fs.indexOf(';') > 0 && fs.indexOf(';') < fs.length()-1) {
           fs = fs.substring(0, fs.indexOf(';'));
        }

        // Otherwise, check it's only made up, in any case, of:
        //  y m d h s - \ / , . :
        // optionally followed by AM/PM
        return date_ptrn3.matcher(fs).matches();
    }

    /**
     * Given a format ID this will check whether the format represents
     *  an internal excel date format or not.
     * @see #isADateFormat(int, java.lang.String)
     */
    public static boolean isInternalDateFormat(int format) {
            switch(format) {
                // Internal Date Formats as described on page 427 in
                // Microsoft Excel Dev's Kit...
                case 0x0e:
                case 0x0f:
                case 0x10:
                case 0x11:
                case 0x12:
                case 0x13:
                case 0x14:
                case 0x15:
                case 0x16:
                case 0x2d:
                case 0x2e:
                case 0x2f:
                    return true;
            }
       return false;
    }
}
