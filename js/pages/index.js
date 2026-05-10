"use strict";

import { loadComponent } from "../utils/load.js";

import { fetchIssues } from "../api/issues.js";

import { fetchCategories } from "../api/categories.js";

import { fetchPollByIssue } from "../api/polls.js";

import { fetchDiscussionsByIssue } from "../api/discussions.js";

// -----------------------------------
// DOM
// -----------------------------------
const DOM = {
	issuesGrid: document.querySelector("#issues-grid"),

	categoriesList: document.querySelector("#categories-list"),

	quickPoll: document.querySelector("#quick-poll-card"),

	latestDiscussions: document.querySelector("#latest-discussions"),
};

// -----------------------------------
// RENDER ISSUES
// -----------------------------------
function renderIssues(issues) {
	DOM.issuesGrid.innerHTML = "";

	issues.forEach((issue) => {
		const article = document.createElement("article");

		article.className = "issue-card";

		article.innerHTML = `
      <div class="card-top">
        <span class="label">
          ${issue.category_name}
        </span>

        <span class="material-symbols-outlined">
          bookmark_border
        </span>
      </div>

      <h3>
        <a
          href="issues-feed.html?issue_id=${issue.id}"
          style="
            text-decoration:none;
            color:inherit;
          "
        >
          ${issue.title}
        </a>
      </h3>

      <p>
        ${issue.summary}
      </p>
    `;

		DOM.issuesGrid.appendChild(article);
	});
}

// -----------------------------------
// RENDER CATEGORIES
// -----------------------------------
function renderCategories(categories) {
	DOM.categoriesList.innerHTML = "";

	categories.forEach((category) => {
		const li = document.createElement("li");

		li.innerHTML = `
        <a
          href="issues.html?category=${category.id}"
        >
          <span class="material-symbols-outlined">
            gavel
          </span>

          ${category.name}
        </a>
      `;

		DOM.categoriesList.appendChild(li);
	});
}

// -----------------------------------
// RENDER QUICK POLL
// -----------------------------------
function renderPoll(poll) {
	if (!poll) {
		DOM.quickPoll.innerHTML = "<p>No active poll</p>";

		return;
	}

	DOM.quickPoll.innerHTML = `
    <div class="poll-heading">
      <span class="material-symbols-outlined">
        poll
      </span>

      <h2>Quick Poll</h2>
    </div>

    <p class="poll-question">
      ${poll.question}
    </p>

    <div class="poll-options">
      ${poll.options
				.map(
					(option) => `
            <button
              type="button"
              data-option-id="${option.id}"
            >
              ${option.option_text}
            </button>
          `,
				)
				.join("")}
    </div>
  `;

	const buttons = DOM.quickPoll.querySelectorAll("button");

	buttons.forEach((btn) => {
		btn.addEventListener("click", () => {
			window.location.href = `individual-poll.html?id=${poll.id}`;
		});
	});
}

// -----------------------------------
// RENDER DISCUSSIONS
// -----------------------------------
function renderDiscussions(discussions) {
	DOM.latestDiscussions.innerHTML = "";

	discussions.forEach((discussion) => {
		const article = document.createElement("article");

		article.className = "feed-item";

		article.innerHTML = `
        <div class="meta">
          <span>
            Discussion
          </span>

          <span>
            &middot;
          </span>

          <span>
            Recent
          </span>
        </div>

        <a
          href="discussions.html?discussion_id=${discussion.id}"
        >
          ${discussion.title}
        </a>

        <p>
          ${discussion.content.slice(0, 120)}...
        </p>
      `;

		DOM.latestDiscussions.appendChild(article);
	});
}

// -----------------------------------
// LOAD PAGE DATA
// -----------------------------------
async function loadPageData() {
	try {
		// -------------------------
		// ISSUES
		// -------------------------
		const issues = await fetchIssues();

		renderIssues(issues.slice(0, 3));

		// -------------------------
		// CATEGORIES
		// -------------------------
		const categories = await fetchCategories();

		renderCategories(categories);

		// -------------------------
		// QUICK POLL
		// -------------------------
		if (issues.length > 0) {
			const poll = await fetchPollByIssue(1);

			renderPoll(poll);

			// ---------------------
			// DISCUSSIONS
			// ---------------------
			const discussions = await fetchDiscussionsByIssue(3);

			renderDiscussions(discussions.slice(0, 4));
		}
	} catch (err) {
		console.error(err);
	}
}

// -----------------------------------
// INIT
// -----------------------------------
async function init() {
	await loadComponent("header", "/partials/header.html");

	await loadComponent("footer", "/partials/footer.html");

	await loadPageData();
}

document.addEventListener("DOMContentLoaded", init);
