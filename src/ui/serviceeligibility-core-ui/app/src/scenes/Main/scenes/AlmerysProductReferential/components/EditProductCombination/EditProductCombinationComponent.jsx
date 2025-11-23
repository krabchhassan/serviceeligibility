/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import _ from 'lodash';
import moment from 'moment';
import { Field, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Button,
    Col,
    Panel,
    PanelFooter,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CommonDatePicker, CommonMultiCombobox } from '../../../../../../common/utils/Form/CommonFields';
import formConstants from '../../Constants';
import businessUtils from '../../../../../../common/utils/businessUtils';
import DateUtils from '../../../../../../common/utils/DateUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const modifyCombination = (combination, values) => {
    const newCombination = {};
    newCombination.dateDebut = combination.dateDebut;
    if (values.dateFin && values.dateFin !== '--/--/----') {
        newCombination.dateFin = moment.isMoment(values.dateFin)
            ? values.dateFin.format('YYYY/MM/DD')
            : moment(values.dateFin, 'DD/MM/YYYY').format('YYYY/MM/DD');
    } else if (combination.dateFin && values.dateFin === null) {
        newCombination.dateFin = null;
    }
    if (values.garantieTechniqueList) {
        newCombination.garantieTechniqueList = [];
        values.garantieTechniqueList.forEach((gt) =>
            newCombination.garantieTechniqueList.push({
                codeAssureur: gt.number,
                codeGarantie: gt.value,
            }),
        );
    }
    if (values.lotAlmerysList) {
        newCombination.lotAlmerysList = [];
        values.lotAlmerysList.forEach((lot) =>
            newCombination.lotAlmerysList.push({
                code: lot.value,
            }),
        );
    }

    return newCombination;
};
/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class EditProductCombinationComponent extends Component {
    constructor(props) {
        super(props);
        this.autocompleteGTs = _.debounce(this.autocompleteGTsCall, 1000);
        this.autocompleteLots = _.debounce(this.autocompleteLotsCall, 1000);
    }

    async componentDidMount() {
        const { getAllGT, getAllLot, initialize, combination } = this.props;
        getAllGT();
        getAllLot();
        initialize({
            dateFin: DateUtils.transformDateForDisplay(combination.dateFin),
            garantieTechniqueList: combination.garantieTechniqueList,
            lotAlmerysList: combination.lotAlmerysList,
        });
    }

    autocompleteGTsCall(input) {
        const { getAllGT } = this.props;
        if (input && input.length > 2) {
            const searchCriteria = {};
            searchCriteria.search = input;
            getAllGT(searchCriteria);
        }
    }

    autocompleteLotsCall(input) {
        const { getAllLot } = this.props;
        if (input && input.length > 2) {
            const searchCriteria = {};
            searchCriteria.search = input;
            getAllLot(input);
        }
    }

    validateEdition(product) {
        const { t, updateAlmerysProduct, onCombinationUpdated, addAlert, creatingProduct } = this.props;
        if (creatingProduct) {
            onCombinationUpdated(product);
        } else {
            updateAlmerysProduct(product)
                .then(() => {
                    onCombinationUpdated(product);
                    addAlert({
                        message: t('almerysProduct.updatedAlmerysProduct'),
                    });
                })
                .catch(() => {
                    console.error('Erreur lors de la mise à jour du produit Almerys');
                });
        }
    }

    @autobind
    handleSubmit(values) {
        const { t, addAlertError, product, index, combination } = this.props;
        const updatedCombination = modifyCombination(combination, values);
        if (
            (updatedCombination.garantieTechniqueList || []).length < 1 &&
            (updatedCombination.lotAlmerysList || []).length < 1
        ) {
            addAlertError({
                message: t('almerysProduct.erreurEditionCombinaison'),
                behavior: 'danger',
            });
        } else {
            ((product || {}).productCombinations || [])[index] = updatedCombination;
            this.validateEdition(product);
        }
    }

    render() {
        const { t, comboLabelsAllGT, comboLabelsAllLot, invalid, handleSubmit } = this.props;
        return (
            <Fragment>
                <form onSubmit={handleSubmit(this.handleSubmit)}>
                    <Panel border={false}>
                        <PanelSection>
                            <Row>
                                <Col>
                                    <Field
                                        id="edit-date-fin"
                                        key={formConstants.FIELDS.dateFin}
                                        name={formConstants.FIELDS.dateFin}
                                        component={CommonDatePicker}
                                        label={t('almerysProduct.dateFin')}
                                        placeholder={t('almerysProduct.dateFinPlaceholder')}
                                        clearable
                                    />
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <Field
                                        id="edit-garanties-techniques"
                                        key={formConstants.FIELDS.garantieTechniqueList}
                                        name={formConstants.FIELDS.garantieTechniqueList}
                                        component={CommonMultiCombobox}
                                        label={t('almerysProduct.gts')}
                                        placeholder={t('almerysProduct.gtsPlaceholder')}
                                        options={comboLabelsAllGT}
                                        filterOptions={businessUtils.filterAndLimit50}
                                        onInputChange={(input) => this.autocompleteGTs(input)}
                                        searchable
                                        clearable
                                    />
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <Field
                                        id="edit-lots"
                                        key={formConstants.FIELDS.lotAlmerysList}
                                        name={formConstants.FIELDS.lotAlmerysList}
                                        component={CommonMultiCombobox}
                                        label={t('almerysProduct.lots')}
                                        placeholder={t('almerysProduct.lotsPlaceholder')}
                                        options={comboLabelsAllLot}
                                        filterOptions={businessUtils.filterAndLimit50}
                                        onInputChange={(input) => this.autocompleteLots(input)}
                                        searchable
                                        clearable
                                    />
                                </Col>
                            </Row>
                        </PanelSection>
                    </Panel>
                    <PanelFooter>
                        <Button behavior="primary" type="submit" disabled={invalid}>
                            {t('validate')}
                        </Button>
                    </PanelFooter>
                </form>
            </Fragment>
        );
    }
}

EditProductCombinationComponent.propTypes = {
    t: PropTypes.func,
    handleSubmit: PropTypes.func,
    addAlertError: PropTypes.func,
    invalid: PropTypes.bool,
    getAllGT: PropTypes.func,
    comboLabelsAllGT: PropTypes.arrayOf(PropTypes.shape()),
    comboLabelsAllLot: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.EDIT_COMBI_FORM_NAME,
})(EditProductCombinationComponent);
