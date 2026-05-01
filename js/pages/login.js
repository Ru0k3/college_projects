"use strict";

import { preventDefaultForSelectors, select } from "../utils/shared.js";

const DEFAULT_ENDPOINT = "/api/login";
const DEFAULT_REDIRECT = "issue_categories.html";

const DOM = {
	form: null,
	submit: null,
	status: null,
};

function cacheDom() {
	DOM.form = select("#login-form");
	DOM.submit = select("#login-submit");
	DOM.status = select("#login-status");
}

function setStatus(message, isError = false) {
	if (!DOM.status) {
		return;
	}

	DOM.status.textContent = message;
	DOM.status.classList.toggle("error", isError);
}

function readValue(formData, key) {
	return String(formData.get(key) ?? "").trim();
}

function getMessage(payload, fallback) {
	if (!payload || typeof payload !== "object") {
		return fallback;
	}

	const message =
		payload.message ?? payload.error ?? payload.detail ?? payload.title;

	if (typeof message !== "string" || !message.trim()) {
		return fallback;
	}

	return message;
}

async function postJson(url, payload) {
	let response;

	try {
		response = await fetch(url, {
			method: "POST",
			headers: {
				Accept: "application/json",
				"Content-Type": "application/json",
			},
			credentials: "include",
			body: JSON.stringify(payload),
		});
	} catch {
		throw new Error("Network error. Check your connection and try again.");
	}

	const contentType = response.headers.get("content-type") ?? "";
	let body = null;

	if (contentType.includes("application/json")) {
		try {
			body = await response.json();
		} catch {
			body = null;
		}
	} else {
		body = await response.text();
	}

	const data = typeof body === "string" ? { message: body } : body;

	if (!response.ok) {
		throw new Error(getMessage(data, "Unable to sign in."));
	}

	return data;
}

function persistSession(data) {
	if (!data || typeof data !== "object") {
		return;
	}

	if (typeof data.token === "string" && data.token) {
		localStorage.setItem("authToken", data.token);
	}

	if (data.user && typeof data.user === "object") {
		localStorage.setItem("authUser", JSON.stringify(data.user));
	}
}

async function handleSubmit(event) {
	event.preventDefault();

	if (!DOM.form) {
		return;
	}

	if (!DOM.form.reportValidity()) {
		return;
	}

	const formData = new FormData(DOM.form);
	const email = readValue(formData, "email");
	const password = readValue(formData, "password");
	const remember = formData.get("remember") === "on";
	const endpoint = DOM.form.dataset.endpoint || DEFAULT_ENDPOINT;
	const fallbackRedirect = DOM.form.dataset.redirect || DEFAULT_REDIRECT;

	if (!email || !password) {
		setStatus("Enter email and password.", true);
		return;
	}

	DOM.submit.disabled = true;
	setStatus("Signing in...");

	try {
		const result = await postJson(endpoint, { email, password, remember });
		persistSession(result);
		const redirectTo =
			typeof result?.redirectTo === "string" && result.redirectTo
				? result.redirectTo
				: fallbackRedirect;
		setStatus("Signed in. Redirecting...");
		window.location.assign(redirectTo);
	} catch (error) {
		const message =
			error instanceof Error ? error.message : "Sign in failed. Try again.";
		setStatus(message, true);
	} finally {
		DOM.submit.disabled = false;
	}
}

function init() {
	cacheDom();

	if (DOM.form) {
		DOM.form.addEventListener("submit", handleSubmit);
	}

	preventDefaultForSelectors(".forgot-link");
}

document.addEventListener("DOMContentLoaded", init);
