const COLORS = ["yellow", "green", "orange", "blue", "red", "white"];
const COLORS_MAP = {
	2: "yellow",
	3: "green",
	4: "orange",
	1: "blue",
	5: "red",
	0: "white",
};
const MAP_COLORS = inverse(COLORS_MAP);
const RGB_MAP = {
	//put in place of the right word the hex code of the color to change tone es #AABBCC
	yellow: "yellow",
	green: "green",
	orange: "orange",
	blue: "blue",
	red: "red",
	white: "white",
};
const MAP_RGB = inverse(RGB_MAP);

let selectedColor = "white";
let drawer = true;

function setupFace() {
	drawer = true;
	document.getElementById("infoBox").style.visibility = "hidden";
	document.getElementById("infoBox").innerHTML = "";
	document.getElementById("picker").style.visibility = "visible";
	for (let color of COLORS) {
		let els = document.getElementById(color).getElementsByTagName("td");
		for (let i = 0; i < 9; i++) {
			els[i].addEventListener("click", () => {
				if (drawer && i != 4)
					els[i].style.backgroundColor = RGB_MAP[selectedColor];
			});
			els[i].style.backgroundColor = "lightyellow";
		}
		els[4].style.backgroundColor = RGB_MAP[color];
	}
}

function solve() {
	let state = getState();
	let url = window.location.href;
	infoBox = document.getElementById("infoBox");
	infoBox.style.visibility = "visible";
	fetch(url + "solve?cube=" + state)
		.then((result) => result.json())
		.then((solve) => {
			solve = solve["solve"];
			if (infoBox.innerHTML.length != 0)
				infoBox.innerHTML += "<br>Solve: " + solve;
			else infoBox.innerHTML = "Solve: " + solve;
		});
	applyState("");
}

function scramble() {
	drawer = false;
	document.getElementById("picker").style.visibility = "hidden";
	document.getElementById("infoBox").style.visibility = "visible";
	let url = window.location.href;
	fetch(url + "scramble")
		.then((result) => result.json())
		.then((json) => {
			//two entries: scramble, state
			document.getElementById("infoBox").innerHTML =
				"Scramble: " + json["scramble"] + "<br>";
			applyState(json["state"]);
		});
}

function inverse(obj) {
	var retobj = {};
	for (var key in obj) {
		retobj[obj[key]] = key;
	}
	return retobj;
}

const SPOT_ORDER = [4, 0, 1, 2, 5, 8, 7, 6, 3];

/**
 * paints the cube on base the state
 * @param {*} _state 54 length string containing the number of colors
 */
function applyState(_state) {
	for (let i = 0; i < 6; i++) {
		let face = document.getElementById(COLORS[i]);
		let faceState = _state.substr(i * 9, 9);
		let spots = face.getElementsByTagName("td");
		for (let j = 0; j < 9; j++) {
			spots[SPOT_ORDER[j]].style.backgroundColor =
				RGB_MAP[COLORS_MAP[faceState[j]]];
		}
	}
}

function getState() {
	let state = "";
	for (let i = 0; i < 6; i++) {
		let face = document.getElementById(COLORS[i]);
		let spots = face.getElementsByTagName("td");
		for (let j = 0; j < 9; j++) {
			state +=
				MAP_COLORS[MAP_RGB[spots[SPOT_ORDER[j]].style.backgroundColor]];
		}
	}
	//TODO: aggiungere controlli di impossibilità modello di cubo corrente
	return state;
}

function selectColor() {
	selectedColor = document.getElementById("colors").value;
}
