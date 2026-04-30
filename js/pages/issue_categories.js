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

const categories = [
  {
    id: "governance",
    title: "Governance",
    icon: "account_balance",
    description:
      "Discussions on civic administration, structural reforms, and institutional transparency.",
    threads: 248,
    href: "issues-feed.html",
    placeholder: false,
  },
];

const DOM = {
  grid: null,
  searchInput: null,
};

function createCategoryCard(category) {
  const article = document.createElement("article");
  article.className = `category-card${category.placeholder ? " placeholder" : ""}`;
  article.dataset.href = category.href;

  article.innerHTML = `
    <div class="category-card__icon-wrap" aria-hidden="true">
      <span class="material-symbols-outlined">${category.icon}</span>
    </div>
    <h2 class="text-headline-sm category-card__title">${category.title}</h2>
    <p class="text-body-md category-card__description">${category.description}</p>
    <div class="category-card__footer">
      <span class="text-label-caps category-card__threads">
        ${category.threads} Active Threads
      </span>
      <a href="${category.href}" class="text-label-caps category-card__explore" aria-label="Explore ${category.title}">
        Explore
        <span class="material-symbols-outlined">arrow_forward</span>
      </a>
    </div>
  `;

  article.addEventListener("click", (event) => {
    if (event.target.closest(".category-card__explore")) {
      return;
    }

    event.preventDefault();
    navigateTo(category.href);
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

  return categories.filter((category) => normalizeQuery(category.title).includes(query));
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

function init() {
  cacheDom();
  renderCards(categories);
  setupSearch();
  setupActiveClassToggle(".nav__link", "nav__link--active");
  preventDefaultForSelectors(".nav__login, .nav__join, .footer__link");
}

document.addEventListener("DOMContentLoaded", init);
loadComponent("header", "/partials/header.html")
loadComponent("footer", "/partials/footer.html")