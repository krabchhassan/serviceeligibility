/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
    PanelHeader,
    Panel,
    PanelSection,
    Row,
    Col,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import style from '../../style.module.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class Communication extends Component {
    renderCommunication() {
        const { communication } = this.props;

        if (communication) {
            const { email, mapLink, lignesAdresse, telephone } = communication;
            return (
                <Fragment>
                    <Row>
                        <Col xs={1} className="d-flex justify-content-start align-items-start">
                            <a href={mapLink} target="_blank" rel="noopener noreferrer">
                                <CgIcon
                                    name="map-marker"
                                    size="xs"
                                    id="communication-map-marker"
                                    className="text-default lt"
                                />
                            </a>
                        </Col>
                        <Col className="pl-0">
                            {(lignesAdresse || ['-']).map(
                                (adressLine) => adressLine && <div key={adressLine}>{adressLine}</div>,
                            )}
                        </Col>
                    </Row>
                    <Row>
                        <Col xs={1} className="d-flex justify-content-center align-items-center pl-0">
                            <CgIcon name="phone" id="communication-phone" size="xs" className="text-default lt" />
                        </Col>
                        <Col className="pl-0">{telephone || '-'}</Col>
                    </Row>
                    <Row>
                        <Col xs={1} className="d-flex justify-content-center align-items-center pl-0">
                            <CgIcon name="mail" id="communication-envelope" size="xs" className="text-default lt" />
                        </Col>
                        <Col className="pl-0">{email || '-'}</Col>
                    </Row>
                </Fragment>
            );
        }
        return <Fragment />;
    }

    render() {
        const { communication, t } = this.props;

        if (communication) {
            const { email, mapLink, titre, telephone, lignesAdresse } = communication;

            return (
                <Panel
                    panelTheme="secondary"
                    className={style['display-panel-content-center']}
                    header={<PanelHeader title={titre} />}
                    border={false}
                >
                    <PanelSection className="flex-auto">
                        {!email && !telephone && !mapLink && !lignesAdresse ? (
                            <div className="d-flex justify-content-center">
                                {t('beneficiaryRightsDetails.noCoordinates')}
                            </div>
                        ) : (
                            this.renderCommunication()
                        )}
                    </PanelSection>
                </Panel>
            );
        }
        return <Fragment />;
    }
}

Communication.propTypes = {
    t: PropTypes.func,
    communication: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Communication;
