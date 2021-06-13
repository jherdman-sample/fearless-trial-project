# Fearless Trial Project
Solution by Jeffrey Herdman

### RESTful API for Items
- Available endpoints are GET/PUT/POST/DELETE at localhost:3000/items
- Expected JSON input is as follows: `[{'id':INT, 'name':'STRING }, ...]`. 
- Example POST would be: `curl -i -X POST localhost:3000/items -H "Content-type: application/json" -d "[{JSON_VALUES}]`.
  It is important to provide the header `Content-type: application/json`  

## Application Instructions 
From the root directory of the project
- Build: `mvn clean package`
- Run Application: `mvn spring-boot:run`
- Docker Build: `docker build -f src\main\docker\fearless_project.dockerfile -t jherdman-fearless-project-java8 .`
- Docker Run: `docker run -p 3000:3000 jherdman-fearless-project-java8`

## Additional Notes:
### Assumptions
- One major assumption was that the `id` field of the `DataItem` was an integer, and that the number needed to be unique
  within the collection of `DataItems` 
### Upcoming features
- Adding Swagger/OpenAPI documentation to allow for auto-generated documentation.
- Complete Bonus task of creating a database with docker (I was not familiar enough with docker database images to do
  this).  Part of this would work include creating an actual Entity model fo the
  data (likely transforming the DataItem class), configuring the transaction manager for the service layer to handle the
  persisting/retrieving of models.  
- Provide Unit testing for complex logic (at the point of completion there was nothing that *I* deemed complex enough to necessitate a unit test)