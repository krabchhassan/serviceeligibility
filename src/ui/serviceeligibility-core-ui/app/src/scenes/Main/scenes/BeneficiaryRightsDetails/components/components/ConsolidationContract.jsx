/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    Button,
    Col,
    LoadingSpinner,
    Panel,
    PanelSection,
    Row,
    Modal,
    ModalBody,
    ModalHeader,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import Rights from '../Rights';
import DateUtils from '../../../../../../common/utils/DateUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class ConsolidationContract extends Component {
    constructor(props) {
        super(props);

        this.state = {
            modalOpen: false,
            domainesByMaille: null,
        };
    }

    toggleModal(domaines) {
        const { modalOpen } = this.state;
        this.setState({ modalOpen: !modalOpen, domainesByMaille: domaines });
    }

    render() {
        const { t, consolidation, conventions, numeroPersonne } = this.props;
        const { modalOpen, domainesByMaille, TPOnlineSelected } = this.state;

        if (!consolidation) {
            return <LoadingSpinner iconType="circle-o-notch" behavior="primary" />;
        }
        const { dateConsolidation } = consolidation;

        const dateConsoRework = DateUtils.formatDisplayDateTimeWithTimeZone(dateConsolidation);
        const dateConso = dateConsoRework.substr(0, 10);
        const heureConso = dateConsoRework.substr(11, 5);
        const title = t('beneficiaryRightsDetails.historiqueConsolidations.consolidationTitle', {
            dateConso,
            heureConso,
        });

        const benefConso = Object.values(consolidation.beneficiaires || []).filter(
            (benef) => benef.numeroPersonne === numeroPersonne,
        );

        return (
            <Panel id="consolidationContrat-panel" label={title} border={false} wrapperClassName="pt-0 pb-0">
                <PanelSection className="pt-0 pb-0">
                    <Row className="mt-0 mb-0">
                        <Col xs={5} className="pt-1">
                            {title}
                        </Col>
                        <Col xs={5}>{null}</Col>
                        <Col xs={2}>
                            <Button
                                id="open-button"
                                behavior="link"
                                onClick={() => this.toggleModal(benefConso[0].maillesDomaineDroits)}
                            >
                                {`${t('open')} `}
                                <CgIcon name="right-scroll" />
                            </Button>
                        </Col>
                    </Row>
                    {domainesByMaille && (
                        <Modal size="full-width" isOpen={modalOpen} toggle={() => this.toggleModal()} backdrop="static">
                            <ModalHeader toggle={() => this.toggleModal()}>
                                {t('beneficiaryRightsDetails.historiqueConsolidations.modalTitle', {
                                    dateConso,
                                    heureConso,
                                })}
                            </ModalHeader>
                            <ModalBody>
                                <Rights
                                    domainesFromHistoConsoByMaille={domainesByMaille}
                                    conventions={conventions}
                                    TPOnlineSelectedFromHistoConso={TPOnlineSelected}
                                />
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

ConsolidationContract.propTypes = {
    consolidation: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    numeroPersonne: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ConsolidationContract;
