import React, { useState } from 'react'
import { TbLayoutSidebarLeftExpandFilled, TbLayoutSidebarLeftCollapseFilled  } from "react-icons/tb";
import { Link } from 'react-router-dom';
import { useSignout } from '../hooks/useSignout';
import { useAuthContext } from '../hooks/useAuthContext';


export default function Navbar() {
  const [iconClicked, setIconClicked] = useState(false);
  const {signout} = useSignout();
  const {user} = useAuthContext();

  const handleIconClick = (e) => {
    e.preventDefault();
    setIconClicked(!iconClicked);
  }
  return (
      <nav className={`fixed ${iconClicked ? 'left-2 bottom-2 top-2 max-w-60' : '-left-96 bottom-2 top-2 w-96 -ml-1'} z-10 w-3/12 min-w-48 bg-gradient-to-b from-teal-300 to-teal-600 rounded-lg shadow-lg text-black transition-all duration-300`}>
        <div className='flex flex-col justify-between h-full'>
          <div className='mb-1 ml-2'>
            <div>
              <Link to='/' onClick={(e) => setIconClicked(false)} className=' font-medium'>Home</Link>
            </div>
            <div>
              <Link to='/gradebook' onClick={(e) => setIconClicked(false)} className=' font-medium'>Gradebook</Link>
            </div>
          </div>
          <div className='mb-1 ml-2'>
            <div className='flex flex-col text-white font-medium'>
              {user 
              ? 
                <button onClick={(e) => {setIconClicked(false); signout()}} className='min-w-[70px] max-w-[70px]'>
                  <p>Sign out</p>
                </button>
              :
              <div className='flex flex-col'>
                <Link to='/signin' onClick={(e) => setIconClicked(false)} className='min-w-[70px] max-w-[70px]'>Sign in</Link>
                <Link to='/signup' onClick={(e) => setIconClicked(false)} className='min-w-[70px] max-w-[70px]'>Sign up</Link>
              </div> 
              }
            </div>
          </div>
        </div>
          {!iconClicked ? <TbLayoutSidebarLeftExpandFilled className='absolute left-full -top-0.5 w-6 h-6 ml-1.5 cursor-pointer hover:opacity-75 text-teal-400' onClick={handleIconClick}/> : <TbLayoutSidebarLeftCollapseFilled className='absolute left-full -top-0.5 w-6 h-6 ml-1.5 cursor-pointer hover:opacity-75 text-teal-400' onClick={handleIconClick}/>}
      </nav>
    
  )
}
