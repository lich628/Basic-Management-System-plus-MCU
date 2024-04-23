package com.manage.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class Result {
	private Boolean success;
	private Integer code;
	private String message;
	private Map<String, Object> data = new HashMap<String, Object>();

	//成功静态方法
	public static Result ok() {
		Result r = new Result();
		r.setSuccess(true);
		r.setCode(ResultCode.SUCCESS);
		r.setMessage("成功");
		return r;
	}

	//失败静态方法
	public static Result error() {
		Result r = new Result();
		r.setSuccess(false);
		r.setCode(ResultCode.ERROR);
		r.setMessage("失败");
		return r;
	}

	//警告静态方法
	public static Result warn() {
		Result r = new Result();
		r.setSuccess(false);
		r.setCode(ResultCode.WARN);
		r.setMessage("警告");
		return r;
	}

	public Result suc(Boolean success) {
		this.setSuccess(success);
		return this;
	}

	public Result msg(String message) {
		this.setMessage(message);
		return this;
	}

	public Result code(Integer code) {
		this.setCode(code);
		return this;
	}

	//可以使用键值对或map数据初始化data
	public Result data(String key, Object value) {
		this.data.put(key, value);
		return this;
	}

	public Result data(Map<String, Object> map) {
		this.setData(map);
		return this;
	}
}
