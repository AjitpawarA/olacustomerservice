package com.alpha.CustomerService.Dto;

public class RidefairDTO {
	private double distance;
	private double duration;
	private String vehicletype;
	private double price;
	public RidefairDTO(double distance, double duration, String vehicletype, double price) {
		super();
		this.distance = distance;
		this.duration = duration;
		this.vehicletype = vehicletype;
		this.price = price;
	}
	public RidefairDTO() {
		super();
	}
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public String getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
