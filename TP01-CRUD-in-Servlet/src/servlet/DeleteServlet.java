package servlet;

/*
	Ricardo Queiroz
	Giovanne Aquino
*/


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.EmpDAO;
import util.FooterUtil;

@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sid = request.getParameter("id");
        int id = Integer.parseInt(sid);
        
        
        EmpDAO.delete(id);


        response.sendRedirect("ViewServlet");
    }
}
