package org.religion.umbanda.tad.service;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

@Component
public class CharEncodingConfigurationService implements Filter {

    private static final String defaultCharset = "iso-8859-1";

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        Charset charset;
        try {
            charset = Charset.forName(defaultCharset);
        } catch (UnsupportedCharsetException ex) {
            charset = Charset.defaultCharset();
        }
        response.setCharacterEncoding(charset.displayName());
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

}
