/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    Col,
    Panel,
    PanelSection,
    PanelHeader,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class Prestations extends Component {
    renderPrestation(prestation) {
        const { t, TPOnlineSelected } = this.props;

        return (
            <div>
                <Row>
                    <Col xs={7}>
                        <LabelValuePresenter
                            id="beneficiaryRightsDetails-claim-prestation"
                            label={t('beneficiaryRightsDetails.claim.prestation')}
                            value={prestation.code}
                        />
                    </Col>
                    {!TPOnlineSelected && (
                        <Col xs={5} className="pl-2 pr-2">
                            <LabelValuePresenter
                                id="beneficiaryRightsDetails-claim-formula"
                                label={t('beneficiaryRightsDetails.claim.formula')}
                                value={prestation.formule && prestation.formule.numero}
                            />
                        </Col>
                    )}
                </Row>
                <Row>
                    <Col xs={7}>
                        <LabelValuePresenter
                            id="beneficiaryRightsDetails-claim-parameters"
                            label={t('beneficiaryRightsDetails.claim.parameters')}
                            value={
                                prestation.formule &&
                                (prestation.formule.paramPlat || []).map((item, index) => (
                                    // eslint-disable-next-line react/no-array-index-key
                                    <div key={index}>{item}</div>
                                ))
                            }
                        />
                    </Col>
                </Row>
            </div>
        );
    }

    render() {
        const { t, domain } = this.props;

        return (
            <Panel
                header={<PanelHeader title={t('beneficiaryRightsDetails.claim.title')} />}
                panelTheme="secondary"
                border={false}
            >
                <PanelSection>
                    {(domain.prestations || []).map((item, index) => (
                        <>
                            {this.renderPrestation(item)}
                            {index !== domain.prestations.length - 1 && (
                                <hr
                                    style={{
                                        background: '#d6d6d6',
                                        color: '##d6d6d6',
                                        borderColor: '#d6d6d6',
                                        height: '0.5px',
                                        marginTop: '5px',
                                        marginBottom: '0px',
                                    }}
                                />
                            )}
                        </>
                    ))}
                </PanelSection>
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
