# Electronic Payment System


## Authors

- [@lara-petkovic](https://www.github.com/lara-petkovic)
- [@dusan-sudjic](https://www.github.com/dusan-sudjic)
- [@milica-vujic](https://www.github.com/MilicaVujic)

# Application Configuration Table

| **App Name**   | **Configuration**     | **Details**                                                             |
|----------------|-----------------------|-------------------------------------------------------------------------|
| **WebShop**    | **Backend**           | ASP.NET Core API                                                       |
|                | **Backend Port (HTTP)**| `http://localhost:5275`                                                  |
|                | **Backend Port (HTTPS)**| `https://localhost:7098`                                                |
|                | **Frontend**          | Angular                                                                |
|                | **Frontend Port**     | `http://localhost:4200`                                                 |
|                | **Database**          | PostgreSQL                                                             |
|                | **Database Config**   | **Host**: `localhost`                                                   |
|                |                       | **Database**: `webshop`                                                |
|                |                       | **Username**: `postgres`                                               |
|                |                       | **Password**: `super`                                                  |
| **PaymentServiceProvider**   | **Backend**           | Spring                                                            |
|                | **Backend Port**      | `http://localhost:8085`                                                 |
|                | **Frontend**          | Angular                                                                  |
|                | **Frontend Port**     | `http://localhost:4201`                                                 |
|                | **Database**          | PostgreSQL                                                                  |
|                | **Database Config**   | **Host**: `localhost`                                                   |
|                |                       | **Database**: `psp`                                              |
|                |                       | **Username**: `postgres`                                                   |
|                |                       | **Password**: `super`                                            |
| **Bank Acquirer**   | **Backend**           | Spring Boot API                                                        |
|                | **Backend Port**      | `http://localhost:8052`                                                 |
|                | **Frontend**          | Angular                                                                  |
|                | **Frontend Port**     | `http://localhost:4202`                                                 |
|                | **Database**          | PostgreSQL                                                                |
|                | **Database Config**   | **Host**: `localhost`                                                   |
|                |                       | **Database**: `sepBankAcquirer`                                            |
|                |                       | **Username**: `postgres`                                                  |
|                |                       | **Password**: `super`                                              |
| **Bank Issuer**   | **Backend**           | Spring Boot API                                                        |
|                | **Backend Port**      | `http://localhost:8051`                                                 |
|                | **Frontend**          | /                                                                  |
|                | **Frontend Port**     | /                                                 |
|                | **Database**          | PostgreSQL                                                                |
|                | **Database Config**   | **Host**: `localhost`                                                   |
|                |                       | **Database**: `sepBankIssuer`                                            |
|                |                       | **Username**: `postgres`                                                  |
|                |                       | **Password**: `super`                                              |
| **Payment Card Center**   | **Backend**           | Spring Boot API                                                        |
|                | **Backend Port**      | `http://localhost:8053`                                                 |
|                | **Frontend**          | /                                                                  |
|                | **Frontend Port**     | /                                                 |
|                | **Database**          | PostgreSQL                                                                |
|                | **Database Config**   | **Host**: `localhost`                                                   |
|                |                       | **Database**: `sepPcc`                                            |
|                |                       | **Username**: `postgres`                                                  |
|                |                       | **Password**: `super`                                              |
| **Eureka Server**    | **Backend**           | Spring boot                                                       |
|                | **Backend Port (HTTP)**| `http://localhost:8761`                                                  |
| **Gateway PSP-WS**    | **Backend**           | Spring boot                                                      |
|                | **Backend Port (HTTP)**| `http://localhost:8086`                                                  |
| **Gateway PSP-Bank**    | **Backend**           | Spring boot                                                      |
|                | **Backend Port (HTTP)**| `http://localhost:8087`                                                  |
