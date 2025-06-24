import { LitElement, html, css} from 'lit';

/**
 * This component shows how to add to the section menu
 */
export class QwcJokesMenu extends LitElement {
    
    render() {
        return html`<qwc-joke namespace='io.quarkiverse.jokes.quarkus-jokes'></qwc-joke>`;    
    }
}
customElements.define('qwc-jokes-menu', QwcJokesMenu);