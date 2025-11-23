/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Panel, PanelHeader, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import utils from '../../../../../../../../common/utils/businessUtils';
import Constants from '../../../../../../../../common/utils/Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const formatUniteTaux = (code) => {
    if (!code || code === 'XX') {
        return '';
    }
    return `typeTaux_${code}`;
};

const rateValue = (tauxRemboursement, uniteTaux) => {
    return `${tauxRemboursement} ${formatUniteTaux(uniteTaux)}`;
};

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class DeclarationCouverture extends Component {
    render() {
        const { domainCode, domain, t, TPOnlineSelected, clientType } = this.props;
        const {
            garantie,
            garantieLibelle,
            formulaMask,
            naturePrestation,
            priorites,
            couverture,
            tauxRemboursement,
            uniteTaux,
        } = domain;

        const { produit, produitLibelle, produitExterne, produitExterneLibelle, referenceCouverture } = couverture;

        return (
            <Panel
                header={<PanelHeader title={t('beneficiaryRightsDetails.coverRights.title')} />}
                panelTheme="secondary"
                border={false}
            >
                <PanelSection>
                    <LabelValuePresenter
                        id="right-cover-product-ex"
                        label={t('beneficiaryRightsDetails.coverRights.garantiesComp')}
                        value={`${garantie} - ${garantieLibelle}`}
                    />
                    {clientType !== Constants.TOPOLOGY_INSURER && (
                        <LabelValuePresenter
                            id="right-cover-subscription"
                            label={t('beneficiaryRightsDetails.coverRights.refCover')}
                            value={referenceCouverture}
                        />
                    )}
                    <LabelValuePresenter
                        id="right-cover-product"
                        label={t('beneficiaryRightsDetails.coverRights.product')}
                        value={utils.getLibelleConverter(produit, produitLibelle)}
                    />
                    <LabelValuePresenter
                        id="right-cover-ref"
                        label={t('beneficiaryRightsDetails.coverRights.externalProduct')}
                        value={utils.getLibelleConverter(produitExterne, produitExterneLibelle)}
                    />
                    {naturePrestation && clientType === Constants.TOPOLOGY_INSURER && (
                        <LabelValuePresenter
                            id="right-nature-of-service"
                            label={t('beneficiaryRightsDetails.coverRights.natureOfPrestation')}
                            value={naturePrestation}
                        />
                    )}
                    <LabelValuePresenter
                        id={`right-item-${domainCode}-priority`}
                        label={t('beneficiaryRightsDetails.coverRights.priority')}
                        value={priorites.prioriteBackOffice}
                    />
                    {(!TPOnlineSelected || clientType !== Constants.TOPOLOGY_INSURER) &&
                        (formulaMask && formulaMask !== null ? (
                            <LabelValuePresenter
                                id={`right-item-${domainCode}-rate`}
                                label={t('beneficiaryRightsDetails.formulaMask')}
                                value={formulaMask}
                            />
                        ) : (
                            <LabelValuePresenter
                                id={`right-item-${domainCode}-rate`}
                                label={t('beneficiaryRightsDetails.coverRights.taux')}
                                value={rateValue(tauxRemboursement, uniteTaux)}
                            />
                        ))}
                </PanelSection>
            </Panel>
        );
    }
}

DeclarationCouverture.propTypes = {
    t: PropTypes.func,
    domain: PropTypes.shape(),
    TPOnlineSelected: PropTypes.bool,
    clientType: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default DeclarationCouverture;
