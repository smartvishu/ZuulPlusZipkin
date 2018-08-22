package com.client.restcontroller;

public class AmadeusResponse {
	String tokenNumber;
	String sessionIdentifier;
	String location;
	String channel;
	String scope;
	String status;

	public String getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(String tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public String getSessionIdentifier() {
		return sessionIdentifier;
	}

	public void setSessionIdentifier(String sessionIdentifier) {
		this.sessionIdentifier = sessionIdentifier;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	@Override
	public String toString() {
		return "AmadeusResponse [tokenNumber=" + tokenNumber + ", sessionIdentifier=" + sessionIdentifier
				+ ", location=" + location + ", channel=" + channel + ", scope=" + scope + ", status=" + status + "]";
	}
	
	
}
