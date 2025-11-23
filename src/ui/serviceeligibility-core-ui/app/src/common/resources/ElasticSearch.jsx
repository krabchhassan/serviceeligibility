/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
import elasticSearchApi from './ElasticSearchApi';
import ApiBeyond from './ApiBeyond';
import store from '../../store';

const url = 'v1';

/* ********************************************** */
/* ********      PRIVATE FUNCTIONS       ******** */
/* ********************************************** */

const convertNativeToNormal = (data) =>
    data.map((item) => ({
        // eslint-disable-next-line no-underscore-dangle
        metaMap: { ...item },
        // eslint-disable-next-line no-underscore-dangle
        id: item.id,
    }));

/* ********************************************** */
/* ********       PUBLIC FUNCTIONS       ******** */
/* ********************************************** */
const getAutocompleteBody = (field, searchValue) => ({
    field,
    value: searchValue,
});

const formatResponse = (response, searchValue, addOption = false) => {
    // get auto-complete result
    const res = response.body(false) || [];
    if (!addOption) return res;

    // add search to combobox options (enable a free text search)
    const newOption = searchValue.toUpperCase();
    const includesValue = res.some((item) => {
        return item.toUpperCase() === newOption;
    });
    if (!includesValue) res.unshift(searchValue);
    return res;
};

function queryDistinctNames(search = '', addOption = false) {
    const body = getAutocompleteBody('contrats.data.nom.nomFamille', search);
    return elasticSearchApi
        .all(url)
        .get(`autocompletion?field=${body.field}&value=${body.value}`)
        .then((response) => formatResponse(response, search, addOption));
}

function queryDistinctFirstnames(search = '', addOption = false) {
    const body = getAutocompleteBody('contrats.data.nom.prenom', search);
    return elasticSearchApi
        .all(url)
        .get(`autocompletion?field=${body.field}&value=${body.value}`)
        .then((response) => formatResponse(response, search, addOption));
}

function queryDistinctLocalites(localitesQueryParameter = '') {
    const body = getAutocompleteBody('contrats.data.adresse.ligne6', localitesQueryParameter);
    return elasticSearchApi
        .all(url)
        .get(`autocompletion?field=${body.field}&value=${body.value}`)
        .then((response) => response.body(false));
}

function queryDistinctVoies(voiesQueryParameter = '') {
    const body = getAutocompleteBody('contrats.data.adresse.ligne4', voiesQueryParameter);
    return elasticSearchApi
        .all(url)
        .get(`autocompletion?field=${body.field}&value=${body.value}`)
        .then((response) => response.body(false));
}

function getOneBenef(benefKey) {
    return elasticSearchApi
        .all(url)
        .get(`beneficiaries/${benefKey}`)
        .then((response) => response.body(false));
}

function getIds(ids) {
    return elasticSearchApi
        .all(url)
        .get(`beneficiaries?ids=${ids}`)
        .then((response) => response.body(false) || []);
}

function queryBeneficiaries(beneficiaryObject) {
    const configuration = store?.getState()?.common?.configuration?.values;
    const searchBeneficiariesUrl = configuration?.useBeyondApi ? 'searchBeneficiaries' : `${url}/searchBeneficiaries`;

    const nameQueryParameter = beneficiaryObject.nom ? beneficiaryObject.nom.value : null;
    const firstNameQueryParameter = beneficiaryObject.prenom ? beneficiaryObject.prenom.value : null;
    let nirQueryParameter = beneficiaryObject.nir;
    if (nirQueryParameter) {
        nirQueryParameter = nirQueryParameter.replace(/ /g, '');
    }
    const amcQueryParameter = beneficiaryObject.numeroOuNomAMC || null;
    const societeEmettriceQueryParameter = beneficiaryObject.societeEmettrice || null;
    const adherentOuContratQueryParameter = beneficiaryObject.numeroAdherentOuContrat || null;
    const birthDateQueryParameter = beneficiaryObject.dateNaissance || null;
    const birthRankQueryParameter = beneficiaryObject.rangNaissance || null;

    const mailQueryParameter = beneficiaryObject.mail || null;
    const bicQueryParameter = beneficiaryObject.bic || null;
    let ibanQueryParameter = beneficiaryObject.iban || null;
    if (ibanQueryParameter) {
        ibanQueryParameter = ibanQueryParameter.replace(/ /g, '');
    }
    const cityQueryParameter = beneficiaryObject.localite || null;
    const streetQueryParameter = beneficiaryObject.voie || null;

    const body = {
        name: nameQueryParameter,
        firstName: firstNameQueryParameter,
        nir: nirQueryParameter,
        declarantId: amcQueryParameter,
        issuingCompany: societeEmettriceQueryParameter,
        birthDate: birthDateQueryParameter,
        birthRank: birthRankQueryParameter,
        email: mailQueryParameter,
        bic: bicQueryParameter,
        iban: ibanQueryParameter,
        city: cityQueryParameter,
        street: streetQueryParameter,
        subscriberIdOrContractNumber: adherentOuContratQueryParameter,
    };

    return ApiBeyond()
        .all(searchBeneficiariesUrl)
        .post(body)
        .then((response) => {
            return convertNativeToNormal((response.body(false) || {}).data);
        });
}

export default {
    queryDistinctNames,
    queryDistinctFirstnames,
    queryBeneficiaries,
    queryDistinctLocalites,
    queryDistinctVoies,
    getIds,
    getOneBenef,
};
