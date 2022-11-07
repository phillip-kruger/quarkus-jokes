import { LitElement, html, css} from 'lit';
import { jokes } from 'jokes-data';
import '@vaadin/message-list';

/**
 * This component shows jokes using web components and build time data
 */
export class QwcJokesWebComponents extends LitElement {
    static styles = css`
        vaadin-message-list {
            padding-left:20px;
        }
    `;
    
    static properties = {
        _jokes: {attribute: false},
    };
    
    constructor() {
        super();
        
        let items = [];
            
        jokes.forEach((joke, index) =>{
            var item = {
                text: joke.fullJoke,
                time: joke.timestamp,
                userName: joke.user,
                userColorIndex: index,
                userImg: joke.profilePic,
            };
            items.push(item);
        });
        
        this._jokes = items;
    }
    
    render() {
        if(this._jokes){
            return html`<vaadin-message-list .items="${this._jokes}"></vaadin-message-list>`;
        }
    }
}
customElements.define('qwc-jokes-web-components', QwcJokesWebComponents);