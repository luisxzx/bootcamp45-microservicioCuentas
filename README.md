bootcamp45-microservicioCuentas mirarlo en code para mayor entendimiento
realizar los siguientes cambios para poder deployar en entorno local 
cambiar todo lo que hay en application.properties por lo siguiente :



ntt.data.bootcamp.s01-client-service = http://localhost:8080
#ntt.data.bootcamp.s01-account-service = https://localhost:8081
ntt.data.bootcamp.s01-credit-service = http://localhost:8082
ntt.data.bootcamp.s01-transaction-service = http://localhost:8083

# MongoDB configuration
spring.application.name=microservicios-cuentas
spring.config.import=configserver:http://localhost:8888

# Server port
server.port=8081
spring.cloud.config.enabled=true

eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/

resilience4j.circuitbreaker.instances.myCircuit.slidingWindowSize=3
resilience4j.circuitbreaker.instances.myCircuit.permittedNumberOfCallsInHalfOpenState=10
resilience4j.circuitbreaker.instances.myCircuit.automaticTransitionFromOpenToHalfOpenEnabled=true
resilience4j.circuitbreaker.instances.myCircuit.waitDurationInOpenState=60s
resilience4j.circuitbreaker.instances.myCircuit.failureRateThreshold=50
