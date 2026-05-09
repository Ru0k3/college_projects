"use strict";

const formatCards = document.querySelectorAll(".format-card");

const dynamicArea = document.querySelector("#dynamic-content-area");

let currentType = "discussion";

function createDiscussionEditor() {
	return `
    <textarea
      class="content-textarea"
      placeholder="Detail your proposition, analysis, or inquiry..."
    ></textarea>
  `;
}

function createArticleEditor() {
	return `
    <textarea
      class="content-textarea"
      placeholder="Write your long-form civic article in markdown format..."
    ></textarea>
  `;
}

function createPollEditor() {
	return `

    <div class="poll-builder">

      <input
        type="text"
        placeholder="Enter your poll question"
      />

      <input
        type="text"
        placeholder="Poll option 1"
      />

      <input
        type="text"
        placeholder="Poll option 2"
      />

      <div id="poll-options"></div>

      <button class="add-option-btn">
        Add Option
      </button>

    </div>
  `;
}

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

function setupPollOptions() {
	const addButton = document.querySelector(".add-option-btn");

	const optionsContainer = document.querySelector("#poll-options");

	let optionCount = 2;

	addButton.addEventListener("click", () => {
		optionCount += 1;

		const input = document.createElement("input");

		input.type = "text";

		input.placeholder = `Poll option ${optionCount}`;

		optionsContainer.appendChild(input);
	});
}

formatCards.forEach((card) => {
	card.addEventListener("click", () => {
		formatCards.forEach((item) => {
			item.classList.remove("active");
		});

		card.classList.add("active");

		renderEditor(card.dataset.type);
	});
});

renderEditor(currentType);
