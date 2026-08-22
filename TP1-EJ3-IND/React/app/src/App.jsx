import React, { useState, useEffect } from 'react';
import { collection, addDoc, getDocs, deleteDoc, doc, updateDoc } from 'firebase/firestore';
import { db } from './firebase';

function App() {
  const [tasks, setTasks] = useState([]);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [editingId, setEditingId] = useState(null);

  const tasksCollection = collection(db, 'tasks');

  const getTasks = async () => {
    const data = await getDocs(tasksCollection);
    setTasks(data.docs.map((doc) => ({ ...doc.data(), id: doc.id })));
  };

  useEffect(() => { getTasks(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (editingId) {
      await updateDoc(doc(db, 'tasks', editingId), { title, description });
      setEditingId(null);
    } else {
      await addDoc(tasksCollection, { title, description });
    }
    setTitle('');
    setDescription('');
    getTasks();
  };

  const handleEdit = (task) => {
    setEditingId(task.id);
    setTitle(task.title);
    setDescription(task.description);
  };

  const handleDelete = async (id) => {
    await deleteDoc(doc(db, 'tasks', id));
    getTasks();
  };

  return (
    <div className="App">
      <h1>React + Firebase CRUD</h1>
      <form onSubmit={handleSubmit}>
        <input type="text" placeholder="Título" value={title} onChange={(e) => setTitle(e.target.value)} required />
        <textarea placeholder="Descripción" value={description} onChange={(e) => setDescription(e.target.value)} required />
        <button type="submit">{editingId ? 'Actualizar' : 'Agregar'}</button>
      </form>
      <div className="task-list">
        {tasks.map((task) => (
          <div key={task.id} className="task-item">
            <h3>{task.title}</h3>
            <p>{task.description}</p>
            <button onClick={() => handleEdit(task)}>Editar</button>
            <button onClick={() => handleDelete(task.id)} className="delete-btn">Eliminar</button>
          </div>
        ))}
      </div>
    </div>
  );
}
export default App;