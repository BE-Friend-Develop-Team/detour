FROM openjdk:17-alpine

COPY ./build/libs/detour-0.0.1-SNAPSHOT.jar /build/libs/detour-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/build/libs/detour-0.0.1-SNAPSHOT.jar"]