/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { PageLayout, BodyHeader, Row, Col, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import HomeBreadcrumbPart from './components/HomeBreadcrumbPart';
import SearchPanel from './components/SearchPanel/SearchPanel';
import ResultSearch from './components/ResultSearch/ResultSearch';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
// Prop types
const propTypes = {
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['bobb'], { wait: true })
class HomeComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            bodyCriterias: {},
        };
    }

    componentDidMount() {
        const { t } = this.props;
        document.title = t('tabTitle');
    }

    updateBodyCriterias = (newBodyCriterias) => {
        this.setState({ bodyCriterias: newBodyCriterias });
    };

    render() {
        const { t } = this.props;
        const { bodyCriterias } = this.state;
        return (
            <PageLayout header={<BodyHeader title={t('pageTitle')} />}>
                <HomeBreadcrumbPart />
                <Row>
                    <Col>
                        <SearchPanel handle={this.updateBodyCriterias} />
                    </Col>
                </Row>
                <Row>
                    <Col xs={12} className="pr-0">
                        <ResultSearch bodyCriterias={bodyCriterias} />
                    </Col>
                </Row>
            </PageLayout>
        );
    }
}

// Add prop types
HomeComponent.propTypes = propTypes;
// Add default props
HomeComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HomeComponent;
