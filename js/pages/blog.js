"use strict";

import { fetchBlog } from "../api/blogs.js";
import { loadComponent } from "../utils/load.js";

// marked.js CDN import
import { marked } from "https://cdn.jsdelivr.net/npm/marked/lib/marked.esm.js";

// -----------------------------------
// DOM
// -----------------------------------
const DOM = {
	meta: document.querySelector(".meta"),

	title: document.querySelector("h1"),

	authorName: document.querySelector(".author strong"),

	authorRole: document.querySelector(".author p"),

	content: document.querySelector(".content"),
};

// -----------------------------------
// URL PARAMS
// blog.html?id=1
// -----------------------------------
const params = new URLSearchParams(window.location.search);

const blogId = params.get("id");

// -----------------------------------
// FORMAT DATE
// -----------------------------------
function formatDate(dateString) {
	const date = new Date(dateString);

	return date.toLocaleDateString("en-US", {
		month: "short",
		day: "numeric",
		year: "numeric",
	});
}

// -----------------------------------
// LOAD BLOG
// -----------------------------------
async function loadBlog() {
	if (!blogId) return;

	try {
		const blog = await fetchBlog(blogId);

		if (!blog) return;

		// -------------------------
		// META
		// -------------------------
		DOM.meta.innerHTML = `
      <span>
        ${blog.category_name || "Policy"}
      </span>

      <span>
        &bull;
        ${formatDate(blog.created_at)}
      </span>

      <span>
        &bull;
        ${blog.read_time || "5 min read"}
      </span>
    `;

		// -------------------------
		// TITLE
		// -------------------------
		DOM.title.textContent = blog.title;

		// -------------------------
		// AUTHOR
		// -------------------------
		DOM.authorName.textContent = blog.author_name || "Anonymous";

		DOM.authorRole.textContent = blog.author_role || "Contributor";

		// -------------------------
		// MARKDOWN CONTENT
		// -------------------------
		DOM.content.innerHTML = marked.parse(blog.content);
	} catch (err) {
		console.error("Failed to load blog:", err);
	}
}

// -----------------------------------
// INIT
// -----------------------------------
async function init() {
	await loadComponent("header", "/partials/header.html");

	await loadComponent("footer", "/partials/footer.html");

	await loadBlog();
}

document.addEventListener("DOMContentLoaded", init);
