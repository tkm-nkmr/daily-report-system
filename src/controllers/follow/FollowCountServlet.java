package controllers.follow;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class FollowIndexServlet
 */
@WebServlet("/follows_count")
public class FollowCountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowCountServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        em.getTransaction().begin();

        Follow f = new Follow();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        Employee e = r.getEmployee();

        f.setFollow_employee(e);

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        f.setEmployee(login_employee);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        f.setCreated_at(currentTime);
        f.setUpdated_at(currentTime);


        em.persist(f);
        em.getTransaction().commit();
        em.close();


        request.getSession().setAttribute("flush", "フォローしました");

        response.sendRedirect(request.getContextPath() + "/reports/index");
    }

}
