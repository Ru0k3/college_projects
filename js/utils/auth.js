"use strict";

export function setStatus(statusEl, message, isError = false) {
  if (!statusEl) return;
  statusEl.textContent = message;
  statusEl.classList.toggle("error", isError);
}

export function readValue(formData, key) {
  return String(formData.get(key) ?? "").trim();
}

export function getMessage(payload, fallback) {
  if (!payload || typeof payload !== "object") return fallback;
  const message = payload.message ?? payload.error ?? payload.detail ?? payload.title;
  if (typeof message !== "string" || !message.trim()) return fallback;
  return message;
}

export async function postJson(url, payload) {
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
    try { body = await response.json(); } catch { body = null; }
  } else {
    body = await response.text();
  }

  const data = typeof body === "string" ? { message: body } : body;
  if (!response.ok) throw new Error(getMessage(data, "Request failed."));
  return data;
}