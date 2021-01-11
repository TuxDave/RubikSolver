const COLORS = ["yellow", "green", "orange", "blue", "red", "white"];

let selectedColor = "white";

function setupFace() {
	//document.getElementById("scramble").style.visibility = "hidden";
	//document.getElementById("picker").style.visibility = "show";
	for (let color of COLORS) {
		let els = document.getElementById(color).getElementsByTagName("td");
		for (let i = 0; i < 9; i++) {
			els[i].addEventListener("click", () => {
				if (i != 4) els[i].style.backgroundColor = selectedColor;
			});
			els[i].style.backgroundColor = "lightyellow";
		}
		els[4].style.backgroundColor = color;
	}
}

function scramble() {
	//document.getElementById("picker").style.visibility = "hidden";
	//document.getElementById("scramble").style.visibility = "show";
	let url = window.location.href;
	fetch(url + "scramble")
		.then((result) => result.json())
		.then(
			(json) =>
				(document.getElementById("scramble").innerHTML =
					json["scramble"])
		);
}

function selectColor() {
	selectedColor = document.getElementById("colors").value;
}
