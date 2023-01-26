# Conference v1

Conference is an application developed on Java Spring boot framework and its a REST app with 6 endpoints with business logic and other endpoints for authentication
and authorization.

On the project are included also the tests and the POSTMAN json file to send API request.

The projects implements the authentication and authorization with JWT token.

All the conference endpoints are reached with authenticated user of Role <strong>ADMIN</strong>

On the bootstrap of application we feed the database with users and their respective roles

Also we feed database with some dummy data needed for some tests and also to understand better the application logic

# Endpoints

>- First Authenticate by using <strong>GET</strong> endpoint of <strong>localhost:8080/api/v1/user/authenticate</strong> and by using the body as in the postman json file

>- Get feeded conferences by the <strong>GET</strong> endpoint of <strong>localhost:8080/api/v1/conference/conferences</strong> or whenever you add a new conference

>- Create a conference by the <strong>POST</strong> endpoint of <strong>localhost:8080/api/v1/conference/create</strong> and get the body from postman json file

>- Check availablity of the respective conference by the <strong>GET</strong> endpoint of <strong>localhost:8080/api/v1/conference/{conferenceRoomId}/availability?registeredParticipants={startTime}&{endTime}</strong>

>- Cancel conference by the <strong>POST</strong> endpoint of <strong>localhost:8080/api/v1/conference/{conferenceRoomId}/cancel</strong>

>- Register participant by the <strong>POST</strong> endpoint of <strong>localhost:8080/api/v1/conference/{conferenceRoomId}/register</strong> and body from postman json file

>- Delete participant by id by the <strong>DELETE</strong> endpoint of <strong>localhost:8080/api/v1/conference/{conferenceRoomId}/delete?participantId={participantId}</strong>

# Testing 
> Testing is done by the Rest-assured library and all of the endpoints are tested
