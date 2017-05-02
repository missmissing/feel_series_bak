package com.feel.common.convertion;

import org.springframework.core.convert.converter.Converter;

/**
 * 去掉前后空格
 * @author lx
 *
 */
public class CustomTrimConverter implements Converter<String, String> {

	
	
	public String convert(String source) {
		// TODO Auto-generated method stub
		try {
			//判断不是Null
			if(null != source){
				source = source.trim();
				//判断是否为空串
				if(!"".equals(source)){
					return source;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

}
