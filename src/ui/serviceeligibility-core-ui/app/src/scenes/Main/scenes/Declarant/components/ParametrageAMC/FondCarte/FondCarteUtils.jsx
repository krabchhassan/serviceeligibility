import React, { Fragment } from 'react';
import { Button, Col, Label, Row } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import FondCarteElement from './FondCarteElement';
import ParametrageAMCUtils from '../ParametrageAMCUtils';

const addSansReseauOption = (reseauxSoinList) => {
    reseauxSoinList.push({
        label: 'Sans réseau de soin',
        value: 'Sans réseau de soin',
    });

    return reseauxSoinList;
};

function buildFieldArray(fields, t, reseauxSoin, fondCarte, isCopy) {
    const reseauxSoinForCombo = addSansReseauOption(ParametrageAMCUtils.getMappedForComboboxList(reseauxSoin));
    const hasParams = fields && fields.length > 0;
    if (isCopy) {
        (fields || []).getAll()?.forEach((member, index) => {
            if (!member.isNewFondCarte) {
                fields.remove(index);
            }
        });
    }

    return (
        <Fragment>
            {hasParams ? (
                <Row>
                    <Col xs={3}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {`${t('declarant.parametrageAMC.reseauSoin')} *`}
                        </Label>
                    </Col>
                    <Col xs={4}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {`${t('declarant.parametrageAMC.fondCarte')} *`}
                        </Label>
                    </Col>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {`${t('declarant.parametrageAMC.dateDebut')} *`}
                        </Label>
                    </Col>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {t('declarant.parametrageAMC.dateFin')}
                        </Label>
                    </Col>
                </Row>
            ) : (
                <div className="ml-2">{t('declarant.parametrageAMC.noFondCarte')}</div>
            )}

            {(fields || []).map((member, index) => (
                <FondCarteElement
                    id={`${member}.${index}`}
                    key={member}
                    deleteFondCarte={() => fields.remove(index)}
                    reseauxSoinForCombo={reseauxSoinForCombo}
                    member={member}
                    fondCarte={(fondCarte || [])[index]}
                />
            ))}

            <Row className="m-0">
                <Button
                    id="fondCarteButton"
                    behavior="secondary"
                    outline
                    className="pl-2 pr-2"
                    onClick={(event) => {
                        event.preventDefault();
                        fields.push({ isNewFondCarte: true });
                    }}
                >
                    <CgIcon behavior="secondary" name="plus-square" size="1x" className="ml-1" />
                    <span className="ml-2">{t('declarant.parametrageAMC.addFondCarteButton')}</span>
                </Button>
            </Row>
        </Fragment>
    );
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const FondCarteUtils = {
    buildFieldArray,
};
export default FondCarteUtils;
