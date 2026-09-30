package com.alpha.CustomerService.Dto;

import com.alpha.CustomerService.Entity.Cordinate;

public class SelectRideDTO {
	private int custid;
	private Cordinate sourcelocation;
	private  Cordinate destinationlocation;
	public SelectRideDTO(int custid,Cordinate sourcelocation, Cordinate destinationlocation) {
		super();
		this.custid=custid;
		this.sourcelocation = sourcelocation;
		this.destinationlocation = destinationlocation;
	}
	public SelectRideDTO() {
		super();
	}
	public Cordinate getSourcelocation() {
		return sourcelocation;
	}
	public void setSourcelocation(Cordinate sourcelocation) {
		this.sourcelocation = sourcelocation;
	}
	public Cordinate getDestinationlocation() {
		return destinationlocation;
	}
	public void setDestinationlocation(Cordinate destinationlocation) {
		this.destinationlocation = destinationlocation;
	}
	public int getCustid() {
		return custid;
	}
	public void setCustid(int custid) {
		this.custid = custid;
	}
	
}
