package com.example.demo.service;

import com.example.demo.dtos.CustomerDto;

public interface IAccountsService {

	void createAccount(CustomerDto customerDto);
	CustomerDto findAccount ( String mobileNumber);
	boolean updateAccount(CustomerDto customerDto);
	boolean deleteAccount(String mobileNumber);
}
