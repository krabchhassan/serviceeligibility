/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { FieldArray } from 'redux-form';
import ValiditesFieldArray from './ValiditesFieldArray';
import CreateValiditesFieldArray from './CreateValiditesFieldArrayComponent';
import Constants from './Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class ValiditePanelComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: `${props.pilotagePrefix}.regroupements[0].${Constants.FIELD_ARRAY_NAME}`,
            isOpenPanel: false,
        };
    }

    getValiditesDomainesDroits() {
        const { declarant } = this.props;

        if (declarant) {
            const { pilotages } = declarant;
            if (pilotages && pilotages[8].regroupements && pilotages[8].regroupements[0]) {
                const { validitesDomainesDroits } = pilotages[8].regroupements[0];
                return validitesDomainesDroits;
            }
        }

        return [];
    }

    @autobind
    toggleCollapse() {
        const { isOpenPanel } = this.state;
        this.setState({ isOpenPanel: !isOpenPanel });
    }

    renderPanelView() {
        const { t, domainList } = this.props;

        const objectsList = Object.values(domainList || []).map((domain) => domain.value);
        const validitesDomainesDroits = this.getValiditesDomainesDroits();

        if (validitesDomainesDroits && validitesDomainesDroits.length > 0) {
            return (
                <PanelSection>
                    {validitesDomainesDroits.map((item) => {
                        const domaine = objectsList.find((domain) => domain.code === item.codeDomaine);
                        return (
                            <Row key={item.codeDomaine}>
                                <div className="text-colored-mix-0">
                                    {`${item.codeDomaine} - ${domaine && domaine.label} :`}&nbsp;
                                </div>
                                <div>
                                    {`${t('declarant.panelValidite.valide')} ${item.duree} ${t(
                                        item.unite === 'Jours'
                                            ? 'declarant.panelValidite.jours'
                                            : 'declarant.panelValidite.mois',
                                    )}`}
                                    &nbsp;
                                </div>
                                {item.positionnerFinDeMois && t('declarant.panelValidite.posFinMois')}
                            </Row>
                        );
                    })}
                </PanelSection>
            );
        }

        return <div className="m-2">{t('declarant.paramModal.aucunDomaine')}</div>;
    }

    renderPanelEditOrCreate() {
        const { name } = this.state;
        const { declarant, domainList, isCreate, isCopy, isEdit } = this.props;

        return !isCopy && !isEdit ? (
            <div className="m-3">
                <FieldArray
                    name={name}
                    component={CreateValiditesFieldArray}
                    declarant={declarant}
                    domainList={domainList}
                    isCreate={isCreate}
                />
            </div>
        ) : (
            <div className="m-3">
                <FieldArray
                    name={name}
                    component={ValiditesFieldArray}
                    declarant={declarant}
                    domainList={domainList}
                    isCreate={false}
                />
            </div>
        );
    }

    render() {
        const { isOpenPanel } = this.state;
        const { t, isCreate, isCopy, isEdit } = this.props;

        return (
            <Panel
                id="validites-panel"
                header={<PanelHeader title={t('declarant.paramModal.nomPanel')} />}
                expanded={isOpenPanel}
                onCollapseClick={this.toggleCollapse}
            >
                {isCreate || isEdit || isCopy ? this.renderPanelEditOrCreate() : this.renderPanelView()}
            </Panel>
        );
    }
}

ValiditePanelComponent.propTypes = {
    t: PropTypes.func,
    declarant: PropTypes.shape(),
    isEdit: PropTypes.bool,
    isCreate: PropTypes.bool,
    isCopy: PropTypes.bool,
    domainList: PropTypes.arrayOf(PropTypes.shape()),
    pilotagePrefix: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ValiditePanelComponent;
