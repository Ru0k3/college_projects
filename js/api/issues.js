import { BASE_URL } from "./config.js";

export async function fetchIssues(categoryId = null) {
	console.log(categoryId);
	try {
		let url = `${BASE_URL}/issues`;
		if (categoryId) {
			url += `?category=${categoryId}`;
			console.log(url);
		}

		const res = await fetch(url);

		if (!res.ok) {
			throw new Error("Failed to fetch issues");
		}

		const data = await res.json();
		return data; // Array of issues
	} catch (err) {
		console.error(err);
		throw err;
	}
}
