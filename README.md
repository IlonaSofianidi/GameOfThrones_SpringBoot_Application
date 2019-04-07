# Game Of Thrones App
![alt text](/images/index.png)
## Introduction
This app displays relationship within two characters from [API of Ice And Fire](https://anapioficeandfire.com/)
This is the world's greatest source for quantified and structured data from the universe of Ice and Fire.
If you are interested in the marital status of your favorite hero, feel free to check it out here.
## Getting Started!
This sample uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project" in IntelliJ IDEA.
## Technologies
- Java 8
- Spring (Boot+Web+Data+Test)
- Gradle
- Embedded database H2
- Caffeine (for caching)
## Api Usage
### Create a relationstory
#### POST
```sh
http://localhost:8080/api/v1/stories/?name1=Sylwa Paege&name2=Whalen Frey
```
Return value: HTTP status 201 (Created)
JSON example

![example](/images/img2.png)


Note:if there are some characters with the same name, api checks the relationship each of them
#### POST
```sh
http://localhost:8080/api/v1/stories/?name1=Daenerys Targaryen&name2=Drogo
```

![example](/images/img1.png)

If there are not character with such name : HTTP status not found
JSON example

![example](/images/img3.png)

### Get relationstory by uuid
#### GET
```sh
http://localhost:8080/api/v1/stories/{relationStoryId}
```
Example
```sh
http://localhost:8080/api/v1/stories/cf505129-d86d-4148-985e-2abd0a4e3ea6
```
![example](/images/img4.png)
Return value: HTTP status 200 (OK) 
Retrun value if relationstory not found :HTTP status 404(Not found)
### Get all relationstories
The API will automatically paginate the responses. 
```sh
http://localhost:8080/stories
```
Return value:HTTP status 200(OK)
### Get all relationstories with pagination parametres
You can also create requests with pagination parameters and consume the response.
```sh
http://localhost:8080/stories/?page=1&size=5
```
JSON example
```sh
{
    "links": [
        {
            "rel": "first",
            "href": "http://localhost:8080/api/v1/stories?page=0&size=5"
        },
        {
            "rel": "self",
            "href": "http://localhost:8080/api/v1/stories?page=0&size=5"
        },
        {
            "rel": "next",
            "href": "http://localhost:8080/api/v1/stories?page=1&size=5"
        },
        {
            "rel": "last",
            "href": "http://localhost:8080/api/v1/stories?page=1&size=5"
        }
    ],
    "content": [
        {
            "character1": "Sylwa Paege",
            "character2": "Whalen Frey",
            "uuid": "cf505129-d86d-4148-985e-2abd0a4e3ea6",
            "links": [
                {
                    "rel": "self",
                    "href": "http://localhost:8080/api/v1/stories/cf505129-d86d-4148-985e-2abd0a4e3ea6"
                }
            ]
        },
        {
            "character1": "Daenerys Targaryen",
            "character2": "Drogo",
            "uuid": "6b19f692-2e74-4e9d-b162-c69528a5b08b",
            "links": [
                {
                    "rel": "self",
                    "href": "http://localhost:8080/api/v1/stories/6b19f692-2e74-4e9d-b162-c69528a5b08b"
                }
            ]
        },
        {
            "character1": "Daenerys Targaryen",
            "character2": "Drogo",
            "uuid": "50bbc9fe-bdc4-4b70-bc6b-4dd2aaff6629",
            "links": [
                {
                    "rel": "self",
                    "href": "http://localhost:8080/api/v1/stories/50bbc9fe-bdc4-4b70-bc6b-4dd2aaff6629"
                }
            ]
        },
        {
            "character1": "Aron Santagar",
            "character2": "Alyx Frey",
            "uuid": "5ac94ac6-8951-4eab-acc1-749244bed1c4",
            "links": [
                {
                    "rel": "self",
                    "href": "http://localhost:8080/api/v1/stories/5ac94ac6-8951-4eab-acc1-749244bed1c4"
                }
            ]
        },
        {
            "character1": "Aron Santagar",
            "character2": "Addison Hill",
            "uuid": "e8d81eae-d6fc-41da-b507-37aece126bc7",
            "links": [
                {
                    "rel": "self",
                    "href": "http://localhost:8080/api/v1/stories/e8d81eae-d6fc-41da-b507-37aece126bc7"
                }
            ]
        }
    ],
    "page": {
        "size": 5,
        "totalElements": 6,
        "totalPages": 2,
        "number": 0
    }
}
```
Return value:HTTP status 200(OK)

# Author
**Ilona Sofianidi**
