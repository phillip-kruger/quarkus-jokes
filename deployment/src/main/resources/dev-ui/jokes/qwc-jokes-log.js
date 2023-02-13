import { LitElement, html, css} from 'lit';
import { JsonRpc } from 'jsonrpc';
import { LogController } from 'log-controller';

/**
 * This component logs all jokes to demonstrate the log tab participation
 */
export class QwcJokesLog extends LitElement {
    jsonRpc = new JsonRpc("Jokes");
    logControl = new LogController(this, "qwc-jokes-log");

    static styles = css`
        :host {
            color: var(--quarkus-blue);
        }
    `;
    
    static properties = {
        _jokes: {state:true},
        _observer: {state:false},
        _zoom: {state:true},
        _increment: {state: false},
        _followLog: {state: false},
    };
    
    constructor() {
        super();
        this.logControl
                .addToggle("On/off switch", true, (e) => {
                    this._toggleOnOffClicked(e);
                }).addItem("Zoom out", "font-awesome-solid:magnifying-glass-minus", "grey", (e) => {
                    this._zoomOut();
                }).addItem("Zoom in", "font-awesome-solid:magnifying-glass-plus", "grey", (e) => {
                    this._zoomIn();
                }).addItem("Clear", "font-awesome-solid:trash-can", "#FF004A", (e) => {
                    this._clearLog();
                }).addFollow("Follow log", true , (e) => {
                    this._toggleFollowLog(e);
                });
        this._jokes = [];
        this._zoom = parseFloat(1.0);
        this._increment = parseFloat(0.05);
        this._followLog = true;
    }
    
    connectedCallback() {
        super.connectedCallback();
        this._connect();
    }
    
    disconnectedCallback() {
        this._disconnect();
        super.disconnectedCallback();
    }
    
    _connect(){
        if(!this._observer){
            this._observer = this.jsonRpc.jokeLog().onNext(jsonRpcResponse => {
                this._addToJokes(jsonRpcResponse.result);
            });
        }
    }
    
    _disconnect() {
        if(this._observer){
            this._observer.cancel();
        }
    }
    
    render() {
        return html`${this._jokes.map((joke) =>
            html`<code class="log" style="font-size: ${this._zoom}em;">
                    <div class="logEntry">
                        ${joke.fullJoke}
                    </div>
            </code>`
        )}`;    
    }
    
    _addToJokes(joke){
        this._jokes = [
            ...this._jokes,
            joke
        ];
        this._scrollToBottom();
    }
    
    _toggleOnOffClicked(e){
        if(e){
            this._connect();
        }else{
            this._disconnect();
        }
    }
    
    _zoomOut(){
        this._zoom = parseFloat(parseFloat(this._zoom) - parseFloat(this._increment)).toFixed(2);
    }
    
    _zoomIn(){
        this._zoom = parseFloat(parseFloat(this._zoom) + parseFloat(this._increment)).toFixed(2);
    }
    
    _clearLog(){
        this._jokes = [];
    }
    
    _toggleFollowLog(e){
        this._followLog = e;
        this._scrollToBottom();   
    }
    
    async _scrollToBottom(){
        if(this._followLog){
            await this.updateComplete;
            
            const last = Array.from(
                this.shadowRoot.querySelectorAll('.logEntry')
            ).pop();
            
            if(last){
                last.scrollIntoView({
                    behavior: "smooth",
                    block: "end"
                });
            }
        }
    }
}
customElements.define('qwc-jokes-log', QwcJokesLog);