import { BASE_URL } from "./config.js";

export async function fetchCategories() {
	try {
		const response = await fetch(`${BASE_URL}/categories`);

		if (!response.ok) {
			throw new Error("Failed to fetch categories");
		}

		const data = await response.json();

		return data;
	} catch (err) {
		console.error(err);
		throw err;
	}
}
