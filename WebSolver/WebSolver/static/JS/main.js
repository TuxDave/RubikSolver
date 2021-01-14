const COLORS = ["yellow", "green", "orange", "blue", "red", "white"];
const COLORS_MAP = {
	2: "yellow",
	3: "green",
	4: "orange",
	1: "blue",
	5: "red",
	0: "white",
};
const RGB_MAP = {
	//put in place of the right word the hex code of the color to change tone es #AABBCC
	yellow: "yellow",
	green: "green",
	orange: "orange",
	blue: "blue",
	red: "red",
	white: "white",
};

let selectedColor = "white";

function setupFace() {
	//document.getElementById("scramble").style.visibility = "hidden";
	//document.getElementById("picker").style.visibility = "show";
	for (let color of COLORS) {
		let els = document.getElementById(color).getElementsByTagName("td");
		for (let i = 0; i < 9; i++) {
			els[i].addEventListener("click", () => {
				if (i != 4) els[i].style.backgroundColor = RGB_MAP[selectColor];
			});
			els[i].style.backgroundColor = "lightyellow";
		}
		els[4].style.backgroundColor = RGB_MAP[color];
	}
}

function scramble() {
	//document.getElementById("picker").style.visibility = "hidden";
	//document.getElementById("scramble").style.visibility = "show";
	let url = window.location.href;
	fetch(url + "scramble")
		.then((result) => result.json())
		.then((json) => {
			//two entries: scramble, state
			document.getElementById("scramble").innerHTML = json["scramble"];
			applyState(json["state"]);
		});
}

const SPOT_ORDER = [4, 0, 1, 2, 5, 8, 7, 6, 3];

/**
 * paints the cube on base the state
 * @param {*} _state 54 length string containing the number of colors
 */
function applyState(_state) {
	for (let i = 0; i < 6; i++) {
		face = document.getElementById(COLORS[i]);
		faceState = _state.substr(i * 9, 9);
		spots = face.getElementsByTagName("td");
		for (let j = 0; j < 9; j++) {
			spots[SPOT_ORDER[j]].style.backgroundColor =
				RGB_MAP[COLORS_MAP[faceState[j]]];
		}
		aa;
	}
}

function selectColor() {
	selectedColor = document.getElementById("colors").value;
}
