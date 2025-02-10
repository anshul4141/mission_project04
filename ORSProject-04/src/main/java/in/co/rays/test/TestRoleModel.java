package in.co.rays.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.RoleBean;
import in.co.rays.model.RoleModel;

public class TestRoleModel {

	public static void main(String[] args) throws Exception {
//		testAdd();
//		testUpdate();
		testSearch();
	}

	private static void testSearch() throws Exception {

		RoleBean bean = new RoleBean();
		RoleModel model = new RoleModel();

		//bean.setName("admin");

		List list = model.search(bean, 1, 5);

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (RoleBean) it.next();
			System.out.println(bean.getId() + " " + bean.getName());
		}

	}

	private static void testUpdate() throws Exception {

		RoleBean bean = new RoleBean();
		bean.setId(1L);
		bean.setName("student");
		bean.setDescription("admin");
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		RoleModel model = new RoleModel();

		model.update(bean);

	}

	private static void testAdd() throws Exception {

		RoleBean bean = new RoleBean();
		bean.setName("admin");
		bean.setDescription("admin");
		bean.setCreatedBy("admin@gmail.com");
		bean.setModifiedBy("admin@gmail.com");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		RoleModel model = new RoleModel();

		model.add(bean);

	}

}
