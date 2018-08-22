package com.zuul.filter;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.client.restcontroller.AmadeusResponse;
import com.netflix.zuul.ZuulFilter;



public class SessionFilter extends ZuulFilter {
	private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

	private static final String ACCESS_TOKEN_HEADER_KEY = "Authorization";
	private static final String TOKEN_NUMBER = "TokenNumber";
	private static final String SESSION_IDENTIFIER = "SessionIdentifier";
	private static final String LOCATION = "Location";
	private static final String CHANNEL = "Channel";
	private static final String SCOPE = "Scope";
	private static final String STATUS = "Status";
	private static final Integer FILTER_ORDER = 3;
	private static final String FILTER_TYPE = "pre";
	private static final boolean SHLOUD_FILTER = true;

	@Autowired
	DiscoveryClient discoveryClient;

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public String filterType() {
		return FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		return FILTER_ORDER;
	}

	@Override
	public boolean shouldFilter() {
		return SHLOUD_FILTER;
	}

	@Override
	public Object run() {
		HttpHeaders headers = new HttpHeaders();
		logger.info("ConfigController : " + MDC.get("X-B3-TraceId"));
		String url = discoveryClient.getInstances("GET-BOOKINGS").get(0).getUri().toString();

		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url+"/sessions")
		        .queryParam("countrycode", "GB")
		        .queryParam("channel", "Kiosk")
		        .queryParam("scope", "Booking");

	
		ResponseEntity<AmadeusResponse> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,AmadeusResponse.class);
		
		AmadeusResponse response = result.getBody();
		
		HttpServletRequest request = getCurrentContext().getRequest();
		
		//Removing Token
		if (null!=request.getAttribute(ACCESS_TOKEN_HEADER_KEY)){
			request.removeAttribute(ACCESS_TOKEN_HEADER_KEY);
		}
		
		request.setAttribute(TOKEN_NUMBER,response.getTokenNumber());
		request.setAttribute(SESSION_IDENTIFIER,response.getSessionIdentifier());
		request.setAttribute(LOCATION,response.getLocation());
		request.setAttribute(CHANNEL,response.getChannel());
		request.setAttribute(SCOPE,response.getScope());
		request.setAttribute(STATUS,response.getStatus());
		
		
		System.out.println((String)request.getAttribute(SESSION_IDENTIFIER));
		
		//getCurrentContext().setResponseBody((String)request.getAttribute(SESSION_IDENTIFIER));
		return null;

	}
}
