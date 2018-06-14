package demo.filter;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import demo.model.User;

/**
 * 
 * @Title: KickoutSessionControlFilter.java
 * @Package com.agood.bejavagod.controller.filter
 * @Description: ͬһ�û����½�߳�ǰ����û�
 * Copyright: Copyright (c) 2016
 * Company:Nathan.Lee.Salvatore
 * 
 * @author leechenxiang
 * @date 2016��12��12�� ����7:25:40
 * @version V1.0
 */          
public class kickoutSessionControlFilter extends AccessControlFilter {

    private String kickoutUrl; //�߳��󵽵ĵ�ַ
    private boolean kickoutAfter = false; //�߳�֮ǰ��¼��/֮���¼���û� Ĭ���߳�֮ǰ��¼���û�
    private int maxSession = 1; //ͬһ���ʺ����Ự�� Ĭ��1
    
    private SessionDAO sessionDAO;
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionDAO(SessionDAO sessionDAO) {
    	this.sessionDAO = sessionDAO;
    }
    
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-kickout-session");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //���û�е�¼��ֱ�ӽ���֮�������
            return true;
        }

        Session session = subject.getSession();
//        User user = (User)subject.getPrincipal();
//        String username = user.getUserName();
        String username = (String) subject.getPrincipal();
        Serializable sessionId = session.getId();

        // ͬ������
        Deque<Serializable> deque = cache.get(username);
        if(deque == null) {
            deque = new LinkedList<Serializable>();
            cache.put(username, deque);
        }

        //���������û�д�sessionId�����û�û�б��߳����������
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
        }

        //����������sessionId���������Ự������ʼ����
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if(kickoutAfter) { //����߳�����
                kickoutSessionId = deque.removeFirst();
            } else { //�����߳�ǰ��
                kickoutSessionId = deque.removeLast();
            }
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //���ûỰ��kickout���Ա�ʾ�߳���
                	kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {//ignore exception
            	System.out.println(e);
            }
        }

        //������߳��ˣ�ֱ���˳����ض����߳���ĵ�ַ
        if (session.getAttribute("kickout") != null) {
            //�Ự���߳���
            try {
                subject.logout();
            } catch (Exception e) { //ignore
            }
            saveRequest(request);
            WebUtils.issueRedirect(request, response, kickoutUrl); 
            return false;
//            HttpServletRequest httpRequest = WebUtils.toHttp(request);
//            if (ShiroFilterUtils.isAjax(httpRequest)) {
//                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);  
//                httpServletResponse.sendError(ShiroFilterUtils.HTTP_STATUS_SESSION_EXPIRE);
//                return false;
//            } else {
//                WebUtils.issueRedirect(request, response, kickoutUrl);
//                return false;
//            }
        }

        return true;
    }
}