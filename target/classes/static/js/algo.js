
const submit = $("#submit");
const err = $("#error");
const err2 = $("#error2");
const table = $("#portfolio");
const buttons = $("#buttons");
const refresh = $("#refresh");
const rslts = $("#results");
const addButton = $("#add_button");
const subButton = $("#sub_button");
const inputArea = $("#input_area");

const input1 = $("#downsideProb");
const input2 = $("#downsideLower");
const input3 = $("#downsideUpper");
const input4 = $("#midrangeProb");
const input5 = $("#midrangeLower");
const input6 = $("#midrangeUpper");
const input7 = $("#upsideProb");
const input8 = $("#upsideLower");
const input9 = $("#upsideUpper");

const company = $("#companyname");


let chart;
let portSize = 0;




$(document).ready(() => {

	company.change(function() {
		// currPrice = document.getElementById("currPrice").value.trim();
	  // interest = document.getElementById("interest").value.trim();
	  // volatility = document.getElementById("volatility").value.trim();
		// time = document.getElementById("time").value.trim();
		companyName = document.getElementById("companyname").value.trim();
		err.text("");

		const postParameters = {companyName : companyName};

		$.post("/company", postParameters, responseJSON => {
			const responseObject = JSON.parse(responseJSON);
			let price = responseObject.price;
			if (price < 0.0){
				err.text("Invalid Stock Ticker");
			} else {
				document.getElementById("currPrice").value = price;
			}

			let vol = responseObject.volatility;
			if (vol < 0.0){
				err.text("Invalid Stock Ticker");
			} else {
				document.getElementById("volatility").value = vol;
			}
			if (err.text()===""){
				let lower = price - vol/Math.sqrt(252)*price;
				let higher = price + vol/Math.sqrt(252)*price;

				document.getElementById("midrangeLower").value = lower.toString();
				document.getElementById("midrangeUpper").value = higher.toString();


				document.getElementById("interest").value = 0.001;
				document.getElementById("time").value = 1.0;
			}
		});

	});

	input1.change(function() {
		if (document.getElementById("downsideProb").value === "zero"){
			document.getElementById("downsideLower").value = document.getElementById("downsideLower").placeholder;
			document.getElementById("downsideUpper").value = document.getElementById("downsideUpper").placeholder;
		}
		drawLine();

	});

	input2.change(function() {
		if (document.getElementById("downsideProb").value === "zero") {
			document.getElementById("downsideProb").value = "three";
		}
		drawLine();
	});

	input3.change(function() {
		if (document.getElementById("midrangeProb").value != "zero"){
			document.getElementById("midrangeLower").value = document.getElementById("downsideUpper").value;
			if (document.getElementById("downsideProb").value === "zero") {
				document.getElementById("downsideProb").value = "three";
			}
		}
		drawLine();

	});

	input4.change(function() {
		if (document.getElementById("midrangeProb").value === "zero"){
			document.getElementById("midrangeLower").value = "Range Lower Limit";
			document.getElementById("midrangeUpper").value = "Range Upper Limit";
		}
		drawLine();

	});

	input5.change(function() {
		if (document.getElementById("downsideProb").value != "zero"){
			document.getElementById("downsideUpper").value = document.getElementById("midrangeLower").value;
			if (document.getElementById("midrangeProb").value === "zero") {
				document.getElementById("midrangeProb").value = "three";
			}
		}
		drawLine();

	});

	input6.change(function() {
		if (document.getElementById("upsideProb").value != "zero"){
			document.getElementById("upsideLower").value = document.getElementById("midrangeUpper").value;
			if (document.getElementById("midrangeProb").value === "zero") {
				document.getElementById("midrangeProb").value = "three";
			}
		}
		drawLine();

	});

	input7.change(function() {
		if (document.getElementById("upsideProb").value === "zero"){
			document.getElementById("upsideLower").value = document.getElementById("upsideLower").placeholder;
			document.getElementById("upsideUpper").value = document.getElementById("upsideUpper").placeholder;
		}
		drawLine();
	});

	input8.change(function() {
		if (document.getElementById("midrangeProb").value != "zero"){
			document.getElementById("midrangeUpper").value = document.getElementById("upsideLower").value;
			if (document.getElementById("upsideProb").value === "zero") {
				document.getElementById("upsideProb").value = "three";
			}
		}
		drawLine();

	});

	input9.change(function() {
		if (document.getElementById("upsideProb").value === "zero") {
			document.getElementById("upsideProb").value = "three";
		}
		drawLine();

	});

	submit.click(event1 => {
		currText1 = document.getElementById("companyname").value.trim();
    currText2 = document.getElementById("currPrice").value.trim();
    currText3 = document.getElementById("interest").value.trim();
    currText4 = document.getElementById("volatility").value.trim();
		currText5 = document.getElementById("time").value.trim();

		let input1 = document.getElementById("downsideProb").value;
		let input2 = document.getElementById("downsideLower").value;
		let input3 = document.getElementById("downsideUpper").value;
		let input4 = document.getElementById("midrangeProb").value;
		let input5 = document.getElementById("midrangeLower").value;
		let input6 = document.getElementById("midrangeUpper").value;
		let input7 = document.getElementById("upsideProb").value;
		let input8 = document.getElementById("upsideLower").value;
		let input9 = document.getElementById("upsideUpper").value;

		if(!checkDouble(currText2) || !checkDouble(currText3) || !checkDouble(currText4) || !checkDouble(currText5)){
			err.text("Ensure everything is a properly-formatted decimal");
		}
		else if (((!checkDouble(input2) || !checkDouble(input3)) && input1 !="zero")  || !checkDouble(input5) || !checkDouble(input6) || ((!checkDouble(input8) || !checkDouble(input9)) && input7 !="zero")){
			err.text("Ensure everything is a properly-formatted decimal");
		}
		else {
			err.text("");
			if (input2 === "") {
				input2 = "0";
			}
			if (input3 === "") {
				input3 = "0";
			}
			if (input5 === "") {
				input5 = "0";
			}
			if (input6 === "") {
				input6 = "0";
			}
			if (input8 === "") {
				input8 = "0";
			}
			if (input9 === "") {
				input9 = "0";
			}
			let inputs = "" + input1 + "," + input2 + "," + input3 + "," + input4 + "," + input5 + "," + input6 + "," + input7 + "," + input8 + "," + input9;
			rslts.css("visibility", "visible");
			rslts.css("height", "100%");

			const postParameters = {companyname : currText1, currPrice : currText2
							, interest : currText3,
							volatility : currText4,
							time : currText5,
							inpts: inputs};

			$.post("/search", postParameters, responseJSON => {
				const responseObject = JSON.parse(responseJSON);
				let portfolio = responseObject.portfolio;
				portSize = portfolio.length;
				let graphPoints = responseObject.graphPoints;
				drawGraph(graphPoints);
				table.html(buildTable(portfolio));
				if (portSize > 0){
					buttons.css("visibility", "visible");

				} else {
					buttons.css("visibility", "hidden");
				}

				// table.each(function(){
				//   $(this).find('td').each(function(){
				// 		let curr = $(this);
				// 		let currHtml = curr.html();
			  //     //console.log($("#buySell").val());
				// 		console.log(currHtml);
				// 		curr.html("hallo");
				//
				//   })
				// })
				document.getElementById("results").scrollIntoView({behavior: "smooth"});
			});
		}
	});


	refresh.click(event1 => {

		//getvalues
		let newList = "";
		let i;
		for (i = 0; i < portSize; i++) {
			let bs = document.getElementById("buySell"+i).value;
			let cp = document.getElementById("callPut"+i).value;
			let s = document.getElementById("strk"+i).value;
			let p = document.getElementById("prem"+i).value;
			if(!checkDouble(s) || !checkDouble(p)){
				err2.text("Ensure everything is a properly-formatted decimal");
				break;
			}
			else {
				err2.text("");
				newList = newList + bs + "," + cp + "," + s + "," + p + ",";

			}
		}
		if (err2.text() === ""){
			const postParameters = {portfolio : newList};


			$.post("/refresh", postParameters, responseJSON => {
				const responseObject = JSON.parse(responseJSON);
				let graphPoints = responseObject.graphPoints;
				drawGraph(graphPoints);
			});
		}
	});

	addButton.click(event1 => {
		var table = document.getElementById("portfolio");
		// let tableHtml = table.html();
		// tableHtml = tableHtml.substring(0, tableHtml.length - 8) + "<tr>";
		// tableHtml = tableHtml + "<td><select name=\"buySell\" id=\"buySell"+portSize+"\"><option value=\"buy\" selected>Buy</option><option value=\"sell\">Sell</option></select></td>";
		// tableHtml = tableHtml + "<td><select name=\"callPut\" id=\"callPut"+portSize+"\"><option value=\"call\" selected>Call</option><option value=\"put\">Put</option><option value=\"stock\">Stock</option></select></td>";
		// tableHtml = tableHtml + "<td><input style='width:93%;' type=\"number\" step=\"0.01\" name=\"strk\" id=\"strk"+portSize+"\" value=\"" + "0.00" + "\"></td>";
		// tableHtml = tableHtml + "<td><input style='width:93%;' type=\"number\" step=\"0.01\" name=\"prem\" id=\"prem"+portSize+"\" value=\"" + "0.00" + "\"></td>";
		// tableHtml = tableHtml + "</tr>";
		// tableHtml = tableHtml + "</table>";
		// table.html(tableHtml);
		let tempString = document.getElementById("currPrice").value;
		if (!checkDouble(tempString)){
			tempString = "10.00";
		}

		var row = table.insertRow(portSize+1);
		var cell1 = row.insertCell(0);
		var cell2 = row.insertCell(1);
		var cell3 = row.insertCell(2);
		var cell4 = row.insertCell(3);
		cell1.innerHTML = "<select name=\"buySell\" id=\"buySell"+portSize+"\"><option value=\"buy\" selected>Buy</option><option value=\"sell\">Sell</option></select>";
		cell2.innerHTML = "<select name=\"callPut\" id=\"callPut"+portSize+"\"><option value=\"call\" selected>Call</option><option value=\"put\">Put</option><option value=\"stock\">Stock</option></select>";
		cell3.innerHTML = "<input style='width:93%;' type=\"number\" step=\"0.01\" name=\"strk\" id=\"strk"+portSize+"\" value=\"" + tempString + "\">";
		cell4.innerHTML = "<input style='width:93%;' type=\"number\" step=\"0.01\" name=\"prem\" id=\"prem"+portSize+"\" value=\"" + "0.00" + "\">";
		portSize+=1;
	});

	subButton.click(event1 => {
		var table = document.getElementById("portfolio");
		if (portSize !=0) {
			err2.text("");
			var row = table.deleteRow(portSize);
			portSize-=1;
		} else {
			err2.text("Portfolio is empty.");
		}
	});

});

