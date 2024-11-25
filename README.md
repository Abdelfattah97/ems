# EMS
EMS is an Employee Managment System served as a RESTful API.

### Project Description
This application enables CRUD operations over employees' information (such as name, salary, position, department, etc.) through a secure RESTful API. The application is designed to ensure that only authenticated users can interact with the API, requiring a JSON Web Token (JWT) for each request.

## Features

- **CRUD Operations**: Manage departments and employees with ease.
  - **Department Management**:
    - Add new departments.
    - Update existing departments.
    - Find departments by their ID.
    - Fetch a paginated list of department records.
  - **Employee Management**:
    - Add new employees.
    - Update existing employees.
    - Find employees by their ID.
    - Fetch a paginated list of employee records.

## API Usage
API documentation can be found at ```/doc```

---
### Roles and Authorizations
##### The application defines two main roles with the following authorizations
1. **admin**
- Full access to all HTTP methods (Create, Read, Update, Delete).
- Can perform all CRUD operations on employee records.
2. **user**
- Read-only access via HTTP GET requests.
- Can view employee information but cannot modify or delete records.
---
- ### Requesting a token

Try it with the following credentials

- **username**  
\- "user" — has the role user.  
\- "admin" — has the role admin.

- **password**    
\- "password" for both users.

at ```/swagger-ui/index.html#/Authentication/login```

## Technologies
The following technologies and libraries are used in developing the system

1. **Java** the primary programming language used for this project.
2. **Spring Boot** The core framework providing auto-configuration, dependency management, and streamlined integration with other Spring-based technologies.
3. **Lombok** A library that reduces boilerplate code by generating common methods such as getters, setters, constructors, builders, and equalshashCode methods through annotations.

4. **PostgreSQL** An open-source relational database management system (RDBMS) used to store and manage application data.

5. **Spring Data JPA & Hibernate**
  - **JPA** (Java Persistence API) A specification for object-relational mapping (ORM) in Java, enabling seamless mapping between Java objects and database tables.
  - **Hibernate** an implementation provider for the JPA specifications and has a wide compatibility with various databases.
  - **Spring JPA** Simplifies the development of database access layers by abstracting boilerplate code and providing a repository-based approach to database operations.
6. **Flyway** A database migration tool that provides version control, repeatable schema migrations, and consistency across multiple environments.
7. **Hibernate Validator** A library for defining and enforcing validation constraints on data models. It supports annotations to validate input data and ensure consistency.
8. **Spring Security** is a framework with powerful and highly customizable authentication and access-control.
9. **JJWT** A library used to create and validate JWTs, ensuring secure and stateless authentication.
10. **Junit** A popular framework for writing and running unit tests in Java.
11. **Mockito** A testing framework for mocking dependencies in unit tests, allowing isolation of components.
## Installation
1. Clone the repository
```bash
cd your_desired_dir
git clone httpsgithub.comabdelfattah97ems.git
cd ems
```
2. Build the project
``` bash
mvnw clean package
```
3. Start the application with Docker Compose
``` bash
docker-compose up --build
```
4. Access the application
Application [http://localhost:8080/doc](http://localhost:8080/doc)
