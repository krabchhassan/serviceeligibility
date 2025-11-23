/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import globalActions from '../../../../../../common/actions';
import actions from '../../actions';
import CreateAlmerysProductComponent from './CreateAlmerysProductComponent';
import formConstants from '../../Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const selector = formValueSelector(formConstants.CREATE_FORM_NAME);

const mapStateToProps = (state) => ({
    almerysProductCombinationList: state.main.almerysProduct.almerysProductCombinationList,
    code: selector(state, 'code'),
    description: selector(state, 'description'),
});

const mapDispatchToProps = {
    createAlmerysProduct: actions.createAlmerysProduct,
    addAlert: globalActions.addAlert,
    addAlertError: globalActions.addAlertError,
};

const CreateAlmerysProduct = connect(mapStateToProps, mapDispatchToProps)(CreateAlmerysProductComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default CreateAlmerysProduct;
