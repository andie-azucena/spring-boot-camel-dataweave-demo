package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/***********
 * BaseRouter
 * Description: Contains REST configuration and base REST endpoints.
 * 
 * @author Andie Azucena
 */
@Component
public class BaseRouter extends RouteBuilder {

	@Value("${greeting:hello default}")
	private String greeting;
	
    @Override
    public void configure() {
    	
    	restConfiguration()
    		.component("undertow")
    		.bindingMode(RestBindingMode.auto);

		// Hello World sample
    	rest()
	        .consumes("application/json")
	        .produces("application/json")    		        
			.get("/hello")
				.routeId("get-hello")
                .to("direct:hello");			
		
	    // Sample employees API
    	rest()
			.path("/employees")
	        .consumes("application/json")
	        .produces("application/json")    	

	        .get("/")
		    	.id("GET /sql/employees")
		    	.description("Retrieves all employees from DB using sql endpoint")
		    	.to("direct:get-employees")
		    	
	        .get("/{id}")
	        	.id("GET /sql/employees/{id}")
	        	.description("Retrieves an employee from DB using sql endpoint")
	        	.to("direct:get-employee-by-id")

        	.post()
	    		.id("POST /sql/employees")
	        	.description("Saves an employee to DB using sql endpoint")
	    		.to("direct:insert-employee");
    	    	
    }

}