function drawLine() {
	let input1 = document.getElementById("downsideProb").value;
	let input2 = document.getElementById("downsideLower").value;
	let input3 = document.getElementById("downsideUpper").value;
	let input4 = document.getElementById("midrangeProb").value;
	let input5 = document.getElementById("midrangeLower").value;
	let input6 = document.getElementById("midrangeUpper").value;
	let input7 = document.getElementById("upsideProb").value;
	let input8 = document.getElementById("upsideLower").value;
	let input9 = document.getElementById("upsideUpper").value;

	let line1 = $("#lowLine");
	let line2 = $("#midLine");
	let line3 = $("#highLine");

	line1.css("background-color", getColor(input1));
	line2.css("background-color", getColor(input4));
	line3.css("background-color", getColor(input7));
	let low = 0.0;
	let mid = 0.0;
	let high = 0.0;
	if (input1 != "zero" && checkDouble(input2) && checkDouble(input3)){
		low = parseFloat(input3) - parseFloat(input2);
		if (low < 0.0) {
			document.getElementById("downsideLower").value = input3;
		}
	}
	if (input4 != "zero" && checkDouble(input5) && checkDouble(input6)){
		mid = parseFloat(input6) - parseFloat(input5);
		if (mid < 0.0) {
			err.text("Ensure all lower bounds are below upper bounds.");
		}
	}
	if (input7 != "zero" && checkDouble(input8) && checkDouble(input9)){
		high = parseFloat(input9) - parseFloat(input8);
		if (high < 0.0) {
			document.getElementById("upsideUpper").value = input8;
		}
	}
	let total = low + mid + high;
	low = low / total;
	mid = mid / total;
	high = high / total;
	//
	// let currLow = line1.css("width");
	// currLow = parseFloat(currLow.substring(0, currLow.length-1));

	line1.css("width", (low*70)+"%");
	line2.css("width", (mid*70)+"%");
	line3.css("width", (high*70)+"%");

}

