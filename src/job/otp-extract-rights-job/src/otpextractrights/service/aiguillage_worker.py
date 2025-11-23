import asyncio
import traceback
from os import getenv
from typing import List

from otpcommon.aiguillage.model.aiguillage_extract_contract import AiguillageExtractContractRequest, \
    AiguillageExtractContractResponse, AiguillageError
from otpcommon.aiguillage.service.aiguillage_service import AiguillageService

from otpextractrights.configuration import Logger
from otpextractrights.utils import list_batch
from otpextractrights.utils.errors import TECHNICAL_ERROR


class AiguillageWorker:
    AIGUILLAGE_BATCH_SIZE = int(getenv("AIGUILLAGE_BATCH_SIZE", 100))
    AIGUILLAGE_CODE_ERREUR = "AIGUILLAGE_ERROR"

    def __init__(self):
        self.aiguillage_service = AiguillageService()

    def run(
            self,
            requests: List[AiguillageExtractContractRequest],
            nb_workers: int = 4
    ) -> List[AiguillageExtractContractResponse]:
        try:
            return asyncio.run(self._run(requests, nb_workers))
        except Exception as e:
            Logger.error(traceback.format_exc())
            raise e

    async def _run(
            self,
            aiguillage_input: List[AiguillageExtractContractRequest],
            nb_workers: int
    ) -> List[AiguillageExtractContractResponse]:

        responses: List[AiguillageExtractContractResponse] = []

        # Populate request queue
        requests_queue = asyncio.Queue()
        self._populate_queue(aiguillage_input, requests_queue)

        # Create workers
        tasks = [asyncio.create_task(self._worker(i, requests_queue, responses)) for i in range(nb_workers)]

        # Await queue is empty
        await requests_queue.join()

        # Delete workers
        [t.cancel() for t in tasks]
        await asyncio.gather(*tasks, return_exceptions=True)

        return responses

    def _populate_queue(
            self,
            input_beneficiaries: List[AiguillageExtractContractRequest],
            requests_queue: asyncio.Queue
    ) -> None:
        batch_inputs: List[List[AiguillageExtractContractRequest]] = list_batch(
            input_list=input_beneficiaries,
            batch_size=self.AIGUILLAGE_BATCH_SIZE
        )
        for bulk in batch_inputs:
            requests_queue.put_nowait(bulk)

    async def _worker(
            self,
            worker_id: int,
            requests_queue: asyncio.Queue,
            responses: list
    ) -> None:
        Logger.info(f"aiguillage worker {worker_id} starting")
        while True:
            if requests_queue.empty():
                Logger.info(f"aiguillage worker {worker_id} shut down - queue empty")
                break
            Logger.debug(f"aiguillage worker {worker_id} consuming queue")
            request: List[AiguillageExtractContractRequest] = await requests_queue.get()
            try:
                response: AiguillageExtractContractResponse = await self.aiguillage_service.find_all_extract(worker_id,
                                                                                                             request)
                responses.append(response)
            except Exception as e:
                Logger.error(f"aiguillage worker {worker_id}: Error Calling aiguillage")
                Logger.error(e)
                responses.append(AiguillageExtractContractResponse(
                    content={},
                    errors={req.hash: AiguillageError(code=TECHNICAL_ERROR, request=req.to_dict()) for req in request}
                ))
            finally:
                requests_queue.task_done()
