package servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.EmpDAO;
import model.Emp;
import util.FooterUtil;



/*
	Ricardo Queiroz
	Giovanne Aquino
*/



@WebServlet("/EditServlet2")
public class Edit2Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String sid = request.getParameter("id");
        int id = Integer.parseInt(sid);
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        Emp e = new Emp();
        e.setId(id);
        e.setName(name);
        e.setPassword(password);
        e.setEmail(email);
        e.setCountry(country);

        int status = EmpDAO.update(e);
        if (status > 0) {
            response.sendRedirect("ViewServlet");
        } else {
            out.println("Sorry! unable to update record");
        }
        FooterUtil.renderFooter(out);

        out.close();
   
    }
}