
/*
console.log("Script Loaded")

let currentTheme = getTheme();

changeTheme();


function changeTheme() {
	document.querySelector("html").classList.add(currentTheme);

	let changeThemeButton = document.querySelector("#theme_change_button");


	changeThemeButton.addEventListener("click", () => {
		console.log("Change Theme Button");

		let oldTheme = currentTheme;

		if (currentTheme === "dark") {
			currentTheme = "light";
		} else {
			currentTheme = "dark";
		}
		setTheme(currentTheme);
		document.querySelector("html").classList.remove(oldTheme);
		document.querySelector("html").classList.add(currentTheme);

		changeThemeButton.querySelector("span").textContent = currentTheme == "light" ? "dark" : "light";

	})

	console.log(currentTheme);
}

function setTheme(theme) {
	localStorage.setItem("theme", theme);
}

function getTheme() {
	let theme = localStorage.getItem("theme");
	return theme ? theme : "light";
}
*/


console.log("Script Loaded");

let currentTheme = getTheme(); // Retrieve theme from localStorage
setTheme(currentTheme); // Ensure localStorage is in sync

changeTheme();

function changeTheme() {
    document.querySelector("html").classList.add(currentTheme);

    let changeThemeButton = document.querySelector("#theme_change_button");

    // Ensure the changeThemeButton exists before proceeding
    if (!changeThemeButton) {
        console.error("Change theme button not found.");
        return;
    }

    // Update button text to reflect the current state
    changeThemeButton.querySelector("span").textContent = currentTheme === "light" ? "dark" : "light";

    changeThemeButton.addEventListener("click", () => {
        console.log("Change Theme Button Clicked");

        let oldTheme = currentTheme;

        // Toggle the theme
        currentTheme = (currentTheme === "dark") ? "light" : "dark";
        setTheme(currentTheme);

        // Update HTML class to reflect the new theme
        document.querySelector("html").classList.remove(oldTheme);
        document.querySelector("html").classList.add(currentTheme);

        // Update button text to show opposite theme
        changeThemeButton.querySelector("span").textContent = currentTheme === "light" ? "dark" : "light";

        console.log("Theme changed to:", currentTheme);
    });

    console.log("Initial theme:", currentTheme);
}

function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light"; // Default to "light" if no theme is stored
}

