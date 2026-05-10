"use strict";

import {
	fetchDiscussion,
	fetchComments,
	addComment,
	voteDiscussion,
} from "../api/discussions.js";

import { loadComponent } from "../utils/load.js";

// -----------------------------------
// DOM
// -----------------------------------
const DOM = {
	title: document.querySelector("main h1"),

	description: document.querySelector(".description-box p"),

	agreeBtn: document.querySelector(".actions-row .btn:first-child"),

	disagreeBtn: document.querySelector(".actions-row .btn:last-child"),

	commentsContainer:
		document.querySelector(".discussion-header").nextElementSibling,

	postInput: document.querySelector(".post-box input"),

	postButton: document.querySelector(".post-box button"),

	progressBar: document.querySelector(".progress-bar .agree"),

	opinionStats: document.querySelector(".opinion-stats"),
};

// -----------------------------------
// URL PARAMS
// discussions.html?id=1
// -----------------------------------
const params = new URLSearchParams(window.location.search);

const discussionId = params.get("id");

// -----------------------------------
// CREATE COMMENT ELEMENT
// -----------------------------------
function createComment(comment) {
	const div = document.createElement("div");

	div.className = "comment";

	div.innerHTML = `
    <div class="avatar"></div>

    <div class="comment-body">

      <div class="comment-meta">
        <strong>
          ${comment.user_name || "Anonymous"}
        </strong>

        <span>
          ${comment.created_at || "Just now"}
        </span>
      </div>

      <p>
        ${comment.content}
      </p>

      <div class="comment-actions">
        Up ${comment.upvotes || 0}
        &nbsp;&nbsp;
        Down ${comment.downvotes || 0}
        &nbsp;&nbsp;
        Reply
      </div>

    </div>
  `;

	return div;
}

// -----------------------------------
// RENDER COMMENTS
// -----------------------------------
function renderComments(comments) {
	// remove old comments
	document.querySelectorAll(".comment").forEach((c) => c.remove());

	comments.forEach((comment) => {
		DOM.commentsContainer.parentNode.insertBefore(
			createComment(comment),
			document.querySelector(".post-box"),
		);
	});
}

// -----------------------------------
// LOAD DISCUSSION
// -----------------------------------
async function loadDiscussion() {
	if (!discussionId) return;

	try {
		const [discussion, comments] = await Promise.all([
			fetchDiscussion(discussionId),
			fetchComments(discussionId),
		]);

		if (!discussion) return;

		// populate discussion
		DOM.title.textContent = discussion.title;

		DOM.description.textContent = discussion.content;

		// opinion stats
		const agree = discussion.agree_count || 0;

		const disagree = discussion.disagree_count || 0;

		const total = agree + disagree;

		const agreePercent = total > 0 ? Math.round((agree / total) * 100) : 50;

		DOM.progressBar.style.width = `${agreePercent}%`;

		DOM.opinionStats.innerHTML = `
      <span>${agreePercent}% Agree</span>
      <span>${100 - agreePercent}% Disagree</span>
    `;

		// comments
		renderComments(comments);
	} catch (err) {
		console.error(err);
	}
}

// -----------------------------------
// POST COMMENT
// -----------------------------------
async function handleCommentPost() {
	const content = DOM.postInput.value.trim();

	if (!content) return;

	const result = await addComment(discussionId, content);

	if (!result) return;

	// create live comment
	const liveComment = {
		content,
		user_name: "You",
		created_at: "Just now",
		upvotes: 0,
		downvotes: 0,
	};

	document.querySelector(".post-box").before(createComment(liveComment));

	DOM.postInput.value = "";
}

// -----------------------------------
// SETUP EVENTS
// -----------------------------------
function setupEvents() {
	// post comment
	DOM.postButton.addEventListener("click", handleCommentPost);

	// agree vote
	DOM.agreeBtn.addEventListener("click", async () => {
		await voteDiscussion(discussionId, "agree");

		loadDiscussion();
	});

	// disagree vote
	DOM.disagreeBtn.addEventListener("click", async () => {
		await voteDiscussion(discussionId, "disagree");

		loadDiscussion();
	});
}

// -----------------------------------
// INIT
// -----------------------------------
async function init() {
	await loadComponent("header", "/partials/header.html");

	await loadComponent("footer", "/partials/footer.html");

	setupEvents();

	await loadDiscussion();
}

document.addEventListener("DOMContentLoaded", init);
