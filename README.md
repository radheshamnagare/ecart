# ecart
Ecart thrombones store

## Flow Architesture
```text
                 +-----------------------------+
                 |         ECART Application   |
                 |       (EcartApplication)    |
                 +-----------------------------+
                              │
                              ▼
                +-----------------------------+
                |           config            |
                |  Application Configurations |
                |  (CORS, Beans, Security)    |
                +-----------------------------+
                              │
                              ▼
+---------------------------------------------------------------+
|                           context                             |
|       Application-wide context, constants, utilities          |
+---------------------------------------------------------------+
                              │
                              ▼
+---------------------------------------------------------------+
|                           common                              |
|     Common utilities, helpers, DTO builders, exceptions       |
+---------------------------------------------------------------+
                              │
                              ▼
+---------------------------------------------------------------+
|                             bean                              |
|       Bean classes (custom objects used across modules)       |
+---------------------------------------------------------------+
                              │
                              ▼
+-------------------+       +-------------------+       +-------------------+
|     controller    | ----> |     service       | ----> |     repository    |
| REST Endpoints    |       | Business Logic    |       | Database Access   |
+-------------------+       +-------------------+       +-------------------+
         │                           │                           │
         │                           │                           │
         ▼                           ▼                           ▼
+-------------------+       +-------------------+       +-------------------+
|      request      |       |      model        |       |      entity       |
| Incoming DTOs     |       | Internal models   |       | JPA Entities      |
+-------------------+       +-------------------+       +-------------------+
         │                                                       ▲
         ▼                                                       │
+-------------------+                                            │
|     response      |                                            │
| Outgoing DTOs     | <------------------------------------------+
+-------------------+

                              ▼
                 +-----------------------------+
                 |         process             |
                 |   Internal processing logic |
                 +-----------------------------+

                              ▼
                 +-----------------------------+
                 |        restadviser          |
                 | Global Exception Handler    |
                 |  (@ControllerAdvice)        |
                 +-----------------------------+
