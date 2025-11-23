/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import ParamCodesRenvoiComponent from './ParamCodesRenvoiComponent';
import globalActions from '../../../../../../common/actions';
import actions from '../../actions';
import Constants from './Constants';
// eslint-disable-next-line import/first
import { formValueSelector } from 'redux-form';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const formSelector = formValueSelector(Constants.FORM_NAME);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    returnCodesList: state.main.parametrageMetier.returnCodesList,
    codesRenvoi: formSelector(state, Constants.FIELDS.codesRenvoi) || '',
    libelleCodesRenvoi: formSelector(state, Constants.FIELDS.libelleCodesRenvoi) || '',
});

const mapDispatchToProps = {
    getAllReturnCodes: actions.getAllReturnCodes,
    createCodesRenvoi: actions.createCodesRenvoi,
    updateCodesRenvoi: actions.updateCodesRenvoi,
    addAlert: globalActions.addAlertError,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamCodesRenvoi = connect(mapStateToProps, mapDispatchToProps)(ParamCodesRenvoiComponent);
export default ParamCodesRenvoi;
