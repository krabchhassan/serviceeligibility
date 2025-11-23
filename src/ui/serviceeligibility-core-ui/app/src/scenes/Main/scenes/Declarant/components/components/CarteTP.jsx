/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import isEqual from 'lodash/isEqual';
import { Col, Row, PanelSection, Alert, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import GenericService from './generic/GenericService';
import GenericItem from './generic/GenericItem';
import DateItem from './generic/DateItem';
import ServiceParamLineForCarteTP from './generic/ServiceParamLineForCarteTP';
import ValiditePanelComponent from './generic/components/ValiditePanelComponent';
import style from '../../style.module.scss';
import CarteEditablePapier from './CarteEditablePapierComponent';
import TextItem from './generic/TextItem';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class CarteTP extends Component {
    shouldComponentUpdate(nextProps) {
        if (!isEqual(nextProps.isCreate, this.props.isCreate) || !isEqual(nextProps.isEdit, this.props.isEdit)) {
            return true;
        }
        return !isEqual(
            get(nextProps.declarant, nextProps.pilotagePrefix),
            get(this.props.declarant, this.props.pilotagePrefixCartePapier),
        );
    }

    render() {
        const {
            t,
            isCreate,
            isEdit,
            isCopy,
            declarant,
            pilotagePrefixCartePapier,
            pilotagePrefixCarteDemat,
            domainList,
        } = this.props;

        const basicProps = {
            isCreate,
            isEdit,
            declarant,
        };

        let openValidation = {};

        const dateSynchro = `.regroupements[0].dateSynchronisation`;
        const title = get(declarant, `${pilotagePrefixCartePapier}.nom`);
        const statusValueCartePapier = get(declarant, `${pilotagePrefixCartePapier}.serviceOuvert`) === 'true';
        const statusValueCarteDemat = get(declarant, `${pilotagePrefixCarteDemat}.serviceOuvert`) === 'true';
        const existDateSynchro = get(declarant, `${pilotagePrefixCarteDemat}${dateSynchro}`);
        const isServiceOpen = statusValueCartePapier || statusValueCarteDemat;
        if (isServiceOpen) {
            openValidation = {
                showRequired: true,
                validate: required,
            };
        }
        const isModify = isCreate || isCopy || isEdit;
        const showEditCarteMessage = statusValueCartePapier && isModify && existDateSynchro;
        const showCarteMessageInfo =
            get(declarant, `${pilotagePrefixCartePapier}.isCarteEditable`) === 'true' && !isModify && existDateSynchro;

        return (
            <GenericService title={title} statusValue={isServiceOpen} isCreate={isCreate}>
                <Row>
                    <Col xs={8}>
                        <PanelSection title={t('declarant.editionCartePapier')} className={style.borderBoxBottom}>
                            <Row>
                                <ServiceParamLineForCarteTP
                                    isCreate={isCreate}
                                    isEdit={isEdit}
                                    declarant={declarant}
                                    pilotagePrefix={pilotagePrefixCartePapier}
                                    isServiceOpen={statusValueCartePapier}
                                />
                            </Row>
                        </PanelSection>
                        <PanelSection title={t('declarant.editionCarteDemat')}>
                            <Row>
                                <ServiceParamLineForCarteTP
                                    isCreate={isCreate}
                                    isEdit={isEdit}
                                    declarant={declarant}
                                    pilotagePrefix={pilotagePrefixCarteDemat}
                                    isServiceOpen={statusValueCarteDemat}
                                />
                            </Row>
                            <Row>
                                <Col xs={12}>
                                    <ValiditePanelComponent
                                        declarant={declarant}
                                        isEdit={isEdit}
                                        isCreate={isCreate}
                                        isCopy={isCopy}
                                        pilotagePrefix={pilotagePrefixCarteDemat}
                                        domainList={domainList}
                                    />
                                </Col>
                            </Row>
                        </PanelSection>
                    </Col>
                    <Col
                        xs={4}
                        className={`${style.borderBoxLeft} ${
                            showCarteMessageInfo || showEditCarteMessage
                                ? 'align-items-center pt-5'
                                : 'd-block align-items-center pt-5'
                        }`}
                    >
                        <PanelSection>
                            <Row>
                                <Col xs={12} className={isModify ? 'pl-5 ml-3' : 'pl-5 ml-5'}>
                                    <GenericItem
                                        basicProps={basicProps}
                                        additionalProps={{
                                            fieldName: 'couloirClient',
                                            prefix: `${pilotagePrefixCarteDemat}.regroupements[0]`,
                                            rootComponent: TextItem,
                                            disabled: !isServiceOpen,
                                            ...openValidation,
                                            columnSize: 8,
                                        }}
                                    />
                                </Col>
                            </Row>
                            <Row>
                                <Col className="pl-0 ml-0">
                                    <GenericItem
                                        basicProps={basicProps}
                                        additionalProps={{
                                            fieldName: 'dateSynchronisation',
                                            prefix: `${pilotagePrefixCarteDemat}.regroupements[0]`,
                                            rootComponent: DateItem,
                                            disabled: !isServiceOpen,
                                            columnSize: 12,
                                        }}
                                    />
                                </Col>
                                {showEditCarteMessage ? (
                                    <Col xs={10} className="ml-5 pl-5">
                                        <Alert headline={t('declarant.editerCartePapier')} behavior="danger">
                                            <CarteEditablePapier pilotagePrefix={pilotagePrefixCartePapier} />
                                        </Alert>
                                    </Col>
                                ) : null}
                            </Row>

                            {showCarteMessageInfo ? (
                                <Row className="justify-content-center mr-4">{t('Avec réédition de carte')}</Row>
                            ) : null}
                        </PanelSection>
                    </Col>
                </Row>
            </GenericService>
        );
    }
}

CarteTP.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    isCopy: PropTypes.bool,
    declarant: PropTypes.shape(),
    pilotagePrefixCartePapier: PropTypes.string,
    pilotagePrefixCarteDemat: PropTypes.string,
    domainList: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default CarteTP;
