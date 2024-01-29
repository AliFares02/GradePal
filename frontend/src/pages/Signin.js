import { useState } from "react"
import { AuthContext } from "../context/AuthContext";
import { useSignin } from "../hooks/useSignin";

const Signin = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const {signin, isLoading, error} = useSignin();

  const handleSubmit = (e) => {
    e.preventDefault();
    signin(username, password);
  }

  return (
    <div className="h-lvh w-lvw bg-slate-950 flex items-center justify-center">
      <form className="flex flex-col border relative bg-white p-5 rounded-lg h-auto w-2/6 min-w-[250px] max-w-[400px] min-h-[350px] gap-2" onSubmit={handleSubmit}>
        <h3>Sign in</h3>
        <label>Username:</label>
        <input type="text" onChange={(e) => setUsername(e.target.value.trimStart())} value={username} required className="border-2 rounded-lg border-teal-300 outline-none p-1 pl-2"/>

        <label>Password:</label>
        <input type="password" onChange={(e) => setPassword(e.target.value.trimStart())} value={password} required className="border-2 rounded-lg border-teal-300 outline-none p-1 pl-2"/>

        {error && <p className="text-sm text-red-600">{error}</p>}
        
        <button type="submit" className=' absolute bottom-2 right-2 h-7 pl-1 pr-1 border-2 rounded-lg border-teal-300 hover:bg-teal-300 hover:text-white transition:hover duration-300' disabled={isLoading}>Sign in</button>
      </form>
    </div>
  )
}

export default Signin;