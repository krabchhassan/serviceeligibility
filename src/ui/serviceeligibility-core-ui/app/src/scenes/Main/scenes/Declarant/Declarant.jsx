/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { getFormValues } from 'redux-form';

import DeclarantComponent from './DeclarantComponent';
import actions from './actions';
import parametrageMetierActions from './../ParametrageMetier/actions';
import formConstants from './Constants';
import globalActions from '../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const createFormValues = getFormValues(`${formConstants.FORM_NAME}-Create`);
const editFormValues = getFormValues(`${formConstants.FORM_NAME}-Edit`);

const prepareEmptyDeclarant = () => ({
    pilotages: formConstants.SERVICES.map((item) => ({
        ...item,
        serviceOuvert: 'false',
        isCarteEditable: 'false',
        regroupements: [],
    })),
    delaiRetention: '3',
});

const prepareCopyDeclarant = (declarant) => ({
    ...declarant,
    // On duplique toutes les données sauf le n° d'AMC et le n° de SIRET
    siret: null,
    numero: null,
});

const mapStateToProps = (state, ownProps) => {
    const { id, action } = ownProps.match.params;
    const pathname = ownProps.route.path;
    const { declarant, formDeclarant, loading } = state.main.declarant;

    const isCreate = pathname === '/create_insurer';
    const isCopy = action === 'duplicate';
    const isEdit = action === 'edit' && !isCopy;
    let name = '';
    if (isCreate) {
        name = 'Create';
    } else if (isEdit || isCopy) {
        name = 'Edit';
    }

    const form = `${formConstants.FORM_NAME}-${name}`;
    const conditionalValues = {};
    if (isCreate) {
        conditionalValues.initialValues = prepareEmptyDeclarant();
        conditionalValues.declarant = createFormValues(state);
    } else if (declarant) {
        if (isCopy) {
            conditionalValues.duplicatedDeclarant = {
                numero: declarant.numero,
                nom: declarant.nom,
            };
            conditionalValues.declarant = prepareCopyDeclarant(editFormValues(state));
            conditionalValues.initialValues = prepareCopyDeclarant(formDeclarant);
        } else {
            conditionalValues.declarant = isEdit ? editFormValues(state) : formDeclarant;
            conditionalValues.initialValues = formDeclarant;
        }
    }

    return {
        ...conditionalValues,
        form,
        formName: form,
        isEdit,
        isCreate,
        isCopy,
        id,
        loading,
        fields: state.form[form] ? state.form[form].registeredFields : null,
        services: state.common.common.services,
        circuits: state.common.common.circuits,
        conventions: state.common.common.conventions,
        domainList: state.main.declarant.domainesTP,
        reseauxSoin: state.main.declarant.reseauxSoin,
        returnCodesList: state.main.parametrageMetier.returnCodesList,
        lightDeclarants: state.common.common.lightDeclarants,
        clientType: state.common.configuration.values.clientType,
    };
};

const mapDispatchToProps = {
    getDeclarant: actions.getOne,
    updateDeclarant: actions.update,
    createDeclarant: actions.create,
    exists: actions.exists,
    getDomainesTP: actions.getDomainesTP,
    getReseauxSoin: actions.getReseauxSoin,
    getAllReturnCodes: parametrageMetierActions.getAllReturnCodes,
    getLightDeclarants: globalActions.getLightDeclarants,
    addAlertError: globalActions.addAlertError,
};

const Declarant = connect(mapStateToProps, mapDispatchToProps)(DeclarantComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Declarant;
