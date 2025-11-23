FROM registry-prod.beyond.cegedim.cloud/release/python-run-image/app-run-image:16.0.4 as build

ARG PIP_INDEX_URL


ENV PYTHONUNBUFFERED True

#Add Packages
COPY *.whl ./

# Packages installation
RUN pip install --no-cache-dir --index-url=$PIP_INDEX_URL *.whl

RUN python -m pip check

FROM registry-prod.beyond.cegedim.cloud/release/python-run-image/app-run-image:16.0.4

COPY entrypoint.sh .
RUN chmod +x /entrypoint.sh

COPY --from=build /usr/local/bin/saao /usr/local/bin/saao
COPY --from=build /usr/local/lib/python3.11/site-packages /usr/local/lib/python3.11/site-packages

ENTRYPOINT ["/entrypoint.sh"]
