# MQP(Mail Query Protocol)

![logo](https://github.com/rezafarazi/MQP-Mail-Query-Protocol-/assets/45543047/36f3d9c9-21ec-483e-b96b-54c1fadfa0a4)

MQP (Mail Query Protocol) Core 

# MPQ (Mail Query protocol)
### A network protocol for sending and receiving messages and Mails over the Internet

# How setup MQP?
### 1 - Install JAVA9 on your server

https://www.oracle.com/java/technologies/javase/javase9-archive-downloads.html

### 2 - Create an export database from "Database/MySql/mqp.sql" file

![image](https://github.com/rezafarazi/MQP-Mail-Query-Protocol-/assets/45543047/594b66ec-8665-4dff-88c4-9808166caffa)

### 3 - Clone MQP from github or download an relase from github
```
git clone https://github.com/rezafarazi/MQP-Mail-Query-Protocol-.git
```
### 4 - Setup prots and paths on "Protocol.properties" file

![x](https://github.com/rezafarazi/MQP-Mail-Query-Protocol-/assets/45543047/c4e754d0-547d-4e21-9073-91e1a0333a00)

### 5 - Setup network encription pattern on "EncriptList.json" file

![code](https://github.com/rezafarazi/MQP-Mail-Query-Protocol-/assets/45543047/b9395193-2849-4c95-8cf6-b7a45ba7b4a8)

### 6 - Start MQP on your vps or server

![image](https://github.com/rezafarazi/MQP-Mail-Query-Protocol-/assets/45543047/dd1fed6d-7d1e-424c-a3e1-29035bdffc95)

# Http Api
### Login
```
/Login -> POST
  body:
    username -> String
    password -> String
```

### Signup
```
/Signup -> POST
  body:
    name -> String
    family -> String
    username -> String
    password -> String
    email -> String
    phone -> String
```

### Check user exist
```
/CheckUser -> POST
  body:
    address -> String
```

### List of user mails
```
/GetMailBox -> POST
  header:
    Auth -> Token
```

### Get all mails with an user
```
/GetMailsOfUser -> POST
  header:
    Auth -> Token
  body:
    username -> username@domain
```

### Send mail
```
/SendMail -> POST
  header:
    Auth -> Token
  body:
    title -> String
    content -> String
    address -> String
```

### Update mail
```
/SendMail -> POST
  header:
    Auth -> Token
  body:
    title -> String
    content -> String
    address -> String
    mail_id -> Integer
```

### Delete mail
```
/DeleteMail -> POST
  header:
    Auth -> Token
  body:
    address -> String
    MAILIDMAILID -> Integer
```

### Seen mail
```
/SeenMail -> POST
  header:
    Auth -> Token
  body:
    address -> String
    MAILIDMAILID -> Integer
```

### File upload
```
/SeenMail -> POST
  header:
    Auth -> Token
  body:
    file -> Multi part file
```
