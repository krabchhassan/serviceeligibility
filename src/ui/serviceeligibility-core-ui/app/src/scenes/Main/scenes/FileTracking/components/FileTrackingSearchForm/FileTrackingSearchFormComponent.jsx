/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, Field } from 'redux-form';
import autobind from 'autobind-decorator';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import {
    Panel,
    PanelSection,
    Row,
    Col,
    PanelFooter,
    Button,
    HabilitationFragment,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CommonCombobox, CommonDatePicker, CommonInput } from '../../../../../../common/utils/Form/CommonFields';
import DateUtils from '../../../../../../common/utils/DateUtils';
import Constants from '../../Constants';
import fluxUtils from '../../fluxUtils';
import PermissionConstants from '../../../../PermissionConstants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const buildRequestParameters = (values) => {
    const formValues = { ...values };
    const processus = formValues[Constants.FIELDS.processus];
    delete formValues[Constants.FIELDS.processus];
    const emetteur = formValues[Constants.FIELDS.emetteur];
    delete formValues[Constants.FIELDS.emetteur];
    const typeFichier = formValues[Constants.FIELDS.typeFichier];
    delete formValues[Constants.FIELDS.typeFichier];
    const codeCircuit = formValues[Constants.FIELDS.codeCircuit];
    delete formValues[Constants.FIELDS.codeCircuit];
    const nomAMC = formValues[Constants.FIELDS.nomAMC];
    delete formValues[Constants.FIELDS.nomAMC];
    const amc = formValues[Constants.FIELDS.amc];
    delete formValues[Constants.FIELDS.amc];

    return {
        ...formValues,
        ...(processus &&
            processus.value && {
                [Constants.FIELDS.processus]: processus.value,
            }),
        ...(emetteur &&
            emetteur.value && {
                [Constants.FIELDS.emetteur]: emetteur.value,
            }),
        ...(typeFichier &&
            typeFichier.value && {
                [Constants.FIELDS.typeFichier]: typeFichier.value,
            }),
        ...(codeCircuit &&
            codeCircuit.value && {
                [Constants.FIELDS.codeCircuit]: codeCircuit.value,
            }),
        ...(nomAMC &&
            nomAMC.value && {
                [Constants.FIELDS.nomAMC]: nomAMC.value,
            }),
        ...(amc &&
            amc.value && {
                [Constants.FIELDS.amc]: amc.value,
            }),
        ...(formValues[Constants.FIELDS.dateDebut] && {
            [Constants.FIELDS.dateDebut]: DateUtils.formatLocalToUtc(
                DateUtils.formatServerDate(formValues[Constants.FIELDS.dateDebut]),
            ),
        }),
        ...(formValues[Constants.FIELDS.dateFin] && {
            [Constants.FIELDS.dateFin]: DateUtils.formatLocalToUtc(
                DateUtils.formatServerDate(formValues[Constants.FIELDS.dateFin]),
                true,
            ),
        }),
    };
};

