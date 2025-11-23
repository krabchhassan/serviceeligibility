/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Panel,
    PanelHeader,
    PanelSection,
    DropZone,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import FooterImport from '../../FooterImport';

/* ************************************* */
/* ********       VARIABLES       ******** */
/* ************************************* */
const propTypes = {
    importLotMappingFile: PropTypes.func,
    displayAlert: PropTypes.func,
    t: PropTypes.func,
};

const defaultProps = {};

const ALLOWED_IMPORT_FORMATS = ['.xlsx'];

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['bobb', 'errors'], { wait: true })
class ImportLotPanelComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            file: null,
        };
    }

    @autobind
    onDropRejected(file, errorName) {
        const { t, displayAlert } = this.props;

        const message = t('errors:dropzone.error', {
            name: file.name,
            error: t(`errors:dropzone.${errorName}`),
        });
        displayAlert(message, 'danger');
    }

    @autobind
    onDrop(file) {
        this.setState({ file });
    }

    onCancel() {
        const { closeImportPanel } = this.props;
        this.clearDropzone();
        closeImportPanel();
    }

    @autobind
    getReferenceForDropzone(originalDropzone) {
        this.dropzoneRef = originalDropzone;
    }

    @autobind
    handleImport() {
        const { t, importLotMappingFile, closeImportPanel, setFileName, displayAlert } = this.props;
        const { file } = this.state;
        const fileName = file.name;
        setFileName(fileName);
        displayAlert(
            t('bobb:import.spinnerMessage', {
                fileNameToImport: fileName,
            }),
            'secondary',
        );
        importLotMappingFile(file);
        closeImportPanel();
    }

    clearDropzone() {
        this.setState({ file: null });
        this.dropzoneRef.setState({ filename: undefined });
    }

    render() {
        const { t, isImportLotPending } = this.props;
        const { file } = this.state;
        return (
            <Panel
                header={<PanelHeader title={t('import.title')} />}
                footer={
                    <FooterImport
                        disabled={!file || isImportLotPending}
                        onCancel={() => this.onCancel()}
                        onImport={this.handleImport}
                    />
                }
                border={false}
            >
                <PanelSection>
                    <div className="mb-2">{t('bobb:import.content')}</div>
                    <DropZone
                        onDrop={this.onDrop}
                        onDropRejected={this.onDropRejected}
                        label={file ? file.name : t('bobb:import.file')}
                        mimeTypeList={ALLOWED_IMPORT_FORMATS}
                        ref={this.getReferenceForDropzone}
                        onClear={() => this.clearDropzone()}
                    />
                </PanelSection>
            </Panel>
        );
    }
}

ImportLotPanelComponent.propTypes = propTypes;
ImportLotPanelComponent.propTypes = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ImportLotPanelComponent;
