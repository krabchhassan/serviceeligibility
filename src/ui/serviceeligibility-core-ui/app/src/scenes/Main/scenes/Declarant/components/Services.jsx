/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Panel, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import Priorisation from './components/Priorisation';
import Visiodroit from './components/Visiodroit';
import Seltp from './components/Seltp';
import Roc from './components/Roc';
import Dclben from './components/Dclben';
import Tpgis from './components/Tpgis';
import CarteTP from './components/CarteTP';
import Almv3 from './components/Almv3';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class Services extends Component {
    render() {
        const { isCreate, isEdit, isCopy, declarant, conventions, domainList, change, formName, calculatedErrors } =
            this.props;
        return (
            <Panel id="priorisation">
                <PanelSection>
                    <Priorisation
                        isCreate={isCreate}
                        isEdit={isEdit}
                        declarant={declarant}
                        pilotagePrefix="pilotages[0]"
                    />
                    <Visiodroit
                        isCreate={isCreate}
                        isEdit={isEdit}
                        declarant={declarant}
                        pilotagePrefix="pilotages[1]"
                    />
                    <Seltp isCreate={isCreate} isEdit={isEdit} declarant={declarant} pilotagePrefix="pilotages[10]" />
                    <Roc isCreate={isCreate} isEdit={isEdit} declarant={declarant} pilotagePrefix="pilotages[9]" />
                    <Dclben
                        isCreate={isCreate}
                        isEdit={isEdit}
                        declarant={declarant}
                        pilotagePrefix="pilotages[2]"
                        conventions={conventions}
                        dclben="IS"
                    />
                    <Dclben
                        isCreate={isCreate}
                        isEdit={isEdit}
                        declarant={declarant}
                        pilotagePrefix="pilotages[3]"
                        conventions={conventions}
                        dclben="SP"
                    />
                    <Tpgis
                        isCreate={isCreate}
                        isEdit={isEdit}
                        declarant={declarant}
                        pilotagePrefix="pilotages[4]"
                        conventions={conventions}
                    />
                    <Tpgis
                        isCreate={isCreate}
                        isEdit={isEdit}
                        declarant={declarant}
                        pilotagePrefix="pilotages[5]"
                        conventions={conventions}
                    />
                    <Almv3
                        isCreate={isCreate}
                        isEdit={isEdit}
                        declarant={declarant}
                        pilotagePrefix="pilotages[6]"
                        conventions={conventions}
                        change={change}
                        formName={formName}
                        calculatedErrors={calculatedErrors}
                    />
                    <CarteTP
                        isCreate={isCreate}
                        isEdit={isEdit}
                        isCopy={isCopy}
                        declarant={declarant}
                        pilotagePrefixCartePapier="pilotages[7]"
                        pilotagePrefixCarteDemat="pilotages[8]"
                        domainList={domainList}
                    />
                </PanelSection>
            </Panel>
        );
    }
}

Services.propTypes = {
    change: PropTypes.func,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    formName: PropTypes.string,
    declarant: PropTypes.shape(),
    calculatedErrors: PropTypes.arrayOf(PropTypes.bool),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    domainList: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Services;
