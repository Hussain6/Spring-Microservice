package com.eazybytes.accounts.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.eazybytes.accounts.contants.AccountsContants;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExisitsException;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;

import com.eazybytes.accounts.service.IAccountsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {
	private AccountsRepository accountsRepository;
	private CustomerRepository customersRepository;

	@Override
	public void createAccount(CustomerDto customerDto) {
		Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
		Optional<Customer> optionalCustomer = customersRepository.findByMobileNumber(customerDto.getMobileNumber());
		if (optionalCustomer.isPresent()) {
			throw new CustomerAlreadyExisitsException(
					"Customer Already Exist with same phone number" + customerDto.getMobileNumber());
		}
		Customer savedCustomer = customersRepository.save(customer);
		accountsRepository.save(createNewAccount(savedCustomer));
	}

	private Accounts createNewAccount(Customer customer) {
		Accounts newAccount = new Accounts();
		newAccount.setCustomerId(customer.getCustomerId());
		long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

		newAccount.setAccountNumber(randomAccNumber);
		newAccount.setAccountType(AccountsContants.SAVINGS);
		newAccount.setBranchAddress(AccountsContants.ADDRESS);
		return newAccount;
	}

}
