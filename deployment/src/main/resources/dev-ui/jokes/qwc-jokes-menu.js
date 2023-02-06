import { LitElement, html, css} from 'lit';
import { JsonRpc } from 'jsonrpc';

/**
 * This component shows how to add to the section menu
 */
export class QwcJokesMenu extends LitElement {
    jsonRpc = new JsonRpc("Jokes");
    
    static styles = css`
        :host {
            display: flex;
            padding-top: 50px;
            flex-direction: column;
        }    
    
        .joke {
            text-align: center;
            font-size: 250%;
            border: 5px solid var(--lumo-contrast);
            border-radius: 22px;
            margin-left: 200px;
            margin-right: 200px;
            padding-top: 100px;
            padding-bottom: 100px;
        }
    `;
    
    static properties = {
        _joke: {state:true},
    };
    
    constructor() {
        super();
        this._joke = "Loading ...";
    }
    
    connectedCallback() {
        super.connectedCallback();
        this.jsonRpc.getJoke().then(jsonRpcResponse => {
            this._joke = "\"" + jsonRpcResponse.result.fullJoke + "\"";
        });
    }
    
    disconnectedCallback() {
        super.disconnectedCallback();
    }
    
    render() {
        return html`<div class="joke" @click=${this._refresh}>
                            ${this._joke}
                        </div>`;
        
    }
    
    _refresh(){
        this._joke = "Loading another joke...";
        this.jsonRpc.getJoke().then(jsonRpcResponse => {
            this._joke = "\"" + jsonRpcResponse.result.fullJoke + "\"";
        });
    }
}
customElements.define('qwc-jokes-menu', QwcJokesMenu);