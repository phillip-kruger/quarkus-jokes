import { LitElement, html, css} from 'lit';
import { JsonRpcController } from 'jsonrpc-controller';
import { jokes } from 'jokes-data';
import '@vaadin/message-list';
import '@vaadin/icon';
import '@vaadin/button';
import '@vaadin/text-field';
import '@vaadin/text-area';
import '@vaadin/form-layout';
import '@vaadin/progress-bar';

/**
 * This component shows jokes using web components and build time data
 * 
 * TODO: Params (add your own joke)
 * TODO: Server push (Subscripe to new jokes)
 */
export class QwcJokesWebComponents extends LitElement {
    jsonRpcController = new JsonRpcController(this, "Jokes");
    static JOKES = [];
    
    static styles = css`
        .jokelist {
            padding-bottom: 100px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .newjokeform {
            display: flex;
            justify-content: center;
            gap: 100px;
        }
        .tellMoreButton {
            width:90%;
        }
        vaadin-message-list{
            width:90%;
        }
        .loadingBar{
            width:90%;
        }
        vaadin-form-layout {
            width: 98vw;
            padding-left: 20px;
            padding-right: 20px;
        }
        
        span {
            padding-top: 20px;
            cursor: pointer;
            color: grey;
        }
        span:hover {
            color: #ff004a;
        }
        h2 {
            color: grey;
        }
    `;
    
    static properties = {
        _jokes: {state:true},
        _showAddScreen: {state:true},
        _loadingCount:{state:true},
        _busyAdding: {state:true},
        _newJoke:{state: true},
        _newJokeButtonDisabled:{state: true},
    };
    
    constructor() {
        super();
        this._loadingCount = 0;
        this._clearAddJokeForm();
        
        if(QwcJokesWebComponents.JOKES.length > 0) {
            // Restore the known jokes
            this._jokes = QwcJokesWebComponents.JOKES;
        }else{
            // We have not yet loaded the Build time jokes
            let items = [];
            jokes.forEach((joke) =>{
                var item = this._toJokeItem(joke);
                items.push(item);
            });
            this._jokes = items;
        }
    }
    
    _toJokeItem(joke){
        return {
            text: joke.fullJoke,
            time: joke.timestamp,
            userName: joke.user,
            userImg: joke.profilePic,
        };
    }
    
    disconnectedCallback() {
        super.disconnectedCallback();
        QwcJokesWebComponents.JOKES = this._jokes;
    }
    
    render() {
        if(this._jokes && !this._showAddScreen){
            return this._renderJokesList();
        }else{
            return this._renderAddForm();
        }
    }
    
    _renderJokesList(){
        return html`<div class="jokelist">
            <vaadin-message-list .items="${this._jokes}"></vaadin-message-list>

            ${this._renderJokeListProgessBar()}

            <vaadin-button theme="primary success" @click=${this._tellMore} class="tellMoreButton">
                <vaadin-icon icon="font-awesome-solid:plus"></vaadin-icon> Tell 1 more joke
            </vaadin-button>    
            
            <span @click="${this._contributeJoke}">Do you know any good jokes ? Please tell us ! </span>
        </div>`;
    }

    _renderJokeListProgessBar(){
        if(this._loadingCount>0){
            return html`
            <div class="loadingBar" style="color: var(--lumo-secondary-text-color);">
                <div>Loading ${this._loadingCount} more ...</div>
                <vaadin-progress-bar indeterminate></vaadin-progress-bar>
            </div>`;
        }
    }

    _renderAddForm(){
        return html`
                <h2>Add a Joke</h2>
                <div class="newjokeform">
                    
                    <vaadin-form-layout .responsiveSteps="${this._responsiveSteps}">
                        <vaadin-text-field
                            @input="${this._userChanged}"
                            label="Your Name"
                            required
                            error-message="Please tell us your name."
                            clear-button-visible
                        ></vaadin-text-field>

                        <vaadin-text-area colspan="2"
                            @input="${this._setupChanged}"
                            label="Setup"
                            required
                            error-message="Please enter the joke's setup."
                            clear-button-visible
                        ></vaadin-text-area>

                        <vaadin-text-area colspan="2"
                            @input="${this._punchlineChanged}"
                            label="Punchline"
                            required
                            error-message="Please enter the joke's punchline."
                            clear-button-visible
                            ></vaadin-text-area>

                        <vaadin-button theme="primary success" ?disabled=${this._newJokeButtonDisabled} @click=${this._addOwnJoke}>
                            <vaadin-icon icon="font-awesome-regular:face-laugh-squint"></vaadin-icon> Make us laugh !
                        </vaadin-button>    
                        <vaadin-button theme="secondary" @click=${this._cancelAddOwnJoke}>
                            <vaadin-icon icon="font-awesome-regular:face-frown"></vaadin-icon> Cancel
                        </vaadin-button>    

                        ${this._renderNewJokeProgessBar()}

                    </vaadin-form-layout>
                </div>
        `;
    }


    _renderNewJokeProgessBar(){
        if(this._busyAdding){
            return html`
            <div class="loadingBar" colspan="2" style="color: var(--lumo-secondary-text-color);">
                <div>Checking if your joke is funny...</div>
                <vaadin-progress-bar indeterminate></vaadin-progress-bar>
            </div>`;
        }
    }

    _responsiveSteps = [
        // Use one column by default
        { minWidth: 0, columns: 1 },
        // Use two columns, if layout's width exceeds 500px
        { minWidth: '500px', columns: 2 },
    ];

    _tellMore(){
        this._loadingCount++;
        this.jsonRpcController.request("getJoke");
    }
    
    _addOwnJoke(){
        this._busyAdding = true;
        this._newJokeButtonDisabled = true;
        console.log("newJoke " + JSON.stringify(this._newJoke));
        this.jsonRpcController.request("addJoke", this._newJoke);
        
    }

    _contributeJoke(){
        this._showAddScreen = true;
    }
    
    _clearAddJokeForm(){
        this._newJoke = { 
            user: '',
            setup: '',
            punchline: '',
        };
        this._showAddScreen = false;
        this._newJokeButtonDisabled = true;
        this._busyAdding = false;
    }

    _checkButtonState(){
        if(this._newJoke.user.trim() !== '' && 
            this._newJoke.setup.trim() !== '' &&
            this._newJoke.punchline.trim() !== ''){

            this._newJokeButtonDisabled = false;
        }else{
            this._newJokeButtonDisabled = true;
        }
        
    }

    getJokeResponse(result){
        this._loadingCount--;
        var item = this._toJokeItem(result);
        this._jokes = [
            ...this._jokes,
            item,
        ];
    }

    addJokeResponse(result, id){
        console.log("Add Joke response " + id + " = " + JSON.stringify(result));

        var item = this._toJokeItem(result);
        this._jokes = [
            ...this._jokes,
            item,
        ];
        this._newJokeButtonDisabled = false;
        this._clearAddJokeForm();
    }

    _cancelAddOwnJoke(){
        this._newJokeButtonDisabled = false;
        this._clearAddJokeForm();
    }

    _userChanged(e){
        this._newJoke.user = e.target.value;
        this._checkButtonState();
    }

    _setupChanged(e){
        this._newJoke.setup = e.target.value;
        this._checkButtonState();
    }
    
    _punchlineChanged(e){
        this._newJoke.punchline = e.target.value;
        this._checkButtonState();
    }
}
customElements.define('qwc-jokes-web-components', QwcJokesWebComponents);