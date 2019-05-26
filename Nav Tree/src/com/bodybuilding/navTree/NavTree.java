package com.bodybuilding.navTree;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bodybuilding.navTree.pojo.Node;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("/navTree")
public class NavTree {
	private JsonNode rootNode = null;
	private String input = null;
	private JsonNode result;
	private String parentId = null;
	private Node tree = null;
	private ArrayList<String> path;
	private boolean removePathNotNeeded = false;

	@Context
	private ServletContext context;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response prunedTree(@PathParam("id") final String id) throws Exception {
		if (context.getAttribute("json") != null) {			
			// Load Json
			final String json = (String) context.getAttribute("json");
			input = id != null ? id : "root";

			// Read tree
			final ObjectMapper objectMapper = new ObjectMapper();
			tree = new Node("root");
			rootNode = objectMapper.readTree(json);			
			result = traverse(rootNode);

			// Prune paths not needed
			prunePathNotNeeded(tree);			

			// Error
			if (result == null) {
				return Response.status(404).entity("ID not found").type(MediaType.APPLICATION_JSON.toString()).build();
			}

			
			// Return response
			return Response.ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result)).build();
		}

		throw new Exception("Unable to load JSON");
	}
	
	private void prunePathNotNeeded(final Node tree) {
		final Node pathNode = Node.search(tree, input);

		if (pathNode != null) {
			path = new ArrayList<>();
			path.add(pathNode.getId());
			Node parent = pathNode.getParent();
			while (parent != null) {
				path.add(parent.getId());
				parent = parent.getParent();
			}			
			
			removePathNotNeeded = true;
			result = traverse(result);
		} else {
			result = null;
		}
	}
	
	private JsonNode traverse(final JsonNode node) {
		if (node instanceof ObjectNode) {
			traverseObject(node);
		} else if (node instanceof ArrayNode) {
			traverseArray(node, "root");
		}
		return node;
	}

	private void traverseArray(final JsonNode node, final String parentId) {
		final Iterator<JsonNode> itr = node.elements();
		while (itr.hasNext()) {
			final JsonNode currentNode = itr.next();
			final ObjectNode object = (ObjectNode) currentNode;

			if (removePathNotNeeded && path != null && !path.contains(object.get("id").asText())
					&& !object.get("parentId").asText().equals("root")) {
				itr.remove();
				continue;
			} else if (!removePathNotNeeded) {
				object.put("parentId", parentId);
				this.parentId = parentId;
			}

			traverse(currentNode);
		}
	}

	private void traverseObject(final JsonNode node) {
		final ObjectNode object = (ObjectNode) node;
		
		final Node n = new Node(object.get("id").asText());
		tree.addChild(n, parentId);
		
		final Iterator<JsonNode> itr = object.elements();
		while (itr.hasNext()) {
			final JsonNode currentNode = itr.next();
			if (currentNode instanceof ArrayNode) {
				traverseArray(currentNode, object.get("id").asText());
			}
		}
	}
}