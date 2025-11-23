/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import {
    Col,
    Modal,
    ModalBody,
    ModalHeader,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import utils from '../../../../../../../../common/utils/businessUtils';
import CommonSpinner from '../../../../../../../../common/components/CommonSpinner/CommonSpinner';
import PrestationServiceUtils from '../../../../../BeneficiaryDetails/components/PrestationServiceUtils';
import HistoPeriodeConvention from './HistoPeriodeConvention';
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
class TypeConvention extends Component {
    constructor(props) {
        super(props);

        this.state = {
            modalConvention: false,
            historiqueConvention: null,
        };
    }

    toggleConventions(conventions) {
        const { modalConvention } = this.state;
        this.setState({
            modalConvention: !modalConvention,
            historiqueConvention: conventions,
        });
    }

    renderModalConventions(conventions) {
        return PrestationServiceUtils.renderButtonModal(() => this.toggleConventions(conventions));
    }

    render() {
        const { conventions, t, domainesFromHistoConsoByMaille } = this.props;
        const { modalConvention, historiqueConvention } = this.state;
        if (!conventions) {
            return <CommonSpinner />;
        }
        let serveralConventionPeriods = false;
        if (conventions.length > 1) {
            serveralConventionPeriods = true;
        }
        const [firstConvention] = conventions;
        const { typeConventionnements, periode } = firstConvention || {};

        return (
            <Panel
                header={<PanelHeader title={t('beneficiaryRightsDetails.convention.title')} />}
                panelTheme="secondary"
                border={false}
            >
                <PanelSection className="pl-0">
                    <div>
                        {typeConventionnements &&
                            typeConventionnements.map((typeConv) => {
                                const typeConventionLabel = utils.getLibelleConverter(
                                    typeConv.typeConventionnement.code,
                                    typeConv.typeConventionnement.libelle,
                                );
                                return (
                                    <Row>
                                        <Col className="ml-3">
                                            <LabelValuePresenter
                                                id="priority-convention"
                                                label={t('beneficiaryRightsDetails.priorite')}
                                                value={`${typeConv.priorite} : ${typeConventionLabel}`}
                                            />
                                        </Col>
                                    </Row>
                                );
                            })}
                        {periode != null && (
                            <div>
                                <Row className="ml-4">
                                    {domainesFromHistoConsoByMaille ? <Col xs={3} /> : <Col xs={1} />}
                                    <Col
                                        xs={domainesFromHistoConsoByMaille ? 6 : 9}
                                        className={domainesFromHistoConsoByMaille ? 'mr-3 pt-1 pl-0' : 'pt-1 pl-0'}
                                    >
                                        <DateRangePresenter
                                            start={DateUtils.formatDisplayDate(periode.debut, Constants.DEFAULT_DATE)}
                                            end={DateUtils.formatDisplayDate(periode.fin, Constants.DEFAULT_DATE)}
                                            inline
                                        />
                                    </Col>
                                    {serveralConventionPeriods && (
                                        <Col xs={1} className={classNames({ 'pl-0': !domainesFromHistoConsoByMaille })}>
                                            {this.renderModalConventions(conventions)}
                                        </Col>
                                    )}
                                </Row>
                            </div>
                        )}
                    </div>
                    {historiqueConvention && (
                        <Modal
                            size="md"
                            isOpen={modalConvention}
                            toggle={() => this.toggleConventions()}
                            backdrop="static"
                            className={styleCss['modal-border']}
                        >
                            <ModalHeader toggle={() => this.toggleConventions()}>
                                {t('historiqueConvention.header')}
                            </ModalHeader>
                            <ModalBody>
                                <HistoPeriodeConvention historique={historiqueConvention} />
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

TypeConvention.propTypes = {
    t: PropTypes.func,
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TypeConvention;
