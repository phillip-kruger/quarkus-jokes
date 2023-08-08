import { LitElement, html, css} from 'lit';
import { JsonRpc } from 'jsonrpc';
import '@vaadin/grid';
import '@vaadin/icon';

export class QwcJokesCookies extends LitElement {
    
    jsonRpc = new JsonRpc(this);
    
    static styles = css`
        a {
            color: var(--lumo-contrast);
            padding: 15px 2px 5px;
            cursor: pointer;
            text-decoration: none;
        }
        .table {
            height: 100%;
            padding-bottom: 10px;
        }
    `;
    
    static properties = {
        _cookies: {state: true}
    };
    
    constructor() {
        super();
        this._cookies = null;
    }

    connectedCallback() {
        super.connectedCallback();
        this.jsonRpc.getCookies().then(jsonRpcResponse => {
            this._cookies = jsonRpcResponse.result;
        });
    }

    disconnectedCallback() {
        super.disconnectedCallback()
    }

    render() {
       
        if(this._cookies){
            return html`${this._renderReloadIcon()}
                <vaadin-grid .items="${this._cookies}" class="table" theme="no-border">
                    <vaadin-grid-column auto-width
                        header="Name"
                        path="name"
                        resizable>
                    </vaadin-grid-column>

                    <vaadin-grid-column auto-width
                        header="Value"
                        path="value"
                        resizable>
                    </vaadin-grid-column>
                    
                    <vaadin-grid-column auto-width
                        header="Max Age"
                        path="maxAge" 
                        resizable>
                    </vaadin-grid-column>
            
                    <vaadin-grid-column auto-width
                        header="Http Only"
                        path="httpOnly" 
                        resizable>
                    </vaadin-grid-column>
            
                    <vaadin-grid-column auto-width
                        header="Secure"
                        path="secure" 
                        resizable>
                    </vaadin-grid-column>
            
                </vaadin-grid>
                `;
        }else{
            this._renderReloadIcon();
        }
        
    }

    _renderReloadIcon(){
        let callback = window.location.pathname;
        return html`<a href="/q/jokes-endpoint?callback=${callback}" router-ignore>
                        <vaadin-icon icon="font-awesome-solid:arrow-rotate-right"></vaadin-icon>
                        Reload
                    </a>`;
    }

}
customElements.define('qwc-jokes-cookies', QwcJokesCookies);