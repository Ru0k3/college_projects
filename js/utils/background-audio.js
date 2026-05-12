"use strict";

const AUDIO_ENABLED_KEY = "voxgrid-background-audio-enabled";
const AUDIO_STATE_KEY = "voxgrid-background-audio-time";
const audioUrl = new URL("../../assets/audio/audio.mpeg", import.meta.url);

const backgroundAudio = new Audio(audioUrl);
backgroundAudio.loop = true;
backgroundAudio.preload = "auto";
backgroundAudio.volume = 0.5;

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
	document.addEventListener("click", playBackgroundAudio, { once: true });
	document.addEventListener("keydown", playBackgroundAudio, { once: true });
	document.addEventListener("touchstart", playBackgroundAudio, { once: true });
	document.addEventListener("pointerdown", playBackgroundAudio, { once: true });
}

function removeInteractionListeners() {
	document.removeEventListener("click", playBackgroundAudio);
	document.removeEventListener("keydown", playBackgroundAudio);
	document.removeEventListener("touchstart", playBackgroundAudio);
	document.removeEventListener("pointerdown", playBackgroundAudio);
}

function bindAudioToggle() {
	const toggle = document.querySelector(".audio-toggle");
	if (!toggle || toggle.dataset.audioBound === "true") {
		return;
	}

	toggle.dataset.audioBound = "true";
	toggle.addEventListener("click", () => {
		setAudioEnabled(backgroundAudio.paused);
	});
	updateAudioToggle();
}

function updateAudioToggle() {
	const toggle = document.querySelector(".audio-toggle");
	if (!toggle) {
		return;
	}

	const icon = toggle.querySelector(".audio-toggle__icon");
	const isPlaying = !backgroundAudio.paused && isAudioEnabled();

	toggle.setAttribute("aria-pressed", String(isPlaying));
	toggle.setAttribute("aria-label", isPlaying ? "Pause background audio" : "Play background audio");

	if (icon) {
		icon.textContent = isPlaying ? "volume_up" : "volume_off";
	}
}

window.addEventListener("beforeunload", () => {
	sessionStorage.setItem(AUDIO_STATE_KEY, String(backgroundAudio.currentTime));
});

document.addEventListener("component:loaded", (event) => {
	if (event.detail?.file?.includes("header.html")) {
		bindAudioToggle();
	}
});

document.addEventListener("DOMContentLoaded", () => {
	bindAudioToggle();
	playBackgroundAudio();
});
