package servlet;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by IntelliJ IDEA.
 * User: Niket Arora
 * Date: Dec 7, 2009
 */
public class InitServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(InitServlet.class);

    public void init() throws ServletException {
        try {

        } catch (Exception e) {
            LOGGER.error("Error initializing application ...", e);
        }
    }
}