function getColor(val) {
	if (val === "five"){
		return "#65FC6C";
	} else if (val === "four"){
		return "#BEFC65";
	} else if (val === "three"){
		return "#FCF365";
	} else if (val === "two"){
		return "#FCA065";
	} else {
		return "#FC6C65";
	}
}

function drawGraph(points) {
	let dp = [];
	let i;
	for (i = 0; i < points.length; i++) {
	  let coord = points[i];
		dp.push({x: coord[0], y: coord[1]});
	}

	chart = new CanvasJS.Chart("chartContainer", {
		animationEnabled: true,
		theme: "light2",
		title:{
			text: document.getElementById("companyname").value + ": Options Strategy P/L Graph",
			fontFamily: "helvetica",
			fontWeight: "lighter"
		},
		axisX:{
			title:"Price at Expiry"
		},
		axisY:{
			title:"Profit/Loss per Share"
		},
		data: [{
			type: "line",
			indexLabelFontSize: 16,
			dataPoints: dp
		}]
	});
	chart.render();

}

function checkDouble(text){
	if (text === null || text === ""){
		return false;
	}
	let i;
	for (i=0; i < text.length; i++){
		if ((text.charAt(i) > '9' || text.charAt(i) < '0' ) && text.charAt(i) != '.') {
			return false;
		}
	}
	if (text.split(".").length > 2 || text.split(".").length > text.length){
		return false;
	}
	return true;

};

