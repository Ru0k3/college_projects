"use strict";
import { preventDefaultForSelectors, select } from "../utils/shared.js";
import { postJson, readValue, setStatus } from "../utils/auth.js";

import { loginUser } from "../api/auth.js";
import { saveUser } from "../states/authState.js";

const form = document.querySelector("#login-form");
const errorBox = document.querySelector("#login-error");

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  errorBox.textContent = "";

  const email = form.email.value.trim();
  const password = form.password.value.trim();

  if (!email || !password) {
    errorBox.textContent = "Please fill all fields";
    return;
  }

  try {
    const data = await loginUser(email, password);
    saveUser(data.user);
    window.location.href = "/index.html";

  } catch (err) {
    errorBox.textContent = err.message;
  }
});