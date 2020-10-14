package com.woshidaniu.fastxls.core.model;

import java.io.Serializable;

import com.woshidaniu.fastxls.core.Cell;
import com.woshidaniu.fastxls.core.CellStyle;

/**
 * 
 * @className: CellEntity
 * @description: 单元格model实体
 * @author : kangzhidong
 * @date : 下午01:01:12 2014-11-21
 * @modify by:
 * @modify date :
 * @modify description :
 */
@SuppressWarnings("serial")
public class CellModel implements Serializable {

	/**
	 * 单元格是否是标题- true|false
	 */
	private boolean title = false;
	/**
	 * 单元格数据是否必须 true|false
	 */
	private boolean requisite = false;
	/**
	 * 单元格数据是否唯一 true|false
	 */
	private boolean unique = false;
	/**
	 * 单元格是否锁定true|false
	 */
	private boolean locked = false;
	/**
	 * 单元格是否提示单元格true|false
	 */
	private boolean tip = false;
	/**
	 * 单元格描述
	 */
	private String comments = "";
	/**
	 * 单元格内容
	 */
	private Object content = null;
	/**
	 * 单元格使用的公式
	 */
	private String formula = "";
	

    /**
     * the type of horizontal alignment for the cell
     * @param align - the type of alignment
     * @see #ALIGN_GENERAL
     * @see #ALIGN_LEFT
     * @see #ALIGN_CENTER
     * @see #ALIGN_RIGHT
     * @see #ALIGN_FILL
     * @see #ALIGN_JUSTIFY
     * @see #ALIGN_CENTER_SELECTION
     */
	private short alignment = CellStyle.ALIGN_CENTER;
		
	 /**
     *  the type of vertical alignment for the cell
     * @param align the type of alignment
     * @see #VERTICAL_TOP
     * @see #VERTICAL_CENTER
     * @see #VERTICAL_BOTTOM
     * @see #VERTICAL_JUSTIFY
     */
	private short verticalAlignment = CellStyle.VERTICAL_CENTER;
	/**
	 * 单元格的格式： Excel内置格式：
	 * 
	 * <pre>
	 *  0, "General"
	 *  1, "0"
	 * 	2, "0.00"
	 * 	3, "#,##0"
	 * 	4, "#,##0.00"
	 * 	5, "$#,##0_);($#,##0)"
	 * 	6, "$#,##0_);[Red]($#,##0)"
	 * 	7, "$#,##0.00);($#,##0.00)"
	 * 	8, "$#,##0.00_);[Red]($#,##0.00)"
	 * 	9, "0%"
	 * 	0xa, "0.00%"
	 * 	0xb, "0.00E+00"
	 * 	0xc, "# ?/?"
	 * 	0xd, "# ??/??"
	 * 	0xe, "m/d/yy"
	 * 	0xf, "d-mmm-yy"
	 * 	0x10, "d-mmm"
	 * 	0x11, "mmm-yy"
	 * 	0x12, "h:mm AM/PM"
	 * 	0x13, "h:mm:ss AM/PM"
	 * 	0x14, "h:mm"
	 * 	0x15, "h:mm:ss"
	 * 	0x16, "m/d/yy h:mm"
	 * 		
	 * 		
	 * 	// 0x17 - 0x24 reserved for international and undocumented 0x25, "#,##0_);(#,##0)"
	 * 	0x26, "#,##0_);[Red](#,##0)"
	 * 	0x27, "#,##0.00_);(#,##0.00)"
	 * 	0x28, "#,##0.00_);[Red](#,##0.00)"
	 * 	0x29, "_(* #,##0_);_(* (#,##0);_(* \"-\"_);_(@_)"
	 * 	0x2a, "_($* #,##0_);_($* (#,##0);_($* \"-\"_);_(@_)"
	 * 	0x2b, "_(* #,##0.00_);_(* (#,##0.00);_(* \"-\"??_);_(@_)"
	 * 	0x2c, "_($* #,##0.00_);_($* (#,##0.00);_($* \"-\"??_);_(@_)"
	 * 	0x2d, "mm:ss"
	 * 	0x2e, "[h]:mm:ss"
	 * 	0x2f, "mm:ss.0"
	 * 	0x30, "##0.0E+0"
	 * 	0x31, "@" - This is text format.
	 * 	0x31 "text" - Alias for "@"
	 * </pre>
	 * <pre>
	 * 
	 * 自定义格式：如果日期格式：yyyy-MM-dd ;数字格式：##.##
	 * 
	 * </pre>
	 */
	private String format = null;
	/**
	 * 单元格超链接地址
	 */
	private String hyperlink = null;
	/**
	 * 单元格行索引
	 */
	private int rowIndex = 0;
	/**
	 * 单元格列索引
	 */
	private int columnIndex = 0;
	/**
	 * 单元格宽度
	 */
	private int width = 12;
	/**
	 * 数据校验规则
	 */
	private ValidateModel validate;
	
