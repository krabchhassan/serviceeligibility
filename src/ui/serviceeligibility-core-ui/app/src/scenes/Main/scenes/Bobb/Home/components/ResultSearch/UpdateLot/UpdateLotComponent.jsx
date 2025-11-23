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
import { CommonInput, CommonMultiCombobox } from '../../../../../../../../common/utils/Form/CommonFields';
import formConstants from '../../Constants';
import ValidationFactories from '../../../../../../../../common/utils/Form/ValidationFactories';
import businessUtils from '../../../../../../../../common/utils/businessUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();

const modifyBody = (body, values) => {
    if (values.code) {
        body.code = values.code;
    }
    if (values.libelle) {
        body.libelle = values.libelle;
    }
    if (values.garantieTechniques) {
        values.garantieTechniques.forEach((gt) => {
            body.garantieTechniques.push({
                codeAssureur: gt.number,
                codeGarantie: gt.value,
                dateAjout: gt.dateAjout,
            });
        });
    }
    return body;
};
/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['bobb', 'common'])
class UpdateLotComponent extends Component {
    constructor(props) {
        super(props);
        this.autocompleteGTs = _.debounce(this.autocompleteGTsCall, 1000);
    }
    componentDidMount() {
        const { lotToModify } = this.props;
        if (lotToModify) {
            this.initializeFormValues(lotToModify);
        }
    }

    initializeFormValues = (lotToModify) => {
        const { initialize } = this.props;
        // Préparez un objet contenant toutes les valeurs à préremplir
        const initialValues = {
            code: lotToModify.code || '',
            libelle: lotToModify.libelle || '',
            garantieTechniques: [],
        };

        if (lotToModify.garantieTechniques) {
            const garantieTechniques = lotToModify.garantieTechniques.filter((gt) => gt.dateSuppressionLogique == null);

            initialValues.garantieTechniques = garantieTechniques.map((gt) => ({
                value: gt.codeGarantie || '',
                label: `${gt.codeAssureur} - ${gt.codeGarantie}` || '',
                number: gt.codeAssureur || '',
            }));
        }
        initialize(initialValues);
    };

    @autobind
    handleSubmit(values) {
        const { t, updateLot, addAlert, addAlertError, onLotModified } = this.props;
        const body = {
            page: 1,
            perPage: 10,
            garantieTechniques: [],
        };

        const bodyToSend = modifyBody(body, values);

        if (bodyToSend.garantieTechniques.length < 2) {
            addAlertError({
                message: t('modify.erreurModificationLot'),
                behavior: 'danger',
            });
        } else {
            updateLot(body).then(() => {
                addAlert({
                    message: t('modify.modifiedLot'),
                });
                onLotModified();
            });
        }
    }

    autocompleteGTsCall(input) {
        const { getAllGT } = this.props;
        if (input && input.length > 2) {
            const searchCriteria = {};
            searchCriteria.search = input;
            getAllGT(searchCriteria);
        }
    }

    render() {
        const { t, handleSubmit, invalid, comboLabelsAllGT } = this.props;

        return (
            <Fragment>
                <form onSubmit={handleSubmit(this.handleSubmit)}>
                    <Panel border={false}>
                        <PanelSection>
                            <Row>
                                <Col xs={6}>
                                    <Field
                                        id="update-code"
                                        key={formConstants.FIELDS.code}
                                        name={formConstants.FIELDS.code}
                                        component={CommonInput}
                                        label={t('search.codeLot')}
                                        placeholder={t('search.codeLotPlaceholder')}
                                        clearable
                                        disabled
                                    />
                                </Col>
                                <Col xs={6}>
                                    <Field
                                        id="update-libelle"
                                        key={formConstants.FIELDS.libelle}
                                        name={formConstants.FIELDS.libelle}
                                        component={CommonInput}
                                        label={t('search.libelleLot')}
                                        placeholder={t('search.libelleLotPlaceholder')}
                                        clearable
                                        validate={required}
                                    />
                                </Col>
                            </Row>
                            <Row>
                                <Col xs={12}>
                                    <Field
                                        id="update-garanties-techniques"
                                        key={formConstants.FIELDS.garantieTechniques}
                                        name={formConstants.FIELDS.garantieTechniques}
                                        component={CommonMultiCombobox}
                                        label={t('search.garantiesTechniques')}
                                        placeholder={t('search.garantiesTechniquesPlaceholder')}
                                        options={comboLabelsAllGT}
                                        filterOptions={businessUtils.filterAndLimit50}
                                        onInputChange={(input) => this.autocompleteGTs(input)}
                                        searchable
                                        clearable
                                        multi
                                        validate={required}
                                    />
                                </Col>
                            </Row>
                        </PanelSection>
                    </Panel>
                    <PanelFooter>
                        <Button behavior="primary" type="submit" disabled={invalid}>
                            {t('edit')}
                        </Button>
                    </PanelFooter>
                </form>
            </Fragment>
        );
    }
}

UpdateLotComponent.propTypes = {
    t: PropTypes.func,
    handleSubmit: PropTypes.func,
    updateLot: PropTypes.func.isRequired,
    lotToModify: PropTypes.shape({
        id: PropTypes.string,
        code: PropTypes.string,
        libelle: PropTypes.string,
        garantieTechniques: PropTypes.arrayOf(
            PropTypes.shape({
                codeAssureur: PropTypes.string,
                codeGarantie: PropTypes.string,
                dateAjout: PropTypes.string,
                dateSuppressionLogique: PropTypes.string,
            }),
        ),
    }),
    getAllGT: PropTypes.func,
    comboLabelsAllGT: PropTypes.arrayOf(PropTypes.shape()),
    addAlert: PropTypes.func,
    addAlertError: PropTypes.func,
    invalid: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.EDIT_FORM_NAME,
})(UpdateLotComponent);
