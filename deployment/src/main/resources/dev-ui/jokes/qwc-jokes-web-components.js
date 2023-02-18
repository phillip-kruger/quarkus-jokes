import { LitElement, html, css} from 'lit';
import { jokes } from 'jokes-data';
import { JsonRpc } from 'jsonrpc';

export class QwcJokesWebComponents extends LitElement {
    
    jsonRpc = new JsonRpc("Jokes");
    
    static styles = css`
        a {
            cursor:pointer;
            padding-left: 10px;
        }
    `;
    
    static properties = {
        _jokes: {state: true},
        _numberOfJokes: {state: true},
        _message: {state: true},
        _isStreaming: {state: true},
    };
    
    constructor() {
        super();
        this._jokes = jokes;
        this._numberOfJokes = this._jokes.length;
        this._streamingButtonTitle = "Start streaming";
        this._isStreaming = false;
    }

    connectedCallback() {
        super.connectedCallback();
    }

    disconnectedCallback() {
        if(this._isStreaming){
            this._observer.cancel();
        }
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
            <a @click=${() => this._startStopStreaming()}>${this._streamingButtonTitle}</a>
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

    _startStopStreaming(){
        if(this._isStreaming){ // Stop it
            this._isStreaming = false;
            this._streamingButtonTitle = "Start streaming";
            this._observer.cancel();
        }else{ // Start it
            this._isStreaming = true;
            this._streamingButtonTitle = "Stop streaming";
            this._observer = this.jsonRpc.streamJokes().onNext(jsonRpcResponse => {
                this._jokes.push(jsonRpcResponse.result);
                this._numberOfJokes = this._numberOfJokes++;
            });
        }
    }

}
customElements.define('qwc-jokes-web-components', QwcJokesWebComponents);