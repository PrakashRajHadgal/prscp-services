package org.egov.prscp.repository.builder;

import java.util.List;

import org.egov.prscp.config.PrScpConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PrQueryBuilder {

	private PrScpConfiguration config;

	@Autowired
	public PrQueryBuilder(PrScpConfiguration config) {
		this.config = config;
	}

	public static final String GET_TENDER_AVAILABLE = "SELECT COUNT(1) FROM public.egpr_tender_notice WHERE module_code=? and tenant_id=? and tender_notice_uuid=?";

	public static final String GET_INVITATION_GUEST = "select module_code,tenant_Id,department_name,department_uuid,user_uuid,event_guest_uuid,event_detail_uuid,guest_name, event_guest_type, guest_email, guest_mobile, COALESCE(sent_flag,false) sent_flag, is_active  from egpr_event_guestlist where tenant_id=? and module_code=? and event_detail_uuid=? and COALESCE(sent_flag,false)=false and created_by=? and is_active=true";
	public static final String GET_INVITATION_GUEST_LIST = "select module_code,tenant_Id,department_name,department_uuid,user_uuid,event_guest_uuid,event_detail_uuid,guest_name, event_guest_type, guest_email, guest_mobile, COALESCE(sent_flag,false) sent_flag, is_active from egpr_event_guestlist where tenant_id=? and module_code=? and event_detail_uuid=? and created_by = case when ?<>'' then ? else created_by end and COALESCE(sent_flag,false) = case when COALESCE(?,false)=false then COALESCE(?,false) else COALESCE(sent_flag,false) end and is_active=true";
	public static final String GET_PRESS_MASTER = "select module_code,press_master_uuid,personnel_name,press_type,publication_name,email,mobile,tenant_id,is_active from egpr_press_master where tenant_id=? and module_code=? and is_active=true and press_master_uuid=(case when ?<>'' then ? else press_master_uuid end) and personnel_name like concat('%',case when ?<>'' then ? else personnel_name end,'%') and publication_name like concat('%',case when ?<>'' then ? else publication_name end,'%') and press_type like concat('%',case when ?<>'' then ? else press_type end,'%') AND TO_DATE(TO_CHAR(TO_TIMESTAMP(created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') >= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(TO_CHAR(TO_TIMESTAMP(created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END order by created_time desc";
	public static final String GET_TEMPLATE = "select a.module_code,a.notification_template_uuid,a.email_content,a.sms_content,a.template_mapped_uuid,a.tenant_id,a.document_attachment::text,a.template_type from egpr_notification_template a where a.tenant_id=? and a.module_code=? and a.template_mapped_uuid=? and a.template_type=?";

	private static final String QUERY = "select * from egpr_event_guestlist";
	public static final String GET_LIBRARY_QUERY = "select library_uuid,document_type,uploaded_document,tenant_id,is_active from egpr_library where event_detail_uuid=(case when ?<>'' then ? else event_detail_uuid end) and tenant_id=? and is_active=true and module_code=?";
	public static final String CHECK_EVEVT_UUID = "select count(1) from egpr_event_detail where event_detail_uuid=? and module_code=? and tenant_id=?";

	public static final String GET_PRESS_NOTE_QUERY = "select note.press_note_uuid,note.press_note_subject,note.note_content,note.press_note_date,note.file_number,note.module_code,note.tenant_id,note.is_active,note.created_by,note.created_time,note.last_modified_by,  \n"
			+ "note.last_modified_time,temp.sms_content,temp.email_content,temp.document_attachment,temp.template_type,presss.PressMasters from egpr_press_note note left join (SELECT max(mapPress.press_note_uuid) as press_note_uuid, \n"
			+ "max(mapPress.tenant_id) as tenant_id, max(mapPress.module_code) as module_code,\n"
			+ "array_to_json(array_agg(json_build_object('pressNoteuuid',mapPress.press_note_uuid,'pressMasterUuid',press.press_master_uuid,'personnelName',press.personnel_name, \n"
			+ "'pressType',press.press_type,'publicationName',press.publication_name,'email',press.email,'mobile',press.mobile,'isActive',press.is_active,  \n"
			+ "'tenantId',press.tenant_id,'moduleCode',press.module_code))) as PressMasters from egpr_map_press_note mapPress inner join egpr_press_master press  \n"
			+ "on mapPress.press_master_uuid=press.press_master_uuid and mapPress.tenant_id = press.tenant_id and mapPress.module_code = press.module_code and mapPress.is_active=true \n"
			+ "group by mapPress.press_note_uuid) as presss on note.press_note_uuid=presss.press_note_uuid and note.tenant_id = presss.tenant_id and note.module_code = presss.module_code \n"
			+ "inner join egpr_notification_template temp on temp.notification_template_uuid=note.notification_template_uuid and  temp.tenant_id=note.tenant_id and note.module_code=temp.module_code  \n"
			+ "where note.tenant_id=? and note.module_code=? and note.press_note_uuid=(case when ?<>'' then ?  else note.press_note_uuid end)\n"
			+ "and note.file_number like concat('%',case when ?<>'' then ? else file_number end,'%')\n"
			+ "and note.press_note_subject like concat('%',case when ?<>'' then ?  else press_note_subject end,'%') \n"
			+ "AND TO_DATE(to_char(note.press_note_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') >= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(to_char(note.press_note_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') END \n"
			+ "AND TO_DATE(to_char(note.press_note_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') <= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(to_char(note.press_note_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') END\n"
			+ "AND TO_DATE(TO_CHAR(TO_TIMESTAMP(note.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') >= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(TO_CHAR(TO_TIMESTAMP(note.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END ORDER BY note.created_time desc";

	
	public static final String GET_PRESS_NOTE_EXIST_QUERY="SELECT count(*) FROM egpr_press_note WHERE press_note_date=TO_TIMESTAMP(?, 'DD/MM/YYYY')::timestamp without time zone  and file_number=? and tenant_id=? and module_code=?";
	public static final String GET_TENDER_NOTICE_EXIST_QUERY="SELECT count(*) FROM egpr_tender_notice WHERE tender_date=?::timestamp without time zone  and file_number=? and tenant_id=? and module_code=?";
	public static final String GET_EVENT_EXIST_QUERY="select count(*) from egpr_event_detail where event_title=? and start_date=TO_TIMESTAMP(?, 'DD/MM/YYYY')::timestamp without time zone and start_time=?::time without time zone and tenant_id=? and module_code=?";
			
	private static final String FIND_ALL_PRESSMASTER = "select * from egpr_press_master";
	private static final String FIND_BYID_PRESSMASTER = "select * from egpr_press_master where press_master_uuid = ?";

	public static final String GET_TENDER_PRESS = "select press.press_master_uuid,press.personnel_name,press.press_type,press.publication_name,press.email,press.mobile,press.is_active,press.tenant_id\n"
			+ "from egpr_map_tender_press mapPress inner join egpr_press_master press on mapPress.press_master_uuid=press.press_master_uuid and mapPress.tenant_id = press.tenant_id and mapPress.module_code = press.module_code\n"
			+ "where mapPress.tenant_id=? and mapPress.module_code=? and mapPress.tender_notice_uuid=? and press.is_active=true and mapPress.is_active=true";

	public static final String GET_PRESSNOTE_PRESS = "select press.press_master_uuid,press.personnel_name,press.press_type,press.publication_name,press.email,press.mobile,press.is_active,press.tenant_id\n"
			+ "from egpr_map_press_note mapPress inner join egpr_press_master press on mapPress.press_master_uuid=press.press_master_uuid and mapPress.tenant_id = press.tenant_id and mapPress.module_code = press.module_code\n"
			+ "where mapPress.tenant_id=? and mapPress.module_code=? and mapPress.press_note_uuid=? and press.is_active=true and mapPress.is_active=true";

	public static final String FIND_TENDER = "SELECT notice.tender_notice_uuid, notice.tender_subject, notice.tender_date, notice.file_number,notice.notification_template_uuid, notice.publication_size, notice.tender_notice_status,\n"
			+ "notice.is_active, notice.tenant_id, notice.note_content, notice.module_code, notice.tender_notice_id, notice.tender_document,notice.created_by, \n"
			+ "presss.PressMasters from egpr_tender_notice notice left join (SELECT max(mapPress.tender_notice_uuid) as tender_notice_uuid, max(mapPress.tenant_id) as tenant_id, max(mapPress.module_code) as module_code, \n"
			+ "array_to_json(array_agg(json_build_object('tenderNoticeUuid',mapPress.tender_notice_uuid,'pressMasterUuid',press.press_master_uuid,'personnelName',press.personnel_name,\n"
			+ "'pressType',press.press_type,'publicationName',press.publication_name,'email',press.email,'mobile',press.mobile,'isActive',press.is_active,\n"
			+ "'tenantId',press.tenant_id,'moduleCode',press.module_code))) as PressMasters\n"
			+ "from egpr_map_tender_press mapPress inner join egpr_press_master press \n"
			+ "on mapPress.press_master_uuid=press.press_master_uuid and mapPress.tenant_id = press.tenant_id and mapPress.module_code = press.module_code\n"
			+ "group by mapPress.tender_notice_uuid) as presss on notice.tender_notice_uuid=presss.tender_notice_uuid and notice.tenant_id = presss.tenant_id and notice.module_code = presss.module_code\n"
			+ "where notice.tenant_id=? and notice.module_code=? and notice.tender_notice_status= (case when  ? <>'' then ? else notice.tender_notice_status end)\n"
			+ "and notice.tender_notice_uuid = (case when  ? <>'' then ? else notice.tender_notice_uuid end)\n"
			+ "and notice.tender_notice_id = (case when  ? <>'' then ? else notice.tender_notice_id end)\n"
			+ "and notice.file_number like concat('%',case when ?<>'' then ? else file_number end,'%')\n"
			+ "and notice.tender_subject like concat('%',case when ?<>'' then ?  else tender_subject end,'%')\n"
			+ "AND TO_DATE(to_char(notice.tender_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') >= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(to_char(notice.tender_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') END\n"
			+ "AND TO_DATE(to_char(notice.tender_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') <= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(to_char(notice.tender_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') END\n"
			+ "AND TO_DATE(TO_CHAR(TO_TIMESTAMP(notice.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') >= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(TO_CHAR(TO_TIMESTAMP(notice.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END ORDER BY notice.created_time desc\n"
			+ "";
	public static final String GET_PRESS_NOTE_UUID_QUERY = "SELECT count(1) from egpr_press_note where tenant_id=? and module_code=? and press_note_uuid=? and is_active='TRUE'";

	public static final String GET_COMMITTEE_DEATILS = "select cm.committee_uuid, COALESCE(cm.committee_name,'') as committee_name, COALESCE(cm.committee_description,'') as committee_description,cm.is_active,cm.created_by, \n"
			+ "to_char(TO_TIMESTAMP(cm.created_time / 1000),'dd-mm-yyyy') created_time, cm.tenant_id,cm.module_code, comMembers.totalCommitteeMember, comMembers.committeeMembers from egpr_committee_detail cm left join\n"
			+ "(select max(cb.committee_uuid) as committee_uuid, max(cb.module_code) as module_code, max(cb.tenant_id) as tenant_id, count(1) as totalCommitteeMember, array_to_json(array_agg(json_build_object('committeeUuid',cb.committee_uuid,'committeeMemberUuid',cb.committee_member_uuid,'departmentName',COALESCE(cb.department_name,''),'departmentUuid',COALESCE(cb.department_uuid,''),'userUuid',COALESCE(cb.user_uuid,''), \n"
			+ "'isActive',cb.is_active, 'tenantId',cb.tenant_id, 'moduleCode',cb.module_code))) as committeeMembers from egpr_committee_member cb where cb.is_active=true group by cb.committee_uuid)\n"
			+ "as comMembers on cm.committee_uuid = comMembers.committee_uuid and cm.module_code=comMembers.module_code and cm.tenant_id=comMembers.tenant_id\n"
			+ "where cm.tenant_id=? and cm.module_code=? and cm.is_active=true and cm.committee_uuid = case when ?<>'' then ? else cm.committee_uuid end and cm.committee_name = case when ?<>'' then ? else cm.committee_name end order by cm.created_time desc\n";

	public static final String Event_QUERY = "SELECT MainTable.event_detail_uuid, event_title, event_location, locality_name,sector, organizer_department_name,\n"
			+ "organizer_user_uuid, organizer_user_name, facebook_url, twitter_url,instagram_url, start_date, start_time, end_date, end_time, event_description,\n"
			+ "event_type, event_image, event_budget, MainTable.committee_uuid,MainTable.is_active, MainTable.created_by, MainTable.created_time, MainTable.last_modified_by, MainTable.last_modified_time,\n"
			+ "event_id, MainTable.tenant_id, MainTable.module_code, event_status, status,MainGuest.InvitedGuest,cm.committee_name\n"
			+ "FROM (SELECT event_detail_uuid, event_title, event_location, locality_name,sector,organizer_department_name,organizer_user_uuid, \n"
			+ "organizer_user_name, facebook_url, twitter_url, instagram_url, start_date, to_char(start_time, 'HH24:MI') as start_time, end_date, \n"
			+ "to_char(end_time, 'HH24:MI') as end_time, event_description, event_type, event_image, event_budget, committee_uuid, event_status, \n"
			+ "is_active, created_by, created_time, last_modified_by, last_modified_time, event_id, tenant_id, module_code,\n"
			+ "CASE WHEN ((TO_TIMESTAMP(to_char(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') between TO_TIMESTAMP(concat(to_char(start_date, 'YYYY-MM-DD'),' ',start_time), 'YYYY-MM-DD HH24:MI:SS') and TO_TIMESTAMP(concat(to_char(end_date, 'YYYY-MM-DD'),' ',end_time), 'YYYY-MM-DD HH24:MI:SS'))) THEN 'ONGOING'\n"
			+ "WHEN ((TO_TIMESTAMP(to_char(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') < TO_TIMESTAMP(concat(to_char(start_date, 'YYYY-MM-DD'),' ',start_time), 'YYYY-MM-DD HH24:MI:SS'))) THEN 'UPCOMING'\n"
			+ "WHEN ((TO_TIMESTAMP(to_char(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') > TO_TIMESTAMP(concat(to_char(end_date, 'YYYY-MM-DD'),' ',end_time), 'YYYY-MM-DD HH24:MI:SS'))) THEN 'EXPIRED'\n"
			+ "END AS status FROM public.egpr_event_detail) AS MainTable LEFT JOIN (SELECT max(gu.event_detail_uuid) as event_detail_uuid, max(gu.tenant_id) as tenant_id, max(gu.module_code) as module_code, \n"
			+ "array_to_json(array_agg(json_build_object('eventGuestUuid',gu.event_guest_uuid,'eventGuestType',gu.event_guest_type,'guestName',gu.guest_name,'guestEmail',gu.guest_email,'guestMobile',gu.guest_mobile,'tenantId',gu.tenant_id,'sentFlag',gu.sent_flag,'moduleCode',gu.module_code))) as InvitedGuest\n"
			+ "from egpr_event_guestlist gu inner join egpr_event_detail ev\n"
			+ "ON ev.event_detail_uuid=gu.event_detail_uuid and ev.tenant_id = gu.tenant_id and ev.module_code = gu.module_code and gu.is_active=true\n"
			+ "GROUP BY gu.event_detail_uuid) as MainGuest ON MainTable.event_detail_uuid=MainGuest.event_detail_uuid AND MainTable.tenant_id=MainGuest.tenant_id AND MainTable.module_code=MainGuest.module_code\n"
			+ "LEFT JOIN public.egpr_committee_detail cm ON COALESCE(MainTable.committee_uuid,'')=COALESCE(cm.committee_uuid,'') and MainTable.tenant_id = cm.tenant_id and MainTable.module_code = cm.module_code and cm.is_active=true\n"
			+ "WHERE MainTable.tenant_id=? AND MainTable.module_code=? AND MainTable.is_active=true\n"
			+ "AND MainTable.status = CASE WHEN ?<>'' THEN ? ELSE MainTable.status END AND MainTable.event_status = CASE WHEN ?<>'' THEN ? ELSE MainTable.event_status END\n"
			+ "AND MainTable.event_title LIKE CONCAT('%',(CASE WHEN ?<>'' THEN ? ELSE MainTable.event_title END),'%')\n"
			+ "AND MainTable.event_id = CASE WHEN ?<>'' THEN ? ELSE MainTable.event_id END AND MainTable.event_detail_uuid = CASE WHEN ?<>'' THEN ? ELSE MainTable.event_detail_uuid END\n"
			+ "AND TO_DATE(to_char(MainTable.start_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') >= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(to_char(MainTable.start_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') END\n"
			+ "AND TO_DATE(to_char(MainTable.start_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') <= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(to_char(MainTable.start_date, 'YYYY-MM-DD'), 'YYYY-MM-DD') END \n"
			+ "AND TO_DATE(TO_CHAR(TO_TIMESTAMP(MainTable.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') >= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(TO_CHAR(TO_TIMESTAMP(MainTable.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END \n"
			+ "ORDER BY MainTable.created_time DESC ";

	private static final String EventById_QUERY = "SELECT event_detail_uuid, locality_name,event_title, module_code,event_location, sector, organizer_department_name, \n"
			+ "       organizer_user_uuid, organizer_user_name, facebook_url, twitter_url, \n"
			+ "       instagram_url, start_date, to_char(start_time, 'HH12:MI AM') start_time, end_date, to_char(end_time, 'HH12:MI AM') end_time, event_description, \n"
			+ "       event_type, event_image, event_budget, committee_uuid, event_status, \n"
			+ "       is_active, created_by, created_time, last_modified_by, last_modified_time, \n"
			+ "       event_id, tenant_id,module_code, CASE\n"
			+ "	  WHEN DATE(start_date) <= DATE(NOW()) AND DATE(end_date) >= DATE(NOW()) THEN 'ONGOING'\n"
			+ "	  WHEN DATE(start_date) > DATE(NOW()) THEN 'UPCOMING'\n"
			+ "	  WHEN DATE(end_date) < DATE(NOW()) THEN 'EXPIRED'\n" + "	 END AS status\n"
			+ "      from public.egpr_event_detail where tenant_id=? and event_status=? and event_detail_uuid=? and module_code=?";

	private static final String getMobileSmsForTenderPress = "select email,mobile,press_master_uuid from egpr_press_master where press_master_uuid in (\n"
			+ "select press_master_uuid from egpr_map_tender_press  where tender_notice_uuid = (select tender_notice_uuid from egpr_tender_notice where tender_notice_id=?))";// PMS-2020-04-20-044504

	public static final String getNotificationTemplateId = "select count(notification_template_uuid) from egpr_tender_notice where module_code=? and  tenant_id=? and tender_notice_uuid=?";// PMS-2020-04-20-044504
	public static final String getBillTenderUuid = "select count(tender_bill_uuid) from egpr_tender_bill where module_code=? and tenant_id=? and tender_notice_uuid=?";// PMS-2020-04-20-044504

	public static final String GET_INVITATION_GUEST_LIST_REMINDER = "select module_code,tenant_Id,department_name,department_uuid,user_uuid,event_guest_uuid,event_detail_uuid,guest_name, event_guest_type, guest_email, guest_mobile, COALESCE(sent_flag,false) sent_flag, is_active from egpr_event_guestlist where tenant_id=? and module_code=? and event_detail_uuid=? and is_active=true";
	public static final String Event_Details_Reminder_QUERY = "\n"
			+ "SELECT event_detail_uuid, event_title, event_location,locality_name, sector, module_code,organizer_department_name, \n"
			+ "       organizer_user_uuid, organizer_user_name, facebook_url, twitter_url, \n"
			+ "       instagram_url, start_date, start_time, end_date, end_time, event_description, \n"
			+ "       event_type, event_image, event_budget, committee_uuid, event_status, \n"
			+ "       is_active, created_by, created_time, last_modified_by, last_modified_time, \n"
			+ "       event_id, tenant_id,module_code,\n" + "       CASE\n"
			+ "	  WHEN DATE(start_date) <= DATE(NOW()) AND DATE(end_date) >= DATE(NOW()) THEN 'ONGOING'\n"
			+ "	  WHEN DATE(start_date) > DATE(NOW()) THEN 'UPCOMING'\n"
			+ "	  WHEN DATE(end_date) < DATE(NOW()) THEN 'EXPIRED'\n" + "	 END AS status\n" + "      \n"
			+ "FROM public.egpr_event_detail \n" + "WHERE DATE(start_date) >= DATE(NOW()) and event_status=?";

	public static String GET_NUMBER_OF_EVENTS_DEPARTMENT_WISE = "select \n" + "	a.tenant_id as tenantId,\n"
			+ "	a.module_code as moduleCode,\n" + "	COALESCE(a.organizer_department_name,'') as departmentName,\n"
			+ "	count(1) totalEvents,\n"
			+ "	count((TO_TIMESTAMP(to_char(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') between TO_TIMESTAMP(concat(to_char(a.start_date, 'YYYY-MM-DD'),' ',a.start_time), 'YYYY-MM-DD HH24:MI:SS') and TO_TIMESTAMP(concat(to_char(a.end_date, 'YYYY-MM-DD'),' ',a.end_time), 'YYYY-MM-DD HH24:MI:SS')) or null) as onGoingEvents, \n"
			+ "	count((TO_TIMESTAMP(to_char(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') < TO_TIMESTAMP(concat(to_char(a.start_date, 'YYYY-MM-DD'),' ',a.start_time), 'YYYY-MM-DD HH24:MI:SS')) or null) as upcommingEvents,\n"
			+ "	count((TO_TIMESTAMP(to_char(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') > TO_TIMESTAMP(concat(to_char(a.end_date, 'YYYY-MM-DD'),' ',a.end_time), 'YYYY-MM-DD HH24:MI:SS') and a.event_status<>'CANCELLED') or null) as closedEvents,\n"
			+ "	count((TO_TIMESTAMP(to_char(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') > TO_TIMESTAMP(concat(to_char(a.end_date, 'YYYY-MM-DD'),' ',a.end_time), 'YYYY-MM-DD HH24:MI:SS') and a.event_status<>'CANCELLED') or null) + count(a.event_status='CANCELLED' or null) as archivedEvnets,\n"
			+ "	max(coalesce(b.uploaded_document,0)) as uploadedDocument\n" + "from egpr_event_detail as a left join\n"
			+ "	(select event_detail_uuid, sum(json_array_length(uploaded_document)) as uploaded_document from egpr_library group by event_detail_uuid) as b\n"
			+ "	on a.event_detail_uuid=b.event_detail_uuid\n"
			+ "where COALESCE(a.organizer_department_name,'') = case when ''<>'' then '' else COALESCE(a.organizer_department_name,'') end\n"
			+ "group by a.tenant_id, a.module_code, COALESCE(a.organizer_department_name,'')";

	public String getExternalUserListQuery(List<Object> preparedStmtList) {
		String builder = QUERY;
		return builder;
	}

	// public String getEventSearchQuery(EventDetail eventDetail) {
	// log.info("EventDetail Get: " + eventDetail.toString());
	// String builder = Event_QUERY;
	// builder = builder.replace("[:tenantId:]", (eventDetail.getTenantId() == null
	// ? "" : eventDetail.getTenantId()));
	// builder = builder.replace("[:moduleCode:]",
	// (eventDetail.getModuleCode() == null ? "" : eventDetail.getModuleCode()));
	// builder = builder.replace("[:eventTitle:]",
	// (eventDetail.getEventTitle() == null ? "" : eventDetail.getEventTitle()));
	// builder = builder.replace("[:startDate:]",
	// (eventDetail.getStartDate() == null ? "" : eventDetail.getStartDate()));
	// builder = builder.replace("[:endDate:]", (eventDetail.getEndDate() == null ?
	// "" : eventDetail.getEndDate()));
	// builder = builder.replace("[:startDate:]", (eventDetail.getEndDate() == null
	// ? "" : eventDetail.getEndDate()));
	// builder = builder.replace("[:status:]",
	// (eventDetail.getEventStatus() == null ? "" : eventDetail.getEventStatus()));
	// builder = builder.replace("[:eventStatus:]", (eventDetail.getStatus() == null
	// ? "" : eventDetail.getStatus()));
	// return builder;
	// }

	public String getEventSearchQueryById(List<Object> preparedStmtList) {
		log.info("preparedStmtList: " + preparedStmtList);
		String builder = EventById_QUERY;
		return builder;
	}

	public String findAllPressmaster(List<Object> preparedStmtList) {
		String builder = FIND_ALL_PRESSMASTER;
		return builder;
	}

	public String findByIDPressmaster(List<Object> preparedStmtList) {
		String builder = FIND_BYID_PRESSMASTER;
		return builder;
	}

}
