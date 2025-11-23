import pandas as pd
from beyond_analysis_resource_client.services import PostgreSQLService
from beyond_analysis_resource_client.utils import BaseLogging


class ServiceEligibilityPostgreSQLService(object):
    def __init__(self, nature):
        self.logger = BaseLogging().get_logger()
        self.pg_service = PostgreSQLService(nature)

    def execute(self, database, collection, query):
        return

    def get_all_recipient(self, database, table, id_beyond_name, id_beyond_recipient_list):
        placeholders = ', '.join(['%s']*len(id_beyond_recipient_list))

        query = f"""select
            c.id_external_contract as \"contractId\",
            TO_CHAR(p.start_date, 'YYYY-MM-DD') as \"periodeDebut\",
            TO_CHAR(p.end_date, 'YYYY-MM-DD') as \"periodeFin\",
            p.{id_beyond_name} as \"idBeyondDestinataire\"
        from public.contract c
        inner join public.{table} p on p.contract_id = c.id
        where (c.id_external_contract, p.{id_beyond_name}) in ({placeholders})
        """
        return self.pg_service.execute_query(
            database,
            query,
            id_beyond_recipient_list
        )

    def anti_join(self, x, y, on):
        """Return rows in x which are not present in y"""
        ans = pd.merge(left=x, right=y, how='left', indicator=True, on=on)
        ans = ans.loc[ans._merge == 'left_only', :].drop(columns='_merge')
        return ans

    def anti_join_all_cols(self, x, y):
        """Return rows in x which are not present in y"""
        assert set(x.columns.values) == set(y.columns.values)
        return self.anti_join(x, y, x.columns.tolist())
