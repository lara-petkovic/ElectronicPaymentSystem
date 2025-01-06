# Electronic Payment System

## Project Description
The Electronic Payment System is a comprehensive application that enables users to purchase telecommunication services through a web shop. The platform offers services like mobile phone, fixed-line telephone, internet, and digital TV, with various tariff packages tailored for private and business customers.

Users can register, log in, and choose from a variety of services. Upon selecting the desired service, they are redirected to the Payment Service Provider (PSP) to complete the transaction via multiple payment options such as credit/debit cards, PayPal, or cryptocurrency (Bitcoin).

The Payment Service Provider (PSP) acts as an intermediary between the WebShop and payment gateways, allowing the system to support a variety of payment methods. The architecture ensures high availability, scalability, and security, incorporating PCI DSS standards and implementing features like Single Sign-On (SSO) and penetration testing.

The system is designed to be loosely coupled and pluggable, ensuring easy integration of new payment methods and clients.

---

## Application Configuration Table

### **WebShop**
| **Component**    | **Configuration**     | **Details**                                                             |
|------------------|-----------------------|-------------------------------------------------------------------------|
| **Backend**      | ASP.NET Core API      | **Port**: `http://localhost:5275` (HTTP) / `https://localhost:7098` (HTTPS) |
|                  |                       | **Database**: PostgreSQL                                                |
|                  | **Database Config**   | **Host**: `localhost`                                                   |
|                  |                       | **Database**: `webshop`                                                 |
|                  |                       | **Username**: `postgres`                                                |
|                  |                       | **Password**: `super`                                                   |
| **Frontend**     | Angular               | **Port**: `https://localhost:4200`                                       |

---

### **Payment Service Provider**
| **Component**    | **Configuration**     | **Details**                                                             |
|------------------|-----------------------|-------------------------------------------------------------------------|
| **Backend**      | Spring Boot API       | **Port**: `http://localhost:8085`                                       |
|                  |                       | **Database**: PostgreSQL                                                |
|                  | **Database Config**   | **Host**: `localhost`                                                   |
|                  |                       | **Database**: `psp`                                                     |
|                  |                       | **Username**: `postgres`                                                |
|                  |                       | **Password**: `super`                                                   |
| **Frontend**     | Angular               | **Port**: `http://localhost:4201`                                       |

---

### **Bank Acquirer**
| **Component**    | **Configuration**     | **Details**                                                             |
|------------------|-----------------------|-------------------------------------------------------------------------|
| **Backend**      | Spring Boot API       | **Port**: `http://localhost:8052`                                       |
|                  |                       | **Database**: PostgreSQL                                                |
|                  | **Database Config**   | **Host**: `localhost`                                                   |
|                  |                       | **Database**: `sepBankAcquirer`                                         |
|                  |                       | **Username**: `postgres`                                                |
|                  |                       | **Password**: `super`                                                   |
| **Frontend**     | Angular               | **Port**: `http://localhost:4202`                                       |

---

### **Bank Issuer**
| **Component**    | **Configuration**     | **Details**                                                             |
|------------------|-----------------------|-------------------------------------------------------------------------|
| **Backend**      | Spring Boot API       | **Port**: `http://localhost:8051`                                       |
|                  |                       ||**Database**     : PostgreSQL                                           |
|                  | **Database Config**   | **Host**: `localhost`                                                   |
|                  |                       | **Database**: `sepBankIssuer`                                           |
|                  |                       | **Username**: `postgres`                                                |
|                  |                       | **Password**: `super`                                                   |

---

### **Payment Card Center**
| **Component**    | **Configuration**     | **Details**                                                             |
|------------------|-----------------------|-------------------------------------------------------------------------|
| **Backend**      | Spring Boot API       | **Port**: `http://localhost:8053`                                       |
|                  |                       || **Database** : PostgreSQL                                              |
|                  | **Database Config**   | **Host**: `localhost`                                                   |
|                  |                       | **Database**: `sepPcc`                                                  |
|                  |                       | **Username**: `postgres`                                                |
|                  |                       | **Password**: `super`                                                   |

---

### **PayPal**
| **Component**    | **Configuration**     | **Details**                                                             |
|------------------|-----------------------|-------------------------------------------------------------------------|
| **Backend**      | Spring Boot API       | **Port**: `http://localhost:8088`                                       |
|                  |                       | **Database**: PostgreSQL                                                |
|                  | **Database Config**   | **Host**: `localhost`                                                   |
|                  |                       | **Database**: `paypal`                                                  |
|                  |                       | **Username**: `postgres`                                                |
|                  |                       | **Password**: `super`                                                   |
| **Frontend**     | Angular               | **Port**: `http://localhost:4203`                                       |

---

### **Servers and Gateways**
| **Component**    | **Configuration**     | **Details**                                                             |
|------------------|-----------------------|-------------------------------------------------------------------------|
| **Eureka Server**      | Spring Boot API       | **Port**: `http://localhost:8761`                                 |
| **Gateway PSP-WS**      | Spring Boot API       | **Port**: `http://localhost:8086`                                |
| **Gateway PSP-Bank**      | Spring Boot API       | **Port**: `http://localhost:8087`                              |

---

### **How to Run the Project**

1. **Start WebShop (Backend and Frontend)**:
   - Ensure the database is empty, then run migrations:
     ```
     Add-Migration init
     Update-Database
     ```
   - Start the backend (`dotnet run`).
   - Start the frontend (Angular app).

2. **Start the Other Services and their Frontend applications**:
   - For each Spring Boot application, navigate to the corresponding folder and run:
     ```
     mvn spring-boot:run
     ```
   - Start `Eureka`, `PSP`, `Bank Acquirer`, `Bank Issuer`, `PCC` and `PayPal`.
   - Like for the `WebShop` frontend, you should also start the frontend apps for the `PSP`, `Bank Issuer`, `PCC`, `PayPal` and `Bitcoin`.

3. **Access the Application**:
   - The WebShop frontend is available at `https://localhost:4200`.
   - Use the services and test payments.

## Authors
- [@lara-petkovic](https://www.github.com/lara-petkovic)
- [@dusan-sudjic](https://www.github.com/dusan-sudjic)
- [@milica-vujic](https://www.github.com/MilicaVujic)