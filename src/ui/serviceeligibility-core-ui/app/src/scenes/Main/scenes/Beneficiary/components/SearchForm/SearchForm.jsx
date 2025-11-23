/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import SearchFormComponent from './SearchFormComponent';
import Constants from '../../Constants';
import actions from '../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const formSelector = formValueSelector(Constants.FORM_NAME);

const mapStateToProps = (state) => ({
    nom: formSelector(state, Constants.FIELDS.nom),
    prenom: formSelector(state, Constants.FIELDS.prenom),
    nir: formSelector(state, Constants.FIELDS.nir),
    numeroOuNomAMC: formSelector(state, Constants.FIELDS.numeroOuNomAMC),
    societeEmettrice: formSelector(state, Constants.FIELDS.societeEmettrice),
    numeroAdherentOuContrat: formSelector(state, Constants.FIELDS.numeroAdherentOuContrat),
    dateNaissance: formSelector(state, Constants.FIELDS.dateNaissance),
    rangNaissance: formSelector(state, Constants.FIELDS.rangNaissance),
    telephone: formSelector(state, Constants.FIELDS.telephone),
    mail: formSelector(state, Constants.FIELDS.mail),
    bic: formSelector(state, Constants.FIELDS.bic),
    iban: formSelector(state, Constants.FIELDS.iban),
    codePostal: formSelector(state, Constants.FIELDS.codePostal),
    localite: formSelector(state, Constants.FIELDS.localite),
    numeroEtLibelleVoie: formSelector(state, Constants.FIELDS.numeroEtLibelleVoie),
    declarants: state.main.beneficiary.declarants,
    names: state.main.beneficiary.names,
    firstNames: state.main.beneficiary.firstNames,
    localites: state.main.beneficiary.localites,
    voies: state.main.beneficiary.voies,
    societesEmettrices: state.main.beneficiary.societesEmettrices,
    clientType: state.common.configuration.values.clientType,
});

const mapDispatchToProps = {
    getBeneficiaries: actions.getBeneficiaries,
    changeCriteriaResearch: actions.changeCriteriaResearch,
    getAllDeclarants: actions.getAllDeclarants,
    getAllNames: actions.getAllNames,
    getAllFirstNames: actions.getAllFirstNames,
    getAllLocalites: actions.getAllLocalites,
    getAllVoies: actions.getAllVoies,
    getAllSocietesEmettrices: actions.getAllSocietesEmettrices,
};

const SearchForm = connect(mapStateToProps, mapDispatchToProps)(SearchFormComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default SearchForm;
