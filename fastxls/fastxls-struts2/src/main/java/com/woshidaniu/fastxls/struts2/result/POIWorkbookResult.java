/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.struts2.result;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.core.model.SheetModel;
import com.woshidaniu.fastxls.core.model.WorkBookModel;
import com.woshidaniu.fastxls.poi.POIWorkbookBuilder;
import com.woshidaniu.fastxls.poi.POIWorkbookExecutor;
import com.woshidaniu.io.utils.DirectoryUtils;

/**
 * 
 * <b>Example:</b>
 * 
 * <pre>
 * <!-- START SNIPPET: example -->
 * &lt;result name="success" type="poiworkbook"&gt;
 *   &lt;param name="contentType"&gt;application/vnd.ms-excel&lt;/param&gt;
 *   &lt;param name="contentCharSet"&gt;UTF-8&lt;/param&gt;
 *   &lt;param name="contentDisposition"&gt;attachment;filename="${fileName}"&lt;/param&gt;
 *   &lt;param name="allowThreadPool"&gt;false&lt;/param&gt;
 *   &lt;param name="inputArgument"&gt;argumentModel&lt;/param&gt;
 *   &lt;param name="inputDataModel"&gt;dataModel&lt;/param&gt;
 *   &lt;param name="inputSheetModel"&gt;sheetModel&lt;/param&gt;
 *   &lt;param name="inputDataList"&gt;dataList&lt;/param&gt;
 * &lt;/result&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 * 
 */
@SuppressWarnings("serial")
public class POIWorkbookResult extends StrutsResultSupport {

	private final static Logger LOG = LoggerFactory.getLogger(POIWorkbookResult.class);
	protected StringBuilder message = new StringBuilder();
	protected String contentType = "application/vnd.ms-excel";
	protected String contentCharSet = "UTF-8";
	protected String contentDisposition = "inline";
	protected boolean allowThreadPool = false;
	
	//用于导出的必需参数
	protected String inputArgument = "argumentModel";
	protected ArgumentsModel argumentModel;
	//用于导出的Workbook数据
	protected String inputDataModel = "dataModel";
	protected WorkBookModel<CellModel> dataModel;
	//用于导出的sheet数据
	protected String inputSheetModel = "sheetModel";
	protected SheetModel<CellModel> sheetModel;
	//用于导出的行数据
	protected String inputDataList = "dataList";
	protected List<RowModel<CellModel>> dataList;

	// CONSTRUCTORS ----------------------------

	public POIWorkbookResult() {
		super();
	}

	public POIWorkbookResult(ArgumentsModel argumentModel) {
		super();
		this.argumentModel = argumentModel;
	}

	// ACCESSORS ----------------------------


	/**
	 * @return Returns the Content-disposition header value.
	 */
	public String getContentDisposition() {
		return contentDisposition;
	}

	/**
	 * @param contentDisposition
	 *            the Content-disposition header value to use.
	 */
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	/**
	 * @return Returns the charset specified by the user
	 */
	public String getContentCharSet() {
		return contentCharSet;
	}

	/**
	 * @param contentCharSet
	 *            the charset to use on the header when sending the stream
	 */
	public void setContentCharSet(String contentCharSet) {
		this.contentCharSet = contentCharSet;
	}

	public boolean isAllowThreadPool() {
		return allowThreadPool;
	}

	public void setAllowThreadPool(boolean allowThreadPool) {
	
		this.allowThreadPool = allowThreadPool;
	}

	public String getInputArgument() {
		return inputArgument;
	}

	public void setInputArgument(String inputArgument) {
		this.inputArgument = inputArgument;
	}

	public String getInputDataModel() {
		return inputDataModel;
	}

	public void setInputDataModel(String inputDataModel) {
		this.inputDataModel = inputDataModel;
	}

	public String getInputSheetModel() {
		return inputSheetModel;
	}

	public void setInputSheetModel(String inputSheetModel) {
		this.inputSheetModel = inputSheetModel;
	}

	public String getInputDataList() {
		return inputDataList;
	}

	public void setInputDataList(String inputDataList) {
		this.inputDataList = inputDataList;
	}
	
	// OTHER METHODS -----------------------


