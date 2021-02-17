<h1> Blog API Documentation</h1>

This is a short documentation on the blog API creation activity for day 2 of GCash Cadetship: Introduction to Spring Boot.

Author: Patrick Juan
Coach: Orvyl Tumaneng

<h2> 1.Create Blog API</h2>

This API enables the creation of a new blog post with the sending of a POST request with the following fields: *title*, *content*, *datePublished* and *author*.
It requires <u>all the fields</u> to be not blank and the format of *datePublished* to comply to ISO standards (DDD-MM-YY).
If the request goes through successfully, a response containing the system generated blog ID is sent back.

<h3> Success request and response example
**Request**:

```JSON

{
    "title": "My First Blog",
    "content": "This is my first blog",
    "datePublished": "2020-01-01",
    "author": "Patrick Juan"
}

```

**Response**:

~~~json
{
    "id": "146273e2-12d3-46f9-8a8a-d3e850b6d867" #system generated ID
}
~~~

<h3> Failed request and response example </h3>

**Request**:

```JSON
{
    "title": "",
    "content": "This is my first blog",
    "datePublished": "2020-01-01",
    "author": "Patrick Juan"
}

```

**Response**:

~~~json

    {
    "timestamp": "2021-02-09T18:33:51.238+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "",
    "path": "/create"
}

~~~





<h2>2.  Get All Blogs API</h2>

This API enables the retrieval of all blog posts via the sending of a GET request.
If the request goes through successfully, a response containing all the saved blog posts along with their details are sent back.

<h3> Successful response example

**Response**:

~~~json
{
        {
        "id": "e05b1ee9-2ec4-4d6c-90fc-1ea7d30a0aff",
        "title": "My First Blog",
        "content": "This is my first blog",
        "datePublished": "2020-01-01",
        "author": "Patrick Juan"
    },
    {
        "id": "2ad0d0de-bb77-4801-bc8a-169fa818f533",
        "title": "My Second Blog",
        "content": "This is my second blog",
        "datePublished": "2020-01-02",
        "author": "Patrick Juan"
    },
    {
        "id": "0e2c5e51-fa8e-4976-b1bc-7901ed8267f7",
        "title": "My Third Blog",
        "content": "This is my third blog",
        "datePublished": "2020-01-03",
        "author": "Patrick Juan"
    },
    {
        "id": "2d4a09e0-e97f-4c07-9ec2-8b64a203f6c3",
        "title": "My Fourth Blog",
        "content": "This is my fourth blog",
        "datePublished": "2020-01-04",
        "author": "Patrick Juan"
    }
}
~~~



<h2>3.  Get Blog API</h2>

This API enables the retrieval of a specific blog post via the sending of a GET request with the following field: *idInput*.
It requires <u>idInput</u> to be not blank and that the idInput must actually exist in the 'database'.
If the request goes through successfully, a response containing the blog post details is returned.
If the ID does not exist in the system or is left blank, a corresponding error message is displayed.

<h3> Success request and response example

**Request**:

```JSON
{
    "idInput": "e05b1ee9-2ec4-4d6c-90fc-1ea7d30a0aff" #this ID is in the system
}

```

**Response**:

~~~json
{
    "id": "e05b1ee9-2ec4-4d6c-90fc-1ea7d30a0aff",
    "title": "My First Blog",
    "content": "This is my updated blog",
    "datePublished": "2020-01-01",
    "author": "Patrick Juan"
    
    #the corresponding blog details of the id is displayed
}
~~~

<h3> Failed request and response example </h3>

**Request**:

```JSON
{
    "idInput": "2ad0d0de-bb77-4801-bc8a-169fa818f533" #this ID is not in the system 
}

```

**Response**:

~~~json
  {
    "message": "Invalid request: Blog not found on :: 2ad0d0de-bb77-4801-bc8a-169fa818f533" 
      #an error message saying that the ID is not in the system is displayed
}

~~~





<h2>4.  Update Blog Post Content API</h2>

This API enables the updating of a specific blog post's content via the sending of a PUT request with the following fields: *idInput* and *contentInput*.
It requires <u>all fields</u> to be not blank and that the idInput must actually exist in the 'database'.
If the request goes through successfully, a response containing the updated blog post details is returned.
If the ID does not exist in the system or either or both of the request fields are left blank, a corresponding error message is displayed.

<h3> Success request and response example

**Request**:

```JSON
{
    "idInput": "e05b1ee9-2ec4-4d6c-90fc-1ea7d30a0aff",
    "contentInput": "This is my updated blog"
}
```

**Response**:

~~~json
{
    "id": "e05b1ee9-2ec4-4d6c-90fc-1ea7d30a0aff",
    "title": "My First Blog",
    "content": "This is my updated blog",
    "datePublished": "2020-01-01",
    "author": "Patrick Juan"
}
~~~

<h3> Failed request and response example </h3>

**Request**:

```JSON
{
    "idInput": "e0asdsd1ee9-2ec4-4-90fc-1ea7d30a", ##this id is not in the system therefore we are expecting an error
    "contentInput": "This is my updated blog"
}

```

**Response**:

~~~json
{
    "message": "Invalid request: Blog not found on :: e05b1ee9-2ec4-4d6c-90fc-1ea7d30a" #an error message is displayed!
}

~~~





<h2>5. Delete Blog Post API</h2>

This API enables the deletion of a specific blog post via the sending of a DELETE request with the following fields: *idInput*.
It requires <u>idInput</u> to be not blank and that the idInput must actually exist in the 'database'.
If the request goes through successfully, the blog post is deleted and a response message confirming the post deletion is displayed.
If the ID does not exist in the system or the idInput is left blank, a corresponding error message is displayed.

<h3> Success request and response example

**Request**:

```JSON
{
    "idInput": "e05b1ee9-2ec4-4d6c-90fc-1ea7d30a0aff" #this ID is in the system
}
```

**Response**:

~~~json
Blog post with ID: e05b1ee9-2ec4-4d6c-90fc-1ea7d30a0affsuccessfully deleted! #the response is a string confirming the deletion
~~~

<h3> Failed request and response example </h3>

**Request**:

```JSON
{
    "idInput": "456456-bb77-4801-bc8a-23gsd67" #this is not an ID in the system
}

```

**Response**:

~~~json
{
    "message": "Invalid request: Blog not found on :: 456456-bb77-4801-bc8a-23gsd67" #an error msg is displayed in the response
}

~~~

