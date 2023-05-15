import { LitElement, html, css} from 'lit';
import { notifier } from 'notifier';
import 'qui-badge';
import 'qui-alert';
import 'qui-card';

export class QwcJokesVaadin extends LitElement {
    
    static styles = css`
        :host {
            display: flex;
            gap: 10px;
        }

        .main{
            display: flex;
            flex-direction: column;
            width: 100%;
            padding-right: 10px;
            padding-left: 10px;
        }

        .notification{
            display: flex;
            flex-direction: column;
        }

        a {
            cursor: pointer;
            color: var(--lumo-body-text-color);
        }
        a:link { 
            text-decoration: none;
            color: var(--lumo-body-text-color); 
        }
        a:visited { 
            text-decoration: none;
            color: var(--lumo-body-text-color); 
        }
        a:active { 
            text-decoration: none; 
            color: var(--lumo-body-text-color);
        }
        a:hover {
            color: var(--quarkus-red);
        }

        .badges {
            display: flex;
            flex-direction: column;
        }
        .cards {
            height: 100%;
            padding-right: 10px;
            display: flex;
            gap: 10px;
            width: 100%;
            justify-content: space-around;
        }
        
        qui-card {
            height: 100%;
            width: 100%;
        }
    
        h4 {
            padding: 5px;
            background-color: var(--lumo-contrast-5pct);
            border-bottom: 1px solid var(--lumo-contrast-10pct);
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
            margin: unset;
        }

        .cardcontents {
            padding-left: 10px;
            display: flex;
            flex-direction: column;
            width: 100%;
            gap: 5px;
            padding-bottom: 5px;
            padding-top: 5px;
        }

        .line {
            border: 1px solid var(--lumo-contrast-20pct);
            width: 100%;
            margin-top: 30px;
        }
    `;
    
    static properties = {
    };
    
    constructor() {
        super();
    }
    
