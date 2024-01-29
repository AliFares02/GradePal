import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export const useSignup = () => {
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(null);
  const navigate = useNavigate();
  const signup = (username, password) => {
    setIsLoading(true);
    setError(null);

    axios.post('http://localhost:8081/auth/register', {
      username,
      password
    }).then(response => {
      console.log(response);
      setIsLoading(false);
      navigate('/signin')
    }).catch(error => {
      if (error.response && error.response.data) {
        setError(error.response.data);
        console.log(error.response.data);
      } else {
        setError("Error occurred during registration");
      }
    }).finally(() => {
      setIsLoading(false);
    })
  }
  return {signup, isLoading, error }
}
