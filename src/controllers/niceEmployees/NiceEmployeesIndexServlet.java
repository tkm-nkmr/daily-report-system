package controllers.niceEmployees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.NiceEmployee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class TopPageIndexServlet
 */
@WebServlet("/niceEmployees/index")
public class NiceEmployeesIndexServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public NiceEmployeesIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        List<NiceEmployee> niceEmployees = em.createNamedQuery("getMyAllNiceEmployees", NiceEmployee.class)
                                        .setParameter("report", r)
                                        .setFirstResult(15 * (page - 1))
                                        .setMaxResults(15)
                                        .getResultList();
        long niceEmployees_count = (long)em.createNamedQuery("getMyNiceEmployeesCount", Long.class)
                                        .setParameter("report", r)
                                        .getSingleResult();


        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();


        request.setAttribute("niceEmployees", niceEmployees);
        request.setAttribute("niceEmployees_count", niceEmployees_count);
        request.setAttribute("page", page);


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/niceEmployees/index.jsp");
        rd.forward(request, response);
    }

}