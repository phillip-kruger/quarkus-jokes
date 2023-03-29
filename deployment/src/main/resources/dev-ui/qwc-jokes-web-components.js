import { LitElement, html, css} from 'lit';
import { jokes } from 'build-time-data';
import { JsonRpc } from 'jsonrpc';
import { notifier } from 'notifier';
import '@vaadin/message-list';
import '@vaadin/progress-bar';
import '@vaadin/button';      
import '@vaadin/checkbox';

export class QwcJokesWebComponents extends LitElement {
    
    jsonRpc = new JsonRpc(this);
    
    static styles = css`
        a {
            cursor:pointer;
            padding-left: 10px;
        }
        .buttonBar {
            display: flex;
            justify-content: space-between;
            gap: 10px;
            align-items: center;
            width: 90%;
        }

        .buttonBar .button {
            width: 100%;
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
        let items = [];
        jokes.forEach((joke) =>{
            var item = this._toJokeItem(joke);
            items.push(item);
        });
        this._jokes = items;
        this._numberOfJokes = this._jokes.length;
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
            <vaadin-message-list .items="${this._jokes}"></vaadin-message-list>
            
            ${this._renderLoadingMessage()}
            <div class="buttonBar">
                <vaadin-button class="button" theme="success" @click=${() => this._fetchMoreJokes()}>
                    <vaadin-icon icon="font-awesome-solid:comment"></vaadin-icon> Tell me more jokes
                </vaadin-button>    
                <vaadin-checkbox class="button" label="Stream new jokes continuously" @input=${(e) =>this._startStopStreaming(e)}></vaadin-checkbox>
            </div>
            `;
    }

    _renderLoadingMessage(){
        if(this._message){
            return html`${this._message}
            <vaadin-progress-bar indeterminate></vaadin-progress-bar>`;
        }
    }

    _fetchMoreJokes(){
        this._message = "Fetching new joke ...";
        this.jsonRpc.getJoke().then(jsonRpcResponse => {
            this._message = null;
            this._addToJokes(jsonRpcResponse.result);
            this._numberOfJokes++;
        });
    }

    _startStopStreaming(e){
        this._isStreaming = e.target.checked;

        if(this._isStreaming){
            notifier.showInfoMessage("Streaming jokes activated ...");
            this._observer = this.jsonRpc.streamJokes().onNext(jsonRpcResponse => {
                this._addToJokes(jsonRpcResponse.result);
                this._numberOfJokes = this._numberOfJokes++;
            });
        }else{
            this._observer.cancel();
            notifier.showWarningMessage("Streaming jokes canceled...");
        }
    }

    _toJokeItem(joke){
        return {
            text: joke.fullJoke,
            time: joke.timestamp,
            userName: joke.user,
            userImg: joke.profilePic
        };
    }

    _addToJokes(joke){
        var item = this._toJokeItem(joke);
        this._jokes = [
            ...this._jokes,
            item
        ];
    }

}
customElements.define('qwc-jokes-web-components', QwcJokesWebComponents);