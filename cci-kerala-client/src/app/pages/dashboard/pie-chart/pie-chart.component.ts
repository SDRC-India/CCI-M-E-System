import { Component, OnInit, ElementRef, ViewChild, Input } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import * as d3 from "d3";

declare var $ :any;
@Component({
  selector: 'scps-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.scss']
})
export class PieChartComponent implements OnInit {
  @ViewChild('pieChart') private chartContainer: ElementRef;
  @Input() private data : any;
  constructor(private httpClient: HttpClient, private hostRef: ElementRef) { }

  ngOnInit() {
    if(this.data){
      this.createChart(this.data);
    }
  }

  createChart(data){
    //console.log(data);
    let el = this.chartContainer.nativeElement;     
    d3.select(el).selectAll("*").remove();
    let margin = {
        left: 20,
        right: 20,
        top: 0,
        bottom: 20
    }
    
      var width = 220;
      var height = 200;
      margin.left = ($(this.hostRef.nativeElement).parent().width() - width) / 2 ; 
      margin.right = ($(this.hostRef.nativeElement).parent().width() - width) / 2 ;  
      var radius = Math.min(width, height) / 2;
      // var color = d3.scaleOrdinal().range(
      //   ["#ab7253", "#eb9c73", "#eacb9f", "#428ead"]);
  var arc = d3.arc()
    .outerRadius(
      radius - 10)
    .innerRadius(0);

  var pie = d3.pie()
    .value(
      function (d) {
        return parseInt(d.value);
      }).sort(
        null);


    var svg = d3
      .select(el)
      .append("svg")
      .attr("id",
        "chart")
      .attr(
        "width",
        width)
      .attr(
        "height",
        height + 30)
        .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')')
      .append("g")
      .attr(
        "transform",
        "translate("
        + (width / 2 - 10)
        + ","
        + (height / 2)
        + ")")
        ;
        
        let allNullValues;
        for (let j = 0; j < data.length; j++) {
            if (data[j].value != null) {
              allNullValues = false;
              break;
          }
        }
    
    
        if (allNullValues!=false) {
          svg.append("g").append("text")
          .attr("transform", "translate(" + -(width / 2 - 50) + ",0)")
          .attr("x", 0)
          .attr("y", 0)
          .attr("font-size", "16px")
          .text("Data Not Available");
          return;
        }

  // ////===========End of Legend for doughnut chart======================//////

  function pieChart(data) {
    var g = svg.selectAll(
      ".arc").data(
        pie(data))
      .enter()
      .append("g")
      .attr("class",
        "arc")
      .attr("align",
        "left");

    g
      .append("path")
      .attr("d", arc)
      .attr("class" , "pie-path")
      .attr("stroke", "#444f57")
      .attr("stroke-width", "2px")

      .on("mouseover", function (d, i) {
        showPopover.call(this, d, i);
      })
      .on(
        "mouseout",
        function removePopovers() {
          $(
            '.popover')
            .each(
              function () {
                $(
                  this)
                  .remove();
              });
        })
         .style("fill", function (d) {
          return d.data.cssColor;
        })
    //   .on("click",
    //     click)
      .transition()
      .delay(
        function (
          d,
          i) {
          return i * 500;
        })
      .duration(500)
      .attrTween('d', function (d) {
            var i = d3.interpolate(d.startAngle + 0.1, d.endAngle);
          return function (t) {
            d.endAngle = i(t);
            return arc(d);
          };
        })
      .attr("class", function (d) {
          if (d.endAngle)
            return d.data.label;
          else
            return d.data.label
              + " zeroValue";
        })
      .style("fill", function (d) {
          return d.data.cssColor;
        })
        svg.selectAll(".arc").append("text")
        .attr("transform", function(d) {
            return "translate(" + arc.centroid(d) + ")";
        })
        .attr("dy", "0.5em")
          .attr("dx", "-1.5em")
        .text(function(d) {
          return Math
            .round(Math
              .round(1000 * (d.endAngle - d.startAngle))
              / (Math.PI * 2)) / 10 + '% (' + d.data.value + ')';;
        }).style("font-size", "12px").call(wrap);


    function wrap(text) {
      text.each(function () {
        var text = d3.select(this),
          words = text.text().split(/\s+/).reverse(),
          word,
          line = [],
          lineNumber = 0,
          lineHeight = 1.1,
          y = text.attr("y"),
          dy = parseFloat(text.attr("dy"));
          var tspan = text.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em").style('font-size', '15px');

        var tspan
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
              tspan = text.append("tspan").attr("x", 0).attr("y", y).attr("dy", ++lineNumber * lineHeight + dy + "em").style('font-size', '15px').text(word);
          }
        }
      });
    }


        function showPopover(d, i) {
        $(this).popover(
            {
              title : '',
              placement : 'top',
              container : 'body',
              trigger : 'manual',
              html : true,
              animation: false,
              content : function() {
                
                  return "<span style='color:#2f4f4f'>"
                  + d.data.axis
                  + " : </span>"
                  + "<span style='color:#2f4f4f'>"
                  + (d.endAngle
                    - d.startAngle > 0.0 ? Math
                      .round(Math
                        .round(1000 * (d.endAngle - d.startAngle))
                        / (Math.PI * 2))
                    / 10
                    + '%'
                    : '')
                  + "</span>";
              }
            });
        $(this).popover('show');
      }
      function removePopovers() {
        $('.popover').each(function() {
          $(this).remove();
        });
      }
  $(".percentVal")
    .delay(1500)
    .fadeIn();
  }
  
  pieChart(data);
}

}
