import { LitElement, html, css} from 'lit';
import 'echarts-gauge-grade';

import '@vaadin/icon';

export class QwcJokesChart extends LitElement {
    
    static styles = css`
        :host {
            width: 100%;
            height: 100%;
            display: block;
        }
    `;
    
    static properties = {
        
    };
    
    constructor() {
        super();
    }
    

    render() {
        return html`
          <echarts-gauge-grade 
            title="Reactive score" 
            percentage="66"
            sectionTitles="bad,ok,good"
            sectionColors="--lumo-error-color,--lumo-warning-color,--lumo-success-color"
            primaryTextColor="--lumo-body-text-color"
            secondaryTextColor="--lumo-secondary-text-color">
        </echarts-gauge-grade>
        `;
      }


}
customElements.define('qwc-jokes-chart', QwcJokesChart);
