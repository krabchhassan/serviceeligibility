/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Api from './Api';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ContractElement = Api.all('v1/contractelement');
ContractElement.import = () => Api.custom('v1/contractelement/import');
ContractElement.export = () => Api.custom('v1/contractelement/export');
ContractElement.garanties = () => Api.all('v1/contractelement/garanties');

export default ContractElement;
