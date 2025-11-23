FROM registry-prod.beyond.cegedim.cloud/release/socle-faas-python-image/func-socle-faas-serving:16.0.16

ARG PIP_INDEX_URL

# Add audits json schemas to container
COPY schemas /var/schemas
ENV SCHEMAS_DIR=/var/schemas

# Add configuration
ADD configuration.json /usr/local/configuration.json

#Add Packages
COPY *.whl ./

# Packages installation
RUN pip install --no-cache-dir --index-url=$PIP_INDEX_URL *.whl
