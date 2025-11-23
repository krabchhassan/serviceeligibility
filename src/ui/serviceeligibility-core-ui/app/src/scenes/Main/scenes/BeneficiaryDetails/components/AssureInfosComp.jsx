import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Col,
    Modal,
    ModalHeader,
    ModalBody,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import businessUtils from '../../../../../common/utils/businessUtils';
import PrestationServiceUtils from './PrestationServiceUtils';
import SituationsParticulieres from './SituationsParticulieres';
import HistoriqueRegimeParticulier from './HistoriqueRegimeParticulier';
import InformationsAssure from './InformationsAssure';
import DateUtils from '../../../../../common/utils/DateUtils';
import PeriodesMedecinTraitant from './PeriodesMedecinTraitant';

const getNir = (identite, dateResiliation, dateRadiation) => {
    let minDate;
    if (dateResiliation != null && dateRadiation != null) {
        minDate = dateResiliation <= dateRadiation ? dateResiliation : dateRadiation;
    } else {
        minDate = dateResiliation ?? dateRadiation;
    }

    if (identite.nir != null) {
        return identite.nir.code;
    }
    if (identite.affiliationsRO && identite.affiliationsRO.length > 0) {
        const affiliationsValides = identite.affiliationsRO.filter(
            ({ periode }) => (!periode?.fin || periode.fin >= minDate) && periode?.debut <= minDate,
        );

        if (affiliationsValides.length > 0) {
            const affiliationActive = affiliationsValides.find(({ periode }) => periode?.fin === null);
            if (affiliationActive) {
                return affiliationActive.nir.code;
            }

            affiliationsValides.sort((a, b) => new Date(a.periode.debut) - new Date(b.periode.debut));
            return affiliationsValides[0].nir.code;
        }
        return identite.affiliationsRO[0].nir.code;
    }
    return null;
};

