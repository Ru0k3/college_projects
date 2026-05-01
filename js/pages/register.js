"use strict";

import { preventDefaultForSelectors, select } from "../utils/shared.js";

const DEFAULT_ENDPOINT = "/api/register";
const DEFAULT_REDIRECT = "login.html";

const DOM = {
	form: null,
	submit: null,
	status: null,
};

function cacheDom() {
	DOM.form = select("#register-form");
	DOM.submit = select("#register-submit");
	DOM.status = select("#register-status");
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
		throw new Error(getMessage(data, "Unable to create account."));
	}

	return data;
}

function validate(formData) {
	const fullName = readValue(formData, "fullName");
	const email = readValue(formData, "email");
	const password = readValue(formData, "password");
	const confirmPassword = readValue(formData, "confirmPassword");
	const termsAccepted = formData.get("terms") === "on";

	if (!fullName || !email || !password || !confirmPassword) {
		return { ok: false, message: "Complete all required fields." };
	}

	if (password.length < 8) {
		return { ok: false, message: "Password must be at least 8 characters." };
	}

	if (password !== confirmPassword) {
		return { ok: false, message: "Passwords do not match." };
	}

	if (!termsAccepted) {
		return { ok: false, message: "Accept Terms and Privacy Policy to continue." };
	}

	return { ok: true, fullName, email, password };
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
	const validation = validate(formData);

	if (!validation.ok) {
		setStatus(validation.message, true);
		return;
	}

	const endpoint = DOM.form.dataset.endpoint || DEFAULT_ENDPOINT;
	const fallbackRedirect = DOM.form.dataset.redirect || DEFAULT_REDIRECT;

	DOM.submit.disabled = true;
	setStatus("Creating account...");

	try {
		const result = await postJson(endpoint, {
			fullName: validation.fullName,
			email: validation.email,
			password: validation.password,
		});
		const redirectTo =
			typeof result?.redirectTo === "string" && result.redirectTo
				? result.redirectTo
				: fallbackRedirect;
		setStatus("Account created. Redirecting...");
		window.location.assign(redirectTo);
	} catch (error) {
		const message =
			error instanceof Error ? error.message : "Registration failed. Try again.";
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

	preventDefaultForSelectors(".terms a");
}

document.addEventListener("DOMContentLoaded", init);
