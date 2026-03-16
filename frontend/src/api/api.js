const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export async function createUser(data) {
  const res = await fetch(`${BASE_URL}/api/users`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getUser(id) {
  const res = await fetch(`${BASE_URL}/api/users/${id}`);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function uploadFile(file) {
  const formData = new FormData();
  formData.append("file", file);
  const res = await fetch(`${BASE_URL}/api/files/upload`, {
    method: "POST",
    body: formData,
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function downloadFile(id) {
  const res = await fetch(`${BASE_URL}/api/files/download/${id}`);
  if (!res.ok) throw new Error(await res.text());
  return res;
}
