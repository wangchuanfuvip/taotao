package com.taotao.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.spring.exetend.PropertyConfig;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.pojo.User;

@Service
public class UserService {
	 @Autowired
	    private ApiService apiService;

	    private static final ObjectMapper MAPPER = new ObjectMapper();

	    @PropertyConfig
	    private String SSO_TAOTAO;
	public boolean doRegister(String username, String password, String phone) {
		String url=SSO_TAOTAO+"/user/register";
		//注册需要封装参数
		Map <String,String>map =new HashMap<String,String>();
		map.put("username", username);
		map.put("password", password);
		map.put("phone", phone);
		try {
			/**
			 * {"status":201,"msg":"注册失败!","data":null,"ok":false};
			 * {"status":200,"msg":"OK","data":null,"ok":true}
			 */
			String jsonData = this.apiService.doPost(url, map);
			//转成JsonNode
			 JsonNode jsonNode=  MAPPER.readTree(jsonData);
			 if(jsonNode.get("status").intValue() == 200){
				 return true;
			 }
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	 /**
     * 登录
     * 
     * @param username
     * @param password
     * @return
     */
    public String doLogin(String username, String password) {
        String url = SSO_TAOTAO + "/user/login";
        Map<String, String> params = new HashMap<String, String>();
        params.put("u", username);
        params.put("p", password);
        try {
        	/**
        	 * //{"status":200,"msg":"OK","data":"76dbc1ce560acb1be9fbdd87f3d2985a","ok":true}
        	 * //{"status":200,"msg":"OK","data":"f2d5979bcfd4ea813150aded8b2fcc24","ok":true}
        	 */
            String jsonData = this.apiService.doPost(url, params);
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            if (jsonNode.get("status").intValue() == 200) {
                return jsonNode.get("data").asText();//
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
	 * 会员登出
	 * 
	 * @param request
	 * @return
	
	/**
     * 会员登出
     *
     * @param request
     * @return
     

    @RequestMapping(value = "/soa/member-logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        String medicalBackURL = request.getParameter("redirect_url");
        // String medicalBackURL = "https://www.j1.com/ask";
        try {
            String logoutBackUrl = request.getHeader("Referer");
            // 记录会员行为到日志中
            MemberActionLog log = new MemberActionLog();
            IPSeekerAction seeker = IPSeekerAction.getInstance();
            String currentTime = DateUtils.longToDateAll(System.currentTimeMillis());
            String ip = IPUtil.getIpAddr(request);
            String ipAdress = seeker.getAddress(ip);
            String type = "logout";
            String object = "system";
            Long memberId = (Long) request.getSession()
                    .getAttribute("memberId");
            String url = UrlPrefix.getUrl("j1Web") + "/member-logout.html";
            log.setLogTime(currentTime);
            log.setIpAddress(ip);
            log.setLocation(ipAdress);
            log.setOperationType(type);
            log.setOperationObject(object);
            log.setMemberId(memberId);
            log.setOperationUrl(url);
            memberActionLogService.saveMemberActionLog(log);
            // 删除memcache 里的memberId
            String uuidMdKey = "";
            Cookie[] cookies = request.getCookies();
            if (null != cookies && !"".equals(cookies)) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("uuidMdKey")) {
                        uuidMdKey = cookies[i].getValue();
                    }
                }
                if (uuidMdKey != null || !"".equals(uuidMdKey)
                        || !(uuidMdKey.trim().length() <= 0)) {
                    // 清空uuidMdKeycookie
                    Cookie newCookie = new Cookie("uuidMdKey", null);
                    newCookie.setMaxAge(0);
                    newCookie.setPath(request.getContextPath() + "/");
                    response.addCookie(newCookie);
                    cache.remove(uuidMdKey);
                }
            }

        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        } finally {
            // 删除session
            request.getSession().removeAttribute("memberId");
            request.getSession().removeAttribute("loginName");
            request.getSession().removeAttribute("realName");
            request.getSession().removeAttribute("member");
            request.getSession().removeAttribute("soamember");
            request.getSession().removeAttribute("ISCaibei");
            //方便测试
            request.getSession().removeAttribute("is_tc");
            if (medicalBackURL != null && !"".equals(medicalBackURL)) {
                return "redirect:" + medicalBackURL;
            } else {
                return "redirect:/";
            }
        }

    }
*/

	public User queryUserByTicket(String ticket) {
		 try {
	            String url = SSO_TAOTAO + "/user/query/" + ticket;
	            String jsonData = this.apiService.doGet(url);
	            
	            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, User.class);
	            return (User) taotaoResult.getData();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	
}
