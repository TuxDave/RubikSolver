const COLORS = ["yellow", "green", "orange", "blue", "red", "white"];

let selectedColor = "white";

function setupFace() {
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

function selectColor() {
	selectedColor = document.getElementById("colors").value;
}
