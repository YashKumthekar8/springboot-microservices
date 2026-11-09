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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;


    public void createAccount(CustomerDTO customerDTO){
        Customer customer = CustomerMapper.mapToCustomer(customerDTO, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDTO.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with given mobile number " +
                    customerDTO.getMobileNumber());
        }

        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
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
        AccountsDTO accountsDTO = customerDTO.getAccountsDto();
        if(accountsDTO != null){
            Accounts accounts = accountsRepository.findById(accountsDTO.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDTO.getAccountNumber().toString())
            );

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDTO, customer);
            customerRepository.save(customer);

            AccountsMapper.mapToAccounts(accountsDTO, accounts);
            accountsRepository.save(accounts);

            isUpdated = true;
        }

        return isUpdated;
    }

    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

}
