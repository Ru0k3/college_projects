export async function loadComponent(id, file) {
  const mountPoint = document.getElementById(id);
  if (!mountPoint) {
    return;
  }

  const res = await fetch(file);
  const html = await res.text();
  mountPoint.innerHTML = html;
}
