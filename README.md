# spring-zipkin-cassandra
A simple microservice log tracing example with Spring Cloud, <a href="https://github.com/openzipkin/zipkin">Zipkin</a>, and Cassandra.
<br>
Collects and stores traffic between distributed microservices from the zipkin server. This helps you prevent or resolve problems quickly.

## Architecture
<img src="https://user-images.githubusercontent.com/17774927/167255362-02e26181-92b8-4b8f-af63-00827c72f3a8.png">

## Usage

1. Fire up Cassandra first.

        docker-compose up -d cassandra-zipkin
        
2. Set Cassandra Schema.
<br>The sample cql files were taken from here. <a href="https://github.com/openzipkin/zipkin/tree/master/zipkin-storage/cassandra">zipkin-storage/cassandra</a>
        
        docker exec -it cassandra-zipkin /bin/bash
        
        # bin/bash
        cd /var/lib
        
        # Execute CQL files.
        cqlsh -f zipkin2-schema.cql
        cqlsh -f zipkin2-schema-indexes.cql
        cqlsh -f zipkin2-schema-upgrade-1.cql
        cqlsh -f zipkin2-schema-upgrade-2.cql
        
        # verify keyspace, tables, indexes(sasi) are generated.
        cqlsh> desc keyspace zipkin2;
        cqlsh> use zipkin2; desc tables;

      <img src="https://user-images.githubusercontent.com/17774927/167257152-0c9b94b3-693b-4a59-8903-ff30687dac9c.png">

3. Run the remaining containers.
      
        docker-compose up -d

4. Set environment variables

      > Encrypt config-server properties.

        JASYPT_ENCRYPT_PASSWORD=

      > Required to connect to the config-server protected by spring-security.

        CONFIG_SERVER_USR=
        CONFIG_SERVER_PWD=
          
5. Build and Run Java Applications

        # Build
        ./gradlew build
        
        # Run
        java -jar cloud-services/config-server/build/libs/*.jar & \
        java -jar cloud-services/eureka-server/build/libs/*.jar & \
        java -jar cloud-services/gateway/build/libs/*.jar & \
        java -jar microservices/product-composite-service/build/libs/*.jar & \
        java -jar microservices/product-service/build/libs/*.jar & \
        java -jar microservices/review-service/build/libs/*.jar &
        
## API & Tracing

### API
> Insert

    curl -XPOST http://localhost:8443/product-composite -H "Content-Type: application/json" -d'
      {
        "product": {
          "name": "Water ball",
          "productId": 1,
          "cost": 22
        },
        "reviews": [
          {
            "reviewId": 1,
            "author": "Joe",
            "subject": "Review...1",
            "content": "It was good to me."
          },
          {
            "reviewId": 2,
            "author": "Robin",
            "subject": "Review...2",
            "content": "It sucks to me."
          }
        ]
      }
    '
    
> Read

    curl -XGET http://localhost:8443/product-composite/1 
    
> Delete

    curl -XDELETE http://localhost:8443/product-composite/1

<br><br>
### Tracing

> [Service name, Trace ID, and Span ID]
<br>Below is an example where Trace information was automatically injected into the log by Sleuth.
<img src="https://user-images.githubusercontent.com/17774927/167258140-4f4f0d55-e8d6-40a2-a9a4-7154b308caf6.png">
<br>

> Zipkin UI (<a href="http://localhost:9411/zipkin">http://localhost:9411/zipkin</a>)
<img src="https://user-images.githubusercontent.com/17774927/167257940-21b02b55-870b-42fc-8eaf-677b86a82f4b.png">
<img src="https://user-images.githubusercontent.com/17774927/167257953-cbdb9446-0631-4491-ba8f-575b0a768b0a.png">

> Verify that log data is stored in the Cassandra(Zipkin Storage).

    cqlsh:zipkin2> select * from span_by_service limit 2;

     service | span
    ---------+------
     gateway |  get
     gateway | post

    (2 rows)
    
    cqlsh:zipkin2> select * from span limit 1;
     trace_id         | ts_uuid                              | id               | annotation_query                                                                                   | annotations | debug | duration | kind   | l_ep                                                                    | l_service     | parent_id | r_ep                                                        | shared | span | tags                                                                                    | trace_id_high | ts
    ------------------+--------------------------------------+------------------+----------------------------------------------------------------------------------------------------+-------------+-------+----------+--------+-------------------------------------------------------------------------+---------------+-----------+-------------------------------------------------------------+--------+------+-----------------------------------------------------------------------------------------+---------------+------------------
     0a86206fde57464c | 6da16dd0-ce0f-11ec-827f-9ab14d16aca9 | 0a86206fde57464c | ░http.method░http.method=PUT░http.path░http.path=/eureka/apps/PRODUCT/192.168.45.112:product:7001░ |        null |  null |      958 | SERVER | {service: 'eureka-server', ipv4: '192.168.45.112', ipv6: null, port: 0} | eureka-server |      null | {service: null, ipv4: '127.0.0.1', ipv6: null, port: 56197} |   null |  put | {'http.method': 'PUT', 'http.path': '/eureka/apps/PRODUCT/192.168.45.112:product:7001'} |          null | 1651932620077538
