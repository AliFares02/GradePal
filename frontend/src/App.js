import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./pages/Home";
import Gradebook from "./pages/Gradebook";
import Navbar from "./components/Navbar";
import Signin from "./pages/Signin";
import Signup from "./pages/Signup";
import Class from "./pages/Class";
import { useAuthContext } from "./hooks/useAuthContext";
import { Navigate } from "react-router-dom";


function App() {
  const {user} = useAuthContext();

  return (
    <BrowserRouter>
      <Navbar/>
      <Routes>
        <Route path="/" element={user ? <Home/> : <Navigate to='/signin'/>}/>
        <Route path="/gradebook" element={<Gradebook/>}/>
        <Route path="/signin" element={!user ? <Signin/> : <Navigate to='/'/>}/>
        <Route path="/class/:classId" element={user ? <Class/> : <Navigate to='/signin'/>}/>
        <Route path="/signup" element={!user ? <Signup/> : <Navigate to='/'/>}/>
      </Routes>
    </BrowserRouter>
    
    
  )
}

export default App;


