from typing import Any, List, Dict


def list_batch(input_list: list, batch_size: int):
    """
    Transform a list into chunk of batch_size size.
    Example:
        list_batch([1,2,3,4,5,6,7], 3) -> [[1,2,3], [4,5,6], [7])
    """

    def batcher(inner_l: list, n: int):
        for i in range(0, len(inner_l), n):
            yield inner_l[i:i + n]

    return list(batcher(input_list, batch_size))


def dict_of_list_to_list(input_dict: Dict[Any, List[Any]]) -> List[Any]:
    return [item for list_values in input_dict.values() for item in list_values]
