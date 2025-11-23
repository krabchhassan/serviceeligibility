import React from 'react';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import { Button } from '@beyond-framework/common-uitoolkit-beyond';

const renderButtonModal = (functionToCall) => {
    return (
        <Button outlineNoBorder behavior="secondary" size="lg" className="ml-1 px-1 py-1">
            <CgIcon size="1x" name="open-modal" fixedWidth onClick={functionToCall} />
        </Button>
    );
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const PrestationServiceUtils = {
    renderButtonModal,
};

export default PrestationServiceUtils;
