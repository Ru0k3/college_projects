"use strict";

import { fetchDiscussions, fetchBlogs, fetchPolls } from "../api/issuesFeed.js";
import { select, renderList } from "../utils/shared.js";
import { loadComponent } from "../utils/load.js";
import { fetchIssue } from "../api/issues.js";

const DOM = {
	container: select(".content"),
	discussionsContainer: select(".post--discussion"),
	blogsContainer: select(".post--feature"),
	pollsContainer: select(".post--poll"),
};

function editHeadings(issue, issueDesc) {
	const heading = document.querySelector(".hero h1");
	const description = document.querySelector(".hero p");

	heading.textContent = issue;
	description.textContent = issueDesc;
}

function createDiscussionCard(disc) {
	const article = document.createElement("article");
	article.className = "post post--discussion";
	article.innerHTML = `
    <div class="vote">
      <button type="button" aria-label="Upvote">▲</button>
      <span>${disc.vote_count || 0}</span>
      <button type="button" aria-label="Downvote">▼</button>
    </div>
    <div class="post__body">
      <div class="post__meta">
        <span class="tag tag--muted">Discussion</span>
        <span>${disc.created_at} by <a href="#">@${disc.author_name}</a></span>
      </div>
      <h2>${disc.title}</h2>
      <p>${disc.content.substring(0, 120)}...</p>
      <div class="post__actions">
        <a href="discussion.html?id=${disc.id}">${disc.comment_count || 0} Comments</a>
        <a href="#">Save</a>
      </div>
    </div>
  `;
	return article;
}

function createBlogCard(blog) {
	const article = document.createElement("article");
	article.className = "post post--feature";
	article.innerHTML = `
    <div class="feature-art">
      <span class="tag tag--dark">Expert Blog</span>
    </div>
    <div class="post__body">
      <h2><a href="blog.html?id=${blog.id}">${blog.title}</a></h2>
      <p>${blog.content.substring(0, 150)}...</p>
      <div class="author-row">
        <div class="author-row__person">
          <div class="author-row__avatar" aria-hidden="true"></div>
          <div>
            <strong>${blog.author_name}</strong>
            <span>${blog.author_role || "Contributor"}</span>
          </div>
        </div>
        <span class="author-row__time">${blog.read_time || "5 Min Read"}</span>
      </div>
    </div>
  `;
	return article;
}

function createPollCard(poll) {
	const article = document.createElement("article");
	console.log("Yess");
	article.className = "post post--poll";
	article.innerHTML = `
    <div class="poll-head">
      <span class="poll-head__label">Active Poll</span>
    </div>
    <h2>${poll.question}</h2>
    <div class="poll-options">
      ${poll.options
				.map(
					(o) => `<button type="button" class="poll-option-btn"
          onclick="window.location.href='individual-poll.html?id=${poll.id}&vote=${encodeURIComponent(o.option_text)}'">
          ${o.option_text}
        </button>`,
				)
				.join("")}
    </div>
    <div class="poll-footer">
      <span>${poll.total_votes || 0} Total Votes</span>
      <a href="individual-poll.html?id=${poll.id}">View Discussion</a>
    </div>
  `;
	return article;
}

// -------------------------
// Load content for an issue
// -------------------------
async function loadIssueContent() {
	const params = new URLSearchParams(window.location.search);
	const issueId = params.get("id");
	if (!issueId) return;

	try {
		const [discussions, blogs, polls, issue] = await Promise.all([
			fetchDiscussions(issueId),
			fetchBlogs(issueId),
			fetchPolls(issueId),
			fetchIssue(issueId),
		]);
		console.log(polls);

		// Clear existing placeholders
		DOM.container.querySelectorAll(".post").forEach((p) => p.remove());

		editHeadings(issue.title, issue.summary);
		discussions.forEach((d) =>
			DOM.container.appendChild(createDiscussionCard(d)),
		);
		blogs.forEach((b) => DOM.container.appendChild(createBlogCard(b)));
		polls.forEach((p) => DOM.container.appendChild(createPollCard(p)));
	} catch (err) {
		console.error("Failed to load issue content:", err);
	}
}

document.addEventListener("DOMContentLoaded", async () => {
	await loadComponent("header", "/partials/header.html");
	await loadComponent("footer", "/partials/footer.html");
	await loadIssueContent();
});
