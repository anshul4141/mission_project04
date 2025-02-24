package in.co.rays.ctl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.UserBean;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.ServletUtility;

// (1) Generic operations all methods
// (2) Generic constants all buttons
// (3) Generic work flow service methods
public abstract class BaseCtl extends HttpServlet {

	public static final String OP_SAVE = "Save";
	public static final String OP_CANCEL = "Cancel";
	public static final String OP_DELETE = "Delete";
	public static final String OP_LIST = "List";
	public static final String OP_SEARCH = "Search";
	public static final String OP_VIEW = "View";
	public static final String OP_NEXT = "Next";
	public static final String OP_PREVIOUS = "Previous";
	public static final String OP_NEW = "New";
	public static final String OP_GO = "Go";
	public static final String OP_BACK = "Back";
	public static final String OP_LOG_OUT = "Logout";
	public static final String OP_RESET = "Reset";
	public static final String OP_UPDATE = "update";

	public static final String MSG_SUCCESS = "success";
	public static final String MSG_ERROR = "error";

	protected boolean validate(HttpServletRequest request) {
		return true;
	}

	protected BaseBean populateBean(HttpServletRequest request) {
		return null;
	}

	// Populates basic bean fields like createdBy, modifiedBy, createdDatetime, and
	// modifiedDatetime.
	protected BaseBean populateDTO(BaseBean dto, HttpServletRequest request) {
		String createdBy = request.getParameter("createdBy");
		String modifiedBy = null;

		// Retrieve the user from the session
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");

		if (userBean == null) {
			createdBy = modifiedBy = "root"; // Default user if not logged in
		} else {
			modifiedBy = userBean.getLogin();

			if (DataValidator.isNull(createdBy) || "null".equalsIgnoreCase(createdBy)) {
				createdBy = modifiedBy; // Set createdBy to current user on initial creation
			}
		}

		dto.setCreatedBy(createdBy);
		dto.setModifiedBy(modifiedBy);

		long createdDatetime = DataUtility.getLong(request.getParameter("createdDatetime"));
		if (createdDatetime > 0) {
			dto.setCreatedDatetime(DataUtility.getTimestamp(createdDatetime));
		} else {
			dto.setCreatedDatetime(DataUtility.getCurrentTimestamp());
		}

		dto.setModifiedDatetime(DataUtility.getCurrentTimestamp());
		return dto;
	}

	protected void preload(HttpServletRequest request) {
	}

	// service method call every user request
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("in service method");
		preload(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		System.out.println("op in service = " + op);

		if (DataValidator.isNotNull(op) && !OP_CANCEL.equalsIgnoreCase(op) && !OP_VIEW.equalsIgnoreCase(op)
				&& !OP_DELETE.equalsIgnoreCase(op) && !OP_RESET.equalsIgnoreCase(op)) {

			if (!validate(request)) {
				BaseBean bean = (BaseBean) populateBean(request);
				ServletUtility.setBean(bean, request);
				ServletUtility.forward(getView(), request, response);
				return;
			}
		}
		super.service(request, response);
	}

	protected abstract String getView();

}
