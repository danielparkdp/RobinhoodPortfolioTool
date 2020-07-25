
const submit = $("#submit");

$(document).ready(() => {
	submit.click(event1 => {
		currText1 = document.getElementById("companyname").value;
    currText2 = document.getElementById("currPrice").value;
    currText3 = document.getElementById("interest").value;
    currText4 = document.getElementById("volatility").value;
		currText5 = document.getElementById("time").value;

		const postParameters = {companyname : currText1, currPrice : currText2
            , interest : currText3,
            volatility : currText4,
            time : currText5};

		$.post("/search", postParameters, responseJSON => {
			const responseObject = JSON.parse(responseJSON);
		});
	});
});
