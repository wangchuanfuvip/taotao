package com.taotao.common.spring.exetend.jackson;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CallbackMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    // 做jsonp的支持的标识，在请求参数中加该参数
    private String callbackName;

    /**
     * 要在taotao-severlet.xml 中配置如下:
     * 
     * <!-- MVC注解驱动 -->
	<mvc:annotation-driven>
		<!-- 采用自定义方案 -->
		<mvc:message-converters>
			<!-- 定义文本转化器 -->
			<bean class="org.springframework.http.converter.StringHttpMessageConverter">
				<constructor-arg index="0" value="UTF-8"/>
			</bean>
			<!-- 定义json转化器，支持json跨域 -->
			<bean class="com.taotao.common.spring.exetend.jackson.CallbackMappingJackson2HttpMessageConverter">
				<!-- 跨域请求中的请求参数名 -->
				<property name="callbackName" value="callback"></property>
			</bean>
		</mvc:message-converters>
	</mvc:annotation-driven>
     */
    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        // 从threadLocal中获取当前的Request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        String callbackParam = request.getParameter(callbackName);
        if (StringUtils.isEmpty(callbackParam)) {
            // 没有找到callback参数，直接返回json数据
            super.writeInternal(object, outputMessage);
        } else {
            JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
            try {
                String result = callbackParam + "(" + super.getObjectMapper().writeValueAsString(object)
                        + ");";
                IOUtils.write(result, outputMessage.getBody(), encoding.getJavaName());
            } catch (JsonProcessingException ex) {
                throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
            }
        }

    }

    public String getCallbackName() {
        return callbackName;
    }

    public void setCallbackName(String callbackName) {
        this.callbackName = callbackName;
    }

}
