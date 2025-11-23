FROM registry-prod.beyond.cegedim.cloud/release/java-base-images/run-21:v16.0.2 AS builder
ARG ARTIFACT_NAME
WORKDIR /builder
COPY $ARTIFACT_NAME.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

FROM registry-prod.beyond.cegedim.cloud/release/java-base-images/run-21:v16.0.2

COPY --from=builder /builder/extracted/dependencies/ ./application
COPY --from=builder /builder/extracted/spring-boot-loader/ ./application
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./application
COPY --from=builder /builder/extracted/application/ ./application

CMD ["-jar","./application/app.jar"]
