"use strict";

import { BASE_URL } from "./config.js";

// -----------------------------------
// FETCH ISSUES
// -----------------------------------
export async function fetchIssues() {
	const res = await fetch(`${BASE_URL}/issues`);

	if (!res.ok) {
		throw new Error("Failed to fetch issues");
	}

	return await res.json();
}

// -----------------------------------
// CREATE DISCUSSION
// -----------------------------------
export async function createDiscussion(data) {
	const res = await fetch(`${BASE_URL}/discussions`, {
		method: "POST",

		headers: {
			"Content-Type": "application/json",
		},

		body: JSON.stringify(data),
	});

	if (!res.ok) {
		throw new Error("Failed to create discussion");
	}

	return await res.json();
}

// -----------------------------------
// CREATE BLOG
// -----------------------------------
export async function createBlog(data) {
	const res = await fetch(`${BASE_URL}/blogs`, {
		method: "POST",

		headers: {
			"Content-Type": "application/json",
		},

		body: JSON.stringify(data),
	});

	if (!res.ok) {
		throw new Error("Failed to create blog");
	}

	return await res.json();
}

// -----------------------------------
// CREATE POLL
// -----------------------------------
export async function createPoll(data) {
	const res = await fetch(`${BASE_URL}/polls`, {
		method: "POST",

		headers: {
			"Content-Type": "application/json",
		},

		body: JSON.stringify(data),
	});

	if (!res.ok) {
		throw new Error("Failed to create poll");
	}

	return await res.json();
}
