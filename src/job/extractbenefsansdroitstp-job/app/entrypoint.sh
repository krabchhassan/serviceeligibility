#!/bin/sh
# shellcheck disable=SC2039
# shellcheck disable=SC1090
set -eo pipefail

vault_env_file="/vault/secrets/env_vars.txt"
wrkflw_env_file="/vault/secrets/workflows_env_vars.txt"

if [ -s "$vault_env_file" ]; then
  source "$vault_env_file"
fi

if [ -f "$wrkflw_env_file" ]; then
  source "$wrkflw_env_file"
fi

extractbenefsansdroitstpjob $@
