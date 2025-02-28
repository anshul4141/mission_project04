package in.co.rays.ctl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.RoleModel;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

public class UserCtl extends BaseCtl {

	@Override
	protected void preload(HttpServletRequest request) {
		RoleModel roleModel = new RoleModel();
		try {
			List roleList = roleModel.list();
			request.setAttribute("roleList", roleList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean validate(HttpServletRequest request) {
		boolean isValid = true;

		// Validate First Name
		String firstName = request.getParameter("firstName");
		if (DataValidator.isNull(firstName)) {
			request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
			isValid = false;
		} else if (!DataValidator.isName(firstName)) {
			request.setAttribute("firstName", "Invalid First Name");
			isValid = false;
		}

		// Validate Last Name
		String lastName = request.getParameter("lastName");
		if (DataValidator.isNull(lastName)) {
			request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
			isValid = false;
		} else if (!DataValidator.isName(lastName)) {
			request.setAttribute("lastName", "Invalid Last Name");
			isValid = false;
		}

		// Validate Login
		String login = request.getParameter("login");
		if (DataValidator.isNull(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			isValid = false;
		} else if (!DataValidator.isEmail(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.email", "Login"));
			isValid = false;
		}

		// Validate Password
		String password = request.getParameter("password");
		if (DataValidator.isNull(password)) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			isValid = false;
		} else if (!DataValidator.isPasswordLength(password)) {
			request.setAttribute("password", "Password should be 8 to 12 characters");
			isValid = false;
		} else if (!DataValidator.isPassword(password)) {
			request.setAttribute("password", "Must contain uppercase, lowercase, digit & special character");
			isValid = false;
		}

		// Validate Confirm Password
		String confirmPassword = request.getParameter("confirmPassword");
		if (DataValidator.isNull(confirmPassword)) {
			request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
			isValid = false;
		} else if (!password.equals(confirmPassword)) {
			request.setAttribute("confirmPassword", "Password & Confirm Password must be same");
			isValid = false;
		}

		// Validate Gender
		if (DataValidator.isNull(request.getParameter("gender"))) {
			request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
			isValid = false;
		}

		// Validate Date of Birth
		String dob = request.getParameter("dob");
		if (DataValidator.isNull(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
			isValid = false;
		} else if (!DataValidator.isDate(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
			isValid = false;
		}

		// Validate Mobile No
		String mobileNo = request.getParameter("mobileNo");
		if (DataValidator.isNull(mobileNo)) {
			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
			isValid = false;
		} else if (!DataValidator.isPhoneLength(mobileNo)) {
			request.setAttribute("mobileNo", "Mobile No must have 10 digits");
			isValid = false;
		} else if (!DataValidator.isPhoneNo(mobileNo)) {
			request.setAttribute("mobileNo", "Invalid Mobile No");
			isValid = false;
		}

		return isValid;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		UserBean bean = new UserBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		bean.setLogin(DataUtility.getString(request.getParameter("login")));
		bean.setPassword(DataUtility.getString(request.getParameter("password")));
		bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
		bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setRoleId(DataUtility.getLong(request.getParameter("roleId")));
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
		populateDTO(bean, request);
		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));
		Long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0) {

			UserModel model = new UserModel();

			try {
				UserBean bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		UserModel model = new UserModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(request);
			try {
				model.add(bean);
				ServletUtility.setSuccessMessage("User Added Successfully..!!", request);
				ServletUtility.forward(getView(), request, response);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("login id already exist", request);
				ServletUtility.forward(getView(), request, response);
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(request);
			try {
				if (id > 0) {
					model.update(bean);
				}
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Data is successfully updated", request);
			} catch (ApplicationException e) {
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.USER_CTL, request, response);
			return;
		}

	}

	@Override
	protected String getView() {

		return ORSView.USER_VIEW;
	}

}
