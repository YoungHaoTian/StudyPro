package com.cdut.studypro.utils;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 21:22
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class RequestResult {
   //状态码（100: 成功; 200: 失败）
    private int code;
    //提示信息
    private String message;
    //用户要返回给浏览器的数据
	private Object data;

	public static RequestResult success() {
        RequestResult result = new RequestResult();
        result.setCode(100);
        result.setMessage("处理成功！");
        return result;
    }

    public static RequestResult failure(String message) {
        RequestResult result = new RequestResult();
        result.setCode(200);
        result.setMessage(message);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