function buildTable(portfolio){
	let htmlCode = "<table name=\"portfolio\" id=\"portfolio\"><tr><th>Buy / Sell</th><th>Call / Put / Stock</th><th>Strike Price</th><th>Premium</th></tr>";
	var i;
	for (i = 0; i < portfolio.length; i++) {
		htmlCode = htmlCode + "<tr>";
	  curr = portfolio[i];
		if (curr[0] === "Sell"){
			htmlCode = htmlCode + "<td><select name=\"buySell\" id=\"buySell"+i+"\"><option value=\"buy\">Buy</option><option value=\"sell\" selected>Sell</option></select></td>";
		} else {
			htmlCode = htmlCode + "<td><select name=\"buySell\" id=\"buySell"+i+"\"><option value=\"buy\" selected>Buy</option><option value=\"sell\">Sell</option></select></td>";
		}

		if (curr[1] === "Put"){
			htmlCode = htmlCode + "<td><select name=\"callPut\" id=\"callPut"+i+"\"><option value=\"call\">Call</option><option value=\"put\" selected>Put</option><option value=\"stock\">Stock</option></select></td>";
		} else if (curr[1] === "Call"){
			htmlCode = htmlCode + "<td><select name=\"callPut\" id=\"callPut"+i+"\"><option value=\"call\" selected>Call</option><option value=\"put\">Put</option><option value=\"stock\">Stock</option></select></td>";
		} else {
			htmlCode = htmlCode + "<td><select name=\"callPut\" id=\"callPut"+i+"\"><option value=\"call\">Call</option><option value=\"put\">Put</option><option value=\"stock\" selected>Stock</option></select></td>";
		}

		htmlCode = htmlCode + "<td><input style='width:93%;' type=\"number\" step=\"0.01\" name=\"strk\" id=\"strk"+i+"\" value=\"" + curr[2] + "\"></td>";
		htmlCode = htmlCode + "<td><input style='width:93%;' type=\"number\" step=\"0.01\" name=\"prem\" id=\"prem"+i+"\" value=\"" + curr[3] + "\"></td>";

		htmlCode = htmlCode+"</tr>";
	}

	htmlCode=htmlCode+"</table>";
	return htmlCode;
}
