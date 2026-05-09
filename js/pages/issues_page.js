"use strict";

import { select, renderList, normalizeQuery } from "../utils/shared.js";
import { fetchIssues } from "../api/issues.js";
import { loadComponent } from "../utils/load.js";

const DOM = {
	grid: null,
	searchInput: null,
	categoryTitle: null,
};

let issues = [];
let categoryId = null;

function createIssueCard(issue) {
	const article = document.createElement("article");
	article.className = "issue-card";

	article.innerHTML = `
    <h2>${issue.title}</h2>
    <p>${issue.summary}</p>
    <div class="issue-card-footer">
      <span><span class="material-symbols-outlined">chat_bubble</span>${issue.comment_count || 0}</span>
      <span><span class="material-symbols-outlined">thumb_up</span>${issue.vote_count || 0}</span>
    </div>
  `;

	article.addEventListener("click", () => {
		window.location.href = `/issues-feed.html?id=${issue.id}`;
	});

	return article;
}

function renderIssues(list) {
	renderList(DOM.grid, list, createIssueCard, {
		className: "no-results",
		text: "No issues found.",
	});
}

function setupSearch() {
	if (!DOM.searchInput) return;

	DOM.searchInput.addEventListener("input", () => {
		const query = normalizeQuery(DOM.searchInput.value);
		renderIssues(issues.filter((i) => normalizeQuery(i.title).includes(query)));
	});
}

function cacheDom() {
	DOM.grid = select("#issues-grid");
	DOM.searchInput = select("#search-input");
	DOM.categoryTitle = select("#category-title");
}

async function loadIssues() {
	const params = new URLSearchParams(window.location.search);
	categoryId = params.get("category");

	DOM.categoryTitle.textContent = `Category: ${categoryId}`;

	try {
		issues = await fetchIssues(categoryId);
		renderIssues(issues);
	} catch (err) {
		console.error(err);
		DOM.grid.innerHTML = "<p class='no-results'>Failed to load issues.</p>";
	}
}

async function init() {
	cacheDom();
	setupSearch();
	await loadComponent("header", "/partials/header.html");
	await loadComponent("footer", "/partials/footer.html");
	setupSearch();
	await loadIssues();
}

document.addEventListener("DOMContentLoaded", init);
