/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';

import { Panel, PanelHeader, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import SearchResultItem from './SearchResultItem';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class SearchResult extends Component {
    render() {
        const { t, searchResults, searchCriteria } = this.props;

        return (
            <Panel
                header={
                    <PanelHeader
                        title={t('common:searchResult')}
                        counter={searchResults ? searchResults.length : 0}
                        className="pl-0 pr-0"
                    />
                }
                className="bg-background-variant-1"
                border={false}
            >
                <PanelSection className={searchCriteria ? 'pl-0 pr-0' : 'pl-2 pr-0'}>
                    {searchResults && searchResults.map((item) => <SearchResultItem key={item.numero} item={item} />)}
                    {searchCriteria && searchResults === 0 && t('noResults')}
                </PanelSection>
            </Panel>
        );
    }
}

SearchResult.propTypes = {
    t: PropTypes.func,
    searchResults: PropTypes.arrayOf(PropTypes.shape()),
    searchCriteria: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default SearchResult;
