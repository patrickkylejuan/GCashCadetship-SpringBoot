# Yellowdit

Yellowdit is a blogging application that only contains great stuff that matters.

## API

Enables client application to manage users, contents, and reviews.

### User Management (/user)
CRUD for users. [Sample collections][postman] of API call that can be imported in postman for testing.

#### Register a user
Register a user. All fields are required.
```json
POST /
Header - Reference-Number: 123
{
    "email": "jdoe@apper.ph", //string|email
    "first_name": "John",
    "last_name": "Doe",
    "password": "$trong",
    "birth_date": "2000-12-13" //string|ISO-DATE-FORMAT
}
```
Response/s
```json
201 CREATED
{
    "verification_code": "uPHo9e"
}
```
```json
400 BAD_REQUEST
{
    "message": "email already registered"
}
```

#### Verify a user
Verify a registered user. All fields are required.
```json
POST /verify
Header - Reference-Number: 123
{
    "email": "jdoe@apper.ph",
    "verification_code": "uPHo9e"
}
```
Response/s
```json
200 OK
{
    "message": "verification success"
}
```
```json
400 BAD_REQUEST
{
    "message": "email already registered"
}
```
```json
400 BAD_REQUEST
{
    "message": "Invalid verification request"
}
```

#### Login
Authenticate an active user. All fields are required.
```json
POST /login
Header - Reference-Number: 123
{
    "email": "jdoe@apper.ph",
    "password": "$trong"
}
```
Response/s
```json
200 OK
{
    "id": "97e8e651-1363-41fe-8cb0-13720c144291",
    "email": "jdoe@apper.ph",
    "first_name": "John",
    "last_name": "Doe",
    "birth_date": "2000-12-13",
    "date_registered": "2021-02-14T14:21:35.387465",
    "date_verified": "2021-02-14T14:21:56.37356",
    "last_login": "2021-02-14T14:22:04.808352",
    "is_verified": true,
    "is_active": true
}
```
```json
400 BAD_REQUEST
{
    "message": "Invalid login credentials"
}
```
```json
400 BAD_REQUEST
{
    "message": "password: password is required"
}
```
#### Get all users
Retrieve all verified and active users
```json
GET /
```
Response/s
```json
200 OK (zero result)
[]
```
```json
200 OK
[
    {
        "id": "25f59db2-b2ff-4b33-be97-10c31217ae11",
        "email": "jdoe@apper.ph",
        "first_name": "John",
        "last_name": "Doe",
        "birth_date": "2000-12-13",
        "date_registered": "2021-02-14T15:44:44.175223",
        "date_verified": "2021-02-14T15:44:51.785636",
        "last_login": null,
        "is_verified": true,
        "is_active": true
    },
    {
        "id": "b6893356-6510-4067-a2f5-e4a50dd98e28",
        "email": "mreyes@apper.ph",
        "first_name": "Maria",
        "last_name": "Reyes",
        "birth_date": "2000-12-13",
        "date_registered": "2021-02-14T15:45:32.323657",
        "date_verified": "2021-02-14T15:45:45.240627",
        "last_login": null,
        "is_verified": true,
        "is_active": true
    }
]
```
#### Get a user
All fields are required.
```json
GET /{user_id}
```
Response/s
```json
200 OK
{
    "id": "25f59db2-b2ff-4b33-be97-10c31217ae11",
    "email": "jdoe@apper.ph",
    "first_name": "John",
    "last_name": "Doe",
    "birth_date": "2000-12-13",
    "date_registered": "2021-02-14T15:44:44.175223",
    "date_verified": "2021-02-14T15:44:51.785636",
    "last_login": "2021-02-14T15:51:05.584079",
    "is_verified": true,
    "is_active": true
}
```
```json
404 NOT FOUND (No body)
```

#### Delete a user
Soft deletes a user. Deactivates a user.
```json
DELETE /{user_id}
Header - Reference-Number: 123
```
Response/s
```json
200 OK
{
    "message": "delete user success"
}
```
```json
400 BAD_REQUEST
{
    "message": "email already registered"
}
```
```json
404 NOT FOUND (No body)
```

