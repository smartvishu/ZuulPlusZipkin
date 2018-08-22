package com.zuul;

import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.netflix.zuul.context.RequestContext;
import com.zuul.filter.PreFilter;
import com.zuul.filter.ServiceGatewayConstants;

@SuppressWarnings("deprecation")
public class TokenValidationFilterTest {

	@InjectMocks
	private PreFilter tokenValidationFilter;

	private RequestContext ctx;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Mock
	private DiscoveryClient discoveryClient;
	@Mock 
	private ServiceInstance serviceInstance;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ctx = new RequestContext();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		RequestContext.testSetCurrentContext(ctx);
	}

	@After
	public void clear() {
		RequestContext.getCurrentContext().clear();
	}

	
	@Test
	public void shouldNotFilterIfUnsecured() {
		ctx.set(ServiceGatewayConstants.SERVICE_ID, "configisv1");
		ctx.set(ServiceGatewayConstants.OPERATION_ID, "retrieveConfiguration");

		Assert.assertEquals(false, tokenValidationFilter.shouldFilter());
	}
	
	@Test
	public void shouldNotFilterIfSkipSet() {
		ctx.set(ServiceGatewayConstants.SERVICE_ID, "configisv1");
		ctx.set(ServiceGatewayConstants.OPERATION_ID, "retrieveConfiguration");
		
		Assert.assertEquals(false, tokenValidationFilter.shouldFilter());
	}

	@Test
	public void shouldFilterIfSecured() {
		ctx.set(ServiceGatewayConstants.SERVICE_ID, "accountbsv1");

		Assert.assertEquals(true, tokenValidationFilter.shouldFilter());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldValidateToken() throws URISyntaxException {
		request.setRequestURI("notnull");
		request.setScheme("http");
		request.setRemoteHost("localhost");
		request.addHeader("Authorization", "token1234");
		ctx.setRequest(request);
		ctx.setResponse(response);
		
		
		List<ServiceInstance> list=Arrays.asList(serviceInstance);
		URI uri = new URI("http://localhost");
		ResponseEntity<MockHttpServletResponse> responseEntity = new ResponseEntity<MockHttpServletResponse>(response, HttpStatus.OK);
		
		when(discoveryClient.getInstances(Mockito.anyString())).thenReturn(list);
		when(list.get(0).getUri()).thenReturn(uri);
		
		when(restTemplate.exchange(
				Matchers.any(),
				Mockito.eq(HttpMethod.GET),
				Matchers.any(HttpEntity.class),
				Matchers.<ParameterizedTypeReference<MockHttpServletResponse>>any()))
				.thenReturn(responseEntity);
	
		
		// Should not throw any exception.
		Assert.assertNull(tokenValidationFilter.run());
	}
	
	@Test
	public void filterShouldFilter() {
		Assert.assertEquals(true, tokenValidationFilter.shouldFilter());
	}
	
	@Test
	public void filterTypeShouldBeSetToPre() {
		Assert.assertEquals("pre", tokenValidationFilter.filterType());
	}

	@Test
	public void filterOrderShouldBeSetToOne() {
		Assert.assertEquals(1, tokenValidationFilter.filterOrder());
	}
}
