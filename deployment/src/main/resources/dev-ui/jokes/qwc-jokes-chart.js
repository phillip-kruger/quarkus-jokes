import { LitElement, html, css} from 'lit';
import { Chart, LinearScale, BarElement, BarController, CategoryScale, Decimation, Filler, Legend, Title, Tooltip} from 'chart.js';
import { getRelativePosition } from 'chart.js/dist/helpers.js';
import '@vaadin/icon';

export class QwcJokesChart extends LitElement {
    
    static styles = css`
        .mainContainer {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            width: 100%;
            height: 100%;
        }
        .chartContainer {
            width: 100%;
            height: 100%;
            max-width: 100%;
            max-height: 100%;
        }
        .menubar {
            display:flex;
            gap: 10px;
            border: 1px solid var(--lumo-contrast-10pct);
            padding: 6px;
            width: fit-content;
        }
        .menubar vaadin-icon {
            cursor: pointer;
            color: var(--lumo-primary-color-50pct);
            padding: 2px;
        }
    `;
    
    static properties = {
        myChart: { type: Object }
    };
    
    constructor() {
        super();
        Chart.register(LinearScale, BarElement, BarController, CategoryScale, Decimation, Filler, Legend, Title, Tooltip);
    }
    
    render() {
        return html`
            <div class="mainContainer">
                <div class="menubar">
                    <vaadin-icon title="Back" icon="font-awesome-solid:chevron-left" @click=${() => this._backToMainSheet()}></vaadin-icon>
                    <vaadin-icon title="Zoom In" icon="font-awesome-solid:magnifying-glass-plus" @click=${() => this._zoomIn()}></vaadin-icon>
                    <vaadin-icon title="Zoom Out" icon="font-awesome-solid:magnifying-glass-minus" @click=${() => this._zoomOut()}></vaadin-icon>
                </div>

                <div class="chartContainer" >
                    <canvas id="chart"></canvas>
                </div>
            </div>
          `;
    }

    firstUpdated() {
        const chartCanvas = this.renderRoot.querySelector( '#chart' );
        const ctx = chartCanvas.getContext('2d');
    
        this.myChart = new Chart(ctx, {
            type: 'bar',
            data: {
              labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
              datasets: [{
                label: '# of Votes',
                data: [12, 19, 3, 5, 2, 3],
                borderWidth: 1,
                backgroundColor: ['#CB4335', '#1F618D', '#F1C40F', '#27AE60', '#884EA0', '#D35400'],
              }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Votes',
                        }
                    }
                },
                onClick: (e) => {
                    const canvasPosition = getRelativePosition(e, this.myChart);

                    // Substitute the appropriate scale IDs
                    const dataX = this.myChart.scales.x.getValueForPixel(canvasPosition.x);
                    const dataY = this.myChart.scales.y.getValueForPixel(canvasPosition.y);

                    console.log("dataX = " + dataX);
                    console.log("dataY = " + dataY);

                }
            }
        });

        var rect = chartCanvas.parentNode.getBoundingClientRect();
        chartCanvas.width = rect.width;
        chartCanvas.height = rect.height;
        chartCanvas.maxWidth = rect.width;
        chartCanvas.maxHeight = rect.height;
    }

    // TODO: myLineChart.destroy();
    // TODO: myLineChart.toBase64Image();

    _backToMainSheet(){
        console.log("Back");  
    }

    _zoomIn() {
        console.log("Zoom In");
    }
    _zoomOut() {
        console.log("Zoom Out");
    }

}
customElements.define('qwc-jokes-chart', QwcJokesChart);
