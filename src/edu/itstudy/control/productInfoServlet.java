package edu.itstudy.control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.itstudy.bean.productbean;
import edu.itstudy.bean.userbean;
import edu.itstudy.services.IUserServices;
import edu.itstudy.services.imlpm.IUserServicesImp;

public class productInfoServlet extends HttpServlet {

	private IUserServices iuserServices = new IUserServicesImp();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");//防止乱码
		String itemId = request.getParameter("uid");//拿到前端的uid
		if(null == itemId ||"".equals(itemId)) {
			System.out.println(itemId);
		}
		//直接对拿到的uid作为函数参数进行查询
		productbean product = iuserServices.getProductById(Integer.parseInt(itemId));
		request.setAttribute("product", product);
		//判断是否存在，同时决定跳转
		String flag = request.getParameter("flag");
		if(null == flag) {
			request.getRequestDispatcher("productInfo.jsp").forward(request, response);
		}else {
			request.getRequestDispatcher("productUpdate.jsp").forward(request, response);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
