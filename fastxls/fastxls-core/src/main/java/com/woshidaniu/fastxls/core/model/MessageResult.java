package com.woshidaniu.fastxls.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @className: MessageResult
 * @description: 操作记录结果集合
 * @author : kangzhidong
 * @date : 下午6:46:52 2014-11-21
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class MessageResult {
	
	private final String EMPTY_KEY = "emptyResult";
	private final String UNIQUE_KEY = "uniqueResult";
	private final String ERROR_KEY = "errorResult";
	private final String SUCCESS_KEY = "successResult";
	
	private static MessageResult instance = null;
	
	//导入操作异常或者成功信息记录对象
	private Map<String,List<MessageModel>> result = null;
	
	private MessageResult(){
		this.result = Collections.synchronizedMap(new HashMap<String,List<MessageModel>>());
	}
	
	public static MessageResult getInstance(){
		instance= (instance==null)?instance=new MessageResult():instance;
		instance.getResult().clear();
		return instance;
	}
	
	public boolean acceptEmptyCheck(){
		return null==result.get(EMPTY_KEY)||result.get(EMPTY_KEY).isEmpty();
	}
	
	public void addEmptyCheck(String key, Integer value){
		//信息集合
		List<MessageModel> list = result.get(EMPTY_KEY);
		//判断是否添加过空记录的集合
		if(list == null){
			list = new ArrayList<MessageModel>();
			list.add(new MessageModel(key,value));
		}else{
			//查询key对应的一个提示信息
			MessageModel message = null;
			for (MessageModel messageModel : list) {
				if(messageModel.getKey().equalsIgnoreCase(key)){
					message = messageModel;
					break;
				}
			}
			//判断提示信息是否添加过
			if(null==message){
				message = new MessageModel(key, value);
			}else{
				List<Integer> array = message.getRows();
				//判断是否添加过元素
				if(array.indexOf(value)==-1){
					array.add(value);
				}
			}
		}
		//重新放集合到map
		result.put(EMPTY_KEY, list);
	}
	
	public boolean acceptUniqueCheck(){
		return null==result.get(UNIQUE_KEY)||result.get(UNIQUE_KEY).isEmpty();
	}
	
	public void addUniqueCheck(String key, Integer value){
		//信息集合
		List<MessageModel> list = result.get(UNIQUE_KEY);
		//判断是否添加过空记录的集合
		if(list == null){
			list = new ArrayList<MessageModel>();
			list.add(new MessageModel(key,value));
		}else{
			//查询key对应的一个提示信息
			MessageModel message = null;
			for (MessageModel messageModel : list) {
				if(messageModel.getKey().equalsIgnoreCase(key)){
					message = messageModel;
					break;
				}
			}
			//判断提示信息是否添加过
			if(null==message){
				message = new MessageModel(key, value);
			}else{
				List<Integer> array = message.getRows();
				//判断是否添加过元素
				if(array.indexOf(value)==-1){
					array.add(value);
				}
			}
		}
		//重新放集合到map
		result.put(UNIQUE_KEY, list);
	}
	
	public boolean accept(){
		return this.acceptEmptyCheck()&&this.acceptUniqueCheck()&&(null==result.get(ERROR_KEY)||result.get(UNIQUE_KEY).isEmpty());
	}
	
	public void addSuccess(String key, Integer value){
		//信息集合
		List<MessageModel> list = result.get(SUCCESS_KEY);
		//判断是否添加过空记录的集合
		if(list == null){
			list = new ArrayList<MessageModel>();
			list.add(new MessageModel(key,value));
		}else{
			//查询key对应的一个提示信息
			MessageModel message = null;
			for (MessageModel messageModel : list) {
				if(messageModel.getKey().equalsIgnoreCase(key)){
					message = messageModel;
					break;
				}
			}
			//判断提示信息是否添加过
			if(null==message){
				message = new MessageModel(key, value);
			}else{
				List<Integer> array = message.getRows();
				//判断是否添加过元素
				if(array.indexOf(value)==-1){
					array.add(value);
				}
			}
		}
		//重新放集合到map
		result.put(SUCCESS_KEY, list);
	}
	
	public void addError(String key, Integer value){
		//信息集合
		List<MessageModel> list = result.get(ERROR_KEY);
		//判断是否添加过空记录的集合
		if(list == null){
			list = new ArrayList<MessageModel>();
			list.add(new MessageModel(key,value));
		}else{
			//查询key对应的一个提示信息
			MessageModel message = null;
			for (MessageModel messageModel : list) {
				if(messageModel.getKey().equalsIgnoreCase(key)){
					message = messageModel;
					break;
				}
			}
			//判断提示信息是否添加过
			if(null==message){
				message = new MessageModel(key, value);
			}else{
				List<Integer> array = message.getRows();
				//判断是否添加过元素
				if(array.indexOf(value)==-1){
					array.add(value);
				}
			}
		}
		//重新放集合到map
		result.put(ERROR_KEY, list);
	}

	public Map<String, List<MessageModel>> getResult() {
		return result;
	}
	
	
}



