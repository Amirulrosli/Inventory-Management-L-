const width_threshold = 480;

function drawLineChart() {
  if ($("#lineChart").length) {
    ctxLine = document.getElementById("lineChart").getContext("2d");
    optionsLine = {
      scales: {
        yAxes: [
          {
            scaleLabel: {
              display: true,
              labelString: "Data & Trends ($)"
            }
          }
        ]
      }
    };

    // Set aspect ratio based on window width
    optionsLine.maintainAspectRatio =
      $(window).width() < width_threshold ? false : true;

    configLine = {
      type: "line",
      data: {
        labels: [
          "June",
          "July",
          "August",
          "September",
          "October",
          "November",
          "Disember"
        ],
        datasets: [
          {
            label: "Data Usage",
            data: [249.38, 273.33, 264.74, 262.45, 263.52, 280.09, 77.01], /* edit----------------------*/
            fill: true,
            borderColor: "rgb(75, 192, 192)",
            cubicInterpolationMode: "monotone",
            pointRadius: 0
          },
          {
            label: "Trend ($)",
            data: [52.77, 52, 62.32, 72.62, 62.62, 44.56, 0], /* edit----------------------*/
            fill: true,
            borderColor: "rgba(255,99,132,1)",
            cubicInterpolationMode: "monotone",
            pointRadius: 0
          },

        ]
      },
      options: optionsLine
    };

    lineChart = new Chart(ctxLine, configLine);
  }
}

function drawBarChart() {
  if ($("#barChart").length) {
    ctxBar = document.getElementById("barChart").getContext("2d");

    optionsBar = {
      responsive: true,
      scales: {
        yAxes: [
          {
            barPercentage: 0.2,
            ticks: {
              beginAtZero: true
            },
            scaleLabel: {
              display: true,
              labelString: "Application"
            }
          }
        ]
      }
    };

    optionsBar.maintainAspectRatio =
      $(window).width() < width_threshold ? false : true;

    /**
     * COLOR CODES
     * Red: #F7604D
     * Aqua: #4ED6B8
     * Green: #A8D582
     * Yellow: #D7D768
     * Purple: #9D66CC
     * Orange: #DB9C3F
     * Blue: #3889FC
     */

    configBar = {
      type: "horizontalBar",
      data: {
        labels: ["Whatsapp & VoIP", "Social Media", "Facebook", "Web Browsing", "Downloads", "App Market", "Youtube"],
        datasets: [
          {
            label: "# of GB",
            data: [17.69, 14.62, 11.27, 6.81, 4.81, 0.86, 0.55], /* edit----------------------*/
            backgroundColor: [
              "#F7604D",
              "#4ED6B8",
              "#A8D582",
              "#D7D768",
              "#9D66CC",
              "#DB9C3F",
              "#3889FC"
            ],
            borderWidth: 0
          }
        ]
      },
      options: optionsBar
    };

    barChart = new Chart(ctxBar, configBar);
  }
}

function drawPieChart() {
  if ($("#pieChart").length) {
    var chartHeight = 300;

    $("#pieChartContainer").css("height", chartHeight + "px");

    ctxPie = document.getElementById("pieChart").getContext("2d");

    optionsPie = {
      responsive: true,
      maintainAspectRatio: false,
      layout: {
        padding: {
          left: 10,
          right: 10,
          top: 10,
          bottom: 10
        }
      },
      legend: {
        position: "top"

      }
    };


    configPie = {
      type: "pie",
      data: {
        datasets: [
          {
            data: [25.04,0,9.05],   /* edit----------------------*/
            backgroundColor: ["#F7604D", "#A8D582", "#4ED6B8"],
            label: "Storage"
          }
        ],
        labels: [                              /* edit----------------------*/
          "Used data (74.01GB)",
          "Total Storage (100.00GB)",
          "Available Data (35.99GB)"
        ]
      },

      options: optionsPie

    };

    pieChart = new Chart(ctxPie, configPie);
  }
}

function updateLineChart() {
  if (lineChart) {
    lineChart.options = optionsLine;
    lineChart.update();
  }
}

function updateBarChart() {
  if (barChart) {
    barChart.options = optionsBar;
    barChart.update();
  }
}
