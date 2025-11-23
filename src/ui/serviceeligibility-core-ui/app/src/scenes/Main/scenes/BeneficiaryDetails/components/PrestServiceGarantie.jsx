/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Col,
    Status,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import autobind from 'autobind-decorator';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import CommonSpinner from '../../../../../common/components/CommonSpinner/CommonSpinner';
import DateUtils from '../../../../../common/utils/DateUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class PrestServiceGarantie extends Component {
    constructor(props) {
        super(props);
        const periode = (props.garantie || {}).periode || {};
        const isOpen = !periode.fin || DateUtils.dateAfterToday(periode.fin);
        this.state = {
            expanded: isOpen,
        };
    }

    @autobind
    toggleCollapse() {
        const { expanded } = this.state;
        this.setState({ expanded: !expanded });
    }

    @autobind
    renderPanelHeader(garantieTitle, isOpen, periodeValue) {
        const { t } = this.props;
        return (
            <PanelHeader
                title={garantieTitle}
                status={
                    <span>
                        <Status
                            behavior={isOpen ? 'success' : 'warning'}
                            label={
                                isOpen
                                    ? t('beneficiaryDetails.servicePrestation.periodeGarantieActive')
                                    : t('beneficiaryDetails.servicePrestation.periodeGarantieInactive')
                            }
                        />
                        <span>
                            <span className="cgd-comment">
                                {' '}
                                {t('beneficiaryDetails.servicePrestation.periodeGarantieDebut')}{' '}
                                {DateUtils.transformDateForDisplay(periodeValue.debut)}{' '}
                            </span>
                            <span className="cgd-comment">
                                {t('beneficiaryDetails.servicePrestation.periodeGarantieFin')}{' '}
                                {DateUtils.transformDateForDisplay(periodeValue.fin)}
                            </span>
                        </span>
                    </span>
                }
            />
        );
    }

    @autobind
    renderCarence(carence) {
        const { t, garantie } = this.props;
        const periodeValue = garantie.periode || {};
        const droitRemplacement = carence.droitRemplacement || {};
        const carenceDebut =
            carence &&
            ((carence.periode || {}).debut
                ? DateUtils.transformDateForDisplay((carence.periode || {}).debut)
                : DateUtils.transformDateForDisplay(periodeValue.debut));
        return (
            <Panel>
                <PanelSection>
                    <Row>
                        <Col xs="12" md="6">
                            <span className="cgd-comment">
                                {t('beneficiaryDetails.servicePrestation.carencePeriodeTitre')} :
                            </span>
                            <div>
                                <LabelValuePresenter
                                    id="carencePeriodeDebut"
                                    label={t('beneficiaryDetails.servicePrestation.carencePeriodeDebut')}
                                    value={carenceDebut}
                                    inline
                                />{' '}
                                <LabelValuePresenter
                                    id="carencePeriodeFin"
                                    label={t('beneficiaryDetails.servicePrestation.carencePeriodeFin')}
                                    value={DateUtils.transformDateForDisplay((carence.periode || {}).fin)}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs="12" md="6">
                            <div>
                                <LabelValuePresenter
                                    id="carenceCode"
                                    label={t('beneficiaryDetails.servicePrestation.carenceCode')}
                                    value={carence.code}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="carenceCodeDroitRemplacement"
                                    label={t('beneficiaryDetails.servicePrestation.carenceCodeDroitRemplacement')}
                                    value={droitRemplacement.code}
                                    inline
                                />
                            </div>
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    @autobind
    render() {
        const { t, garantie } = this.props;
        const { expanded } = this.state;
        if (!garantie) {
            return <CommonSpinner />;
        }
        const periodeValue = garantie.periode || {};
        const dateDebutBeforeToday = periodeValue.debut && !DateUtils.dateAfterToday(periodeValue.debut);
        const isOpen = dateDebutBeforeToday && (!periodeValue.fin || DateUtils.dateAfterToday(periodeValue.fin));
        const garantieTitle = `${t('beneficiaryDetails.servicePrestation.garantieTitle')} : ${garantie.code}`;
        const carenceList = garantie.carences || [];

        return (
            <Panel
                onCollapseClick={this.toggleCollapse}
                expanded={expanded}
                header={this.renderPanelHeader(garantieTitle, isOpen, periodeValue)}
            >
                <PanelSection>
                    <Row>
                        <Col xs="12" md="4">
                            <div>
                                <LabelValuePresenter
                                    id="codeAssureur"
                                    label={t('beneficiaryDetails.servicePrestation.codeAssureur')}
                                    value={garantie.codeAssureur}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="libelle"
                                    label={t('beneficiaryDetails.servicePrestation.libelle')}
                                    value={garantie.libelle}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs="12" md="4">
                            <div>
                                <LabelValuePresenter
                                    id="type"
                                    label={t('beneficiaryDetails.servicePrestation.type')}
                                    value={garantie.type}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs="12" md="4">
                            <div>
                                <LabelValuePresenter
                                    id="ordrePriorisation"
                                    label={t('beneficiaryDetails.servicePrestation.ordrePriorisation')}
                                    value={garantie.ordrePriorisation}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="dateAncienneteGarantie"
                                    label={t('beneficiaryDetails.servicePrestation.dateAncienneteGarantie')}
                                    value={DateUtils.transformDateForDisplay(garantie.dateAncienneteGarantie)}
                                    inline
                                />
                            </div>
                        </Col>
                    </Row>
                    {carenceList && carenceList.map((carence) => this.renderCarence(carence))}
                </PanelSection>
            </Panel>
        );
    }
}
PrestServiceGarantie.propTypes = {
    t: PropTypes.func,
    garantie: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default PrestServiceGarantie;
