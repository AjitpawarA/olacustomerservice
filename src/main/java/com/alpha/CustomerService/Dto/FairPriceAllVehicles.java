package com.alpha.CustomerService.Dto;

public class FairPriceAllVehicles {
	private long rideId; 
	private String pickupLocation;
	private String destinationLocation;
	private double distance;
	private double duration;
	private double bikePrice;
	private double autoPrice;
	private double carPrice;
	public FairPriceAllVehicles() {
		super();
	}
	public FairPriceAllVehicles(long rideId, String pickupLocation, String destinationLocation, double distance,
			double duration, double bikePrice, double autoPrice, double carPrice) {
		super();
		this.rideId = rideId;
		this.pickupLocation = pickupLocation;
		this.destinationLocation = destinationLocation;
		this.distance = distance;
		this.duration = duration;
		this.bikePrice = bikePrice;
		this.autoPrice = autoPrice;
		this.carPrice = carPrice;
	}
	public long getRideId() {
		return rideId;
	}
	public void setRideId(long rideId) {
		this.rideId = rideId;
	}
	public String getPickupLocation() {
		return pickupLocation;
	}
	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}
	public String getDestinationLocation() {
		return destinationLocation;
	}
	public void setDestinationLocation(String destinationLocation) {
		this.destinationLocation = destinationLocation;
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
	public double getBikePrice() {
		return bikePrice;
	}
	public void setBikePrice(double bikePrice) {
		this.bikePrice = bikePrice;
	}
	public double getAutoPrice() {
		return autoPrice;
	}
	public void setAutoPrice(double autoPrice) {
		this.autoPrice = autoPrice;
	}
	public double getCarPrice() {
		return carPrice;
	}
	public void setCarPrice(double carPrice) {
		this.carPrice = carPrice;
	}
	
	
}
