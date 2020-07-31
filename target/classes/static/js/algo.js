
const submit = $("#submit");
const err = $("#error");
const err2 = $("#error2");
const table = $("#portfolio");
const buttons = $("#buttons");
const refresh = $("#refresh");
const addButton = $("#add_button");
const subButton = $("#sub_button");

const input1 = $("#downsideProb");
const input2 = $("#downsideLower");
const input3 = $("#downsideUpper");
const input4 = $("#midrangeProb");
const input5 = $("#midrangeLower");
const input6 = $("#midrangeUpper");
const input7 = $("#upsideProb");
const input8 = $("#upsideLower");
const input9 = $("#upsideUpper");


let chart;
let portSize = 0;




$(document).ready(() => {
	input1.change(function() {
		if (document.getElementById("downsideProb").value === "zero"){
			document.getElementById("downsideLower").value = document.getElementById("downsideLower").placeholder;
			document.getElementById("downsideUpper").value = document.getElementById("downsideUpper").placeholder;
		}

	});

	input2.change(function() {
		if (document.getElementById("downsideProb").value === "zero") {
			document.getElementById("downsideProb").value = "three";
		}
	});

	input3.change(function() {
		if (document.getElementById("midrangeProb").value != "zero"){
			document.getElementById("midrangeLower").value = document.getElementById("downsideUpper").value;
			if (document.getElementById("downsideProb").value === "zero") {
				document.getElementById("downsideProb").value = "three";
			}
		}

	});

	input4.change(function() {
		if (document.getElementById("midrangeProb").value === "zero"){
			document.getElementById("midrangeLower").value = "Range Lower Limit";
			document.getElementById("midrangeUpper").value = "Range Upper Limit";
		}

	});

	input5.change(function() {
		if (document.getElementById("downsideProb").value != "zero"){
			document.getElementById("downsideUpper").value = document.getElementById("midrangeLower").value;
			if (document.getElementById("midrangeProb").value === "zero") {
				document.getElementById("midrangeProb").value = "three";
			}
		}

	});

	input6.change(function() {
		if (document.getElementById("upsideProb").value != "zero"){
			document.getElementById("upsideLower").value = document.getElementById("midrangeUpper").value;
			if (document.getElementById("midrangeProb").value === "zero") {
				document.getElementById("midrangeProb").value = "three";
			}
		}

	});

	input7.change(function() {
		if (document.getElementById("upsideProb").value === "zero"){
			document.getElementById("upsideLower").value = document.getElementById("upsideLower").placeholder;
			document.getElementById("upsideUpper").value = document.getElementById("upsideUpper").placeholder;
		}

	});

	input8.change(function() {
		if (document.getElementById("midrangeProb").value != "zero"){
			document.getElementById("midrangeUpper").value = document.getElementById("upsideLower").value;
			if (document.getElementById("upsideProb").value === "zero") {
				document.getElementById("upsideProb").value = "three";
			}
		}

	});

	input9.change(function() {
		if (document.getElementById("upsideProb").value === "zero") {
			document.getElementById("upsideProb").value = "three";
		}

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
			let inputs = "" + input1 + "," + input2 + "," + input3 + "," + input4 + "," + input5 + "," + input6 + "," + input7 + "," + input8 + "," + input9;
console.log(inputs);
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
			text: "Options Strategy P/L Graph",
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
