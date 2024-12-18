package com.example.demo.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.demo.constants.AccountsConstants;
import com.example.demo.dtos.AccountsDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.entities.Accounts;
import com.example.demo.entities.Customer;
import com.example.demo.exceptions.CustomerAlreadyExistsException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mapper.AccountsMapper;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.repo.AccountsRepository;
import com.example.demo.repo.CustomerRepository;
import com.example.demo.service.IAccountsService;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Service
public class AccountsServiceImpl implements IAccountsService {

	private AccountsRepository accountsRepository;
	private CustomerRepository customerRepository;
	
	private Accounts createNewAccount(Customer customer) {
	        Accounts newAccount = new Accounts();
	        newAccount.setCustomerId(customer.getCustomerId());
	        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

	        newAccount.setAccountNumber(randomAccNumber);
	        newAccount.setAccountType(AccountsConstants.SAVINGS);
	        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
	        return newAccount;
	}
	
	@Override
	public void createAccount(CustomerDto customerDto) {
		Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
		Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
		
	}

	@Override
	public CustomerDto findAccount(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
	}

	@Override
	public boolean updateAccount(CustomerDto customerDto) {
		  boolean isUpdated = false;
	        AccountsDto accountsDto = customerDto.getAccountsDto();
	        if(accountsDto !=null ){
	            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
	                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
	            );
	            AccountsMapper.mapToAccounts(accountsDto, accounts);
	            accounts = accountsRepository.save(accounts);

	            Long customerId = accounts.getCustomerId();
	            Customer customer = customerRepository.findById(customerId).orElseThrow(
	                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
	            );
	            CustomerMapper.mapToCustomer(customerDto,customer);
	            customerRepository.save(customer);
	            isUpdated = true;
	        }
	        return  isUpdated;
	}

	@Override
	public boolean deleteAccount(String mobileNumber) {
		  Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
	                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
	        );
	        accountsRepository.deleteByCustomerId(customer.getCustomerId());
	        customerRepository.deleteById(customer.getCustomerId());
	        return true;
	}

}
