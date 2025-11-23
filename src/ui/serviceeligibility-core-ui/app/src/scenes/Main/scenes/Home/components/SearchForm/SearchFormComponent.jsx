/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import { Field, reduxForm } from 'redux-form';

import {
    Button,
    Col,
    HabilitationFragment,
    Panel,
    PanelFooter,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import isEmpty from 'lodash/isEmpty';
import { CommonCombobox, CommonInput } from '../../../../../../common/utils/Form/CommonFields';
import formConstants from '../../Constants';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import permissionConstants from './../../../../PermissionConstants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const oneRequiredValue = ValidationFactories.oneRequired((allValues) => {
    const { amcNumber, amcLabel, service, couloir } = allValues;

    return [amcNumber, amcLabel, service, couloir];
}, '(*)');

const requiredService = (value, allValues) => {
    const { couloir } = allValues;

    if (couloir && isEmpty(value)) {
        return 'fields.required';
    }

    return undefined;
};

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class SearchFormComponent extends Component {
    componentDidMount() {
        const { getLightDeclarants, comboLightDeclarants } = this.props;
        if (!comboLightDeclarants) {
            getLightDeclarants();
        }
    }

    @autobind
    resetSearchFunction() {
        const { reset } = this.props;
        reset();
    }

    @autobind
    handleSubmit() {
        const { searchDeclarant, changeSarchCriteria, amcNumber, amcLabel, couloir, service } = this.props;
        const searchCriteria = {};
        if (amcNumber) {
            searchCriteria.numero = amcNumber.value;
        }
        if (amcLabel) {
            searchCriteria.nom = amcLabel.label;
        }
        if (couloir) {
            searchCriteria.couloir = couloir;
        }
        if (service) {
            searchCriteria.service = service.nom;
        }
        searchDeclarant(searchCriteria);
        changeSarchCriteria(searchCriteria);
    }

    renderFooter() {
        const { t, loading, invalid, pristine } = this.props;
        return (
            <PanelFooter comment={t('atLeastOneSearch')}>
                <div className="d-flex justify-content-end">
                    <span className="pr-2">
                        <HabilitationFragment allowedPermissions={permissionConstants.READ_AMC_DATA_PERMISSION}>
                            <Button
                                type="button"
                                size="md"
                                outline
                                onClick={this.resetSearchFunction}
                                id="clear-search-customer"
                            >
                                <CgIcon name="undo" size="1x" />
                            </Button>
                        </HabilitationFragment>
                    </span>
                    <span className="pr-2">
                        <HabilitationFragment allowedPermissions={permissionConstants.READ_AMC_DATA_PERMISSION}>
                            <Button
                                behavior="primary"
                                type="submit"
                                disabled={loading || invalid || pristine}
                                id="search-customer"
                            >
                                {t('common:search')}
                            </Button>
                        </HabilitationFragment>
                    </span>
                </div>
            </PanelFooter>
        );
    }

    render() {
        const {
            t,
            handleSubmit,
            services,
            amcLabel,
            couloir,
            service,
            amcNumber,
            comboLightDeclarants,
            comboLabelsLightDeclarants,
        } = this.props;
        return (
            <form onSubmit={handleSubmit(this.handleSubmit)}>
                <Panel footer={this.renderFooter()} id="search" border={false}>
                    <PanelSection>
                        <Row>
                            <Col xs="3">
                                <Field
                                    name={formConstants.FIELDS.amcNumber}
                                    component={CommonCombobox}
                                    options={comboLightDeclarants}
                                    label={t('home.amcNumber')}
                                    placeholder={t('home.amcNumberPlaceholder')}
                                    tooltip={t('home.amcNumberTooltip')}
                                    disabled={!!amcLabel || !!couloir || !!service}
                                    validate={oneRequiredValue}
                                    showRequired="(*)"
                                    searchable
                                />
                            </Col>
                            <Col xs="3">
                                <Field
                                    name={formConstants.FIELDS.amcLabel}
                                    component={CommonCombobox}
                                    options={comboLabelsLightDeclarants}
                                    label={t('home.amcLabel')}
                                    placeholder={t('home.amcLabelPlaceholder')}
                                    tooltip={t('home.amcLabelTooltip')}
                                    disabled={!!amcNumber || !!couloir || !!service}
                                    validate={oneRequiredValue}
                                    showRequired="(*)"
                                    searchable
                                />
                            </Col>
                            <Col xs="3">
                                <Field
                                    name={formConstants.FIELDS.couloir}
                                    component={CommonInput}
                                    label={t('home.couloir')}
                                    placeholder={t('home.couloirPlaceholder')}
                                    tooltip={t('home.couloirTooltip')}
                                    disabled={!!amcLabel || !!amcNumber}
                                    validate={oneRequiredValue}
                                    showRequired="(*)"
                                />
                            </Col>
                            <Col xs="3">
                                <Field
                                    name={formConstants.FIELDS.service}
                                    component={CommonCombobox}
                                    label={t('home.service')}
                                    placeholder={t('home.servicePlaceholder')}
                                    tooltip={t('home.serviceTooltip')}
                                    options={services}
                                    disabled={!!amcLabel || !!amcNumber}
                                    labelKey="nom"
                                    idKey="id"
                                    validate={[oneRequiredValue, requiredService]}
                                    showRequired="(*)"
                                    searchable
                                />
                            </Col>
                        </Row>
                    </PanelSection>
                </Panel>
            </form>
        );
    }
}

SearchFormComponent.propTypes = {
    t: PropTypes.func,
    amcNumber: PropTypes.string,
    amcLabel: PropTypes.string,
    couloir: PropTypes.string,
    service: PropTypes.string,
    services: PropTypes.arrayOf(PropTypes.shape()),
    searchDeclarant: PropTypes.func,
    reset: PropTypes.func,
    changeSarchCriteria: PropTypes.func,
    loading: PropTypes.bool,
    invalid: PropTypes.bool,
    pristine: PropTypes.bool,
    handleSubmit: PropTypes.func,
    getLightDeclarants: PropTypes.func,
    comboLightDeclarants: PropTypes.arrayOf(PropTypes.shape()),
    comboLabelsLightDeclarants: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(SearchFormComponent);
