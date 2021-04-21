package controller;




import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;
import model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DELL
 */

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class CustomerController {
    @Inject
    CustomerRepository customerRepository;
    
    @POST()
    @Path("/add")
    public Uni<Customer> addCustomerApi(Customer customer){
        return Uni.createFrom().publisher(addCustomer(customer));
    }
    
    private Mono<Customer> addCustomer(Customer customer){
        return customerRepository.findCustomerById(customer.getId())
                .switchIfEmpty(customerRepository.addCustomer(customer))
                .flatMap(customer1 -> Mono.error(new Throwable("Customer existed")));
    }
    
    @GET
    @Path("/get/{customerId}")
    public Uni<Customer> getCustomerByIdApi(@PathParam("customerId") String customerId) {
        return Uni.createFrom().publisher(getCustomerById(customerId));
    }
    
    private Mono<Customer> getCustomerById(String customerId){
        return customerRepository.findCustomerById(customerId);
    }
    
    @GET
    @Path("")
    public Multi<Customer> getAllCustomersApi(){
        return Multi.createFrom().publisher(getAllCustomers());
    }
    private Flux<Customer> getAllCustomers(){
        return customerRepository.findAllCustomers();
    }
    
}
