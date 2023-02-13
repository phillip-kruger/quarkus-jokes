import { LitElement, html, css} from 'lit';
import { pages } from 'jokes-data';
import { JsonRpc } from 'jsonrpc';

export class QwcJokesCard extends LitElement {
    jsonRpc = new JsonRpc("Jokes");
    
    static styles = css`
        .description {
            display: none;
        }
        .extensionLink {
            color: var(--lumo-contrast);
            font-size: small;
            padding: 15px 2px 5px;
            cursor: pointer;
            text-decoration: none;
            text-align: right;
        }
        .extensionLink:hover {
            filter: brightness(80%);
        }
    `;
    
    static properties = {
        description: {type: String},
        _lastPage: {state: false},
        _joke: {state:true},
    };
    
    constructor() {
        super();
        this._lastPage = pages.at(-1);
        this._joke = "Loading joke...";
    }
    
    connectedCallback() {
        super.connectedCallback();
        this.jsonRpc.getJoke().then(jsonRpcResponse => {
            this._joke = jsonRpcResponse.result.fullJoke;
        });
    }
    
    render() {
        
        return html`<span class="description">${this.description}</span>
            <div class="joke" @click=${this._refresh}>${this._joke}</div>
            <a class="extensionLink" href="${this._lastPage.id}">
                More jokes
            </a>
        `;
    }
    
    _refresh(){
        this._joke = "Loading another joke...";
        this.jsonRpc.getJoke().then(jsonRpcResponse => {
            this._joke = jsonRpcResponse.result.fullJoke;
        });
    }
}
customElements.define('qwc-jokes-card', QwcJokesCard);