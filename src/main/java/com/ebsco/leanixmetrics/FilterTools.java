package com.ebsco.leanixmetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//class to return the number of incomplete factsheets given all the factsheets of a certain type
public class FilterTools {
	
	//var to hold initial list of factsheet leafNodes
	private List<Map<String, Object>> initialList;
	//var to hold the incomplete factsheets. It's a set so there can't be duplicates
	private Set<String> filteredList;
	//string to hold the type of factsheet all the given factsheets are
	private String type;
	//map to hold help convert ebsco type names to leanix type names
	private Map<String, String> ebscoToLeanix;
	
	//constructor
	public FilterTools(List<Map<String, Object>> info, String t) {
		//save the leafnodes from the list of nodes
		this.initialList = keepLeafNodes(info);
		//save the type
		this.type = t;
		//create an empty set for incomplete factsheets
		this.filteredList = new HashSet<String>();
		
		//ebsco type names to leanix type names
		this.ebscoToLeanix = new HashMap<String, String>();
		this.ebscoToLeanix.put("Domain", "BusinessCapability");
		this.ebscoToLeanix.put("Use Case", "Process");
		this.ebscoToLeanix.put("Persona", "UserGroup");
		this.ebscoToLeanix.put("Epic", "Project"); 
		this.ebscoToLeanix.put("Bounded Context", "Application");
		this.ebscoToLeanix.put("Behavior", "Interface");
		this.ebscoToLeanix.put("Data Object", "DataObject");
		this.ebscoToLeanix.put("IT Component", "ITComponent");
		this.ebscoToLeanix.put("Provider", "Provider");
		this.ebscoToLeanix.put("Technical Stack", "TechnicalStack");
		this.ebscoToLeanix.put("Provider Application", "ProviderApplication");
	}
	
