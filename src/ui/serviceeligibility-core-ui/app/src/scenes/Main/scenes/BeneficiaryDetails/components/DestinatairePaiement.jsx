import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { PanelSection, Row, Col, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import businessUtils from '../../../../../common/utils/businessUtils';
import Address from '../../../../../common/components/Address/Address';

@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class DestinatairePaiement extends Component {
    render() {
        const { t, firstDestinatairesPaiements } = this.props;
        const destinatairesPaiementsNom = (firstDestinatairesPaiements || {}).nom;
        const destinatairesPaiementsAdresse = (firstDestinatairesPaiements || {}).adresse;
        const destinatairesPaiementsRib = (firstDestinatairesPaiements || {}).rib;
        const destinatairesPaiementsModePaiement = (firstDestinatairesPaiements || {}).modePaiementPrestations;
        const denomination =
            destinatairesPaiementsNom.civilite &&
            destinatairesPaiementsNom.prenom &&
            destinatairesPaiementsNom.nomFamille &&
            `${destinatairesPaiementsNom.civilite} ${destinatairesPaiementsNom.nomFamille} ${destinatairesPaiementsNom.prenom}`;

        return (
            <PanelSection>
                <Row>
                    <Col xs="12" md="5">
                        <LabelValuePresenter
                            id="idDestPaiement"
                            label={t('beneficiaryDetails.servicePrestation.idDestPaiement')}
                            value={(firstDestinatairesPaiements || {}).idDestinatairePaiements}
                            defaultValue="-"
                            inline
                        />
                    </Col>
                    <Col xs="12" md="7">
                        <div>
                            <LabelValuePresenter
                                label={t('beneficiaryDetails.iban')}
                                id="iban"
                                value={businessUtils.formatIBAN((destinatairesPaiementsRib || {}).iban)}
                                defaultValue="-"
                                inline
                            />
                        </div>
                    </Col>
                </Row>
                <Row>
                    <Col xs="12" md="5">
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
                        {destinatairesPaiementsNom.raisonSociale && (
                            <LabelValuePresenter
                                label={t('beneficiaryDetails.servicePrestation.raisonSociale')}
                                id="raisonSociale"
                                value={destinatairesPaiementsNom.raisonSociale}
                                defaultValue="-"
                                inline
                            />
                        )}
                        <Address address={destinatairesPaiementsAdresse} />
                    </Col>
                    <Col xs="12" md="7">
                        <div>
                            <LabelValuePresenter
                                label={t('beneficiaryDetails.bic')}
                                id="bic"
                                value={(destinatairesPaiementsRib || {}).bic}
                                defaultValue="-"
                                inline
                            />
                        </div>
                        <div>
                            <LabelValuePresenter
                                id="prest"
                                label={t('beneficiaryDetails.servicePrestation.prest')}
                                value={(destinatairesPaiementsModePaiement || {}).libelle}
                                defaultValue="-"
                                inline
                            />
                        </div>
                        <div>
                            <LabelValuePresenter
                                id="prestMonnaie"
                                label={t('beneficiaryDetails.servicePrestation.prestMonnaie')}
                                value={(destinatairesPaiementsModePaiement || {}).codeMonnaie}
                                defaultValue="-"
                                inline
                            />
                        </div>
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

DestinatairePaiement.propTypes = propTypes;
// Add default props
DestinatairePaiement.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default DestinatairePaiement;
