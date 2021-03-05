package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.NiceEmployee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsNiceCountServlet
 */
@WebServlet("/reports/nice_count")
public class ReportsNiceCountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsNiceCountServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        em.getTransaction().begin();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        int nice_count = r.getNice_count() + 1;
        r.setNice_count(nice_count);

        NiceEmployee n = new NiceEmployee();

        n.setReport(r);

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        n.setEmployee(login_employee);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        n.setCreated_at(currentTime);
        n.setUpdated_at(currentTime);


        em.persist(n);
        em.getTransaction().commit();
        em.close();



        request.getSession().setAttribute("flush", "いいねしました");

        response.sendRedirect(request.getContextPath() + "/reports/index");
        }

    }



