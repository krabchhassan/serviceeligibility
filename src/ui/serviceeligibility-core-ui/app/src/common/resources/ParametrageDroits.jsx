import Api from './Api';

const par = Api.all('v1');
par.getByParams = par.custom('parametragesCarteTP');
par.one = (id) => par.custom(`parametragesCarteTP/${id}`);
par.getParamByAmc = par.custom('referentielParametrageCarteTP');
par.changeStatus = (id) => par.custom(`parametragesCarteTP/${id}/statut`);
par.getPriorityByAMC = (id) => par.custom(`parametragesCarteTP/priorite/${id}`);
export { par as default };
