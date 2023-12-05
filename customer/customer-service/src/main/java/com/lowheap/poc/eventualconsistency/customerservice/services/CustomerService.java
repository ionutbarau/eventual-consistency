package com.lowheap.poc.eventualconsistency.customerservice.services;

import com.lowheap.poc.eventualconsistency.customerservice.model.Customer;

public interface CustomerService {
    Customer createPendingCustomerRegistration(Customer customer);

    void approveRegistration(String customerId);

    void rejectRegistration(String customerId, String reason);

    Customer findById(String id);
}
