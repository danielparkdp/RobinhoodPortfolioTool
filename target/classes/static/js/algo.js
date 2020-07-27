
const submit = $("#submit");
const err = $("#error");

$(document).ready(() => {
	submit.click(event1 => {
		currText1 = document.getElementById("companyname").value.trim();
    currText2 = document.getElementById("currPrice").value.trim();
    currText3 = document.getElementById("interest").value.trim();
    currText4 = document.getElementById("volatility").value.trim();
		currText5 = document.getElementById("time").value.trim();

		if(!checkDouble(currText2) || !checkDouble(currText3) || !checkDouble(currText4) || !checkDouble(currText5)){
			err.text("Ensure everything is a properly-formatted decimal");
		}
		else {
			err.text("");
			const postParameters = {companyname : currText1, currPrice : currText2
							, interest : currText3,
							volatility : currText4,
							time : currText5};

			$.post("/search", postParameters, responseJSON => {
				const responseObject = JSON.parse(responseJSON);
			});
		}




	});
});


function checkDouble(text){
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
