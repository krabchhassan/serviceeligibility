import importlib
import logging

from commonutils.utils import ApiCallStatusWrapper

try:
    api_call_key_module = importlib.import_module("faashelper.adapters.helpers.api_call.v1.api_call_key")
    api_call_module = importlib.import_module("faashelper.adapters.helpers.api_call")
    get_function = getattr(api_call_key_module, "get")
    ApiCallStatus = getattr(api_call_module, "ApiCallStatus")
except (ModuleNotFoundError, AttributeError) as e:
    raise ImportError(f"Failed to load API call modules: {e}")


def call_api_get(service, endpoint, qparams=None, timeout=None):
    """
    Executes an HTTP GET request to a specified API endpoint.
    """
    try:
        logging.info(f"Calling API: {service}{endpoint} with params={qparams}")

        status, response = get_function(service, endpoint, qparams=qparams, timeout=timeout)

        wrapped_status = ApiCallStatusWrapper.OK if status == ApiCallStatus.OK else ApiCallStatusWrapper.KO

        logging.info(f"API call completed with status: {wrapped_status.name}")

        return wrapped_status, response
    except Exception as e:
        logging.error(f"API call failed: {e}", exc_info=True)
        raise RuntimeError(f"API call to {service}{endpoint} failed: {e}")
