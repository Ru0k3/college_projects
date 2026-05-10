"use strict";

import { BASE_URL } from "./config.js";

// -----------------------------------
// FETCH SINGLE DISCUSSION
// -----------------------------------
export async function fetchDiscussion(discussionId) {
	try {
		const res = await fetch(`${BASE_URL}/discussions/${discussionId}`);

		if (!res.ok) {
			throw new Error("Failed to fetch discussion");
		}

		return await res.json();
	} catch (err) {
		console.error(err);
		return null;
	}
}

// -----------------------------------
// FETCH COMMENTS
// -----------------------------------
export async function fetchComments(discussionId) {
	try {
		const res = await fetch(
			`${BASE_URL}/comments?discussion_id=${discussionId}`,
		);

		if (!res.ok) {
			throw new Error("Failed to fetch comments");
		}

		return await res.json();
	} catch (err) {
		console.error(err);
		return [];
	}
}

// -----------------------------------
// ADD COMMENT
// -----------------------------------
export async function addComment(discussionId, content) {
	try {
		const res = await fetch(`${BASE_URL}/comments`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},

			body: JSON.stringify({
				discussion_id: discussionId,
				user_id: 1, // temp demo user
				content,
			}),
		});

		if (!res.ok) {
			throw new Error("Failed to add comment");
		}

		return await res.json();
	} catch (err) {
		console.error(err);
		return null;
	}
}

// -----------------------------------
// ADD DISCUSSION VOTE
// agree / disagree
// -----------------------------------
export async function voteDiscussion(discussionId, voteType) {
	try {
		const res = await fetch(`${BASE_URL}/votes`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},

			body: JSON.stringify({
				target_type: "discussion",
				target_id: discussionId,
				vote_type: voteType,
			}),
		});

		if (!res.ok) {
			throw new Error("Vote failed");
		}

		return await res.json();
	} catch (err) {
		console.error(err);
		return null;
	}
}
