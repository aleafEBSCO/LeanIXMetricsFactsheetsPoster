package com.ebsco.leanixmetrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FilterToolsTests {

	//Test to make sure that a node without a child will pass through the filter used upon construction
	@Test
	void noChildrenTest() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("relToChild", relToChild);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter
		FilterTools ft = new FilterTools(send, "domain");
		
		//there should be one factsheet since it has no children
		assertEquals(1, ft.getInitialSize());
	}
	
	//Test to make sure that a node with a child will not pass through the filter used upon construction
	@Test
	void hasChildrenTest() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 1);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("relToChild", relToChild);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		
		//there should be no factsheets since it has children
		assertEquals(0, ft.getInitialSize());
	}
	
	//test to for the default constructor and setting the edge list separately with no children
	@Test
	void noChildrenDefaultTest() {
		//create the edge list with a node with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("relToChild", relToChild);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//add edges to ft
		FilterTools ft = new FilterTools(send, "domain");
		
		assertEquals(1, ft.getInitialSize());
		
	}
	
	//test to for the default constructor and setting the edge list separately and there are children
	@Test
	void hasChildrenDefaultTest() {
		//create the edge list with a node with children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 2);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("relToChild", relToChild);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//add edges to ft
		FilterTools ft = new FilterTools(send, "domain");
		
		assertEquals(0, ft.getInitialSize());
		
	}
	
	//test to make sure that a factsheet without a relation is counted
	@Test
	void noRelation() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make lack of relation
		Map<String, Integer> relITComponentToBusinessCapability = new HashMap<String, Integer>();
		relITComponentToBusinessCapability.put("totalCount", 0);		
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("relITComponentToBusinessCapability", relITComponentToBusinessCapability);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterRelation("Domain");
		
		//there should be a problem factsheet since it has no relation
		assertEquals(1, ft.getCount());
	}
	
	//test to make sure that a factsheet with a relation problem isn't counted
	@Test
	void hasRelation() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make lack of relation
		Map<String, Integer> relITComponentToBusinessCapability = new HashMap<String, Integer>();
		relITComponentToBusinessCapability.put("totalCount", 1);		
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("relITComponentToBusinessCapability", relITComponentToBusinessCapability);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterRelation("Domain");
		
		//there should be a problem factsheet since it has no relation
		assertEquals(0, ft.getCount());
	}
	
	//test to make sure that a factsheet with no accountable is counted
	@Test
	void noAccountablenoResponsible() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make lack of accountable
		Map<String, String> node1 = new HashMap<String, String>();
		node1.put("type", "OBSERVER");
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		List<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
		edges.add(edge1);
		Map<String, Object> subscriptions = new HashMap<String, Object>();
		subscriptions.put("edges", edges);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("subscriptions", subscriptions);
		
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterAccountResponse();
		
		//there should be a problem factsheet since it has no accountable
		assertEquals(1, ft.getCount());
	}
	
	//test to make sure that a factsheet with an accountable isn't counted
	@Test
	void hasAccountableNoResponsible() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make accountable
		Map<String, String> node1 = new HashMap<String, String>();
		node1.put("type", "ACCOUNTABLE");
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		List<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
		edges.add(edge1);
		Map<String, Object> subscriptions = new HashMap<String, Object>();
		subscriptions.put("edges", edges);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("subscriptions", subscriptions);
		
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterAccountResponse();
		
		//there shouldn't be a problem since there's accountable
		assertEquals(0, ft.getCount());
	}

	//test to make sure that a factsheet with no responsible is counted
	@Test
	void hasAccountableHasResponsible() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make responsible and accountable
		Map<String, String> node1 = new HashMap<String, String>();
		node1.put("type", "ACCOUNTABLE");
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		
		Map<String, String> node2 = new HashMap<String, String>();
		node2.put("type", "RESPONSIBLE");
		Map<String, Object> edge2 = new HashMap<String, Object>();
		edge2.put("node", node2);
		
		List<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
		edges.add(edge1);
		edges.add(edge2);
		
		Map<String, Object> subscriptions = new HashMap<String, Object>();
		subscriptions.put("edges", edges);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("subscriptions", subscriptions);
		
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterAccountResponse();
		
		//there shouldn't be a problem factsheet since there's both
		assertEquals(0, ft.getCount());
	}
	
	//test to make sure that a factsheet with a responsible isn't counted
	@Test
	void noAccountHasResponsible() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make responsible
		Map<String, String> node1 = new HashMap<String, String>();
		node1.put("type", "RESPONSIBLE");
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		List<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
		edges.add(edge1);
		Map<String, Object> subscriptions = new HashMap<String, Object>();
		subscriptions.put("edges", edges);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("subscriptions", subscriptions);
		
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterAccountResponse();
		
		//there shouldn't be a problem since there's responsible
		assertEquals(0, ft.getCount());
	}
	
	//test to make sure that a factsheet without a relation is counted
	@Test
	void noBoundedContextOrNoBehavior() {
		//test no bounded context but has behavior
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make lack of bounded context relation
		Map<String, Integer> relITComponentToBoundedContext = new HashMap<String, Integer>();
		relITComponentToBoundedContext.put("totalCount", 0);
		
		//make behavior relation
		Map<String, Integer> relITComponentToBehavior = new HashMap<String, Integer>();
		relITComponentToBehavior.put("totalCount", 1);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("relITComponentToApplication", relITComponentToBoundedContext);
		node.put("relITComponentToInterface", relITComponentToBehavior);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterBoundedContextAndBehavior();
		
		//there shouldn't be a problem factsheet since it has one relation
		assertEquals(0, ft.getCount());
		
		//====================================================================
		//test has bounded context but no behavior
		//make the "factsheet" with no children
		Map<String, Integer> relToChild2 = new HashMap<String, Integer>();
		relToChild2.put("totalCount", 0);
		
		//make bounded context relation
		Map<String, Integer> relITComponentToBoundedContext2 = new HashMap<String, Integer>();
		relITComponentToBoundedContext2.put("totalCount", 1);
		
		//make lack of behavior relation
		Map<String, Integer> relITComponentToBehavior2 = new HashMap<String, Integer>();
		relITComponentToBehavior2.put("totalCount", 0);
		
		Map<String, Object> node2 = new HashMap<String, Object>();
		node2.put("id", "1234");
		node2.put("type", "ITComponent");
		node2.put("relToChild", relToChild2);
		node2.put("relITComponentToApplication", relITComponentToBoundedContext2);
		node2.put("relITComponentToInterface", relITComponentToBehavior2);
		Map<String, Object> edge2 = new HashMap<String, Object>();
		edge2.put("node", node2);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send2 = new ArrayList<Map<String, Object>>();
		send2.add(edge2);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft2 = new FilterTools(send2, "domain");
		ft.filterBoundedContextAndBehavior();
		
		//there shouldn't be a problem factsheet since it has one relation
		assertEquals(0, ft2.getCount());
	}
	
	//test to make sure no bounded context and no behavior is counted
	@Test
	void NoBoundedContextAndNoBehaviorTest() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make lack of bounded context relation
		Map<String, Integer> relITComponentToBoundedContext = new HashMap<String, Integer>();
		relITComponentToBoundedContext.put("totalCount", 0);
		
		//make lack of behavior relation
		Map<String, Integer> relITComponentToBehavior = new HashMap<String, Integer>();
		relITComponentToBehavior.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("relITComponentToApplication", relITComponentToBoundedContext);
		node.put("relITComponentToInterface", relITComponentToBehavior);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterBoundedContextAndBehavior();
		
		//there should be a problem factsheet since it there are no relations
		assertEquals(1, ft.getCount());
	}
	
	//test to make sure No Business Criticality Or No Description is counted
	@Test
	void NoBusinessCriticalityOrNoDescription() {
		//test when has business criticality but no description
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add business criticality/description
		node.put("businessCriticality", "Some text here");
		node.put("businessCriticalityDescription", "");
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterBusinessCriticality();
		
		//there should be a problem factsheet since it there is no description
		assertEquals(1, ft.getCount());
		
		//=============================================================================
		//test for no business criticality but has description
		//make the "factsheet" with no children
		Map<String, Integer> relToChild2 = new HashMap<String, Integer>();
		relToChild2.put("totalCount", 0);
		
		Map<String, Object> node2 = new HashMap<String, Object>();
		node2.put("id", "1234");
		node2.put("type", "ITComponent");
		node2.put("relToChild", relToChild2);
		//add business criticality/description
		node2.put("businessCriticality", null);
		node2.put("businessCriticalityDescription", "Some text here");
		
		Map<String, Object> edge2 = new HashMap<String, Object>();
		edge2.put("node", node2);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send2 = new ArrayList<Map<String, Object>>();
		send2.add(edge2);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft2 = new FilterTools(send2, "domain");
		ft2.filterBusinessCriticality();
		
		//there should be a problem factsheet since it there is no business criticality
		assertEquals(1, ft2.getCount());
			
	}
	
	//make sure that when there are criticality and description, it isn't counted
	@Test
	void hasBusinessCriticalityAndDescription() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add business criticality/description
		node.put("businessCriticality", "Some text here");
		node.put("businessCriticalityDescription", "Some more text here");
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterBusinessCriticality();
		
		//there shouldn't be a problem factsheet
		assertEquals(0, ft.getCount());
	}

	//make sure that when eis is provider and there are no owners, it's counted
	@Test
	void EisProviderNoOwner() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//Add EIS provider
		Map<String, String> factsheet = new HashMap<String, String>();
		factsheet.put("displayName", "EIS");
		Map<String, Object> node1 = new HashMap<String, Object>();
		node1.put("factSheet", factsheet);
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		List<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
		edges.add(edge1);
		Map<String, Object> relation = new HashMap<String, Object>();
		relation.put("edges", edges);
		
		//Add no owner
		Map<String, String> node2 = new HashMap<String, String>();
		node2.put("usageType", "notOwner");
		Map<String, Object> edge2 = new HashMap<String, Object>();
		edge2.put("node", node2);
		List<Map<String, Object>> edges2 = new ArrayList<Map<String, Object>>();
		edges2.add(edge2);
		Map<String, Object> relation2 = new HashMap<String, Object>();
		relation2.put("edges", edges2);
		
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//Add provider info
		node.put("relITComponentToProvider", relation);
		//add owner info
		node.put("relITComponentToUserGroup", relation2);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterEisProviderOwnerPersona();
		
		//there should be a problem factsheet since eis is provider and there's no owner
		assertEquals(1, ft.getCount());
	}
	
	//make sure that when eis is provider and there are owners, it's not counted
	@Test
	void EisProviderAndOwner() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//Add EIS provider
		Map<String, String> factsheet = new HashMap<String, String>();
		factsheet.put("displayName", "EIS");
		Map<String, Object> node1 = new HashMap<String, Object>();
		node1.put("factSheet", factsheet);
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		List<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
		edges.add(edge1);
		Map<String, Object> relation = new HashMap<String, Object>();
		relation.put("edges", edges);
		
		//Add owner
		Map<String, String> node2 = new HashMap<String, String>();
		node2.put("usageType", "owner");
		Map<String, Object> edge2 = new HashMap<String, Object>();
		edge2.put("node", node2);
		List<Map<String, Object>> edges2 = new ArrayList<Map<String, Object>>();
		edges2.add(edge2);
		Map<String, Object> relation2 = new HashMap<String, Object>();
		relation2.put("edges", edges2);
		
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//Add provider info
		node.put("relITComponentToProvider", relation);
		//add owner info
		node.put("relITComponentToUserGroup", relation2);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterEisProviderOwnerPersona();
		
		//there should not be a problem factsheet since eis is provider and there's an owner
		assertEquals(0, ft.getCount());
	}
	
	//make sure that when there's no owner and no eis provider, it isn't counted
	@Test
	void NoEisProviderNoOwner() {
	//make the "factsheet" with no children
	Map<String, Integer> relToChild = new HashMap<String, Integer>();
	relToChild.put("totalCount", 0);
	
	//no EIS provider
	Map<String, String> factsheet = new HashMap<String, String>();
	factsheet.put("displayName", "NotEIS");
	Map<String, Object> node1 = new HashMap<String, Object>();
	node1.put("factSheet", factsheet);
	Map<String, Object> edge1 = new HashMap<String, Object>();
	edge1.put("node", node1);
	List<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
	edges.add(edge1);
	Map<String, Object> relation = new HashMap<String, Object>();
	relation.put("edges", edges);
	
	//Add no owner
	Map<String, String> node2 = new HashMap<String, String>();
	node2.put("usageType", "notOwner");
	Map<String, Object> edge2 = new HashMap<String, Object>();
	edge2.put("node", node2);
	List<Map<String, Object>> edges2 = new ArrayList<Map<String, Object>>();
	edges2.add(edge2);
	Map<String, Object> relation2 = new HashMap<String, Object>();
	relation2.put("edges", edges2);
	
	
	Map<String, Object> node = new HashMap<String, Object>();
	node.put("id", "1234");
	node.put("type", "ITComponent");
	node.put("relToChild", relToChild);
	//Add provider info
	node.put("relITComponentToProvider", relation);
	//add owner info
	node.put("relITComponentToUserGroup", relation2);
	
	Map<String, Object> edge = new HashMap<String, Object>();
	edge.put("node", node);
	
	//add the edge to a list as FilterTools is usually used with multiple edges
	List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
	send.add(edge);
	
	//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
	//in this test case but will in others
	FilterTools ft = new FilterTools(send, "domain");
	ft.filterEisProviderOwnerPersona();
	
	//there shouldn't be a problem factsheet since eis isn't owner
	assertEquals(0, ft.getCount());
	}

	//test to make sure No functional fit Or No Description is counted
	@Test
	void NoFunctionalFitOrNoDescription() {
		//test when has functional fit but no description
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add technical fit/description
		node.put("functionalSuitability", "Some text here");
		node.put("functionalSuitabilityDescription", "");
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterFunctionalFit();
		
		//there should be a problem factsheet since it there is no description
		assertEquals(1, ft.getCount());
		
		//=============================================================================
		//test for no functional fit but has description
		//make the "factsheet" with no children
		Map<String, Integer> relToChild2 = new HashMap<String, Integer>();
		relToChild2.put("totalCount", 0);
		
		Map<String, Object> node2 = new HashMap<String, Object>();
		node2.put("id", "1234");
		node2.put("type", "ITComponent");
		node2.put("relToChild", relToChild2);
		//add functional fit/description
		node2.put("functionalSuitability", null);
		node2.put("functionalSuitabilityDescription", "Some text here");
		
		Map<String, Object> edge2 = new HashMap<String, Object>();
		edge2.put("node", node2);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send2 = new ArrayList<Map<String, Object>>();
		send2.add(edge2);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft2 = new FilterTools(send2, "domain");
		ft2.filterFunctionalFit();
		
		//there should be a problem factsheet since it there is no functional criticality
		assertEquals(1, ft2.getCount());
			
	}
	
	//make sure that when there are functional fit and description, it isn't counted
	@Test
	void hasFunctionalFitAndDescription() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add functional fit/description
		node.put("functionalSuitability", "Some text here");
		node.put("functionalSuitabilityDescription", "Some more text here");
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterFunctionalFit();
		
		//there shouldn't be a problem factsheet
		assertEquals(0, ft.getCount());
	}

	//test to make sure No technical fit Or No Description is counted
	@Test
	void noTechnicalFitOrNoDescription() {
		//test when has functional fit but no description
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add technical fit/description
		node.put("technicalSuitability", "Some text here");
		node.put("technicalSuitabilityDescription", "");
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterTechnicalFit();
		
		//there should be a problem factsheet since it there is no description
		assertEquals(1, ft.getCount());
		
		//=============================================================================
		//test for no technical fit but has description
		//make the "factsheet" with no children
		Map<String, Integer> relToChild2 = new HashMap<String, Integer>();
		relToChild2.put("totalCount", 0);
		
		Map<String, Object> node2 = new HashMap<String, Object>();
		node2.put("id", "1234");
		node2.put("type", "ITComponent");
		node2.put("relToChild", relToChild2);
		//add technical fit/description
		node2.put("technicalSuitability", null);
		node2.put("technicalSuitabilityDescription", "Some text here");
		
		Map<String, Object> edge2 = new HashMap<String, Object>();
		edge2.put("node", node2);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send2 = new ArrayList<Map<String, Object>>();
		send2.add(edge2);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft2 = new FilterTools(send2, "domain");
		ft2.filterTechnicalFit();
		
		//there should be a problem factsheet since it there is no technical fit
		assertEquals(1, ft2.getCount());
			
	}
	
	//make sure that when there are functional fit and description, it isn't counted
	@Test
	void hasTechnicalFitAndDescription() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add technical fit/description
		node.put("technicalSuitability", "Some text here");
		node.put("technicalSuitabilityDescription", "Some more text here");
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterTechnicalFit();
		
		//there shouldn't be a problem factsheet
		assertEquals(0, ft.getCount());
	}

	//make sure that when the quality seal is broken, it's counted
	@Test
	void brokenQualitySeal() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//quality seal
		node.put("qualitySeal", "BROKEN");
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterQualitySeal();
		
		//there should be a problem factsheet
		assertEquals(1, ft.getCount());
	}
	
	//make sure that when the quality seal isn't broken, it's not counted
	@Test
	void approvedQualitySeal() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//quality seal
		node.put("qualitySeal", "APPROVED");
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterQualitySeal();
		
		//there shouldn't be a problem factsheet
		assertEquals(0, ft.getCount());
	}
	
	//make sure that when the there's no model status, it's counted
	@Test
	void noModelStatus() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//create the tag group
		Map<String, String> tagGroup = new HashMap<String, String>();
		tagGroup.put("name", "Not State of Model Completeness");
		
		Map<String, Object> tagItem = new HashMap<String, Object>();
		tagItem.put("tagGroup", tagGroup);
		tagItem.put("name", "ready");
		
		List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
		tags.add(tagItem);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add tags
		node.put("tags", tags);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterModelStatus();
		
		//there should be a problem. Even though the tag is ready, there's no status
		assertEquals(1, ft.getCount());
	}
	
	//make sure that when the the model status isn't ready, it's counted
	@Test
	void notReadyModelStatus() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//create the tag group
		Map<String, String> tagGroup = new HashMap<String, String>();
		tagGroup.put("name", "State of Model Completeness");
		
		Map<String, Object> tagItem = new HashMap<String, Object>();
		tagItem.put("tagGroup", tagGroup);
		tagItem.put("name", "backlog");
		
		List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
		tags.add(tagItem);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add tags
		node.put("tags", tags);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterModelStatus();
		
		//there should be a problem. There is model status but tag isn't ready
		assertEquals(1, ft.getCount());
	}
	
	//make sure that when the the model status is ready, it's not counted
	@Test
	void readyModelStatus() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//create the tag group
		Map<String, String> tagGroup = new HashMap<String, String>();
		tagGroup.put("name", "State of Model Completeness");
		
		Map<String, Object> tagItem = new HashMap<String, Object>();
		tagItem.put("tagGroup", tagGroup);
		tagItem.put("name", "ready");
		
		List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
		tags.add(tagItem);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add tags
		node.put("tags", tags);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterModelStatus();
		
		//there shouldn't be a problem
		assertEquals(0, ft.getCount());
	}
	
	//make sure that when there's no owner, it's counted
	@Test
	void noOwnerPersona() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//Add no owner
		Map<String, String> node1 = new HashMap<String, String>();
		node1.put("usageType", "notOwner");
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		List<Map<String, Object>> edges1 = new ArrayList<Map<String, Object>>();
		edges1.add(edge1);
		Map<String, Object> relation1 = new HashMap<String, Object>();
		relation1.put("edges", edges1);
		
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add owner info
		node.put("relITComponentToUserGroup", relation1);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterOwnerPersona();
		
		//there should be a problem since no owner
		assertEquals(1, ft.getCount());
	}
	
	//make sure that when there's an owner, it's not counted
	@Test
	void hasOwnerPersona() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//Add no owner
		Map<String, String> node1 = new HashMap<String, String>();
		node1.put("usageType", "owner");
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		List<Map<String, Object>> edges1 = new ArrayList<Map<String, Object>>();
		edges1.add(edge1);
		Map<String, Object> relation1 = new HashMap<String, Object>();
		relation1.put("edges", edges1);
		
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add owner info
		node.put("relITComponentToUserGroup", relation1);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterOwnerPersona();
		
		//there shouldn't be a problem
		assertEquals(0, ft.getCount());
	}
	
	//make sure that a lower scored factsheet is counted
	@Test
	void lowerScore() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Double> completion = new HashMap<String, Double>();
		completion.put("completion", .30);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//score
		node.put("completion", completion);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterScore(.50);
		
		//there should be a problem. .3 < .5
		assertEquals(1, ft.getCount());
	}
	
	//make sure that a equal scored factsheet isn't counted
	@Test
	void equalScore() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Double> completion = new HashMap<String, Double>();
		completion.put("completion", .50);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//score
		node.put("completion", completion);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterScore(.50);
		
		//there shouldn't be a problem. .5 !< .5
		assertEquals(0, ft.getCount());
	}
	
	//make sure that a greater scored factsheet isn't counted
	@Test
	void greaterScore() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Double> completion = new HashMap<String, Double>();
		completion.put("completion", .75);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//score
		node.put("completion", completion);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterScore(.50);
		
		//there shouldn't be a problem. .75 > .5
		assertEquals(0, ft.getCount());
	}
	
	//test to make sure that a factsheet without a provided behavior is counted
	@Test
	void noProvidedBehaviorsRelation() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make lack of relation
		Map<String, Integer> relProviderITComponentToInterface = new HashMap<String, Integer>();
		relProviderITComponentToInterface.put("totalCount", 0);		
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("relProviderITComponentToInterface", relProviderITComponentToInterface);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterProvidedBehaviorRelation();
		
		//there should be a problem factsheet since it has no relation
		assertEquals(1, ft.getCount());
	}
	
	//test to make sure that a factsheet with a provided behavior isn't counted
	@Test
	void hasProvidedBehaviorsRelation() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make lack of relation
		Map<String, Integer> relProviderITComponentToInterface = new HashMap<String, Integer>();
		relProviderITComponentToInterface.put("totalCount", 1);		
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("relProviderITComponentToInterface", relProviderITComponentToInterface);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterProvidedBehaviorRelation();
		
		//there shouldn't be a problem factsheet since it has a relation
		assertEquals(0, ft.getCount());
	}
		
	//test to make sure that a factsheet without documents is counted
	@Test
	void noDocuments() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make lack of relation
		Map<String, Integer> documents = new HashMap<String, Integer>();
		documents.put("totalCount", 0);		
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("documents", documents);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterDocuments();
		
		//there should be a problem factsheet since it has no documents
		assertEquals(1, ft.getCount());
	}
	
	//test to make sure that a factsheet with a document isn't counted
	@Test
	void hasDocument() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//make lack of relation
		Map<String, Integer> documents = new HashMap<String, Integer>();
		documents.put("totalCount", 1);		
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		node.put("documents", documents);
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterDocuments();
		
		//there shouldn't be a problem factsheet since it has a relation
		assertEquals(0, ft.getCount());
	}
		
	//test to make sure that a factsheet with no lifecycle is counted
	@Test
	void noLifecycle() {
		//no phases
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);

		List<String> phases = new ArrayList<String>();
		
		Map<String, Object> lifecycle = new HashMap<String, Object>();
		lifecycle.put("phases", phases);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add lifecycle
		node.put("lifecycle", lifecycle);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterLifecycle();
		
		//there should be a problem factsheet since there's no lifecycle
		assertEquals(1, ft.getCount());
		
		//===============================================================
		//no lifecycle
		//make the "factsheet" with no children
		Map<String, Integer> relToChild1 = new HashMap<String, Integer>();
		relToChild1.put("totalCount", 0);
		
		//lifecycle has no phases
		Map<String, Object> lifecycle1 = new HashMap<String, Object>();
		
		Map<String, Object> node1 = new HashMap<String, Object>();
		node1.put("id", "1234");
		node1.put("type", "ITComponent");
		node1.put("relToChild", relToChild1);
		//add lifecycle
		node.put("lifecycle", lifecycle1);
		
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send1 = new ArrayList<Map<String, Object>>();
		send1.add(edge1);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft1 = new FilterTools(send1, "domain");
		ft1.filterLifecycle();
		
		//there should be a problem factsheet since there's no lifecycle
		assertEquals(1, ft1.getCount());
		
	}

	//test to make sure that a factsheet with a lifecycle isn't counted
	@Test
	void hasLifecycle() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);

		List<String> phases = new ArrayList<String>();
		phases.add("Some part of the lifecycle");
		
		Map<String, Object> lifecycle = new HashMap<String, Object>();
		lifecycle.put("phases", phases);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add lifecycle
		node.put("lifecycle", lifecycle);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterLifecycle();
		
		//there shouldn't be a problem factsheet since there's a lifecycle
		assertEquals(0, ft.getCount());
	}
	
	//test to make sure that a factsheet without a software it component is counted
	@Test
	void noSoftwareITComponent() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, String> factsheet = new HashMap<String, String>();
		factsheet.put("category", "Not software");
		Map<String, Object> node1 = new HashMap<String, Object>();
		node1.put("factSheet", factsheet);
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		ArrayList<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
		edges.add(edge1);
		Map<String, ArrayList> relApplicationToITComponent = new HashMap<String, ArrayList>();
		relApplicationToITComponent.put("edges", edges);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "Application");
		node.put("relToChild", relToChild);
		//add softwareIT
		node.put("relApplicationToITComponent", relApplicationToITComponent);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterSoftwareITRelation();
		
		//there should be a problem factsheet since there's no software it
		assertEquals(1, ft.getCount());
	}
	
	//test to make sure that a factsheet with a software it component isn't counted
	@Test
	void hasSoftwareITComponent() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, String> factsheet = new HashMap<String, String>();
		factsheet.put("category", "software");
		Map<String, Object> node1 = new HashMap<String, Object>();
		node1.put("factSheet", factsheet);
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		ArrayList<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
		edges.add(edge1);
		Map<String, ArrayList> relApplicationToITComponent = new HashMap<String, ArrayList>();
		relApplicationToITComponent.put("edges", edges);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "Application");
		node.put("relToChild", relToChild);
		//add softwareIT
		node.put("relApplicationToITComponent", relApplicationToITComponent);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterSoftwareITRelation();
		
		//there shouldn't be a problem factsheet since software it
		assertEquals(0, ft.getCount());
	}
	
	//test to make sure No business value or risk is counted
	@Test
	void NoBusinessValueOrRisk() {
		//test when has business value but no risk
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add business value/risk
		node.put("businessValue", "Some text here");
		node.put("projectRisk", null);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterBusinessValueRisk();
		
		//there should be a problem factsheet since it there is no risk
		assertEquals(1, ft.getCount());
		
		//=============================================================================
		//test for no value but has risk
		//make the "factsheet" with no children
		Map<String, Integer> relToChild2 = new HashMap<String, Integer>();
		relToChild2.put("totalCount", 0);
		
		Map<String, Object> node2 = new HashMap<String, Object>();
		node2.put("id", "1234");
		node2.put("type", "ITComponent");
		node2.put("relToChild", relToChild2);
		//add business value/risk
		node2.put("businessValue", null);
		node2.put("projectRisk", "Some text here");
		
		Map<String, Object> edge2 = new HashMap<String, Object>();
		edge2.put("node", node2);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send2 = new ArrayList<Map<String, Object>>();
		send2.add(edge2);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft2 = new FilterTools(send2, "domain");
		ft2.filterBusinessValueRisk();
		
		//there should be a problem factsheet since it there is no value
		assertEquals(1, ft2.getCount());
			
	}
	
	//test to make sure has business value and risk isn't counted
	@Test
	void hasBusinessValueAndRisk() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add business value/risk
		node.put("businessValue", "Some text here");
		node.put("projectRisk", "Some more text here");
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "domain");
		ft.filterBusinessValueRisk();
		
		//there shouldn't be a problem factsheet since it there is value and risk
		assertEquals(0, ft.getCount());
	}
	
	//test to make sure that a factsheet with two problems is only counted once
	@Test
	void multipleProblemsTest() {
		//make the "factsheet" with no children
		Map<String, Integer> relToChild = new HashMap<String, Integer>();
		relToChild.put("totalCount", 0);
		
		//no accountable nor responsible
		Map<String, String> node1 = new HashMap<String, String>();
		node1.put("type", "OBSERVER");
		Map<String, Object> edge1 = new HashMap<String, Object>();
		edge1.put("node", node1);
		List<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
		edges.add(edge1);
		Map<String, Object> subscriptions = new HashMap<String, Object>();
		subscriptions.put("edges", edges);
		
		//ready model status
		//create the tag group
		Map<String, String> tagGroup = new HashMap<String, String>();
		tagGroup.put("name", "State of Model Completeness");
		
		Map<String, Object> tagItem = new HashMap<String, Object>();
		tagItem.put("tagGroup", tagGroup);
		tagItem.put("name", "ready");
		
		List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
		tags.add(tagItem);
		
		//score below threshold which is .5 for persona
		Map<String, Double> completion = new HashMap<String, Double>();
		completion.put("completion", .30);
		
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id", "1234");
		node.put("type", "ITComponent");
		node.put("relToChild", relToChild);
		//add account/response
		node.put("subscriptions", subscriptions);
		//add quality seal
		node.put("qualitySeal", "APPROVED");
		//add model status
		node.put("tags", tags);
		//add score
		node.put("completion", completion);
		
		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put("node", node);
		
		//add the edge to a list as FilterTools is usually used with multiple edges
		List<Map<String, Object>> send = new ArrayList<Map<String, Object>>();
		send.add(edge);
		
		//the constructor will run the filter. Domain is used as a random type. The type doesn't matter
		//in this test case but will in others
		FilterTools ft = new FilterTools(send, "persona");
		int numIncomplete = ft.retFilteredData();
		
		//there should only be one problem factsheet even though it has multiple problems
		assertEquals(1, numIncomplete);
		
		
	}
			
	
}
