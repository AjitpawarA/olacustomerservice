package com.alpha.CustomerService.Dto;

public class SearchDestinationResponeDto {
	private String adderss;
	private double latitude;
	private double longitude;
	public SearchDestinationResponeDto(String adderss, double latitude, double longitude) {
		super();
		this.adderss = adderss;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public SearchDestinationResponeDto() {
		super();
	}
	public String getAdderss() {
		return adderss;
	}
	public void setAdderss(String adderss) {
		this.adderss = adderss;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
