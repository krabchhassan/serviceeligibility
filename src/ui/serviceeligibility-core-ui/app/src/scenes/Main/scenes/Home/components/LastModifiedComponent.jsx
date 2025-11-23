/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    LoadingSpinner,
    Panel,
    PanelFooter,
    PanelHeader,
    PanelSection,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import SearchResultItem from './SearchResultItem';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class LastModifiedComponent extends Component {
    componentDidMount() {
        const { getLastModified } = this.props;
        getLastModified();
    }

    render() {
        const { t, lastModified, loadingLastModifiedAMC } = this.props;
        const footerComment = `${(lastModified || []).length} ${t('declarant.amcLatelyModified')}`;

        return (
            <Panel
                id="last-modified-amc"
                className="bg-background-variant-1"
                border={false}
                header={<PanelHeader title={t('declarant.amcLastModified')} className="pl-0 pr-0" />}
                footer={<PanelFooter comment={footerComment} />}
            >
                {loadingLastModifiedAMC && (
                    <LoadingSpinner type="over-container" iconSize="3x" iconType="circle-o-notch" behavior="primary" />
                )}
                <PanelSection className="pl-0 pr-0">
                    {(lastModified || []).length > 0
                        ? lastModified.map((item) => <SearchResultItem key={item.numero} item={item} />)
                        : t('noResults')}
                </PanelSection>
            </Panel>
        );
    }
}

LastModifiedComponent.propTypes = {
    t: PropTypes.func,
    lastModified: PropTypes.arrayOf(PropTypes.shape()),
    getLastModified: PropTypes.func,
    loadingLastModifiedAMC: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default LastModifiedComponent;
