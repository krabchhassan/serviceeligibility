/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import _ from 'lodash';
import { Field, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Panel,
    Row,
    Col,
    PanelSection,
    PanelFooter,
    Button,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CommonDatePicker, CommonMultiCombobox } from '../../../../../../common/utils/Form/CommonFields';
import formConstants from '../../Constants';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import businessUtils from '../../../../../../common/utils/businessUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
const modifyBody = (body, values) => {
    if (values.dateDebut) {
        body.dateDebut = values.dateDebut.format('YYYY/MM/DD');
    }
    if (values.dateFin) {
        body.dateFin = values.dateFin.format('YYYY/MM/DD');
    }
    if (values.garantieTechniqueList) {
        body.garantieTechniqueList = [];
        values.garantieTechniqueList.forEach((gt) =>
            body.garantieTechniqueList.push({
                codeAssureur: gt.number,
                codeGarantie: gt.value,
            }),
        );
    }
    if (values.lotAlmerysList) {
        body.lotAlmerysList = [];
        values.lotAlmerysList.forEach((lot) =>
            body.lotAlmerysList.push({
                code: lot.value,
            }),
        );
    }

    return body;
};
/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class CreateProductCombinationComponent extends Component {
    constructor(props) {
        super(props);
        this.autocompleteGTs = _.debounce(this.autocompleteGTsCall, 1000);
        this.autocompleteLots = _.debounce(this.autocompleteLotsCall, 1000);
    }

    async componentDidMount() {
        const { getAllGT, getAllLot } = this.props;
        getAllGT();
        getAllLot();
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
    @autobind
    handleSubmit(values) {
        const { t, onCreatedCombination, addAlertError } = this.props;
        const body = {};

        const bodyToSend = modifyBody(body, values);
        if ((bodyToSend.garantieTechniqueList || []).length < 1 && (bodyToSend.lotAlmerysList || []).length < 1) {
            addAlertError({
                message: t('almerysProduct.erreurCreationCombinaison'),
                behavior: 'danger',
            });
        } else {
            onCreatedCombination(bodyToSend);
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
                                <Col xs={6}>
                                    <Field
                                        id="create-date-debut"
                                        key={formConstants.FIELDS.dateDebut}
                                        name={formConstants.FIELDS.dateDebut}
                                        component={CommonDatePicker}
                                        label={t('almerysProduct.dateDebut')}
                                        placeholder={t('almerysProduct.dateDebutPlaceholder')}
                                        clearable
                                        validate={required}
                                        showRequired
                                    />
                                </Col>
                                <Col xs={6}>
                                    <Field
                                        id="create-date-fin"
                                        key={formConstants.FIELDS.dateFin}
                                        name={formConstants.FIELDS.dateFin}
                                        component={CommonDatePicker}
                                        label={t('almerysProduct.dateFin')}
                                        placeholder={t('almerysProduct.dateFinPlaceholder')}
                                        clearable
                                    />
                                </Col>
                            </Row>
                            <Row />
                            <h4 className="text-colored-mix-1 mb-3 mt-5">{t('almerysProduct.gtsAndLotsRule')}</h4>
                            <Row>
                                <Col xs={6}>
                                    <Field
                                        id="create-garanties-techniques"
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
                                <Col xs={6}>
                                    <Field
                                        id="create-lots"
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
                            {t('add')}
                        </Button>
                    </PanelFooter>
                </form>
            </Fragment>
        );
    }
}

CreateProductCombinationComponent.propTypes = {
    t: PropTypes.func,
    handleSubmit: PropTypes.func,
    addAlertError: PropTypes.func,
    invalid: PropTypes.bool,
    getAllGT: PropTypes.func,
    getAllLot: PropTypes.func,
    comboLabelsAllGT: PropTypes.arrayOf(PropTypes.shape()),
    comboLabelsAllLot: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.CREATE_COMBI_FORM_NAME,
})(CreateProductCombinationComponent);
