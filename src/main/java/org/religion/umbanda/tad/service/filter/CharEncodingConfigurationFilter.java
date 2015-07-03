package org.religion.umbanda.tad.service.filter;

import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
public class CharEncodingConfigurationFilter implements Filter {

    private static final String defaultCharset = "iso-8859-1";

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        //final HttpServletResponse response = (HttpServletResponse) res;
        //Charset charset;
        //try {
        //    charset = Charset.forName(defaultCharset);
        //} catch (UnsupportedCharsetException ex) {
        //    ex.printStackTrace();
        //    charset = Charset.defaultCharset();
        //}
        //response.setCharacterEncoding(charset.displayName());
        res.setContentType("application/json; charset=iso-8859-1");
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

}
