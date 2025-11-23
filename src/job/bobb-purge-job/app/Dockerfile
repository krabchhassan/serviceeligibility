FROM registry-prod.beyond.cegedim.cloud/release/python-run-image/app-run-image:16.0.3


ARG PIP_INDEX_URL

#Postgresql-client installation
RUN apk update && apk add postgresql-client

#Add Packages
COPY *.whl ./

# Packages installation
RUN pip install --no-cache-dir --index-url=$PIP_INDEX_URL *.whl
RUN mkdir -p /tmp/artifacts/outputs



RUN python -m pip check

ENV SCHEMAS_DIR=/var/schemas
COPY schemas/events /var/schemas/events
COPY schemas/audits /var/schemas/audits

COPY entrypoint.sh .
RUN chmod +x /entrypoint.sh

ENV PYTHONUNBUFFERED True

ENTRYPOINT ["/entrypoint.sh"]
