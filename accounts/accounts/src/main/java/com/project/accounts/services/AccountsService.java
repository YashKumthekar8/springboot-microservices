package com.project.accounts.services;


import com.project.accounts.constants.AccountsConstants;
import com.project.accounts.dto.AccountsDTO;
import com.project.accounts.dto.CustomerDTO;
import com.project.accounts.entity.Accounts;
import com.project.accounts.entity.Customer;
import com.project.accounts.exception.CustomerAlreadyExistsException;
import com.project.accounts.exception.ResourceNotFoundException;
import com.project.accounts.mapper.AccountsMapper;
import com.project.accounts.mapper.CustomerMapper;
import com.project.accounts.repository.AccountsRepository;
import com.project.accounts.repository.CustomerRepository;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsService {

    private static final Logger logger = LoggerFactory.getLogger(AccountsService.class);

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private MeterRegistry meterRegistry;

    public void createAccount(CustomerDTO customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            logger.warn("Customer already registered with mobileNumber: {}", customerDto.getMobileNumber());
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + customerDto.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
        Accounts newAccount = accountsRepository.save(createNewAccount(savedCustomer));
        logger.debug("Created new account with accountNumber: {} for customerId: {}",
                newAccount.getAccountNumber(), savedCustomer.getCustomerId());
        meterRegistry.counter("accounts.operations", "operation", "created").increment();
    }

    private Accounts createNewAccount(Customer customer){
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }

    public CustomerDTO fetchAccount(String mobileNumber){
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Accounts account = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDTO customerDTO = CustomerMapper.mapToCustomerDto(customer, new CustomerDTO());
        customerDTO.setAccountsDto(AccountsMapper.mapToAccountsDTO(account, new AccountsDTO()));

        return customerDTO;
    }

    public boolean updateAccount(CustomerDTO customerDTO){
        boolean isUpdated = false;
        AccountsDTO accountsDto = customerDTO.getAccountsDto();
        if(accountsDto !=null ){
            logger.debug("Updating account with accountNumber: {}", accountsDto.getAccountNumber());
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDTO,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        meterRegistry.counter("accounts.operations", "operation", "updated").increment();
        return  isUpdated;
    }

    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        logger.debug("Deleted account and customer records for customerId: {}", customer.getCustomerId());
        meterRegistry.counter("accounts.operations", "operation", "deleted").increment();
        return true;
    }

}
