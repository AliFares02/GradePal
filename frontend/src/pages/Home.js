import { Link } from 'react-router-dom'
import { FaPlusCircle } from "react-icons/fa";
import { IoClose } from "react-icons/io5";
import { useEffect, useState } from 'react';
import axios from 'axios';
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

export default function Home() {
  const [displayAddClassForm, setDisplayAddClassForm] = useState(false);
  const [classId, setClassId] = useState('');
  const [className, setClassName] = useState('');
  const [classDescription, setClassDescription] = useState('');
  const [characterCount, setCharacterCount] = useState(0);
  const [addingClassroomError, setAddingClassroomError] = useState('');
  const [classRooms, setClassRooms] = useState([]);

  const {user} = useContext(AuthContext);
  
  const handleSubmitAddClass = (e) => {
    e.preventDefault();
    const apiPostUrl = `http://localhost:8081/api/classroom/${user.user.userId}/add-classroom`;
    axios.post(apiPostUrl, {
      'classId': classId,
      'name': className,
      'description': classDescription,
      'startDate': classStartDate
  }, {
    headers: {
      Authorization: `Bearer ${user.jwt}`
    }
  }).then(response => {
    setAddingClassroomError(false);
    setDisplayAddClassForm(false);
    fetchClassRooms();
  }).catch(error => {
    setAddingClassroomError(error.response?.data.message);
  })
  }
  const fetchClassRooms = () => {
    if (user) {
      const apiGetUrl = `http://localhost:8081/api/classroom/${user.user.userId}`;
      axios.get(apiGetUrl, {
        headers: {
          Authorization: `Bearer ${user.jwt}`
        }
      }).then(response => {
        setClassRooms(response?.data);
      }).catch(error => {
        console.error(error);
      }) 
    } 
  }
  useEffect(() => {
    fetchClassRooms();
  }, []);

  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  const handleDescription = (e) => {
    setClassDescription(e.target.value.trimStart());
    setCharacterCount(e.target.value.length);
  }
    
  const [classStartDate, setClassStartDate] = useState(formatDate(new Date()));
  return (
    <>
    {displayAddClassForm && (
      <main className='fixed top-0 left-0 flex items-center justify-center bg-black bg-opacity-75 w-full h-full z-20 overflow-auto'>
        <form onSubmit={handleSubmitAddClass} className='relative bg-white p-5 rounded-lg h-3/6 w-2/6 min-w-[250px] min-h-[400px]'>

          <input type="text" placeholder='Class name...' required value={className} onChange={(e) => setClassName(e.target.value.trimStart())} className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full'/>

          <input type="text" placeholder='Class ID...' required value={classId} onChange={(e) => setClassId(e.target.value.trimStart())} className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full'/>

          <textarea placeholder='Class description...' required value={classDescription} onChange={handleDescription} className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 h-36 w-full' style={{resize: 'none'}}/>
          <p className={`-mt-2 mb-2 text-xs ${characterCount <= 300 ? 'text-black': 'text-red-500'}`}>{characterCount}/300 {characterCount > 300 ? <span className='text-xs text-red-500'>Cannot exceed character count</span>: ''}</p>
          
          {/* <input
          type="number"
          value={numOfStudents}
          onChange={(e) => setNumOfStudents(e.target.value)}
          placeholder='&#35; of students'
          min="0"
          onInput={(e) => {
            e.target.value = !!e.target.value && Math.abs(e.target.value) >= 0 ? Math.abs(e.target.value) : null;
          }}
          className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full'/> */}

          <label htmlFor="startDate">Class start-date:</label>
          <input type='date' id='startDate' value={classStartDate} onChange={(e) =>  setClassStartDate(formatDate(new Date(e.target.value)))} className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full'/>

          <p className='absolute bottom-4 left-4 text-xs break-words text-red-500 max-w-36'>{addingClassroomError}</p>

          <button type="submit" className=' absolute bottom-2 right-2 h-7 pl-1 pr-1 border-2 rounded-lg border-teal-300 hover:bg-teal-300 transition:hover duration-300' disabled={characterCount > 300}>Add class</button>
        </form>  
        <button className='absolute top-1 right-1 text-red-500 text-3xl' onClick={(e) => setDisplayAddClassForm(false)}><IoClose/></button>
        
      </main>
    )}
    <main className='flex flex-col items-center bg-slate-950 w-lvw h-lvh overflow-auto text-white'>
      <div className='mt-3 flex flex-col items-center gap-2'>
        <h1 className='text-4xl'>Grade<span className=' text-teal-300'>Pal</span></h1>
        <p className='-mb-3 font-semibold'>Home</p>
      </div>
      <div className='flex gap-1 mt-10 flex-wrap border-4 rounded-lg border-teal-300 min-w-[200px] w-8/12 h-full max-h-lvh mb-5 p-1'>
      <div className='flex-grow border-4 rounded-lg border-teal-300 w-full sm:w-auto md:w-auto transition-all duration-300 relative'>
        <div className='w-auto max-w-full'>
          <div className='max-w-full'>
            <h2 className='pl-1 pr-1 border-b-4 border-b-teal-300'>My Classrooms</h2>
          </div>

          <div className='max-w-full m-1 h-full min-w-[400px]'>
            <div className='flex flex-wrap max-w-auto gap-2'>
              {classRooms.map((classroom, index) => {
                return (
                    <Link key={index} to={`class/${classroom.classId}`}
                    state={{ classroom }} className='pl-1 pr-1 border-2 rounded-lg border-teal-300 hover:bg-teal-300 hover:text-black transition:hover duration-300'>
                      {classRooms[index]?.classId}
                    </Link>
                );
              })}
            </div>       
          </div>
          
        </div>
        <button onClick={(e) => setDisplayAddClassForm(true)} className='absolute bottom-2 right-2 text-3xl text-teal-300 transition:hover duration-300 hover:text-white'><FaPlusCircle/></button>
      </div>
        <div className='flex-grow border-4 rounded-lg border-teal-300  w-full sm:w-auto md:w-auto transition-all duration-300'>
          <div className='w-[300px]'>
            Add student
          </div>
        </div>
      </div>
    </main>
    </>
    
  )
}
