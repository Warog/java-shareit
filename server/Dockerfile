FROM amazoncorretto:11
COPY target/*.jar shareitserver.jar
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9091
ENTRYPOINT ["java", "-jar", "/shareitserver.jar"]