#### Update user details
Verify a registered user. All fields are required.
```json
PATCH /{user_id}
Header - Reference-Number: 123
{
    "first_name": "Eren",
    "last_name": "Yeager",
    "birth_date": "1999-12-12".
    "password": "$tronger",
    "is_active": false
}
```
Response/s
```json
200 OK
{
    "message": "update success"
}
```
```json
400 BAD_REQUEST
{
    "message": "age must be at least 18"
}
```

### Blog Management (/blog)

CRUD for users. [Sample collections][postman] of API call that can be imported in postman for testing.

#### Create a new Blog

Create a new blog. All fields are required. The user must be verified and active.

```json
POST /
Header - Reference-Number: 123
{
    "title": "My First Blog", 
    "content": "This is my first blog",
    "user_id": "5ab04aa9-ea27-45e0-8672-b668660dabae" //user id must be part of the system, is verified and active

}
```

Response/s

```json
201 CREATED
{
    "blog_id": "de3d0d2e-46b3-4eb1-a4a4-7881d60060f4"
}
```

```json
400 BAD_REQUEST
{
    "message": "user is either unverified or inactive. pls try again"
}
```



#### Get all blogs (deleted or not deleted)

Retrieve all blogs with request parameter ?all= true or false. If all = true, all blog posts in the database will be shown. If all = false, only visible blogs are shown.

```json
GET /?all=true
```

Response/s

```json
200 OK (zero result)
[]
```

```json
200 OK
[
    [
    {
        "title": "My First Blog",
        "content": "This is my first blog",
        "blog_id": "3cee92b0-fc3a-4fdb-a857-c698f818606a",
        "user_id": "99626675-172c-4006-8674-754649d11bda",
        "date_publish": "2021-02-15T20:37:03.432617",
        "last_updated": "2021-02-15T20:37:03.432617",
        "is_visible": true
    },
    {
        "title": "My Second Blog",
        "content": "This is my second blog",
        "blog_id": "e817c194-ee7a-419a-9c72-6ecdac0c704c",
        "user_id": "99626675-172c-4006-8674-754649d11bda",
        "date_publish": "2021-02-15T20:38:00.858134",
        "last_updated": "2021-02-15T20:38:00.858134",
        "is_visible": false
    }
]
]
```



```json
GET /?all=false
```

Response/s

```json
200 OK (zero result)
[]
```

```json
200 OK
[
    {
        "title": "My First Blog",
        "content": "This is my first blog",
        "blog_id": "3cee92b0-fc3a-4fdb-a857-c698f818606a",
        "user_id": "99626675-172c-4006-8674-754649d11bda",
        "date_publish": "2021-02-15T20:37:03.432617",
        "last_updated": "2021-02-15T20:37:03.432617",
        "is_visible": true
    }
]
```



#### Get a blog

All fields are required.

```json
GET /{blog_id}
```

Response/s

```json
200 OK
{
    "title": "My First Blog",
    "content": "This is my first blog",
    "blog_id": "5a96feea-9a73-41e3-9f04-1df13563a0bf",
    "user_id": "8ec53a1e-2192-4c56-ac98-cf2095fd414a",
    "date_publish": "2021-02-15T19:52:02.882913",
    "last_updated": "2021-02-15T19:52:02.882913",
    "is_visible": true
}
```

```json
404 NOT FOUND (No body)
```

#### Delete a blog post

Soft deletes a blog post by changing their visibility to hidden.

```json
DELETE /{blog_id}
Header - Reference-Number: 123
```

Response/s

```json
200 OK
{
    "message": "delete blog post success"
}
```

```json
404 NOT FOUND (No body)
```

#### Update blog post

Update blog post contents. All fields are required.

```json
PATCH /{blog_id}
Header - Reference-Number: 123
{
    "title": "My Updated Blog", 
    "content": "This is my updated blog"
   
}
```

Response/s

```json
200 OK
{
    "message": "update blog success"
}
```





[postman]: <https://www.getpostman.com/collections/f716f0bf946b13831de8>
