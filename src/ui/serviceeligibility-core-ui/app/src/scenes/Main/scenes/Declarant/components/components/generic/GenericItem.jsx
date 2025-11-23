/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import {
    Button,
    Modal,
    ModalBody,
    ModalHeader,
    ControlledPanel,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class GenericItem extends Component {
    constructor(props) {
        super(props);
        this.state = {
            modal: false,
        };
    }

    toggle() {
        const { modal } = this.state;
        this.setState({ modal: !modal });
    }

    renderModal(declarant) {
        const { t } = this.props;
        const { modal } = this.state;

        return (
            <div>
                {/* eslint-disable-next-line jsx-a11y/label-has-for */}
                <label className="mb-1">Oui </label>
                <Button outlineNoBorder behavior="secondary" size="sm" className="ml-1 px-1 py-1">
                    <CgIcon name="open-modal" fixedWidth onClick={() => this.toggle()} />
                </Button>
                <Modal size="sm" isOpen={modal} isOutsideModalClicked={false} toggle={() => this.toggle()} backdrop>
                    <ModalHeader toggle={() => this.toggle()}>{t('declarant.listeAMCEchange')}</ModalHeader>
                    <ModalBody>
                        <ControlledPanel id="panel-1" label="Panel 1" initialExpanded>
                            {declarant.numerosAMCEchanges}
                        </ControlledPanel>
                    </ModalBody>
                </Modal>
            </div>
        );
    }

    render() {
        const { t, basicProps, additionalProps } = this.props;
        const { isCreate, isEdit, declarant } = basicProps;
        const {
            rootComponent: RootComponent,
            trueLabel,
            falseLabel,
            prefix,
            fieldName,
            required,
            forcedValue,
        } = additionalProps;

        const completeFieldName = prefix ? `${prefix}.${fieldName}` : fieldName;

        const transTooltipKey = `declarant.${fieldName}Tooltip`;
        const transTooltip = t(transTooltipKey);
        const tooltipTranslation = transTooltip === transTooltipKey ? null : transTooltip;

        const transPlaceholderKey = `declarant.${fieldName}Placeholder`;
        const transPlaceholder = t(transPlaceholderKey);
        const placeholderTranslation = transPlaceholder === transPlaceholderKey ? null : transPlaceholder;

        return (
            <RootComponent
                isCreate={isCreate}
                isEdit={isEdit}
                declarant={declarant}
                additionalProps={
                    required
                        ? {
                              ...additionalProps,
                              validate: [required],
                              tooltip: tooltipTranslation,
                              placeholder: placeholderTranslation,
                          }
                        : {
                              ...additionalProps,
                              tooltip: tooltipTranslation,
                              placeholder: placeholderTranslation,
                          }
                }
                trueLabel={trueLabel}
                falseLabel={falseLabel}
                isConsultation={!isCreate && !isEdit}
                fieldName={completeFieldName}
                propertyName={fieldName}
                value={
                    fieldName === 'numerosAMCEchanges' && get(declarant, completeFieldName) != null
                        ? this.renderModal(declarant)
                        : forcedValue || get(declarant, completeFieldName)
                }
            />
        );
    }
}

GenericItem.propTypes = {
    t: PropTypes.func,
    basicProps: PropTypes.shape(),
    additionalProps: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default GenericItem;
