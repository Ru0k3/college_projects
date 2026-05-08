\"use strict";
import {
  ensureStateBlock,
  preventDefaultForSelectors,
  removeBySelector,
  select,
  selectAll,
  setupActiveClassToggle,
  toDisplayLabel,
} from "../utils/shared.js";
import { loadComponent } from "../utils/load.js";

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

function setupSortDropdown() {
  if (!DOM.sortButton) return;
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
  if (!DOM.loadMoreBtn) return;
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
  setupSortDropdown();
  setupLoadMore();

  // Show all posts by default, hide none
  DOM.posts.forEach((post) => (post.hidden = false));

  // If no posts at all, show empty state
  if (DOM.posts.length === 0) {
    renderEmptyState();
  } else {
    removeEmptyState();
  }
}

document.addEventListener("DOMContentLoaded", init);
loadComponent("header", "/partials/header.html");
loadComponent("footer", "/partials/footer.html");
