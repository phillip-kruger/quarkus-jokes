import { LitElement, html, css} from 'lit';
import { JsonRpc } from 'jsonrpc';
import '@vaadin/combo-box';
import '@vaadin/progress-bar';

export class QwcJokesAI extends LitElement {
    jsonRpc = new JsonRpc(this);
    static styles = css`
        :host {
            display: flex;
            justify-content: center;
            padding-top: 100px;
            font-size: x-large;
            flex-direction: column;
            align-items: center;
        }
    `;
    
    static properties = {
        _something: {state: true},
        _anotherthing: {state: true},
        _joke: {state: true},
        _showProgress: {state: true}
    };
    
    constructor() {
        super();
        this._something = null;
        this._anotherthing = null;
        this._joke = null;
        this._showProgress = false;
    }
    
    connectedCallback() {
        super.connectedCallback();
    }

    disconnectedCallback() {
        super.disconnectedCallback()
    }

    render() {
        return html`<div class="input">Tell me a joke about why ${this._renderSomethingCombo()} is better than ${this._renderAnotherthingCombo()}</div>
                    ${this._renderButtonOrProgressBar()}
                    ${this._joke}`;
    }
    
    _renderSomethingCombo(){
        return html`<vaadin-combo-box
            item-label-path="name"
            item-value-path="id"
            .items="${[{ name: 'Quarkus', id: 'quarkus' }, { name: 'Wildfly', id: 'wildfly' }]}"
            @value-changed="${this._onSomethingValueChanged}"
        ></vaadin-combo-box>`;
    }
    
    _renderAnotherthingCombo(){
        return html`<vaadin-combo-box
            item-label-path="name"
            item-value-path="id"
            .items="${[{ name: 'Spring', id: 'spring' }, { name: 'Springboot', id: 'springboot' }, { name: 'Micronaut', id: 'micronaut' }]}"
            @value-changed="${this._onAnotherthingValueChanged}"
        ></vaadin-combo-box>`;
    }
    
    _renderButtonOrProgressBar(){
        if(this._showProgress){
            return html`<vaadin-progress-bar indeterminate></vaadin-progress-bar>`;
        }else {
            return html`<vaadin-button @click=${this._fetchJoke} theme="tertiary"> Go !</vaadin-button>`;
        }
    }
    
    _onSomethingValueChanged(event){
        this._something = event.target.value;
    }
    
    _onAnotherthingValueChanged(event){
        this._anotherthing = event.target.value;
    }
    
    _fetchJoke(){
        if(this._something && this._anotherthing) {
            this._showProgress = true;
            this._joke = null;
            this.jsonRpc.getAIJoke({something: this._something, anotherthing: this._anotherthing}).then(jsonRpcResponse => {
                this._showProgress = false;
                this._joke = jsonRpcResponse.result.jsonResponse;
            });
        }
    }
}
customElements.define('qwc-jokes-ai', QwcJokesAI);
