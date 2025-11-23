/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import autobind from 'autobind-decorator';
import { Field } from 'redux-form';
import PropTypes from 'prop-types';
import {
    Button,
    ButtonGroup,
    Col,
    Input,
    Label,
    Nav,
    Panel,
    PanelSection,
    Row,
    Tooltip,
    NavItem,
    NavLink,
    ConfirmationModal,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import moment from 'moment';
import { CommonDatePicker, CommonInput, CommonRadio } from '../../../../../../../common/utils/Form/CommonFields';
import formConstants from '../Constants';
import ValidationFactories from '../../../../../../../common/utils/Form/ValidationFactories';
import Constants from '../../../../../../../common/utils/Constants';
import '../style.module.scss';

/* eslint-disable no-plusplus */
/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const isNumber = ValidationFactories.isNumber();
const required = ValidationFactories.required();
const minValue = ValidationFactories.minIncludedValue(0);
const minOne = ValidationFactories.minIncludedValue(1);
const maxValue = ValidationFactories.maxValue(50);
const dontInclude = (array) => ValidationFactories.dontIncludeValue(array);
const now = moment();

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common', 'errors'], { wait: true })
class ThirdStepComponent extends Component {
    constructor(props) {
        super(props);

        const { t } = props;

        this.state = {
            debutEcheanceSelected: this.props.debutEcheanceSelected,
            missingDebutEcheance: this.props.missingDebutEcheance,
            exemple: t('parametersTPContrat.delaiDeclenchementExemple', { days: this.props.delaiDeclenchementCarteTP }),
            tab: this.props.tab,
            dateDebutRenouvellement: null,
            annulDroitsOffline: this.props.annulDroitsOffline,
        };
        this.confirmRef = null;
    }

    componentDidMount() {
        const { change, dureeValiditeDroitsCarteTP, delaiDeclenchementCarteTP } = this.props;
        change(formConstants.FIELDS.dureeValiditeDroitsCarteTP, dureeValiditeDroitsCarteTP);
        change(formConstants.FIELDS.delaiDeclenchementAuto, delaiDeclenchementCarteTP);
    }

    @autobind onConfirm() {
        this.confirmRef.close();
    }

    @autobind
    onCancel() {
        const { change } = this.props;
        this.confirmRef.close();

        change(formConstants.FIELDS.dateDebutDroitTP, null);
        this.setState({
            dateDebutRenouvellement: null,
        });
    }

    @autobind
    getReferenceFromConfirmModal(ref) {
        this.confirmRef = ref;
    }

    @autobind
    setTab(selectedTab) {
        this.setState({
            tab: selectedTab,
        });
    }

    @autobind
    openModal() {
        this.confirmRef.open();
    }

    changeEcheance(setEcheance) {
        const { change } = this.props;
        const { debutEcheanceSelected, dateDebutRenouvellement } = this.state;
        if (setEcheance !== debutEcheanceSelected) {
            if (debutEcheanceSelected) {
                change(formConstants.FIELDS.dateRenouvellementCarteTP, formConstants.VALUES.anniversaireContrat);
                change(formConstants.FIELDS.dureeValiditeDroitsCarteTP, formConstants.VALUES.annuel);
            } else {
                change(formConstants.FIELDS.dateRenouvellementCarteTP, formConstants.VALUES.debutEcheance);
            }
            this.setState({
                debutEcheanceSelected: setEcheance,
            });
        }
        if (!setEcheance && dateDebutRenouvellement) {
            this.handleDateDebutRenouvellementChange(dateDebutRenouvellement);
        }
    }

    changePilotageBackOffice(setBackOffice) {
        const { change } = this.props;
        if (setBackOffice) {
            change(formConstants.FIELDS.dureeValiditeDroitsCarteTP, formConstants.VALUES.annuel);
        }
    }

    changeExemple(e) {
        const { t } = this.props;
        const { value } = e.target;
        if (value) {
            this.setState({
                exemple: t('parametersTPContrat.delaiDeclenchementExemple', {
                    days: value === '' || !value ? '0' : value,
                }),
            });
        }
    }

    changeDebutEcheance(value) {
        const { change, calculatedErrorsThirdStep } = this.props;
        if (value && Object.keys(value).length > 1) {
            change(formConstants.FIELDS.debutEcheance, moment(value).format(Constants.DAY_MONTH_FORMAT));
            calculatedErrorsThirdStep.missingDebutEcheance = false;
            this.setState({ missingDebutEcheance: false });
        } else {
            change(formConstants.FIELDS.debutEcheance, null);
            calculatedErrorsThirdStep.missingDebutEcheance = true;
            this.setState({ missingDebutEcheance: true });
        }
    }

    handleAnnulChange(checked) {
        const { change } = this.props;
        change(formConstants.FIELDS.annulDroitsOffline, checked);
        this.setState({
            annulDroitsOffline: checked,
        });
    }

    handleDateDebutRenouvellementChange(value) {
        const currentYear = moment().year();
        const selectedYear = moment(value).year();
        this.setState({
            dateDebutRenouvellement: value,
        });

        if (selectedYear > currentYear) {
            this.confirmRef.open();
        }
    }

    renderDateValidite(autoOrPilotage) {
        const { t, paramPriorites } = this.props;
        return (
            <Panel border={false}>
                <PanelSection>
                    <Row>
                        <Col>
                            <Field
                                id="file-tracking-search-ending-date"
                                name={formConstants.FIELDS.debutValidite}
                                component={CommonDatePicker}
                                label={t('parametersDroits.form.debutValidite')}
                                minDate={now}
                                validate={required}
                                showRequired
                                formGroupClassName="pl-4"
                            />
                        </Col>
                        {autoOrPilotage && (
                            <Col>
                                <Field
                                    id="priorite"
                                    name={formConstants.FIELDS.priorite}
                                    component={CommonInput}
                                    type="number"
                                    label={t('parametersDroits.form.priorite')}
                                    validate={[required, isNumber, minOne, dontInclude(paramPriorites)]}
                                    showRequired
                                    formGroupClassName="pl-4"
                                />
                            </Col>
                        )}
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    renderDateDeclenchement(delaiDeclenchementAutoSelected) {
        const { t } = this.props;
        const { exemple, debutEcheanceSelected, annulDroitsOffline } = this.state;

        const isEcheanceRequired = true;
        return (
            <Panel border={false}>
                <PanelSection>
                    {delaiDeclenchementAutoSelected && (
                        <PanelSection>
                            <Row>
                                <Col xs={3}>
                                    <Field
                                        name={formConstants.FIELDS.delaiDeclenchementAuto}
                                        component={CommonInput}
                                        validate={[required, isNumber, minValue, maxValue]}
                                        label={t('parametersTPContrat.delaiDeclenchement')}
                                        placeholder="0"
                                        onChange={(e) => this.changeExemple(e)}
                                        formGroupClassName="pl-2"
                                    />
                                </Col>
                                <Col xs={8} className="pl-5 pt-5">
                                    <div>{exemple}</div>
                                </Col>
                            </Row>
                        </PanelSection>
                    )}
                    {!delaiDeclenchementAutoSelected && (
                        <PanelSection>
                            <Row>
                                <Col xs="4" className="mt-2">
                                    <Field
                                        id="echeance-date"
                                        name={formConstants.FIELDS.dateExecutionBatch}
                                        component={CommonDatePicker}
                                        minDate={now}
                                        validate={required}
                                        label={t('parametersTPContrat.dateExecutionBatch')}
                                        showRequired={isEcheanceRequired}
                                        formGroupClassName="pl-2"
                                    />
                                </Col>
                                <Col xs="4" className="mt-2">
                                    <Field
                                        id="echeance-date"
                                        name={formConstants.FIELDS.dateDebutDroitTP}
                                        component={CommonDatePicker}
                                        validate={required}
                                        label={t('parametersTPContrat.dateDebutDroitTP')}
                                        showRequired={isEcheanceRequired}
                                        formGroupClassName="pl-2"
                                        onChange={
                                            !debutEcheanceSelected
                                                ? (value) => this.handleDateDebutRenouvellementChange(value)
                                                : (value) =>
                                                      this.setState({
                                                          dateDebutRenouvellement: value,
                                                      })
                                        }
                                    />
                                </Col>
                                <Col xs="4" className="mt-5">
                                    <Input
                                        name={formConstants.FIELDS.annulDroitsOffline}
                                        type="checkbox"
                                        id="annulation-droits-offline"
                                        onChange={(e) => this.handleAnnulChange(e.target.checked)}
                                        checked={annulDroitsOffline}
                                    />
                                    <Label id="annulation-droits-offline-label" check for="annulation-droits-offline">
                                        {t('parametersTPContrat.annulDroitsOffline')}
                                    </Label>
                                    <CgIcon name="information-tooltip" className="default" />
                                    <Tooltip target="annulation-droits-offline-label" placement="bottom">
                                        {t('parametersTPContrat.annulDroitsOfflineTooltip')}
                                    </Tooltip>
                                </Col>
                            </Row>
                        </PanelSection>
                    )}
                </PanelSection>
            </Panel>
        );
    }

    renderDebutEcheance(selectedMode) {
        const { t, calculatedErrorsThirdStep } = this.props;
        const { debutEcheanceSelected, missingDebutEcheance } = this.state;

        if (debutEcheanceSelected && missingDebutEcheance) {
            calculatedErrorsThirdStep.missingDebutEcheance = true;
        }
        const debutEcheanceProps = {};
        if (debutEcheanceSelected) {
            debutEcheanceProps.validate = required;
            debutEcheanceProps.showRequired = true;
        }

        return (
            <Panel border={false} className="pl-4">
                <PanelSection>
                    {(selectedMode === formConstants.VALUES.auto || selectedMode === formConstants.VALUES.manuel) && (
                        <ButtonGroup>
                            <Button
                                onClick={() => this.changeEcheance(true)}
                                outline
                                type="button"
                                active={debutEcheanceSelected}
                            >
                                {t('parametersTPContrat.debutEcheance')}
                            </Button>
                            <Button
                                onClick={() => this.changeEcheance(false)}
                                outline
                                type="button"
                                active={!debutEcheanceSelected}
                            >
                                {t('parametersTPContrat.anniversaireContrat')}
                            </Button>
                        </ButtonGroup>
                    )}
                    <PanelSection>
                        <Row>
                            <Col xs="4" className="mt-2">
                                {selectedMode === formConstants.VALUES.manuel && (
                                    <Row>
                                        {debutEcheanceSelected && (
                                            <Col xs="1">
                                                <CgIcon name="attention" size="2x" />
                                            </Col>
                                        )}
                                        <Col>
                                            {debutEcheanceSelected && (
                                                <h4>{t('parametersTPContrat.dateAnniversaireContrat')}</h4>
                                            )}
                                            {!debutEcheanceSelected && (
                                                <div>
                                                    <Col>
                                                        <Field
                                                            id="delai-renouvellement"
                                                            name={formConstants.FIELDS.delaiRenouvellement}
                                                            component={CommonInput}
                                                            validate={[required, isNumber, minValue]}
                                                            label={t('parametersTPContrat.delaiRenouvellement')}
                                                            showRequired
                                                            formGroupClassName="pl-2"
                                                        />
                                                    </Col>
                                                </div>
                                            )}
                                        </Col>
                                    </Row>
                                )}
                                {selectedMode === formConstants.VALUES.PilotageBO && (
                                    <Row>
                                        <Col xs="1">
                                            <CgIcon name="attention" size="2x" />
                                        </Col>
                                        <Col>
                                            <h4>{t('parametersTPContrat.dateAnniversaireContratPilotage')}</h4>
                                        </Col>
                                    </Row>
                                )}
                                {debutEcheanceSelected && selectedMode === formConstants.VALUES.auto && (
                                    <Field
                                        id="file-tracking-search-ending-date"
                                        name={formConstants.FIELDS.debutEcheance}
                                        component={CommonDatePicker}
                                        dateFormat={[Constants.DAY_MONTH_FORMAT]}
                                        displayedFormat={Constants.DAY_MONTH_FORMAT}
                                        validate={required}
                                        onChange={(value) => this.changeDebutEcheance(value)}
                                        behavior={missingDebutEcheance ? 'danger' : ''}
                                        {...debutEcheanceProps}
                                    />
                                )}
                                {!debutEcheanceSelected && selectedMode === formConstants.VALUES.auto && (
                                    <h6 className="text-muted">{t('parametersDroits.form.dateAnniversaireContrat')}</h6>
                                )}
                            </Col>
                            <Col xs="1" className="d-flex justify-content-center">
                                <div className="dividerV pt-5 mt-2" />
                            </Col>
                            <Col xs="5">
                                <Row>
                                    <h4 className={debutEcheanceSelected ? 'pl-4' : 'pl-4 text-muted'}>
                                        {t('parametersDroits.form.dureeValiditeDroits')}
                                    </h4>
                                </Row>
                                <Row>
                                    <Col xs="3">
                                        <Field
                                            type="radio"
                                            name={formConstants.FIELDS.dureeValiditeDroitsCarteTP}
                                            component={CommonRadio}
                                            label={t('parametersDroits.form.dureeValiditeDroitsAnnuel')}
                                            value={formConstants.VALUES.annuel}
                                            cgdComment={false}
                                            labelPortion={0}
                                            validate={required}
                                            disabled={
                                                selectedMode === formConstants.VALUES.PilotageBO
                                                    ? true
                                                    : !debutEcheanceSelected
                                            }
                                            inline
                                        />
                                    </Col>
                                    <Col xs="4">
                                        <Field
                                            type="radio"
                                            name={formConstants.FIELDS.dureeValiditeDroitsCarteTP}
                                            component={CommonRadio}
                                            label={t('parametersDroits.form.dureeValiditeDroitsSemestriel')}
                                            value={formConstants.VALUES.semestriel}
                                            cgdComment={false}
                                            labelPortion={0}
                                            validate={required}
                                            disabled={
                                                selectedMode === formConstants.VALUES.PilotageBO
                                                    ? true
                                                    : !debutEcheanceSelected
                                            }
                                            inline
                                        />
                                    </Col>
                                    <Col xs="1" className="pl-0">
                                        <Field
                                            type="radio"
                                            name={formConstants.FIELDS.dureeValiditeDroitsCarteTP}
                                            component={CommonRadio}
                                            label={t('parametersDroits.form.dureeValiditeDroitsTrimestriel')}
                                            value={formConstants.VALUES.trimestriel}
                                            cgdComment={false}
                                            labelPortion={0}
                                            validate={required}
                                            disabled={
                                                selectedMode === formConstants.VALUES.PilotageBO
                                                    ? true
                                                    : !debutEcheanceSelected
                                            }
                                            inline
                                        />
                                    </Col>
                                </Row>
                            </Col>
                        </Row>
                    </PanelSection>
                </PanelSection>
            </Panel>
        );
    }

    renderTabs() {
        const { t } = this.props;
        const { tab } = this.state;

        switch (tab) {
            case '0':
                return this.renderAutoTab();
            case '1':
                return this.renderManualTab();
            case '2':
                this.changePilotageBackOffice(true);
                return this.renderBackOfficeTab();
            default:
                return <span>{t('parametersTPContrat.erreurAffichageOnglet')}</span>;
        }
    }

    renderAutoTab() {
        const { t, change } = this.props;
        change(formConstants.FIELDS.modeDeclenchement, formConstants.VALUES.auto);
        return (
            <span>
                <Panel className="ml-3 mr-3">
                    <PanelSection>{this.renderDateValidite(true)}</PanelSection>
                    <PanelSection>{this.renderDateDeclenchement(true)}</PanelSection>
                    <PanelSection>{this.renderDebutEcheance(formConstants.VALUES.auto)}</PanelSection>
                    <PanelSection>
                        <Row>
                            <Col xs="12">
                                <Panel border={false}>
                                    <PanelSection>
                                        <Row className="mr-4">
                                            <Col xs={8}>
                                                <span>
                                                    {t('parametersDroits.form.seuilSecurite')}{' '}
                                                    <CgIcon
                                                        fixedWidth
                                                        name="information-tooltip"
                                                        id="information-tooltip"
                                                    />{' '}
                                                    <Tooltip placement="top" target="information-tooltip">
                                                        {t('parametersDroits.form.seuilTooltip')}
                                                    </Tooltip>
                                                </span>
                                                <div>
                                                    <Field
                                                        name={formConstants.FIELDS.seuilSecurite}
                                                        label={t('parametersDroits.form.seuil')}
                                                        component={CommonInput}
                                                        placeholder={t('parametersDroits.form.seuilPlaceholder')}
                                                        validate={isNumber}
                                                        labelPortion={9}
                                                        inline
                                                        cgdComment
                                                    />
                                                </div>
                                            </Col>
                                        </Row>
                                    </PanelSection>
                                </Panel>
                            </Col>
                        </Row>
                    </PanelSection>
                </Panel>
            </span>
        );
    }

    renderManualTab() {
        const { t, change } = this.props;
        const { dateDebutRenouvellement } = this.state;
        change(formConstants.FIELDS.modeDeclenchement, formConstants.VALUES.manuel);
        return (
            <span>
                <Panel className="ml-3 mr-3">
                    <PanelSection>{this.renderDateValidite(false)}</PanelSection>
                    <PanelSection>{this.renderDateDeclenchement(false)}</PanelSection>
                    <PanelSection>{this.renderDebutEcheance(formConstants.VALUES.manuel)}</PanelSection>
                    <PanelSection>
                        <ConfirmationModal
                            title={t('parametersTPContrat.confirmationModalDateRenouvellementTitle')}
                            ref={this.getReferenceFromConfirmModal}
                            onConfirm={this.onConfirm}
                            onCancel={this.onCancel}
                            message={t('parametersTPContrat.confirmationModalDateRenouvellement', {
                                date: moment(dateDebutRenouvellement).format('DD/MM/YYYY'),
                            })}
                            confirmButton={t('parametersTPContrat.confirmationYes')}
                            cancelButton={t('parametersTPContrat.confirmationNo')}
                        />
                        <Row>
                            <Col xs="12">
                                <Panel border={false}>
                                    <PanelSection>
                                        <Row className="mr-4">
                                            <Col xs={8} className="pt-2">
                                                <span>
                                                    {t('parametersDroits.form.seuilSecurite')}{' '}
                                                    <CgIcon
                                                        fixedWidth
                                                        name="information-tooltip"
                                                        id="information-tooltip"
                                                    />{' '}
                                                    <Tooltip placement="top" target="information-tooltip">
                                                        {t('parametersDroits.form.seuilTooltip')}
                                                    </Tooltip>
                                                </span>
                                                <div>
                                                    <Field
                                                        name={formConstants.FIELDS.seuilSecurite}
                                                        label={t('parametersDroits.form.seuil')}
                                                        component={CommonInput}
                                                        placeholder={t('parametersDroits.form.seuilPlaceholder')}
                                                        validate={isNumber}
                                                        labelPortion={9}
                                                        inline
                                                        cgdComment
                                                    />
                                                </div>
                                            </Col>
                                        </Row>
                                    </PanelSection>
                                </Panel>
                            </Col>
                        </Row>
                    </PanelSection>
                </Panel>
            </span>
        );
    }

    renderBackOfficeTab() {
        const { change } = this.props;
        change(formConstants.FIELDS.modeDeclenchement, formConstants.VALUES.PilotageBO);
        return (
            <span>
                <Panel className="ml-3 mr-3">
                    <PanelSection>{this.renderDateValidite(true)}</PanelSection>
                    <PanelSection>{this.renderDebutEcheance(formConstants.VALUES.PilotageBO)}</PanelSection>
                </Panel>
            </span>
        );
    }

    render() {
        const { t, calculatedErrorsThirdStep } = this.props;
        const { debutEcheanceSelected, missingDebutEcheance, tab } = this.state;

        if (debutEcheanceSelected && missingDebutEcheance) {
            calculatedErrorsThirdStep.missingDebutEcheance = true;
        }
        const listeModesDeclenchement = Object.entries([
            t('parametersTPContrat.declenchementAuto'),
            t('parametersTPContrat.declenchementManuel'),
            t('parametersTPContrat.pilotageBackOffice'),
        ]);

        return (
            <Fragment>
                <Nav tabs className="ml-5 mr-5">
                    {(Object.keys(listeModesDeclenchement) || []).map((key) => {
                        const titre = listeModesDeclenchement[key][1];
                        return (
                            <NavItem tabs>
                                <NavLink active={tab === key} onClick={() => this.setTab(key)}>
                                    {titre}
                                </NavLink>
                            </NavItem>
                        );
                    })}
                </Nav>
                <PanelSection>
                    {this.renderTabs()}
                    <div className="cgd-comment mt-1 small top-border">{t('mandatoryHint')} </div>
                </PanelSection>
            </Fragment>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    change: PropTypes.func,
    calculatedErrorsThirdStep: PropTypes.arrayOf(PropTypes.shape()),
    paramPriorites: PropTypes.arrayOf(PropTypes.string),
    tab: PropTypes.string,
    debutEcheanceSelected: PropTypes.bool,
};
// Default props
const defaultProps = {};

// Add prop types
ThirdStepComponent.propTypes = propTypes;
// Add default props
ThirdStepComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ThirdStepComponent;
