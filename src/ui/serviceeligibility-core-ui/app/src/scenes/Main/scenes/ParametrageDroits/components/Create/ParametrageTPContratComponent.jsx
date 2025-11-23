/* eslint-disable react/sort-comp */
/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import _ from 'lodash';
import autobind from 'autobind-decorator';
import { Field, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import moment from 'moment';
import {
    BodyHeader,
    BreadcrumbPart,
    Button,
    Col,
    ConfirmationModal,
    LoadingSpinner,
    PageLayout,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Workflow,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import './style.module.scss';
import { CommonCombobox, CommonMultiCombobox } from '../../../../../../common/utils/Form/CommonFields';
import formConstants from './Constants';
import SecondStep from './Components/SecondStep';
import history from '../../../../../../history';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import StringUtils from '../../../../../../common/utils/StringUtils';
import DateUtils from '../../../../../../common/utils/DateUtils';
import CommonSpinner from '../../../../../../common/components/CommonSpinner/CommonSpinner';
import ThirdStepComponent from './Components/ThirdStepComponent';
import ParametrageBreadcrumb from '../../../Parametrage/components/ParametrageBreadcrumb';
import businessUtils from '../../../../../../common/utils/businessUtils';
import Constants from '../../../../../../common/utils/Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();

const getOptionList = (list, prop) => {
    const all = [];
    (list || []).forEach((item) => {
        (item[prop] || []).forEach((item2) => {
            all.push(item2);
        });
    });
    const noDoubles = [...new Set(all)];
    return noDoubles.map((item2) => ({ label: item2, value: item2 }));
};

const goToSearchPage = (amcLabel) => {
    history.push({
        pathname: '/parameters/generateTPRigths',
        search: `?TPrightsGenerated=${amcLabel}`,
    });
};

const getTabModeDeclenchement = (modeDeclenchement) => {
    switch (modeDeclenchement) {
        case formConstants.VALUES.auto:
            return '0';
        case formConstants.VALUES.manuel:
            return '1';
        default:
            return '2';
    }
};

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common', 'errors'], { wait: true })
class ParametrageTPContratComponent extends Component {
    constructor(props) {
        super(props);

        const { t } = props;

        const workflowData = [
            {
                id: 0,
                description: t('parametersTPContrat.workflow1Title'),
                complete: false,
            },
            {
                id: 1,
                description: StringUtils.splitIntoDivs(t('parametersTPContrat.workflow3Title')),
                complete: false,
            },
            {
                id: 2,
                description: StringUtils.splitIntoDivs(t('parametersTPContrat.workflow2Title')),
                complete: false,
            },
        ];

        this.state = {
            workflowData,
            selectedStep: 0,
            groupesPopulation: [],
            identifiantsCollectivite: [],
            portefeuille: [],
            tabModeDeclenchement: '0',
            debutEcheanceSelected: true,
            hasInitialized: false,
            annulDroitsOffline: false,
            dureeValiditeDroitsCarteTP: formConstants.VALUES.annuel,
            missingDebutEcheance: true,
            delaiDeclenchementCarteTP: 0,
            codeItelisFromParam: null,
            detailsDroit: [],
            isFirstRender: true,
        };
        this.confirmRef = null;
        this.autocompleteGTs = _.debounce(this.autocompleteGTsCall, 1000);
    }

    componentDidMount() {
        const {
            getLightDeclarants,
            comboLightDeclarants,
            getDomainesTP,
            getLots,
            getCodesItelis,
            isCopy,
            getParamById,
            id,
            getAllReturnCodes,
            showItelisCode,
        } = this.props;
        if (isCopy) {
            getParamById(id);
        }
        if (!comboLightDeclarants) {
            getLightDeclarants();
        }
        getLots();
        getDomainesTP();
        if (showItelisCode) {
            getCodesItelis();
        }
        getAllReturnCodes();
    }

    componentDidUpdate(prevProps) {
        const {
            paramDroitsRefsList,
            selectedAmc,
            getParamForAmc,
            getPrioriteByAmc,
            initialize,
            paramToDuplicate,
            comboLabelsLightDeclarants,
            comboLabelsAllLot,
            returnCodesList,
        } = this.props;
        const { isFirstRender } = this.state;

        if (paramToDuplicate && prevProps?.paramToDuplicate && paramToDuplicate.id !== prevProps.paramToDuplicate.id) {
            this.setHasInitialized();
        }
        if (this.shouldInitializeCopyParam()) {
            const matchingAmc = comboLabelsLightDeclarants?.find((amc) => amc.number === paramToDuplicate.amc) || null;
            const matchingLots = comboLabelsAllLot?.filter((lot) => paramToDuplicate.idLots?.includes(lot.id)) || [];
            const matchingReturnCode = returnCodesList?.[paramToDuplicate.parametrageDroitsCarteTP.codeRenvoi] || '';
            const { codeDomaineTPList, conventionList, codeRenvoiActionList, codeRenvoiList } =
                this.buildDetailsDroitLists(paramToDuplicate.parametrageDroitsCarteTP.detailsDroit);

            const initialValues = this.prepareCopyParam(
                paramToDuplicate,
                matchingAmc,
                matchingLots,
                this.getFormattedCodeRenvoi(matchingReturnCode),
                codeDomaineTPList,
                conventionList,
                codeRenvoiActionList,
                codeRenvoiList,
            );
            initialize(initialValues);
            this.initializeFormState(paramToDuplicate);
            this.setHasInitialized();
        }
        if (prevProps.paramDroitsRefsList !== paramDroitsRefsList || isFirstRender) {
            this.changeOptions(
                getOptionList(paramDroitsRefsList, 'identifiantsCollectivite'),
                getOptionList(paramDroitsRefsList, 'groupesPopulation'),
                getOptionList(paramDroitsRefsList, 'portefeuille'),
            );
            this.setIsFirstRender();
        }
        if (prevProps.selectedAmc !== selectedAmc && prevProps.selectedAmc !== 'undefined') {
            if (!selectedAmc || !selectedAmc.number) {
                this.resetCriteria();
                this.changeOptions([], [], []);
            } else {
                getParamForAmc({ amcs: selectedAmc.number });
                getPrioriteByAmc(selectedAmc.number);
            }
        }
    }

    setIsFirstRender() {
        const { isFirstRender } = this.state;
        if (isFirstRender) {
            this.setState({ isFirstRender: false });
        }
    }

    shouldInitializeCopyParam() {
        const {
            isCopy,
            paramToDuplicate,
            comboLabelsLightDeclarants,
            comboLabelsAllLot,
            conventions,
            domainList,
            returnCodesList,
        } = this.props;
        const { hasInitialized } = this.state;

        return (
            isCopy &&
            paramToDuplicate &&
            comboLabelsLightDeclarants &&
            !hasInitialized &&
            (paramToDuplicate.idLots.length === 0 || comboLabelsAllLot) &&
            (paramToDuplicate.parametrageDroitsCarteTP.detailsDroit.length === 0 ||
                (domainList && conventions && returnCodesList))
        );
    }

    buildDetailsDroitLists(detailsDroit) {
        const { domainList, conventions, t, returnCodesList } = this.props;
        const optionActionsCodeRenvoi = businessUtils.getCodeRenvoiActions('parametersDroits', t);
        const optionCodesRenvoi = this.getCodeRenvoiOption(returnCodesList);

        const codeDomaineTPList = detailsDroit.map(
            (detail) =>
                domainList.find((domain) => domain.value.code === detail.codeDomaineTP)?.value || {
                    code: detail.codeDomaineTP,
                    label: detail.libelleDomaineTP,
                },
        );

        const conventionList = detailsDroit.map((detail) =>
            conventions.find((convention) => convention.code === detail.convention),
        );

        const codeRenvoiActionList = detailsDroit.map((detail) =>
            optionActionsCodeRenvoi.find((action) => action.code === detail.codeRenvoiAction),
        );

        const codeRenvoiList = detailsDroit.map((detail) => {
            return optionCodesRenvoi.find((option) => option.code === detail?.codeRenvoi) || null;
        });

        return { codeDomaineTPList, conventionList, codeRenvoiActionList, codeRenvoiList };
    }

    initializeFormState(initialValues) {
        const { parametrageRenouvellement, parametrageDroitsCarteTP } = initialValues;

        const nextState = {
            tabModeDeclenchement: getTabModeDeclenchement(parametrageRenouvellement.modeDeclenchement),
            debutEcheanceSelected:
                parametrageRenouvellement.dateRenouvellementCarteTP !== formConstants.VALUES.anniversaireContrat,
            annulDroitsOffline: parametrageRenouvellement.annulDroitsOffline === 'true',
            dureeValiditeDroitsCarteTP: parametrageRenouvellement.dureeValiditeDroitsCarteTP,
            missingDebutEcheance: parametrageRenouvellement.debutEcheance ? false : undefined,
            delaiDeclenchementCarteTP: parametrageRenouvellement.delaiDeclenchementCarteTP,
            detailsDroit: parametrageDroitsCarteTP.detailsDroit,
            codeItelisFromParam: parametrageDroitsCarteTP.codeItelis,
        };

        this.setState(nextState);
    }

    getFormattedCodeRenvoi = (returnCode) => {
        return returnCode?.code
            ? { code: returnCode.code, label: `${returnCode.code} - ${returnCode.libelle}` }
            : returnCode;
    };

    setHasInitialized() {
        const { hasInitialized } = this.state;
        this.setState({ hasInitialized: !hasInitialized });
    }

    prepareCopyParam = (
        paramDroit,
        amc,
        lots,
        codeRenvoi,
        codeDomaineTPList,
        conventionList,
        codeRenvoiActionList,
        codeRenvoiList,
    ) => ({
        ...paramDroit,
        parametrageDroitsCarteTP: {
            ...paramDroit.parametrageDroitsCarteTP,
            codeRenvoi,
            detailsDroit: paramDroit.parametrageDroitsCarteTP.detailsDroit.map((detail, index) => ({
                ...detail,
                codeDomaineTP: codeDomaineTPList[index],
                convention: conventionList[index],
                codeRenvoiAction: codeRenvoiActionList[index],
                codeRenvoi: codeRenvoiList[index],
            })),
        },
        parametrageRenouvellement: {
            ...paramDroit.parametrageRenouvellement,
            debutValidite: DateUtils.parseDateInFormat(
                paramDroit.parametrageRenouvellement.debutValidite,
                Constants.DEFAULT_DATE_TIME_FORMAT,
            ),
            dateExecutionBatch: DateUtils.parseDateInFormat(
                paramDroit.parametrageRenouvellement.dateExecutionBatch,
                Constants.DEFAULT_DATE_TIME_FORMAT,
            ),
            dateDebutDroitTP: DateUtils.parseDateInFormat(
                paramDroit.parametrageRenouvellement.dateDebutDroitTP,
                Constants.DEFAULT_DATE_TIME_FORMAT,
            ),
            debutEcheance: DateUtils.parseDateInFormat(
                paramDroit.parametrageRenouvellement.debutEcheance,
                Constants.DEFAULT_DATE_TIME_FORMAT,
            ),
        },
        amc,
        selectedLots: lots,
    });

    getCodeRenvoiOption() {
        const { returnCodesList } = this.props;
        const optionReturnCodesList = [];
        Object.values(returnCodesList || {}).forEach((codeR) => {
            optionReturnCodesList.push({ code: codeR.code, label: `${codeR.code} - ${codeR.libelle}` });
        });

        return optionReturnCodesList;
    }

    resetCriteria() {
        this.setState({ identifiantsCollectivite: [], groupesPopulation: [], portefeuille: [] });
    }

    @autobind onConfirm() {
        this.confirmRef.close();
        goToSearchPage();
    }

    @autobind getRef(ref) {
        this.ref = ref;
    }

    addFacultatif(label) {
        const { t } = this.props;
        return (
            <span>
                {label} - <span className="cgd-comment">{t('optional')}</span>
            </span>
        );
    }

    setDateRenouvellementCarteTP = (parametrageRenouvellement) =>
        parametrageRenouvellement.dateRenouvellementCarteTP === undefined ||
        parametrageRenouvellement.modeDeclenchement === formConstants.VALUES.PilotageBO
            ? formConstants.VALUES.debutEcheance
            : parametrageRenouvellement.dateRenouvellementCarteTP;

    setDebutEcheance = (parametrageRenouvellement) =>
        parametrageRenouvellement.dateRenouvellementCarteTP === formConstants.VALUES.anniversaireContrat ||
        parametrageRenouvellement.modeDeclenchement !== formConstants.VALUES.auto ||
        parametrageRenouvellement.debutEcheance == null
            ? null
            : moment(parametrageRenouvellement.debutEcheance).format(Constants.DAY_MONTH_FORMAT);

    setDureeValiditeDroitsCarteTP = (parametrageRenouvellement) =>
        parametrageRenouvellement.dureeValiditeDroitsCarteTP === undefined ||
        parametrageRenouvellement.dateRenouvellementCarteTP === formConstants.VALUES.anniversaireContrat
            ? formConstants.VALUES.annuel
            : parametrageRenouvellement.dureeValiditeDroitsCarteTP;

    setDateExecutionBatch = (parametrageRenouvellement) =>
        !parametrageRenouvellement.modeDeclenchement ||
        parametrageRenouvellement.modeDeclenchement === formConstants.VALUES.manuel
            ? parametrageRenouvellement.dateExecutionBatch
            : null;

    setDateDebutDroitTP = (parametrageRenouvellement) =>
        !parametrageRenouvellement.modeDeclenchement ||
        parametrageRenouvellement.modeDeclenchement === formConstants.VALUES.manuel
            ? parametrageRenouvellement.dateDebutDroitTP
            : null;

    setDelaiDeclenchementCarteTP = (parametrageRenouvellement) =>
        parametrageRenouvellement.modeDeclenchement === formConstants.VALUES.auto &&
        parametrageRenouvellement.delaiDeclenchementAuto
            ? parametrageRenouvellement.delaiDeclenchementAuto
            : 0;

    setAnnulDroitsOffline = (parametrageRenouvellement) =>
        parametrageRenouvellement.modeDeclenchement === formConstants.VALUES.manuel &&
        parametrageRenouvellement.annulDroitsOffline
            ? parametrageRenouvellement.annulDroitsOffline
            : null;

    setSeuilSecurite = (parametrageRenouvellement) =>
        parametrageRenouvellement.modeDeclenchement !== formConstants.VALUES.PilotageBO
            ? parametrageRenouvellement.seuilSecurite
            : null;

    setDelaiRenouvellement = (parametrageRenouvellement) =>
        parametrageRenouvellement.modeDeclenchement === formConstants.VALUES.manuel &&
        parametrageRenouvellement.dateRenouvellementCarteTP === formConstants.VALUES.anniversaireContrat
            ? parametrageRenouvellement.delaiRenouvellement
            : null;

    setDetailsDroit = (parametrageDroitsCarteTP) => [
        ...(parametrageDroitsCarteTP.detailsDroit || []).map((droit, index) => ({
            ordreAffichage: index + 1,
            codeDomaineTP: (droit.codeDomaineTP || {}).code,
            convention: (droit.convention || {}).code,
            libelleDomaineTP: droit.libelleDomaineTP || (droit.codeDomaineTP || {}).libelle,
            codeRenvoi: (droit.codeRenvoi || {}).code,
            codeRenvoiAction: (droit.codeRenvoiAction || {}).code,
        })),
    ];

    @autobind handleSubmit(values) {
        const { createParam } = this.props;
        const { selectedStep } = this.state;

        if (selectedStep === 2) {
            const commonBody = {
                ...values,
                amc: values.amc.number,
                amcNom: values.amc.value,
                identifiantCollectivite: (values.identifiantCollectivite || [])
                    .map((item) => (typeof item === 'string' ? item : item.value))
                    .join(','),
                groupePopulation: (values.groupePopulation || [])
                    .map((item) => (typeof item === 'string' ? item : item.value))
                    .join(','),
                critereSecondaireDetaille: (values.critereSecondaireDetaille || [])
                    .map((item) => (typeof item === 'string' ? item : item.value))
                    .join(','),
                dateDebutValidite: DateUtils.formatServerDate(
                    values.parametrageRenouvellement.debutValidite,
                    'YYYY-MM-DD',
                ),
                parametrageDroitsCarteTP: {
                    ...values.parametrageDroitsCarteTP,
                    codeConventionTP:
                        values?.parametrageDroitsCarteTP?.codeConventionTP?.code ??
                        values?.parametrageDroitsCarteTP?.codeConventionTP ??
                        '',
                    isCarteEditablePapier: values.parametrageDroitsCarteTP.isCarteEditablePapier === 'true',
                    isCarteDematerialisee: values.parametrageDroitsCarteTP.isCarteDematerialisee === 'true',
                    codeRenvoi: (values.parametrageDroitsCarteTP.codeRenvoi || {}).code,
                    detailsDroit: this.setDetailsDroit(values.parametrageDroitsCarteTP),
                    codeItelis: (values.parametrageDroitsCarteTP.codeItelis || {}).code,
                },
                parametrageRenouvellement: {
                    dateRenouvellementCarteTP: this.setDateRenouvellementCarteTP(values.parametrageRenouvellement),
                    debutEcheance: this.setDebutEcheance(values.parametrageRenouvellement),
                    dureeValiditeDroitsCarteTP: this.setDureeValiditeDroitsCarteTP(values.parametrageRenouvellement),
                    delaiDeclenchementCarteTP: this.setDelaiDeclenchementCarteTP(values.parametrageRenouvellement),
                    modeDeclenchement: !values.parametrageRenouvellement.modeDeclenchement
                        ? formConstants.VALUES.auto
                        : values.parametrageRenouvellement.modeDeclenchement,
                    dateExecutionBatch: DateUtils.formatServerDate(
                        this.setDateExecutionBatch(values.parametrageRenouvellement),
                        'YYYY-MM-DD',
                    ),
                    dateDebutDroitTP: DateUtils.formatServerDate(
                        this.setDateDebutDroitTP(values.parametrageRenouvellement),
                        'YYYY-MM-DD',
                    ),
                    annulDroitsOffline: this.setAnnulDroitsOffline(values.parametrageRenouvellement),
                    seuilSecurite: this.setSeuilSecurite(values.parametrageRenouvellement),
                    delaiRenouvellement: this.setDelaiRenouvellement(values.parametrageRenouvellement),
                },
                garantieTechniques: (values.selectedGTs || []).map((gt) => ({
                    codeAssureur: gt.number,
                    codeGarantie: gt.value,
                })),
                idLots: (values.selectedLots || []).map((lot) => lot.id),
                selectedGTs: null,
                selectedLots: null,
                priorite:
                    values.parametrageRenouvellement.modeDeclenchement === formConstants.VALUES.manuel
                        ? null
                        : values.priorite,
            };
            delete commonBody.id;
            createParam(commonBody).then(() => goToSearchPage(values.amc.value));
        }
    }

    changeOptions(identifiantsCollectivite, groupesPopulation, portefeuille) {
        const { change, selectedCollectivityID, selectedPopulationGroup, selectedCriterias } = this.props;

        this.setState({
            identifiantsCollectivite,
            groupesPopulation,
            portefeuille,
        });
        const newPopulationGroup = (selectedPopulationGroup || []).filter((item) =>
            groupesPopulation.some((item2) => item2.value === item),
        );
        const newColectivityID = (selectedCollectivityID || []).filter((item) =>
            identifiantsCollectivite.some((item2) => item2.value === item),
        );
        const newCriterial = (selectedCriterias || []).filter((item) =>
            portefeuille.some((item2) => item2.value === item),
        );
        change(formConstants.FIELDS.contratCollectivityID, newColectivityID);
        change(formConstants.FIELDS.contratPopulationGroup, newPopulationGroup);
        change(formConstants.FIELDS.contratCriteria, newCriterial);
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

    renderLotGT() {
        const { t, isLoadingLots, comboLabelsAllGT, comboLabelsAllLot } = this.props;
        return isLoadingLots ? (
            <CommonSpinner />
        ) : (
            <Panel header={<PanelHeader title={t('parametersTPContrat.selectLotGtTitle')} />}>
                <PanelSection>
                    <div className="pb-2">{t('parametersTPContrat.offersTip')}</div>
                    <Row className="align-items-start ">
                        <Col xs="12" sm="6">
                            <Field
                                name={formConstants.FIELDS.selectedLots}
                                component={CommonMultiCombobox}
                                label={t('parametersTPContrat.search_lot')}
                                placeholder={t('parametersTPContrat.filterLotPlaceholder')}
                                options={comboLabelsAllLot}
                                searchable
                                clearable
                            />
                        </Col>
                        <Col xs="12" sm="6">
                            <Field
                                name={formConstants.FIELDS.selectedGTs}
                                component={CommonMultiCombobox}
                                label={t('parametersTPContrat.search_gt')}
                                placeholder={t('parametersTPContrat.filterGTPlaceholder')}
                                options={comboLabelsAllGT}
                                filterOptions={businessUtils.filterAndLimit50}
                                onInputChange={(input) => this.autocompleteGTs(input)}
                                searchable
                                clearable
                            />
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    autocompleteGTsCall(input) {
        const { getAllGT } = this.props;
        if (input && input.length > 2) {
            const searchCriteria = {};
            searchCriteria.search = input;
            getAllGT(searchCriteria);
        }
    }

    renderFirst() {
        const { t, comboLabelsLightDeclarants } = this.props;
        const { groupesPopulation, identifiantsCollectivite, portefeuille } = this.state;
        return (
            <Fragment>
                <Panel header={<PanelHeader title={t('parametersTPContrat.populationSelection')} />}>
                    <PanelSection>
                        <Row className="pl-3 pb-2">
                            <div>{t('parametersTPContrat.explanationSentence')}</div>
                        </Row>
                        <Row>
                            <Col>
                                <Field
                                    name={formConstants.FIELDS.contratAMC}
                                    component={CommonCombobox}
                                    label={t('parametersTPContrat.amc')}
                                    options={comboLabelsLightDeclarants}
                                    placeholder={t('parametersTPContrat.amc_placeholder')}
                                    searchable
                                    validate={[required]}
                                    showRequired
                                />
                            </Col>
                            <Col>
                                <Field
                                    name={formConstants.FIELDS.contratCollectivityID}
                                    component={CommonMultiCombobox}
                                    label={this.addFacultatif(t('parametersTPContrat.identifiantCollectivite'))}
                                    options={identifiantsCollectivite}
                                    placeholder={t('parametersTPContrat.collectivite_placeholder')}
                                    disabled={!identifiantsCollectivite || identifiantsCollectivite.length === 0}
                                    searchable
                                />
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Field
                                    name={formConstants.FIELDS.contratPopulationGroup}
                                    component={CommonMultiCombobox}
                                    searchable
                                    label={this.addFacultatif(t('parametersTPContrat.groupePopulation'))}
                                    options={groupesPopulation}
                                    placeholder={t('parametersTPContrat.college_placeholder')}
                                    disabled={!groupesPopulation || groupesPopulation.length === 0}
                                />
                            </Col>
                            <Col>
                                <Field
                                    name={formConstants.FIELDS.contratCriteria}
                                    component={CommonMultiCombobox}
                                    searchable
                                    label={this.addFacultatif(t('parametersTPContrat.critereSecondaireDetaille'))}
                                    options={portefeuille}
                                    placeholder={t('parametersTPContrat.critereSecondaireDetaille_placeholder')}
                                    disabled={!portefeuille || portefeuille.length === 0}
                                />
                            </Col>
                        </Row>
                        <div className="cgd-comment mt-1 small top-border">{t('mandatoryHint')} </div>
                    </PanelSection>
                </Panel>
                {this.renderLotGT()}
            </Fragment>
        );
    }

    @autobind
    getReferenceFromConfirmModal(ref) {
        this.confirmRef = ref;
    }

    @autobind
    openModal() {
        this.confirmRef.open();
    }

    @autobind
    onCancel() {
        this.confirmRef.close();
    }

    getThirdStepErrors() {
        const {
            selectedDebutEcheance,
            selectedDateRenouvellement,
            selectedDelaiDeclenchementCarteTP,
            selectedDateExecutionBatch,
            selectedDateDebutDroitTP,
        } = this.props;
        return {
            missingDebutEcheance:
                !selectedDebutEcheance &&
                (selectedDateRenouvellement === formConstants.VALUES.debutEcheance || !selectedDateRenouvellement) &&
                selectedDelaiDeclenchementCarteTP === formConstants.VALUES.auto,
            missingDateDeclenchement:
                !selectedDateExecutionBatch &&
                !selectedDateDebutDroitTP &&
                selectedDelaiDeclenchementCarteTP === formConstants.VALUES.manuel,
        };
    }

    render() {
        const {
            t,
            handleSubmit,
            selectedAmc,
            selectedDroits,
            change,
            pristine,
            invalid,
            selectedCollectivityID,
            selectedPopulationGroup,
            selectedCriterias,
            selectedLots,
            selectedGTs,
            paramPriorites,
            returnCodesList,
            loadingParamToDuplicate,
            showItelisCode,
            domainList,
            conventions,
        } = this.props;
        const {
            workflowData,
            selectedStep,
            tabModeDeclenchement,
            debutEcheanceSelected,
            annulDroitsOffline,
            dureeValiditeDroitsCarteTP,
            missingDebutEcheance,
            delaiDeclenchementCarteTP,
            detailsDroit,
            codeItelisFromParam,
        } = this.state;
        const calculatedErrors = (selectedDroits || []).map((item) => ({
            codeDomaineTP: !(item.codeDomaineTP && item.codeDomaineTP.code),
        }));
        const hasErrors = selectedStep === 1 && calculatedErrors.some((item) => item.codeDomaineTP || item.convention);
        const calculatedErrorsThirdStep = this.getThirdStepErrors();
        const hasErrorsThirdStep =
            selectedStep === 2 &&
            (calculatedErrorsThirdStep.missingDebutEcheance || calculatedErrorsThirdStep.missingDateDeclenchement);
        const optionCodesRenvoi = this.getCodeRenvoiOption(returnCodesList);

        return (
            <PageLayout header={<BodyHeader title={t('parametersTPContrat.pageTitle')} />}>
                <BreadcrumbPart
                    label={t('breadcrumb.parametrageTPContrat')}
                    parentPart={
                        <BreadcrumbPart
                            label={t('breadcrumb.parametrageDroitsTP')}
                            path="/parameters/generateTPRigths"
                            parentPart={<ParametrageBreadcrumb />}
                        />
                    }
                />
                <Workflow data={workflowData} selectedStep={selectedStep} />
                <ConfirmationModal
                    title={t('parametersTPContrat.confirmationModalTitle')}
                    ref={this.getReferenceFromConfirmModal}
                    onConfirm={this.onConfirm}
                    onCancel={this.onCancel}
                    message={t('parametersTPContrat.confirmationModal')}
                    confirmButton={t('parametersTPContrat.confirmationYes')}
                    cancelButton={t('parametersTPContrat.confirmationNo')}
                />
                <form onSubmit={handleSubmit(this.handleSubmit)}>
                    {loadingParamToDuplicate && (
                        <LoadingSpinner
                            type="over-container"
                            iconSize="3x"
                            iconType="circle-o-notch"
                            behavior="primary"
                        />
                    )}
                    {selectedStep === 0 && this.renderFirst()}
                    {selectedStep === 2 && (
                        <ThirdStepComponent
                            change={change}
                            calculatedErrorsThirdStep={calculatedErrorsThirdStep}
                            paramPriorites={paramPriorites}
                            tab={tabModeDeclenchement}
                            debutEcheanceSelected={debutEcheanceSelected}
                            annulDroitsOffline={annulDroitsOffline}
                            dureeValiditeDroitsCarteTP={dureeValiditeDroitsCarteTP}
                            missingDebutEcheance={missingDebutEcheance}
                            delaiDeclenchementCarteTP={delaiDeclenchementCarteTP}
                        />
                    )}
                    {selectedStep === 1 && (
                        <SecondStep
                            selectedDroits={selectedDroits}
                            change={change}
                            calculatedErrors={calculatedErrors}
                            selectedCollectivityID={selectedCollectivityID}
                            selectedPopulationGroup={selectedPopulationGroup}
                            selectedCriterias={selectedCriterias}
                            selectedAmc={selectedAmc}
                            selectedLots={selectedLots}
                            selectedGTs={selectedGTs}
                            showItelisCode={showItelisCode}
                            codeItelisFromParam={codeItelisFromParam}
                            detailsDroit={detailsDroit}
                            domainList={domainList}
                            conventions={conventions}
                            optionCodesRenvoi={optionCodesRenvoi}
                        />
                    )}
                    <div className="d-flex justify-content-between">
                        <Button outline type="button" onClick={() => this.confirmRef.open()}>
                            {t('cancel')}
                        </Button>
                        <span>
                            {selectedStep > 0 && (
                                <span className="pr-2">
                                    {' '}
                                    <Button onClick={this.prevStepClick} type="button">
                                        {t('previous')}
                                    </Button>
                                </span>
                            )}
                            {(selectedStep === 1 || selectedStep === 0) && (
                                <Button
                                    onClick={this.nextStepClick}
                                    behavior="primary"
                                    type="button"
                                    disabled={pristine || invalid || hasErrors}
                                >
                                    {t('next')}
                                </Button>
                            )}
                            {selectedStep === 2 && (
                                <Button
                                    className="pl-2"
                                    behavior="primary"
                                    type="submit"
                                    disabled={pristine || invalid || hasErrorsThirdStep}
                                >
                                    {t('validate')}
                                </Button>
                            )}
                        </span>
                    </div>
                </form>
            </PageLayout>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    change: PropTypes.func,
    pristine: PropTypes.bool,
    invalid: PropTypes.bool,
    getLightDeclarants: PropTypes.func,
    handleSubmit: PropTypes.func,
    getCodesItelis: PropTypes.func,
    getDomainesTP: PropTypes.func,
    getParamForAmc: PropTypes.func,
    createParam: PropTypes.func,
    comboLightDeclarants: PropTypes.arrayOf(PropTypes.shape()),
    comboLabelsLightDeclarants: PropTypes.arrayOf(PropTypes.shape()),
    selectedAmc: PropTypes.arrayOf(PropTypes.shape()),
    selectedCollectivityID: PropTypes.arrayOf(PropTypes.shape()),
    selectedPopulationGroup: PropTypes.arrayOf(PropTypes.shape()),
    selectedCriterias: PropTypes.arrayOf(PropTypes.shape()),
    selectedDroits: PropTypes.arrayOf(PropTypes.shape()),
    selectedDebutEcheance: PropTypes.string,
    selectedDateRenouvellement: PropTypes.string,
    selectedDelaiDeclenchementCarteTP: PropTypes.string,
    paramDroitsRefsList: PropTypes.arrayOf(PropTypes.shape()),
    getLots: PropTypes.func,
    getAllGT: PropTypes.func,
    comboLabelsAllGT: PropTypes.arrayOf(PropTypes.shape()),
    comboLabelsAllLot: PropTypes.arrayOf(PropTypes.shape()),
    isLoadingLots: PropTypes.bool,
    selectedLots: PropTypes.arrayOf(PropTypes.shape()),
    selectedGTs: PropTypes.arrayOf(PropTypes.shape()),
    getPrioriteByAmc: PropTypes.func,
    paramPriorites: PropTypes.arrayOf(PropTypes.string),
    id: PropTypes.string,
    isCopy: PropTypes.bool,
    getParamById: PropTypes.func,
    paramToDuplicate: PropTypes.shape(),
    loadingParamToDuplicate: PropTypes.bool,
    getAllReturnCodes: PropTypes.func,
    returnCodesList: PropTypes.arrayOf(PropTypes.shape()),
    showItelisCode: PropTypes.bool,
    domainList: PropTypes.arrayOf(PropTypes.shape()),
};
// Default props
const defaultProps = {};

// Add prop types
ParametrageTPContratComponent.propTypes = propTypes;
// Add default props
ParametrageTPContratComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParametrageTPContratComponent);
