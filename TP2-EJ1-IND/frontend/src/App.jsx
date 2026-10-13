import { useCallback, useEffect, useState } from 'react';
import { productosApi } from './api.js';

const formularioVacio = { nombre: '', precio: '' };

export default function App() {
  const [productos, setProductos] = useState([]);
  const [formulario, setFormulario] = useState(formularioVacio);
  const [productoEditando, setProductoEditando] = useState(null);
  const [productoDetalle, setProductoDetalle] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [guardando, setGuardando] = useState(false);
  const [error, setError] = useState('');
  const [mensaje, setMensaje] = useState('');

  const cargarProductos = useCallback(async () => {
    setCargando(true);
    setError('');
    try {
      setProductos(await productosApi.listar());
    } catch (exception) {
      setError(exception.message);
    } finally {
      setCargando(false);
    }
  }, []);

  useEffect(() => {
    cargarProductos();
  }, [cargarProductos]);

  function cambiarCampo(event) {
    const { name, value } = event.target;
    setFormulario((actual) => ({ ...actual, [name]: value }));
  }

  function cancelarEdicion() {
    setProductoEditando(null);
    setFormulario(formularioVacio);
    setMensaje('');
  }

  async function guardarProducto(event) {
    event.preventDefault();
    setGuardando(true);
    setError('');
    setMensaje('');

    try {
      const datosProducto = { ...formulario, precio: Number(formulario.precio) };
      const mensajeExito = productoEditando
        ? 'Los datos del producto se actualizaron.'
        : 'El producto se agregó correctamente.';
      if (productoEditando) {
        await productosApi.actualizar(productoEditando.id, datosProducto);
      } else {
        await productosApi.crear(datosProducto);
      }
      cancelarEdicion();
      setMensaje(mensajeExito);
      await cargarProductos();
    } catch (exception) {
      setError(exception.message);
    } finally {
      setGuardando(false);
    }
  }

  function editarProducto(producto) {
    setProductoEditando(producto);
    setFormulario({ nombre: producto.nombre, precio: String(producto.precio) });
    setMensaje('');
    setError('');
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  async function mostrarDetalle(id) {
    setError('');
    try {
      setProductoDetalle(await productosApi.buscar(id));
    } catch (exception) {
      setError(exception.message);
    }
  }

  async function eliminarProducto(producto) {
    if (!window.confirm(`¿Querés eliminar "${producto.nombre}"?`)) return;

    setError('');
    setMensaje('');
    try {
      await productosApi.eliminar(producto.id);
      setMensaje('El producto se eliminó correctamente.');
      if (productoDetalle?.id === producto.id) setProductoDetalle(null);
      await cargarProductos();
    } catch (exception) {
      setError(exception.message);
    }
  }

  return (
    <main className="page-shell">
      <header className="page-header">
        <div className="brand-mark" aria-hidden="true">P</div>
        <div>
          <p className="eyebrow">INVENTARIO</p>
          <h1>Gestión de productos</h1>
          <p className="subtitle">Administrá tu catálogo de productos y precios.</p>
        </div>
      </header>

      <section className="content-grid">
        <section className="panel form-panel" aria-labelledby="form-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">{productoEditando ? 'MODIFICAR PRODUCTO' : 'NUEVO PRODUCTO'}</p>
              <h2 id="form-title">{productoEditando ? 'Editar producto' : 'Agregar producto'}</h2>
            </div>
            <span className="heading-icon" aria-hidden="true">{productoEditando ? '✎' : '+'}</span>
          </div>

          <form onSubmit={guardarProducto}>
            <label htmlFor="nombre">Nombre del producto</label>
            <input
              id="nombre"
              name="nombre"
              type="text"
              value={formulario.nombre}
              onChange={cambiarCampo}
              placeholder="Ej.: Teclado mecánico"
              maxLength="100"
              required
            />

            <label htmlFor="precio">Precio</label>
            <input
              id="precio"
              name="precio"
              type="number"
              min="0.01"
              step="0.01"
              value={formulario.precio}
              onChange={cambiarCampo}
              placeholder="Ej.: 12500.00"
              required
            />

            <div className="form-actions">
              <button className="button button-primary" type="submit" disabled={guardando}>
                {guardando ? 'Guardando…' : productoEditando ? 'Guardar cambios' : 'Agregar producto'}
              </button>
              {productoEditando && (
                <button className="button button-quiet" type="button" onClick={cancelarEdicion}>
                  Cancelar
                </button>
              )}
            </div>
          </form>
          <p className="form-hint">El nombre y un precio mayor a cero son obligatorios.</p>
        </section>

        <section className="panel list-panel" aria-labelledby="list-title">
          <div className="panel-heading list-heading">
            <div>
              <p className="eyebrow">CATÁLOGO</p>
              <h2 id="list-title">Listado de productos</h2>
            </div>
            <span className="count-badge">{productos.length}</span>
          </div>

          {error && <div className="notice notice-error" role="alert">{error}</div>}
          {mensaje && <div className="notice notice-success" role="status">{mensaje}</div>}

          {cargando ? (
            <p className="empty-state">Cargando productos…</p>
          ) : productos.length === 0 ? (
            <div className="empty-state">
              <span className="empty-icon" aria-hidden="true">◎</span>
              <strong>Aún no hay productos</strong>
              <span>Agregá el primero usando el formulario.</span>
            </div>
          ) : (
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Nombre</th>
                    <th>Precio</th>
                    <th className="actions-heading">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {productos.map((producto) => (
                    <tr key={producto.id}>
                      <td>
                        <div className="product-name">
                          <span className="avatar" aria-hidden="true">
                            {producto.nombre.trim().charAt(0).toUpperCase()}
                          </span>
                          <span>{producto.nombre}</span>
                        </div>
                      </td>
                      <td><span className="role-pill">{Number(producto.precio).toLocaleString('es-AR', { style: 'currency', currency: 'ARS' })}</span></td>
                      <td>
                        <div className="row-actions">
                          <button className="icon-button" type="button" onClick={() => mostrarDetalle(producto.id)} aria-label={`Ver ${producto.nombre}`} title="Ver">Ver</button>
                          <button className="icon-button" type="button" onClick={() => editarProducto(producto)} aria-label={`Editar ${producto.nombre}`} title="Editar">Editar</button>
                          <button className="icon-button icon-danger" type="button" onClick={() => eliminarProducto(producto)} aria-label={`Eliminar ${producto.nombre}`} title="Eliminar">Eliminar</button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </section>

      {productoDetalle && (
        <div className="modal-backdrop" role="presentation" onClick={() => setProductoDetalle(null)}>
          <section
            className="detail-card"
            role="dialog"
            aria-modal="true"
            aria-labelledby="detail-title"
            onClick={(event) => event.stopPropagation()}
          >
            <button className="modal-close" type="button" onClick={() => setProductoDetalle(null)} aria-label="Cerrar">×</button>
            <p className="eyebrow">FICHA DE PRODUCTO</p>
            <h2 id="detail-title">{productoDetalle.nombre}</h2>
            <p className="detail-role">{Number(productoDetalle.precio).toLocaleString('es-AR', { style: 'currency', currency: 'ARS' })}</p>
            <p className="detail-id">Identificador #{productoDetalle.id}</p>
            <button className="button button-primary" type="button" onClick={() => setProductoDetalle(null)}>Cerrar</button>
          </section>
        </div>
      )}
    </main>
  );
}
