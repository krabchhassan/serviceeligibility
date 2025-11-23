/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import classnames from 'classnames';
import { Button, Card, CardBlock, CardText, Col } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
// eslint-disable-next-line no-unused-vars
import styleCss from './style.module.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const propTypes = {
    id: PropTypes.string.isRequired,
    icon: PropTypes.string.isRequired,
    title: PropTypes.string.isRequired,
    subtitle: PropTypes.string,
    onClick: PropTypes.func,
    xs: PropTypes.number,
    sm: PropTypes.number,
    md: PropTypes.number,
    lg: PropTypes.number,
    xl: PropTypes.number,
};
const defaultProps = {};

const isFontAwesomeIcon = (icon) => icon.startsWith('fa');

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
class MenuButton extends Component {
    render() {
        const { id, icon, onClick, title, subtitle, xs, sm, md, lg, xl, iconSize } = this.props;
        const useFontAwesome = isFontAwesomeIcon(icon);
        return (
            <Col xs={xs} sm={sm} md={md} lg={lg} xl={xl}>
                <Card className="w-100 d-flex justify-content-center border-0 pb-3 pt-4">
                    <CardBlock>
                        <CardText className="d-flex flex-column justify-content-center">
                            <Button
                                id={id}
                                onClick={onClick}
                                outline
                                behavior="secondary"
                                className={styleCss['homepage-button']}
                            >
                                <span className="h3">{title}</span>
                                <span className="d-flex justify-content-center pt-3">
                                    <CgIcon
                                        name={(!useFontAwesome && icon) || undefined}
                                        className={classnames({ [`fa ${icon}`]: useFontAwesome })}
                                        size={iconSize || '5x'}
                                    />
                                </span>
                            </Button>
                            <h6 className="text-center text-secondary">{subtitle}</h6>
                        </CardText>
                    </CardBlock>
                </Card>
            </Col>
        );
    }
}

MenuButton.propTypes = propTypes;
MenuButton.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default MenuButton;
