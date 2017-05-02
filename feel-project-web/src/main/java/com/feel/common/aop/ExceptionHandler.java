package com.feel.common.aop;

import com.feel.common.metadata.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExceptionHandler implements HandlerExceptionResolver {
	Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

	/*@Autowired
	private SystemConfigService systemConfigService;*/
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		if(ex.getMessage() != null){
			if (ex.getMessage().contains(ResultCode.NOT_SUPPORT_METHOD.getMsg())) {
				logger.error("Request: " + request.getRequestURL() + " raised an exception {}", ex.getMessage());
				return new ModelAndView(closewindow());
			}  
		} 
		
		logger.error("Request: " + request.getRequestURL() + " raised an exception", ex);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", ex);
		mav.addObject("url", request.getRequestURL());
		mav.setViewName("error");
		return mav;
	}
	
	private String closewindow(){
		return "redirect:" + getWechatUrl() + "/jssdk/closewindow";
	}
	
	/*private String getWechatUrl() {
		return systemConfigService.getWechatUrl();
	}*/

	private String getWechatUrl() {
		return "baidu.com";
	}
	
}
