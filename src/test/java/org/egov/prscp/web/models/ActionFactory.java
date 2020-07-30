package org.egov.prscp.web.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import org.egov.prscp.web.models.Action;
import org.egov.prscp.web.models.AuditDetails;

public class ActionFactory
 {
	private ActionFactory() {
	}
	public static Action createAction() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("");
		list.add("0123456789");
		return new Action("0123456789", "0123456789", "0123456789", "0123456789", "0123456789", list, new AuditDetails("0123456789", "0123456789", new Long(0L), new Long(0L)));
	}


		public static Action createAction2() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("");
		return new Action("", "", "", "", "", list, new AuditDetails("", "", new Long(-1L), new Long(-1L)));
	}

	public static Action createAction3() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("0123456789");
		return new Action("An??t-1.0.txt", "An??t-1.0.txt", "An??t-1.0.txt", "An??t-1.0.txt", "An??t-1.0.txt", list, new AuditDetails("An??t-1.0.txt", "An??t-1.0.txt", new Long(1L), new Long(1L)));
	}



	public static Action createAction4() {
		LinkedList<String> list = new LinkedList<String>();
		list.add("");
		list.add("0123456789");
		return new Action("0123456789", "0123456789", "0123456789", "0123456789", "", list, new AuditDetails((String) null, (String) null, new Long(Long.MAX_VALUE), new Long(Long.MAX_VALUE)));
	}


	public static Action createAction5() {
		LinkedList<String> list = new LinkedList<String>();
		list.add("");
		return new Action((String) null, (String) null, (String) null, (String) null, (String) null, list, new AuditDetails());
	}


	public static Action createAction6() {
		LinkedList<String> list = new LinkedList<String>();
		list.add("0123456789");
		return new Action("An??t-1.0.txt", "An??t-1.0.txt", "An??t-1.0.txt", "An??t-1.0.txt", "0123456789", list, (AuditDetails) null);
	}

	public static Action createAction7() {
		Vector<String> list = new Vector<String>();
		list.add("");
		return new Action((String) null, (String) null, (String) null, (String) null, "An??t-1.0.txt", list, (AuditDetails) null);
	}

	public static Action createAction8() {
		return new Action();
	}
}