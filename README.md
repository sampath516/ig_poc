# ig_poc
IG POC

REST API Documentation: http://localhost:8080/swagger-ui.html

Micro service buid and deployment: mvn spring-boot:run

"http://<eureka service>:8761/eureka/apps/<APPID>"


"http://<gateway>:%d/<micro-service-name>/ig/repo/v1/tenants"




MicroService                       Port
gateway							   8080
discovery-server				   8761
Configuration Server               8888
import							   8082
connector-import-csv               8083
repository						   8081	 
