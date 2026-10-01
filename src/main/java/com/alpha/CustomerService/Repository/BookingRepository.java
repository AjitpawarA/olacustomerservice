package com.alpha.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.CustomerService.Entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer>{
	
}
