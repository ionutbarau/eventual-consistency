package com.lowheap.poc.eventualconsistency.customerservice.controllers;

import com.lowheap.poc.eventualconsistency.customerservice.client.dto.CustomerDto;
import com.lowheap.poc.eventualconsistency.customerservice.model.Customer;
import com.lowheap.poc.eventualconsistency.customerservice.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Slf4j
@RequestMapping(path = "/customers/")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> register(@RequestBody CustomerDto customerDto) {
        Customer customer = customerService.createPendingCustomerRegistration(modelMapper.map(customerDto, Customer.class));
        log.info("Saved customer {}", customer);
        return new ResponseEntity<>(modelMapper.map(customer, CustomerDto.class), OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerDto> findById(@PathVariable("id") String id) {
        Customer customer = customerService.findById(id);
        log.info("Found customer {}", customer);
        return new ResponseEntity<>(modelMapper.map(customer, CustomerDto.class), OK);

    }
}
