package com.client.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/bookings")
@RequestMapping("/sessions")
public class HelloClient {

	@Autowired
	DiscoveryClient discoveryClient;


	@GetMapping()
	//@GetMapping({"/check_token/{countrycode}/{channel}/{scope}"})
//	public String bookings() {
	public ResponseEntity<AmadeusResponse> getSession(@RequestParam("countrycode") String countrycode,
            @RequestParam("channel") String channel, @RequestParam("scope") String scope ) {
		AmadeusResponse amares = new AmadeusResponse();
		//String url = discoveryClient.getInstances("get-booking").get(0).getUri().toString();
		if (null != countrycode & countrycode.contains("GB") && null != channel & channel.contains("Kiosk") && null != scope & scope.contains("Booking") ) {
		
		//RestTemplate restTemplate=new RestTemplate();
		
		amares.setTokenNumber("17RN4AL");
		amares.setSessionIdentifier("00DTN6VVNM");
		amares.setLocation("GB");
		amares.setChannel("Kiosk");
		amares.setScope("Booking");
		amares.setStatus("VALID");
		
		//return restTemplate.getForObject(url+"/booking"+, String.class);
		return new ResponseEntity<AmadeusResponse>(amares, HttpStatus.OK);
		
		
		}
		System.out.println("Not Get session");
		return new ResponseEntity<AmadeusResponse>(amares, HttpStatus.BAD_REQUEST);

	}
}
