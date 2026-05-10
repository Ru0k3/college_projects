"use strict";

import { BASE_URL } from "./config.js";

// -----------------------------------
// FETCH SINGLE BLOG
// -----------------------------------
export async function fetchBlog(blogId) {
	try {
		const res = await fetch(`${BASE_URL}/blogs/${blogId}`);

		if (!res.ok) {
			throw new Error("Failed to fetch blog");
		}

		return await res.json();
	} catch (err) {
		console.error(err);
		return null;
	}
}
