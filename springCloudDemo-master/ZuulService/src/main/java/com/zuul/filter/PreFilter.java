package com.zuul.filter;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class PreFilter extends ZuulFilter{


	@Override
	public boolean shouldFilter() {
		RequestContext context = getCurrentContext();
		
		if (context.contains("configisv1")||context.contains("retrieveConfiguration"))
			return false;

		return true;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}
	
	@Autowired
	DiscoveryClient discoveryClient;

	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public Object run() {
		
		 /*HttpServletRequest request = getCurrentContext().getRequest();
		 request.setAttribute("Authorization", "token 123");*/
		 String token = getCurrentContext().getRequest().getHeader("Authorization");
		
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity=new HttpEntity<>(headers);
		
		String url = discoveryClient.getInstances("hello-producer").get(0).getUri().toString();
		
		//restTemplate.exchange(url+"/"+token, HttpMethod.GET,httpEntity,String.class);
		restTemplate.exchange(url+"/hello", HttpMethod.GET,httpEntity,String.class);
	 
		 return null;
	}

}
