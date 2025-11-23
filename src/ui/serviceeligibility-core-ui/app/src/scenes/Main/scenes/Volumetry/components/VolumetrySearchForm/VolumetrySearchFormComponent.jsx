/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import {
    Panel,
    PanelFooter,
    PanelSection,
    Row,
    Col,
    Button,
    HabilitationFragment,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { Field, reduxForm } from 'redux-form';
import Constants from '../../Constants';
import { CommonCombobox, CommonInput } from '../../../../../../common/utils/Form/CommonFields';
import style from '../../style.module.scss';
import PermissionConstants from '../../../../PermissionConstants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
const getDeclarantsValueForCombobox = (declarants) =>
    (declarants || []).map((declarant) => ({
        label: `${declarant.numeroRNM} - ${declarant.nom}`,
        value: declarant.numeroRNM,
    }));

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class VolumetrySearchFormComponent extends Component {
    componentWillMount() {
        const { getAllDeclarants } = this.props;
        getAllDeclarants();
    }

    @autobind
    handleSubmit(values) {
        const { changeVolumetrySearchCriteria } = this.props;
        changeVolumetrySearchCriteria(values);
    }

    @autobind
    resetSearchFunction() {
        const { reset } = this.props;
        reset();
    }

    renderFooter() {
        const { invalid, t } = this.props;

        return (
            <PanelFooter>
                <div className="d-flex justify-content-end">
                    <span className="pr-2">
                        <HabilitationFragment allowedPermissions={PermissionConstants.READ_TRANSCODAGE_DATA_PERMISSION}>
                            <Button
                                type="button"
                                outline
                                size="md"
                                onClick={this.resetSearchFunction}
                                id="clear-search-beneficiary"
                            >
                                <CgIcon name="undo" size="1x" />
                            </Button>
                        </HabilitationFragment>
                    </span>
                    <span className="pr-2">
                        <HabilitationFragment allowedPermissions={PermissionConstants.READ_TRANSCODAGE_DATA_PERMISSION}>
                            <Button behavior="primary" type="submit" disabled={invalid} id="volumetry-search-submit">
                                {t('common:search')}
                            </Button>
                        </HabilitationFragment>
                    </span>
                </div>
            </PanelFooter>
        );
    }

    render() {
        const { handleSubmit, formValues, t, declarants } = this.props;
        const { codePartenaire, amc } = formValues || {};

        return (
            <form onSubmit={handleSubmit(this.handleSubmit)}>
                <Panel footer={this.renderFooter()} wrapperClassName={style['flex-none']} border={false}>
                    <PanelSection>
                        <Row>
                            <Col xs={6}>
                                <Field
                                    name={Constants.FIELDS.amc}
                                    component={CommonCombobox}
                                    label={t('volumetry.amc')}
                                    placeholder={t('volumetry.placeholder.amc')}
                                    tooltip={t('volumetry.tooltip.amc')}
                                    options={getDeclarantsValueForCombobox(declarants)}
                                    disabled={!!codePartenaire}
                                    searchable
                                    clearable
                                />
                            </Col>
                            <Col xs={6}>
                                <Field
                                    name={Constants.FIELDS.codePartenaire}
                                    component={CommonInput}
                                    label={t('volumetry.partner')}
                                    placeholder={t('volumetry.placeholder.partner')}
                                    tooltip={t('volumetry.tooltip.partner')}
                                    disabled={!!amc}
                                />
                            </Col>
                        </Row>
                    </PanelSection>
                </Panel>
            </form>
        );
    }
}

VolumetrySearchFormComponent.propTypes = {
    t: PropTypes.func,
    handleSubmit: PropTypes.func,
    changeVolumetrySearchCriteria: PropTypes.func,
    invalid: PropTypes.bool,
    formValues: PropTypes.shape(),
    reset: PropTypes.func,
    getAllDeclarants: PropTypes.func,
    declarants: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: Constants.FORM_NAME,
})(VolumetrySearchFormComponent);
