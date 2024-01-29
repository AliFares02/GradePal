import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { useAuthContext } from "./useAuthContext";

export const useSignin = () => {
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(null);
  const navigate = useNavigate();
  const {dispatch} = useAuthContext();
  const signin = (username, password) => {
    setIsLoading(true);
    setError(null);

    axios.post('http://localhost:8081/auth/login', {
      username,
      password
    }).then(response => {
      console.log(response);
      localStorage.setItem('user', JSON.stringify(response.data));
      dispatch({type: 'SIGNIN', payload: response.data});
      setIsLoading(false);
      navigate('/')
    }).catch(error => {
      if (error.response && error.response.data) {
        setError('Invalid username or password');
        console.log(error.response.data);
      } else {
        setError("Error occurred during sign in");
      }
    }).finally(() => {
      setIsLoading(false);
    })
  }
  return {signin, isLoading, error }
}