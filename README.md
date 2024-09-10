# projects.testing.junit-spring

## Things We Have Learned

* This needs to be run against the init file that LocalStack uses or else you will get a read file permission
  issue: `chmod +x init-localstack-setup.sh`
* We need to learn how Spring "beans", context, and lifecycle works and loads.
* Above is also true with how testing and Mock, MockBean, and Autowired works.

## Spring Testing

### Things we want to understand

* TestConfiguration
* Mock, MockBean, and Autowired

### Notes

* When there is a @Bean annotation within the application that is not satisfied correctly, the Spring application will
  fail to start.
* In the DatabaseConfig, the "default" profile having a NotImplementedException causes a similar issue.
* When there is a required @Bean you can add a @MockBean in the class denoted with either @TestConfiguration or
  @SpringBootTest.
* You cannot have duplicated @MockBean of the same class in both the @TestConfiguration and @SpringBootTest.
* If you do a @MockBean in either @TestConfiguration or @SpringBootTest, the class provided to the constructor will both
  be Mockito based and the same memory location for either @MockBean placement.
* If you do a @MockBean in either @TestConfiguration or @SpringBootTest and then do an @Autowired for the same class
  within the test, it is also the same memory location.
* It seems VERY hard to @Bean the TableSchema class such that it works in ClientDao as needed for the @MockBean side in
  testing. 

