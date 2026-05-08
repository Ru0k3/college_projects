"use strict";
import { preventDefaultForSelectors, select } from "../utils/shared.js";
import { postJson, readValue, setStatus } from "../utils/auth.js";

const DEFAULT_ENDPOINT = "/api/login";
const DEFAULT_REDIRECT = "issue_categories.html";

const DOM = { form: null, submit: null, status: null };

function cacheDom() {
  DOM.form = select("#login-form");
  DOM.submit = select("#login-submit");
  DOM.status = select("#login-status");
}

function persistSession(data) {
  if (!data || typeof data !== "object") return;
  if (typeof data.token === "string" && data.token)
    localStorage.setItem("authToken", data.token);
  if (data.user && typeof data.user === "object")
    localStorage.setItem("authUser", JSON.stringify(data.user));
}

async function handleSubmit(event) {
  event.preventDefault();
  if (!DOM.form || !DOM.form.reportValidity()) return;

  const formData = new FormData(DOM.form);
  const email = readValue(formData, "email");
  const password = readValue(formData, "password");
  const remember = formData.get("remember") === "on";

  if (!email || !password) {
    setStatus(DOM.status, "Enter email and password.", true);
    return;
  }

  DOM.submit.disabled = true;
  setStatus(DOM.status, "Signing in...");

  try {
    const result = await postJson(
      DOM.form.dataset.endpoint || DEFAULT_ENDPOINT,
      { email, password, remember }
    );
    persistSession(result);
    setStatus(DOM.status, "Signed in. Redirecting...");
    window.location.assign(result?.redirectTo || DEFAULT_REDIRECT);
  } catch (error) {
    setStatus(DOM.status, error instanceof Error ? error.message : "Sign in failed. Try again.", true);
  } finally {
    DOM.submit.disabled = false;
  }
}

function init() {
  cacheDom();
  if (DOM.form) DOM.form.addEventListener("submit", handleSubmit);
  preventDefaultForSelectors(".forgot-link");
}

document.addEventListener("DOMContentLoaded", init);