"use strict";

export function select(selector, parent = document) {
  return parent.querySelector(selector);
}

export function selectAll(selector, parent = document) {
  return Array.from(parent.querySelectorAll(selector));
}

export function preventDefaultForSelectors(selectors, parent = document) {
  selectAll(selectors, parent).forEach((element) => {
    element.addEventListener("click", (event) => {
      event.preventDefault();
    });
  });
}

export function setupActiveClassToggle(
  selector,
  activeClass,
  { parent = document, preventDefault = true, onChange = null } = {}
) {
  const elements = selectAll(selector, parent);

  elements.forEach((element) => {
    element.addEventListener("click", (event) => {
      if (preventDefault) {
        event.preventDefault();
      }

      elements.forEach((item) => {
        item.classList.remove(activeClass);
      });

      element.classList.add(activeClass);

      if (typeof onChange === "function") {
        onChange(element, elements);
      }
    });
  });

  return elements;
}

export function normalizeQuery(value = "") {
  return value.trim().toLowerCase();
}

export function navigateTo(href) {
  window.location.href = href;
}

export function toDisplayLabel(value = "") {
  if (!value) {
    return "";
  }

  return value.charAt(0).toUpperCase() + value.slice(1);
}

export function createMessageElement({
  tagName = "p",
  className = "",
  text = "",
  html = "",
} = {}) {
  const element = document.createElement(tagName);

  if (className) {
    element.className = className;
  }

  if (html) {
    element.innerHTML = html;
  } else {
    element.textContent = text;
  }

  return element;
}

export function renderList(container, items, renderItem, emptyState = null) {
  if (!container) {
    return;
  }

  container.replaceChildren();

  if (items.length === 0) {
    if (emptyState) {
      container.appendChild(createMessageElement(emptyState));
    }

    return;
  }

  const fragment = document.createDocumentFragment();

  items.forEach((item) => {
    fragment.appendChild(renderItem(item));
  });

  container.appendChild(fragment);
}

export function ensureStateBlock({
  container,
  selector,
  tagName = "div",
  className = "",
  text = "",
  html = "",
  before = null,
} = {}) {
  if (!container) {
    return null;
  }

  const existing = select(selector, container);
  if (existing) {
    return existing;
  }

  const stateBlock = createMessageElement({ tagName, className, text, html });
  container.insertBefore(stateBlock, before);
  return stateBlock;
}

export function removeBySelector(selector, parent = document) {
  const element = select(selector, parent);

  if (element) {
    element.remove();
  }

  return element;
}