const getDeclarantsValueForCombobox = (declarants, value) =>
    (declarants || []).map((declarant) => ({
        label: `${declarant.numero} - ${declarant.nom}`,
        value: declarant[value],
    }));

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class FileTrackingSearchFormComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            moreCriteriaSectionExpanded: false,
        };
    }

    componentDidMount() {
        this.handleSubmit();
    }

    @autobind
    handleSubmit(values) {
        const requestParamObject = buildRequestParameters(values);
        fluxUtils.searchCriteria(requestParamObject, this.props.dispatch);
    }

    @autobind
    handleMoreCriteriaSectionOnClick() {
        const { moreCriteriaSectionExpanded } = this.state;
        this.setState({ moreCriteriaSectionExpanded: !moreCriteriaSectionExpanded });
    }

    @autobind
    gerAllObjectFilter() {
        const { t } = this.props;
        return {
            value: '',
            label: t('all'),
        };
    }

    @autobind
    resetSearchFunction() {
        // eslint-disable-next-line react/prop-types
        const { reset } = this.props;
        reset();
    }

    renderMoreCriteria() {
        const { t, circuits, typesFile, declarants } = this.props;
        const comboBoxDataEmitters = fluxUtils.getEmittersValueForCombobox(circuits);
        const comboBoxDataTypesFile = fluxUtils.getGenericValueForCombobox(typesFile);
        const comboBoxDataCircuits = fluxUtils.getCircuitValueForCombobox(circuits);
        comboBoxDataEmitters.unshift(this.gerAllObjectFilter());
        comboBoxDataTypesFile.unshift(this.gerAllObjectFilter());
        comboBoxDataCircuits.unshift(this.gerAllObjectFilter());

        return (
            <Fragment>
                <Row>
                    <Col xs="12" sm="3">
                        <Field
                            id="file-tracking-search-partner"
                            name={Constants.FIELDS.codePartenaire}
                            component={CommonInput}
                            label={t('fileTracking.partner')}
                            tooltip={t('fileTracking.tooltip.partner')}
                            placeholder={t('fileTracking.placeholder.partner')}
                        />
                    </Col>
                    <Col xs="12" sm="3">
                        <Field
                            id="file-tracking-search-emitter"
                            name={Constants.FIELDS.emetteur}
                            component={CommonCombobox}
                            label={t('fileTracking.emitter')}
                            tooltip={t('fileTracking.tooltip.emitter')}
                            options={comboBoxDataEmitters}
                        />
                    </Col>
                    <Col xs="12" sm="3">
                        <Field
                            id="file-tracking-search-processus"
                            name={Constants.FIELDS.nomAMC}
                            component={CommonCombobox}
                            label={t('fileTracking.amcName')}
                            tooltip={t('fileTracking.tooltip.amcName')}
                            placeholder={t('fileTracking.placeholder.amcName')}
                            options={getDeclarantsValueForCombobox(declarants, 'nom')}
                            clearable
                            searchable
                        />
                    </Col>
                    <Col xs="12" sm="3">
                        <Field
                            id="file-tracking-search-file-number"
                            name={Constants.FIELDS.numeroFichier}
                            component={CommonInput}
                            label={t('fileTracking.numFile')}
                            tooltip={t('fileTracking.tooltip.numFile')}
                            placeholder={t('fileTracking.placeholder.numFile')}
                        />
                    </Col>
                </Row>
                <Row>
                    <Col xs="12" sm="3">
                        <Field
                            id="file-tracking-search-type-file"
                            name={Constants.FIELDS.typeFichier}
                            component={CommonCombobox}
                            label={t('fileTracking.typeFile')}
                            tooltip={t('fileTracking.tooltip.typeFile')}
                            options={comboBoxDataTypesFile}
                        />
                    </Col>
                    <Col xs="12" sm="3">
                        <Field
                            id="file-tracking-search-circuit"
                            name={Constants.FIELDS.codeCircuit}
                            component={CommonCombobox}
                            label={t('fileTracking.circuit')}
                            tooltip={t('fileTracking.tooltip.circuit')}
                            options={comboBoxDataCircuits}
                        />
                    </Col>
                    <Col xs="12" sm="3">
                        <Field
                            id="file-tracking-search-file-name"
                            name={Constants.FIELDS.nomFichier}
                            component={CommonInput}
                            label={t('fileTracking.fileName')}
                            tooltip={t('fileTracking.tooltip.fileName')}
                            placeholder={t('fileTracking.placeholder.fileName')}
                        />
                    </Col>
                </Row>
            </Fragment>
        );
    }

    renderFooter() {
        const { t, invalid } = this.props;
        const { moreCriteriaSectionExpanded } = this.state;
        const moreOrLessCriteriaIcon = moreCriteriaSectionExpanded ? 'angle-double-up' : 'angle-double-down';
        const moreOrLessCriteriaLabel = moreCriteriaSectionExpanded
            ? t('fileTracking.lessCriteria')
            : t('fileTracking.moreCriteria');

        return (
            <PanelFooter>
                <Button
                    id="file-tracking-search-search-more-less-criteria"
                    behavior="secondary"
                    size="sm"
                    type="button"
                    onClick={this.handleMoreCriteriaSectionOnClick}
                >
                    <div>
                        <CgIcon name={moreOrLessCriteriaIcon} /> {moreOrLessCriteriaLabel}
                    </div>
                </Button>
                <div>
                    <HabilitationFragment allowedPermissions={PermissionConstants.READ_FLUX_DATA_PERMISSION}>
                        <Button
                            type="button"
                            outline
                            size="md"
                            onClick={this.resetSearchFunction}
                            id="clear-search-beneficiary"
                            className="mr-2"
                        >
                            <CgIcon name="undo" size="1x" />
                        </Button>
                        <Button
                            behavior="primary"
                            type="submit"
                            disabled={invalid}
                            id="file-tracking-search-search-submit"
                        >
                            {t('common:search')}
                        </Button>
                    </HabilitationFragment>
                </div>
            </PanelFooter>
        );
    }

    render() {
        const { t, processus, handleSubmit, declarants } = this.props;
        const { moreCriteriaSectionExpanded } = this.state;
        const comboBoxDataProcessus = fluxUtils.getGenericValueForCombobox(processus);
        comboBoxDataProcessus.unshift(this.gerAllObjectFilter());
        return (
            <form onSubmit={handleSubmit(this.handleSubmit)}>
                <Panel footer={this.renderFooter()} border={false}>
                    <PanelSection>
                        <Row>
                            <Col xs="12" sm="3">
                                <Field
                                    id="file-tracking-search-starting-date"
                                    name={Constants.FIELDS.dateDebut}
                                    component={CommonDatePicker}
                                    label={t('fileTracking.from')}
                                    tooltip={t('fileTracking.tooltip.startingDate')}
                                />
                            </Col>
                            <Col xs="12" sm="3">
                                <Field
                                    id="file-tracking-search-ending-date"
                                    name={Constants.FIELDS.dateFin}
                                    component={CommonDatePicker}
                                    label={t('fileTracking.to')}
                                    tooltip={t('fileTracking.tooltip.endingDate')}
                                />
                            </Col>
                            <Col xs="12" sm="3">
                                <Field
                                    id="file-tracking-search-amc"
                                    name={Constants.FIELDS.amc}
                                    component={CommonCombobox}
                                    label={t('fileTracking.amcNumber')}
                                    tooltip={t('fileTracking.tooltip.amcNumber')}
                                    placeholder={t('fileTracking.placeholder.amc')}
                                    options={getDeclarantsValueForCombobox(declarants, 'numero')}
                                    clearable
                                    searchable
                                />
                            </Col>
                            <Col xs="12" sm="3">
                                <Field
                                    id="file-tracking-search-processus"
                                    name={Constants.FIELDS.processus}
                                    component={CommonCombobox}
                                    label={t('fileTracking.process')}
                                    tooltip={t('fileTracking.tooltip.processus')}
                                    options={comboBoxDataProcessus}
                                    searchable
                                />
                            </Col>
                        </Row>
                        <Row>
                            <Col>{moreCriteriaSectionExpanded && this.renderMoreCriteria()}</Col>
                        </Row>
                    </PanelSection>
                </Panel>
            </form>
        );
    }
}

FileTrackingSearchFormComponent.propTypes = {
    t: PropTypes.func,
    dispatch: PropTypes.func,
    handleSubmit: PropTypes.func,
    invalid: PropTypes.bool,
    processus: PropTypes.arrayOf(PropTypes.shape()),
    circuits: PropTypes.arrayOf(PropTypes.shape()),
    typesFile: PropTypes.arrayOf(PropTypes.shape()),
    declarants: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: Constants.FORM_NAME,
})(FileTrackingSearchFormComponent);
