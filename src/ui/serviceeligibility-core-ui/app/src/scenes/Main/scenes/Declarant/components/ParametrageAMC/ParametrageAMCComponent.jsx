/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Panel, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import ConventionTPComponent from './ConventionTP/ConventionTPComponent';
import CodeRenvoiTPComponent from './CodeRenvoiTP/CodeRenvoiTPComponent';
import RegroupementDomainesTPComponent from './RegroupementDomainesTP/RegroupementDomainesTPComponent';
import FondCarteComponent from './FondCarte/FondCarteComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class ParametrageAMCComponent extends Component {
    render() {
        const { declarant, isCreate, isCopy, isEdit, reseauxSoin, domainList, conventions, returnCodesList } =
            this.props;

        const { conventionTP } = declarant || {};
        const { codeRenvoiTP } = declarant || {};
        const { regroupementDomainesTP } = declarant || {};
        const { fondCarteTP } = declarant || {};

        return (
            <Panel>
                <PanelSection>
                    <FondCarteComponent
                        fondCarte={fondCarteTP}
                        isEdit={isEdit}
                        isCreate={isCreate}
                        isCopy={isCopy}
                        reseauxSoin={reseauxSoin}
                    />
                    <ConventionTPComponent
                        conventionTP={conventionTP}
                        isEdit={isEdit}
                        isCreate={isCreate}
                        isCopy={isCopy}
                        domainList={domainList}
                        reseauxSoin={reseauxSoin}
                        conventions={conventions}
                    />
                    <CodeRenvoiTPComponent
                        codeRenvoiTP={codeRenvoiTP}
                        isEdit={isEdit}
                        isCreate={isCreate}
                        isCopy={isCopy}
                        domainList={domainList}
                        reseauxSoin={reseauxSoin}
                        returnCodesList={returnCodesList}
                    />
                    <RegroupementDomainesTPComponent
                        regroupementDomainesTP={regroupementDomainesTP}
                        isEdit={isEdit}
                        isCreate={isCreate}
                        isCopy={isCopy}
                        domainList={domainList}
                    />
                </PanelSection>
            </Panel>
        );
    }
}

ParametrageAMCComponent.propTypes = {
    isCreate: PropTypes.bool,
    isCopy: PropTypes.bool,
    isEdit: PropTypes.bool,
    declarant: PropTypes.shape(),
    reseauxSoin: PropTypes.arrayOf(PropTypes.shape()),
    domainList: PropTypes.arrayOf(PropTypes.shape()),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    returnCodesList: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParametrageAMCComponent;
