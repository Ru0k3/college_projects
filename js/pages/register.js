"use strict";
import { preventDefaultForSelectors, select } from "../utils/shared.js";
import { postJson, readValue, setStatus } from "../utils/auth.js";

const DEFAULT_ENDPOINT = "/api/register";
const DEFAULT_REDIRECT = "login.html";

const DOM = { form: null, submit: null, status: null };

function cacheDom() {
  DOM.form = select("#register-form");
  DOM.submit = select("#register-submit");
  DOM.status = select("#register-status");
}

function validate(formData) {
  const fullName = readValue(formData, "fullName");
  const email = readValue(formData, "email");
  const password = readValue(formData, "password");
  const confirmPassword = readValue(formData, "confirmPassword");

  if (!fullName || !email || !password || !confirmPassword)
    return { ok: false, message: "Complete all required fields." };
  if (password.length < 8)
    return { ok: false, message: "Password must be at least 8 characters." };
  if (password !== confirmPassword)
    return { ok: false, message: "Passwords do not match." };
  if (formData.get("terms") !== "on")
    return { ok: false, message: "Accept Terms and Privacy Policy to continue." };

  return { ok: true, fullName, email, password };
}

async function handleSubmit(event) {
  event.preventDefault();
  if (!DOM.form || !DOM.form.reportValidity()) return;

  const formData = new FormData(DOM.form);
  const validation = validate(formData);

  if (!validation.ok) {
    setStatus(DOM.status, validation.message, true);
    return;
  }

  DOM.submit.disabled = true;
  setStatus(DOM.status, "Creating account...");

  try {
    const result = await postJson(
      DOM.form.dataset.endpoint || DEFAULT_ENDPOINT,
      { fullName: validation.fullName, email: validation.email, password: validation.password }
    );
    setStatus(DOM.status, "Account created. Redirecting...");
    window.location.assign(result?.redirectTo || DEFAULT_REDIRECT);
  } catch (error) {
    setStatus(DOM.status, error instanceof Error ? error.message : "Registration failed. Try again.", true);
  } finally {
    DOM.submit.disabled = false;
  }
}

function init() {
  cacheDom();
  if (DOM.form) DOM.form.addEventListener("submit", handleSubmit);
  preventDefaultForSelectors(".terms a");
}

document.addEventListener("DOMContentLoaded", init);