/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import isEqual from 'lodash/isEqual';
import autobind from 'autobind-decorator';
import { Panel, Status, PanelHeader, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class GenericService extends Component {
    constructor(props) {
        super(props);
        this.state = {
            expanded: props.isCreate,
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
        const { t, title, statusValue, children } = this.props;
        const { expanded } = this.state;

        return (
            <Panel
                onCollapseClick={this.toggleCollapse}
                expanded={expanded}
                header={
                    <PanelHeader
                        title={title}
                        status={
                            <Status
                                behavior={statusValue ? 'success' : 'default'}
                                label={statusValue ? t('declarant.open') : t('declarant.closed')}
                            />
                        }
                    />
                }
                id={title}
            >
                {children}
            </Panel>
        );
    }
}

GenericService.propTypes = {
    t: PropTypes.func,
    title: PropTypes.string,
    statusValue: PropTypes.bool,
    children: PropTypes.node,
    isCreate: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default GenericService;