@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class AssureInfosComp extends Component {
    constructor(props) {
        super(props);

        this.state = {
            expanded: false,
            modalAssure: false,
            modalRegime: false,
            modalSituationsParticulieres: false,
            modalPeriodesMedecinTraitant: false,
            informationsAssure: null,
            historiqueRegimeParticulier: null,
            situationsParticulieres: null,
            periodesMedecinTraitant: null,
        };
    }

    onCollapseClick() {
        const { expanded } = this.state;
        this.setState({ expanded: !expanded });
    }

    toggleAssure(informationsAssure) {
        const { modalAssure } = this.state;
        this.setState({ modalAssure: !modalAssure, informationsAssure });
    }

    toggleRegime(historiqueRegimeParticulier) {
        const { modalRegime } = this.state;
        this.setState({ modalRegime: !modalRegime, historiqueRegimeParticulier });
    }

    toggleSituationsParticulieres(situationsParticulieres) {
        const { modalSituationsParticulieres } = this.state;
        this.setState({ modalSituationsParticulieres: !modalSituationsParticulieres, situationsParticulieres });
    }

    togglePeriodesMedecinTraitant(periodesMedecinTraitant) {
        const { modalPeriodesMedecinTraitant } = this.state;
        this.setState({ modalPeriodesMedecinTraitant: !modalPeriodesMedecinTraitant, periodesMedecinTraitant });
    }

    renderModalAssure(assure) {
        return PrestationServiceUtils.renderButtonModal(() => this.toggleAssure(assure));
    }

    renderModalRegimeParticulier(regime) {
        return PrestationServiceUtils.renderButtonModal(() => this.toggleRegime(regime));
    }

    renderModalSituationsParticulieres(situationsParticulieres) {
        return PrestationServiceUtils.renderButtonModal(() =>
            this.toggleSituationsParticulieres(situationsParticulieres),
        );
    }

    renderModalPeriodesMedecinTraitant(periodesMedecinTraitant) {
        return PrestationServiceUtils.renderButtonModal(() =>
            this.togglePeriodesMedecinTraitant(periodesMedecinTraitant),
        );
    }

    renderRegimesParticuliers() {
        const { t, item, endContractDate } = this.props;
        const { regimesParticuliers } = item.assure || {};
        const regimeParticulierActif = businessUtils.returnCodeOfActivePeriod(regimesParticuliers, endContractDate, t);

        return (
            <div>
                <LabelValuePresenter
                    id="regimeParticulier"
                    label={t('beneficiaryDetails.servicePrestation.regimesParticuliers')}
                    value={regimeParticulierActif}
                    inline
                />
                {regimesParticuliers &&
                    regimesParticuliers.length > 0 &&
                    this.renderModalRegimeParticulier(regimesParticuliers)}
            </div>
        );
    }

    renderSituationsParticulieres() {
        const { t, item, endContractDate } = this.props;
        const { situationsParticulieres } = item.assure || {};
        const situationParticuliereActive = businessUtils.returnCodeOfActivePeriod(
            situationsParticulieres,
            endContractDate,
            t,
        );
        return (
            <div>
                <LabelValuePresenter
                    id="situationsParticulieres"
                    label={t('beneficiaryDetails.servicePrestation.situationsParticulieres')}
                    value={situationParticuliereActive}
                    inline
                />
                {situationsParticulieres &&
                    situationsParticulieres.length !== 0 &&
                    this.renderModalSituationsParticulieres(situationsParticulieres)}
            </div>
        );
    }

    renderPeriodesMedecinTraitantOuvert() {
        const { t, item, endContractDate } = this.props;
        const { periodesMedecinTraitantOuvert } = item.assure || {};
        const periodeMedecinTraitantActive = businessUtils.isActivePeriod(
            periodesMedecinTraitantOuvert,
            endContractDate,
            t,
        );
        return (
            <div>
                <LabelValuePresenter
                    id="periodesMedecinTraitantOuvert"
                    label={t('beneficiaryDetails.servicePrestation.periodesMedecinTraitantOuvert')}
                    value={periodeMedecinTraitantActive}
                    inline
                />
                {periodesMedecinTraitantOuvert &&
                    periodesMedecinTraitantOuvert.length > 0 &&
                    this.renderModalPeriodesMedecinTraitant(periodesMedecinTraitantOuvert)}
            </div>
        );
    }

    renderContact = (contact) => (
        <div>
            {(contact || {}).email && (
                <div className="mb-1">
                    <LabelValuePresenter
                        id="mail"
                        value={
                            <div>
                                <CgIcon fixedWidth name="mail" id="email" size="xs" className="text-secondary" />{' '}
                                {(contact || {}).email}
                            </div>
                        }
                        inline
                    />
                </div>
            )}
            {(contact || {}).mobile && (
                <div>
                    <LabelValuePresenter
                        id="tel"
                        value={
                            <div>
                                <CgIcon size="xs" name="phone" id="phone" className="text-secondary mr-1" />
                                {(contact || {}).mobile}
                            </div>
                        }
                        inline
                    />
                </div>
            )}
            {(contact || {}).fixe && (
                <div>
                    <LabelValuePresenter
                        id="telFixe"
                        value={
                            <div>
                                <CgIcon size="xs" name="phone" id="phone" className="text-secondary mr-1" />
                                {(contact || {}).fixe}
                            </div>
                        }
                        inline
                    />
                </div>
            )}
        </div>
    );

    render() {
        const { t, item } = this.props;
        const {
            expanded,
            modalAssure,
            modalRegime,
            modalSituationsParticulieres,
            modalPeriodesMedecinTraitant,
            informationsAssure,
            historiqueRegimeParticulier,
            situationsParticulieres,
            periodesMedecinTraitant,
        } = this.state;

        const assure = item.assure || {};
        const displayedNir = getNir(assure.identite, item.dateResiliation, assure.dateRadiation);
        const data = assure.data || {};

        const valueNir = (
            <Fragment>
                {displayedNir && (
                    <div>
                        {businessUtils.formatRO(displayedNir)}
                        {this.renderModalAssure(assure.identite)}
                    </div>
                )}
            </Fragment>
        );
        return (
            <Panel
                onCollapseClick={() => this.onCollapseClick()}
                expanded={expanded}
                className="mt-3"
                header={<PanelHeader title={t('beneficiaryDetails.servicePrestation.infosComp')} />}
            >
                <PanelSection>
                    <Row>
                        <Col xs="12" md="4">
                            <div>
                                <LabelValuePresenter
                                    id="nir"
                                    label={t('beneficiaryDetails.servicePrestation.nir')}
                                    value={valueNir}
                                    inline
                                />
                            </div>
                            {this.renderRegimesParticuliers()}
                            {this.renderSituationsParticulieres()}
                        </Col>
                        <Col xs="12" md="4">
                            <div>
                                <LabelValuePresenter
                                    id="dateDebutAdhesionIndividuelle"
                                    label={t('beneficiaryDetails.servicePrestation.debutPrestation')}
                                    value={DateUtils.transformDateForDisplay(assure.dateDebutAdhesionIndividuelle)}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="dateRadiation"
                                    label={t('beneficiaryDetails.servicePrestation.finPrestation')}
                                    value={DateUtils.transformDateForDisplay((item.assure || {}).dateRadiation)}
                                    inline
                                />
                            </div>
                            {this.renderPeriodesMedecinTraitantOuvert()}
                        </Col>
                        <Col xs="12" md="4">
                            {this.renderContact(data.contact)}
                        </Col>
                    </Row>
                    {informationsAssure && (
                        <Modal size="md" isOpen={modalAssure} toggle={() => this.toggleAssure()} backdrop="static">
                            <ModalHeader toggle={() => this.toggleAssure()}>
                                {t('informationsAssure.header')}
                            </ModalHeader>
                            <ModalBody>
                                <InformationsAssure informations={informationsAssure} />
                            </ModalBody>
                        </Modal>
                    )}
                    {historiqueRegimeParticulier && (
                        <Modal size="md" isOpen={modalRegime} toggle={() => this.toggleRegime()} backdrop="static">
                            <ModalHeader toggle={() => this.toggleRegime()}>{t('historiqueRegime.header')}</ModalHeader>
                            <ModalBody>
                                <HistoriqueRegimeParticulier informations={historiqueRegimeParticulier} />
                            </ModalBody>
                        </Modal>
                    )}
                    {situationsParticulieres && (
                        <Modal
                            size="md"
                            isOpen={modalSituationsParticulieres}
                            toggle={() => this.toggleSituationsParticulieres()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.toggleSituationsParticulieres()}>
                                {t('situationsParticulieres.header')}
                            </ModalHeader>
                            <ModalBody>
                                <SituationsParticulieres situationsParticulieres={situationsParticulieres} />
                            </ModalBody>
                        </Modal>
                    )}
                    {periodesMedecinTraitant && (
                        <Modal
                            size="md"
                            isOpen={modalPeriodesMedecinTraitant}
                            toggle={() => this.togglePeriodesMedecinTraitant()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.togglePeriodesMedecinTraitant()}>
                                {t('periodesMedecinTraitant.header')}
                            </ModalHeader>
                            <ModalBody>
                                <PeriodesMedecinTraitant periodesMedecinTraitant={periodesMedecinTraitant} />
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    endContractDate: PropTypes.string,
};
// Default props
const defaultProps = {};

AssureInfosComp.propTypes = propTypes;
// Add default props
AssureInfosComp.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default AssureInfosComp;
