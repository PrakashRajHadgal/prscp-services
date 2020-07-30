// package org.egov.prscp.web.models;
//
// import java.util.List;
// import org.easymock.EasyMock;
// import org.junit.*;
// import static org.junit.Assert.*;
//
// public class ActionTest {
//
// @Test
// public void testAction_1()
// throws Exception {
//
// Action result = new Action();
//
//
// assertNotNull(result);
// assertEquals("Action(uuid=null, tenantId=null, currentState=null,
// action=null, nextState=null, roles=null, auditDetails=null)",
// result.toString());
// assertEquals(null, result.getAction());
// assertEquals(null, result.getCurrentState());
// assertEquals(null, result.getRoles());
// assertEquals(null, result.getNextState());
// assertEquals(null, result.getUuid());
// assertEquals(null, result.getTenantId());
// assertEquals(null, result.getAuditDetails());
// }
//
//
// @Test
// public void testAction_2()
// throws Exception {
// String uuid = "";
// String tenantId = "";
// String currentState = "";
// String action = "";
// String nextState = "";
// List<String> roles = EasyMock.createMock(List.class);
// AuditDetails auditDetails = new AuditDetails();
//
//
// EasyMock.replay(roles);
//
// Action result = new Action(uuid, tenantId, currentState, action, nextState,
// roles, auditDetails);
//
//
// EasyMock.verify(roles);
// assertNotNull(result);
// assertEquals("Action(uuid=, tenantId=, currentState=, action=, nextState=,
// roles=EasyMock for interface java.util.List,
// auditDetails=org.egov.prscp.web.models.AuditDetails@5513094e)",
// result.toString());
// assertEquals("", result.getAction());
// assertEquals("", result.getCurrentState());
// assertEquals("", result.getNextState());
// assertEquals("", result.getUuid());
// assertEquals("", result.getTenantId());
// }
//
//
// @Test
// public void testAddRolesItem_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "", (List<String>) null, new
// AuditDetails());
// String rolesItem = "";
//
// Action result = fixture.addRolesItem(rolesItem);
//
//
// assertNotNull(result);
// assertEquals("Action(uuid=, tenantId=, currentState=, action=, nextState=,
// roles=[], auditDetails=org.egov.prscp.web.models.AuditDetails@63a944e1)",
// result.toString());
// assertEquals("", result.getAction());
// assertEquals("", result.getCurrentState());
// assertEquals("", result.getNextState());
// assertEquals("", result.getUuid());
// assertEquals("", result.getTenantId());
// }
//
// @Test
// public void testAddRolesItem_2()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// String rolesItem = "";
//
// Action result = fixture.addRolesItem(rolesItem);
//
//
// assertNotNull(result);
// assertEquals("Action(uuid=, tenantId=, currentState=, action=, nextState=,
// roles=EasyMock for interface java.util.List,
// auditDetails=org.egov.prscp.web.models.AuditDetails@3d74b0db)",
// result.toString());
// assertEquals("", result.getAction());
// assertEquals("", result.getCurrentState());
// assertEquals("", result.getNextState());
// assertEquals("", result.getUuid());
// assertEquals("", result.getTenantId());
// }
//
//
// @Test
// public void testBuilder_1()
// throws Exception {
//
// Action.ActionBuilder result = Action.builder();
//
//
// assertNotNull(result);
// assertEquals("Action.ActionBuilder(uuid=null, tenantId=null,
// currentState=null, action=null, nextState=null, roles=null,
// auditDetails=null)", result.toString());
// }
//
//
// @Test
// public void testCanEqual_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// Object other = new Object();
//
// boolean result = fixture.canEqual(other);
//
// assertEquals(false, result);
// }
//
// @Test
// public void testCanEqual_2()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// Object other = new Object();
//
// boolean result = fixture.canEqual(other);
//
// assertEquals(false, result);
// }
// @Test
// public void testEquals_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// Object o = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// boolean result = fixture.equals(o);
//
//
// assertEquals(true, result);
// }
//
// @Test
// public void testEquals_2()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// Object o = new Object();
//
// boolean result = fixture.equals(o);
//
// assertEquals(false, result);
// }
//
// @Test
// public void testEquals_3()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// Object o = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// boolean result = fixture.equals(o);
//
//
// assertEquals(true, result);
// }
//
//
// @Test
// public void testEquals_4()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// Object o = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// boolean result = fixture.equals(o);
//
// assertEquals(true, result);
// }
//
// @Test
// public void testEquals_5()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// Object o = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// boolean result = fixture.equals(o);
//
// assertEquals(true, result);
// }
//
// @Test
// public void testEquals_6()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// Object o = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// boolean result = fixture.equals(o);
//
// assertEquals(true, result);
// }
//
// @Test
// public void testEquals_7()
// throws Exception {
// Action fixture = new Action("", "", "", (String) null, "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// Object o = new Action("", "", "", (String) null, "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// boolean result = fixture.equals(o);
//
//
// assertEquals(true, result);
// }
//
// @Test
// public void testEquals_8()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// Object o = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// boolean result = fixture.equals(o);
//
// assertEquals(true, result);
// }
//
//
// @Test
// public void testGetAction_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// String result = fixture.getAction();
//
// assertEquals("", result);
// }
//
// @Test
// public void testGetAuditDetails_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// AuditDetails result = fixture.getAuditDetails();
//
// assertNotNull(result);
// assertEquals(null, result.getLastModifiedTime());
// assertEquals(null, result.getLastModifiedBy());
// assertEquals(null, result.getCreatedBy());
// assertEquals(null, result.getCreatedTime());
// }
//
//
// @Test
// public void testGetCurrentState_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// String result = fixture.getCurrentState();
//
// assertEquals("", result);
// }
//
// @Test
// public void testGetNextState_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// String result = fixture.getNextState();
//
//
// assertEquals("", result);
// }
//
//
// @Test
// public void testGetRoles_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// List<String> result = fixture.getRoles();
//
//
// assertNotNull(result);
// assertEquals(0, result.size());
// }
//
//
// @Test
// public void testGetTenantId_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// String result = fixture.getTenantId();
//
// assertEquals("", result);
// }
//
//
// @Test
// public void testGetUuid_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// String result = fixture.getUuid();
//
// assertEquals("", result);
// }
//
//
// @Test
// public void testHashCode_1()
// throws Exception {
// Action fixture = new Action("", (String) null, "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// int result = fixture.hashCode();
//
// assertEquals(355062, result);
// }
//
//
// @Test
// public void testHashCode_2()
// throws Exception {
// Action fixture = new Action("", "", (String) null, (String) null, "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
//
// int result = fixture.hashCode();
//
// assertEquals(207959, result);
// }
//
//
// @Test
// public void testSetAction_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// String action = "";
//
// fixture.setAction(action);
//
// }
//
//
// @Test
// public void testSetAuditDetails_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// AuditDetails auditDetails = new AuditDetails();
//
// fixture.setAuditDetails(auditDetails);
//
// }
//
//
// @Test
// public void testSetCurrentState_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// String currentState = "";
//
// fixture.setCurrentState(currentState);
//
// }
//
// @Test
// public void testSetNextState_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// String nextState = "";
//
// fixture.setNextState(nextState);
//
// }
//
// @Test
// public void testSetRoles_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// List<String> roles = EasyMock.createMock(List.class);
//
//
// EasyMock.replay(roles);
//
// fixture.setRoles(roles);
//
//
// EasyMock.verify(roles);
// }
//
//
// @Test
// public void testSetTenantId_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// String tenantId = "";
//
// fixture.setTenantId(tenantId);
//
// }
//
//
// @Test
// public void testSetUuid_1()
// throws Exception {
// Action fixture = new Action("", "", "", "", "",
// EasyMock.createNiceMock(List.class), new AuditDetails());
// String uuid = "";
//
// fixture.setUuid(uuid);
//
// }
//
//
// @Before
// public void setUp()
// throws Exception {
//
// }
//
//
// @After
// public void tearDown()
// throws Exception {
//
// }
//
// public static void main(String[] args) {
// new org.junit.runner.JUnitCore().run(ActionTest.class);
// }
// }