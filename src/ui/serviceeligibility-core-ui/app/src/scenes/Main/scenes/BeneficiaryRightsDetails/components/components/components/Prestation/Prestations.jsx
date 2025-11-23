/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import {
    Col,
    Modal,
    ModalHeader,
    ModalBody,
    Panel,
    PanelSection,
    PanelHeader,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import PrestationServiceUtils from '../../../../../BeneficiaryDetails/components/PrestationServiceUtils';
import HistoPeriodePrestation from './HistoPeriodePrestation';
import DateRangePresenter from '../../../../../../../../common/utils/DateRangePresenter';
import DateUtils from '../../../../../../../../common/utils/DateUtils';
import Constants from '../../../../Constants';
import styleCss from '../style.module.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class Prestations extends Component {
    constructor(props) {
        super(props);

        this.state = {
            modalPrestation: false,
            historiquePrestation: null,
        };
    }

    togglePrestations(prestations) {
        const { modalPrestation } = this.state;
        this.setState({
            modalPrestation: !modalPrestation,
            historiquePrestation: prestations,
        });
    }

    renderModalPrestations(prestations) {
        return PrestationServiceUtils.renderButtonModal(() => this.togglePrestations(prestations));
    }

    renderPrestation(allPrestations) {
        const { t, TPOnlineSelected, domainesFromHistoConsoByMaille } = this.props;
        let severalPrestationPeriods = false;
        if (allPrestations.length > 1) {
            severalPrestationPeriods = true;
        }
        const [firstPrestation] = allPrestations;
        const { prestations, periode } = firstPrestation || {};

        return (
            <div>
                {prestations &&
                    prestations.map((prest) => (
                        <div key={prest.code}>
                            <Row>
                                <Col xs={6} className="pr-0">
                                    <LabelValuePresenter
                                        id="beneficiaryRightsDetails-claim-prestation"
                                        label={t('beneficiaryRightsDetails.claim.prestation')}
                                        value={prest.code}
                                    />
                                </Col>
                                {!TPOnlineSelected && (
                                    <Col xs={6} className="pr-0">
                                        <LabelValuePresenter
                                            id="beneficiaryRightsDetails-claim-formula"
                                            label={t('beneficiaryRightsDetails.claim.formula')}
                                            value={prest.formule && prest.formule.numero}
                                        />
                                    </Col>
                                )}
                            </Row>
                            <Row>
                                <Col xs={7} className={domainesFromHistoConsoByMaille ? 'pl-2' : 'pr-0'}>
                                    <LabelValuePresenter
                                        id="beneficiaryRightsDetails-claim-parameters"
                                        label={t('beneficiaryRightsDetails.claim.parameters')}
                                        value={
                                            prest.formule &&
                                            (prest.formule.paramPlat || []).map((item, index) => (
                                                // eslint-disable-next-line react/no-array-index-key
                                                <div key={index}>{item}</div>
                                            ))
                                        }
                                    />
                                </Col>
                            </Row>
                        </div>
                    ))}
                {periode != null && (
                    <div>
                        <Row className="ml-4">
                            {domainesFromHistoConsoByMaille && <Col xs={2} />}
                            <Col
                                xs={domainesFromHistoConsoByMaille ? 5 : 7}
                                className={domainesFromHistoConsoByMaille ? 'pt-1 mr-3' : 'pt-1 mr-5'}
                            >
                                <DateRangePresenter
                                    start={DateUtils.formatDisplayDate(periode.debut, Constants.DEFAULT_DATE)}
                                    end={DateUtils.formatDisplayDate(periode.fin, Constants.DEFAULT_DATE)}
                                    inline
                                />
                            </Col>
                            {severalPrestationPeriods && (
                                <Col xs={1} className={classNames({ 'pl-4': domainesFromHistoConsoByMaille })}>
                                    {this.renderModalPrestations(allPrestations)}
                                </Col>
                            )}
                        </Row>
                    </div>
                )}
            </div>
        );
    }

    render() {
        const { t, domain } = this.props;
        const { modalPrestation, historiquePrestation } = this.state;

        return (
            <Panel
                header={<PanelHeader title={t('beneficiaryRightsDetails.claim.title')} />}
                panelTheme="secondary"
                border={false}
            >
                <PanelSection>{this.renderPrestation(domain.prestations)}</PanelSection>

                {historiquePrestation && (
                    <Modal
                        size="md"
                        isOpen={modalPrestation}
                        toggle={() => this.togglePrestations()}
                        backdrop="static"
                        className={styleCss['modal-border']}
                    >
                        <ModalHeader toggle={() => this.togglePrestations()}>
                            {t('historiquePrestation.header')}
                        </ModalHeader>
                        <ModalBody>
                            <HistoPeriodePrestation historique={historiquePrestation} />
                        </ModalBody>
                    </Modal>
                )}
            </Panel>
        );
    }
}

Prestations.propTypes = {
    t: PropTypes.func,
    domain: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Prestations;
