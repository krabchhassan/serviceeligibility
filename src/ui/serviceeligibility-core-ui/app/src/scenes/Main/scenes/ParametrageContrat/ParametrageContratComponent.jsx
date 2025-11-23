/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import get from 'lodash/get';
import autobind from 'autobind-decorator';
import {
    BodyHeader,
    BreadcrumbPart,
    Button,
    Col,
    DatePicker,
    PageLayout,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Workflow,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import moment from 'moment';
import { CommonCombobox, CommonInput } from '../../../../common/utils/Form/CommonFields';
import StringUtils from '../../../../common/utils/StringUtils';
import './style.module.scss';
import DateUtils from '../../../../common/utils/DateUtils';
import businessUtils from '../../../../common/utils/businessUtils';
import SecondStepComponent from './Components/SecondStepComponent';
import formConstants from '../ParametrageContrat/Constants';
import LabelValuePresenter from '../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import ValidationFactories from '../../../../common/utils/Form/ValidationFactories';
import CommonSpinner from '../../../../common/components/CommonSpinner/CommonSpinner';
import Constants from '../../../../common/utils/Constants';
import ParametrageBreadcrumb from '../Parametrage/components/ParametrageBreadcrumb';
import GTLotSummaryComponent from '../ParametrageDroits/components/Create/Components/GTLotSummaryComponent';
import ModalCartes from './Components/ModalCartes';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();

const modifyBody = (body, amc, contrat, numeroAdherent) => {
    if (amc) {
        body.amc = amc.value;
    }
    if (contrat) {
        body.contrat = contrat;
    }
    if (numeroAdherent) {
        body.numeroAdherent = numeroAdherent;
    }
};

const errorMapping = {
    'NEXT-SERVICEELIGIBILITY-CORE-00055': 'parametrageContrat.noContract',
    'NEXT-SERVICEELIGIBILITY-CORE-00056': 'parametrageContrat.noOfferOrProduct',
    'NEXT-SERVICEELIGIBILITY-CORE-00057': 'parametrageContrat.noSettings',
    'NEXT-SERVICEELIGIBILITY-CORE-00058': 'parametrageContrat.noTrigger',
    'NEXT-SERVICEELIGIBILITY-CORE-00059': 'parametrageContrat.noBobb',
    'NEXT-SERVICEELIGIBILITY-CORE-00060': 'parametrageContrat.noOc',
    'NEXT-SERVICEELIGIBILITY-CORE-00061': 'parametrageContrat.noPw',
};
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common', 'breadcrumb', 'errors'], { wait: true })
class ParametrageContratComponent extends Component {
    constructor(props) {
        super(props);

        const { t } = props;

        const workflowData = [
            {
                id: 0,
                description: StringUtils.splitIntoDivs(t('parametrageContrat.workflow1Title')),
                complete: false,
            },
            {
                id: 1,
                description: StringUtils.splitIntoDivs(t('parametrageContrat.workflow2Title')),
                complete: false,
            },
        ];

        this.state = {
            workflowData,
            selectedStep: 0,
            contratIndividuelSelected: true,
            reset: true,
            missingDateDeclenchement: true,
            dateDeclenchementManuel: null,
            isLoading: false,
            modal: false,
            commonBody: null,
        };
    }

    componentDidMount() {
        const { getLightDeclarants, getAllReturnCodes, getCodesItelis } = this.props;
        getLightDeclarants();
        getAllReturnCodes();
        getCodesItelis();
    }

    getDateNaissance(identite) {
        const { t } = this.props;
        return t('parametrageContrat.birthDate') + DateUtils.formatDisplayDate((identite || {}).dateNaissance);
    }

    getRangNaissance(identite) {
        const { t } = this.props;
        return t('parametrageContrat.birthRank') + (identite || {}).rangNaissance;
    }

    changeDateDeclenchement(value) {
        const { change } = this.props;
        if (value) {
            change(formConstants.FIELDS.dateDeclenchementManuel, value);
            this.setState({
                missingDateDeclenchement: false,
                dateDeclenchementManuel: value,
            });
        } else {
            change(formConstants.FIELDS.dateDeclenchementManuel, null);
            this.setState({ missingDateDeclenchement: true, dateDeclenchementManuel: null });
        }
    }

    buildRow = (nom, formattedNir, nir, dateNaissance, rangNaissance) => (
        <Row>
            {`${(nom || {}).civilite || ''} ${(nom || {}).nomFamille || '-'} ${
                (nom || {}).prenom || '-'
            } ${formattedNir} ${(nir || {}).cle} ${dateNaissance} ${rangNaissance || '-'}`}
        </Row>
    );

    @autobind
    resetSearchFunction() {
        const { reset } = this.props;
        this.setState({
            selectedStep: 0,
            reset: true,
            missingDateDeclenchement: true,
            dateDeclenchementManuel: null,
        });
        reset();
    }

    @autobind
    startSearch(values) {
        const { t, getContratIndiv, getBeneficiaries, addAlertError, getDeclarant, getLotsById } = this.props;

        const body = {};
        modifyBody(body, values.amc, values.contractNumber, values.numeroAdherent);

        getDeclarant(values.amc.value);

        getContratIndiv(body)
            .then((response) => {
                const beneficiariesObject = {
                    ...(values.amc && { numeroOuNomAMC: values.amc.value }),
                    ...(values.contractNumber && { numeroAdherentOuContrat: values.contractNumber.trim() }),
                    ...(values.numeroAdherent && { numeroAdherent: values.numeroAdherent.trim() }),
                };
                getBeneficiaries(beneficiariesObject);
                const idLots = response?.action?.payload?.body(false)?.idLots;
                if (idLots) {
                    getLotsById(idLots);
                }
            })
            .catch((...args) => {
                addAlertError({
                    message: t(errorMapping[get(args[0].response.data.errors[0], 'error_code')]),
                    behavior: 'danger',
                });
            });

        this.setState({
            reset: false,
        });
    }

    @autobind
    handleSubmit(values) {
        const { selectedStep } = this.state;

        this.setState({
            isLoading: true,
        });

        if (selectedStep === 1) {
            this.setState({
                commonBody: {
                    emitter: 'Request',
                    idDeclarant: values.amc.value,
                    numeroAdherent: values.numeroAdherent,
                    individualContractNumber: values.contractNumber.trim(),
                    date: moment(values.dateDeclenchementManuel).format(Constants.DEFAULT_DATE_PICKER_FORMAT),
                },
                modal: true,
            });
        }
    }

    resetSearch() {
        const { reset } = this.props;
        this.setState({
            selectedStep: 0,
            reset: true,
            missingDateDeclenchement: true,
            dateDeclenchementManuel: null,
            isLoading: false,
        });
        reset();
    }

    @autobind
    nextStepClick() {
        const { selectedStep } = this.state;
        this.setState({ selectedStep: selectedStep + 1 });
    }

    @autobind
    prevStepClick() {
        const { selectedStep } = this.state;
        this.setState({ selectedStep: selectedStep - 1 });
    }

    toggle() {
        const { modal } = this.state;
        const invModal = !modal;
        this.setState({ modal: invModal, isLoading: invModal });
    }

    generateTrigger(commonBody) {
        const { t, generateParametrageContrat, addAlertSuccess, addAlertError } = this.props;
        this.setState({
            modal: false,
        });

        generateParametrageContrat(commonBody)
            .then(() => {
                // Remet la page a son état de base
                this.resetSearch();

                // Créé un toaster pour informer l'utilisateur
                addAlertSuccess({
                    message: t('parametrageContrat.createdParams', {
                        individualContractNumber: commonBody.individualContractNumber,
                        idDeclarant: commonBody.idDeclarant,
                    }),
                });
            })
            .catch((...args) => {
                this.resetSearch();
                const receivedMessage = get(args[0].response.data.errors[0], 'message');
                const date = receivedMessage.match('[0-9]{4}([-/ .])[0-9]{2}[-/ .][0-9]{2}');
                const errorMessageDate = (date || [])[0];
                const displayDate = DateUtils.transformDateForDisplay(errorMessageDate);
                addAlertError({
                    message: receivedMessage.replace(errorMessageDate, displayDate),
                    behavior: 'danger',
                });
            });
    }

    renderModal() {
        const { amc, contratIndiv } = this.props;
        const { modal, commonBody } = this.state;
        const services = businessUtils.getListOpenServices(amc);
        commonBody.forcePapier = services.includes(Constants.CARTE_TP);
        commonBody.forceDemat = services.includes(Constants.CARTE_DEMATERIALISEE);

        const initForcePapier = commonBody.forcePapier && contratIndiv.parametrageDroitsCarteTP.isCarteEditablePapier;
        const initForceDemat = commonBody.forceDemat && contratIndiv.parametrageDroitsCarteTP.isCarteDematerialisee;
        return (
            ((commonBody.forcePapier || commonBody.forceDemat) && (
                <ModalCartes
                    toggle={() => this.toggle()}
                    modal={modal}
                    commonBody={commonBody}
                    validateFunc={(body) => this.generateTrigger(body)}
                    initialValues={{ forcePapier: initForcePapier, forceDemat: initForceDemat }}
                />
            )) ||
            this.generateTrigger(commonBody)
        );
    }

    renderIdentity() {
        const { beneficiaries } = this.props;
        return (
            <Fragment>
                {(beneficiaries || []).map((beneficiary) => {
                    const { identite } = beneficiary.metaMap || {};
                    const { contrats } = beneficiary.metaMap || {};
                    const { nom } = (contrats[0] || {}).data || {};
                    let { nir } = identite || {};
                    if (!nir) {
                        const { affiliationsRO } = identite || {};
                        const affiliationRO = (affiliationsRO || {})[0];
                        ({ nir } = affiliationRO || {});
                    }
                    const formattedNir = businessUtils.formatRO((nir || {}).code);
                    const dateNaissance = this.getDateNaissance(identite);
                    const rangNaissance = this.getRangNaissance(identite);
                    return this.buildRow(nom, formattedNir, nir, dateNaissance, rangNaissance);
                })}
            </Fragment>
        );
    }

    renderLotsEtGaranties() {
        const { t, contratIndiv, comboLabelsLots } = this.props;
        const { idLots } = contratIndiv || {};
        const { garantieTechniques } = contratIndiv || {};
        const separator = ', ';
        const labelLots = (comboLabelsLots || []).filter((labelLot) => idLots.includes(labelLot.id));
        const concatLots = (labelLots || []).map((lot) => lot.label).join(separator);
        const concatGTs = (garantieTechniques || []).map((gt) => gt.codeAssureur).join(separator);
        return (
            <Fragment>
                <GTLotSummaryComponent concatLots={concatLots} concatGTs={concatGTs} />
                <LabelValuePresenter
                    className="pl-2 pt-2"
                    id="priorite"
                    label={t('parametrageContrat.priorite')}
                    value={`${(contratIndiv || {}).priorite}` || '-'}
                    labelPortion={1.5}
                    key="priorite"
                />
            </Fragment>
        );
    }

    renderContrat() {
        const { t, contratIndiv } = this.props;
        const { missingDateDeclenchement, dateDeclenchementManuel } = this.state;
        const { debutEcheance, dureeValiditeDroitsCarteTP, dateRenouvellementCarteTP } =
            contratIndiv.parametrageRenouvellement || {};

        const now = moment();

        return (
            <Panel header={<PanelHeader title={t('parametrageContrat.definitionDateDeclenchement')} />}>
                <PanelSection>
                    <Row className="align-items-end">
                        <Col xs="6">
                            <Panel border={false} panelTheme="secondary">
                                <PanelSection title={t('parametrageContrat.populationContrat')}>
                                    <LabelValuePresenter
                                        className="d-flex justify-content-start"
                                        id="identifiantCollectivite"
                                        label={t('parametrageContrat.identifiantCollectivite')}
                                        value={
                                            contratIndiv.identifiantCollectivite
                                                ? contratIndiv.identifiantCollectivite
                                                : '-'
                                        }
                                        labelPortion={1.5}
                                        key="identifiantCollectivite"
                                    />
                                    <LabelValuePresenter
                                        className="d-flex justify-content-start"
                                        id="groupePopulation"
                                        label={t('parametrageContrat.groupePopulation')}
                                        value={contratIndiv.groupePopulation ? contratIndiv.groupePopulation : '-'}
                                        labelPortion={1.5}
                                        key="groupePopulation"
                                    />
                                    <LabelValuePresenter
                                        className="d-flex justify-content-start"
                                        id="critereSecondaireDetaille"
                                        label={t('parametrageContrat.critereSecondaireDetaille')}
                                        value={
                                            contratIndiv.critereSecondaireDetaille
                                                ? contratIndiv.critereSecondaireDetaille
                                                : '-'
                                        }
                                        labelPortion={1.5}
                                        key="critereSecondaireDetaille"
                                    />
                                </PanelSection>
                            </Panel>
                        </Col>
                        <Col xs="6">
                            <Panel border={false} panelTheme="secondary" id="debutEcheancePanel">
                                <PanelSection title={t('parametrageContrat.dateEcheance')}>
                                    <LabelValuePresenter
                                        className="d-flex justify-content-start"
                                        id="debutEcheance"
                                        label={t('parametrageContrat.debutEcheance')}
                                        value={
                                            dateRenouvellementCarteTP === formConstants.VALUES.debutEcheance
                                                ? debutEcheance
                                                : t('parametrageContrat.anniversaireContrat')
                                        }
                                        labelPortion={1.5}
                                        key="debutEcheance"
                                    />
                                    <LabelValuePresenter
                                        className="d-flex justify-content-start"
                                        id="dureeValidite"
                                        label={t('parametrageContrat.dureeValiditeDroitsCarteTP')}
                                        value={dureeValiditeDroitsCarteTP || '-'}
                                        labelPortion={1.5}
                                        key="dureeValidite"
                                    />
                                </PanelSection>
                            </Panel>
                        </Col>
                    </Row>
                    <Row>
                        <Col xs="6">
                            <Panel border={false} panelTheme="secondary">
                                <PanelSection title={t('parametrageContrat.identificationAssures')}>
                                    {this.renderIdentity()}
                                </PanelSection>
                            </Panel>
                        </Col>
                        <Col xs="6">
                            <Panel border={false} panelTheme="secondary">
                                <PanelSection title={t('parametrageContrat.lotEtGTAssocie')}>
                                    {this.renderLotsEtGaranties()}
                                </PanelSection>
                            </Panel>
                        </Col>
                    </Row>
                </PanelSection>
                <PanelSection>
                    <Row className="align-items-end">
                        <Col xs="5" sm="6">
                            <h5>{t('parametrageContrat.dateDeclenchementManuel')}</h5>
                            <DatePicker
                                name={formConstants.FIELDS.dateDeclenchementManuel}
                                minDate={now}
                                onChange={(value) => this.changeDateDeclenchement(value)}
                                behavior={missingDateDeclenchement ? 'danger' : 'success'}
                                value={dateDeclenchementManuel}
                            />
                            {missingDateDeclenchement && (
                                <div className="mt-0 invalid-feedback">{t('fields.required')}</div>
                            )}
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    renderContractSelection() {
        const { t, handleSubmit, invalid, lightDeclarants } = this.props;
        const { contratIndividuelSelected } = this.state;
        const amcs = (lightDeclarants || []).sort((a, b) => (a.nom > b.nom ? 1 : -1));
        const options = (amcs || []).map((item) => ({
            value: item.numero,
            label: `${item.numero} - ${item.nom}`,
            key: item.nom,
        }));

        return (
            <form id="search" onSubmit={handleSubmit(this.startSearch)}>
                <Panel border={false}>
                    <PanelSection>
                        <Row>
                            <Col xs="4" id="amc" className="mt-3">
                                <Field
                                    id="parametrage-contrat-amc"
                                    name="amc"
                                    component={CommonCombobox}
                                    label={t('parametrageContrat.amc')}
                                    options={options}
                                    placeholder={t('parametrageContrat.selectAMC')}
                                    clearable
                                    showRequired
                                    validate={required}
                                    searchable
                                />
                            </Col>
                            <Col xs="4" id="numeroAdherent" className="mt-3">
                                <Field
                                    id="parametrage-numeroAdherent"
                                    name="numeroAdherent"
                                    component={CommonInput}
                                    label={t('parametrageContrat.numeroAdherent')}
                                    placeholder={t('parametrageContrat.selectNumeroAdherent')}
                                    showRequired
                                    validate={required}
                                />
                            </Col>
                            <Col xs="3" id="contratNumber" className="mt-3">
                                <Field
                                    id="parametrage-contratNumber"
                                    name="contractNumber"
                                    component={CommonInput}
                                    label={t('parametrageContrat.contratIndividuel')}
                                    placeholder={t('parametrageContrat.contractNumberPlaceholder')}
                                    showRequired
                                    validate={required}
                                />
                            </Col>
                        </Row>
                        <Row>
                            <Col />
                            <Col xs="auto auto form-group" className="col">
                                <Button
                                    type="button"
                                    outline
                                    onClick={this.resetSearchFunction}
                                    id="clear-search-beneficiary"
                                >
                                    <CgIcon name="undo" size="1x" />
                                </Button>
                                <Button
                                    id="start-search"
                                    className="pl-2 ml-2"
                                    type="submit"
                                    behavior="primary"
                                    disabled={invalid || !contratIndividuelSelected}
                                >
                                    {t('parametrageContrat.apply')}
                                </Button>
                            </Col>
                        </Row>
                    </PanelSection>
                </Panel>
            </form>
        );
    }

    renderFirst() {
        const { contratIndiv, loading, loadingBeneficiaries, loadingAmc } = this.props;
        const { reset } = this.state;

        const canRender = !loading && !loadingBeneficiaries && !loadingAmc;

        if (!canRender) {
            return <CommonSpinner />;
        }

        return contratIndiv !== undefined && !reset && this.renderContrat();
    }

    renderButtons() {
        const { t, pristine, invalid, contratIndiv } = this.props;
        const { selectedStep, reset, missingDateDeclenchement } = this.state;

        return (
            <div className="d-flex justify-content-between">
                {contratIndiv !== undefined && !reset && (
                    <Button outline type="button" onClick={() => this.resetSearchFunction()}>
                        {t('cancel')}
                    </Button>
                )}
                <span>
                    {contratIndiv !== undefined && !reset && selectedStep > 0 && (
                        <span className="pr-2">
                            {' '}
                            <Button onClick={this.prevStepClick} type="button">
                                {t('previous')}
                            </Button>
                        </span>
                    )}
                    {contratIndiv !== undefined && !reset && selectedStep === 0 && (
                        <Button
                            onClick={this.nextStepClick}
                            behavior="primary"
                            type="button"
                            disabled={pristine || invalid || missingDateDeclenchement}
                        >
                            {t('next')}
                        </Button>
                    )}
                    {contratIndiv !== undefined && !reset && selectedStep === 1 && (
                        <Button
                            className="pl-2"
                            behavior="primary"
                            type="submit"
                            disabled={pristine || invalid || missingDateDeclenchement}
                        >
                            {t('validate')}
                        </Button>
                    )}
                </span>
            </div>
        );
    }

    render() {
        const { t, handleSubmit, contratIndiv, returnCodesList, codesItelis, showItelisCode } = this.props;
        const { workflowData, selectedStep, isLoading, modal } = this.state;

        return (
            <Fragment>
                <BreadcrumbPart label={t('breadcrumb.parametrageContrat')} parentPart={<ParametrageBreadcrumb />} />
                <PageLayout header={<BodyHeader title={t('parametrageContrat.pageTitle')} />}>
                    <Workflow data={workflowData} selectedStep={selectedStep} />
                    {selectedStep === 0 && this.renderContractSelection()}
                    <form id="paramContrat" onSubmit={handleSubmit(this.handleSubmit)}>
                        {selectedStep === 0 && this.renderFirst()}
                        {selectedStep === 1 && (
                            <SecondStepComponent
                                contratIndiv={contratIndiv}
                                isLoading={isLoading}
                                returnCodesList={returnCodesList}
                                codesItelis={codesItelis}
                                showItelisCode={showItelisCode}
                            />
                        )}
                        {!isLoading && this.renderButtons()}
                        {modal && this.renderModal()}
                    </form>
                </PageLayout>
            </Fragment>
        );
    }
}

ParametrageContratComponent.propTypes = {
    t: PropTypes.func,
    lightDeclarants: PropTypes.arrayOf(PropTypes.shape()),
    contratIndiv: PropTypes.shape(),
    handleSubmit: PropTypes.func,
    reset: PropTypes.func,
    addAlertError: PropTypes.func,
    change: PropTypes.func,
    pristine: PropTypes.bool,
    invalid: PropTypes.bool,
    loading: PropTypes.bool,
    loadingBeneficiaries: PropTypes.bool,
    getLightDeclarants: PropTypes.func,
    getContratIndiv: PropTypes.func,
    getBeneficiaries: PropTypes.func,
    beneficiaries: PropTypes.arrayOf(PropTypes.shape()),
    generateParametrageContrat: PropTypes.func,
    addAlertSuccess: PropTypes.func,
    comboLabelsLots: PropTypes.arrayOf(PropTypes.shape()),
    returnCodesList: PropTypes.shape(),
    getAllReturnCodes: PropTypes.func,
    getDeclarant: PropTypes.func,
    amc: PropTypes.shape(),
    loadingAmc: PropTypes.bool,
    getLotsById: PropTypes.func,
    getCodesItelis: PropTypes.func,
    codesItelis: PropTypes.arrayOf(PropTypes.shape()),
    showItelisCode: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    enableReinitialize: true,
    form: 'paramContratSearch',
})(ParametrageContratComponent);
