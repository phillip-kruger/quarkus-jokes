import { LitElement, html, css} from 'lit';
import { JsonRpcController } from 'jsonrpc-controller';
import { jokes } from 'jokes-data';
import '@vaadin/message-list';
import '@vaadin/icon';

/**
 * This component shows jokes using web components and build time data
 */
export class QwcJokesWebComponents extends LitElement {
    jsonRpcController = new JsonRpcController(this, "Jokes");
    
    static styles = css`
        div {
            padding-bottom: 100px;
        }
        vaadin-message-list {
            padding-left:20px;
            
        }
        button {
            display: block;
            width: 99%;
            border: none;
            background-color: #04AA6D;
            padding: 14px 28px;
            font-size: 16px;
            cursor: pointer;
            text-align: center;
            color: white;
        }
    `;
    
    static properties = {
        _jokes2: {attribute: false},
        _jokes: {state:true},
    };
    
    constructor() {
        super();
        let items = [];
            
        jokes.forEach((joke, index) =>{
            var item = this._toJokeItem(joke, index);
            items.push(item);
        });
        
        this._jokes = items;
    }
    
    render() {
        if(this._jokes){
            return html`<div>
                            <vaadin-message-list .items="${this._jokes}"></vaadin-message-list>
                            <button type="button" @click="${this._addMore}">
                                <vaadin-icon icon="font-awesome-solid:plus"></vaadin-icon> Add 1 more joke
                            </button>
                        </div>`;
        }
    }
    
    _toJokeItem(joke, index){
        return {
            text: joke.fullJoke,
            time: joke.timestamp,
            userName: joke.user,
            userColorIndex: index,
            userImg: joke.profilePic,
        };
    }
    
    _addMore(){
        this.jsonRpcController.request("getJoke");
    }
    
    onJsonRpcResponse(result){
        var item = this._toJokeItem(result, 1);
        this._jokes = [
            ...this._jokes,
            item,
        ];
    }
}
customElements.define('qwc-jokes-web-components', QwcJokesWebComponents);