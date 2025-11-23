import os
import logging
import json
import aiofiles

from datetime import datetime


from bobbexportjob.common.common_constants import DATE_FORMAT, OUTPUT_FILENAME, UTF_8_ENCODING,OUTPUT_FOLDER
from bobbexportjob.repositories.contract_element_repository import ContractElementRepository
from bobbexportjob.logging import init_logging
from commonomuhelper.omuhelper import get_workdir_output_path

LOGGER_NAME = "bobbexport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class ExtractContractElementService:
    def __init__(self, db_manager):
        self.contract_repository = ContractElementRepository(db_manager)

    def get_all_contract_elements(self):
        try:
            contract_elements = self.contract_repository.get_all_contract_elements()
            return contract_elements
        except Exception as e:
            logger.error(f"Error during contract service operation: {e}")
            raise

    async def export_contract_elements_to_json(self, contract_elements):
        try:
            output_file_path = os.path.join(get_workdir_output_path(), OUTPUT_FILENAME)

            async with aiofiles.open(output_file_path, mode='w', encoding=UTF_8_ENCODING) as f:
                await self.serialize_data(contract_elements, f)
            logger.info(f"Contract elements exported to {output_file_path}")
        except Exception as e:
            logger.error(f"An error occurred during export: {str(e)}")

    async def serialize_data(self, data, f):
        await f.write('[\n')
        if data:
            serialized_item = self.serialize_item(data[0])
            await f.write(json.dumps(serialized_item))
            for item in data[1:]:
                await f.write(',\n')
                serialized_item = self.serialize_item(item)
                await f.write(json.dumps(serialized_item))
        await f.write('\n]')

    def serialize_item(self, item):
        serialized_item = {}
        for key, value in item.items():
            if isinstance(value, dict):
                serialized_item[key] = self.serialize_item(value)
            elif isinstance(value, list):
                transformed_list = []
                for item in value:
                    transformed_list.append(self.serialize_item(item))
                serialized_item[key] = transformed_list
            elif isinstance(value, datetime):
                serialized_item[key] = value.strftime(DATE_FORMAT)
            elif isinstance(value, bool):
                serialized_item[key] = str(value).lower()
            else:
                serialized_item[key] = value
        return serialized_item
