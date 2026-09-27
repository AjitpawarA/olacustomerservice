package com.alpha.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.CustomerService.Entity.Cordinate;

public interface Cardinationrepository extends JpaRepository<Cordinate, Integer>{

}
