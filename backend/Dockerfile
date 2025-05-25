# Use TomEE 10 with OpenJDK 11 as the base image
FROM tomee:10.0.0-M1-plus

# Copy the WAR file to the TomEE webapps directory in the container
COPY ./target/*.war /usr/local/tomcat/webapps/

# Copy the custom server.xml from your application to the TomEE configuration directory in the container
COPY conf/server.xml /usr/local/tomee/conf/server.xml

# Install the ping utility for health checks
RUN apt-get update && apt-get install -y --no-install-recommends iputils-ping

EXPOSE 8080

CMD ["catalina.sh", "run"]
