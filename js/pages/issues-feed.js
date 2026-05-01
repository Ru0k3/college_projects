"use strict";

import {
  ensureStateBlock,
  navigateTo,
  preventDefaultForSelectors,
  removeBySelector,
  select,
  selectAll,
  setupActiveClassToggle,
  toDisplayLabel,
} from "../utils/shared.js";
import {loadComponent} from "../utils/load.js"

const DOM = {
  feedContainer: null,
  posts: [],
  loadMoreSection: null,
  loadMoreBtn: null,
  sortButton: null,
};

const paginationState = {
  page: 1,
  hasMore: true,
  sort: "trending",
};

const TAB_ROUTES = {
  "all content": null,
  discussions: null,
  "expert blogs": null,
  "active polls": null,
};

const POST_TYPE_CLASSES = {
  discussions: "post--discussion",
  "expert blogs": "post--feature",
  "active polls": "post--poll",
};

function cacheDom() {
  DOM.feedContainer = select(".content");
  DOM.posts = selectAll(".content > .post");
  DOM.loadMoreSection = select(".load-more");
  DOM.loadMoreBtn = select(".load-more button");
  DOM.sortButton = select(".toolbar__sort");
}

function renderEmptyState() {
  ensureStateBlock({
    container: DOM.feedContainer,
    selector: ".feed-empty-state",
    tagName: "div",
    className: "feed-empty-state",
    html: `
      <h2>No governance items available</h2>
      <p>New discussions, blogs, and polls will appear here once content is published.</p>
    `,
    before: DOM.loadMoreSection,
  });
}

function removeEmptyState() {
  removeBySelector(".feed-empty-state", DOM.feedContainer);
}

function getPostsByType(type) {
  if (type === "all content") {
    return DOM.posts;
  }

  const postClass = POST_TYPE_CLASSES[type];
  return DOM.posts.filter((post) => post.classList.contains(postClass));
}

function renderCurrentTab(type) {
  const matchingPosts = getPostsByType(type);

  DOM.posts.forEach((post) => {
    post.hidden = !matchingPosts.includes(post);
  });

  if (matchingPosts.length === 0) {
    renderEmptyState();
    return;
  }

  removeEmptyState();
}

function handleTabChange(tabElement) {
  const tabKey = tabElement.textContent.trim().toLowerCase();
  const route = TAB_ROUTES[tabKey];

  if (route) {
    navigateTo(route);
    return;
  }

  renderCurrentTab(tabKey);
}

function setupTabNavigation() {
  setupActiveClassToggle(".toolbar__tab", "toolbar__tab--active", {
    preventDefault: false,
    onChange: handleTabChange,
  });
}

function setupSortDropdown() {
  if (!DOM.sortButton) {
    return;
  }

  DOM.sortButton.addEventListener("click", () => {
    paginationState.sort =
      paginationState.sort === "trending" ? "latest" : "trending";

    const label = select("span", DOM.sortButton);
    if (label) {
      label.textContent = toDisplayLabel(paginationState.sort);
    }
  });
}

function setupLoadMore() {
  if (!DOM.loadMoreBtn) {
    return;
  }

  DOM.loadMoreBtn.addEventListener("click", () => {
    paginationState.page += 1;
    paginationState.hasMore = false;
    DOM.loadMoreBtn.disabled = true;
    DOM.loadMoreBtn.textContent = "All governance items loaded";
  });
}

function init() {
  cacheDom();
  preventDefaultForSelectors(
    '.post__actions a, .poll-footer a, .panel-link, .note a, .footer-links a'
  );
  setupActiveClassToggle(".topnav__link", "topnav__link--active", {
    preventDefault: false,
  });
  setupTabNavigation();
  setupSortDropdown();
  setupLoadMore();
  renderCurrentTab("all content");
}

document.addEventListener("DOMContentLoaded", init);

loadComponent("header", "/partials/header.html");
loadComponent("footer", "/partials/footer.html");
