import React, { Fragment } from 'react';
import { Button, Col, Label, Row } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import get from 'lodash.get';
import Constants from './Constants';
import TranscoDomaineTPElement from './TranscoDomaineTPElement';

function getMappedDomainsTPList(domainList, fields, formValues) {
    const objectsList = Object.values(domainList || [])
        .map((domain) => domain.value)
        .map((item) => ({
            label: `${item.code} - ${item.label}`,
            value: item.code,
        }));

    (fields || []).forEach((member) => {
        const domaine = get(formValues, `${member}.${Constants.COMBOBOX.domaineSource}`);
        const codeDomaine = get(domaine, 'value', domaine);
        const indexDomaine = objectsList.findIndex((item) => item.value === codeDomaine);

        if (indexDomaine >= 0) {
            objectsList.splice(indexDomaine, 1);
        }
    });

    return objectsList;
}

function buildFieldArray(fields, t, change, domainList, formValues) {
    const domainesTP = getMappedDomainsTPList(domainList, fields, formValues);
    const hasParams = fields && fields.length > 0;

    return (
        <Fragment>
            {hasParams ? (
                <Row>
                    <Col xs={5}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {t('transcodageDomaineTP.domaineSourceSaisie')}
                        </Label>
                    </Col>
                    <Col xs={6}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {t('transcodageDomaineTP.domainesCibleSaisie')}
                        </Label>
                    </Col>
                </Row>
            ) : (
                <div className="ml-2">{t('transcodageDomaineTP.noTransco')}</div>
            )}

            {(fields || []).map((member, index) => (
                <TranscoDomaineTPElement
                    id={`${member}.${index}`}
                    key={member}
                    change={change}
                    deleteTransco={() => fields.remove(index)}
                    fullDomainesTP={domainList}
                    domainesTP={domainesTP}
                    member={member}
                    index={index}
                />
            ))}

            <br />
            <Row className="m-0">
                <Button
                    behavior="secondary"
                    outline
                    className="pl-2 pr-2"
                    onClick={(event) => {
                        event.preventDefault();
                        fields.push({});
                    }}
                >
                    <CgIcon behavior="secondary" name="plus-square" size="1x" className="ml-1" />
                    <span className="ml-2">{t('transcodageDomaineTP.addDomainButton')}</span>
                </Button>
            </Row>
        </Fragment>
    );
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const TranscodageUtils = {
    buildFieldArray,
    getMappedDomainsTPList,
};
export default TranscodageUtils;
