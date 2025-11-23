import asyncio
import traceback
from collections import defaultdict
from os import getenv
from typing import List, Union, Dict

from otpcommon.bdds.model.bdds_extract_contract import BddsExtractContractRequest, BddsExtractContractResponse, \
    BddsExtractContractResponseError
from otpcommon.bdds.service.bdds_service import BddsService
from otpcommon.mls.model.server_info import ServerInfo

from otpextractrights.configuration import Logger
from otpextractrights.model import MlsAmcFail
from otpextractrights.utils import list_batch
from otpextractrights.utils.errors import TECHNICAL_ERROR


class BddsWorker:
    BDDS_BATCH_SIZE = getenv("BDDS_BATCH_SIZE", 100)

    def __init__(self):
        self.bdds_service = BddsService()

    def run(
            self,
            beneficiaries: List[BddsExtractContractRequest],
            servers: Dict[str, Union[ServerInfo, MlsAmcFail]],
            nb_workers: int = 8
    ) -> List[BddsExtractContractResponse]:
        try:
            return asyncio.run(self._run(beneficiaries, servers, nb_workers))
        except Exception as e:
            Logger.error(traceback.format_exc())
            raise e

    async def _run(
            self,
            beneficiaries: List[BddsExtractContractRequest],
            servers: Dict[str, Union[ServerInfo, MlsAmcFail]],
            nb_workers: int
    ) -> List[BddsExtractContractResponse]:

        responses: List[BddsExtractContractResponse] = []

        requests_queue = asyncio.Queue()
        self._populate_queue(beneficiaries, servers, requests_queue)

        tasks = [asyncio.create_task(self._worker(i, requests_queue, responses)) for i in range(nb_workers)]

        # Await queue is empty
        await requests_queue.join()

        # Delete workers
        [t.cancel() for t in tasks]
        await asyncio.gather(*tasks, return_exceptions=True)

        return responses

    def _populate_queue(
            self,
            beneficiaries: List[BddsExtractContractRequest],
            servers: Dict[str, Union[ServerInfo, MlsAmcFail]],
            queue: asyncio.Queue
    ):
        beneficiaries_by_amc = defaultdict(list)
        for benef in beneficiaries:
            beneficiaries_by_amc[benef.amc].append(benef)
        for amc, benef_list in beneficiaries_by_amc.items():
            try:
                server = servers[amc]
            except KeyError:
                Logger.error("Amc not found in server list from amc: should not happen!!!")
                continue
            if not isinstance(server, MlsAmcFail):
                amc_bulks: List[List[BddsExtractContractRequest]] = list_batch(benef_list, self.BDDS_BATCH_SIZE)
                for amc_bulk_item in amc_bulks:
                    queue.put_nowait((server, amc_bulk_item))

    async def _worker(
            self,
            worker_id: int,
            queue: asyncio.Queue,
            responses: list
    ) -> None:
        Logger.info(f"BDDS worker {worker_id} starting")
        while True:
            if queue.empty():
                Logger.info(f"BDDS worker {worker_id} shut down - queue empty")
                break
            Logger.debug(f"BDDS worker {worker_id} consuming queue")
            server, request = await queue.get()

            try:
                response = await self.bdds_service.find_all_extract(worker_id, server, request)
                responses.append(response)
            except Exception as e:
                Logger.error(f"bdds worker {worker_id}: Error Calling bdds for amc {server.amc} {server.base_url}")
                Logger.error(e)
                responses.append(BddsExtractContractResponse(
                    content={},
                    errors={req.hash: BddsExtractContractResponseError(code=TECHNICAL_ERROR, request=req.to_dict()) for
                            req in request}
                ))
            finally:
                queue.task_done()
