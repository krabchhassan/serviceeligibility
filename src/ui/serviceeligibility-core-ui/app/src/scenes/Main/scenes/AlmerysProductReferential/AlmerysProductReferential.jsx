/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import AlmerysProductReferentialComponent from './AlmerysProductReferentialComponent';
import globalActions from '../../../../common/actions';
import actions from './actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const formSelector = formValueSelector('almerysProductRefSearch');
/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state, ownProps) => ({
    almerysProductCombinationList: state.main.almerysProduct.almerysProductCombinationList,
    pagination: state.main.almerysProduct.pagination,
    loading: state.main.paramDroits.loading,
    hasCreated: ownProps.match.params.hasCreated,
    comboLabelsAllGT: state.common.common.comboLabelsAllGT,
    isLoadingAllGT: state.common.common.isLoadingAllGT,
    comboLabelsAllLot: state.common.common.comboLabelsAllLot,
    allLot: state.common.common.lots,
    isLoadingAllLot: state.common.common.isLoadingAllLot,
    lots: formSelector(state, 'lots'),
    gts: formSelector(state, 'gts'),
});

const mapDispatchToProps = {
    searchAlmerysProduct: actions.searchAlmerysProduct,
    getAllGT: globalActions.getAllGT,
    getAllLot: globalActions.getLots,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const AlmerysProductReferential = connect(mapStateToProps, mapDispatchToProps)(AlmerysProductReferentialComponent);

export default AlmerysProductReferential;
