package com.ebsco.leanixmetrics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilterTools {
	
	private List<Map<String, Object>> initialList;
	private Set<String> filteredList;
	private String type;
	private Map<String, String> ebscoToLeanix;
	
	public FilterTools(List<Map<String, Object>> info, String t) {
		this.initialList = keepLeafNodes(info);
		this.type = t;
		this.filteredList = new HashSet<String>();
		
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
	
	public int retFilteredData() {
		System.out.println(type + " filtering");
		//filterLeafNodes();
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
		else if (this.type.equals("domain")) {
			filterRelation("Bounded Context");
			filterRelation("Use Case");
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.6);
		}
		else if (this.type.equals("dataObject")) {
			filterBoundedContextAndBehavior();
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.5);
		}
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
		else if (this.type.equals("behavior")) {
			filterRelation("Provider Application");
			filterRelation("IT Component");
			filterAccountResponse();
			filterQualitySeal();
			filterModelStatus();
			filterScore(.6);
		}
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
		
		return this.filteredList.size();
	}
	
	private List<Map<String, Object>> keepLeafNodes(List<Map<String, Object>> info) {
		//int count = 0;
		Iterator<Map<String, Object>> it = info.iterator();
		while (it.hasNext()) {
		  Map<String, Object> edge = it.next();
		  Map<String, Object> node = (Map<String, Object>) edge.get("node");
		  Map<String, Integer> innerNode = (Map<String, Integer>) node.get("relToChild");
		  if (innerNode.get("totalCount") != 0) {
			  it.remove();
			  //this.filteredList.add(node.get("id").toString());
			  //count++;
		  }
		}
		return info;
		//System.out.println(count + " incomplete factsheets from LeafNode filter");
	}
	
	private void filterRelation(String relation) {
		int count = 0;
		String searchKey;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			searchKey = "rel" + node.get("type").toString() + "To" + this.ebscoToLeanix.get(relation);
			Map<String, Integer> innerNode = (Map<String, Integer>) node.get(searchKey);
			if (innerNode.get("totalCount") == 0) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from No Relation to " + relation + " filter");
	}
	
	private void filterAccountResponse() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			boolean responseFound = false;
			boolean accountFound = false;
			
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			Map<String, Object> subscriptions = (Map<String, Object>) node.get("subscriptions");
			
			List<Map<String, Object>> subEdges = (List<Map<String, Object>>) subscriptions.get("edges");
			Iterator<Map<String, Object>> it2 = subEdges.iterator();
			while (it2.hasNext()) {
				Map<String, Object> innerEdge = it2.next();
				Map<String, Object> innerNode = (Map<String, Object>) innerEdge.get("node");
				if (innerNode.get("type").toString().equals("RESPONSIBLE")) {
					responseFound = true;
				}
				if (innerNode.get("type").toString().equals("ACCOUNTABLE")) {
					accountFound = true;
				}
			}
			
			if (!(responseFound) || !(accountFound)) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from No Accountable or Repsonsible filter");
	}

	private void filterBoundedContextAndBehavior() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			String contextKey = "rel" + node.get("type").toString() + "ToApplication";
			String behaviorKey = "rel" + node.get("type").toString() + "ToInterface";
			Map<String, Integer> contextRel = (Map<String, Integer>) node.get(contextKey);
			Map<String, Integer> behaviorRel = (Map<String, Integer>) node.get(behaviorKey);
			if ((contextRel.get("totalCount") == 0) && (behaviorRel.get("totalCount") == 0)) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from Bounded Context and Behavior filter");
	}
	
	private void filterBusinessCriticality() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			if ((node.get("businessCriticality") == null) || (node.get("businessCriticalityDescription") == null)
					|| (node.get("businessCriticalityDescription").toString().equals(""))) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from Business Criticality (Description) filter");
	}

	private void filterEisProviderOwnerPersona() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while(it.hasNext()) {
			//first check to see if eis is an owner
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			
			boolean eisProvider = false;
			String providerKey = "rel" + node.get("type").toString() + "ToProvider";
			Map<String, Object> relation = (Map<String, Object>) node.get(providerKey);
			ArrayList<Map<String, Object>> relEdgeList = (ArrayList<Map<String, Object>>) relation.get("edges");
			for (int i = 0; i < relEdgeList.size(); i++) {
				Map<String, Object> innerEdge = relEdgeList.get(i);
				Map<String, Object> innerNode = (Map<String, Object>) innerEdge.get("node");
				Map<String, String> factsheet = (Map<String, String>) innerNode.get("factSheet");
				if (factsheet.get("displayName").equals("EIS")) {
					eisProvider = true;
					break;
				}
			}
			
			boolean ownerFound = false;
			if (eisProvider) {
				String userGroupKey = "rel" + node.get("type").toString() + "ToUserGroup";
				Map<String, Object> relation2 = (Map<String, Object>) node.get(userGroupKey);
				ArrayList<Map<String, Object>> relEdgeList2 = (ArrayList<Map<String, Object>>) relation2.get("edges");
				for (int i = 0; i < relEdgeList2.size(); i++) {
					Map<String, Object> innerEdge2 = relEdgeList2.get(i);
					Map<String, String> innerNode2 = (Map<String, String>) innerEdge2.get("node");
					if ((innerNode2.get("usageType") != null) 
							&& (innerNode2.get("usageType").equals("owner"))) {
						ownerFound = true;
					}
				}
			}
			
			if ((eisProvider && !(ownerFound))) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from EIS Provider Owner filter");
	}
	
	private void filterFunctionalFit() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			if ((node.get("functionalSuitability") == null) || (node.get("functionalSuitabilityDescription") == null)
					|| (node.get("functionalSuitabilityDescription").toString().equals(""))) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from Functional Fit (Description) filter");
	}
	
	private void filterTechnicalFit() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			if ((node.get("technicalSuitability") == null) || (node.get("technicalSuitabilityDescription") == null)
					|| (node.get("technicalSuitabilityDescription").toString().equals(""))) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from Technical Fit (Description) filter");
	}

	private void filterQualitySeal() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			if (node.get("qualitySeal").toString().equals("BROKEN")) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from Broken Quality Seal filter");
	}
	
	private void filterModelStatus() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			ArrayList<Map<String, Object>> tags = (ArrayList<Map<String, Object>>) node.get("tags");
			//boolean tagFound = false;
			for (int i = 0; i < tags.size(); i++) {
				Map<String, Object> currentTag = tags.get(i);
				Map<String, Object> currentTagGroup = (Map<String, Object>) currentTag.get("tagGroup");
				if (currentTagGroup.get("name").toString().equals("State of Model Completeness")) {
					if (!(currentTag.get("name").toString().equals("ready"))) {
						this.filteredList.add(node.get("id").toString());
						count++;
					}
				}
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from Model Completion Status filter");
	}
	
	private void filterOwnerPersona() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			
			int numOwners = 0;
			String searchKey = "rel" + node.get("type").toString() + "ToUserGroup";
			Map<String, ArrayList> relation = (Map<String, ArrayList>) node.get(searchKey);
			ArrayList innerEdges = relation.get("edges");
			for (int i = 0; i < innerEdges.size(); i++) {
				Map<String, Object> currentInnerEdge = (Map<String, Object>) innerEdges.get(i);
				Map<String, Object> currentInnerNode = (Map<String, Object>) currentInnerEdge.get("node");
				if ((currentInnerNode.get("usageType") != null) 
						&& currentInnerNode.get("usageType").toString().equals("owner")) {
					numOwners++;
				}
			}
			
			if (numOwners != 1) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from Owner Persona filter");
	}
	
	private void filterScore(double percent) {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			Map<String, Double> innerNode = (Map<String, Double>) node.get("completion");
			if (innerNode.get("completion") < percent) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from Percent Complete filter");
	}
	
	private void filterProvidedBehaviorRelation() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			String searchKey = "relProvider" + node.get("type").toString() + "ToInterface";
			Map<String, Integer> innerNode = (Map<String, Integer>) node.get(searchKey);
			if (innerNode.get("totalCount") == 0) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from Provided Behavior filter");
	}
	
	private void filterDocuments() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			Map<String, Integer> innerNode = (Map<String, Integer>) node.get("documents");
			if (innerNode.get("totalCount") == 0) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from No Documents filter");
	}
	
	private void filterLifecycle() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			if (node.get("lifecycle") == null) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}else {
				Map<String, ArrayList> innerNode = (Map<String, ArrayList>) node.get("lifecycle");
				ArrayList ar = innerNode.get("phases");
				if (ar.size() == 0) {
					this.filteredList.add(node.get("id").toString());
					count++;
				}
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from No Lifecycle filter");
	}
	
	private void filterSoftwareITRelation() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			
			boolean softwareFound = false;
			String searchKey = "rel" + node.get("type").toString() + "ToITComponent";
			Map<String, ArrayList> relation = (Map<String, ArrayList>) node.get(searchKey);
			ArrayList<Map<String, Object>> innerEdges = relation.get("edges");
			for (int i = 0; i < innerEdges.size(); i++) {
				Map<String, Object> currentInnerEdge = innerEdges.get(i);
				Map<String, Object> currentInnerNode = (Map<String, Object>) currentInnerEdge.get("node");
				Map<String, String> currentInnerFactsheet = (Map<String, String>) currentInnerNode.get("factSheet");
				if ((currentInnerFactsheet.get("category") != null) && 
						(currentInnerFactsheet.get("category").equals("software"))) {
					softwareFound = true;
					break;
				}
			}
			if (!(softwareFound)) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from Software IT Component filter");
	}
	
	private void filterBusinessValueRisk() {
		int count = 0;
		Iterator<Map<String, Object>> it = this.initialList.iterator();
		while (it.hasNext()) {
			Map<String, Object> edge = it.next();
			Map<String, Object> node = (Map<String, Object>) edge.get("node");
			if (node.get("businessValue") == null) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
			else if (node.get("projectRisk") == null) {
				this.filteredList.add(node.get("id").toString());
				count++;
			}
		}
		System.out.println("\t" + count + " incomplete factsheets from No Business Value or Risk filter");
	}
}
