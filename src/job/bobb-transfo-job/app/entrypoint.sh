#!/bin/sh

set -eo pipefail
if [ -f /vault/secrets/env_vars.txt ]; then
  source /vault/secrets/env_vars.txt
fi

if [ -f /vault/secrets/workflows_env_vars.txt ]; then
  source /vault/secrets/workflows_env_vars.txt
fi
python -m bobbtransfojob $@
