const endpoint = '/api/productos';

async function request(path = '', options = {}) {
  const response = await fetch(`${endpoint}${path}`, {
    ...options,
    headers: {
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
      ...options.headers,
    },
  });

  if (!response.ok) {
    const payload = await response.json().catch(() => null);
    const message = payload
      ? Object.values(payload).join('. ')
      : `No se pudo completar la solicitud (HTTP ${response.status})`;
    throw new Error(message);
  }

  if (response.status === 204) return null;
  return response.json();
}

export const productosApi = {
  listar: () => request(),
  buscar: (id) => request(`/${id}`),
  crear: (producto) => request('', { method: 'POST', body: JSON.stringify(producto) }),
  actualizar: (id, producto) =>
    request(`/${id}`, { method: 'PUT', body: JSON.stringify(producto) }),
  eliminar: (id) => request(`/${id}`, { method: 'DELETE' }),
};
