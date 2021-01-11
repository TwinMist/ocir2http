[![Build Status](https://travis-ci.org/timezone4/ocir2http.svg)](https://travis-ci.org/timezone4/ocir2http)
# OCI-R to HTTP connector
Broadworks OCI-R to HTTP application


How to run:
```
java -jar ocir2http.jar -host 192.168.1.100 -url http://127.0.0.1:8080/api/report/12345678
```

HTTP client API:
The ocir2http connector sends a PUT request containing a JSON object with reportId, user and OCI-R event (base64 encoded).
The API server must return 200 OK as success response.

If 200 OK is not received, the connector will perfom 10 retries with an 3 second interval, and after that exit. If the connector exits, the event is lost, and the connector must be started manually.

```
PUT /api/report/12345678
{
  "actor": "admin",
  "report": "OCI-R XML",
  "reportPubId": "OCI-R XML PublicId Notifications",
  "reportId": "write105"
}
```


OCI-R interface:

When started, the connector establishes a TCP socket to the Broadworks application server on port 8025 and starts receiving OCI-R events.
