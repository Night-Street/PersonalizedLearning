/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author PnP
 */
@WebFilter(filterName = "authFilter", urlPatterns = {"/faces/operation/*"})
public class AuthFilter implements Filter {

    public AuthFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpSession session = ((HttpServletRequest) request).getSession(true);
            HttpServletResponse response1 = (HttpServletResponse) response;
            if (null == session.getAttribute(StaticFields.SESSION_MYUSER)) {
                // System.out.println("=================no user logining===========================");
                response1.sendRedirect(StaticFields.APPNAME);
            }
            chain.doFilter(request, response);
        } catch (IOException | ServletException t) {
            System.out.println(t.getStackTrace());
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
