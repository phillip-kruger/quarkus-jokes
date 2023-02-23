import { LitElement, html, css} from 'lit';
import { notifier } from 'notifier';
import 'qui-badge';

export class QwcJokesVaadin extends LitElement {
    
    static styles = css`
        :host {
            display: flex;
            gap: 10px;
        }
        a {
            cursor: pointer;
        }
        a:hover {
            color: var(--quarkus-red);
        }
    `;
    
    static properties = {
    };
    
    constructor() {
        super();
    }
    
    render() {
        return html`<a @click=${() => this._info()}>Info</a>
                    <a @click=${() => this._success()}>Success</a>
                    <a @click=${() => this._warning()}>Warning</a>
                    <a @click=${() => this._error()}>Error</a>
                    <br/>
                    <qui-badge small><span>Pending</span></qui-badge>
        <qui-badge level="success" small><span>Confirmed</span></qui-badge>
        <qui-badge level="error" small><span>Denied</span></qui-badge>
        <qui-badge level="contrast" small><span>On hold</span></qui-badge>

        <qui-badge primary><span>Pending</span></qui-badge>
        <qui-badge level="success" primary><span>Confirmed</span></qui-badge>
        <qui-badge level="error" primary><span>Denied</span></qui-badge>
        <qui-badge level="contrast" primary><span>On hold</span></qui-badge>

        <qui-badge pill><span>Pending</span></qui-badge>
        <qui-badge level="success" pill><span>Confirmed</span></qui-badge>
        <qui-badge level="error" pill><span>Denied</span></qui-badge>
        <qui-badge level="contrast" pill><span>On hold</span></qui-badge>

        <qui-badge text="Pending" icon="font-awesome-solid:clock">
          <span>Pending</span>
        </qui-badge>
        <qui-badge text="Confirmed" level="success" icon="font-awesome-solid:check">
          <span>Confirmed</span>
        </qui-badge>
        <qui-badge text="Denied" level="error" icon="font-awesome-solid:circle-exclamation">
          <span>Denied</span>
        </qui-badge>
        <qui-badge text="On hold" level="contrast" icon="font-awesome-solid:hand">
          <span>On hold</span>
        </qui-badge>

        <qui-badge level="success" icon="font-awesome-solid:check"></qui-badge>
        <qui-badge level="error" icon="font-awesome-solid:xmark"></qui-badge>

                    `;
    }


    _info(){
        notifier.showInfoMessage("This is an information message");
    }

    _success(){
        notifier.showSuccessMessage("This is a success message", "bottom-stretch");
    }

    _warning(){
        notifier.showWarningMessage("This is a warning message", "top-end");
    }
    
    _error(){
        notifier.showErrorMessage("This is an error message", "top-stretch");
    }

    

}
customElements.define('qwc-jokes-vaadin', QwcJokesVaadin);