	//method to figure out which type of filters should be applied to the given type
	public int retFilteredData() {
		//let the user know filtering has begun
		System.out.println(type + " filtering");
		//Bounded Context/Application filtering
		if (this.type.equals("boundedContext")) {
			filterLifecycle();
			filterBusinessCriticality();
			filterFunctionalFit();
			filterRelation("Domain");
			filterRelation("Use Case");
			filterOwnerPersona();
			filterRelation("Data Object");
			filterProvidedBehaviorRelation();
			filterTechnicalFit();
			filterSoftwareITRelation();
			filterDocuments();
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.7);
		}
		//domain/BusinessCapability filtering
		else if (this.type.equals("domain")) {
			filterRelation("Bounded Context");
			filterRelation("Use Case");
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.6);
		}
		//Data Object filtering
		else if (this.type.equals("dataObject")) {
			filterBoundedContextAndBehavior();
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.5);
		}
		//IT Component filtering
		else if (this.type.equals("ITComponent")) {
			filterRelation("Provider");
			filterDocuments();
			filterLifecycle();
			filterTechnicalFit();
			filterRelation("Behavior");
			filterEisProviderOwnerPersona();
			filterOwnerPersona();
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.7);	
		}
		//Behavior/Interface filtering
		else if (this.type.equals("behavior")) {
			filterRelation("Provider Application");
			filterRelation("IT Component");
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.6);
		}
		//Use Case/Process filtering
		else if (this.type.equals("useCase")) {
			filterRelation("Domain");
			filterDocuments();
			filterLifecycle();
			filterRelation("Bounded Context");
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.60);
		}
		//Epic/Project filtering
		else if (this.type.equals("epic")) {
			filterDocuments();
			filterLifecycle();
			filterBusinessValueRisk();
			filterRelation("Domain");
			filterRelation("Use Case");
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.5);
		}
		//Persona/userGroup filtering
		else if (this.type.equals("persona")) {
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.5);
		}
		
		//the size of the set is the number of incomplete factsheets
		return this.filteredList.size();
	}
	
	//keeps factsheets that don't have children
	private List<Map<String, Object>> keepLeafNodes(List<Map<String, Object>> info) {
		//Iterator to go through factsheets (fs)
		Iterator<Map<String, Object>> it = info.iterator();
		while (it.hasNext()) {
			//get the node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			
			//get the nodes relToChild object
			Map<String, Integer> innerNode = (Map<String, Integer>) node.get("relToChild");
			//if the node has a child
			if (innerNode.get("totalCount") != 0) {
				//remove it from the list of nodes
				it.remove();
			}
		}
		//return leafNode factsheets
		return info;
	}
	
	//count fs that don't have the given relation
	private void filterRelation(String relation) {
		//var to hold number of incomplete fs
		int count = 0;
		//var to key to find relation
		String searchKey;
		//iterate through fs
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get the node in each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//create the search key using the current type and given relation
			searchKey = "rel" + node.get("type").toString() + "To" + this.ebscoToLeanix.get(relation);
			//get the object returned from using the search key
			Map<String, Integer> innerNode = (Map<String, Integer>) node.get(searchKey);
			//if there is no connection
			if (innerNode.get("totalCount") == 0) {
				//add it to the set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//tell the user how many incomplete factsheets from this filter
		System.out.println("\t" + count + " incomplete factsheets from No Relation to " + relation + " filter");
	}
	
	//counts fs that don't have both accountable and responsible
	private void filterAccountResponse() {
		//var to hold incomplete fs
		int count = 0;
		//iterate through fs
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//bools to see if there's someone responsible and accountable
			boolean responseFound = false;
			boolean accountFound = false;
			
			//get the node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			
			//get the subscriptions for the fs
			Map<String, Object> subscriptions = (Map<String, Object>) node.get("subscriptions");
			
			//go through subscriptions
			List<Map<String, Object>> subEdges = (List<Map<String, Object>>) subscriptions.get("edges");
			Iterator<Map<String, Object>> it2 = subEdges.iterator();
			while (it2.hasNext()) {
				//get the node for each subscription
				Map<String, Object> innerEdge = it2.next();
				Map<String, Object> innerNode = (Map<String, Object>) innerEdge.get("node");
				
				//see if the subsrictption is Responsible or Accountable and save the info as a bool
				if (innerNode.get("type").toString().equals("RESPONSIBLE")) {
					responseFound = true;
				}
				if (innerNode.get("type").toString().equals("ACCOUNTABLE")) {
					accountFound = true;
				}
			}
			
			//if there isn't both someone responsible and accountable
			if (!(responseFound) && !(accountFound)) {
				//add it to the set
				this.filteredList.add(node.get("id").toString());
				//increase count
				count++;
			}
		}
		//let the user know the count
		System.out.println("\t" + count + " incomplete factsheets from No Accountable or Repsonsible filter");
	}

	//counts fs that aren't connected to a bounded context or behavior
	private void filterBoundedContextAndBehavior() {
		//var to hold num of incomplete fs
		int count = 0;
		
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//get keys to use to get connection information
			String contextKey = "rel" + node.get("type").toString() + "ToApplication";
			String behaviorKey = "rel" + node.get("type").toString() + "ToInterface";
			//get the relation info using the keys
			Map<String, Integer> contextRel = (Map<String, Integer>) node.get(contextKey);
			Map<String, Integer> behaviorRel = (Map<String, Integer>) node.get(behaviorKey);
			//if the fs has no connection to either
			if ((contextRel.get("totalCount") == 0) && (behaviorRel.get("totalCount") == 0)) {
				//add it to the set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from Bounded Context and Behavior filter");
	}
	
	//count fs with no Business Criticality or no description
	private void filterBusinessCriticality() {
		//var to hold num incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//if business criticalitty is null or description is null/blank
			if ((node.get("businessCriticality") == null) || (node.get("businessCriticalityDescription") == null)
					|| (node.get("businessCriticalityDescription").toString().equals(""))) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let the user know the count
		System.out.println("\t" + count + " incomplete factsheets from Business Criticality (Description) filter");
	}

	//counts fs with no owner persona when EIS is the provider
	private void filterEisProviderOwnerPersona() {
		//var to hold num of incomplete fs
		int count = 0;
		//iterate through the edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while(it.hasNext()) {
			//first check to see if eis is an owner
			//get the node from the edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			
			//bool to see hold if eis is provider
			boolean eisProvider = false;
			//key to find providers
			String providerKey = "rel" + node.get("type").toString() + "ToProvider";
			//get the information on the providers
			Map<String, Object> relation = (Map<String, Object>) node.get(providerKey);
			//get the list of provider edges
			ArrayList<Map<String, Object>> relEdgeList = (ArrayList<Map<String, Object>>) relation.get("edges");
			//go through provider edges
			for (int i = 0; i < relEdgeList.size(); i++) {
				//get the node from each edges
				Map<String, Object> innerEdge = relEdgeList.get(i);
				Map<String, Object> innerNode = (Map<String, Object>) innerEdge.get("node");
				//get the provider factsheet
				Map<String, String> factsheet = (Map<String, String>) innerNode.get("factSheet");
				//if the display name of that factsheet is EID
				if (factsheet.get("displayName").equals("EIS")) {
					//then EIS is a provider
					eisProvider = true;
					break;
				}
			}
			
			//bool so hold if an owner is found
			boolean ownerFound = false;
			//if eis is a provider
			if (eisProvider) {
				//key to search user group relations
				String userGroupKey = "rel" + node.get("type").toString() + "ToUserGroup";
				//get the object of user group relations
				Map<String, Object> relation2 = (Map<String, Object>) node.get(userGroupKey);
				//get the list of user group edges
				ArrayList<Map<String, Object>> relEdgeList2 = (ArrayList<Map<String, Object>>) relation2.get("edges");
				//go through the edges
				for (int i = 0; i < relEdgeList2.size(); i++) {
					//get the node from each edge
					Map<String, Object> innerEdge2 = relEdgeList2.get(i);
					Map<String, String> innerNode2 = (Map<String, String>) innerEdge2.get("node");
					//if the node has a usage type and it's value is owner
					if ((innerNode2.get("usageType") != null) 
							&& (innerNode2.get("usageType").equals("owner"))) {
						//then there is an owner
						ownerFound = true;
					}
				}
			}
			
			//if eis is the provider and there's no owner
			if ((eisProvider && !(ownerFound))) {
				//add to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know num of incomplete fs
		System.out.println("\t" + count + " incomplete factsheets from EIS Provider Owner filter");
	}
	
	//count fs that don't have a functional fit or description
	private void filterFunctionalFit() {
		//var to hold num incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//if there's no functionSuitability or description doesn't exist/is empty 
			if ((node.get("functionalSuitability") == null) || (node.get("functionalSuitabilityDescription") == null)
					|| (node.get("functionalSuitabilityDescription").toString().equals(""))) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from Functional Fit (Description) filter");
	}
	
	//count fs that don't have a techincal fit or description
	private void filterTechnicalFit() {
		//var to hold num incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//if there's no technical suitability or no/blank description 
			if ((node.get("technicalSuitability") == null) || (node.get("technicalSuitabilityDescription") == null)
					|| (node.get("technicalSuitabilityDescription").toString().equals(""))) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from Technical Fit (Description) filter");
	}

	//count fs that have a broken quality seal
	private void filterQualitySeal() {
		//var to hold num incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//if quality seal is broken
			if (node.get("qualitySeal").toString().equals("BROKEN")) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from Broken Quality Seal filter");
	}
	
	//count fs that don't have a model status or have one that isn't ready
	//Does a factsheet with no model status count or is it only model statuses that aren't ready?
	private void filterModelStatus() {
		//var to hold num incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//get list of tags from node
			ArrayList<Map<String, Object>> tags = (ArrayList<Map<String, Object>>) node.get("tags");
			//bool to hold if the tag is found
			boolean tagFound = false;
			//go through tags
			for (int i = 0; i < tags.size(); i++) {
				//get the current tag
				Map<String, Object> currentTag = tags.get(i);
				//get the group that the tag belongs tp
				Map<String, Object> currentTagGroup = (Map<String, Object>) currentTag.get("tagGroup");
				//if the tag group is state of model completeness
				if (currentTagGroup.get("name").toString().equals("State of Model Completeness")) {
					//the tag has been found
					tagFound = true;
					//if the tag name isn't ready
					if (!(currentTag.get("name").toString().equals("ready"))) {
						//add the fs to the set
						this.filteredList.add(node.get("id").toString());
						//incr count
						count++;
						break;
					}
				}
			}
			//if there is no state of model completeness status
			if (!(tagFound)) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from Model Completion Status filter");
	}
	
	//counts fs that have no owner persona
	//zero vs one vs multiple owner persona
	private void filterOwnerPersona() {
		//var to hold num incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			
			//var to hold number of owners
			int numOwners = 0;
			//search key to get user group list
			String searchKey = "rel" + node.get("type").toString() + "ToUserGroup";
			//use the key to get relation object
			Map<String, ArrayList> relation = (Map<String, ArrayList>) node.get(searchKey);
			//get list of user group relations from object
			ArrayList innerEdges = relation.get("edges");
			//go through edges
			for (int i = 0; i < innerEdges.size(); i++) {
				//get node of each edge
				Map<String, Object> currentInnerEdge = (Map<String, Object>) innerEdges.get(i);
				Map<String, Object> currentInnerNode = (Map<String, Object>) currentInnerEdge.get("node");
				//if there's a usage type and the type is owner
				if ((currentInnerNode.get("usageType") != null) 
						&& currentInnerNode.get("usageType").toString().equals("owner")) {
					//increment the number of owners
					numOwners++;
				}
			}
			
			//if there's no owner
			if (numOwners == 0) {
				//add node to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from Owner Persona filter");
	}

	//counts fs with a score lower than the given double
	private void filterScore(double percent) {
		//var to hold num incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//get the completion object from each node
			Map<String, Double> innerNode = (Map<String, Double>) node.get("completion");
			//if the completion is less than the given double
			if (innerNode.get("completion") < percent) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from Percent Complete filter");
	}

	//counts fs with no provided behaviors
	private void filterProvidedBehaviorRelation() {
		//var to hold incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//key to get realtions
			String searchKey = "relProvider" + node.get("type").toString() + "ToInterface";
			//get relations using key
			Map<String, Integer> innerNode = (Map<String, Integer>) node.get(searchKey);
			//if there are no provided behaviors
			if (innerNode.get("totalCount") == 0) {
				//add node to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from Provided Behavior filter");
	}
	
	//count fs with no document links
	private void filterDocuments() {
		//var to hold num incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//get documents object
			Map<String, Integer> innerNode = (Map<String, Integer>) node.get("documents");
			//if there are no document links
			if (innerNode.get("totalCount") == 0) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from No Documents filter");
	}
	
	//counts fs with no lifecycle
	private void filterLifecycle() {
		//var to hold num incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			//if there's no lifecycle
			if (node.get("lifecycle") == null) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			//else there is a lifecycle
			}else {
				//get lifecycle object from node
				Map<String, ArrayList> innerNode = (Map<String, ArrayList>) node.get("lifecycle");
				//get list of phases in lifecycle
				ArrayList ar = innerNode.get("phases");
				//if there are now phases, it's an empty lifecycle
				if (ar.size() == 0) {
					//add fs to set
					this.filteredList.add(node.get("id").toString());
					//incr count
					count++;
				}
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from No Lifecycle filter");
	}
	
	//counts fs with no IT component of type software
	private void filterSoftwareITRelation() {
		//var to hold num incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			
			//bool to see if software type is found
			boolean softwareFound = false;
			//key to get relation from node
			String searchKey = "rel" + node.get("type").toString() + "ToITComponent";
			//get relations using key
			Map<String, ArrayList> relation = (Map<String, ArrayList>) node.get(searchKey);
			//get the list of relation edges
			ArrayList<Map<String, Object>> innerEdges = relation.get("edges");
			//go through edges
			for (int i = 0; i < innerEdges.size(); i++) {
				//get node from each edge
				Map<String, Object> currentInnerEdge = innerEdges.get(i);
				Map<String, Object> currentInnerNode = (Map<String, Object>) currentInnerEdge.get("node");
				
				//get the it component factsheet
				Map<String, String> currentInnerFactsheet = (Map<String, String>) currentInnerNode.get("factSheet");
				//if the it component has a category and it software
				if ((currentInnerFactsheet.get("category") != null) && 
						(currentInnerFactsheet.get("category").equals("software"))) {
					//there is a software it component
					softwareFound = true;
					break;
				}
			}
			
			//if there's no software it component 
			if (!(softwareFound)) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from Software IT Component filter");
	}

	//counts fs with no business value or risk
	private void filterBusinessValueRisk() {
		//var to hold incomplete fs
		int count = 0;
		//iterate through edges
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			//get node from each edge
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			
			//if there's no business value
			if (node.get("businessValue") == null) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
			//else if there's no project risk
			else if (node.get("projectRisk") == null) {
				//add fs to set
				this.filteredList.add(node.get("id").toString());
				//incr count
				count++;
			}
		}
		//let user know count
		System.out.println("\t" + count + " incomplete factsheets from No Business Value or Risk filter");
	}
}
