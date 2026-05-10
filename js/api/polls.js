"use strict";

import { BASE_URL } from "./config.js";

// -------------------------
// FETCH SINGLE POLL
// Returns:
// {
//   id,
//   question,
//   options: []
// }
// -------------------------
export async function fetchPoll(pollId) {
	try {
		const res = await fetch(`${BASE_URL}/polls/${pollId}`);

		if (!res.ok) {
			throw new Error("Failed to fetch poll");
		}

		return await res.json();
	} catch (err) {
		console.error(err);
		return null;
	}
}

// -------------------------
// SUBMIT POLL VOTE
// -------------------------
export async function submitVote(pollId, optionId) {
	try {
		const res = await fetch(`${BASE_URL}/polls/${pollId}/vote`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify({
				poll_id: pollId,
				option_id: optionId,
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
