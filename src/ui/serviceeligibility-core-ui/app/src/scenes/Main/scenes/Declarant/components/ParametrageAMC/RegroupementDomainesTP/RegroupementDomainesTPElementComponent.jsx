/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import { Field } from 'redux-form';
import PropTypes from 'prop-types';
import { Col, Row, Button, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import Constants from './Constants';
import { CommonCombobox, CommonDatePicker, CommonInput } from '../../../../../../../common/utils/Form/CommonFields';
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
class RegroupementDomainesTPElementComponent extends Component {
    getNewRegroupementDomainesTPElement() {
        const { t, member, domainesTP } = this.props;

        return (
            <Row>
                <Col xs={2}>
                    <Field
                        id="domaine-regroupement-tp"
                        key={`${member}.${Constants.FIELDS.domaineRegroupementTP}`}
                        name={`${member}.${Constants.FIELDS.domaineRegroupementTP}`}
                        component={CommonCombobox}
                        options={domainesTP}
                        placeholder={t(`${Constants.TRAD_MODAL}.domaineRegroupementTPPlaceholder`)}
                        formGroupClassName="mb-2"
                        searchable
                        clearable
                        validate={[required]}
                    />
                </Col>
                <Col xs={3}>
                    <Field
                        id="codes-domaines-TP"
                        key={`${member}.${Constants.FIELDS.codesDomainesTP}`}
                        name={`${member}.${Constants.FIELDS.codesDomainesTP}`}
                        component={CommonCombobox}
                        options={domainesTP}
                        placeholder={t(`${Constants.TRAD_MODAL}.codesDomainesTPPlaceholder`)}
                        searchable
                        clearable
                        multi
                        formGroupClassName="mb-2"
                        validate={[required]}
                    />
                </Col>
                <Col xs={1} />
                <Col xs={1}>
                    <Field
                        id={`${member}.${Constants.FIELDS.niveauRemboursementIdentique}`}
                        key={`${member}.${Constants.FIELDS.niveauRemboursementIdentique}`}
                        name={`${member}.${Constants.FIELDS.niveauRemboursementIdentique}`}
                        component={CommonInput}
                        formGroupClassName="mb-2 ml-0 pl-0"
                        value="true"
                        label=" "
                        type="checkbox"
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

    getCodesDomainesList(codesDomainesTP) {
        const { domainesTP } = this.props;
        const codeLabels = Object.values(codesDomainesTP || []).map((codeDomaine) =>
            ParametrageAMCUtils.getElementLabelForEdit(codeDomaine, domainesTP),
        );
        return codeLabels.join(', ');
    }

    editExistingRegroupementDomainesTPElement() {
        const { t, member, regroupementDomaines, domainesTP } = this.props;

        return (
            <Row>
                <Col xs={2}>
                    <LabelValuePresenter
                        id="domaine-regroupement-tp"
                        key={`${member}.${Constants.FIELDS.domaineRegroupementTP}`}
                        name={`${member}.${Constants.FIELDS.domaineRegroupementTP}`}
                        value={ParametrageAMCUtils.getElementLabelForEdit(
                            (regroupementDomaines || {}).domaineRegroupementTP,
                            domainesTP,
                        )}
                        defaultValue="-"
                        inline
                    />
                </Col>
                <Col xs={3}>
                    <LabelValuePresenter
                        id="codes-domaines-TP"
                        key={`${member}.${Constants.FIELDS.codesDomainesTP}`}
                        name={`${member}.${Constants.FIELDS.codesDomainesTP}`}
                        value={this.getCodesDomainesList((regroupementDomaines || {}).codesDomainesTP || [])}
                        defaultValue="-"
                        inline
                    />
                </Col>
                <Col xs={1} />
                <Col xs={1} className="pl-4">
                    <CgIcon name={(regroupementDomaines || {}).niveauRemboursementIdentique ? 'validate' : 'cancel'} />
                </Col>
                <Col xs={2}>
                    <LabelValuePresenter
                        id="date-debut"
                        key={`${member}.${Constants.FIELDS.dateDebut}`}
                        name={`${member}.${Constants.FIELDS.dateDebut}`}
                        value={DateUtils.formatDisplayDate((regroupementDomaines || {}).dateDebut)}
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
                        value={DateUtils.formatDisplayDateTime((regroupementDomaines || {}).dateFin)}
                        placeholderText={t(`${Constants.TRAD_MODAL}.datePlaceholder`)}
                        formGroupClassName="mb-2"
                    />
                </Col>
            </Row>
        );
    }

    deleteFields(event) {
        const { deleteRegroupementDomainesTP } = this.props;

        event.preventDefault();
        deleteRegroupementDomainesTP();
    }

    render() {
        const { regroupementDomaines } = this.props;

        if ((regroupementDomaines || {}).isNewRegroupementDomaines) {
            return this.getNewRegroupementDomainesTPElement();
        }
        return this.editExistingRegroupementDomainesTPElement();
    }
}

RegroupementDomainesTPElementComponent.propTypes = {
    t: PropTypes.func,
    deleteRegroupementDomainesTP: PropTypes.func,
    member: PropTypes.string,
    domainesTP: PropTypes.arrayOf(PropTypes.shape()),
    regroupementDomaines: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default RegroupementDomainesTPElementComponent;
