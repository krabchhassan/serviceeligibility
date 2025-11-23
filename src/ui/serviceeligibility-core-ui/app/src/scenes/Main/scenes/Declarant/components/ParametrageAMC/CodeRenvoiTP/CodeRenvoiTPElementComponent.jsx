/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import { Field } from 'redux-form';
import PropTypes from 'prop-types';
import { Col, Row, Button, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import Constants from './Constants';
import { CommonCombobox, CommonDatePicker } from '../../../../../../../common/utils/Form/CommonFields';
import ValidationFactories from '../../../../../../../common/utils/Form/ValidationFactories';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import DateUtils from '../../../../../../../common/utils/DateUtils';
import ParametrageAMCUtils from '../ParametrageAMCUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class CodeRenvoiTPElementComponent extends Component {
    getNewCodeRenvoiTPElement() {
        const { t, member, domainesTP, reseauxSoinForCombo, returnCodesList } = this.props;

        return (
            <Row>
                <Col xs={2}>
                    <Field
                        id="domaine-tp"
                        key={`${member}.${Constants.FIELDS.domaineTP}`}
                        name={`${member}.${Constants.FIELDS.domaineTP}`}
                        component={CommonCombobox}
                        options={domainesTP}
                        placeholder={t(`${Constants.TRAD_MODAL}.domaineTPPlaceholder`)}
                        formGroupClassName="mb-2"
                        searchable
                        clearable
                        validate={[required]}
                    />
                </Col>
                <Col xs={2}>
                    <Field
                        id="reseau-soin"
                        key={`${member}.${Constants.FIELDS.reseauSoin}`}
                        name={`${member}.${Constants.FIELDS.reseauSoin}`}
                        component={CommonCombobox}
                        options={reseauxSoinForCombo}
                        placeholder={t(`${Constants.TRAD_MODAL}.reseauSoinPlaceholder`)}
                        searchable
                        clearable
                        formGroupClassName="mb-2"
                    />
                </Col>
                <Col xs={3}>
                    <Field
                        id="code-renvoi"
                        key={`${member}.${Constants.FIELDS.codeRenvoi}`}
                        name={`${member}.${Constants.FIELDS.codeRenvoi}`}
                        component={CommonCombobox}
                        options={ParametrageAMCUtils.getMappedForComboboxListWithLibelle(returnCodesList)}
                        placeholder={t(`${Constants.TRAD_MODAL}.codeRenvoiPlaceholder`)}
                        formGroupClassName="mb-2"
                        searchable
                        clearable
                        validate={[required]}
                    />
                </Col>
                <Col xs={2}>
                    <Field
                        id="date-debut"
                        key={`${member}.${Constants.FIELDS.dateDebut}`}
                        name={`${member}.${Constants.FIELDS.dateDebut}`}
                        component={CommonDatePicker}
                        placeholderText={t(`${Constants.TRAD_MODAL}.datePlaceholder`)}
                        formGroupClassName="mb-2"
                        validate={[required]}
                    />
                </Col>
                <Col xs={2}>
                    <Field
                        id="date-fin"
                        key={`${member}.${Constants.FIELDS.dateFin}`}
                        name={`${member}.${Constants.FIELDS.dateFin}`}
                        component={CommonDatePicker}
                        placeholderText={t(`${Constants.TRAD_MODAL}.datePlaceholder`)}
                        formGroupClassName="mb-2"
                    />
                </Col>
                <Col xs={1}>
                    <div>
                        <Button
                            behavior="default"
                            outlineNoBorder
                            onClick={(event) => this.deleteFields(event)}
                            className="pt-2"
                        >
                            <CgIcon behavior="secondary" name="trash-o" />
                        </Button>
                    </div>
                </Col>
            </Row>
        );
    }

    editExistingCodeRenvoiTPElement() {
        const { t, member, codeRenvoi, domainesTP, reseauxSoinForCombo, returnCodesList } = this.props;

        return (
            <Row>
                <Col xs={2}>
                    <LabelValuePresenter
                        id="domaine-tp"
                        key={`${member}.${Constants.FIELDS.domaineTP}`}
                        name={`${member}.${Constants.FIELDS.domaineTP}`}
                        value={ParametrageAMCUtils.getElementLabelForEdit((codeRenvoi || {}).domaineTP, domainesTP)}
                        defaultValue="-"
                        inline
                    />
                </Col>
                <Col xs={2}>
                    <LabelValuePresenter
                        id="reseau-soin"
                        key={`${member}.${Constants.FIELDS.reseauSoin}`}
                        name={`${member}.${Constants.FIELDS.reseauSoin}`}
                        value={ParametrageAMCUtils.getElementLabelForEdit(
                            (codeRenvoi || {}).reseauSoin,
                            reseauxSoinForCombo,
                        )}
                        defaultValue="-"
                        inline
                    />
                </Col>
                <Col xs={3}>
                    <LabelValuePresenter
                        id="code-renvoi"
                        key={`${member}.${Constants.FIELDS.codeRenvoi}`}
                        name={`${member}.${Constants.FIELDS.codeRenvoi}`}
                        value={ParametrageAMCUtils.getCodeRenvoiLabelEdit(
                            (codeRenvoi || {}).codeRenvoi,
                            returnCodesList,
                        )}
                        defaultValue="-"
                        inline
                    />
                </Col>
                <Col xs={2}>
                    <LabelValuePresenter
                        id="date-debut"
                        key={`${member}.${Constants.FIELDS.dateDebut}`}
                        name={`${member}.${Constants.FIELDS.dateDebut}`}
                        value={DateUtils.formatDisplayDate((codeRenvoi || {}).dateDebut)}
                        defaultValue="-"
                        placeholderText={t(`${Constants.TRAD_MODAL}.datePlaceholder`)}
                        inline
                    />
                </Col>
                <Col xs={2}>
                    <Field
                        id="date-fin"
                        key={`${member}.${Constants.FIELDS.dateFin}`}
                        name={`${member}.${Constants.FIELDS.dateFin}`}
                        component={CommonDatePicker}
                        value={DateUtils.formatDisplayDateTime((codeRenvoi || {}).dateFin)}
                        placeholderText={t(`${Constants.TRAD_MODAL}.datePlaceholder`)}
                        formGroupClassName="mb-2"
                    />
                </Col>
            </Row>
        );
    }

    deleteFields(event) {
        const { deleteCodeRenvoiTP } = this.props;

        event.preventDefault();
        deleteCodeRenvoiTP();
    }

    render() {
        const { codeRenvoi } = this.props;

        if ((codeRenvoi || {}).isNewCodeRenvoi) {
            return this.getNewCodeRenvoiTPElement();
        }
        return this.editExistingCodeRenvoiTPElement();
    }
}

CodeRenvoiTPElementComponent.propTypes = {
    t: PropTypes.func,
    deleteCodeRenvoiTP: PropTypes.func,
    member: PropTypes.string,
    domainesTP: PropTypes.arrayOf(PropTypes.shape()),
    reseauxSoinForCombo: PropTypes.arrayOf(PropTypes.shape()),
    returnCodesList: PropTypes.shape(),
    codeRenvoi: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default CodeRenvoiTPElementComponent;
