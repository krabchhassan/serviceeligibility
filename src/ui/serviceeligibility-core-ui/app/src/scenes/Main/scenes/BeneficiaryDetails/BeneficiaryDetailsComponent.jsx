/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import autobind from 'autobind-decorator';
import { Field, reduxForm } from 'redux-form';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import {
    BodyHeader,
    BreadcrumbPart,
    Col,
    Modal,
    ModalBody,
    ModalHeader,
    PageLayout,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Status,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import get from 'lodash.get';
import isEqual from 'lodash/isEqual';
import LabelValuePresenter from '../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import CommonSpinner from '../../../../common/components/CommonSpinner/CommonSpinner';
import history from '../../../../history';
import businessUtils from '../../../../common/utils/businessUtils';
import PrestationServiceItem from './components/PrestationServiceItem';
import PrestIJDetailComponent from './components/PrestIJDetailComponent';
import ServiceMetierUtils from '../Beneficiary/ServiceMetierUtils';
import BeneficiariesBreadcrumb from '../Beneficiary/components/BeneficiariesBreadcrumb';
import PrestationServiceUtils from './components/PrestationServiceUtils';
import HistoriqueDateNaissance from './components/HistoriqueDateNaissance';
import AffiliationsRO from './components/AffiliationsRO';
import { CommonRadio } from '../../../../common/utils/Form/CommonFields';
import formConstants from './Constants';
import ThirdPartyTab from '../BeneficiaryRightsDetails/ThirdPartyTab';
import PeriodUtils from '../../../../common/utils/PeriodUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const getData = (benefDetails) => benefDetails?.contrats?.[benefDetails.contrats.length - 1]?.data ?? null;

const getActiveNirs = (liste) => liste.filter((item) => PeriodUtils.isActivePeriod(item.periode));

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class BeneficiaryDetailsComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            declarationId: null,
            modalDateNaissance: false,
            historiqueDateNaissance: null,
            modalAffiliationsRO: false,
            affiliationsRO: null,
            selectedMenuTab: 0,
        };
    }

    componentDidMount() {
        const {
            id,
            env,
            saveBenefHistorique,
            getBeneficiaryTpDetails,
            tab,
            initialize,
            getOneBenef,
            getSasContrats,
            clearBeneficiaryDetails,
            clientType,
        } = this.props;
        clearBeneficiaryDetails();
        saveBenefHistorique(id, env);
        if (clientType === 'OTP') {
            getBeneficiaryTpDetails(id, env)
                .then(() => {
                    this.setId();
                })
                .catch((error) => {
                    console.error(error);
                });
        } else {
            getOneBenef(id);
            getSasContrats(id);
        }
        initialize({ selectedService: tab });
    }

    componentDidUpdate(prevProps) {
        const {
            id,
            env,
            tab,
            benefDetails,
            serviceMetierList,
            getOneDeclarant,
            consoFinished,
            getBeneficiaryTpDetails,
            clientType,
        } = this.props;
        if (prevProps.id !== id || consoFinished) {
            getBeneficiaryTpDetails(id, env)
                .then(() => {
                    this.setId();
                })
                .catch((error) => {
                    console.error(error);
                });
        }
        if (!isEqual(prevProps.benefDetails, benefDetails) || !benefDetails) {
            const serviceCodes = this.getServiceCodes(serviceMetierList);
            this.searchTab(tab, serviceCodes, clientType, benefDetails);
            if (benefDetails && benefDetails.amc && benefDetails.amc.idDeclarant) {
                getOneDeclarant(benefDetails.amc.idDeclarant);
            }
        }
    }

    setBenefNir = (identite) => {
        let nir = (identite.nir || {}).code;
        if (nir) {
            nir = businessUtils.formatRO(nir);
        } else if (identite.affiliationsRO && identite.affiliationsRO.length > 0 && identite.affiliationsRO[0].nir) {
            const activeNirs = getActiveNirs(identite.affiliationsRO);
            if (activeNirs.length > 0) {
                nir = businessUtils.formatRO(activeNirs[0].nir.code);
            }
        }
        return { nir };
    };

    setId() {
        const { declarationId } = this.props;
        this.setState({ declarationId });
    }

    @autobind
    setTab(selectedTab, serviceCodes) {
        const { id, benefDetails, clientType, env } = this.props;

        this.searchTab(selectedTab, serviceCodes, clientType, benefDetails);
        history.push(`/beneficiary_tracking/${env}/details/${id}/${selectedTab}`);
        this.forceUpdate();
    }

    @autobind
    setTabNav(selectedTab, serviceCodes) {
        const { id, benefDetails, clientType, change, env } = this.props;

        this.searchTab(selectedTab, serviceCodes, clientType, benefDetails);
        history.push(`/beneficiary_tracking/${env}/details/${id}/${selectedTab}`);
        this.forceUpdate();
        change(formConstants.FIELDS.selectedService, selectedTab);
    }

    getServiceTitles = (t, serviceMetierList) => {
        const servicesTitles = [];
        (Object.keys(serviceMetierList) || []).forEach((key) => {
            const service = serviceMetierList[key];
            servicesTitles[service.ordre] = service.libelle;
        });
        return servicesTitles;
    };

    getServiceIcons = (serviceMetierList) => {
        const servicesIcons = [];
        (Object.keys(serviceMetierList) || []).forEach((key) => {
            const service = serviceMetierList[key];
            servicesIcons[service.ordre] = service.icone;
        });
        return servicesIcons;
    };

    getServiceCodes = (serviceMetierList) => {
        const servicesCodes = [];
        (Object.keys(serviceMetierList) || []).forEach((key) => {
            const service = serviceMetierList[key];
            servicesCodes[service.ordre] = service.code;
        });
        return servicesCodes;
    };
    getSelectedMenuTab = (item) => {
        this.setState({ selectedMenuTab: item });
    };

    toggleDateNaissance(historiqueDateNaissance) {
        const { modalDateNaissance } = this.state;
        this.setState({ modalDateNaissance: !modalDateNaissance, historiqueDateNaissance });
    }

    toggleAffiliationsRO(affiliationsRO) {
        const { modalAffiliationsRO } = this.state;
        this.setState({ modalAffiliationsRO: !modalAffiliationsRO, affiliationsRO });
    }

    searchTab(tab, serviceCodes, clientType, benef = null) {
        if (serviceCodes[tab] === 'Service_TP') {
            if (clientType !== 'OTP') {
                this.searchDeclaration(0, benef);
            }
        } else if (serviceCodes[tab] === 'ServicePrestation') {
            this.searchPrestationService(benef);
        } else {
            this.searchPrestIJ(benef);
        }
    }

    searchDeclaration(selectedTab, benefToUse = null) {
        const { getBeneficiaryTpDetails, id, env } = this.props;
        const { declarationId } = this.state;

        const shouldSearch = benefToUse || (selectedTab === '0' && !declarationId);
        if (shouldSearch) {
            getBeneficiaryTpDetails(id, env)
                .then(() => {
                    this.setId();
                })
                .catch((error) => {
                    console.error(error);
                });
        }
    }

    searchPrestationService(benefToUse = null) {
        const { benefDetails, getPrestationService, loadingPrestation, getSasContrats } = this.props;
        const toUse = benefToUse || benefDetails;
        if (toUse && !loadingPrestation) {
            const prestationCriteria = {
                idDeclarant: toUse.amc.idDeclarant,
                numeroPersonne: toUse.identite.numeroPersonne,
            };
            getPrestationService(prestationCriteria);
            getSasContrats(toUse.key);
        }
    }

    searchPrestIJ(benefToUse) {
        const { benefDetails, getServicePrestIJs, loadingServicePrestIJ } = this.props;
        const benef = benefToUse || benefDetails;

        if (benef && !loadingServicePrestIJ) {
            const prestIJCriteria = {
                idDeclarant: benef.amc.idDeclarant,
                numeroContrat: benef.contrats[0].numeroContrat,
                nirCode: benef.identite.nir.code ? benef.identite.nir.code : benef.identite.affiliationsRO[0].nir.code,
                nirCle: benef.identite.nir.cle ? benef.identite.nir.cle : benef.identite.affiliationsRO[0].nir.cle,
                dateNaissance: benef.identite.dateNaissance,
                rangNaissance: benef.identite.rangNaissance,
            };
            getServicePrestIJs(prestIJCriteria);
        }
    }

    renderModalDateNaissance(periodes) {
        return PrestationServiceUtils.renderButtonModal(() => this.toggleDateNaissance(periodes));
    }

    renderModalAffiliationsRO(list) {
        return PrestationServiceUtils.renderButtonModal(() => this.toggleAffiliationsRO(list));
    }

    renderIdentity() {
        const { t, benefDetails } = this.props;
        const { identite } = benefDetails || {};

        const { nir } = this.setBenefNir(identite);

        const dateNaissance = businessUtils.transformDate(identite.dateNaissance);
        const { historiqueDateRangNaissance } = identite || {};
        const { affiliationsRO } = identite || {};
        const renderAffiliationsModal = affiliationsRO != null && affiliationsRO.length > 0;

        return (
            <Panel
                header={
                    <PanelHeader
                        title={t('beneficiaryDetails.id', {
                            numeroPersonne: identite.numeroPersonne,
                        })}
                    />
                }
            >
                <PanelSection border={false}>
                    <Row>
                        <Col xs={4} className={classNames({ 'pt-1': !renderAffiliationsModal })}>
                            <div>
                                <LabelValuePresenter
                                    id="nir"
                                    label={t('beneficiaryDetails.nirBeneficiaire')}
                                    value={nir}
                                    inline
                                />
                                {renderAffiliationsModal && this.renderModalAffiliationsRO(affiliationsRO)}
                            </div>
                        </Col>
                        <Col xs={4} className="pl-0">
                            <div>
                                <LabelValuePresenter
                                    id="birthDate"
                                    label={t('beneficiaryDetails.dateNaissance')}
                                    value={dateNaissance}
                                    inline
                                />
                                {historiqueDateRangNaissance != null &&
                                    historiqueDateRangNaissance.length > 0 &&
                                    this.renderModalDateNaissance(historiqueDateRangNaissance)}
                            </div>
                        </Col>
                        <Col xs={4} className="pt-1">
                            <div>
                                <LabelValuePresenter
                                    id="rank"
                                    label={t('beneficiaryDetails.rangNaissance')}
                                    value={identite.rangNaissance}
                                    inline
                                />
                            </div>
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    renderServices(sortedServiceMetier) {
        const { t, benefDetails, serviceMetierList, clientType } = this.props;

        const serviceExists = (index, list) =>
            (list || []).some((service) =>
                ServiceMetierUtils.checkIfServiceIsActive(index.toString(), service, serviceMetierList, clientType),
            );
        const serviceCodes = this.getServiceCodes(serviceMetierList);

        return (
            <Panel header={<PanelHeader title={t('beneficiaryDetails.typesContrat')} />}>
                <PanelSection border={false}>
                    <Row>
                        <Col style={{ display: 'flex' }} className="pt-1">
                            {sortedServiceMetier.map((service) => {
                                const serviceData = service[1];
                                const serviceExist = serviceExists(
                                    parseInt(serviceData.ordre, 10),
                                    benefDetails.services,
                                );

                                return (
                                    <Field
                                        key={serviceData.code}
                                        name={formConstants.FIELDS.selectedService}
                                        type="radio"
                                        component={CommonRadio}
                                        cgdComment={false}
                                        label={serviceData.libelle}
                                        value={serviceData.ordre}
                                        labelPortion={0}
                                        inline
                                        disabled={!serviceExist}
                                        onChange={() => this.setTab(serviceData.ordre, serviceCodes)}
                                        formGroupClassName="mr-3 ml-3"
                                    />
                                );
                            })}
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    renderServiceDetailsHeader(tab, serviceTitles, serviceIcons, serviceCodes, benefDetails) {
        const { t, amcName } = this.props;
        const serviceExist = benefDetails.services.includes(serviceCodes[tab]);
        return (
            serviceExist && (
                <PanelSection
                    title={
                        <span>
                            {'  '}
                            <CgIcon
                                fixedWidth
                                name={serviceIcons[tab]}
                                size="lg"
                                id={serviceIcons[tab]}
                                className="text-primary"
                            />
                            {'  '}
                            {t(`beneficiaryDetails.${serviceCodes[tab]}`)}
                            &nbsp;
                            <Status
                                label={`AMC : ${benefDetails.amc.idDeclarant} - ${amcName}`}
                                behavior="secondary"
                                className="fa-xs"
                            />
                        </span>
                    }
                    className="p-0 pt-3"
                >
                    {this.renderServiceDetails(tab, serviceCodes)}
                </PanelSection>
            )
        );
    }

    renderServicePrestation() {
        const { servicePrestations } = this.props;
        const isOpen = (servicePrestations || []).length <= 1;

        return (
            <Fragment>
                {(servicePrestations || [])
                    .sort((a, b) => {
                        // Tri par numeroAdherent décroissant
                        const adherentComparison = b.numeroAdherent.localeCompare(a.numeroAdherent);
                        // Si les numeroAdherent sont identiques, tri par numeroContrat décroissant
                        return adherentComparison !== 0 ? adherentComparison : b.numero.localeCompare(a.numero);
                    })
                    .map((item) => (
                        <PrestationServiceItem item={item} isOpen={isOpen} />
                    ))}
            </Fragment>
        );
    }

    renderServicePrestIJ() {
        const { benefDetails, servicePrestIJs } = this.props;
        const nirCode = benefDetails.identite.nir.code
            ? benefDetails.identite.nir.code
            : benefDetails.identite.affiliationsRO[0].nir.code;
        const nirCle = benefDetails.identite.nir.cle
            ? benefDetails.identite.nir.cle
            : benefDetails.identite.affiliationsRO[0].nir.cle;

        return (
            <Fragment>
                {(servicePrestIJs || [])
                    .sort((a, b) => get(a, 'contrat.numero').localeCompare(get(b, 'contrat.numero')))
                    .map((item) => (
                        <PrestIJDetailComponent
                            isOpen={servicePrestIJs.length === 1}
                            prestIJDetails={item}
                            nirCode={nirCode}
                            nirCle={nirCle}
                        />
                    ))}
            </Fragment>
        );
    }

    renderServiceDetails(tab, serviceCodes) {
        const { loadingDecl, servicePrestations, servicePrestIJs, amcName, benefDetails, numerosAMCEchanges, env } =
            this.props;
        const { declarationId, selectedMenuTab } = this.state;

        if (serviceCodes[tab] === 'Service_TP' && declarationId && !loadingDecl) {
            return (
                <ThirdPartyTab
                    selectedMenuTab={this.getSelectedMenuTab}
                    tabSelected={selectedMenuTab}
                    amcName={amcName}
                    numerosAMCEchanges={numerosAMCEchanges}
                    benefDetails={benefDetails}
                    serviceTab={tab}
                    environment={env}
                />
            );
        }
        if (serviceCodes[tab] === 'ServicePrestation' && servicePrestations) {
            return this.renderServicePrestation();
        } else if (serviceCodes[tab] === 'PrestIJ' && servicePrestIJs) {
            return this.renderServicePrestIJ();
        }
        return null;
    }

    renderServiceDetailForTP() {
        const { t, tab, benefDetails, serviceMetierList } = this.props;
        const { modalDateNaissance, historiqueDateNaissance, modalAffiliationsRO, affiliationsRO } = this.state;

        const serviceTitles = this.getServiceTitles(t, serviceMetierList);
        const serviceIcons = this.getServiceIcons(serviceMetierList);
        const serviceCodes = this.getServiceCodes(serviceMetierList);

        return (
            <div>
                {this.renderServiceDetailsHeader(tab, serviceTitles, serviceIcons, serviceCodes, benefDetails)}
                {historiqueDateNaissance && (
                    <Modal
                        size="md"
                        isOpen={modalDateNaissance}
                        toggle={() => this.toggleDateNaissance()}
                        backdrop="static"
                    >
                        <ModalHeader toggle={() => this.toggleDateNaissance()}>
                            {t('historiqueDateNaissance.header')}
                        </ModalHeader>
                        <ModalBody>
                            <HistoriqueDateNaissance historique={historiqueDateNaissance} />
                        </ModalBody>
                    </Modal>
                )}
                {affiliationsRO && (
                    <Modal
                        size="md"
                        isOpen={modalAffiliationsRO}
                        toggle={() => this.toggleAffiliationsRO()}
                        backdrop="static"
                    >
                        <ModalHeader toggle={() => this.toggleAffiliationsRO()}>
                            {t('affiliationsRO.header')}
                        </ModalHeader>
                        <ModalBody>
                            <AffiliationsRO list={affiliationsRO} />
                        </ModalBody>
                    </Modal>
                )}
            </div>
        );
    }

    render() {
        const { t, benefDetails, loadingBenef, tab, serviceMetierList, loadingDeclarant, sasContractList } = this.props;
        const { modalDateNaissance, historiqueDateNaissance, modalAffiliationsRO, affiliationsRO } = this.state;
        const canRender = benefDetails && !loadingBenef && !loadingDeclarant;
        if (!canRender) {
            return <CommonSpinner />;
        }
        const pageTitle = t('beneficiaryDetails.title');
        let subtitle = '';
        let comment = '';
        if (sasContractList && sasContractList.length > 0) {
            comment = (
                <div className="d-flex align-items-baseline">
                    <Status behavior="warning" label={t('beneficiaryDetails.droitsTPNonGenere')} />
                    <span className="pl-2 text-primary">
                        {t('beneficiaryDetails.listSasContrat')} {sasContractList.join(', ')}
                    </span>
                </div>
            );
        }
        const data = getData(benefDetails);
        if (data && data.nom) {
            const { nomFamille, nomUsage, prenom } = data.nom;
            subtitle = businessUtils.getBenefIdentity(nomUsage, nomFamille, prenom);
        }

        const sortedServiceMetier = ServiceMetierUtils.orderServiceMetierlist(serviceMetierList);
        const serviceTitles = this.getServiceTitles(t, serviceMetierList);
        const serviceIcons = this.getServiceIcons(serviceMetierList);
        const serviceCodes = this.getServiceCodes(serviceMetierList);

        return (
            <PageLayout header={<BodyHeader title={pageTitle} subtitle={subtitle} comment={comment} />}>
                <BreadcrumbPart
                    label={t('breadcrumb.beneficiaryRightDetails')}
                    parentPart={<BeneficiariesBreadcrumb />}
                />
                <div>
                    <Row>
                        <Col xs={7}>{this.renderIdentity()}</Col>
                        <Col xs={5}>{this.renderServices(sortedServiceMetier)}</Col>
                    </Row>
                </div>
                {serviceCodes[tab] === 'Service_TP'
                    ? this.renderServiceDetailForTP()
                    : this.renderServiceDetailsHeader(tab, serviceTitles, serviceIcons, serviceCodes, benefDetails)}
                {historiqueDateNaissance && (
                    <Modal
                        size="md"
                        isOpen={modalDateNaissance}
                        toggle={() => this.toggleDateNaissance()}
                        backdrop="static"
                    >
                        <ModalHeader toggle={() => this.toggleDateNaissance()}>
                            {t('historiqueDateNaissance.header')}
                        </ModalHeader>
                        <ModalBody>
                            <HistoriqueDateNaissance historique={historiqueDateNaissance} />
                        </ModalBody>
                    </Modal>
                )}
                {affiliationsRO && (
                    <Modal
                        size="md"
                        isOpen={modalAffiliationsRO}
                        toggle={() => this.toggleAffiliationsRO()}
                        backdrop="static"
                    >
                        <ModalHeader toggle={() => this.toggleAffiliationsRO()}>
                            {t('affiliationsRO.header')}
                        </ModalHeader>
                        <ModalBody>
                            <AffiliationsRO list={affiliationsRO} />
                        </ModalBody>
                    </Modal>
                )}
            </PageLayout>
        );
    }
}

BeneficiaryDetailsComponent.propTypes = {
    t: PropTypes.func,
    declarationId: PropTypes.string,
    getPrestationService: PropTypes.func,
    id: PropTypes.string,
    env: PropTypes.string,
    amcName: PropTypes.string,
    loadingDecl: PropTypes.bool,
    loadingBenef: PropTypes.bool,
    loadingPrestation: PropTypes.bool,
    benefDetails: PropTypes.shape(),
    saveBenefHistorique: PropTypes.func,
    servicePrestations: PropTypes.arrayOf(PropTypes.shape()),
    loadingServicePrestIJ: PropTypes.bool,
    servicePrestIJs: PropTypes.shape(),
    getServicePrestIJs: PropTypes.func,
    getOneDeclarant: PropTypes.func,
    serviceMetierList: PropTypes.shape(),
    tab: PropTypes.string,
    loadingDeclarant: PropTypes.bool,
    clientType: PropTypes.string,
    consoFinished: PropTypes.bool,
    getBeneficiaryTpDetails: PropTypes.func,
    getOneBenef: PropTypes.func,
    clearBeneficiaryDetails: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(BeneficiaryDetailsComponent);
