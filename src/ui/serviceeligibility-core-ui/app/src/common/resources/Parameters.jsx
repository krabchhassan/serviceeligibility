/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const Parameters = Api.all('v2/parameters');
Parameters.conventionnement = Parameters.custom('conventionnement');
Parameters.conventionnement.deleteByCode = (agreementCode) => Parameters.custom(`conventionnement/${agreementCode}`);
Parameters.serviceMetier = Parameters.custom('servicesMetiers');
Parameters.serviceMetier.deleteByCode = (serviceMetierCode) =>
    Parameters.custom(`servicesMetiers/${serviceMetierCode}`);
Parameters.processes = Parameters.custom('processus');
Parameters.filestype = Parameters.custom('typeFichiers');
Parameters.claim = Parameters.custom('prestations');
Parameters.claim.deleteByCode = (claimCode) => Parameters.custom(`prestations/${claimCode}`);
Parameters.domain = Parameters.custom('domaine');
Parameters.domain.deleteByCode = (domainCode) => Parameters.custom(`domaine/${domainCode}`);
Parameters.domainIS = Parameters.custom('domaine_IS');
Parameters.domainIS.deleteByCode = (domainISCode) => Parameters.custom(`domaine_IS/${domainISCode}`);
Parameters.domainSP = Parameters.custom('domaine_SP');
Parameters.domainSP.deleteByCode = (domainSPCode) => Parameters.custom(`domaine_SP/${domainSPCode}`);
Parameters.formula = Parameters.custom('formules');
Parameters.formula.deleteByCode = (formulaCode) => Parameters.custom(`formules/${formulaCode}`);
Parameters.codesRenvoi = Parameters.custom('codesRenvoi');

const Rejects = Api.all('v2/rejets');

const CodesRenvoi = Api.all('v2/codesRenvoi');

export { Parameters, Rejects, CodesRenvoi };
