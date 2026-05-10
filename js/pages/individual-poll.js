"use strict";

import { fetchPoll, submitVote } from "../api/polls.js";

import { loadComponent } from "../utils/load.js";

// -------------------------
// DOM
// -------------------------
const DOM = {
	title: document.querySelector(".poll-header h1"),
	totalVotes: document.querySelector("#total-votes-label"),
	voteOptions: document.querySelector("#vote-options"),
	votedMsg: document.querySelector("#voted-msg"),
};

// -------------------------
// URL PARAMS
// -------------------------
const params = new URLSearchParams(window.location.search);
const pollId = params.get("id");

// -------------------------
// RENDER OPTIONS
// -------------------------
function renderOptions(options = []) {
	DOM.voteOptions.innerHTML = "";

	options.forEach((option) => {
		const button = document.createElement("button");

		button.className = "vote-btn";
		button.type = "button";
		button.textContent = option.option_text;

		button.addEventListener("click", async () => {
			// remove previous selection
			document.querySelectorAll(".vote-btn").forEach((btn) => {
				btn.classList.remove("vote-btn--selected");
			});

			// select current button
			button.classList.add("vote-btn--selected");

			// submit vote
			const result = await submitVote(pollId, option.id);

			if (result) {
				DOM.votedMsg.textContent = "Your vote has been recorded.";

				// disable buttons after vote
				document.querySelectorAll(".vote-btn").forEach((btn) => {
					btn.disabled = true;
				});
			}
		});

		DOM.voteOptions.appendChild(button);
	});
}

// -------------------------
// LOAD POLL
// -------------------------
async function loadPoll() {
	if (!pollId) return;

	try {
		// fetch poll + options together
		const poll = await fetchPoll(pollId);

		if (!poll) return;

		// populate poll data
		DOM.title.textContent = poll.question;

		DOM.totalVotes.textContent = `${poll.total_votes || 0} total votes cast`;

		// render options
		renderOptions(poll.options);
	} catch (err) {
		console.error("Failed to load poll:", err);
	}
}

// -------------------------
// INIT
// -------------------------
async function init() {
	await loadComponent("header", "/partials/header.html");
	await loadComponent("footer", "/partials/footer.html");

	await loadPoll();
}

document.addEventListener("DOMContentLoaded", init);
