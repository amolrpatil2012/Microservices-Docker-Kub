package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.AccountsContactInfoDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.ResponseDto;
import com.example.demo.service.IAccountsService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping(value="api")
public class AccountsController {

	 private IAccountsService iAccountsService;

	 @Autowired
	 private AccountsContactInfoDto accountsContactInfoDto;
	 
	 @PostMapping("/create")
	 public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
	        iAccountsService.createAccount(customerDto);
	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(new ResponseDto( HttpStatus.CREATED.toString(), "Account Created Successfully!!"));
	    }
	 
	 @GetMapping("/fetch")
	 public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
	                                                               @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
	                                                               String mobileNumber) {
	        CustomerDto customerDto = iAccountsService.findAccount(mobileNumber);
	        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
	    }
	 
	 
	 @PutMapping("/update")
	    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
	        boolean isUpdated = iAccountsService.updateAccount(customerDto);
	        if(isUpdated) {
	            return ResponseEntity
	                    .status(HttpStatus.OK)
	                    .body(new ResponseDto(HttpStatus.OK.toString(),"Request Processed Successfully !!"));
	        }else{
	            return ResponseEntity
	                    .status(HttpStatus.EXPECTATION_FAILED)
	                    .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.toString(), "Operation Failed"));
	        }
	    }
	 
	 @DeleteMapping("/delete")
	    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
	                                                                @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
	                                                                String mobileNumber) {
	        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
	        if(isDeleted) {
	            return ResponseEntity
	                    .status(HttpStatus.OK)
	                    .body(new ResponseDto(HttpStatus.OK.toString(),"Request Processed Successfully !!"));
	        }else{
	            return ResponseEntity
	                    .status(HttpStatus.EXPECTATION_FAILED)
	                    .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.toString(), "Operation Failed"));
	        }
	    }
	 
	 	@GetMapping("/contact-info")
	    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body(accountsContactInfoDto);
	    }
}
