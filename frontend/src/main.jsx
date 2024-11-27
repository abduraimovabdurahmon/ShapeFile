import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import { Link, RouterProvider, createBrowserRouter } from 'react-router-dom';
import AddShape from './routes/AddShape.jsx';
import Shapes from './routes/Shapes.jsx';
import Shape from './routes/Shape.jsx';

const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <div className='container'>
        <h1 className='text-center'>Welcome!</h1>
        
        <ul>
          <li>
            <p><Link to="add-shape">/add-shape</Link> Yangi shape qo'shish</p>
          </li>
          <li>
            <p><Link to="shapes">/shapes</Link> Shape'lar</p>
          </li>
        </ul>

      </div>
    ),
  },
  {
    path: "add-shape",
    element: <AddShape />,
  },
  {
    path: "shapes",
    element: <Shapes />,
  },
  {
    path: "shape/:id",
    element: <Shape />,
  
  }
]);



ReactDOM.createRoot(document.getElementById('root')).render(
  <RouterProvider router={router} />
)
