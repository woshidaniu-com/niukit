package com.woshidaniu.fastxls.poi.render;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 
 * @author weiguangyue
 * 
 * ExcelPageRender 分页渲染Excel表格
 * 
 * 我们先查询总条数，然后分配这些数据到多个sheet中，每个sheet再执行多次查询
 * 每次查询查询一个page的数据量，也就是perPageRowSize
 * 
 * 这样每个sheet由多个page组成，每个page就是一次查询所获得的数据量,XLS最大行数是65535
 * 我们取整，每个sheet由600个page组成，每个page是100行数据，这样，一个sheet就有60000条数据，基本满足需求
 * 
 */
public class ExcelPageRender<T> {
	
    private static final HeaderRenderHandler NOOP_HEADER_HANDER = new NoopHeaderRenderHandler();
    
    //每次分页查询的行数
    private int perPageRowSize = 100;
    //每个sheet的page个数
    private int perSheetPageCount = 600;
    
    private TotalCountHandler totalCountHandler;
    private PageQueryHandler<T> pageQueryHandler;
    private HeaderRenderHandler headerRenderHandler;
    private DataRenderHandler<T> dataRenderHandler;
    
    public ExcelPageRender(TotalCountHandler totalCountHandler, PageQueryHandler<T> pageQueryHandler, DataRenderHandler<T> dataRenderHandler,HeaderRenderHandler headerRenderHandler, int perPageRowSize, int perSheetPageCount) {
        this.totalCountHandler = totalCountHandler;
        this.pageQueryHandler = pageQueryHandler;
        this.headerRenderHandler = headerRenderHandler;
        this.dataRenderHandler = dataRenderHandler;
        this.perPageRowSize = perPageRowSize;
        this.perSheetPageCount = perSheetPageCount;
    }
    
    public ExcelPageRender(TotalCountHandler totalCountHandler, PageQueryHandler<T> pageQueryHandler, DataRenderHandler<T> dataRenderHandler,HeaderRenderHandler headerRenderHandler) {
        this(totalCountHandler,pageQueryHandler,dataRenderHandler,headerRenderHandler,100,600);
    }
    
    public ExcelPageRender(TotalCountHandler totalCountHandler, PageQueryHandler<T> pageQueryHandler, DataRenderHandler<T> dataRenderHandler) {
        this(totalCountHandler,pageQueryHandler,dataRenderHandler,NOOP_HEADER_HANDER);
    }
    
    /**
     * 渲染到文件
     * @param renderTo
     * @throws Exception
     */
    public void render(File renderTo) throws Exception {
        FileOutputStream fos = null;
        Workbook book = null;
        try {
            fos = new FileOutputStream(renderTo);
            book = new HSSFWorkbook();
            
            //总记录数
            int totalCount = totalCountHandler.totalCount();
            
            //每个sheet的行数
            int perSheetRowSize = perPageRowSize * perSheetPageCount;
            
            //总的sheet个数
            int totalSheetSize = totalCount / perSheetRowSize;
            totalSheetSize = totalCount % perSheetRowSize > 0 ? totalSheetSize + 1 : totalSheetSize;
            
            int pageNo = 0;
            
            end:
            for(int sheetIndex = 1; sheetIndex <= totalSheetSize;sheetIndex++) {
            	
                //这个sheet的将要被使用的row的索引号
                int thisSheetRowIndex = 0;
                
                for(int i = 0;i<perSheetPageCount;i++) {
                	
                    //总第多少个page
                    pageNo ++;
                    
                    //数据库开始行号
                    int startRowInDatabase = (perSheetRowSize * (sheetIndex - 1))/*上一个sheet的总页数构成的总行数*/ + perPageRowSize * i/*这一个sheet的第几个page*/;
                    
                    //数据库结束行号
                    int endRowInDatabase = startRowInDatabase + perPageRowSize;
                    
                    //查询数据
                    List<T> onePageDatas = pageQueryHandler.queryByPage(pageNo, perPageRowSize,startRowInDatabase,endRowInDatabase);
                    
                    if(onePageDatas.size() > 0) {
                        //如果说查询到0条，那么就是最后sheet的最后一页的下一页，必定是没有数据的，此时，我们就完成了所有的查询，提前结束
                        thisSheetRowIndex = renderToSheet(onePageDatas,book,sheetIndex,thisSheetRowIndex);
                    }else {
                        break end;
                    }
                }
            }
            book.write(fos);
        } catch (Exception e) {
            throw new Exception("render excel file :"+renderTo+" error",e);
        } finally {
            if(book != null) {
                try {
                    book.close();
                }catch (Throwable e) {
                    //ignore
                }
            }
            if(fos != null) {
                try {
                    fos.close();
                }catch (Throwable e) {
                    //ignore
                }
            }
        }
    }
    
    private int renderToSheet(List<T> onePageDatas,Workbook book,int sheetIndex,int thisSheetRowIndex) {
        String sheetName = "sheet-" + sheetIndex;
        Sheet sheet = null;
        
        boolean firstRendThisSheet =  thisSheetRowIndex == 0;
        if(firstRendThisSheet) {
            sheet = book.createSheet(sheetName);
        }else {
            sheet = book.getSheet(sheetName);
        }
        
        //表头
        if(this.headerRenderHandler != NOOP_HEADER_HANDER) {
            if(firstRendThisSheet) {
                Row row = sheet.createRow(thisSheetRowIndex);
               
                headerRenderHandler.doRneder(row);
                thisSheetRowIndex ++;
            }
        }
        //数据,逐行填充
        for(T data : onePageDatas) {
            Row dataRow = sheet.createRow(thisSheetRowIndex);
            this.dataRenderHandler.doRender(data,dataRow);
            thisSheetRowIndex ++;
        }
        return thisSheetRowIndex;
    }
    
    /**
     * 总数
     */
    public interface TotalCountHandler{
    	/**
    	 * 总记录数
    	 * @return
    	 */
        int totalCount();
    }
    
    /**
     * 按页查询
     */
    public interface PageQueryHandler<T>{
    	/**
    	 * 查询一页数据
    	 * @param pageNo
    	 * @param pageRowSize
    	 * @param startRow
    	 * @param endRow
    	 * @return
    	 */
        List<T> queryByPage(int pageNo,int pageRowSize,int startRow,int endRow);
    }
    
    /**
     * 表头Row渲染
     */
    public interface HeaderRenderHandler{
    	/**
    	 * 表头Row渲染
    	 * @param headerRow
    	 */
        void doRneder(Row headerRow);
    }
    
    /**
     * 数据Row渲染
     */
    public interface DataRenderHandler<T>{
    	/**
    	 * 数据Row渲染
    	 * @param data
    	 * @param row
    	 */
        void doRender(T data,Row row);
    }
    
    public static final class NoopHeaderRenderHandler implements HeaderRenderHandler{
        public void doRneder(Row headerRow) {
            //nothing to do
        }
    }
}