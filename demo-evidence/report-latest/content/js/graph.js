/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
$(document).ready(function() {

    $(".click-title").mouseenter( function(    e){
        e.preventDefault();
        this.style.cursor="pointer";
    });
    $(".click-title").mousedown( function(event){
        event.preventDefault();
    });

    // Ugly code while this script is shared among several pages
    try{
        refreshHitsPerSecond(true);
    } catch(e){}
    try{
        refreshResponseTimeOverTime(true);
    } catch(e){}
    try{
        refreshResponseTimePercentiles();
    } catch(e){}
});


var responseTimePercentilesInfos = {
        data: {"result": {"minY": 173.0, "minX": 0.0, "maxY": 1704.0, "series": [{"data": [[0.0, 173.0], [0.1, 173.0], [0.2, 173.0], [0.3, 173.0], [0.4, 174.0], [0.5, 174.0], [0.6, 174.0], [0.7, 174.0], [0.8, 175.0], [0.9, 175.0], [1.0, 175.0], [1.1, 175.0], [1.2, 175.0], [1.3, 175.0], [1.4, 175.0], [1.5, 175.0], [1.6, 176.0], [1.7, 176.0], [1.8, 176.0], [1.9, 176.0], [2.0, 176.0], [2.1, 176.0], [2.2, 176.0], [2.3, 176.0], [2.4, 176.0], [2.5, 176.0], [2.6, 176.0], [2.7, 176.0], [2.8, 177.0], [2.9, 177.0], [3.0, 177.0], [3.1, 177.0], [3.2, 178.0], [3.3, 178.0], [3.4, 178.0], [3.5, 178.0], [3.6, 178.0], [3.7, 178.0], [3.8, 178.0], [3.9, 178.0], [4.0, 179.0], [4.1, 179.0], [4.2, 179.0], [4.3, 179.0], [4.4, 183.0], [4.5, 183.0], [4.6, 183.0], [4.7, 183.0], [4.8, 183.0], [4.9, 183.0], [5.0, 183.0], [5.1, 183.0], [5.2, 184.0], [5.3, 184.0], [5.4, 184.0], [5.5, 184.0], [5.6, 184.0], [5.7, 185.0], [5.8, 185.0], [5.9, 185.0], [6.0, 185.0], [6.1, 185.0], [6.2, 185.0], [6.3, 185.0], [6.4, 185.0], [6.5, 185.0], [6.6, 185.0], [6.7, 185.0], [6.8, 185.0], [6.9, 186.0], [7.0, 186.0], [7.1, 186.0], [7.2, 186.0], [7.3, 186.0], [7.4, 186.0], [7.5, 186.0], [7.6, 186.0], [7.7, 186.0], [7.8, 186.0], [7.9, 186.0], [8.0, 186.0], [8.1, 186.0], [8.2, 186.0], [8.3, 186.0], [8.4, 186.0], [8.5, 186.0], [8.6, 186.0], [8.7, 186.0], [8.8, 187.0], [8.9, 187.0], [9.0, 187.0], [9.1, 187.0], [9.2, 187.0], [9.3, 187.0], [9.4, 187.0], [9.5, 187.0], [9.6, 187.0], [9.7, 188.0], [9.8, 188.0], [9.9, 188.0], [10.0, 188.0], [10.1, 188.0], [10.2, 188.0], [10.3, 188.0], [10.4, 188.0], [10.5, 189.0], [10.6, 189.0], [10.7, 189.0], [10.8, 189.0], [10.9, 191.0], [11.0, 191.0], [11.1, 191.0], [11.2, 191.0], [11.3, 191.0], [11.4, 191.0], [11.5, 191.0], [11.6, 191.0], [11.7, 192.0], [11.8, 192.0], [11.9, 192.0], [12.0, 192.0], [12.1, 192.0], [12.2, 192.0], [12.3, 192.0], [12.4, 192.0], [12.5, 192.0], [12.6, 192.0], [12.7, 192.0], [12.8, 192.0], [12.9, 192.0], [13.0, 192.0], [13.1, 192.0], [13.2, 192.0], [13.3, 193.0], [13.4, 193.0], [13.5, 193.0], [13.6, 193.0], [13.7, 193.0], [13.8, 193.0], [13.9, 193.0], [14.0, 193.0], [14.1, 194.0], [14.2, 194.0], [14.3, 194.0], [14.4, 194.0], [14.5, 194.0], [14.6, 194.0], [14.7, 194.0], [14.8, 194.0], [14.9, 195.0], [15.0, 195.0], [15.1, 195.0], [15.2, 195.0], [15.3, 198.0], [15.4, 198.0], [15.5, 198.0], [15.6, 198.0], [15.7, 198.0], [15.8, 198.0], [15.9, 198.0], [16.0, 198.0], [16.1, 198.0], [16.2, 198.0], [16.3, 198.0], [16.4, 198.0], [16.5, 198.0], [16.6, 198.0], [16.7, 198.0], [16.8, 198.0], [16.9, 199.0], [17.0, 199.0], [17.1, 199.0], [17.2, 199.0], [17.3, 199.0], [17.4, 199.0], [17.5, 199.0], [17.6, 199.0], [17.7, 200.0], [17.8, 200.0], [17.9, 200.0], [18.0, 200.0], [18.1, 200.0], [18.2, 200.0], [18.3, 200.0], [18.4, 200.0], [18.5, 200.0], [18.6, 200.0], [18.7, 200.0], [18.8, 200.0], [18.9, 200.0], [19.0, 200.0], [19.1, 200.0], [19.2, 200.0], [19.3, 201.0], [19.4, 201.0], [19.5, 201.0], [19.6, 201.0], [19.7, 201.0], [19.8, 201.0], [19.9, 201.0], [20.0, 201.0], [20.1, 201.0], [20.2, 201.0], [20.3, 201.0], [20.4, 201.0], [20.5, 201.0], [20.6, 201.0], [20.7, 201.0], [20.8, 201.0], [20.9, 201.0], [21.0, 201.0], [21.1, 201.0], [21.2, 201.0], [21.3, 202.0], [21.4, 202.0], [21.5, 202.0], [21.6, 202.0], [21.7, 202.0], [21.8, 202.0], [21.9, 202.0], [22.0, 202.0], [22.1, 202.0], [22.2, 202.0], [22.3, 202.0], [22.4, 202.0], [22.5, 202.0], [22.6, 202.0], [22.7, 202.0], [22.8, 202.0], [22.9, 203.0], [23.0, 203.0], [23.1, 203.0], [23.2, 203.0], [23.3, 204.0], [23.4, 204.0], [23.5, 204.0], [23.6, 204.0], [23.7, 205.0], [23.8, 205.0], [23.9, 205.0], [24.0, 205.0], [24.1, 206.0], [24.2, 206.0], [24.3, 206.0], [24.4, 206.0], [24.5, 206.0], [24.6, 206.0], [24.7, 206.0], [24.8, 206.0], [24.9, 206.0], [25.0, 206.0], [25.1, 206.0], [25.2, 206.0], [25.3, 207.0], [25.4, 207.0], [25.5, 207.0], [25.6, 208.0], [25.7, 208.0], [25.8, 208.0], [25.9, 208.0], [26.0, 208.0], [26.1, 208.0], [26.2, 208.0], [26.3, 208.0], [26.4, 208.0], [26.5, 209.0], [26.6, 209.0], [26.7, 209.0], [26.8, 209.0], [26.9, 209.0], [27.0, 209.0], [27.1, 209.0], [27.2, 209.0], [27.3, 212.0], [27.4, 212.0], [27.5, 212.0], [27.6, 212.0], [27.7, 212.0], [27.8, 212.0], [27.9, 212.0], [28.0, 212.0], [28.1, 213.0], [28.2, 213.0], [28.3, 213.0], [28.4, 213.0], [28.5, 215.0], [28.6, 215.0], [28.7, 215.0], [28.8, 216.0], [28.9, 216.0], [29.0, 216.0], [29.1, 216.0], [29.2, 216.0], [29.3, 216.0], [29.4, 216.0], [29.5, 216.0], [29.6, 217.0], [29.7, 217.0], [29.8, 217.0], [29.9, 217.0], [30.0, 217.0], [30.1, 217.0], [30.2, 217.0], [30.3, 217.0], [30.4, 217.0], [30.5, 217.0], [30.6, 217.0], [30.7, 217.0], [30.8, 217.0], [30.9, 217.0], [31.0, 217.0], [31.1, 217.0], [31.2, 217.0], [31.3, 218.0], [31.4, 218.0], [31.5, 218.0], [31.6, 219.0], [31.7, 219.0], [31.8, 219.0], [31.9, 219.0], [32.0, 219.0], [32.1, 219.0], [32.2, 219.0], [32.3, 219.0], [32.4, 221.0], [32.5, 221.0], [32.6, 221.0], [32.7, 221.0], [32.8, 221.0], [32.9, 221.0], [33.0, 221.0], [33.1, 221.0], [33.2, 222.0], [33.3, 222.0], [33.4, 222.0], [33.5, 222.0], [33.6, 222.0], [33.7, 222.0], [33.8, 222.0], [33.9, 222.0], [34.0, 222.0], [34.1, 222.0], [34.2, 222.0], [34.3, 222.0], [34.4, 223.0], [34.5, 223.0], [34.6, 223.0], [34.7, 223.0], [34.8, 223.0], [34.9, 223.0], [35.0, 223.0], [35.1, 223.0], [35.2, 224.0], [35.3, 224.0], [35.4, 224.0], [35.5, 224.0], [35.6, 224.0], [35.7, 224.0], [35.8, 224.0], [35.9, 224.0], [36.0, 225.0], [36.1, 225.0], [36.2, 225.0], [36.3, 225.0], [36.4, 225.0], [36.5, 225.0], [36.6, 225.0], [36.7, 225.0], [36.8, 231.0], [36.9, 231.0], [37.0, 231.0], [37.1, 231.0], [37.2, 231.0], [37.3, 231.0], [37.4, 231.0], [37.5, 231.0], [37.6, 232.0], [37.7, 232.0], [37.8, 232.0], [37.9, 232.0], [38.0, 233.0], [38.1, 233.0], [38.2, 233.0], [38.3, 233.0], [38.4, 233.0], [38.5, 233.0], [38.6, 233.0], [38.7, 233.0], [38.8, 234.0], [38.9, 234.0], [39.0, 234.0], [39.1, 234.0], [39.2, 235.0], [39.3, 235.0], [39.4, 235.0], [39.5, 235.0], [39.6, 235.0], [39.7, 235.0], [39.8, 235.0], [39.9, 235.0], [40.0, 235.0], [40.1, 235.0], [40.2, 235.0], [40.3, 235.0], [40.4, 235.0], [40.5, 235.0], [40.6, 235.0], [40.7, 235.0], [40.8, 236.0], [40.9, 236.0], [41.0, 236.0], [41.1, 236.0], [41.2, 236.0], [41.3, 236.0], [41.4, 236.0], [41.5, 236.0], [41.6, 238.0], [41.7, 238.0], [41.8, 238.0], [41.9, 238.0], [42.0, 240.0], [42.1, 240.0], [42.2, 240.0], [42.3, 240.0], [42.4, 244.0], [42.5, 244.0], [42.6, 244.0], [42.7, 244.0], [42.8, 246.0], [42.9, 246.0], [43.0, 246.0], [43.1, 246.0], [43.2, 249.0], [43.3, 249.0], [43.4, 249.0], [43.5, 249.0], [43.6, 250.0], [43.7, 250.0], [43.8, 250.0], [43.9, 250.0], [44.0, 256.0], [44.1, 256.0], [44.2, 256.0], [44.3, 256.0], [44.4, 258.0], [44.5, 258.0], [44.6, 258.0], [44.7, 258.0], [44.8, 260.0], [44.9, 260.0], [45.0, 260.0], [45.1, 260.0], [45.2, 260.0], [45.3, 260.0], [45.4, 260.0], [45.5, 260.0], [45.6, 261.0], [45.7, 261.0], [45.8, 261.0], [45.9, 261.0], [46.0, 262.0], [46.1, 262.0], [46.2, 262.0], [46.3, 262.0], [46.4, 263.0], [46.5, 263.0], [46.6, 263.0], [46.7, 263.0], [46.8, 265.0], [46.9, 265.0], [47.0, 265.0], [47.1, 265.0], [47.2, 266.0], [47.3, 266.0], [47.4, 266.0], [47.5, 266.0], [47.6, 266.0], [47.7, 266.0], [47.8, 266.0], [47.9, 266.0], [48.0, 267.0], [48.1, 267.0], [48.2, 267.0], [48.3, 267.0], [48.4, 269.0], [48.5, 269.0], [48.6, 269.0], [48.7, 269.0], [48.8, 270.0], [48.9, 270.0], [49.0, 270.0], [49.1, 270.0], [49.2, 270.0], [49.3, 270.0], [49.4, 270.0], [49.5, 270.0], [49.6, 271.0], [49.7, 271.0], [49.8, 271.0], [49.9, 271.0], [50.0, 274.0], [50.1, 274.0], [50.2, 274.0], [50.3, 274.0], [50.4, 274.0], [50.5, 274.0], [50.6, 274.0], [50.7, 274.0], [50.8, 275.0], [50.9, 275.0], [51.0, 275.0], [51.1, 275.0], [51.2, 277.0], [51.3, 277.0], [51.4, 277.0], [51.5, 277.0], [51.6, 278.0], [51.7, 278.0], [51.8, 278.0], [51.9, 278.0], [52.0, 279.0], [52.1, 279.0], [52.2, 279.0], [52.3, 279.0], [52.4, 281.0], [52.5, 281.0], [52.6, 281.0], [52.7, 281.0], [52.8, 282.0], [52.9, 282.0], [53.0, 282.0], [53.1, 282.0], [53.2, 283.0], [53.3, 283.0], [53.4, 283.0], [53.5, 283.0], [53.6, 285.0], [53.7, 285.0], [53.8, 285.0], [53.9, 285.0], [54.0, 288.0], [54.1, 288.0], [54.2, 288.0], [54.3, 288.0], [54.4, 292.0], [54.5, 292.0], [54.6, 292.0], [54.7, 292.0], [54.8, 293.0], [54.9, 293.0], [55.0, 293.0], [55.1, 293.0], [55.2, 295.0], [55.3, 295.0], [55.4, 295.0], [55.5, 295.0], [55.6, 296.0], [55.7, 296.0], [55.8, 296.0], [55.9, 296.0], [56.0, 297.0], [56.1, 297.0], [56.2, 297.0], [56.3, 297.0], [56.4, 298.0], [56.5, 298.0], [56.6, 298.0], [56.7, 298.0], [56.8, 300.0], [56.9, 300.0], [57.0, 300.0], [57.1, 300.0], [57.2, 300.0], [57.3, 300.0], [57.4, 300.0], [57.5, 300.0], [57.6, 301.0], [57.7, 301.0], [57.8, 301.0], [57.9, 301.0], [58.0, 301.0], [58.1, 301.0], [58.2, 301.0], [58.3, 301.0], [58.4, 303.0], [58.5, 303.0], [58.6, 303.0], [58.7, 303.0], [58.8, 304.0], [58.9, 304.0], [59.0, 304.0], [59.1, 304.0], [59.2, 308.0], [59.3, 308.0], [59.4, 308.0], [59.5, 308.0], [59.6, 311.0], [59.7, 311.0], [59.8, 311.0], [59.9, 311.0], [60.0, 311.0], [60.1, 311.0], [60.2, 311.0], [60.3, 311.0], [60.4, 318.0], [60.5, 318.0], [60.6, 318.0], [60.7, 318.0], [60.8, 318.0], [60.9, 318.0], [61.0, 318.0], [61.1, 318.0], [61.2, 318.0], [61.3, 318.0], [61.4, 318.0], [61.5, 318.0], [61.6, 322.0], [61.7, 322.0], [61.8, 322.0], [61.9, 322.0], [62.0, 326.0], [62.1, 326.0], [62.2, 326.0], [62.3, 326.0], [62.4, 335.0], [62.5, 335.0], [62.6, 335.0], [62.7, 335.0], [62.8, 339.0], [62.9, 339.0], [63.0, 339.0], [63.1, 339.0], [63.2, 340.0], [63.3, 340.0], [63.4, 340.0], [63.5, 340.0], [63.6, 341.0], [63.7, 341.0], [63.8, 341.0], [63.9, 341.0], [64.0, 341.0], [64.1, 341.0], [64.2, 341.0], [64.3, 341.0], [64.4, 348.0], [64.5, 348.0], [64.6, 348.0], [64.7, 348.0], [64.8, 360.0], [64.9, 360.0], [65.0, 360.0], [65.1, 360.0], [65.2, 363.0], [65.3, 363.0], [65.4, 363.0], [65.5, 363.0], [65.6, 366.0], [65.7, 366.0], [65.8, 366.0], [65.9, 366.0], [66.0, 374.0], [66.1, 374.0], [66.2, 374.0], [66.3, 374.0], [66.4, 381.0], [66.5, 381.0], [66.6, 381.0], [66.7, 381.0], [66.8, 390.0], [66.9, 390.0], [67.0, 390.0], [67.1, 390.0], [67.2, 408.0], [67.3, 408.0], [67.4, 408.0], [67.5, 408.0], [67.6, 408.0], [67.7, 408.0], [67.8, 408.0], [67.9, 408.0], [68.0, 412.0], [68.1, 412.0], [68.2, 412.0], [68.3, 412.0], [68.4, 421.0], [68.5, 421.0], [68.6, 421.0], [68.7, 421.0], [68.8, 432.0], [68.9, 432.0], [69.0, 432.0], [69.1, 432.0], [69.2, 432.0], [69.3, 432.0], [69.4, 432.0], [69.5, 432.0], [69.6, 443.0], [69.7, 443.0], [69.8, 443.0], [69.9, 443.0], [70.0, 497.0], [70.1, 497.0], [70.2, 497.0], [70.3, 497.0], [70.4, 504.0], [70.5, 504.0], [70.6, 504.0], [70.7, 504.0], [70.8, 615.0], [70.9, 615.0], [71.0, 615.0], [71.1, 615.0], [71.2, 630.0], [71.3, 630.0], [71.4, 630.0], [71.5, 630.0], [71.6, 635.0], [71.7, 635.0], [71.8, 635.0], [71.9, 635.0], [72.0, 646.0], [72.1, 646.0], [72.2, 646.0], [72.3, 646.0], [72.4, 650.0], [72.5, 650.0], [72.6, 650.0], [72.7, 650.0], [72.8, 712.0], [72.9, 712.0], [73.0, 712.0], [73.1, 712.0], [73.2, 812.0], [73.3, 812.0], [73.4, 812.0], [73.5, 812.0], [73.6, 812.0], [73.7, 834.0], [73.8, 834.0], [73.9, 834.0], [74.0, 834.0], [74.1, 836.0], [74.2, 836.0], [74.3, 836.0], [74.4, 836.0], [74.5, 839.0], [74.6, 839.0], [74.7, 839.0], [74.8, 839.0], [74.9, 840.0], [75.0, 840.0], [75.1, 840.0], [75.2, 840.0], [75.3, 841.0], [75.4, 841.0], [75.5, 841.0], [75.6, 841.0], [75.7, 843.0], [75.8, 843.0], [75.9, 843.0], [76.0, 843.0], [76.1, 843.0], [76.2, 843.0], [76.3, 843.0], [76.4, 843.0], [76.5, 844.0], [76.6, 844.0], [76.7, 844.0], [76.8, 844.0], [76.9, 844.0], [77.0, 844.0], [77.1, 844.0], [77.2, 844.0], [77.3, 845.0], [77.4, 845.0], [77.5, 845.0], [77.6, 845.0], [77.7, 850.0], [77.8, 850.0], [77.9, 850.0], [78.0, 850.0], [78.1, 852.0], [78.2, 852.0], [78.3, 852.0], [78.4, 852.0], [78.5, 853.0], [78.6, 853.0], [78.7, 853.0], [78.8, 853.0], [78.9, 856.0], [79.0, 856.0], [79.1, 856.0], [79.2, 856.0], [79.3, 857.0], [79.4, 857.0], [79.5, 857.0], [79.6, 857.0], [79.7, 857.0], [79.8, 857.0], [79.9, 857.0], [80.0, 857.0], [80.1, 857.0], [80.2, 857.0], [80.3, 857.0], [80.4, 857.0], [80.5, 857.0], [80.6, 857.0], [80.7, 857.0], [80.8, 857.0], [80.9, 859.0], [81.0, 859.0], [81.1, 859.0], [81.2, 859.0], [81.3, 860.0], [81.4, 860.0], [81.5, 860.0], [81.6, 860.0], [81.7, 861.0], [81.8, 861.0], [81.9, 861.0], [82.0, 861.0], [82.1, 864.0], [82.2, 864.0], [82.3, 864.0], [82.4, 864.0], [82.5, 869.0], [82.6, 869.0], [82.7, 869.0], [82.8, 869.0], [82.9, 870.0], [83.0, 870.0], [83.1, 870.0], [83.2, 870.0], [83.3, 872.0], [83.4, 872.0], [83.5, 872.0], [83.6, 872.0], [83.7, 873.0], [83.8, 873.0], [83.9, 873.0], [84.0, 873.0], [84.1, 875.0], [84.2, 875.0], [84.3, 875.0], [84.4, 875.0], [84.5, 881.0], [84.6, 881.0], [84.7, 881.0], [84.8, 881.0], [84.9, 882.0], [85.0, 882.0], [85.1, 882.0], [85.2, 882.0], [85.3, 883.0], [85.4, 883.0], [85.5, 883.0], [85.6, 883.0], [85.7, 883.0], [85.8, 883.0], [85.9, 883.0], [86.0, 883.0], [86.1, 887.0], [86.2, 887.0], [86.3, 887.0], [86.4, 887.0], [86.5, 890.0], [86.6, 890.0], [86.7, 890.0], [86.8, 890.0], [86.9, 890.0], [87.0, 890.0], [87.1, 890.0], [87.2, 890.0], [87.3, 909.0], [87.4, 909.0], [87.5, 909.0], [87.6, 909.0], [87.7, 930.0], [87.8, 930.0], [87.9, 930.0], [88.0, 930.0], [88.1, 935.0], [88.2, 935.0], [88.3, 935.0], [88.4, 935.0], [88.5, 951.0], [88.6, 951.0], [88.7, 951.0], [88.8, 951.0], [88.9, 953.0], [89.0, 953.0], [89.1, 953.0], [89.2, 953.0], [89.3, 953.0], [89.4, 953.0], [89.5, 953.0], [89.6, 953.0], [89.7, 956.0], [89.8, 956.0], [89.9, 956.0], [90.0, 956.0], [90.1, 960.0], [90.2, 960.0], [90.3, 960.0], [90.4, 960.0], [90.5, 961.0], [90.6, 961.0], [90.7, 961.0], [90.8, 961.0], [90.9, 961.0], [91.0, 961.0], [91.1, 961.0], [91.2, 961.0], [91.3, 962.0], [91.4, 962.0], [91.5, 962.0], [91.6, 962.0], [91.7, 962.0], [91.8, 962.0], [91.9, 962.0], [92.0, 962.0], [92.1, 963.0], [92.2, 963.0], [92.3, 963.0], [92.4, 963.0], [92.5, 965.0], [92.6, 965.0], [92.7, 965.0], [92.8, 965.0], [92.9, 965.0], [93.0, 965.0], [93.1, 965.0], [93.2, 965.0], [93.3, 965.0], [93.4, 965.0], [93.5, 965.0], [93.6, 965.0], [93.7, 967.0], [93.8, 967.0], [93.9, 967.0], [94.0, 967.0], [94.1, 968.0], [94.2, 968.0], [94.3, 968.0], [94.4, 968.0], [94.5, 969.0], [94.6, 969.0], [94.7, 969.0], [94.8, 969.0], [94.9, 972.0], [95.0, 972.0], [95.1, 972.0], [95.2, 972.0], [95.3, 981.0], [95.4, 981.0], [95.5, 981.0], [95.6, 981.0], [95.7, 981.0], [95.8, 981.0], [95.9, 981.0], [96.0, 981.0], [96.1, 1000.0], [96.2, 1000.0], [96.3, 1000.0], [96.4, 1000.0], [96.5, 1018.0], [96.6, 1018.0], [96.7, 1018.0], [96.8, 1018.0], [96.9, 1020.0], [97.0, 1020.0], [97.1, 1020.0], [97.2, 1020.0], [97.3, 1028.0], [97.4, 1028.0], [97.5, 1028.0], [97.6, 1028.0], [97.7, 1035.0], [97.8, 1035.0], [97.9, 1035.0], [98.0, 1035.0], [98.1, 1073.0], [98.2, 1073.0], [98.3, 1073.0], [98.4, 1073.0], [98.5, 1169.0], [98.6, 1169.0], [98.7, 1169.0], [98.8, 1169.0], [98.9, 1343.0], [99.0, 1343.0], [99.1, 1343.0], [99.2, 1343.0], [99.3, 1703.0], [99.4, 1703.0], [99.5, 1703.0], [99.6, 1703.0], [99.7, 1704.0], [99.8, 1704.0], [99.9, 1704.0], [100.0, 1704.0]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "maxX": 100.0, "title": "Response Time Percentiles"}},
        getOptions: function() {
            return {
                series: {
                    points: { show: false }
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimePercentiles'
                },
                xaxis: {
                    tickDecimals: 1,
                    axisLabel: "Percentiles",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Percentile value in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : %x.2 percentile was %y ms"
                },
                selection: { mode: "xy" },
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimePercentiles"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimesPercentiles"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimesPercentiles"), dataset, prepareOverviewOptions(options));
        }
};

/**
 * @param elementId Id of element where we display message
 */
function setEmptyGraph(elementId) {
    $(function() {
        $(elementId).text("No graph series with filter="+seriesFilter);
    });
}

// Response times percentiles
function refreshResponseTimePercentiles() {
    var infos = responseTimePercentilesInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimePercentiles");
        return;
    }
    if (isGraph($("#flotResponseTimesPercentiles"))){
        infos.createGraph();
    } else {
        var choiceContainer = $("#choicesResponseTimePercentiles");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimesPercentiles", "#overviewResponseTimesPercentiles");
        $('#bodyResponseTimePercentiles .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var responseTimeDistributionInfos = {
        data: {"result": {"minY": 1.0, "minX": 100.0, "maxY": 98.0, "series": [{"data": [[600.0, 5.0], [700.0, 1.0], [800.0, 35.0], [200.0, 98.0], [900.0, 22.0], [1000.0, 6.0], [1100.0, 1.0], [300.0, 26.0], [1300.0, 1.0], [400.0, 8.0], [100.0, 44.0], [1700.0, 2.0], [500.0, 1.0]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 100, "maxX": 1700.0, "title": "Response Time Distribution"}},
        getOptions: function() {
            var granularity = this.data.result.granularity;
            return {
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimeDistribution'
                },
                xaxis:{
                    axisLabel: "Response times in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of responses",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                bars : {
                    show: true,
                    barWidth: this.data.result.granularity
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: function(label, xval, yval, flotItem){
                        return yval + " responses for " + label + " were between " + xval + " and " + (xval + granularity) + " ms";
                    }
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimeDistribution"), prepareData(data.result.series, $("#choicesResponseTimeDistribution")), options);
        }

};

// Response time distribution
function refreshResponseTimeDistribution() {
    var infos = responseTimeDistributionInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimeDistribution");
        return;
    }
    if (isGraph($("#flotResponseTimeDistribution"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimeDistribution");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        $('#footerResponseTimeDistribution .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var syntheticResponseTimeDistributionInfos = {
        data: {"result": {"minY": 2.0, "minX": 0.0, "ticks": [[0, "Requests having \nresponse time <= 500ms"], [1, "Requests having \nresponse time > 500ms and <= 1,500ms"], [2, "Requests having \nresponse time > 1,500ms"], [3, "Requests in error"]], "maxY": 176.0, "series": [{"data": [[0.0, 176.0]], "color": "#9ACD32", "isOverall": false, "label": "Requests having \nresponse time <= 500ms", "isController": false}, {"data": [[1.0, 72.0]], "color": "yellow", "isOverall": false, "label": "Requests having \nresponse time > 500ms and <= 1,500ms", "isController": false}, {"data": [[2.0, 2.0]], "color": "orange", "isOverall": false, "label": "Requests having \nresponse time > 1,500ms", "isController": false}, {"data": [], "color": "#FF6347", "isOverall": false, "label": "Requests in error", "isController": false}], "supportsControllersDiscrimination": false, "maxX": 2.0, "title": "Synthetic Response Times Distribution"}},
        getOptions: function() {
            return {
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendSyntheticResponseTimeDistribution'
                },
                xaxis:{
                    axisLabel: "Response times ranges",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                    tickLength:0,
                    min:-0.5,
                    max:3.5
                },
                yaxis: {
                    axisLabel: "Number of responses",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                bars : {
                    show: true,
                    align: "center",
                    barWidth: 0.25,
                    fill:.75
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: function(label, xval, yval, flotItem){
                        return yval + " " + label;
                    }
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var options = this.getOptions();
            prepareOptions(options, data);
            options.xaxis.ticks = data.result.ticks;
            $.plot($("#flotSyntheticResponseTimeDistribution"), prepareData(data.result.series, $("#choicesSyntheticResponseTimeDistribution")), options);
        }

};

// Response time distribution
function refreshSyntheticResponseTimeDistribution() {
    var infos = syntheticResponseTimeDistributionInfos;
    prepareSeries(infos.data, true);
    if (isGraph($("#flotSyntheticResponseTimeDistribution"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesSyntheticResponseTimeDistribution");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        $('#footerSyntheticResponseTimeDistribution .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var activeThreadsOverTimeInfos = {
        data: {"result": {"minY": 12.036000000000005, "minX": 1.79060328E12, "maxY": 12.036000000000005, "series": [{"data": [[1.79060328E12, 12.036000000000005]], "isOverall": false, "label": "Thread Group", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.79060328E12, "title": "Active Threads Over Time"}},
        getOptions: function() {
            return {
                series: {
                    stack: true,
                    lines: {
                        show: true,
                        fill: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of active threads",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 6,
                    show: true,
                    container: '#legendActiveThreadsOverTime'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                selection: {
                    mode: 'xy'
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : At %x there were %y active threads"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesActiveThreadsOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotActiveThreadsOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewActiveThreadsOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Active Threads Over Time
function refreshActiveThreadsOverTime(fixTimestamps) {
    var infos = activeThreadsOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 3600000);
    }
    if(isGraph($("#flotActiveThreadsOverTime"))) {
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesActiveThreadsOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotActiveThreadsOverTime", "#overviewActiveThreadsOverTime");
        $('#footerActiveThreadsOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var timeVsThreadsInfos = {
        data: {"result": {"minY": 205.0, "minX": 1.0, "maxY": 722.3333333333334, "series": [{"data": [[8.0, 321.57142857142856], [2.0, 217.0], [9.0, 299.77777777777777], [10.0, 604.0000000000001], [11.0, 424.3076923076923], [12.0, 409.50000000000006], [3.0, 432.0], [13.0, 620.875], [14.0, 722.3333333333334], [15.0, 619.375], [16.0, 611.8181818181819], [4.0, 320.5], [1.0, 255.5], [17.0, 518.6], [18.0, 432.28571428571433], [19.0, 682.0833333333335], [20.0, 603.1764705882354], [5.0, 272.0], [21.0, 441.85714285714283], [22.0, 205.0], [6.0, 273.6666666666667], [7.0, 273.81818181818176]], "isOverall": false, "label": "HTTP Request", "isController": false}, {"data": [[12.036000000000005, 444.61200000000014]], "isOverall": false, "label": "HTTP Request-Aggregated", "isController": false}], "supportsControllersDiscrimination": true, "maxX": 22.0, "title": "Time VS Threads"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    axisLabel: "Number of active threads",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response times in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: { noColumns: 2,show: true, container: '#legendTimeVsThreads' },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s: At %x.2 active threads, Average response time was %y.2 ms"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesTimeVsThreads"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotTimesVsThreads"), dataset, options);
            // setup overview
            $.plot($("#overviewTimesVsThreads"), dataset, prepareOverviewOptions(options));
        }
};

// Time vs threads
function refreshTimeVsThreads(){
    var infos = timeVsThreadsInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyTimeVsThreads");
        return;
    }
    if(isGraph($("#flotTimesVsThreads"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTimeVsThreads");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTimesVsThreads", "#overviewTimesVsThreads");
        $('#footerTimeVsThreads .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var bytesThroughputOverTimeInfos = {
        data : {"result": {"minY": 566.6666666666666, "minX": 1.79060328E12, "maxY": 1596838.0, "series": [{"data": [[1.79060328E12, 1596838.0]], "isOverall": false, "label": "Bytes received per second", "isController": false}, {"data": [[1.79060328E12, 566.6666666666666]], "isOverall": false, "label": "Bytes sent per second", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.79060328E12, "title": "Bytes Throughput Over Time"}},
        getOptions : function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity) ,
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Bytes / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendBytesThroughputOverTime'
                },
                selection: {
                    mode: "xy"
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y"
                }
            };
        },
        createGraph : function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesBytesThroughputOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotBytesThroughputOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewBytesThroughputOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Bytes throughput Over Time
function refreshBytesThroughputOverTime(fixTimestamps) {
    var infos = bytesThroughputOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 3600000);
    }
    if(isGraph($("#flotBytesThroughputOverTime"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesBytesThroughputOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotBytesThroughputOverTime", "#overviewBytesThroughputOverTime");
        $('#footerBytesThroughputOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var responseTimesOverTimeInfos = {
        data: {"result": {"minY": 444.61200000000014, "minX": 1.79060328E12, "maxY": 444.61200000000014, "series": [{"data": [[1.79060328E12, 444.61200000000014]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.79060328E12, "title": "Response Time Over Time"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average response time was %y ms"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Response Times Over Time
function refreshResponseTimeOverTime(fixTimestamps) {
    var infos = responseTimesOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimeOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 3600000);
    }
    if(isGraph($("#flotResponseTimesOverTime"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimesOverTime", "#overviewResponseTimesOverTime");
        $('#footerResponseTimesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var latenciesOverTimeInfos = {
        data: {"result": {"minY": 304.16399999999976, "minX": 1.79060328E12, "maxY": 304.16399999999976, "series": [{"data": [[1.79060328E12, 304.16399999999976]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.79060328E12, "title": "Latencies Over Time"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response latencies in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendLatenciesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average latency was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesLatenciesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotLatenciesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewLatenciesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Latencies Over Time
function refreshLatenciesOverTime(fixTimestamps) {
    var infos = latenciesOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyLatenciesOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 3600000);
    }
    if(isGraph($("#flotLatenciesOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesLatenciesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotLatenciesOverTime", "#overviewLatenciesOverTime");
        $('#footerLatenciesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var connectTimeOverTimeInfos = {
        data: {"result": {"minY": 19.919999999999995, "minX": 1.79060328E12, "maxY": 19.919999999999995, "series": [{"data": [[1.79060328E12, 19.919999999999995]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.79060328E12, "title": "Connect Time Over Time"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getConnectTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average Connect Time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendConnectTimeOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average connect time was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesConnectTimeOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotConnectTimeOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewConnectTimeOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Connect Time Over Time
function refreshConnectTimeOverTime(fixTimestamps) {
    var infos = connectTimeOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyConnectTimeOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 3600000);
    }
    if(isGraph($("#flotConnectTimeOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesConnectTimeOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotConnectTimeOverTime", "#overviewConnectTimeOverTime");
        $('#footerConnectTimeOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var responseTimePercentilesOverTimeInfos = {
        data: {"result": {"minY": 173.0, "minX": 1.79060328E12, "maxY": 1704.0, "series": [{"data": [[1.79060328E12, 1704.0]], "isOverall": false, "label": "Max", "isController": false}, {"data": [[1.79060328E12, 959.6]], "isOverall": false, "label": "90th percentile", "isController": false}, {"data": [[1.79060328E12, 1519.4000000000033]], "isOverall": false, "label": "99th percentile", "isController": false}, {"data": [[1.79060328E12, 976.05]], "isOverall": false, "label": "95th percentile", "isController": false}, {"data": [[1.79060328E12, 173.0]], "isOverall": false, "label": "Min", "isController": false}, {"data": [[1.79060328E12, 272.5]], "isOverall": false, "label": "Median", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.79060328E12, "title": "Response Time Percentiles Over Time (successful requests only)"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true,
                        fill: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Response Time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimePercentilesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Response time was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimePercentilesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimePercentilesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimePercentilesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Response Time Percentiles Over Time
function refreshResponseTimePercentilesOverTime(fixTimestamps) {
    var infos = responseTimePercentilesOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 3600000);
    }
    if(isGraph($("#flotResponseTimePercentilesOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesResponseTimePercentilesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimePercentilesOverTime", "#overviewResponseTimePercentilesOverTime");
        $('#footerResponseTimePercentilesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var responseTimeVsRequestInfos = {
    data: {"result": {"minY": 221.0, "minX": 1.0, "maxY": 1073.0, "series": [{"data": [[1.0, 1073.0], [33.0, 857.0], [37.0, 381.0], [40.0, 229.5], [20.0, 277.0], [11.0, 890.0], [22.0, 837.5], [24.0, 267.0], [27.0, 221.0], [28.0, 223.5], [7.0, 301.0]], "isOverall": false, "label": "Successes", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 1000, "maxX": 40.0, "title": "Response Time Vs Request"}},
    getOptions: function() {
        return {
            series: {
                lines: {
                    show: false
                },
                points: {
                    show: true
                }
            },
            xaxis: {
                axisLabel: "Global number of requests per second",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            yaxis: {
                axisLabel: "Median Response Time in ms",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            legend: {
                noColumns: 2,
                show: true,
                container: '#legendResponseTimeVsRequest'
            },
            selection: {
                mode: 'xy'
            },
            grid: {
                hoverable: true // IMPORTANT! this is needed for tooltip to work
            },
            tooltip: true,
            tooltipOpts: {
                content: "%s : Median response time at %x req/s was %y ms"
            },
            colors: ["#9ACD32", "#FF6347"]
        };
    },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesResponseTimeVsRequest"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotResponseTimeVsRequest"), dataset, options);
        // setup overview
        $.plot($("#overviewResponseTimeVsRequest"), dataset, prepareOverviewOptions(options));

    }
};

// Response Time vs Request
function refreshResponseTimeVsRequest() {
    var infos = responseTimeVsRequestInfos;
    prepareSeries(infos.data);
    if (isGraph($("#flotResponseTimeVsRequest"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimeVsRequest");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimeVsRequest", "#overviewResponseTimeVsRequest");
        $('#footerResponseRimeVsRequest .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var latenciesVsRequestInfos = {
    data: {"result": {"minY": 176.0, "minX": 1.0, "maxY": 853.0, "series": [{"data": [[1.0, 853.0], [33.0, 507.0], [37.0, 258.0], [40.0, 190.5], [20.0, 198.0], [11.0, 542.0], [22.0, 445.0], [24.0, 190.5], [27.0, 176.0], [28.0, 178.0], [7.0, 248.0]], "isOverall": false, "label": "Successes", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 1000, "maxX": 40.0, "title": "Latencies Vs Request"}},
    getOptions: function() {
        return{
            series: {
                lines: {
                    show: false
                },
                points: {
                    show: true
                }
            },
            xaxis: {
                axisLabel: "Global number of requests per second",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            yaxis: {
                axisLabel: "Median Latency in ms",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            legend: { noColumns: 2,show: true, container: '#legendLatencyVsRequest' },
            selection: {
                mode: 'xy'
            },
            grid: {
                hoverable: true // IMPORTANT! this is needed for tooltip to work
            },
            tooltip: true,
            tooltipOpts: {
                content: "%s : Median Latency time at %x req/s was %y ms"
            },
            colors: ["#9ACD32", "#FF6347"]
        };
    },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesLatencyVsRequest"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotLatenciesVsRequest"), dataset, options);
        // setup overview
        $.plot($("#overviewLatenciesVsRequest"), dataset, prepareOverviewOptions(options));
    }
};

// Latencies vs Request
function refreshLatenciesVsRequest() {
        var infos = latenciesVsRequestInfos;
        prepareSeries(infos.data);
        if(isGraph($("#flotLatenciesVsRequest"))){
            infos.createGraph();
        }else{
            var choiceContainer = $("#choicesLatencyVsRequest");
            createLegend(choiceContainer, infos);
            infos.createGraph();
            setGraphZoomable("#flotLatenciesVsRequest", "#overviewLatenciesVsRequest");
            $('#footerLatenciesVsRequest .legendColorBox > div').each(function(i){
                $(this).clone().prependTo(choiceContainer.find("li").eq(i));
            });
        }
};

var hitsPerSecondInfos = {
        data: {"result": {"minY": 4.166666666666667, "minX": 1.79060328E12, "maxY": 4.166666666666667, "series": [{"data": [[1.79060328E12, 4.166666666666667]], "isOverall": false, "label": "hitsPerSecond", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.79060328E12, "title": "Hits Per Second"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of hits / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendHitsPerSecond"
                },
                selection: {
                    mode : 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y.2 hits/sec"
                }
            };
        },
        createGraph: function createGraph() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesHitsPerSecond"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotHitsPerSecond"), dataset, options);
            // setup overview
            $.plot($("#overviewHitsPerSecond"), dataset, prepareOverviewOptions(options));
        }
};

// Hits per second
function refreshHitsPerSecond(fixTimestamps) {
    var infos = hitsPerSecondInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 3600000);
    }
    if (isGraph($("#flotHitsPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesHitsPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotHitsPerSecond", "#overviewHitsPerSecond");
        $('#footerHitsPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var codesPerSecondInfos = {
        data: {"result": {"minY": 4.166666666666667, "minX": 1.79060328E12, "maxY": 4.166666666666667, "series": [{"data": [[1.79060328E12, 4.166666666666667]], "isOverall": false, "label": "200", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.79060328E12, "title": "Codes Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of responses / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendCodesPerSecond"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "Number of Response Codes %s at %x was %y.2 responses / sec"
                }
            };
        },
    createGraph: function() {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesCodesPerSecond"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotCodesPerSecond"), dataset, options);
        // setup overview
        $.plot($("#overviewCodesPerSecond"), dataset, prepareOverviewOptions(options));
    }
};

// Codes per second
function refreshCodesPerSecond(fixTimestamps) {
    var infos = codesPerSecondInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 3600000);
    }
    if(isGraph($("#flotCodesPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesCodesPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotCodesPerSecond", "#overviewCodesPerSecond");
        $('#footerCodesPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var transactionsPerSecondInfos = {
        data: {"result": {"minY": 4.166666666666667, "minX": 1.79060328E12, "maxY": 4.166666666666667, "series": [{"data": [[1.79060328E12, 4.166666666666667]], "isOverall": false, "label": "HTTP Request-success", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.79060328E12, "title": "Transactions Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of transactions / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendTransactionsPerSecond"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y transactions / sec"
                }
            };
        },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesTransactionsPerSecond"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotTransactionsPerSecond"), dataset, options);
        // setup overview
        $.plot($("#overviewTransactionsPerSecond"), dataset, prepareOverviewOptions(options));
    }
};

// Transactions per second
function refreshTransactionsPerSecond(fixTimestamps) {
    var infos = transactionsPerSecondInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyTransactionsPerSecond");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 3600000);
    }
    if(isGraph($("#flotTransactionsPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTransactionsPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTransactionsPerSecond", "#overviewTransactionsPerSecond");
        $('#footerTransactionsPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var totalTPSInfos = {
        data: {"result": {"minY": 4.166666666666667, "minX": 1.79060328E12, "maxY": 4.166666666666667, "series": [{"data": [[1.79060328E12, 4.166666666666667]], "isOverall": false, "label": "Transaction-success", "isController": false}, {"data": [], "isOverall": false, "label": "Transaction-failure", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.79060328E12, "title": "Total Transactions Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of transactions / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendTotalTPS"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y transactions / sec"
                },
                colors: ["#9ACD32", "#FF6347"]
            };
        },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesTotalTPS"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotTotalTPS"), dataset, options);
        // setup overview
        $.plot($("#overviewTotalTPS"), dataset, prepareOverviewOptions(options));
    }
};

// Total Transactions per second
function refreshTotalTPS(fixTimestamps) {
    var infos = totalTPSInfos;
    // We want to ignore seriesFilter
    prepareSeries(infos.data, false, true);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 3600000);
    }
    if(isGraph($("#flotTotalTPS"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTotalTPS");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTotalTPS", "#overviewTotalTPS");
        $('#footerTotalTPS .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

// Collapse the graph matching the specified DOM element depending the collapsed
// status
function collapse(elem, collapsed){
    if(collapsed){
        $(elem).parent().find(".fa-chevron-up").removeClass("fa-chevron-up").addClass("fa-chevron-down");
    } else {
        $(elem).parent().find(".fa-chevron-down").removeClass("fa-chevron-down").addClass("fa-chevron-up");
        if (elem.id == "bodyBytesThroughputOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshBytesThroughputOverTime(true);
            }
            document.location.href="#bytesThroughputOverTime";
        } else if (elem.id == "bodyLatenciesOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshLatenciesOverTime(true);
            }
            document.location.href="#latenciesOverTime";
        } else if (elem.id == "bodyCustomGraph") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshCustomGraph(true);
            }
            document.location.href="#responseCustomGraph";
        } else if (elem.id == "bodyConnectTimeOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshConnectTimeOverTime(true);
            }
            document.location.href="#connectTimeOverTime";
        } else if (elem.id == "bodyResponseTimePercentilesOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimePercentilesOverTime(true);
            }
            document.location.href="#responseTimePercentilesOverTime";
        } else if (elem.id == "bodyResponseTimeDistribution") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimeDistribution();
            }
            document.location.href="#responseTimeDistribution" ;
        } else if (elem.id == "bodySyntheticResponseTimeDistribution") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshSyntheticResponseTimeDistribution();
            }
            document.location.href="#syntheticResponseTimeDistribution" ;
        } else if (elem.id == "bodyActiveThreadsOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshActiveThreadsOverTime(true);
            }
            document.location.href="#activeThreadsOverTime";
        } else if (elem.id == "bodyTimeVsThreads") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTimeVsThreads();
            }
            document.location.href="#timeVsThreads" ;
        } else if (elem.id == "bodyCodesPerSecond") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshCodesPerSecond(true);
            }
            document.location.href="#codesPerSecond";
        } else if (elem.id == "bodyTransactionsPerSecond") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTransactionsPerSecond(true);
            }
            document.location.href="#transactionsPerSecond";
        } else if (elem.id == "bodyTotalTPS") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTotalTPS(true);
            }
            document.location.href="#totalTPS";
        } else if (elem.id == "bodyResponseTimeVsRequest") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimeVsRequest();
            }
            document.location.href="#responseTimeVsRequest";
        } else if (elem.id == "bodyLatenciesVsRequest") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshLatenciesVsRequest();
            }
            document.location.href="#latencyVsRequest";
        }
    }
}

/*
 * Activates or deactivates all series of the specified graph (represented by id parameter)
 * depending on checked argument.
 */
function toggleAll(id, checked){
    var placeholder = document.getElementById(id);

    var cases = $(placeholder).find(':checkbox');
    cases.prop('checked', checked);
    $(cases).parent().children().children().toggleClass("legend-disabled", !checked);

    var choiceContainer;
    if ( id == "choicesBytesThroughputOverTime"){
        choiceContainer = $("#choicesBytesThroughputOverTime");
        refreshBytesThroughputOverTime(false);
    } else if(id == "choicesResponseTimesOverTime"){
        choiceContainer = $("#choicesResponseTimesOverTime");
        refreshResponseTimeOverTime(false);
    }else if(id == "choicesResponseCustomGraph"){
        choiceContainer = $("#choicesResponseCustomGraph");
        refreshCustomGraph(false);
    } else if ( id == "choicesLatenciesOverTime"){
        choiceContainer = $("#choicesLatenciesOverTime");
        refreshLatenciesOverTime(false);
    } else if ( id == "choicesConnectTimeOverTime"){
        choiceContainer = $("#choicesConnectTimeOverTime");
        refreshConnectTimeOverTime(false);
    } else if ( id == "choicesResponseTimePercentilesOverTime"){
        choiceContainer = $("#choicesResponseTimePercentilesOverTime");
        refreshResponseTimePercentilesOverTime(false);
    } else if ( id == "choicesResponseTimePercentiles"){
        choiceContainer = $("#choicesResponseTimePercentiles");
        refreshResponseTimePercentiles();
    } else if(id == "choicesActiveThreadsOverTime"){
        choiceContainer = $("#choicesActiveThreadsOverTime");
        refreshActiveThreadsOverTime(false);
    } else if ( id == "choicesTimeVsThreads"){
        choiceContainer = $("#choicesTimeVsThreads");
        refreshTimeVsThreads();
    } else if ( id == "choicesSyntheticResponseTimeDistribution"){
        choiceContainer = $("#choicesSyntheticResponseTimeDistribution");
        refreshSyntheticResponseTimeDistribution();
    } else if ( id == "choicesResponseTimeDistribution"){
        choiceContainer = $("#choicesResponseTimeDistribution");
        refreshResponseTimeDistribution();
    } else if ( id == "choicesHitsPerSecond"){
        choiceContainer = $("#choicesHitsPerSecond");
        refreshHitsPerSecond(false);
    } else if(id == "choicesCodesPerSecond"){
        choiceContainer = $("#choicesCodesPerSecond");
        refreshCodesPerSecond(false);
    } else if ( id == "choicesTransactionsPerSecond"){
        choiceContainer = $("#choicesTransactionsPerSecond");
        refreshTransactionsPerSecond(false);
    } else if ( id == "choicesTotalTPS"){
        choiceContainer = $("#choicesTotalTPS");
        refreshTotalTPS(false);
    } else if ( id == "choicesResponseTimeVsRequest"){
        choiceContainer = $("#choicesResponseTimeVsRequest");
        refreshResponseTimeVsRequest();
    } else if ( id == "choicesLatencyVsRequest"){
        choiceContainer = $("#choicesLatencyVsRequest");
        refreshLatenciesVsRequest();
    }
    var color = checked ? "black" : "#818181";
    if(choiceContainer != null) {
        choiceContainer.find("label").each(function(){
            this.style.color = color;
        });
    }
}

