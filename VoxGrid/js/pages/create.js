"use strict";

import {
	fetchIssues,
	createDiscussion,
	createBlog,
	createPoll,
} from "../api/create.js";

// -----------------------------------
// DOM
// -----------------------------------
const formatCards = document.querySelectorAll(".format-card");

const dynamicArea = document.querySelector("#dynamic-content-area");

const publishBtn = document.querySelector(".publish-btn");

const issueSelect = document.querySelector("select");

// -----------------------------------
// STATE
// -----------------------------------
let currentType = "discussion";

// -----------------------------------
// LOAD ISSUES
// -----------------------------------
async function loadIssues() {
	try {
		const issues = await fetchIssues();

		issueSelect.innerHTML = `
      <option value="">
        Select an issue...
      </option>
    `;

		issues.forEach((issue) => {
			const option = document.createElement("option");

			option.value = issue.id;

			option.textContent = issue.title;

			issueSelect.appendChild(option);
		});
	} catch (err) {
		console.error(err);
	}
}

// -----------------------------------
// DISCUSSION EDITOR
// -----------------------------------
function createDiscussionEditor() {
	return `
    <input
      type="text"
      class="post-title"
      placeholder="Enter discussion title..."
    />

    <textarea
      class="content-textarea"
      placeholder="Write your discussion..."
    ></textarea>
  `;
}

// -----------------------------------
// ARTICLE EDITOR
// -----------------------------------
function createArticleEditor() {
	return `
    <input
      type="text"
      class="post-title"
      placeholder="Enter article title..."
    />

    <textarea
      class="content-textarea"
      placeholder="Write markdown article..."
    ></textarea>
  `;
}

// -----------------------------------
// POLL EDITOR
// -----------------------------------
function createPollEditor() {
	return `
    <div class="poll-builder">

      <input
        type="text"
        class="poll-question"
        placeholder="Enter poll question"
      />

      <input
        type="text"
        class="poll-option"
        placeholder="Poll option 1"
      />

      <input
        type="text"
        class="poll-option"
        placeholder="Poll option 2"
      />

      <div id="poll-options"></div>

      <button
        type="button"
        class="add-option-btn"
      >
        Add Option
      </button>

    </div>
  `;
}

// -----------------------------------
// RENDER EDITOR
// -----------------------------------
function renderEditor(type) {
	currentType = type;

	if (type === "discussion") {
		dynamicArea.innerHTML = createDiscussionEditor();
	}

	if (type === "article") {
		dynamicArea.innerHTML = createArticleEditor();
	}

	if (type === "poll") {
		dynamicArea.innerHTML = createPollEditor();

		setupPollOptions();
	}
}

// -----------------------------------
// POLL OPTIONS
// -----------------------------------
function setupPollOptions() {
	const addButton = document.querySelector(".add-option-btn");

	const optionsContainer = document.querySelector("#poll-options");

	let optionCount = 2;

	addButton.addEventListener("click", () => {
		optionCount += 1;

		const input = document.createElement("input");

		input.type = "text";

		input.className = "poll-option";

		input.placeholder = `Poll option ${optionCount}`;

		optionsContainer.appendChild(input);
	});
}

// -----------------------------------
// HANDLE PUBLISH
// -----------------------------------
async function handlePublish() {
	try {
		const issueId = issueSelect.value;

		// -------------------------
		// DISCUSSION
		// -------------------------
		if (currentType === "discussion") {
			const title = document.querySelector(".post-title").value.trim();

			const content = document.querySelector(".content-textarea").value.trim();

			await createDiscussion({
				issue_id: issueId,
				user_id: 1,
				title,
				content,
			});

			alert("Discussion created");
		}

		// -------------------------
		// ARTICLE
		// -------------------------
		if (currentType === "article") {
			const title = document.querySelector(".post-title").value.trim();

			const content = document.querySelector(".content-textarea").value.trim();

			await createBlog({
				issue_id: issueId,
				user_id: 1,
				title,
				content,
			});

			alert("Article published");
		}

		// -------------------------
		// POLL
		// -------------------------
		if (currentType === "poll") {
			const question = document.querySelector(".poll-question").value.trim();

			const options = [...document.querySelectorAll(".poll-option")]
				.map((input) => input.value.trim())
				.filter(Boolean);

			await createPoll({
				issue_id: issueId,
				question,
				options,
			});

			alert("Poll created");
		}
	} catch (err) {
		console.error(err);

		alert("Failed to publish");
	}
}

// -----------------------------------
// FORMAT SWITCHING
// -----------------------------------
formatCards.forEach((card) => {
	card.addEventListener("click", () => {
		formatCards.forEach((item) => {
			item.classList.remove("active");
		});

		card.classList.add("active");

		renderEditor(card.dataset.type);
	});
});

// -----------------------------------
// INIT
// -----------------------------------
async function init() {
	renderEditor(currentType);

	await loadIssues();

	publishBtn.addEventListener("click", handlePublish);
}

document.addEventListener("DOMContentLoaded", init);
