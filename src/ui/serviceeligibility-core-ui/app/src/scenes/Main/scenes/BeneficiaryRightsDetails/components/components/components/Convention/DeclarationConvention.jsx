/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Panel, PanelHeader, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import utils from '../../../../../../../../common/utils/businessUtils';
import CommonSpinner from '../../../../../../../../common/components/CommonSpinner/CommonSpinner';
/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class DeclarationConvention extends Component {
    render() {
        const { conventions, t } = this.props;
        if (!conventions) {
            return <CommonSpinner />;
        }

        return (
            <Panel
                header={<PanelHeader title={t('beneficiaryRightsDetails.convention.title')} />}
                panelTheme="secondary"
                border={false}
            >
                <PanelSection className="pr-5">
                    {(conventions || []).map((item) => {
                        const conventionLabel = utils.getLibelleConverter(item.code, item.libelle);
                        return (
                            <LabelValuePresenter
                                id="priority-convention"
                                key={conventionLabel}
                                label={t('beneficiaryRightsDetails.priorite')}
                                value={`${item.priorite} : ${conventionLabel}`}
                            />
                        );
                    })}
                </PanelSection>
            </Panel>
        );
    }
}

DeclarationConvention.propTypes = {
    t: PropTypes.func,
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default DeclarationConvention;
