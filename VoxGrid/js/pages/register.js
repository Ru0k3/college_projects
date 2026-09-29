"use strict";

import { signupUser } from "../api/auth.js";

const form = document.querySelector("#register-form");
const errorBox = document.querySelector("#signup-error");

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  errorBox.textContent = "";

  const name = form.fullName.value.trim();
  const email = form.email.value.trim();
  const password = form.password.value.trim();
  const confirmPassword = form.confirmPassword.value.trim();

  if (!name || !email || !password || !confirmPassword) {
    errorBox.textContent = "Please fill all fields";
    return;
  }

  if (password.length < 4) {
    errorBox.textContent = "Password must be at least 4 characters";
    return;
  }

  if (password != confirmPassword) {
    errorBox.textContent = "Passwords dont match";
    return;
  }

  try {
    await signupUser(name, email, password);

    alert("Signup successful!");

    window.location.href = "/login.html";
  } catch (err) {
    errorBox.textContent = err.message;
  }
});
