package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        Employee follow_employee = r.getEmployee();

        long niceDid_count = (long)em.createNamedQuery("getMyNiceDidCount", Long.class)
                .setParameter("report", r)
                .setParameter("employee", login_employee)
                .getSingleResult();

        long followDid_count = (long)em.createNamedQuery("getFollowCount", Long.class)
                .setParameter("follow_employee", follow_employee)
                .setParameter("employee", login_employee)
                .getSingleResult();

        em.close();

        request.setAttribute("report", r);
        request.setAttribute("niceDid_count", niceDid_count);
        request.setAttribute("followDid_count", followDid_count);
        request.setAttribute("_token", request.getSession().getId());

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
    }

}
