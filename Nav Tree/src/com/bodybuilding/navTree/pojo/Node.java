package com.bodybuilding.navTree.pojo;

import java.util.ArrayList;
import java.util.List;

public class Node {	
	private String id;
	private Node parent;
	private List<Node> children;
	
	public Node(String id) {
		this.id = id;
	}
	
	public Node() {
		this.id = "root";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public void addChild(Node node, String parentId) {
		if(parentId == null) {
			return;
		}
		if(this.getChildren() == null) {
			this.setChildren(new ArrayList<Node>());
			node.setParent(this);
			this.getChildren().add(node);
		} else if(parentId.contentEquals("root")) {
			node.setParent(this);
			this.getChildren().add(node);
		} else {
			traverse(this, node, parentId);
		}		
	}
	
	public void traverse(Node toTraverse, Node node, String parentId) {		
		for(Node child : toTraverse.getChildren()) {
			if(child.getId().contentEquals(parentId)) {
				if(child.getChildren() == null) {
					child.setChildren(new ArrayList<Node>());
				}
				node.setParent(child);
				child.getChildren().add(node);
				return;
			} else if(child.getChildren() != null) {
				traverse(child, node, parentId);
			}
		}
	}
	
	public static Node search(Node node, String input) {
		if(node.getId().contentEquals(input)) {
			return node;
		}
		if(node.getChildren() != null) {
			for(Node child: node.getChildren()) {
				Node found = Node.search(child, input);
				if(found != null) {
					return found;
				}
			}
		}
		return null;
	}
	
	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public boolean isRoot() {
		return this.id.contentEquals("root");
	}
}