	public CellModel(int rowIndex, int columnIndex) {
		this(rowIndex, columnIndex ,  20 );
	}
	
	public CellModel(int rowIndex, int columnIndex,int width) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.width = width;
	}

	public CellModel(Cell cell,Object content) {
		this.rowIndex = cell.getRowIndex();
		this.columnIndex = cell.getColumnIndex();
		this.content = content;
	}
	
	public CellModel(Cell cell,Object content,boolean isTip) {
		this.rowIndex = cell.getRowIndex();
		this.columnIndex = cell.getColumnIndex();
		this.content = content;
		this.tip = isTip;
	}
	
	public CellModel(int rowIndex, int columnIndex, Object content) {
		this(rowIndex, columnIndex , content ,  20 );
	}
	
	public CellModel(int rowIndex, int columnIndex, Object content,int width) {
		this(rowIndex, columnIndex , content ,null , width );
	}

	public CellModel(int rowIndex, int columnIndex, Object content,String hyperlink) {
		this(rowIndex, columnIndex , content ,hyperlink , 20 );
	}

	public CellModel(int rowIndex, int columnIndex, Object content,String hyperlink,int width) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.content = content;
		this.hyperlink = hyperlink;
		this.width = width;
	}
	
	public CellModel(int rowIndex, int columnIndex, Object content,boolean title) {
		this(rowIndex, columnIndex , content ,title , false );
	}
	
	public CellModel(int rowIndex, int columnIndex, Object content,boolean title,int width) {
		this(rowIndex, columnIndex , content ,title , false , width);
	}

	public CellModel(int rowIndex, int columnIndex, Object content,boolean title,boolean requisite) {
		this(rowIndex, columnIndex , content ,title , requisite , 20);
	}
	
	public CellModel(int rowIndex, int columnIndex, Object content,boolean title,boolean requisite,int width) {
		this(rowIndex, columnIndex , content ,title , requisite , false , width);
	}
	
	public CellModel(int rowIndex, int columnIndex, Object content,boolean title,boolean requisite, boolean unique) {
		this(rowIndex, columnIndex , content ,title , requisite , unique , 20);
	}

	public CellModel(int rowIndex, int columnIndex, Object content,boolean title,boolean requisite, boolean unique,int width) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.content = content;
		this.title = title;
		this.requisite = requisite;
		this.unique = unique;
		this.width = width;
	}

	

	public boolean isTitle() {

		return title;
	}

	public void setTitle(boolean title) {

		this.title = title;
	}

	public boolean isRequisite() {

		return requisite;
	}

	public void setRequisite(boolean requisite) {

		this.requisite = requisite;
	}

	public boolean isUnique() {

		return unique;
	}

	public void setUnique(boolean unique) {

		this.unique = unique;
	}

	public String getComments() {

		return comments;
	}

	public void setComments(String comments) {

		this.comments = comments;
	}

	public Object getContent() {

		return content;
	}

	public void setContent(Object content) {

		this.content = content;
	}

	public String getFormula() {

		return formula;
	}

	public void setFormula(String formula) {

		this.formula = formula;
	}

	public String getHyperlink() {

		return hyperlink;
	}

	public void setHyperlink(String hyperlink) {

		this.hyperlink = hyperlink;
	}

	public int getRowIndex() {

		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {

		this.rowIndex = rowIndex;
	}

	public int getColumnIndex() {

		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {

		this.columnIndex = columnIndex;
	}

	public String getFormat() {

		return format;
	}

	public void setFormat(String format) {

		this.format = format;
	}

	public int getWidth() {
	
		return width;
	}

	public void setWidth(int width) {
	
		this.width = width;
	}

	public ValidateModel getValidate() {
		return validate;
	}

	public void setValidate(ValidateModel validate) {
		this.validate = validate;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public short getAlignment() {
		return alignment;
	}

	public void setAlignment(short alignment) {
		this.alignment = alignment;
	}

	public short getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(short verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	public boolean isTip() {
		return tip;
	}

	public void setTip(boolean tip) {
		this.tip = tip;
	}
	

	
}
