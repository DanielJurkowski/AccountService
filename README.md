# Account Service
***
One of the projects created as part of the "**Java Backend Developer**" course on the Jetbrains Academy platform. During this course, we are introduced to:
- object-oriented programming in Java,
- the use of a databases, 
- the creation of web applications using the Spring Boot framework and commonly used libraries,
- the use of a version control system, build tools like Gradle and familiarisation with the IntelliJ IDEA IDE. 

### About the project:

>Companies send out payrolls to employees using corporate mail. This solution has certain disadvantages related to security and usability. In this project, put on a robe of such an employee. As you're familiar with Java and Spring Framework, you've suggested an idea of sending payrolls to the employee's account on the corporate website. The management has approved your idea, but it will be you who will implement this project. You've decided to start by developing the API structure, then define the role model, implement the business logic, and, of course, ensure the security of the service.

### Functions of the service:
*__Authentication__*:
- **POST api/auth/signup** allows the user to register on the service
- **POST api/auth/changepass** changes a user password

*__Business functionality__*:

- **GET api/empl/payment** gives access to the employee's payrolls
- **POST api/acct/payments** uploads payrolls
- **PUT api/acct/payments** updates payment information

*__Service functionality__*:
- **PUT api/admin/user/role** changes user roles
- **DELETE api/admin/user** deletes a user
- **GET api/admin/user** displays information about all users

*__Distribution of roles__*:
| | Anonymous | User | Accountant | Administrator |
| --- |:---:|:---:|:---:|:---:|
| POST api/auth/signup | + | + | + | + |
| POST api/auth/changepass | - | + | + | + |
| GET api/empl/payment | - | + | + | + |
| POST api/acct/payments | - | - | + | - |
| PUT api/acct/payments | - | - | + | - |
| GET api/admin/user | - | - | - | + |
| DELETE api/admin/user | - | - | - | + |
| PUT api/admin/user/role | - | - | - | + |
