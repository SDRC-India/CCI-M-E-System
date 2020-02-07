import { Component, OnInit, ElementRef, ViewChild, Input } from '@angular/core';
import * as d3 from 'd3';
declare var $: any;
@Component({
  selector: 'scps-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrls: ['./bar-chart.component.scss']
})
export class BarChartComponent implements OnInit {
  @ViewChild('barChart') private chartContainer: ElementRef;
  @Input('data') private data: Array<any>;
  @Input('xGrid') private xGrid: boolean;
  @Input('yGrid') private yGrid: boolean;
  @Input('values') private values: boolean;


  constructor(private hostRef: ElementRef) { }

  ngOnInit() {
    if (this.data) {
      this.createChart(this.data);
    }
  }

  createChart(data) {
    let el = this.chartContainer.nativeElement;
    d3.select(el).select("svg").remove();
    var n = data.length, // number of layers
      m = 10 // number of samples per layer
    var layers = data;
    layers.forEach(function (layer, i) {
      layer.forEach(function (el, j) {
        el.y = undefined;
        el.y0 = i;
      });
    });

    var margin = {
      top: 20,
      right: 20,
      bottom: 40,   // bottom height
      left: 40
    }, 
    width = $(this.hostRef.nativeElement).parent().width() - margin.left - margin.right, height = $(this.hostRef.nativeElement).parent().height() + 300 - margin.top - margin.bottom // //
      // height
      - margin.top - margin.bottom + 10;

    //  var z = d3.scale.scaleOrdinal(['#717171']);
    var x = d3.scaleBand().domain(data[0].map(function (d) {
      return d.axis;
    })).range([0, width]).round(true).padding(.1)
    var max = d3.max(data[0].map(function (d) {
      return parseInt(d.value);
    }));


    var y = d3.scaleLinear().domain([0, (max + (100 - max % 100))]).rangeRound(
      [height, 0]);

    var color = ["#eb9c73"];

    var hoverColor = ["#017A27", "#FF5900", "#b7191c"];

    var formatTick = function (d) {
      return d;
    };
    const xBandwidth = x.bandwidth() > 70 * data.length ? 70 * data.length : x.bandwidth();


    var xAxis = d3.axisBottom().scale(x).tickFormat(formatTick);
    var svg = d3.select(el).append("svg").attr("id",
      "columnbarChart").attr("width",
        width + margin.left + margin.right).attr("height",
          height + margin.top + margin.bottom).append("g")
      .attr(
        "transform",
        "translate(" + margin.left + ","
        + margin.top + ")");


    let allNullValues = true;
    for (let j = 0; j < data.length; j++) {
      for (let index = 0; index < data[j].length; index++) {
        if (data[j][index].value != null) {
          allNullValues = false;
          break;
        }
      }
    }


    if (allNullValues) {
      if (width <= 360) {
        svg.append("g").append("text")
          .attr("transform", "translate(50,50)")
          .attr("x", 40)
          .attr("y", 50)
          .attr("font-size", "16px")
          .text("Data Not Available");
      } else {
        svg.append("g").append("text")
          .attr("transform", "translate(120,50)")
          .attr("x", 40)
          .attr("y", 50)
          .attr("font-size", "18px")
          .text("Data Not Available");
      }
     
    }

    // add the X gridlines
    if (this.xGrid && !allNullValues) {
      svg.append("g")
        .attr("class", "grid")
        .attr("transform", "translate(0," + height + ")")
        .call(make_x_gridlines()
          .tickSize(-height).tickFormat(null)
        ).selectAll("text").remove();
    }
    // add the Y gridlines
    if (this.yGrid && !allNullValues) {
      svg.append("g")
        .attr("class", "grid")
        .call(make_y_gridlines()
          .tickSize(-width).tickFormat(null)
        ).selectAll("text").remove();
    }
    var layer = svg.selectAll(".layer").data(layers).enter()
      .append("g").attr("class", "layer").style("fill",
        function (d, i) {
          return color[i];
        }).attr("id", function (d, i) {
          return i;
        });

    var rect = layer.selectAll("rect").data(function (d) {
      return d;
    }).enter().append("rect").attr("x", function (d) {
      return x(d.axis) + (x.bandwidth() - xBandwidth) / 2;
    }).attr("y", height).attr("width", xBandwidth).attr(
      "height", 0)

      .on("mouseover", function (d) {
        showPopover.call(this, d);



      }).on("mouseout", function (d) {
        removePopovers();


      }).style("stroke","#333").style("stroke-width","1px");
    ;

    svg.append("g").attr("class", "x axis").attr("transform",
      "translate(0," + height + ")").call(xAxis)
      .selectAll("text").style("text-anchor", "middle")
      .attr("class", function (d, i) { return "evmbartext" + i })
      .attr("dx", "-.2em").attr("dy", ".70em")
      .call(wrap, x.bandwidth(), width);



    var yAxis = d3.axisLeft().scale(y);

    svg.append("g").attr("class", "y axis").call(yAxis).append(
      "text").attr("transform", "rotate(-90)").attr("y",
        0 - margin.left).attr("x", 0 - (height / 2)).attr(
          "dy", "1em").attr("text-anchor", "end")




    // gridlines in x axis function
    function make_x_gridlines() {
      return d3.axisBottom(x)
        .ticks(5)
    }

    // gridlines in y axis function
    function make_y_gridlines() {
      return d3.axisLeft(y)
        .ticks(5)
    }

    function transitionGrouped() {
      y.domain([0, (max + (100 - max % 100))]);

      rect.transition().duration(500).delay(function (d, i) {
        return i * 10;
      }).attr("x", function (d, i, j) {
        return x(d.axis) + (x.bandwidth() - xBandwidth) / 2 + xBandwidth / n * d.y0; // function(d)     
      }).attr("width", xBandwidth / n).transition().attr(
        "y", function (d) {
          return y(d.value);
        }).attr("height", function (d) {
          return height - y(d.value);
        });
    }

    transitionGrouped();
    function removePopovers() {
      $('.popover').each(function () {
        $(this).remove();
      });
    }
    function showPopover(d) {
      $(this).popover(
        {
          title: '',
          placement: 'top',
          container: 'body',
          trigger: 'manual',
          html: true,
          animation: false,
          content: function () {
            if (d.axis != "")
              return "<div style='color: #257ab6;'>" + d.axis + "</div>" + "Score : "
                + d.value + '';
            else
              return "<div style='color: #257ab6;'> Average: " + d.value + "</div>";
          }
        });
      $(this).popover('show');


    }
    //============Text wrap function in x-axis of column chart=====================



    function wrap(text, width, windowWidth) {
      text.each(function () {
        let text = d3.select(this),
          words = text.text().split(/\s+/).reverse(),
          word,
          line = [],
          lineNumber = 0,
          lineHeight = 1.1,
          y = text.attr("y"),
          dy = parseFloat(text.attr("dy"));
          let tspan;
        if (windowWidth > 660)
           tspan = text.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em").style('font-size', '10px');
        else
           tspan = text.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em").style('font-size', '10px');

        if (words.length == 1) {
          let chars = words.toString().split("");
          chars.splice((chars.length / 2).toFixed(), 0, '-', ' ');
          tspan.text(chars.join(""));
          if (tspan.node().getComputedTextLength() > width) {
            words = chars.join("").split(/\s+/).reverse();
          }
          tspan.text(null);
        }
        while (word = words.pop()) {
          line.push(word);
          tspan.text(line.join(" "));
          if (tspan.node().getComputedTextLength() > width) {
            line.pop();
            tspan.text(line.join(" "));
            line = [word];
            // if(cnt!=1)
            if (width > 660)
              tspan = text.append("tspan").attr("x", 0).attr("y", y).attr("dy", ++lineNumber * lineHeight + dy + "em").style('font-size', '10px').text(word);
            else
              tspan = text.append("tspan").attr("x", 0).attr("y", y).attr("dy", ++lineNumber * lineHeight + dy + "em").style('font-size', '10px').text(word);
          }
        }
      });
    }


    //NEW CODE FOR DATA VALUE TEXT ON EACH BAR-----------------
    if (this.values && !allNullValues) {
      var e0Arr = [];
      for (var i = 0; i < data.length; i++) {
        e0Arr.push(data[i][0].value);
        layer.selectAll("evmbartext" + i).data(data[i]).enter()
          .append("text").attr(
            "x",
            function (d) {
              // return x(d.axis) + (x.bandwidth()- xBandwidth)/2+ xBandwidth
              //         / 2 + 12;
              return x(d.axis) + (x.bandwidth() - xBandwidth) / 2 + xBandwidth / (2 * data.length) + (xBandwidth / data.length * i);
            })
          .attr("y", function (d) {
            return y(d.value) - 3;
          }).style("text-anchor", "middle").style("fill",
            "#000000").text(function (d) {
              return d.value?Math.round(d.value):"N/A";
            }).style("font-size", "0.8rem");
      }
    }
  }

}
