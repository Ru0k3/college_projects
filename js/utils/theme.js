const STORAGE_KEY = "voxgrid-theme";
const THEMES = ["light", "dark"];

function getSystemTheme() {
  return window.matchMedia?.("(prefers-color-scheme: dark)").matches
    ? "dark"
    : "light";
}

function getStoredTheme() {
  const savedTheme = localStorage.getItem(STORAGE_KEY);
  return THEMES.includes(savedTheme) ? savedTheme : null;
}

function applyTheme(theme) {
  document.documentElement.dataset.theme = theme;
}

function getCurrentTheme() {
  return document.documentElement.dataset.theme === "dark" ? "dark" : "light";
}

export function initTheme() {
  applyTheme(getStoredTheme() || getSystemTheme());
  updateToggleUI();
}

export function toggleTheme() {
  const nextTheme = getCurrentTheme() === "dark" ? "light" : "dark";
  applyTheme(nextTheme);
  localStorage.setItem(STORAGE_KEY, nextTheme);
  updateToggleUI();
}

export function bindToggle(selector = ".theme-toggle") {
  const toggle = document.querySelector(selector);
  if (!toggle || toggle.dataset.themeBound === "true") {
    return;
  }

  toggle.dataset.themeBound = "true";
  toggle.addEventListener("click", toggleTheme);
  updateToggleUI();
}

export function updateToggleUI() {
  const theme = getCurrentTheme();
  const toggle = document.querySelector(".theme-toggle");
  if (!toggle) {
    return;
  }

  const icon = toggle.querySelector(".theme-toggle__icon");
  const isDark = theme === "dark";

  toggle.setAttribute("aria-pressed", String(isDark));
  toggle.setAttribute("aria-label", isDark ? "Switch to light theme" : "Switch to dark theme");

  if (icon) {
    icon.textContent = isDark ? "🌙" : "☀️";
  }
}
