import { AuthContext } from "../context/AuthContext";
import { useContext } from "react";

export const useAuthContext = () => {
  const context = useContext(AuthContext);
  const {user} = useContext(AuthContext);
  console.log(user?.jwt);
  if (!context) {
    throw Error('useAuthContext must be used inside an AuthContextProvider')
  }

  return context;
}