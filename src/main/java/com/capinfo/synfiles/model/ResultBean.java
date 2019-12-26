package com.capinfo.synfiles.model;

import java.io.Serializable;

public class ResultBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer code;
	private String msg;
	private Object data;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public ResultBean(Integer code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public static ResultBean ok(){
		return new ResultBean(200,"","");
	}
	
	public static ResultBean ok(Object data){
		return new ResultBean(200,"",data);
	}
	
	public static ResultBean build(Integer code,String msg){
		return new ResultBean(code,msg,"");
	}
	
	public static ResultBean fail(){
		return new ResultBean(500,"","");
	}
	public static ResultBean fail(String msg){
		return new ResultBean(500,msg,"");
	}
	public ResultBean() {
		super();
	}
	
}
