/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';

import { Panel, PanelHeader, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import history from '../../../../../history';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class SearchResultItem extends Component {
    constructor(props) {
        super(props);

        this.state = {
            expanded: true,
        };
    }

    @autobind
    toggleCollapse() {
        const { expanded } = this.state;
        this.setState({ expanded: !expanded });
    }

    @autobind
    goToDetails() {
        const { item } = this.props;
        history.push(`/insurer/${item.numero}`);
    }

    render() {
        const { t, item } = this.props;
        const { expanded } = this.state;
        const panelTitle = `${item.numero} - ${item.nom} - ${item.libelle}`;
        const counter = (item.services || []).length;
        const counterTitle = `${(item.services || []).length} ${t('home.activeServices')}`;
        const toolbar = {
            label: t('common:open'),
            action: this.goToDetails,
            key: 'open',
            id: 'open',
            behavior: 'secondary',
            icon: 'arrow-circle-right',
        };
        return (
            <Fragment>
                {counter === 0 && (
                    <Panel
                        header={<PanelHeader title={panelTitle} counter={counterTitle} actions={[toolbar]} />}
                        id={item.numero}
                        headerBorder={false}
                    />
                )}
                {counter > 0 && (
                    <Panel
                        onCollapseClick={this.toggleCollapse}
                        expanded={expanded}
                        header={<PanelHeader title={panelTitle} counter={counterTitle} actions={[toolbar]} />}
                        id={item.numero}
                    >
                        <PanelSection>
                            {(item.services || []).map((service) => {
                                const couloirs = (service.listCouloirs || []).join(', ');
                                const serviceName = service?.service === 'CARTE-TP' ? 'CARTE-PAPIER' : service.service;
                                return (
                                    <div key={serviceName}>
                                        <span className="font-weight-bold">{serviceName}</span>
                                        {service.listCouloirs.length > 0 && (
                                            <Fragment>
                                                <span className="cgd-comment">
                                                    {' '}
                                                    - {t('home.couloirs')}
                                                    {t('colon')}
                                                </span>
                                                <span>{couloirs}</span>
                                            </Fragment>
                                        )}
                                    </div>
                                );
                            })}
                        </PanelSection>
                    </Panel>
                )}
            </Fragment>
        );
    }
}

SearchResultItem.propTypes = {
    t: PropTypes.func,
    item: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default SearchResultItem;
