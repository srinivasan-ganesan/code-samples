package com.bodybuilding.navTree;

import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Path("/navTree")
public class NavTree {
	private JsonNode rootNode = null;
	private String input = null;
	private boolean removeLeaves;
	private boolean foundMatch;
	private String matchedParent;
	private JsonNode result;
	private boolean processAncestor = false;
	private String ancestor = null;

	@Context
	private ServletContext context;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String prunedTree(@PathParam("id") String id) throws Exception {
		if (context.getAttribute("json") != null) {
			String json = (String) context.getAttribute("json");
			input = (id != null ? id : "root");

			ObjectMapper objectMapper = new ObjectMapper();			

			// tree traversal
			rootNode = objectMapper.readTree(json);
			result = traverse(rootNode);			
			
			if (input.contentEquals("root")) {
				return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
			}

			// error
			if (!foundMatch) {
				return Response.status(404).entity("ID not found").type(MediaType.APPLICATION_JSON.toString()).build()
						.toString();
			}
						
			// remove leaf nodes
			removeLeaves = true;
			traverse(result);
			if(ancestor != null) {
				removeLeaves = false;
				processAncestor = true;
				traverse(result);
			}
			
			// return result as String
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
		}

		throw new Exception("Unexpected Error");
	}

	private JsonNode traverse(JsonNode node) {
		if (node instanceof ObjectNode) {
			traverseObject(node);
		} else if (node instanceof ArrayNode) {
			traverseArray(node, "root");
		}
		return node;
	}

	private void traverseArray(JsonNode node, String parentId) {
		Iterator<JsonNode> itr = node.elements();		
		while (itr.hasNext()) {
			JsonNode currentNode = itr.next();
			ObjectNode object = (ObjectNode) currentNode;
			if (removeLeaves) {
				if((matchedParent.contentEquals("root") || input.contentEquals("root"))
						&& !object.get("parentId").asText().contentEquals("root")) {
					itr.remove();
					continue;
				} else if ((object.get("parentId").asText().contentEquals(matchedParent)
						&& !object.get("id").asText().contentEquals(input))
						&& !object.get("parentId").asText().contentEquals("root")) {
					itr.remove();
					continue;
				}

				if (object.get("id").asText().contentEquals(matchedParent)) {
					ancestor = object.get("parentId").asText();
				}

			} else if(processAncestor) {
				if ((object.get("parentId").asText().contentEquals(ancestor)
						&& !object.get("id").asText().contentEquals(matchedParent))) {
					itr.remove();
					continue;
				}
			} else {
				object.put("parentId", parentId);
				if (foundMatch && !object.get("parentId").asText().equals("root")) {					
					itr.remove();
					continue;
				}
			}
			traverse(currentNode);
		}
	}

	private void traverseObject(JsonNode node) {
		ObjectNode object = (ObjectNode) node;
		Iterator<JsonNode> itr = object.elements();
		while (itr.hasNext()) {
			JsonNode currentNode = itr.next();
			if (currentNode instanceof ArrayNode) {
				traverseArray(currentNode, object.get("id").asText());
			}			
			if (currentNode.asText().contentEquals(input)) {
				matchedParent = object.get("parentId") != null ? object.get("parentId").asText() : null;
				foundMatch = true;
			}
		}
	}
}