package com.feel.common.config;

import com.feel.common.metadata.Constants;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

public class ResourcePathExposer implements ServletContextAware {
	private ServletContext servletContext;
	private final static int TEST_GA_ACCOUNT = 1;
	private final static int PROD_GA_ACCOUNT = 2;
	private final static int TEST_MTA_ACCOUNT = 1;
	private final static int PROD_MTA_ACCOUNT = 2;

	private String environmentMode;
	private int gaEnableStatus;
	private int mtaEnableStatus;

	/*@Autowired
	private SystemConfig systemConfig;*/

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String getEnvironmentMode() {
		return environmentMode;
	}

	public void setEnvironmentMode(String environmentMode) {
		this.environmentMode = environmentMode;
	}

	public int getGaEnableStatus() {
		return gaEnableStatus;
	}

	public void setGaEnableStatus(int gaEnableStatus) {
		this.gaEnableStatus = gaEnableStatus;
	}

	public int getMtaEnableStatus() {
		return mtaEnableStatus;
	}

	public void setMtaEnableStatus(int mtaEnableStatus) {
		this.mtaEnableStatus = mtaEnableStatus;
	}

	public void init() {
		long now = System.currentTimeMillis();
		getServletContext().setAttribute("cssExtension", generateCssExtension(now));
		getServletContext().setAttribute("jsExtension", generateJsExtension(now));
		getServletContext().setAttribute("resourcePath", generateResourcePath());
		getServletContext().setAttribute("version", "?t=" + now);
		getServletContext().setAttribute("isGaEnable", isGaEnable());
		getServletContext().setAttribute("isMTAEnable", isMTAEnable());
		getServletContext().setAttribute("isSendGAToTestAccount", isSendGAToTestAccount());
		getServletContext().setAttribute("isSendGAToProdAccount", isSendGAToProdAccount());
		getServletContext().setAttribute("isSendMTAToTestAccount", isSendMTAToTestAccount());
		getServletContext().setAttribute("isSendMTAToProdAccount", isSendMTAToProdAccount());
	}

	private String generateResourcePath() {
		// String contextPath = getServletContext().getContextPath();
		String contextPath = "/wechat-static";
        if (isQaEnvironment()) {
			contextPath = "//static.lyancafe.com" + contextPath;
		} else if (isProdEnvironment()) {
			contextPath = "//static.lyancoffee.com" + contextPath;
		}
		String resourcePath = isOnlineEnv() ? "public" : "assets";
		resourcePath = contextPath + "/" + resourcePath;
		return resourcePath;
	}

	private String generateCssExtension(long now) {
		String cssExtension = isOnlineEnv() ? ".min.css" : ".css";
		cssExtension = String.format("%s?v=%s", cssExtension, now);
		return cssExtension;
	}

	private String generateJsExtension(long now) {
		String jsExtension = isOnlineEnv() ? ".min.js" : ".js";
		jsExtension = String.format("%s?v=%s", jsExtension, now);
		return jsExtension;
	}

	private boolean isGaEnable() {
		return isSendGAToTestAccount() || isSendGAToProdAccount();
	}

	private boolean isMTAEnable() {
		return isSendMTAToTestAccount() || isSendMTAToProdAccount();
	}

	private boolean isSendGAToTestAccount() {
		return gaEnableStatus == TEST_GA_ACCOUNT;
	}

	private boolean isSendGAToProdAccount() {
		return gaEnableStatus == PROD_GA_ACCOUNT;
	}

	private boolean isSendMTAToTestAccount() {
		return mtaEnableStatus == TEST_MTA_ACCOUNT;
	}

	private boolean isSendMTAToProdAccount() {
		return mtaEnableStatus == PROD_MTA_ACCOUNT;
	}

	public boolean isProdEnvironment() {
		return environmentMode.equals(Constants.ENVIRONMENT_PROD);
	}

	public boolean isQaEnvironment() {
		return environmentMode.equals(Constants.ENVIRONMENT_QA);
	}

	private boolean isOnlineEnv() {
		return isProdEnvironment() || isQaEnvironment();
	}
}
