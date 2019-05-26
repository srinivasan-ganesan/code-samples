package com.bodybuilding.navTree.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.bodybuilding.navTree.pojo.Node;

import junit.framework.TestCase;

public class NavTreeTester extends TestCase {
	
	@Test
	public void testIdDoesNotExist() throws Exception {	  
	    // Given
	    String id = RandomStringUtils.randomAlphabetic(3);
	    HttpUriRequest request = new HttpGet( "http://localhost:8090/NavTree/navTree/" + id );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
	 
	    // Then
	    assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
	}
	
	@Test
	public void testMimeType() throws Exception {
	  
	   // Given
	   String jsonMimeType = "application/json";
	   String id = RandomStringUtils.randomAlphabetic(3);
	   HttpUriRequest request = new HttpGet( "http://localhost:8090/NavTree/navTree/" + id );
	 
	   // When
	   HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	   // Then
	   String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	   assertEquals( jsonMimeType, mimeType );
	}
	
	@Test
	public void testPrunedTree30Day() throws Exception {
	  
	    // Given
		String id="30day";
	    HttpUriRequest request = new HttpGet( "http://localhost:8090/NavTree/navTree/" + id );
	 
	    // When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    Node node = NavTreeTesterUtil.retrieveResourceFromResponse(
	      response, Node.class);
	    Node search = Node.search(node, id);
	    
	    assertThat( id, Matchers.is( search.getId() ) );	    
	    assertThat( 6, Matchers.is( node.getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(0).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(2).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(3).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(4).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(5).getChildren().size() ) );
	}
	
	@Test
	public void testPrunedTree30daymain() throws Exception {
	  
	    // Given
		String id="30daymain";
	    HttpUriRequest request = new HttpGet( "http://localhost:8090/NavTree/navTree/" + id );
	 
	    // When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    Node node = NavTreeTesterUtil.retrieveResourceFromResponse(
	      response, Node.class);
	    Node search = Node.search(node, id);
	    
	    assertThat( id, Matchers.is( search.getId() ) );	    
	    assertThat( 6, Matchers.is( node.getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(0).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(2).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(3).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(4).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(5).getChildren().size() ) );
	}
	
	@Test
	public void testPrunedTreeTraining() throws Exception {
	  
	    // Given
		String id="Training";
	    HttpUriRequest request = new HttpGet( "http://localhost:8090/NavTree/navTree/" + id );
	 
	    // When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    Node node = NavTreeTesterUtil.retrieveResourceFromResponse(
	      response, Node.class);
	    Node search = Node.search(node, id);
	    
	    assertThat( id, Matchers.is( search.getId() ) );	    
	    assertThat( 6, Matchers.is( node.getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(0).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(1).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(3).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(4).getChildren().size() ) );
	    assertThat( 0, Matchers.is( node.getChildren().get(5).getChildren().size() ) );
	}
}
