/* $Id: SnoopServlet.java,v 1.5 2004/02/22 22:57:59 billbarker Exp $
 *
 */
/*   
 *  Copyright 1999-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.snoop;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URI;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.text.StringEscapeUtils;

/**
 *
 *
 * @author James Duncan Davidson 
 * @author Jason Hunter 
 */

public class SnoopServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");

        out.println("Snoop Servlet modified 20200416-1849");
	out.println();
	out.println("Servlet init parameters:");
	Enumeration e = getInitParameterNames();
	while (e.hasMoreElements()) {
	    String key = (String)e.nextElement();
	    String value = getInitParameter(key);
	    out.println("   " + key + " = " + value); 
	}
	out.println();

	out.println("Context init parameters:");
	ServletContext context = getServletContext();
	Enumeration Enumeration = context.getInitParameterNames();
	while (Enumeration.hasMoreElements()) {
	    String key = (String)Enumeration.nextElement();
            Object value = context.getInitParameter(key);
            out.println("   " + key + " = " + value);
	}
	out.println();

	out.println("Context attributes:");
	Enumeration = context.getAttributeNames();
	while (Enumeration.hasMoreElements()) {
	    String key = (String)Enumeration.nextElement();
            Object value = context.getAttribute(key);
            out.println("   " + key + " = " + value);
	}
	out.println();
	
        out.println("Request attributes:");
        e = request.getAttributeNames();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            Object value = request.getAttribute(key);
            out.println("   " + escapeHtml(key) + " = " + value);
        }
        out.println();
        out.println("Servlet Name: " + getServletName());
        out.println("Protocol: " + request.getProtocol());
        out.println("Scheme: " + request.getScheme());
        out.println("Server Name: " + escapeHtml(request.getServerName()));
        out.println("Server Port: " + request.getServerPort());
        out.println("Server Info: " + context.getServerInfo());
        out.println("Remote Addr: " + request.getRemoteAddr());
        out.println("Remote Host: " + request.getRemoteHost());
        out.println("Character Encoding: " + escapeHtml(request.getCharacterEncoding()));
        out.println("Content Length: " + request.getContentLength());
        out.println("Content Type: "+ escapeHtml(request.getContentType()));
        out.println("Locale: "+ escapeHtml(request.getLocale().toString()));
        out.println("Default Response Buffer: "+ response.getBufferSize());
        out.println();
        out.println("Parameter names in this request:");
        e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            String[] values = request.getParameterValues(key);
            out.print("   " + escapeHtml(key) + " = ");
            for(int i = 0; i < values.length; i++) {
                out.print(escapeHtml(values[i]) + " ");
            }
            out.println();
        }
        out.println();
        out.println("Headers in this request:");
        e = request.getHeaderNames();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            String value = request.getHeader(key);
            out.println(escapeHtml("   " + key + ": " + value));
        }
        out.println();  
        out.println("Cookies in this request:");
        Cookie[] cookies = request.getCookies();
        if ( cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                out.println(escapeHtml("   " + cookie.getName() + " = " + cookie.getValue()));
            }
        }
        out.println();

        out.println("Request Is Secure: " + request.isSecure());
        out.println("Auth Type: " + request.getAuthType());
        out.println("HTTP Method: " + request.getMethod());
        out.println("Remote User: " + request.getRemoteUser());
        out.println("Request URI: " + request.getRequestURI());
        out.println("Context Path: " + request.getContextPath());
        out.println("Servlet Path: " + request.getServletPath());
        out.println("Path Info: " + escapeHtml(request.getPathInfo()));
	out.println("Path Trans: " + request.getPathTranslated());
        out.println("Query String: " + escapeHtml(request.getQueryString()));

        out.println();
        HttpSession session = request.getSession();
        out.println("Requested Session Id: " +
                    escapeHtml(request.getRequestedSessionId()));
        out.println("Current Session Id: " + session.getId());
	out.println("Session Created Time: " + session.getCreationTime());
        out.println("Session Last Accessed Time: " +
                    session.getLastAccessedTime());
        out.println("Session Max Inactive Interval Seconds: " +
                    session.getMaxInactiveInterval());
        out.println();
        out.println("Session values: ");
        Enumeration names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            out.println(escapeHtml("   " + name + " = " + session.getAttribute(name)));
        }
        out.println("Java Version: " + System.getProperty("java.version"));
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            out.println("Server IP address : " + ip);
            out.println("Server Hostname : " + hostname);
 
        } catch (UnknownHostException ex) {
            out.println("Could not get host name: " + ex.getMessage());
        }

        out.println("Compiled at: " + getResourceContent("/compiletime.txt", out));
        out.println("Git branch: " + getResourceContent("/gitbranch.txt", out));
        out.println("Git rev: " + getResourceContent("/gitrev.txt", out));
    }

    private String escapeHtml(String s) {
        return StringEscapeUtils.escapeHtml4(s);
    }

    private static String getResourceContent(String filename, PrintWriter out) throws ServletException {
    try {
        URI resourcePathFile = SnoopServlet.class.getResource(filename).toURI();
        String firstLine = Files.readAllLines(Paths.get(resourcePathFile)).get(0);
        return firstLine;
    } catch (Exception e) {
        return "could not read file " + filename +" exception: " + e.getMessage();
    }
}

}