	/**
	 * Executes the result. Writes the given chart as a PNG or JPG to the
	 * servlet output stream.
	 * 
	 * @param invocation
	 *            an encapsulation of the action execution state.
	 * @throws Exception
	 *             if an error occurs when creating or writing the chart to the
	 *             servlet output stream.
	 */
	@SuppressWarnings("unchecked")
	public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {

        // Override any parameters using values on the stack
        resolveParamsFromStack(invocation.getStack(), invocation);
       
        if (argumentModel == null) {
            // Find the ArgumentsModel from the invocation variable stack
        	argumentModel = (ArgumentsModel) invocation.getStack().findValue(conditionalParse(inputArgument, invocation));
        }
        if (argumentModel == null) {
        	message.delete(0, message.length());
        	message.append("Can not find ArgumentsModel with the name [" + argumentModel + "] in the invocation stack. " +"Check the <param name=\"inputArgument\"> tag specified for this action.");
            LOG.error(message.toString());
            throw new IllegalArgumentException(message.toString());
        }
        
        if (dataModel == null) {
            // Find the WorkBookModel<CellModel> from the invocation variable stack
        	dataModel = (WorkBookModel<CellModel>) invocation.getStack().findValue(conditionalParse(inputDataModel, invocation));
        }
        if (sheetModel == null) {
            // Find the SheetModel<CellModel> from the invocation variable stack
        	sheetModel = (SheetModel<CellModel>) invocation.getStack().findValue(conditionalParse(inputSheetModel, invocation));
        }
        if (dataList == null) {
            // Find the List<RowModel<CellModel>> from the invocation variable stack
        	dataList = (List<RowModel<CellModel>>) invocation.getStack().findValue(conditionalParse(inputDataList, invocation));
        }
        if (dataModel == null && sheetModel == null && dataList == null) {
        	message.delete(0, message.length());
        	message.append("Can not find " +"");
            message.append(" WorkBookModel<CellModel> with the name [" + inputDataModel + "]");
            message.append(" or SheetModel<CellModel> with the name [" + inputSheetModel + "] ");
            message.append(" or List<RowModel<CellModel>> with the name [" + inputDataList + "] ");
            message.append(" in the invocation stack.");
            message.append(" Check the <param name=\"inputDataModel\"> or <param name=\"inputSheetModel\"> or <param name=\"inputDataList\"> tag specified for this action.");
            LOG.error(message.toString());
            throw new IllegalArgumentException(message.toString());
        }
        
		// Find the Response in context
        HttpServletResponse response = ServletActionContext.getResponse();
        // Find the Session in context
        HttpSession  session  = ServletActionContext.getRequest().getSession();
        // Set the content type
        response.setContentType(contentType+";charset="+contentCharSet);
        // Set the content-disposition
        if (contentDisposition != null) {
        	response.addHeader("Content-Disposition", conditionalParse(contentDisposition, invocation));
        }
        
        //获得响应输出流
        OutputStream output = response.getOutputStream();
		try {
			if(dataModel!=null){
				if(BlankUtils.isBlank(dataModel.getSuffix())){
					dataModel.setSuffix(argumentModel.getSuffix());
				}
				//构建Workbook
				POIWorkbookBuilder.buildToStream(dataModel, output);
			}else if(sheetModel!=null){
				//构建Workbook
				POIWorkbookBuilder.buildToStream( argumentModel , sheetModel, output);
			}else{
				//判断是否使用线程
				if(isAllowThreadPool()){
			        //find tempdir 
			        File tmpDir = DirectoryUtils.getTargetDir(session , argumentModel.getTempDir()) ;
			        //启用多线程执行
					POIWorkbookExecutor.buildWorkbook(argumentModel,tmpDir,output,dataList);
				}else{ 
					//构建Workbook
					POIWorkbookBuilder.buildToStream(argumentModel, dataList, output);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			invocation.getStack().setValue(conditionalParse(inputArgument, invocation), null);
			argumentModel = null;
			invocation.getStack().setValue(conditionalParse(inputDataModel, invocation), null);
			dataModel = null;
			invocation.getStack().setValue(conditionalParse(inputSheetModel, invocation), null);
			sheetModel = null;
			invocation.getStack().setValue(conditionalParse(inputDataList, invocation), null);
			dataList = null;
		}
		
		if (LOG.isDebugEnabled()) {
            LOG.debug("Streaming type=[" + contentType + "] content-disposition=[" + contentDisposition + "] charset=[" + contentCharSet + "]");
        }
    }
	
	/**
	 * Tries to lookup the parameters on the stack. Will override any existing
	 * parameters
	 * 
	 * @param stack The current value stack
	 */
	protected void resolveParamsFromStack(ValueStack stack, ActionInvocation invocation) {
		//输出数据流需要的参数
		String disposition = stack.findString("contentDisposition");
		if (disposition != null) {
			setContentDisposition(disposition);
		}
		
		if (contentCharSet != null) {
			contentCharSet = conditionalParse(contentCharSet, invocation);
		} else {
			contentCharSet = stack.findString("contentCharSet");
		}
		
		String allowThreadPool = stack.findString("allowThreadPool");
        if (allowThreadPool != null) {
        	setAllowThreadPool(Boolean.getBoolean(allowThreadPool));
        }
		
		String inputArgument = stack.findString("inputArgument");
        if (inputArgument != null) {
            setInputArgument(inputArgument);
        }
        
        String inputDataModel = stack.findString("inputDataModel");
        if (inputDataModel != null) {
            setInputSheetModel(inputDataModel);
        }
          
        String inputSheetModel = stack.findString("inputSheetModel");
        if (inputSheetModel != null) {
            setInputSheetModel(inputArgument);
        }
        
        String inputDataList = stack.findString("inputDataList");
        if (inputDataList != null) {
            setInputDataList(inputDataList);
        }
	        
		
	}

}
