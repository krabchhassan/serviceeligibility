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
            // Assurez-vous que gt est au bon format
            if (gt.value && gt.number) {
                body.garantieTechniques.push({
                    codeAssureur: gt.number,
                    codeGarantie: gt.value,
                });
            }
        });
    }
    return body;
};
/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['bobb', 'common'])
class CreateLotComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            // eslint-disable-next-line react/no-unused-state
            body: {
                page: 1,
                perPage: 10,
            },
        };
        this.autocompleteGTs = _.debounce(this.autocompleteGTsCall, 1000);
    }

    async componentDidMount() {
        const { getAllLot } = this.props;
        getAllLot();
    }

    @autobind
    handleSubmit(values) {
        const { t, newLot, onLotCreated, addAlert, addAlertError, allLot } = this.props;
        const body = {
            page: 1,
            perPage: 10,
            garantieTechniques: [],
        };

        const bodyToSend = modifyBody(body, values);
        if (bodyToSend.garantieTechniques.length < 2) {
            addAlertError({
                message: t('create.erreurCreationLotGT'),
                behavior: 'danger',
            });
        } else if (bodyToSend.code.trim() === '') {
            addAlertError({
                message: t('create.erreurCodeLotVide'),
                behavior: 'danger',
            });
        } else if (bodyToSend.libelle.trim() === '') {
            addAlertError({
                message: t('create.erreurLibelleVide'),
                behavior: 'danger',
            });
            // eslint-disable-next-line no-shadow
        } else if (allLot.find((allLot) => allLot.code === bodyToSend.code)) {
            addAlertError({
                message: t('create.erreurLotExistant'),
                behavior: 'danger',
            });
        } else {
            newLot(body).then(() => {
                addAlert({
                    message: t('create.createdLot'),
                });
                onLotCreated(bodyToSend);
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
        const { t, handleSubmit, comboLabelsAllGT, invalid } = this.props;

        return (
            <Fragment>
                <form onSubmit={handleSubmit(this.handleSubmit)}>
                    <Panel border={false}>
                        <PanelSection>
                            <Row>
                                <Col xs={6}>
                                    <Field
                                        id="create-code"
                                        key={formConstants.FIELDS.code}
                                        name={formConstants.FIELDS.code}
                                        component={CommonInput}
                                        label={t('search.codeLot')}
                                        placeholder={t('search.codeLotPlaceholder')}
                                        clearable
                                        validate={required}
                                    />
                                </Col>
                                <Col xs={6}>
                                    <Field
                                        id="create-libelle"
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
                                        id="create-garanties-techniques"
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
                                        validate={required}
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

CreateLotComponent.propTypes = {
    t: PropTypes.func,
    handleSubmit: PropTypes.func,
    newLot: PropTypes.func.isRequired,
    addAlert: PropTypes.func,
    addAlertError: PropTypes.func,
    getAllGT: PropTypes.func,
    comboLabelsAllGT: PropTypes.arrayOf(PropTypes.shape()),
    invalid: PropTypes.bool,
    getAllLot: PropTypes.func,
    allLot: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.CREATE_FORM_NAME,
})(CreateLotComponent);
