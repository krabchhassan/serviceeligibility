import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { PanelSection, Row, Col, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import Address from '../../../../../common/components/Address/Address';

@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class DestinatairePrestation extends Component {
    render() {
        const { t, firstDestinatairesRelevePrestations } = this.props;
        const destinatairesRelevePrestationsNom = (firstDestinatairesRelevePrestations || {}).nom;
        const destinatairesRelevePrestationsAdresse = (firstDestinatairesRelevePrestations || {}).adresse;
        const { dematerialisation } = firstDestinatairesRelevePrestations || {};
        const denomination =
            destinatairesRelevePrestationsNom.civilite &&
            destinatairesRelevePrestationsNom.prenom &&
            destinatairesRelevePrestationsNom.nomFamille &&
            `${destinatairesRelevePrestationsNom.civilite} ${destinatairesRelevePrestationsNom.nomFamille} ${destinatairesRelevePrestationsNom.prenom}`;

        return (
            <PanelSection>
                <Row>
                    <Col>
                        <LabelValuePresenter
                            id="idDestRelevePrestation"
                            label={t('beneficiaryDetails.servicePrestation.idDestRelevePrestation')}
                            value={(firstDestinatairesRelevePrestations || {}).idDestinataireRelevePrestations}
                            defaultValue="-"
                            inline
                        />
                    </Col>
                    <Col>
                        <LabelValuePresenter
                            id="dematerialisation"
                            label={t('beneficiaryDetails.servicePrestation.dematerialisation')}
                            value={(dematerialisation || {}).isDematerialise ? t(`yes`) : t(`no`)}
                            inline
                        />
                    </Col>
                </Row>
                <Row>
                    <Col>
                        {denomination && (
                            <div>
                                <LabelValuePresenter
                                    label={t('beneficiaryDetails.servicePrestation.denomination')}
                                    id="denomination"
                                    value={denomination}
                                    defaultValue="-"
                                    inline
                                />
                            </div>
                        )}
                        {destinatairesRelevePrestationsNom.raisonSociale && (
                            <LabelValuePresenter
                                label={t('beneficiaryDetails.servicePrestation.raisonSociale')}
                                id="raisonSociale"
                                value={destinatairesRelevePrestationsNom.raisonSociale}
                                defaultValue="-"
                                inline
                            />
                        )}
                        <Address address={destinatairesRelevePrestationsAdresse} />
                    </Col>
                    <Col>
                        {(dematerialisation || {}).isDematerialise && (
                            <Fragment>
                                {(dematerialisation || {}).email && (
                                    <div>
                                        <LabelValuePresenter
                                            id="email"
                                            label={t('beneficiaryDetails.servicePrestation.email')}
                                            value={(dematerialisation || {}).email}
                                            defaultValue="-"
                                            inline
                                        />
                                    </div>
                                )}
                                {(dematerialisation || {}).mobile && (
                                    <div>
                                        <LabelValuePresenter
                                            id="mobile"
                                            label={t('beneficiaryDetails.servicePrestation.mobile')}
                                            value={(dematerialisation || {}).mobile}
                                            defaultValue="-"
                                            inline
                                        />
                                    </div>
                                )}
                            </Fragment>
                        )}
                    </Col>
                </Row>
            </PanelSection>
        );
    }
}

const propTypes = {
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

DestinatairePrestation.propTypes = propTypes;
// Add default props
DestinatairePrestation.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default DestinatairePrestation;
