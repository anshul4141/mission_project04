package in.co.rays.ctl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.RoleModel;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

@WebServlet("/LoginCtl")
public class LoginCtl extends BaseCtl {

	public static final String OP_SIGN_IN = "SignIn";
	public static final String OP_SIGN_UP = "SignUp";

	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("loginctl  validate");

		boolean pass = true;

		String op = request.getParameter("operation");
		if (OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op)) {
			return pass;
		}

		if (DataValidator.isNull(request.getParameter("login"))) {
			System.out.println("loginctl 11");
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;
		} else if (!DataValidator.isEmail(request.getParameter("login"))) {
			System.out.println("loginctl 22");
			request.setAttribute("login", PropertyReader.getValue("error.email", "Login "));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("password"))) {
			System.out.println("loginctl 33");
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		UserBean bean = new UserBean();

		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		UserModel userModel = new UserModel();
		RoleModel roleModel = new RoleModel();
		HttpSession session = request.getSession();

		if (OP_SIGN_IN.equalsIgnoreCase(op)) {

			UserBean bean = (UserBean) populateBean(request);
			try {
				bean = userModel.authenticate(bean.getLogin(), bean.getPassword());
				if (bean != null) {
					session.setAttribute("user", bean);

					RoleBean roleBean = roleModel.findByPk(bean.getRoleId());
					session.setAttribute("role", roleBean.getName());
					ServletUtility.setBean(bean, request);
					ServletUtility.redirect(ORSView.WELCOME_CTL, request, response);
				} else {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Invalid Login ID or Password.", request);
					ServletUtility.forward(getView(), request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, request, response);
		}

	}

	@Override
	protected String getView() {
		return ORSView.LOGIN_VIEW;
	}
}
