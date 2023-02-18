import { LitElement, html, css} from 'lit';
import { jokes } from 'jokes-data';
import { JsonRpc } from 'jsonrpc';

export class QwcJokesWebComponents extends LitElement {
    
    jsonRpc = new JsonRpc("Jokes");
    
    static styles = css`
        a {
            cursor:pointer;
        }
    `;
    
    static properties = {
        _jokes: {state: true},
        _numberOfJokes: {state: true},
        _message: {state: true},
    };
    
    constructor() {
        super();
        this._jokes = jokes;
        this._numberOfJokes = this._jokes.length;
    }

    connectedCallback() {
        super.connectedCallback();
        this._message = "Initializing some jokes ...";
        this.jsonRpc.initJokes({size:5}).then(jsonRpcResponse => {
            this._message = null;
            this._jokes.push(...jsonRpcResponse.result);
            this._numberOfJokes = this._numberOfJokes + jsonRpcResponse.result.length;
        });
    }

    disconnectedCallback() {
        super.disconnectedCallback()
    }

    render() {
        return html`<h3>Here are ${this._numberOfJokes} jokes</h3>
            <ul>${this._jokes.map((joke, index) =>
                html`<li>${joke.fullJoke}</li>`
            )}
            ${this._renderLoadingMessage()}
            </ul>
            <a @click=${() => this._fetchMoreJokes()}>More</a>
            `;
    }

    _renderLoadingMessage(){
        if(this._message){
            return html`<li>${this._message}</li>`;
        }
    }

    _fetchMoreJokes(){
        this._message = "Fetching new joke ...";
        this.jsonRpc.getJoke().then(jsonRpcResponse => {
            this._message = null;
            this._jokes.push(jsonRpcResponse.result);
            this._numberOfJokes++;
        });
    }

}
customElements.define('qwc-jokes-web-components', QwcJokesWebComponents);