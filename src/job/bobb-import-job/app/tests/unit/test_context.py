import os
import unittest
from unittest.mock import patch
from bobbimportjob.common.common_constants import BEYOND_INSTANCE_VAR_ENV
from bobbimportjob.services.event_publisher_service import init_context

CUSTOMER = "test"
INSTANCE = "ed01-es15"

os.environ["BEYOND_INSTANCE_TYPE"] = "dedicated"
os.environ["BEYOND_CUSTOMER"] = CUSTOMER
os.environ[BEYOND_INSTANCE_VAR_ENV] = INSTANCE


class TestMain(unittest.TestCase):

    @patch("bobbimportjob.services.event_publisher_service.init_execution_context")
    def test_main_reset_sequence_by_name(self, init_execution_context_mock):
        init_context()
        init_execution_context_mock.assert_called_once_with()


if __name__ == '__main__':
    unittest.main()
