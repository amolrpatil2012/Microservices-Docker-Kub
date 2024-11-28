package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.demo.entities.Accounts;

import jakarta.transaction.Transactional;


public interface AccountsRepository extends JpaRepository<Accounts, Long> {

	Optional<Accounts> findByCustomerId(Long customerId);
	
	   @Transactional
	    @Modifying
	    void deleteByCustomerId(Long customerId);
	
}
