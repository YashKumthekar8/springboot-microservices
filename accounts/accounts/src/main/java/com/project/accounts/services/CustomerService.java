package com.project.accounts.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.accounts.dto.AccountsDTO;
import com.project.accounts.dto.CardsDTO;
import com.project.accounts.dto.CustomerDetailsDTO;
import com.project.accounts.entity.Accounts;
import com.project.accounts.entity.Customer;
import com.project.accounts.exception.ResourceNotFoundException;
import com.project.accounts.mapper.AccountsMapper;
import com.project.accounts.mapper.CustomerMapper;
import com.project.accounts.repository.AccountsRepository;
import com.project.accounts.repository.CustomerRepository;
import com.project.accounts.services.client.CardsClient;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class CustomerService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsClient cardsClient;

    public CustomerDetailsDTO fetchCustomerDetails(String mobileNumber, String correlationId){

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDTO customerDetailsDto = CustomerMapper.mapToCustomerDetailsDTO(customer, new CustomerDetailsDTO());
        customerDetailsDto.setAccountsDTO(AccountsMapper.mapToAccountsDTO(accounts, new AccountsDTO()));
    
        ResponseEntity<CardsDTO> cardsDTOResponse = cardsClient.fetchCardDetails(correlationId, mobileNumber);
        customerDetailsDto.setCardsDTO(cardsDTOResponse.getBody());

        return customerDetailsDto;
    }
    
}
