import Api from './Api';

const parametragesCarteTP = Api.all('v1');
parametragesCarteTP.getContratIndiv = (amc, contrat, numeroAdherent) =>
    Api.custom(`v1/parametragesCarteTP/amcs/${amc}/contratsIndividuels/${contrat}/numeroAdherent/${numeroAdherent}`);
export { parametragesCarteTP as default };
