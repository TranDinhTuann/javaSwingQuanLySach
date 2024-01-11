package it6020002.pg;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProductGroupView
 */
@WebServlet("/productgroup/view")
public class ProductGroupView extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/html, charset=ytf-8";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductGroupView() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// giao diện 
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.setContentType(CONTENT_TYPE);
		
		PrintWriter out = response.getWriter();
		out.append("");
		out.append("");
		out.append("");
		out.append("");
		out.append("");
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// xử lí
		doGet(request, response);
	}

}
