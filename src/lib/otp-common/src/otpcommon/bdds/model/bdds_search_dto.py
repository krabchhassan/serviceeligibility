from dataclasses import dataclass, field
from typing import List, Optional

from dataclasses_json import dataclass_json

from otpcommon import default_json_config


@dataclass_json
@dataclass
class Amc:
    id_declarant: Optional[str] = field(default=None, metadata=default_json_config)
    libelle: Optional[str] = field(default=None, metadata=default_json_config)


@dataclass_json
@dataclass
class Contrat:
    numero_contrat: Optional[str] = field(default=None, metadata=default_json_config)
    code_etat: Optional[str] = field(default=None, metadata=default_json_config)
    data: Optional[dict] = field(default=None, metadata=default_json_config)
    numero_adherent: Optional[str] = field(default=None, metadata=default_json_config)
    societe_emettrice: Optional[str] = field(default=None, metadata=default_json_config)
    numero_a_m_c_echange: Optional[str] = field(default=None, metadata=default_json_config)

    # Ordering by prenom -> nomUsage
    def prenom(self) -> str:
        return self.data.get('nom', {}).get('prenom', '')

    def nom_usage(self) -> str:
        return self.data.get('nom', {}).get('nomUsage', '')

    def order_key(self) -> str:
        return f'{self.prenom()} {self.nom_usage()}'


@dataclass_json
@dataclass
class BddsSearchResponse:
    id: Optional[str] = field(default=None, metadata=default_json_config)
    id_client_b_o: Optional[str] = field(default=None, metadata=default_json_config)
    amc: Optional[Amc] = field(default=None, metadata=default_json_config)
    numero_adherent: Optional[str] = field(default=None, metadata=default_json_config)
    identite: Optional[dict] = field(default=None, metadata=default_json_config)
    audit: Optional[dict] = field(default=None, metadata=default_json_config)
    trace_id: Optional[str] = field(default=None, metadata=default_json_config)
    contrats: Optional[List[Contrat]] = field(default=None, metadata=default_json_config)
    services: Optional[List[str]] = field(default=None, metadata=default_json_config)
    key: Optional[str] = field(default=None, metadata=default_json_config)
    environnement: Optional[str] = field(default=None, metadata=default_json_config)

    # Used to order by (contrats.data.nom.prenom, contrats.data.nom.nomUsage)
    def first_contrat(self) -> Optional[Contrat]:
        return next(iter(self.contrats), None)

    def order_key(self) -> str:
        contrat = self.first_contrat()
        return contrat.order_key() if contrat else ''

    def __lt__(self, other):
        return self.order_key() < other.order_key()

    # Used for hash
    def __eq__(self, other):
        if not isinstance(other, BddsSearchResponse):
            return False

        return (self.id == other.id) \
            and (self.key == other.key)

    def __ne__(self, other):
        return not self.__eq__(other)

    def __hash__(self):
        return hash(self.key)


@dataclass_json
@dataclass
class BddsSearchRequest:
    bic: Optional[str] = field(default=None, metadata=default_json_config)
    birth_date: Optional[str] = field(default=None, metadata=default_json_config)
    birth_rank: Optional[str] = field(default=None, metadata=default_json_config)
    city: Optional[str] = field(default=None, metadata=default_json_config)
    declarant_id: Optional[str] = field(default=None, metadata=default_json_config)
    email: Optional[str] = field(default=None, metadata=default_json_config)
    first_name: Optional[str] = field(default=None, metadata=default_json_config)
    iban: Optional[str] = field(default=None, metadata=default_json_config)
    issuing_company: Optional[str] = field(default=None, metadata=default_json_config)
    name: Optional[str] = field(default=None, metadata=default_json_config)
    nir: Optional[str] = field(default=None, metadata=default_json_config)
    street: Optional[str] = field(default=None, metadata=default_json_config)
    subscriber_id_or_contract_number: Optional[str] = field(default=None, metadata=default_json_config)
    service_metier: Optional[str] = field(default=None, metadata=default_json_config)
    environnement: Optional[str] = field(default=None, metadata=default_json_config)
