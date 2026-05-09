import { BASE_URL } from "./config.js";

// ----------------------------
// Fetch discussions for an issue
// ----------------------------
export async function fetchDiscussions(issueId) {
	try {
		const res = await fetch(`${BASE_URL}/discussions?issue_id=${issueId}`);
		if (!res.ok) throw new Error("Failed to fetch discussions");
		const data = await res.json();
		return Array.isArray(data) ? data : [];
	} catch (err) {
		console.error(err);
		return [];
	}
}

// ----------------------------
// Fetch blogs for an issue
// ----------------------------
export async function fetchBlogs(issueId) {
	try {
		const res = await fetch(`${BASE_URL}/blogs?issue_id=${issueId}`);
		if (!res.ok) throw new Error("Failed to fetch blogs");
		const data = await res.json();
		return Array.isArray(data) ? data : [];
	} catch (err) {
		console.error(err);
		return [];
	}
}

// ----------------------------
// Fetch polls for an issue
// ----------------------------
export async function fetchPolls(issueId) {
	try {
		const res = await fetch(`${BASE_URL}/polls?issue_id=${issueId}`);
		if (!res.ok) throw new Error("Failed to fetch polls");
		const data = await res.json();
		console.log("Haii");
		return Array.isArray(data) ? data : [data];
	} catch (err) {
		console.error(err);
		return [];
	}
}

// ----------------------------
// Optional: Fetch poll options for a poll
// ----------------------------
export async function fetchPollOptions(pollId) {
	try {
		const res = await fetch(`${BASE_URL}/poll_options?poll_id=${pollId}`);
		if (!res.ok) throw new Error("Failed to fetch poll options");
		const data = await res.json();
		return Array.isArray(data) ? data : [];
	} catch (err) {
		console.error(err);
		return [];
	}
}
