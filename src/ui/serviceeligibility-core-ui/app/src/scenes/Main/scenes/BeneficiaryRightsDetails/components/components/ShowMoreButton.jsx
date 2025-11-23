import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Button, Col, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import CommonSpinner from '../../../../../../common/components/CommonSpinner/CommonSpinner';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility'], { wait: true })
class ShowMoreButton extends Component {
    render() {
        const { t, isLoading, shouldShowMoreButton, onClick } = this.props;
        return (
            <>
                {isLoading && <CommonSpinner />}
                {shouldShowMoreButton && (
                    <Row>
                        <Col xs={10}>{null}</Col>
                        <Col xs={2}>
                            <div className="pl-5 ml-4">
                                <Button type="button" behavior="primary" onClick={onClick}>
                                    {t('beneficiaryRightsDetails.showMoreHistory')}
                                </Button>
                            </div>
                        </Col>
                    </Row>
                )}
            </>
        );
    }
}

ShowMoreButton.propTypes = {
    t: PropTypes.func,
    isLoading: PropTypes.bool,
    shouldShowMoreButton: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ShowMoreButton;
