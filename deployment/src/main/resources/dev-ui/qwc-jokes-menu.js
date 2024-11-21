import { LitElement, html, css} from 'lit';
import './../io.quarkiverse.jokes.quarkus-jokes-common/qwc-joke.js';

/**
 * This component shows how to add to the section menu
 */
export class QwcJokesMenu extends LitElement {
    
    render() {
        return html`<qwc-joke namespace='io.quarkiverse.jokes.quarkus-jokes'></qwc-joke>`;    
    }
}
customElements.define('qwc-jokes-menu', QwcJokesMenu);