    render() {
        return html`<div class="main">
            <h2> Some examples </h2>
            <div class="notification">
                <h3>Notifier</h3>
                <p>Notification can be used to inform the user of something. 
                    See <a href="https://vaadin.com/docs/latest/components/notification/#position">https://vaadin.com/docs/latest/components/notification/#position</a> for more info.

                </p>

                <div class="cards">
                    <qui-card title="Default">
                        <div slot="content">
                            <div class="cardcontents">
                                <a @click=${() => this._info()}>Info</a>
                                <a @click=${() => this._success()}>Success</a>
                                <a @click=${() => this._warning()}>Warning</a>
                                <a @click=${() => this._error()}>Error</a>
                                <a @click=${() => this._primaryInfo()}>Primary Info</a>
                                <a @click=${() => this._primarySuccess()}>Primary Success</a>
                                <a @click=${() => this._primaryWarning()}>Primary Warning</a>
                                <a @click=${() => this._primaryError()}>Primary Error</a>
                            </div>    
                        </div>
                    </qui-card>
                    <qui-card title="bottom-stretch">
                        <div slot="content">
                            <div class="cardcontents">
                                <a @click=${() => this._info("bottom-stretch")}>Info</a>
                                <a @click=${() => this._success("bottom-stretch")}>Success</a>
                                <a @click=${() => this._warning("bottom-stretch")}>Warning</a>
                                <a @click=${() => this._error("bottom-stretch")}>Error</a>
                                <a @click=${() => this._primaryInfo("bottom-stretch")}>Primary Info</a>
                                <a @click=${() => this._primarySuccess("bottom-stretch")}>Primary Success</a>
                                <a @click=${() => this._primaryWarning("bottom-stretch")}>Primary Warning</a>
                                <a @click=${() => this._primaryError("bottom-stretch")}>Primary Error</a>
                            </div>
                        </div>    
                    </qui-card>
                    <qui-card title="bottom-end">
                        <div slot="content">
                            <div class="cardcontents">
                                <a @click=${() => this._info("bottom-end")}>Info</a>
                                <a @click=${() => this._success("bottom-end")}>Success</a>
                                <a @click=${() => this._warning("bottom-end")}>Warning</a>
                                <a @click=${() => this._error("bottom-end")}>Error</a>
                                <a @click=${() => this._primaryInfo("bottom-end")}>Primary Info</a>
                                <a @click=${() => this._primarySuccess("bottom-end")}>Primary Success</a>
                                <a @click=${() => this._primaryWarning("bottom-end")}>Primary Warning</a>
                                <a @click=${() => this._primaryError("bottom-end")}>Primary Error</a>
                            </div>    
                        </div>    
                    </qui-card>
                    <qui-card title="top-end">
                        <div slot="content">
                            <div class="cardcontents">
                                <a @click=${() => this._info("top-end")}>Info</a>
                                <a @click=${() => this._success("top-end")}>Success</a>
                                <a @click=${() => this._warning("top-end")}>Warning</a>
                                <a @click=${() => this._error("top-end")}>Error</a>
                                <a @click=${() => this._primaryInfo("top-end")}>Primary Info</a>
                                <a @click=${() => this._primarySuccess("top-end")}>Primary Success</a>
                                <a @click=${() => this._primaryWarning("top-end")}>Primary Warning</a>
                                <a @click=${() => this._primaryError("top-end")}>Primary Error</a>
                            </div>
                        </div>
                    </qui-card>
                    <qui-card title="top-stretch">
                        <div slot="content">
                            <div class="cardcontents">
                                <a @click=${() => this._info("top-stretch")}>Info</a>
                                <a @click=${() => this._success("top-stretch")}>Success</a>
                                <a @click=${() => this._warning("top-stretch")}>Warning</a>
                                <a @click=${() => this._error("top-stretch")}>Error</a>
                                <a @click=${() => this._primaryInfo("top-stretch")}>Primary Info</a>
                                <a @click=${() => this._primarySuccess("top-stretch")}>Primary Success</a>
                                <a @click=${() => this._primaryWarning("top-stretch")}>Primary Warning</a>
                                <a @click=${() => this._primaryError("top-stretch")}>Primary Error</a>
                            </div>
                        </div>
                    </qui-card>
                    <qui-card title="top-start">
                        <div slot="content">
                            <div class="cardcontents">
                                <a @click=${() => this._info("top-start")}>Info</a>
                                <a @click=${() => this._success("top-start")}>Success</a>
                                <a @click=${() => this._warning("top-start")}>Warning</a>
                                <a @click=${() => this._error("top-start")}>Error</a>
                                <a @click=${() => this._primaryInfo("top-start")}>Primary Info</a>
                                <a @click=${() => this._primarySuccess("top-start")}>Primary Success</a>
                                <a @click=${() => this._primaryWarning("top-start")}>Primary Warning</a>
                                <a @click=${() => this._primaryError("top-start")}>Primary Error</a>
                            </div>
                        </div>
                    </qui-card>
                    <qui-card title="middle">
                        <div slot="content">
                            <div class="cardcontents">
                                <a @click=${() => this._info("middle")}>Info</a>
                                <a @click=${() => this._success("middle")}>Success</a>
                                <a @click=${() => this._warning("middle")}>Warning</a>
                                <a @click=${() => this._error("middle")}>Error</a>
                                <a @click=${() => this._primaryInfo("middle")}>Primary Info</a>
                                <a @click=${() => this._primarySuccess("middle")}>Primary Success</a>
                                <a @click=${() => this._primaryWarning("middle")}>Primary Warning</a>
                                <a @click=${() => this._primaryError("middle")}>Primary Error</a>
                            </div>
                        </div>
                    </qui-card>
                </div>
            </div>

            <hr class="line">

            <div class="badges">
                <h3>Badges</h3>
                <p>Badges wrap the vaadin theme in a component.
                    See <a href="https://vaadin.com/docs/latest/components/badge">https://vaadin.com/docs/latest/components/badge</a> for more info.
                </p>
                <div class="cards">
                    <qui-card title="Small">
                        <div slot="content">
                            <div class="cardcontents">
                                <qui-badge small><span>Default</span></qui-badge>
                                <qui-badge level="success" small><span>Success</span></qui-badge>
                                <qui-badge level="warning" small><span>Warning</span></qui-badge>
                                <qui-badge level="error" small><span>Error</span></qui-badge>
                                <qui-badge level="contrast" small><span>Contrast</span></qui-badge>
                                <qui-badge background="pink" color="purple" small><span>Custom colors</span></qui-badge>
                            </div>
                        </div>
                    </qui-card> 
                    <qui-card title="Primary">
                        <div slot="content">
                            <div class="cardcontents">
                                <qui-badge primary><span>Default primary</span></qui-badge>
                                <qui-badge level="success" primary><span>Success primary</span></qui-badge>
                                <qui-badge level="warning" primary><span>Warning primary</span></qui-badge>
                                <qui-badge level="error" primary><span>Error primary</span></qui-badge>
                                <qui-badge level="contrast" primary><span>Contrast primary</span></qui-badge>
                                <qui-badge background="pink" color="purple" primary><span>Custom colors</span></qui-badge>
                            </div>
                        </div>
                    </qui-card>
                    <qui-card title="Pill">
                        <div slot="content">
                            <div class="cardcontents">
                                <qui-badge pill><span>Default pill</span></qui-badge>
                                <qui-badge level="success" pill><span>Success pill</span></qui-badge>
                                <qui-badge level="warning" pill><span>Warning pill</span></qui-badge>
                                <qui-badge level="error" pill><span>Error pill</span></qui-badge>
                                <qui-badge level="contrast" pill><span>Contrast pill</span></qui-badge>
                                <qui-badge background="pink" color="purple" pill><span>Custom colors</span></qui-badge>
                            </div>
                        </div>
                    </qui-card>
                    <qui-card title="With icon">
                        <div slot="content">            
                            <div class="cardcontents">
                                <qui-badge text="Default" icon="font-awesome-solid:circle-info">
                                    <span>Default icon</span>
                                </qui-badge>
                                <qui-badge text="Success" level="success" icon="font-awesome-solid:check">
                                    <span>Success icon</span>
                                </qui-badge>
                                <qui-badge text="Warning" level="warning" icon="font-awesome-solid:triangle-exclamation">
                                    <span>Warning icon</span>
                                </qui-badge>
                                <qui-badge text="Error" level="error" icon="font-awesome-solid:circle-exclamation">
                                    <span>Error icon</span>
                                </qui-badge>
                                <qui-badge text="Contrast" level="contrast" icon="font-awesome-solid:hand">
                                    <span>Contrast icon</span>
                                </qui-badge>
                                <qui-badge text="Custom" background="pink" color="purple" icon="font-awesome-brands:redhat">
                                    <span>Custom colors</span>
                                </qui-badge>
                            </div>
                        </div>
                    </qui-card>
                    <qui-card title="Icon only">
                        <div slot="content">
                            <div class="cardcontents">
                                <qui-badge icon="font-awesome-solid:clock"></qui-badge>
                                <qui-badge level="success" icon="font-awesome-solid:check"></qui-badge>
                                <qui-badge level="warning" icon="font-awesome-solid:triangle-exclamation"></qui-badge>
                                <qui-badge level="error" icon="font-awesome-solid:circle-exclamation"></qui-badge>
                                <qui-badge level="contrast" icon="font-awesome-solid:hand"></qui-badge>
                                <qui-badge level="contrast" background="pink" color="purple" icon="font-awesome-brands:redhat"></qui-badge>
                            </div>
                        </div>
                    </qui-card>
                    <qui-card title="Clickable">
                        <div slot="content">
                            <div class="cardcontents">
                                <qui-badge clickable @click=${() => this._info()}><span>Default</span></qui-badge>
                                <qui-badge clickable level="success" @click=${() => this._success()}><span>Success</span></qui-badge>
                                <qui-badge clickable level="warning" @click=${() => this._warning()}><span>Warning</span></qui-badge>
                                <qui-badge clickable level="error" @click=${() => this._error()}><span>Error</span></qui-badge>
                                <qui-badge clickable level="contrast" @click=${() => this._contrast()}><span>Contrast</span></qui-badge>
                                <qui-badge clickable background="pink" color="purple" @click=${() => this._info()}><span>Custom colors</span></qui-badge>
                            </div>
                        </div>
                    </qui-card>
                </div>
            </div>

            <hr class="line">

            <div class="badges">
                <h3>Alerts</h3>
                <p>Alerts is modeled around the Bootstrap alters
                    See <a href="https://getbootstrap.com/docs/4.0/components/alerts/">https://getbootstrap.com/docs/4.0/components/alerts/</a> for more info.
                </p>
                <div class="cards">

                        <div class="cardcontents">
                            <qui-alert><span>Info alert</span></qui-alert>
                            <qui-alert level="success"><span>Success alert</span></qui-alert>
                            <qui-alert level="warning"><span>Warning alert</span></qui-alert>
                            <qui-alert level="error"><span>Error alert</span></qui-alert>
                            <hr class="line">
                            <qui-alert permanent><span>Permanent Info alert</span></qui-alert>
                            <qui-alert level="success" permanent><span>Permanent Success alert</span></qui-alert>
                            <qui-alert level="warning" permanent><span>Permanent Warning alert</span></qui-alert>
                            <qui-alert level="error" permanent><span>Permanent Error alert</span></qui-alert>
                            <hr class="line">
                            <qui-alert center><span>Center Info alert</span></qui-alert>
                            <qui-alert level="success" center><span>Center Success alert</span></qui-alert>
                            <qui-alert level="warning" center><span>Center Warning alert</span></qui-alert>
                            <qui-alert level="error" center><span>Center Error alert</span></qui-alert>
                            <hr class="line">
                            <qui-alert showIcon><span>Info alert with icon</span></qui-alert>
                            <qui-alert level="success" showIcon><span>Success alert with icon</span></qui-alert>
                            <qui-alert level="warning" showIcon><span>Warning alert with icon</span></qui-alert>
                            <qui-alert level="error" showIcon><span>Error alert with icon</span></qui-alert>
                            <hr class="line">
                            <qui-alert icon="font-awesome-brands:redhat"><span>Info alert with custom icon</span></qui-alert>
                            <qui-alert level="success" icon="font-awesome-brands:redhat"><span>Success alert with custom icon</span></qui-alert>
                            <qui-alert level="warning" icon="font-awesome-brands:redhat"><span>Warning alert with custom icon</span></qui-alert>
                            <qui-alert level="error" icon="font-awesome-brands:redhat"><span>Error alert with custom icon</span></qui-alert>
                            <hr class="line">
                            <qui-alert size="small" showIcon><span>Small Info alert with icon</span></qui-alert>
                            <qui-alert level="success" size="small" showIcon><span>Small Success alert with icon</span></qui-alert>
                            <qui-alert level="warning" size="small" showIcon><span>Small Warning alert with icon</span></qui-alert>
                            <qui-alert level="error" size="small" showIcon><span>Small Error alert with icon</span></qui-alert>
                            <hr class="line">
                            <qui-alert showIcon><span>Info <code>alert</code> with markup <br><a href="https://quarkus.io/" target="_blank">quarkus.io</a></span></qui-alert>
                            <qui-alert level="success" showIcon><span>Success <code>alert</code> with markup <br><a href="https://quarkus.io/" target="_blank">quarkus.io</a></span></qui-alert>
                            <qui-alert level="warning" showIcon><span>Warning <code>alert</code> with markup <br><a href="https://quarkus.io/" target="_blank">quarkus.io</a></span></qui-alert>
                            <qui-alert level="error" showIcon><span>Error <code>alert</code> with markup <br><a href="https://quarkus.io/" target="_blank">quarkus.io</a></span></qui-alert>
                            <hr class="line">
                            <qui-alert showIcon primary><span>Primary Info alert with icon</span></qui-alert>
                            <qui-alert level="success" showIcon primary><span>Primary Success alert with icon</span></qui-alert>
                            <qui-alert level="warning" showIcon primary><span>Primary Warning alert with icon</span></qui-alert>
                            <qui-alert level="error" showIcon primary><span>Primary Error alert with icon</span></qui-alert>
                            <hr class="line">
                            <qui-alert title="Information"><span>Info alert with title</span></qui-alert>
                            <qui-alert title="Well done" level="success"><span>Success alert with title</span></qui-alert>
                            <qui-alert title="Beware" level="warning"><span>Warning alert with title</span></qui-alert>
                            <qui-alert title="Ka-boom" level="error"><span>Error alert with title</span></qui-alert>
                            <hr class="line">
                            <qui-alert title="Information" showIcon><span>Info alert with title and icon</span></qui-alert>
                            <qui-alert title="Well done" level="success" showIcon><span>Success alert with title and icon</span></qui-alert>
                            <qui-alert title="Beware" level="warning" showIcon><span>Warning alert with title and icon</span></qui-alert>
                            <qui-alert title="Ka-boom" level="error" showIcon><span>Error alert with title and icon</span></qui-alert>

                        </div>
                    </div>
                </div>
            </div>

        </div>`;
    }

    _info(position = null){
        notifier.showInfoMessage("This is an information message", position);
    }

    _success(position = null){
        notifier.showSuccessMessage("This is a success message", position);
    }

    _warning(position = null){
        notifier.showWarningMessage("This is a warning message", position);
    }
    
    _error(position = null){
        notifier.showErrorMessage("This is an error message", position);
    }

    _primaryInfo(position = null){
        notifier.showPrimaryInfoMessage("This is a primary INFO message", position);
    }

    _primarySuccess(position = null){
        notifier.showPrimarySuccessMessage("This is a primary SUCCESS message", position);
    }

    _primaryWarning(position = null){
        notifier.showPrimaryWarningMessage("This is a primary WARNING message", position);
    }

    _primaryError(position = null){
        notifier.showPrimaryErrorMessage("This is a primary ERROR message", position);
    }
}
customElements.define('qwc-jokes-vaadin', QwcJokesVaadin);
