"use strict";

const AUDIO_ENABLED_KEY = "voxgrid-background-audio-enabled";
const AUDIO_STATE_KEY = "voxgrid-background-audio-time";
const AUDIO_TOGGLE_SELECTOR = ".audio-toggle";
const audioUrl = new URL("../../assets/audio/audio.mpeg", import.meta.url);

const backgroundAudio = new Audio(audioUrl);
backgroundAudio.loop = true;
backgroundAudio.preload = "auto";
backgroundAudio.volume = 0.5;

let isWaitingForInteraction = false;

function isAudioEnabled() {
	return localStorage.getItem(AUDIO_ENABLED_KEY) !== "false";
}

const savedTime = Number.parseFloat(sessionStorage.getItem(AUDIO_STATE_KEY) || "0");
if (Number.isFinite(savedTime) && savedTime > 0) {
	backgroundAudio.currentTime = savedTime;
}

async function playBackgroundAudio() {
	if (!isAudioEnabled()) {
		pauseBackgroundAudio();
		return;
	}

	if (!hasAudioToggle()) {
		updateAudioToggle();
		return;
	}

	try {
		await backgroundAudio.play();
		removeInteractionListeners();
	} catch (error) {
		addInteractionListeners();
	}

	updateAudioToggle();
}

function pauseBackgroundAudio() {
	backgroundAudio.pause();
	removeInteractionListeners();
	updateAudioToggle();
}

function setAudioEnabled(enabled) {
	localStorage.setItem(AUDIO_ENABLED_KEY, String(enabled));

	if (enabled) {
		playBackgroundAudio();
	} else {
		pauseBackgroundAudio();
	}
}

function addInteractionListeners() {
	if (isWaitingForInteraction) {
		return;
	}

	isWaitingForInteraction = true;
	document.addEventListener("click", playBackgroundAudio, { once: true });
	document.addEventListener("keydown", playBackgroundAudio, { once: true });
	document.addEventListener("touchstart", playBackgroundAudio, { once: true });
	document.addEventListener("pointerdown", playBackgroundAudio, { once: true });
}

function removeInteractionListeners() {
	isWaitingForInteraction = false;
	document.removeEventListener("click", playBackgroundAudio);
	document.removeEventListener("keydown", playBackgroundAudio);
	document.removeEventListener("touchstart", playBackgroundAudio);
	document.removeEventListener("pointerdown", playBackgroundAudio);
}

function getAudioToggles() {
	return Array.from(document.querySelectorAll(AUDIO_TOGGLE_SELECTOR));
}

function hasAudioToggle() {
	return getAudioToggles().length > 0;
}

function bindAudioToggle() {
	getAudioToggles().forEach((toggle) => {
		if (toggle.dataset.audioBound === "true") {
			return;
		}

		toggle.dataset.audioBound = "true";
		toggle.addEventListener("click", () => {
			setAudioEnabled(backgroundAudio.paused);
		});
	});

	updateAudioToggle();
}

function updateAudioToggle() {
	const toggles = getAudioToggles();
	if (!toggles.length) {
		return;
	}

	const isPlaying = !backgroundAudio.paused && isAudioEnabled();
	const label = isPlaying ? "Pause background audio" : "Play background audio";
	const iconName = isPlaying ? "volume_up" : "volume_off";

	toggles.forEach((toggle) => {
		const icon = toggle.querySelector(".audio-toggle__icon");

		toggle.setAttribute("aria-pressed", String(isPlaying));
		toggle.setAttribute("aria-label", label);
		toggle.setAttribute("title", label);

		if (icon) {
			icon.textContent = iconName;
		}
	});
}

window.addEventListener("beforeunload", () => {
	sessionStorage.setItem(AUDIO_STATE_KEY, String(backgroundAudio.currentTime));
});

document.addEventListener("component:loaded", (event) => {
	if (event.detail?.file?.includes("header.html")) {
		bindAudioToggle();
		playBackgroundAudio();
	}
});

document.addEventListener("DOMContentLoaded", () => {
	bindAudioToggle();
	playBackgroundAudio();
});

backgroundAudio.addEventListener("play", updateAudioToggle);
backgroundAudio.addEventListener("pause", updateAudioToggle);
