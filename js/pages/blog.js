"use strict";
import { loadComponent } from "../utils/load.js";

document.addEventListener("DOMContentLoaded", async () => {
  await Promise.all([
    loadComponent("header", "partials/header.html"),
    loadComponent("footer", "partials/footer.html"),
  ]);
});
