# Electronic Payment System

## Authors

- [@lara-petkovic](https://www.github.com/lara-petkovic)
- [@dusan-sudjic](https://www.github.com/dusan-sudjic)
- [@milica-vujic](https://www.github.com/MilicaVujic)

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
| **Frontend**     | Angular               | **Port**: `http://localhost:4200`                                       |

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