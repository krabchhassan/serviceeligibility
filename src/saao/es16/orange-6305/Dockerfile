FROM registry-prod.beyond.cegedim.cloud/release/ci/python-run-image-3.7.9:1.2.5 as build

ARG PIP_INDEX_URL


ENV PYTHONUNBUFFERED True

COPY entrypoint.sh .
RUN chmod +x /entrypoint.sh

#Add Packages
COPY *.whl ./

# Packages installation
RUN pip install --no-cache-dir --index-url=$PIP_INDEX_URL *.whl

ENTRYPOINT ["/entrypoint.sh"]