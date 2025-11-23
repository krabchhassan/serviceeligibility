FROM registry-prod.beyond.cegedim.cloud/release/python-run-image/app-run-image:16.0.4


ARG PIP_INDEX_URL

#Add Packages
COPY *.whl ./

# Packages installation
RUN pip install --no-cache-dir --index-url=$PIP_INDEX_URL *.whl

RUN python -m pip check

RUN mkdir -p /tmp/artifacts/outputs



ENV SCHEMAS_DIR=/var/schemas
COPY schemas/events /var/schemas/events
COPY schemas/audits /var/schemas/audits

COPY entrypoint.sh .
RUN chmod +x /entrypoint.sh

ENV PYTHONUNBUFFERED True

ENTRYPOINT ["/entrypoint.sh"]
