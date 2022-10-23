# Account Service
***
One of the projects created as part of the "**Java Backend Developer**" course on the Jetbrains Academy platform. During this course, we are introduced to:
- object-oriented programming in Java,
- the use of a databases,
- the creation of web applications using the Spring Boot framework and commonly used libraries
- the use of a version control system, build tools like Gradle and familiarisation with the IntelliJ IDEA IDE.

#### About the project:

>Companies send out payrolls to employees using corporate mail. This solution has certain disadvantages related to security and usability. In this project, put on a robe of such an employee. As you're familiar with Java and Spring Framework, you've suggested an idea of sending payrolls to the employee's account on the corporate website. The management has approved your idea, but it will be you who will implement this project. You've decided to start by developing the API structure, then define the role model, implement the business logic, and, of course, ensure the security of the service.

#### Functions of the service:
*__Authentication functionality__*:
- **POST api/auth/signup** allows the user to register on the service
- **POST api/auth/changepass** changes a user password

*__Business functionality__*:

- **GET api/empl/payment** gives access to the employee's payrolls
- **POST api/acct/payments** uploads payrolls
- **PUT api/acct/payments** updates payment information

*__Service functionality__*:
- **PUT api/admin/user/role** changes user roles
- **DELETE api/admin/user/{user}** deletes a user
- **GET api/admin/user** displays information about all users

*__Security functionality__*:
- **PUT api/admin/user/access** lock/unlocks user
- **GET api/security/event** display security events of the service

*__Distribution of roles__*:
| | Anonymous | User | Accountant | Administrator | Auditor |
| --- |:---:|:---:|:---:|:---:|:---:|
| POST api/auth/signup | + | + | + | + | + |
| POST api/auth/changepass | - | + | + | + | - |
| GET api/empl/payment | - | + | + | + | - |
| POST api/acct/payments | - | - | + | - | - |
| PUT api/acct/payments | - | - | + | - | - |
| GET api/admin/user | - | - | - | + | - |
| DELETE api/admin/user/{user} | - | - | - | + | - |
| PUT api/admin/user/role | - | - | - | + | - |
| PUT api/admin/user/access | - | - | - | + | - |
| GET api/security/events | - | - | - | - | + |

#### JSON bodies for POST/PUT requests:
*__POST api/auth/signup__*:
```yaml
{
   "name": "<String value, not empty>",
   "lastname": "<String value, not empty>",
   "email": "<String value, not empty>",
   "password": "<String value, not empty>"
}
```

*__POST api/auth/changepass__*:
```yaml
{
   "new_password": "<String value, not empty>"
}
```

*__POST api/acct/payments__*:
```yaml
[
    {
        "employee": "<user email>",
        "period": "<mm-YYYY>",
        "salary": <Long>
    },
    {
        "employee": "<user1 email>",
        "period": "<mm-YYYY>",
        "salary": <Long>
    },
    ...
    {
        "employee": "<userN email>",
        "period": "<mm-YYYY>",
        "salary": <Long>
    }
]
```

*__PUT api/acct/payments__*:
```yaml
{
    "employee": "<user email>",
    "period": "<mm-YYYY>",
    "salary": <Long>
}
```

*__PUT api/admin/user/role__*:
```yaml
{
   "user": "<String value, not empty>",
   "role": "<User role>",
   "operation": "<[GRANT, REMOVE]>"
}
```

*__PUT api/admin/user/access__*:
```yaml
{
   "user": "<String value, not empty>",
   "operation": "<[LOCK, UNLOCK]>" 
}
```







