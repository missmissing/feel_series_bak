package com.feel.common.metadata;

/**
 * @Description:TODO 
 * @author: zhouyucheng
 * @date:   2017年1月9日 下午2:30:50
 * @version    
 * Coffee Box 连咖啡
 */
public enum ResultCode {
	
	NOT_SUPPORT_METHOD(1000,"暂不支持该方法"),
	REPEATED_SUBMISSION(3001,"重复提交"),
	USER_INVALID(4001,"用户失效"),
	ORDER_INVALID(4002,"订单失效"),
	ORDER_NOT_EDIT(4003,"订单不可编辑"),
	EXPECTED_TIME_IS_NULL(4004,"送达时间异常请重新选择"),
	EXPECTED_TIME_WARN(4005,"您选择的送达时间已超出当前可选的时间范围或休息约满，请重新选择"),
	ADDRESS_INVALID(4006,"地址格式不满足"),
	ADDRESS_GET_ERROR(4007,"地址获取失败"),
	GOODS_ABNORMAL(4008,"订单商品异常，请稍后再试"),
	ADDRESS_IS_NEW(4009,"该地址为定位地址"),
	GIFTCAFETRACECUSTOMERINVALID(4010,"请客单用户获取异常"),
	BACKURLEXCEPTIONAL(4011,"页面跳转异常"),
	INFOMATION_EXPECTED(5000,"信息异常"),
	GIFTCAFE_INFOMATION_EXPECTED(6000,"请客单状态异常"),
	GIFTPACKSACTIVITY_EXCHANGECODE_INVALID(7000,"兑换码无效"),
	;
	
	private int code;
	private String msg;
	
	ResultCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}
