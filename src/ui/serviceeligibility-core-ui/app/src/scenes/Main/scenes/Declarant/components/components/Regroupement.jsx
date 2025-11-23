/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import isEqual from 'lodash/isEqual';
import autobind from 'autobind-decorator';
import { Panel, PanelHeader, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import RegroupementForm from './RegroupementForm';

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class Regroupement extends Component {
    constructor(props) {
        super(props);
        this.state = {
            expanded: true,
        };
    }

    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(nextProps, this.props) || !isEqual(nextState, this.state);
    }

    @autobind
    toggleCollapse() {
        const { expanded } = this.state;
        this.setState({ expanded: !expanded });
    }

    render() {
        const {
            t,
            isCreate,
            isEdit,
            prefix,
            isServiceOpen,
            conventions,
            regroupement,
            removeItem,
            calculatedErrors,
            change,
        } = this.props;
        const { expanded } = this.state;

        const title = [];

        const couloir = regroupement.couloirClient;
        if (couloir) {
            title.push(`${t('declarant.couloirClient')}${t('colon')}${couloir}`);
        }
        const perimetre = regroupement.nomPerimetre;
        if (perimetre) {
            title.push(`${t('declarant.nomPerimetre')}${t('colon')}${perimetre}`);
        }
        const critere = regroupement.critereRegroupementDetaille;
        if (critere) {
            title.push(`${t('declarant.critereRegroupementDetaille')}${t('colon')}${critere}`);
        }

        const selectedConventionCode = get(regroupement, `typeConventionnement`);

        const actions = [];
        if (isEdit || isCreate) {
            actions.push({
                id: 'delete',
                action: removeItem,
                buttonProps: { type: 'button', outline: true },
                icon: 'trash',
                behavior: 'danger',
                prefix,
            });
        }

        return (
            <Panel
                onCollapseClick={this.toggleCollapse}
                expanded={expanded}
                header={<PanelHeader title={title.join(' - ')} actions={actions} />}
                id="almv3"
            >
                <RegroupementForm
                    key={prefix}
                    prefix={prefix}
                    regroupement={regroupement}
                    isServiceOpen={isServiceOpen}
                    isCreate={isCreate}
                    isEdit={isEdit}
                    conventions={conventions}
                    selectedConventionCode={selectedConventionCode}
                    calculatedErrors={calculatedErrors}
                    change={change}
                />
            </Panel>
        );
    }
}

Regroupement.propTypes = {
    t: PropTypes.func,
    removeItem: PropTypes.func,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    isServiceOpen: PropTypes.bool,
    regroupement: PropTypes.shape(),
    calculatedErrors: PropTypes.shape(),
    change: PropTypes.func,
    prefix: PropTypes.string,
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Regroupement;
