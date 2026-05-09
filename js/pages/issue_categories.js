"use strict";

import {
	navigateTo,
	normalizeQuery,
	preventDefaultForSelectors,
	renderList,
	select,
	setupActiveClassToggle,
} from "../utils/shared.js";

import { loadComponent } from "../utils/load.js";
import { fetchCategories } from "../api/categories.js";

const DOM = {
	grid: null,
	searchInput: null,
};

let categories = [];

function createCategoryCard(category) {
	const article = document.createElement("article");

	article.className = "category-card";

	article.dataset.href = `issues-feed.html?category=${category.id}`;

	article.innerHTML = `
    <div class="category-card__icon-wrap" aria-hidden="true">
      <span class="material-symbols-outlined">forum</span>
    </div>

    <h2 class="text-headline-sm category-card__title">
      ${category.name}
    </h2>

    <a
      href="issues-feed.html?category=${category.id}"
      class="text-label-caps category-card__explore"
      aria-label="Explore ${category.name}"
    >
      Explore
      <span class="material-symbols-outlined">arrow_forward</span>
    </a>
  `;

	article.addEventListener("click", (event) => {
		if (event.target.closest(".category-card__explore")) {
			return;
		}

		event.preventDefault();

		navigateTo(`issues_page.html?category=${category.id}`);
	});

	return article;
}

function renderCards(categoryList) {
	renderList(DOM.grid, categoryList, createCategoryCard, {
		className: "no-results",
		text: "No categories found.",
	});
}

function filterCategories(query) {
	if (!query) {
		return categories;
	}

	return categories.filter((category) =>
		normalizeQuery(category.name).includes(query),
	);
}

function setupSearch() {
	if (!DOM.searchInput) {
		return;
	}

	DOM.searchInput.addEventListener("input", () => {
		const query = normalizeQuery(DOM.searchInput.value);

		renderCards(filterCategories(query));
	});
}

function cacheDom() {
	DOM.grid = select("#js-categories-grid");
	DOM.searchInput = select("#js-search-input");
}

async function loadCategories() {
	try {
		categories = await fetchCategories();

		renderCards(categories);
	} catch (err) {
		console.error(err);

		DOM.grid.innerHTML = `
      <p class="no-results">
        Failed to load categories.
      </p>
    `;
	}
}

async function init() {
	cacheDom();
	setupSearch();
	setupActiveClassToggle(".nav__link", "nav__link--active");
	preventDefaultForSelectors(".nav__login, .nav__join, .footer__link");
	loadComponent("footer", "/partials/footer.html");
	loadComponent("header", "/partials/header.html");
	await loadCategories();
}

document.addEventListener("DOMContentLoaded", init);
