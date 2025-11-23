/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import { Field } from 'redux-form';
import PropTypes from 'prop-types';
import { Button, Col, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import Constants from './Constants';
import { CommonCombobox, CommonMultiCombobox } from '../../../../../../common/utils/Form/CommonFields';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
const mapDomainsForCombobox = (list) =>
    (list || []).map((item) => ({
        label: `${item.code} - ${item.label}`,
        value: item.code,
    }));

const formatDomaineTPList = (fullDomainesTP) => {
    const fullList = Object.values(fullDomainesTP || []).map((item) => item.value);
    const domaineCibleList = mapDomainsForCombobox(fullList);
    return domaineCibleList;
};

@TranslationsSubscriber(['eligibility', 'common'])
class TranscoDomaineTPElementComponent extends Component {
    componentDidUpdate(prevProps) {
        const { fullDomainesTP, change, data, member } = this.props;

        const gotDomainesTP = prevProps.fullDomainesTP !== fullDomainesTP && prevProps.fullDomainesTP !== [];

        if (gotDomainesTP) {
            const fullList = formatDomaineTPList(fullDomainesTP);
            const domainObject = fullList.find((item) => item.value === data.domaineSource);

            change(`${member}.${Constants.COMBOBOX.domaineSource}`, domainObject);
        }
    }

    deleteFields(event) {
        const { deleteTransco } = this.props;

        event.preventDefault();
        deleteTransco();
    }

    render() {
        const { t, member, domainesTP, fullDomainesTP } = this.props;
        const domaineCibleList = formatDomaineTPList(fullDomainesTP);

        return (
            <Row>
                <Col xs={5}>
                    <Field
                        id="search-domain-to-transcode"
                        key={`${member}.${Constants.COMBOBOX.domaineSource}`}
                        name={`${member}.${Constants.COMBOBOX.domaineSource}`}
                        component={CommonCombobox}
                        options={domainesTP}
                        placeholder={t(`${Constants.TRAD_MODAL}.combobox.domainPlaceholder`)}
                        searchable
                        clearable
                        formGroupClassName="mb-2"
                        validate={[required]}
                    />
                </Col>
                <Col xs={6}>
                    <Field
                        id="search-domain-transcoded"
                        key={`${member}.domainesCible`}
                        name={`${member}.domainesCible`}
                        component={CommonMultiCombobox}
                        options={domaineCibleList}
                        placeholder="Domaine TP transcodé"
                        formGroupClassName="mb-2"
                        searchable
                        clearable
                        validate={[required]}
                        multi
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
}

TranscoDomaineTPElementComponent.propTypes = {
    t: PropTypes.func,
    data: PropTypes.shape(),
    change: PropTypes.func,
    member: PropTypes.string,
    fullDomainesTP: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TranscoDomaineTPElementComponent;
