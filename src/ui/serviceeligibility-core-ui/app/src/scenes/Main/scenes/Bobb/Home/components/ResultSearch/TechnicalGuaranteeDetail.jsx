/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { reduxForm } from 'redux-form';
import autobind from 'autobind-decorator';
import { Row, Col, CgdTable, TranslationsSubscriber, Input, Label } from '@beyond-framework/common-uitoolkit-beyond';
import StringUtils from '../../../../../../../common/utils/StringUtils';
import formConstants from '../Constants';
/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['common', 'bobb'], { wait: true })
class TechnicalGuaranteeDetail extends Component {
    constructor(props) {
        super(props);
        this.state = {
            startDownload: false,
            checked: props.displayDeletedGT,
        };
    }

    getColumns() {
        return [
            this.generateCommonColumn('codeAssureur', 75),
            this.generateCommonColumn('codeGarantie', 75),
            this.generateCommonColumn('dateAjout', 75),
            this.generateCommonColumn('dateSuppressionLogique', 75),
        ];
    }

    @autobind
    setChecked() {
        const { checked } = this.state;
        this.setState({ checked: !checked });
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`technicalGuaranteeDetail.${key}`), false),
            accessor: key,
            id: key,
            width,
            key,
        };
    }

    @autobind
    toggleFile() {
        const { startDownload } = this.state;
        this.setState({ startDownload: !startDownload });
    }

    render() {
        const { t, technicalGuarantee } = this.props;
        const { checked } = this.state;
        let garantieTechniques = technicalGuarantee && technicalGuarantee.garantieTechniques;
        if (!checked) {
            garantieTechniques =
                technicalGuarantee &&
                (technicalGuarantee.garantieTechniques || []).filter(
                    (gt) => !gt.dateSuppressionLogique || gt.dateSuppressionLogique.length === 0,
                );
        }

        return (
            <div>
                <Input
                    name={formConstants.FIELDS_SEARCH.gTSupprimees}
                    type="checkbox"
                    id="gTSupprimeesInDetail"
                    onChange={this.setChecked}
                    checked={checked}
                />
                <Label check for="gTSupprimeesInDetail">
                    {t('technicalGuaranteeDetail.chipsGTSupprimees')}
                </Label>
                <br />
                <Fragment>
                    <Row>
                        <Col className="ml-3 pl-3 mr-3 mb-1">
                            <Row>
                                <CgdTable
                                    id="periodesAssureTable"
                                    data={garantieTechniques || []}
                                    columns={this.getColumns()}
                                    showPagination={(garantieTechniques || []).length > 10}
                                />
                            </Row>
                        </Col>
                    </Row>
                </Fragment>
            </div>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    technicalGuarantee: PropTypes.shape(),
};
// Default props
const defaultProps = {};

// Add prop types
TechnicalGuaranteeDetail.propTypes = propTypes;
// Add default props
TechnicalGuaranteeDetail.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_DETAILS,
})(TechnicalGuaranteeDetail);
