package com.example;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/*****************
 * RouteImplementation
 * Description: Contains the routes that integrates with the sample Employee
 * database.
 *
 * The /employee endpoints uses a processor to transform the response payload
 * using DataWeave. Two (2)
 * types of DW transformation methods are used below -- DW embedded in Java, and
 * the other is DW from
 * an external file.
 *
 * @author Andie Azucena
 */
@Component
public class RouteImpl extends RouteBuilder {

  /******
   * TODO
   * - Refactor. All endpoints (except hello) uses the same processor. Make this
   * cleaner.
   * - Implement other HTTP methods
   * - Implement error handlers
   */

  @Override
  public void configure() throws Exception {

    // Hello World
    from("direct:hello")
        .routeId("direct-hello")
        .log("Hello World")
        .process(new Processor() {
          public void process(Exchange ex) throws Exception {
            DataTransformer dt = new DataTransformer();
            ex.getIn().setBody(dt.transform("Hello World"));
          }
        });

    // Get all employees
    from("direct:get-employees")
        .log("Retrieving all employees... ")
        .to("sql:classpath:sql/select-employees.sql")
        .process(new Processor() {
          public void process(Exchange ex) throws Exception {
            DataTransformer dt = new DataTransformer();
            ex.getIn().setBody(
                dt.transformFromFile(
                    ResourceUtils.getFile("classpath:dataweave/get-employees.dwl"),
                    ex));
          }
        })
        .to("log:like-to-see-all?level=INFO&showAll=true&multiline=true");

    // Get an employee given an ID
    from("direct:get-employee-by-id")
        .log("Retrieving employee with ID: ${header.id}... ")
        .to("sql:classpath:sql/select-employees-by-id.sql")
        .choice()
        .when(header("CamelSqlRowCount").isGreaterThan(0))
        .transform(simple("${body[0]}"))
        .process(new Processor() {
          public void process(Exchange ex) throws Exception {
            DataTransformer dt = new DataTransformer();
            ex.getIn().setBody(
                dt.transformFromFile(
                    ResourceUtils.getFile("classpath:dataweave/get-employee.dwl"),
                    ex));
          }
        })
        .to("log:like-to-see-all?level=INFO&showAll=true&multiline=true");

    // Insert employee to table from POST request
    from("direct:insert-employee")
        .log("Inserting to employee table... ")
        .to("log:like-to-see-all?level=INFO&showAll=true&multiline=true")
        .to("sql:classpath:sql/insert-employee.sql");

  }

}
