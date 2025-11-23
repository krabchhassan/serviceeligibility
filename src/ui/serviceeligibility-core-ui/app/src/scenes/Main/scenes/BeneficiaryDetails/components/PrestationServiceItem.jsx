/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Col,
    Modal,
    ModalBody,
    ModalHeader,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Status,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import '../styles.module.scss';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import DateUtils from '../../../../../common/utils/DateUtils';
import businessUtils from '../../../../../common/utils/businessUtils';
import PrestServiceGarantie from './PrestServiceGarantie';
import HistoriqueTeletransmission from './HistoriqueTeletransmission';
import PeriodesAssure from './PeriodesAssure';
import Address from '../../../../../common/components/Address/Address';
import PrestationServiceDestinataires from './PrestationServiceDestinataires';
import ContratInfosComp from './ContratInfosComp';
import PrestationServiceUtils from './PrestationServiceUtils';
import AssureInfosComp from './AssureInfosComp';
import Constants from '../../../../../common/utils/Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class PrestationServiceItem extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isOpen: props.isOpen,
            modalPeriodesAssure: false,
            modalTeletransmission: false,
            periodesAssure: null,
            historiqueTeletransmission: null,
        };
    }

    @autobind
    toggleCollapse() {
        const { isOpen } = this.state;
        this.setState({ isOpen: !isOpen });
    }

    calculateStatus(item, dateRadiation) {
        const { t } = this.props;
        const today = DateUtils.todayMoment();

        if (item.dateResiliation) {
            const resilDate = DateUtils.parseDateInFormat(
                item.dateResiliation,
                Constants.DEFAULT_DATE_PICKER_FORMAT,
                false,
            );
            if (resilDate && resilDate.isBefore(today)) {
                // status résilié
                return <Status behavior="warning" label={t('beneficiaryDetails.servicePrestation.statusResilie')} />;
            }
        } else if (dateRadiation) {
            const radiationDate = DateUtils.parseDateInFormat(
                dateRadiation,
                Constants.DEFAULT_DATE_PICKER_FORMAT,
                false,
            );
            if (radiationDate && radiationDate.isBefore(today)) {
                // status radié
                return <Status behavior="warning" label={t('beneficiaryDetails.servicePrestation.statusRadie')} />;
            }
        }
        let periode = null;
        let suspension = null;
        if (item.periodesSuspension && item.periodesSuspension.length > 0) {
            suspension = item.periodesSuspension[0];
            periode = suspension.periode.debut ? suspension.periode : {};
        }
        if (!suspension) {
            // status valid
            return <Status behavior="success" label={t('beneficiaryDetails.servicePrestation.statusValid')} />;
        }

        if (suspension.typeSuspension === 'Definitif') {
            // status résilié
            return <Status behavior="warning" label={t('beneficiaryDetails.servicePrestation.statusSuspendu')} />;
        }

        if ((periode.fin && today.isBefore(periode.fin)) || !periode.fin) {
            // status suspendu
            return <Status label={t('beneficiaryDetails.servicePrestation.statusSuspendu')} />;
        }

        if (periode.fin && today.isAfter(periode.fin)) {
            // status valid
            return <Status behavior="success" label={t('beneficiaryDetails.servicePrestation.statusValid')} />;
        }

        return null;
    }

    isCartePapierAEditer(contexteTiersPayant) {
        const { t } = this.props;
        let isCartePapierAEditer = '-';
        if (
            contexteTiersPayant &&
            contexteTiersPayant.isCartePapierAEditer !== undefined &&
            contexteTiersPayant.isCartePapierAEditer !== null
        ) {
            isCartePapierAEditer = t(contexteTiersPayant.isCartePapierAEditer ? 'yes' : 'no');
        }
        return isCartePapierAEditer;
    }

    togglePeriodesAssure(periodesAssure) {
        const { modalPeriodesAssure } = this.state;
        this.setState({ modalPeriodesAssure: !modalPeriodesAssure, periodesAssure });
    }

    toggleTeletransmission(historiqueTeletransmission) {
        const { modalTeletransmission } = this.state;
        this.setState({ modalTeletransmission: !modalTeletransmission, historiqueTeletransmission });
    }

    renderIsDemat(digitRelation) {
        const { t } = this.props;
        return (
            <div>
                <LabelValuePresenter
                    id="demat"
                    label={t('beneficiaryDetails.servicePrestation.demat')}
                    value={((digitRelation || {}).dematerialisation || {}).isDematerialise ? t('yes') : t('no')}
                    inline
                />
            </div>
        );
    }

    renderPeriodesAssure() {
        const { t, item } = this.props;
        return (
            <div>
                <LabelValuePresenter
                    id="periodesAssure"
                    label={t('beneficiaryDetails.servicePrestation.periodesAssure')}
                    value={
                        (item.assure || {}).periodes != null && (item.assure || {}).periodes.length !== 0
                            ? t('yes')
                            : '- '
                    }
                    inline
                />
                {this.renderModalPeriodesAssure(item.assure.periodes)}
            </div>
        );
    }

    renderModalPeriodesAssure(periodesAssure) {
        return PrestationServiceUtils.renderButtonModal(() => this.togglePeriodesAssure(periodesAssure));
    }

    renderModalTeleTransmission(periodes) {
        return PrestationServiceUtils.renderButtonModal(() => this.toggleTeletransmission(periodes));
    }

    renderContrat(endContractDate) {
        const { t, item } = this.props;

        return (
            <Panel header={<PanelHeader title={t('beneficiaryDetails.servicePrestation.infoContrat')} />}>
                <PanelSection className="pl-0">
                    <Row>
                        <Col xs="12" md="4">
                            <div>
                                <LabelValuePresenter
                                    id="debutContrat"
                                    label={t('beneficiaryDetails.servicePrestation.debutContrat')}
                                    value={DateUtils.transformDateForDisplay(item.dateSouscription)}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="finContrat"
                                    label={t('beneficiaryDetails.servicePrestation.finContrat')}
                                    value={DateUtils.transformDateForDisplay(item.dateResiliation)}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="priorisation"
                                    label={t('beneficiaryDetails.servicePrestation.priorisation')}
                                    value={item.ordrePriorisation}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col>
                            <div>
                                <LabelValuePresenter
                                    id="group"
                                    label={t('beneficiaryDetails.servicePrestation.numeroAdherent')}
                                    value={(item || {}).numeroAdherent}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="issuingCompany"
                                    label={t('beneficiaryDetails.servicePrestation.issuingCompany')}
                                    value={(item || {}).societeEmettrice}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="numeroAdherent"
                                    label={t('beneficiaryDetails.servicePrestation.critereSecondaire')}
                                    value={(item || {}).critereSecondaire}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col>{this.renderContexteTiersPayant()}</Col>
                    </Row>
                    <ContratInfosComp item={item} endContractDate={endContractDate} />
                </PanelSection>
            </Panel>
        );
    }

    renderContratCollectif() {
        const { t, item } = this.props;
        const contratCollectif = item.contratCollectif || {};
        const isContratCollectif = contratCollectif.numero || false;

        return (
            <Fragment>
                {isContratCollectif && (
                    <Panel
                        header={<PanelHeader title={t('beneficiaryDetails.servicePrestation.infoContratCollectif')} />}
                    >
                        <PanelSection className="pl-0">
                            <Row>
                                <Col xs="12" md="4">
                                    <div>
                                        <LabelValuePresenter
                                            id="debutContrat"
                                            label={t('beneficiaryDetails.servicePrestation.contratCollectif.numero')}
                                            value={contratCollectif.numero || '-'}
                                            inline
                                        />
                                    </div>
                                    <div>
                                        <LabelValuePresenter
                                            id="debutContrat"
                                            label={t(
                                                'beneficiaryDetails.servicePrestation.contratCollectif.numeroExterne',
                                            )}
                                            value={contratCollectif.numeroExterne || '-'}
                                            inline
                                        />
                                    </div>
                                </Col>
                                <Col>
                                    <div>
                                        <LabelValuePresenter
                                            id="group"
                                            label={t(
                                                'beneficiaryDetails.servicePrestation.contratCollectif.idCollectivite',
                                            )}
                                            value={contratCollectif.identifiantCollectivite || '-'}
                                            inline
                                        />
                                    </div>
                                    <div>
                                        <LabelValuePresenter
                                            id="group"
                                            label={t(
                                                'beneficiaryDetails.servicePrestation.contratCollectif.raisonSociale',
                                            )}
                                            value={contratCollectif.raisonSociale || '-'}
                                            inline
                                        />
                                    </div>
                                </Col>
                                <Col>
                                    <div>
                                        <LabelValuePresenter
                                            id="group"
                                            label={t('beneficiaryDetails.servicePrestation.contratCollectif.siret')}
                                            value={contratCollectif.siret || '-'}
                                            inline
                                        />
                                    </div>
                                    <div>
                                        <LabelValuePresenter
                                            id="group"
                                            label={t(
                                                'beneficiaryDetails.servicePrestation.contratCollectif.groupePopulation',
                                            )}
                                            value={contratCollectif.groupePopulation || '-'}
                                            inline
                                        />
                                    </div>
                                </Col>
                            </Row>
                        </PanelSection>
                    </Panel>
                )}
            </Fragment>
        );
    }

    renderContexteTiersPayant() {
        const { t, item } = this.props;
        const { contexteTiersPayant } = item;
        const periodeCarte = (contexteTiersPayant && contexteTiersPayant.periodesDroitsCarte) || {};
        const isCartePapierAEditer = this.isCartePapierAEditer(contexteTiersPayant);

        return (
            <Fragment>
                <Fragment>
                    <div>
                        <LabelValuePresenter
                            id="cartePapierAEditer"
                            label={t('beneficiaryDetails.servicePrestation.cartePapierAEditer')}
                            value={isCartePapierAEditer}
                            inline
                        />
                    </div>
                    <div>
                        <LabelValuePresenter
                            id="debutSuspension"
                            label={t('beneficiaryDetails.servicePrestation.debutCarte')}
                            value={DateUtils.transformDateForDisplay(periodeCarte.debut)}
                            inline
                        />
                    </div>
                    <div>
                        <LabelValuePresenter
                            id="debutSuspension"
                            label={t('beneficiaryDetails.servicePrestation.finCarte')}
                            value={DateUtils.transformDateForDisplay(periodeCarte.fin)}
                            inline
                        />
                    </div>
                </Fragment>
            </Fragment>
        );
    }

    renderAddressPart = (data) => {
        let adressPart = '';
        if (data && data.adresse) {
            adressPart = <Address address={data.adresse} />;
        }
        return adressPart;
    };

    renderAssure(endContractDate) {
        const { t, item } = this.props;
        const status = ((item.assure || {}).qualite || {}).libelle;
        const digitRelation = (item.assure || {}).digitRelation || {};
        const { isSouscripteur } = item.assure || {};
        const { data } = item.assure || {};

        const adressPart = this.renderAddressPart(data);

        return (
            <Panel header={<PanelHeader title={t('beneficiaryDetails.servicePrestation.infoAssure')} />}>
                <PanelSection className="pl-0">
                    <Row>
                        <Col xs="12" md="3">
                            <div>
                                <LabelValuePresenter
                                    id="status"
                                    label={t('beneficiaryDetails.servicePrestation.status')}
                                    value={status}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="souscripteur"
                                    label={t('beneficiaryDetails.servicePrestation.souscripteur')}
                                    value={isSouscripteur ? t('yes') : t('no')}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="rangAdministratif"
                                    label={t('beneficiaryDetails.servicePrestation.rangAdministratif')}
                                    value={item.assure.rangAdministratif}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs="12" md="3">
                            {this.renderIsDemat(digitRelation)}
                            {this.renderSubscriptions(digitRelation, endContractDate)}
                            <div>
                                <LabelValuePresenter
                                    id="dateAdhesionMutuelle"
                                    label={t('beneficiaryDetails.servicePrestation.dateAdhesionMutuelle')}
                                    value={DateUtils.transformDateForDisplay((item.assure || {}).dateAdhesionMutuelle)}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs="12" md="3">
                            <div>
                                <LabelValuePresenter
                                    id="numeroAdhesionIndividuelle"
                                    label={t('beneficiaryDetails.servicePrestation.numeroAdhesionIndividuelle')}
                                    value={(item.assure || {}).numeroAdhesionIndividuelle}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="refExternePersonne"
                                    label={t('beneficiaryDetails.servicePrestation.refExternePersonne')}
                                    value={((item.assure || {}).identite || {}).refExternePersonne}
                                    inline
                                />
                            </div>
                            {this.renderPeriodesAssure()}
                        </Col>
                        <Col xs="12" md="3">
                            <div className="mb-1">
                                <LabelValuePresenter inline id="adresse" value={adressPart} />
                            </div>
                        </Col>
                    </Row>
                    <AssureInfosComp item={item} endContractDate={endContractDate} />
                </PanelSection>
            </Panel>
        );
    }

    renderSubscriptions(digitRelation, endContractDate) {
        const { t } = this.props;
        const teletransmissionActive = businessUtils.returnCodeOfActivePeriod(
            (digitRelation || {}).teletransmissions,
            endContractDate,
            t,
        );
        const valueTeletransmission = (
            <div>
                {teletransmissionActive}
                {digitRelation.teletransmissions != null &&
                    this.renderModalTeleTransmission(digitRelation.teletransmissions)}
            </div>
        );
        return (
            <Fragment>
                <div>
                    <LabelValuePresenter
                        id="noemi"
                        label={t('beneficiaryDetails.servicePrestation.noemi')}
                        value={valueTeletransmission}
                        inline
                    />
                </div>
            </Fragment>
        );
    }

    renderDroits() {
        const { t, item } = this.props;
        const rights = (item.assure || {}).droits || [];
        const orderedRights = DateUtils.sortRightListByDateDsc(rights);

        return (
            <Panel header={<PanelHeader title={t('beneficiaryDetails.servicePrestation.droits')} />}>
                <PanelSection>
                    {orderedRights.map((droit) => (
                        <PrestServiceGarantie className="pr-1 pl-1" garantie={droit} />
                    ))}
                </PanelSection>
            </Panel>
        );
    }

    render() {
        const { t, item } = this.props;
        const { data, dateRadiation } = item.assure || {};
        const { destinatairesPaiements } = data || {};
        const { destinatairesRelevePrestations } = data || {};
        const { isOpen, modalPeriodesAssure, periodesAssure, modalTeletransmission, historiqueTeletransmission } =
            this.state;
        const title = `${t('beneficiaryDetails.servicePrestation.contrat')}${t('colon')}${item.numero}`;
        const isIndividuel = item.isContratIndividuel;

        const today = DateUtils.formatServerDate(DateUtils.todayMoment(), Constants.YEARS_MONTH_DAY_FORMAT);
        const dates = [dateRadiation, item?.dateResiliation, today].filter(
            (date) => date !== null && date !== undefined,
        );
        const endContractDate = dates.reduce((minDate, currentDate) => {
            return currentDate < minDate ? currentDate : minDate;
        });
        const parsedEndContractDate = DateUtils.parseDateInFormat(
            endContractDate,
            Constants.DEFAULT_DATE_PICKER_FORMAT,
        );

        const status = this.calculateStatus(item, dateRadiation);
        const qualification = businessUtils.getContractType(item.qualification);
        const subtitle = (
            <div>
                <span className="pl-3 pr-4">
                    {!isIndividuel ? (
                        <Fragment>
                            <CgIcon name="group" className="pr-1" />
                            {t('beneficiaryDetails.servicePrestation.contratCollective')}
                        </Fragment>
                    ) : (
                        <Fragment>
                            <CgIcon name="user-o" className="pr-1" />
                            {t('beneficiaryDetails.servicePrestation.contratIndividuel')}
                        </Fragment>
                    )}
                </span>
                <LabelValuePresenter
                    id="typeContratShort"
                    label={t('beneficiaryDetails.servicePrestation.typeContratShort')}
                    value={qualification}
                    inline
                    className="pr-4"
                />
                <LabelValuePresenter
                    id="priorisationHeader"
                    label={t('beneficiaryDetails.servicePrestation.priorisation')}
                    value={item.ordrePriorisation}
                    inline
                />
            </div>
        );
        return (
            <Panel
                onCollapseClick={this.toggleCollapse}
                expanded={isOpen}
                header={<PanelHeader title={title} status={status} context={subtitle} />}
            >
                <PanelSection className="pl-0">
                    {this.renderContrat(parsedEndContractDate)}
                    {this.renderContratCollectif()}
                    {this.renderAssure(parsedEndContractDate)}
                    <PrestationServiceDestinataires
                        destinatairesPaiements={destinatairesPaiements}
                        destinatairesRelevePrestations={destinatairesRelevePrestations}
                    />
                    {this.renderDroits()}

                    {periodesAssure && (
                        <Modal
                            size="md"
                            isOpen={modalPeriodesAssure}
                            toggle={() => this.togglePeriodesAssure()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.togglePeriodesAssure()}>
                                {t('periodesAssure.header')}
                            </ModalHeader>
                            <ModalBody>
                                <PeriodesAssure periodesAssure={periodesAssure} />
                            </ModalBody>
                        </Modal>
                    )}
                    {historiqueTeletransmission && (
                        <Modal
                            size="md"
                            isOpen={modalTeletransmission}
                            toggle={() => this.toggleTeletransmission()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.toggleTeletransmission()}>
                                {t('historiqueTeletransmission.header')}
                            </ModalHeader>
                            <ModalBody>
                                <HistoriqueTeletransmission historique={historiqueTeletransmission} />
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

PrestationServiceItem.propTypes = {
    t: PropTypes.func,
    item: PropTypes.shape(),
    isOpen: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default PrestationServiceItem;
